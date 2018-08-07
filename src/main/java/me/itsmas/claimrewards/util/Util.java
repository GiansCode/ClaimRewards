package me.itsmas.claimrewards.util;

import me.itsmas.claimrewards.ClaimRewards;
import me.itsmas.claimrewards.message.Message;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;

public class Util
{
    private static final ClaimRewards plugin = JavaPlugin.getPlugin(ClaimRewards.class);

    public static String colour(String input)
    {
        return ChatColor.translateAlternateColorCodes('&', input);
    }

    public static void message(CommandSender sender, Message message)
    {
        sender.sendMessage(message.value());
    }

    public static void broadcast(String msg, String permission)
    {
        Bukkit.getOnlinePlayers().stream().filter(player -> player.hasPermission(permission) || player.isOp()).forEach(player -> player.sendMessage(msg));
    }

    public static String[] trimArgs(String[] args)
    {
        ArrayList<String> list = new ArrayList<>(Arrays.asList(args));
        list.remove(0);

        return list.toArray(new String[0]);
    }

    public static void log(String msg)
    {
        plugin.getLogger().info(msg);
    }

    public static void logErr(String msg)
    {
        plugin.getLogger().log(Level.WARNING, msg);
    }

    public static void logFatal(String msg)
    {
        plugin.getLogger().severe(msg);
    }
}
