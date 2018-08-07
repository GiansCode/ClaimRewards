package me.itsmas.claimrewards.data.sql;

import me.itsmas.claimrewards.ClaimRewards;
import me.itsmas.claimrewards.data.DataStorage;
import me.itsmas.claimrewards.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.*;
import java.util.function.Consumer;

public class SqlStorage implements DataStorage
{
    private final ClaimRewards plugin;

    public SqlStorage(ClaimRewards plugin)
    {
        this.plugin = plugin;

        try
        {
            connect();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            Util.logFatal("Unable to connect to MySQL database");

            Bukkit.getPluginManager().disablePlugin(plugin);
        }
    }

    private Connection connection;

    @Override
    public void connect() throws Exception
    {
        String host = plugin.configGet("sql.host");
        int port = plugin.configGet("sql.port");
        String database = plugin.configGet("sql.database");
        String username = plugin.configGet("sql.username");
        String password = plugin.configGet("sql.password");

        assert connection == null || connection.isClosed();

        synchronized (this)
        {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);

            createTables();
        }
    }

    @Override
    public void closeConnection()
    {
        try
        {
            if (connection != null && !connection.isClosed())
            {
                connection.close();
            }
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();

            Util.logErr("Error closing MySQL connection");
        }
    }

    private void createTables()
    {
        try
        {
            connection.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS Rewards(uuid VARCHAR(36), rewards TEXT);");
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
    }

    /* Queries and Updates */
    private final String PLAYER_QUERY = "SELECT rewards FROM Rewards WHERE uuid=?";

    @Override
    public void getData(Player player, Consumer<String> consumer)
    {
        runAsync(() ->
        {
            try
            {
                PreparedStatement statement = connection.prepareStatement(PLAYER_QUERY);
                statement.setString(1, player.getUniqueId().toString());

                ResultSet results = statement.executeQuery();

                if (!results.next())
                {
                    runSync(() -> consumer.accept(""));
                }
                else
                {
                    String raw = results.getString("rewards");

                    runSync(() -> consumer.accept(raw));
                }
            }
            catch (SQLException ex)
            {
                ex.printStackTrace();
            }
        });
    }

    private final String SAVE_DATA = "INSERT INTO Rewards (uuid, rewards) VALUES (?,?) ON DUPLICATE KEY UPDATE rewards=?;";

    @Override
    public void saveData(Player player)
    {
        String serialized = plugin.getRewardManager().getRewardData(player).serialize();

        runAsync(() ->
        {
            try
            {
                PreparedStatement statement = connection.prepareStatement(SAVE_DATA);

                statement.setString(1, player.getUniqueId().toString());
                statement.setString(2, serialized);
                statement.setString(3, serialized);

                statement.execute();
            }
            catch (SQLException ex)
            {
                ex.printStackTrace();
            }
        });
    }

    private void runSync(Runnable runnable)
    {
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                runnable.run();
            }
        }.runTask(plugin);
    }

    private void runAsync(Runnable runnable)
    {
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                runnable.run();
            }
        }.runTaskAsynchronously(plugin);
    }
}
