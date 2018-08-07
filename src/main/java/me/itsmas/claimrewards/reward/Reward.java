package me.itsmas.claimrewards.reward;

import me.itsmas.claimrewards.message.Message;
import me.itsmas.claimrewards.util.Util;
import me.itsmas.claimrewards.util.UtilItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class Reward implements Cloneable
{
    private final String id;
    private final List<String> commands;

    Reward(String id, Material material, int data, String name, List<String> lore, List<String> commands)
    {
        this.id = id;
        this.commands = commands;

        this.stack = UtilItem.createStack(material, 1, (short) data, name, lore);
    }

    private final ItemStack stack;

    public ItemStack getItemStack()
    {
        return stack;
    }

    String getId()
    {
        return id;
    }

    String getDisplayName()
    {
        return stack.getItemMeta().getDisplayName();
    }

    public void claim(Player player)
    {
        player.sendMessage(Message.CLAIM_REWARD.value().replace("%name%", id));

        commands.forEach(cmd -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replace("%player%", player.getName())));
    }
}
