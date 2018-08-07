package me.itsmas.claimrewards.command.sub;

import me.itsmas.claimrewards.ClaimRewards;
import me.itsmas.claimrewards.command.SubCommand;
import me.itsmas.claimrewards.message.Message;
import me.itsmas.claimrewards.util.Perms;
import org.bukkit.command.CommandSender;

public class ListCommand extends SubCommand
{
    public ListCommand(ClaimRewards plugin)
    {
        super(plugin, Perms.COMMAND_LIST, null, "list");
    }

    @Override
    public void execute(CommandSender sender, String[] args)
    {
        String msg = Message.REWARD_LIST.value().replace("%ids%", plugin.getRewardManager().getRewardIds());

        sender.sendMessage(msg);
    }
}
