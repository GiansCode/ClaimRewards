package me.itsmas.claimrewards.command.sub;

import me.itsmas.claimrewards.ClaimRewards;
import me.itsmas.claimrewards.command.SubCommand;
import me.itsmas.claimrewards.util.Perms;
import org.bukkit.command.CommandSender;

public class GiveAllCommand extends SubCommand
{
    public GiveAllCommand(ClaimRewards plugin)
    {
        super(plugin, Perms.COMMAND_GIVE_ALL, "<reward> <amount>", "giveall");
    }

    @Override
    public void execute(CommandSender sender, String[] args)
    {
        if (args.length != 2)
        {
            help(sender);
            return;
        }

        String reward = parseReward(sender, args[0]);

        if (reward == null)
        {
            return;
        }

        Integer amount = parseAmount(sender, args[1]);

        if (amount == null)
        {
            return;
        }

        plugin.getRewardManager().giveAll(sender, reward, amount);
    }
}
