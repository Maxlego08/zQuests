package fr.maxlego08.quests.hologram;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.hologram.Hologram;
import fr.maxlego08.essentials.api.hologram.HologramLine;
import fr.maxlego08.essentials.api.hologram.HologramType;
import fr.maxlego08.essentials.api.hologram.configuration.TextHologramConfiguration;
import fr.maxlego08.essentials.api.utils.SafeLocation;
import fr.maxlego08.quests.QuestsPlugin;
import fr.maxlego08.quests.api.Quest;
import fr.maxlego08.quests.api.hologram.QuestHologram;
import org.bukkit.entity.Player;

public class EssentialsHologram implements QuestHologram {

    private final QuestsPlugin plugin;
    private final Quest quest;
    private final String name;
    private Hologram hologram;
    private EssentialsPlugin essentialsPlugin;

    public EssentialsHologram(QuestsPlugin plugin, Quest quest, String name) {
        this.plugin = plugin;
        this.quest = quest;
        this.name = name;
    }

    @Override
    public void create() {

        if (this.hologram != null) {
            this.hologram.deleteForAllPlayers();
        }

        var hologramManager = this.getEssentialsPlugin().getHologramManager();
        var configuration = this.getConfiguration();

        var hologramConfiguration = quest.getHologramConfiguration();
        this.hologram = hologramManager.createHologram(HologramType.TEXT, configuration, name, name, new SafeLocation(hologramConfiguration.location()));

        for (int i = 0; i < hologramConfiguration.texts().size(); i++) {
            this.hologram.addLine(new HologramLine(i, hologramConfiguration.texts().get(i), false));
        }

        this.hologram.create();
    }

    @Override
    public void create(Player player) {
        if (this.hologram != null) {
            this.hologram.create(player);
        }
    }

    @Override
    public void delete(Player player) {
        if (this.hologram != null) {
            this.hologram.delete(player);
            this.hologram = null;
        }
    }

    @Override
    public void update(Player player) {
        if (this.hologram != null) this.hologram.update(player);
    }

    @Override
    public boolean match(String name) {
        return this.name.equals(name);
    }

    private TextHologramConfiguration getConfiguration() {
        var configuration = new TextHologramConfiguration();
        var questHologramConfiguration = this.quest.getHologramConfiguration();

        configuration.setBillboard(questHologramConfiguration.billboard());
        configuration.setScale(questHologramConfiguration.scale());
        configuration.setTranslation(questHologramConfiguration.translation());
        configuration.setBrightness(questHologramConfiguration.brightness());
        configuration.setShadowRadius(questHologramConfiguration.shadowRadius());
        configuration.setShadowStrength(questHologramConfiguration.shadowStrength());
        configuration.setVisibilityDistance(questHologramConfiguration.visibilityDistance());
        configuration.setTextAlignment(questHologramConfiguration.textAlignment());
        configuration.setTextShadow(questHologramConfiguration.textShadow());
        configuration.setSeeThrough(questHologramConfiguration.seeThrough());
        configuration.setBackground(questHologramConfiguration.background());

        return configuration;
    }

    private EssentialsPlugin getEssentialsPlugin() {
        return this.essentialsPlugin == null ? this.essentialsPlugin = this.plugin.getProvider(EssentialsPlugin.class) : this.essentialsPlugin;
    }
}
