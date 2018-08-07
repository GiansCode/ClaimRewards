package me.itsmas.claimrewards.data;

import me.itsmas.claimrewards.ClaimRewards;
import me.itsmas.claimrewards.data.sql.SqlStorage;
import me.itsmas.claimrewards.data.yaml.YamlStorage;

import java.lang.reflect.InvocationTargetException;

public enum Storage
{
    MYSQL(SqlStorage.class),
    YAML(YamlStorage.class);

    Storage(Class<? extends DataStorage> clazz)
    {
        this.clazz = clazz;
    }

    private final Class<? extends DataStorage> clazz;

    public DataStorage newInstance(ClaimRewards plugin)
    {
        try
        {
            return clazz.getDeclaredConstructor(ClaimRewards.class).newInstance(plugin);
        }
        catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException ex)
        {
            ex.printStackTrace();
            return null;
        }
    }
}
