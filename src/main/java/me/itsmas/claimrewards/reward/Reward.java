package me.itsmas.claimrewards.reward;

import io.samdev.actionutil.ActionUtil;
import me.itsmas.claimrewards.message.Message;
import me.itsmas.claimrewards.util.UtilItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Reward implements Cloneable
{
    private final String id;
    private final List<String> actions;
    private final ItemStack stack;

    Reward(String id, Material material, int data, String name, List<String> lore, List<String> actions)
    {
        this.id = id;
        this.actions = actions;
        this.stack = UtilItem.createStack(material, 1, (short) data, name, lore);
    }

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
        ActionUtil.executeActions(player, actions);
        player.sendMessage(Message.CLAIM_REWARD.value().replace("%name%", id));
    }
}
