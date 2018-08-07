package me.itsmas.claimrewards.message;

import me.itsmas.claimrewards.ClaimRewards;
import me.itsmas.claimrewards.util.Util;

import java.util.Arrays;

public enum Message
{
    CLAIM_REWARD,
    PLAYER_ONLY,
    PLAYER_OFFLINE,
    COMMAND_USAGE,
    NO_PERMISSION,
    NO_REWARDS,
    INVALID_COMMAND,
    INVALID_REWARD,
    INVALID_NUMBER,
    REWARD_MINIMUM,
    GIVE,
    GIVE_ALL,
    RELOADED_CONFIG,
    REWARD_LIST,
    RECEIVED_REWARD,
    HELP;

    private String msg;

    public String value()
    {
        return msg;
    }

    private void setValue(String msg)
    {
        this.msg = msg;
    }

    /**
     * Initialise the plugin messages
     * @param plugin The plugin instance
     */
    public static void init(ClaimRewards plugin)
    {
        Arrays.stream(values()).forEach(message ->
        {
            String raw = plugin.configGet( "messages." + message.name().toLowerCase());

            if (raw == null)
            {
                Util.logErr("Unable to find message value for message '" + message.name() + "'");

                raw = message.name();
            }

            message.setValue(Util.colour(raw));
        });
    }
}
