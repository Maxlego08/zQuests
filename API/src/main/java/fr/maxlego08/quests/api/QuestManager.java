package fr.maxlego08.quests.api;

import fr.maxlego08.quests.api.utils.Parameter;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface QuestManager {

    void loadQuests();

    List<Quest> loadQuests(File file);

    List<Quest> getQuests();

    void handleJoin(Player player);

    void handleQuit(UUID uuid);

    void handleQuests(UUID uuid, QuestType type, int amount, Parameter<?>... parameters);

    void addQuestToPlayer(Player player, Quest quest);

    List<ActiveQuest> getQuestsFromPlayer(UUID uuid);

    Optional<Quest> getQuest(String name);

    void completeQuest(ActiveQuest activeQuest);
}
