package me.itsmas.claimrewards.reward;

import me.itsmas.claimrewards.ClaimRewards;
import me.itsmas.claimrewards.message.Message;
import me.itsmas.claimrewards.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.*;
import java.util.stream.Collectors;

public class RewardManager implements Listener
{
    private final ClaimRewards plugin;

    public RewardManager(ClaimRewards plugin)
    {
        this.plugin = plugin;

        new RewardListeners(plugin);

        initRewards();
    }

    /* Players */
    private Map<UUID, RewardData> rewards = new HashMap<>();

    public RewardData getRewardData(Player player)
    {
        return rewards.get(player.getUniqueId());
    }

    void setRewards(Player player, RewardData rewards)
    {
        this.rewards.put(player.getUniqueId(), rewards);
    }

    void removePlayer(Player player)
    {
        rewards.remove(player.getUniqueId());
    }

    public Set<Reward> getRewards(Player player)
    {
        return getRewardData(player).getRewards().stream().map(this::getReward).filter(Objects::nonNull).collect(Collectors.toSet());
    }

    public String getRewardId(String id)
    {
        for (Reward reward : baseRewards)
        {
            if (reward.getId().equalsIgnoreCase(id))
            {
                return reward.getId();
            }
        }

        return null;
    }

    Reward getReward(String id)
    {
        for (Reward reward : baseRewards)
        {
            if (reward.getId().equals(id))
            {
                return reward;
            }
        }

        return null;
    }

    public Reward getRewardByName(String displayName)
    {
        for (Reward reward : baseRewards)
        {
            if (displayName.equals(reward.getDisplayName()))
            {
                return reward;
            }
        }

        return null;
    }

    public String getRewardIds()
    {
        return baseRewards.stream().map(Reward::getId).collect(Collectors.joining(", "));
    }

    public void giveReward(CommandSender sender, Player target, String reward, int amount)
    {
        sender.sendMessage(Message.GIVE.value().replace("%player%", target.getName()).replace("%amount%", String.valueOf(amount)).replace("%reward%", reward));
        sendReceivedMessage(sender, target, reward, amount);

        getRewardData(target).addReward(reward, amount);
    }

    public void giveAll(CommandSender sender, String reward, int amount)
    {
        sender.sendMessage(Message.GIVE_ALL.value().replace("%amount%", String.valueOf(amount)).replace("%reward%", reward));

        Bukkit.getOnlinePlayers().forEach(player ->
        {
            sendReceivedMessage(sender, player, reward, amount);

            getRewardData(player).addReward(reward, amount);
        });
    }

    private void sendReceivedMessage(CommandSender sender, Player target, String reward, int amount)
    {
        String msg = Message.RECEIVED_REWARD.value()
                .replace("%amount%", String.valueOf(amount))
                .replace("%reward%", reward)
                .replace("%player%", sender.getName());

        target.sendMessage(msg);
    }

    /* Config */
    private final String REWARDS_PATH = "rewards";

    private final Set<Reward> baseRewards = new HashSet<>();

    /**
     * Initialises rewards from config
     */
    public void initRewards()
    {
        baseRewards.clear();

        ConfigurationSection section = plugin.getConfig().getConfigurationSection(REWARDS_PATH);

        for (String rewardName : section.getKeys(false))
        {
            baseRewards.add(fromConfig(rewardName));
        }
    }

    private Reward fromConfig(String name)
    {
        FileConfiguration config = plugin.getConfig();

        String rewardPath = REWARDS_PATH + "." + name + ".";
        String itemPath = rewardPath + "item.";

        Material material;

        try
        {
            material = Material.valueOf(plugin.configGet(itemPath + "type"));
        }
        catch (IllegalArgumentException ex)
        {
            material = Material.PAPER;

            Util.logErr("Invalid material provided for reward '" + name + "'");
        }

        int data;

        try
        {
            data = plugin.configGet(itemPath + "data");
        }
        catch (IllegalArgumentException ex)
        {
            data = 0;

            Util.logErr("Invalid data value for reward '" + name + "'");
        }

        String itemName = Util.colour(plugin.configGet(itemPath + "name", name));
        List<String> lore = plugin.configGet(itemPath + "lore");

        List<String> colouredLore = new ArrayList<>();
        lore.forEach(line -> colouredLore.add(Util.colour(line)));

        List<String> actions = config.getStringList(rewardPath + "actions");

        return new Reward(name, material, data, itemName, colouredLore, actions);
    }
}
