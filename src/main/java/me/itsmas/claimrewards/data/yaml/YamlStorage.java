package me.itsmas.claimrewards.data.yaml;

import me.itsmas.claimrewards.ClaimRewards;
import me.itsmas.claimrewards.data.DataStorage;
import me.itsmas.claimrewards.util.Util;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;

public class YamlStorage implements DataStorage
{
    private final ClaimRewards plugin;

    public YamlStorage(ClaimRewards plugin)
    {
        this.plugin = plugin;

        try
        {
            connect();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            Util.logFatal("Unable to use Yaml data.yml");
        }
    }

    private File dataFile;
    private FileConfiguration data;

    @Override
    public void connect() throws Exception
    {
        dataFile = new File(plugin.getDataFolder(), "data.yml");

        boolean created = dataFile.createNewFile();

        if (created)
        {
            Util.log("Created data.yml");
        }

        data = YamlConfiguration.loadConfiguration(dataFile);
    }

    @Override
    public void closeConnection()
    {
        try
        {
            data.save(dataFile);
        }
        catch (IOException ex)
        {
            ex.printStackTrace();

            Util.logErr("Error while saving yaml data to disk");
        }
    }

    @Override
    public void getData(Player player, Consumer<String> consumer)
    {
        String path = getPath(player);
        
        String raw = data.getString(path);
        
        if (raw == null)
        {
            raw = "";
            data.set(path, "");
        }
        
        consumer.accept(raw);
    }

    private final String PLAYERS_PATH = "players";

    @Override
    public void saveData(Player player)
    {
        String serialized = plugin.getRewardManager().getRewardData(player).serialize();
        data.set(getPath(player), serialized);
    }
    
    private String getPath(Player player)
    {
        return PLAYERS_PATH + "." + player.getUniqueId();
    }
}
