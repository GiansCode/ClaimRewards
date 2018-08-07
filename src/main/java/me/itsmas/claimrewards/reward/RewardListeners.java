package me.itsmas.claimrewards.reward;

import me.itsmas.claimrewards.ClaimRewards;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

class RewardListeners implements Listener
{
    private final ClaimRewards plugin;

    RewardListeners(ClaimRewards plugin)
    {
        this.plugin = plugin;

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();

        plugin.getDataStorage().getData(player, data ->
        {
            RewardData rewards = RewardData.fromString(data);

            plugin.getRewardManager().setRewards(player, rewards);
        });
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event)
    {
        Player player = event.getPlayer();

        plugin.getDataStorage().saveData(player);
        plugin.getRewardManager().removePlayer(player);
    }
}
