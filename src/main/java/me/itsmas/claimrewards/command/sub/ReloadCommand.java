package me.itsmas.claimrewards.command.sub;

import me.itsmas.claimrewards.ClaimRewards;
import me.itsmas.claimrewards.command.SubCommand;
import me.itsmas.claimrewards.message.Message;
import me.itsmas.claimrewards.util.Perms;
import me.itsmas.claimrewards.util.Util;
import org.bukkit.command.CommandSender;

public class ReloadCommand extends SubCommand
{
    public ReloadCommand(ClaimRewards plugin)
    {
        super(plugin, Perms.COMMAND_RELOAD, null, "reload");
    }

    @Override
    public void execute(CommandSender sender, String[] args)
    {
        Util.message(sender, Message.RELOADED_CONFIG);

        // Reload messages and rewards
        Message.init(plugin);
        plugin.getRewardManager().initRewards();
    }
}
