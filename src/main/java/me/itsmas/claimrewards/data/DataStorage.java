package me.itsmas.claimrewards.data;

import org.bukkit.entity.Player;

import java.util.function.Consumer;

public interface DataStorage
{
    void connect() throws Exception;

    void closeConnection();

    void getData(Player player, Consumer<String> consumer);

    void saveData(Player player);
}
