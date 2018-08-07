package me.itsmas.claimrewards.claim;

import com.google.common.collect.Iterables;
import me.itsmas.claimrewards.ClaimRewards;
import me.itsmas.claimrewards.reward.Reward;
import me.itsmas.claimrewards.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ClaimManager implements Listener
{
    private final ClaimRewards plugin;

    static String GUI_NAME;

    public ClaimManager(ClaimRewards plugin)
    {
        this.plugin = plugin;

        Bukkit.getPluginManager().registerEvents(this, plugin);

        GUI_NAME = Util.colour(plugin.configGet("inventory.title", "&cClaim Rewards"));

        String confirmName = Util.colour(plugin.configGet("inventory.confirm.title", "&cConfirm"));
        int size = 9 * (int) plugin.configGet("inventory.confirm.size", 3);

        confirmMenu = new ConfirmMenu(confirmName, size);
    }

    public void openClaimMenu(Player player)
    {
        new ClaimMenu(plugin, player);
    }

    private Map<UUID, Reward> confirmations = new HashMap<>();

    private ConfirmMenu confirmMenu;

    @EventHandler
    public void onClick(InventoryClickEvent event)
    {
        ItemStack clicked = event.getCurrentItem();

        if (clicked == null || clicked.getType() == Material.AIR)
        {
            return;
        }

        Player player = (Player) event.getWhoClicked();
        Inventory inventory = event.getInventory();

        if (inventory.getTitle().equals(GUI_NAME))
        {
            event.setCancelled(true);

            if (clicked.hasItemMeta() && clicked.getItemMeta().hasDisplayName())
            {
                String itemName = clicked.getItemMeta().getDisplayName();

                Reward reward = plugin.getRewardManager().getRewardByName(itemName);

                if (reward != null)
                {
                    confirmations.put(player.getUniqueId(), reward);

                    confirmMenu.open(player);
                }
            }
        }
        else if (inventory.getTitle().equals(confirmMenu.getTitle()))
        {
            event.setCancelled(true);

            if (clicked.getType() == Material.EMERALD_BLOCK)
            {
                Reward reward = confirmations.get(player.getUniqueId());

                if (reward == null)
                {
                    player.getOpenInventory().close();
                    player.sendMessage(Util.colour("&cSomething went wrong claiming your reward, please try again"));

                    return;
                }

                reward.claim(player);
                plugin.getRewardManager().getRewardData(player).decreaseOwned(reward);

                confirmations.remove(player.getUniqueId());
            }

            player.getOpenInventory().close();
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event)
    {
        confirmations.remove(event.getPlayer().getUniqueId());
    }
}
