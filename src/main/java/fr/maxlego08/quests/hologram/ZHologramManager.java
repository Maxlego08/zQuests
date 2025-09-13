package fr.maxlego08.quests.hologram;

import fr.maxlego08.menu.api.utils.TypedMapAccessor;
import fr.maxlego08.quests.QuestsPlugin;
import fr.maxlego08.quests.api.ActiveQuest;
import fr.maxlego08.quests.api.Quest;
import fr.maxlego08.quests.api.UserQuest;
import fr.maxlego08.quests.api.event.QuestEvent;
import fr.maxlego08.quests.api.event.events.QuestCompleteEvent;
import fr.maxlego08.quests.api.event.events.QuestDeleteAllEvent;
import fr.maxlego08.quests.api.event.events.QuestDeleteEvent;
import fr.maxlego08.quests.api.event.events.QuestPostProgressEvent;
import fr.maxlego08.quests.api.event.events.QuestStartEvent;
import fr.maxlego08.quests.api.event.events.QuestUserLoadEvent;
import fr.maxlego08.quests.api.hologram.HologramConfiguration;
import fr.maxlego08.quests.api.hologram.HologramManager;
import fr.maxlego08.quests.save.Config;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class ZHologramManager implements HologramManager {

    private final QuestsPlugin plugin;
    private final Map<String, List<HologramConfiguration>> holograms = new HashMap<>();

    public ZHologramManager(QuestsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onQuestEvent(QuestEvent event) {

        if (event instanceof QuestStartEvent startEvent) {
            this.onQuestStart(startEvent.getActiveQuest(), startEvent.getPlayerUUID());
        } else if (event instanceof QuestUserLoadEvent userLoadEvent) {
            this.onQuestLoad(userLoadEvent.getUserQuest());
        } else if (event instanceof QuestCompleteEvent completeEvent) {
            this.onQuestComplete(completeEvent.getPlayer(), completeEvent.getActiveQuest());
        } else if (event instanceof QuestDeleteEvent questDeleteEvent) {
            this.onQuestDelete(questDeleteEvent.getUserQuest(), questDeleteEvent.getQuest());
        } else if (event instanceof QuestDeleteAllEvent questDeleteAllEvent) {
            this.onQuestDeleteAll(questDeleteAllEvent.getUserQuest());
        } else if (event instanceof QuestPostProgressEvent postProgressEvent) {
            this.onQuestsPostProgress(postProgressEvent.getPlayer(), postProgressEvent.getActiveQuests());
        }
    }

    @Override
    public void refreshHologram(Player player) {
        var userQuests = this.plugin.getQuestManager().getUserQuest(player.getUniqueId());
        userQuests.getHolograms().forEach(e -> e.delete(player));
        userQuests.getHolograms().clear();

        for (ActiveQuest activeQuest : userQuests.getActiveQuests()) {
            this.createHolograms(player, activeQuest.getQuest());
        }
    }

    @Override
    public void loadGlobalConfiguration() {
        File file = new File(this.plugin.getDataFolder(), "holograms.yml");
        if (!file.exists()) {
            this.plugin.saveResource("holograms.yml", false);
        }

        this.holograms.clear();

        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
        var holograms = configuration.getMapList("holograms");

        holograms.forEach(map -> {
            var hologramAccessor = new TypedMapAccessor((Map<String, Object>) map);
            var hologramConfiguration = HologramConfiguration.fromConfiguration(hologramAccessor);
            String name = hologramAccessor.getString("name");

            this.holograms.put(name, hologramConfiguration);

            if (Config.enableDebug) {
                this.plugin.getLogger().info("Loaded global hologram " + name);
            }
        });
    }

    @Override
    public Optional<List<HologramConfiguration>> getConfiguration(String name) {
        return Optional.ofNullable(this.holograms.get(name));
    }

    private void onQuestLoad(UserQuest userQuest) {

        var player = Bukkit.getPlayer(userQuest.getUniqueId());
        if (player == null) return;

        for (ActiveQuest activeQuest : userQuest.getActiveQuests()) {
            this.createHolograms(player, activeQuest.getQuest());
        }
    }

    private void onQuestStart(ActiveQuest activeQuest, UUID playerUUID) {

        var player = Bukkit.getPlayer(playerUUID);
        if (player == null) return;

        this.createHolograms(player, activeQuest.getQuest());
    }

    private void onQuestComplete(Player player, ActiveQuest activeQuest) {
        deleteHolograms(player, activeQuest.getQuest());
    }

    private void onQuestDeleteAll(UserQuest userQuest) {
        userQuest.deleteHolograms();
    }

    private void onQuestDelete(UserQuest userQuest, Quest quest) {
        var player = Bukkit.getPlayer(userQuest.getUniqueId());
        if (player == null) return;

        this.deleteHolograms(player, quest);
    }

    private void onQuestsPostProgress(Player player, Set<ActiveQuest> activeQuests) {
        activeQuests.forEach(activeQuest -> {
            if (!activeQuest.isComplete()) {
                this.updateQuest(player, activeQuest.getQuest());
            }
        });
    }

    private void updateQuest(Player player, Quest quest) {
        if (!quest.hasHologram()) return;

        var userQuest = this.plugin.getQuestManager().getUserQuest(player.getUniqueId());
        var optional = userQuest.getHologram(quest);
        if (optional.isEmpty()) return;

        var questHologram = optional.get();
        questHologram.update(player);

    }

    private void deleteHolograms(Player player, Quest quest) {

        if (!quest.hasHologram()) return;

        var userQuest = this.plugin.getQuestManager().getUserQuest(player.getUniqueId());
        var optional = userQuest.getHologram(quest);
        if (optional.isEmpty()) return;

        var questHologram = optional.get();
        questHologram.delete(player);

        userQuest.removeHologram(questHologram);
    }

    private void createHolograms(Player player, Quest quest) {

        if (!quest.hasHologram()) return;

        var userQuest = this.plugin.getQuestManager().getUserQuest(player.getUniqueId());
        if (userQuest.getHologram(quest).isPresent()) return;

        var questHologram = new EssentialsHologram(this.plugin, quest, quest.getHologramName(player.getUniqueId()));
        questHologram.create();
        questHologram.create(player);

        userQuest.addHologram(questHologram);
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        var player = event.getPlayer();
        var userQuest = this.plugin.getQuestManager().getUserQuest(player.getUniqueId());
        userQuest.getHolograms().forEach(questHologram -> questHologram.create(player));
    }
}
