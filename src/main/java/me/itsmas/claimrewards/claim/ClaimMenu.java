package me.itsmas.claimrewards.claim;

import me.itsmas.claimrewards.ClaimRewards;
import me.itsmas.claimrewards.reward.Reward;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;

class ClaimMenu implements Listener
{
    private final Inventory inventory;

    ClaimMenu(ClaimRewards plugin, Player player)
    {
        Set<Reward> rewards = plugin.getRewardManager().getRewards(player);
        Set<ItemStack> stacks = new HashSet<>();

        int invSize = rewards.size();

        while (invSize % 9 != 0)
        {
            invSize++;
        }

        rewards.forEach(reward ->
        {
            ItemStack stack = reward.getItemStack().clone();

            int amount = plugin.getRewardManager().getRewardData(player).getRewards(reward);
            stack.setAmount(amount);

            stacks.add(stack);
        });

        this.inventory = Bukkit.createInventory(null, invSize, ClaimManager.GUI_NAME);
        this.inventory.addItem(stacks.toArray(new ItemStack[0]));

        player.openInventory(inventory);
    }
}
