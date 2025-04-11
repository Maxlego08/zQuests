package fr.maxlego08.quests.api.hologram;

import org.bukkit.entity.Player;

public interface QuestHologram {

    void create(Player player);

    void delete(Player player);

    void update(Player player);

}
