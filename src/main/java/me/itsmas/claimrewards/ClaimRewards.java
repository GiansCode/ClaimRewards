package me.itsmas.claimrewards;

import me.itsmas.claimrewards.claim.ClaimManager;
import me.itsmas.claimrewards.command.ClaimCommand;
import me.itsmas.claimrewards.command.ClaimRewardsCommand;
import me.itsmas.claimrewards.data.DataStorage;
import me.itsmas.claimrewards.data.Storage;
import me.itsmas.claimrewards.message.Message;
import me.itsmas.claimrewards.reward.RewardManager;
import org.bukkit.plugin.java.JavaPlugin;

public class ClaimRewards extends JavaPlugin
{
    private DataStorage dataStorage;
    private RewardManager rewardManager;
    private ClaimManager claimManager;

    @Override
    public void onEnable()
    {
        saveDefaultConfig();

        Message.init(this);

        getCommand("claim").setExecutor(new ClaimCommand(this));
        getCommand("claimrewards").setExecutor(new ClaimRewardsCommand(this));

        Storage storage = Storage.valueOf(((String) configGet("storage", "yaml")).toUpperCase());
        dataStorage = storage.newInstance(this);

        rewardManager = new RewardManager(this);
        claimManager = new ClaimManager(this);
    }

    @Override
    public void onDisable()
    {
        getDataStorage().closeConnection();
    }

    public DataStorage getDataStorage()
    {
        return dataStorage;
    }

    public RewardManager getRewardManager()
    {
        return rewardManager;
    }

    public ClaimManager getClaimManager()
    {
        return claimManager;
    }

    public <T> T configGet(String path)
    {
        return configGet(path, null);
    }

    @SuppressWarnings("unchecked")
    public <T> T configGet(String path, Object defaultValue)
    {
        return (T) getConfig().get(path, defaultValue);
    }
}
