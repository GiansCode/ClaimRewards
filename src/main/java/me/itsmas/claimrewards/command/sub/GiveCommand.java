package me.itsmas.claimrewards.command.sub;

import me.itsmas.claimrewards.ClaimRewards;
import me.itsmas.claimrewards.command.SubCommand;
import me.itsmas.claimrewards.message.Message;
import me.itsmas.claimrewards.util.Perms;
import me.itsmas.claimrewards.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GiveCommand extends SubCommand
{
    public GiveCommand(ClaimRewards plugin)
    {
        super(plugin, Perms.COMMAND_GIVE, "<player> <reward> <amount>", "give");
    }

    @Override
    public void execute(CommandSender sender, String[] args)
    {
        if (args.length != 3)
        {
            help(sender);
            return;
        }

        Player player = Bukkit.getPlayer(args[0]);

        if (player == null)
        {
            Util.message(sender, Message.PLAYER_OFFLINE);
            return;
        }

        String reward = parseReward(sender, args[1]);

        if (reward == null)
        {
            return;
        }

        Integer amount = parseAmount(sender, args[2]);

        if (amount == null)
        {
            return;
        }

        plugin.getRewardManager().giveReward(sender, player, reward, amount);
    }
}
