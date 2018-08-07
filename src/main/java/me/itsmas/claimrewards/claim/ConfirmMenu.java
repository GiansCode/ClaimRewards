package me.itsmas.claimrewards.claim;

import me.itsmas.claimrewards.util.Util;
import me.itsmas.claimrewards.util.UtilItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

class ConfirmMenu
{
    private static final ItemStack CONFIRM_BUTTON =
            UtilItem.createStack(Material.EMERALD_BLOCK, 1, (short) 0, Util.colour("&a&lConfirm"), null);

    private static final ItemStack CANCEL_BUTTON =
            UtilItem.createStack(Material.REDSTONE_BLOCK, 1, (short) 0, Util.colour("&c&lCancel"), null);

    private final Inventory inventory;

    ConfirmMenu(String title, int size)
    {
        this.inventory = Bukkit.createInventory(null, size, title);

        inventory.setItem(11, CONFIRM_BUTTON);
        inventory.setItem(15, CANCEL_BUTTON);
    }

    public String getTitle()
    {
        return inventory.getTitle();
    }

    void open(Player player)
    {
        player.openInventory(inventory);
    }
}
