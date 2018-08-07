package me.itsmas.claimrewards.command;

import me.itsmas.claimrewards.ClaimRewards;
import me.itsmas.claimrewards.command.sub.GiveAllCommand;
import me.itsmas.claimrewards.command.sub.GiveCommand;
import me.itsmas.claimrewards.command.sub.ListCommand;
import me.itsmas.claimrewards.command.sub.ReloadCommand;
import me.itsmas.claimrewards.message.Message;
import me.itsmas.claimrewards.util.Util;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.HashSet;
import java.util.Set;

public class ClaimRewardsCommand implements CommandExecutor
{
    private final ClaimRewards plugin;

    public ClaimRewardsCommand(ClaimRewards plugin)
    {
        this.plugin = plugin;

        addCommands();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        handleCommands(sender, args);
        return true;
    }

    private final Set<SubCommand> subCommands = new HashSet<>();

    private void addCommands()
    {
        addSubCommand(new ReloadCommand(plugin));
        addSubCommand(new ListCommand(plugin));
        addSubCommand(new GiveCommand(plugin));
        addSubCommand(new GiveAllCommand(plugin));
    }

    private void addSubCommand(SubCommand subCommand)
    {
        subCommands.add(subCommand);
    }

    private void handleCommands(CommandSender sender, String[] args)
    {
        if (args.length == 0)
        {
            Util.message(sender, Message.HELP);
            return;
        }

        String arg = args[0];

        for (SubCommand subCommand : subCommands)
        {
            if (ArrayUtils.contains(subCommand.getAliases(), arg))
            {
                if (!sender.hasPermission(subCommand.getPermission()))
                {
                    Util.message(sender, Message.NO_PERMISSION);
                    return;
                }

                String[] newArgs = Util.trimArgs(args);
                subCommand.execute(sender, newArgs);

                return;
            }
        }

        Util.message(sender, Message.INVALID_COMMAND);
    }
}
