package me.itsmas.claimrewards.command;

import me.itsmas.claimrewards.ClaimRewards;
import me.itsmas.claimrewards.message.Message;
import me.itsmas.claimrewards.reward.RewardData;
import me.itsmas.claimrewards.util.Util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClaimCommand implements CommandExecutor
{
    private final ClaimRewards plugin;

    public ClaimCommand(ClaimRewards plugin)
    {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        handleCommand(sender);
        return false;
    }

    private void handleCommand(CommandSender sender)
    {
        if (!(sender instanceof Player))
        {
            Util.message(sender, Message.PLAYER_ONLY);
            return;
        }

        Player player = (Player) sender;

        RewardData data = plugin.getRewardManager().getRewardData(player);

        if (!data.hasRewards())
        {
            Util.message(player, Message.NO_REWARDS);
            return;
        }

        plugin.getClaimManager().openClaimMenu(player);
    }
}
