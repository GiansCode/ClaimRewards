package me.itsmas.claimrewards.command;

import me.itsmas.claimrewards.ClaimRewards;
import me.itsmas.claimrewards.message.Message;
import me.itsmas.claimrewards.util.Util;
import org.bukkit.command.CommandSender;

public abstract class SubCommand
{
    protected final ClaimRewards plugin;

    private final String permission;
    private final String usage;
    private final String[] aliases;

    public SubCommand(ClaimRewards plugin, String permission, String usage, String... aliases)
    {
        this.plugin = plugin;

        this.permission = permission;
        this.usage = usage;
        this.aliases = aliases;
    }

    String getPermission()
    {
        return permission;
    }

    String[] getAliases()
    {
        return aliases;
    }

    protected void help(CommandSender sender)
    {
        sender.sendMessage(Message.COMMAND_USAGE.value().replace("%usage%", "/claimrewards " + getAliases()[0] + " " + usage));
    }

    protected String parseReward(CommandSender sender, String input)
    {
        String reward = plugin.getRewardManager().getRewardId(input);

        if (reward == null)
        {
            sender.sendMessage(Message.INVALID_REWARD.value().replace("%id%", input));
            return null;
        }

        return reward;
    }

    protected Integer parseAmount(CommandSender sender, String input)
    {
        try
        {
            int value = Integer.parseInt(input);

            if (value < 1)
            {
                Util.message(sender, Message.REWARD_MINIMUM);
                return null;
            }

            return value;
        }
        catch (NumberFormatException ex)
        {
            sender.sendMessage(Message.INVALID_NUMBER.value().replace("%number%", input));
            return null;
        }
    }

    public abstract void execute(CommandSender sender, String[] args);
}
