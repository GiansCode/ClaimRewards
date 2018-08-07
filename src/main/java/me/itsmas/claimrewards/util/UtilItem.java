package me.itsmas.claimrewards.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class UtilItem
{
    public static ItemStack createStack(Material material, int amount, short data, String name, List<String> lore)
    {
        ItemStack stack = new ItemStack(material, amount);

        stack.setDurability(data);

        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(name);

        if (lore != null)
        {
            meta.setLore(lore);
        }

        stack.setItemMeta(meta);

        return stack;
    }
}
