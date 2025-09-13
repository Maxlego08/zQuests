package fr.maxlego08.quests.zcore.utils;

import fr.maxlego08.quests.api.utils.PlayTimeHelper;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;

import java.util.UUID;

public class SpigotPlayTime implements PlayTimeHelper {


    @Override
    public long getPlayTime(UUID uuid) {
        return Bukkit.getOfflinePlayer(uuid).getStatistic(Statistic.PLAY_ONE_MINUTE) / 20;
    }
}
