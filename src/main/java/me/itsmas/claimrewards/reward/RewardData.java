package me.itsmas.claimrewards.reward;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class RewardData
{
    private final Map<String, Integer> rewards;

    private RewardData(Map<String, Integer> rewards)
    {
        this.rewards = rewards;
    }

    public int getRewards(Reward reward)
    {
        return rewards.get(reward.getId());
    }

    public boolean hasRewards()
    {
        for (Integer integer : rewards.values())
        {
            if (integer > 0)
            {
                return true;
            }
        }

        return false;
    }

    public void decreaseOwned(Reward reward)
    {
        String id = reward.getId();

        rewards.put(id, rewards.get(id) - 1);
    }

    Set<String> getRewards()
    {
        return rewards.keySet();
    }

    void addReward(String id, int amount)
    {
        Integer owned = rewards.getOrDefault(id, 0);
        rewards.put(id, owned + amount);
    }

    public String serialize()
    {
        return rewards.entrySet().stream().map(entry -> entry.getKey() + "," + entry.getValue()).collect(Collectors.joining("|"));
    }

    static RewardData fromString(String data)
    {
        Map<String, Integer> rewards = new HashMap<>();

        if (!data.isEmpty())
        {
            Arrays.stream(data.split(Pattern.quote("|"))).forEach(info ->
            {
                String[] split = info.split(",");

                rewards.put(split[0], Integer.parseInt(split[1]));
            });
        }

        return new RewardData(rewards);
    }
}
