package fr.maxlego08.quests.hologram;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.hologram.Hologram;
import fr.maxlego08.essentials.api.hologram.HologramLine;
import fr.maxlego08.essentials.api.hologram.HologramType;
import fr.maxlego08.essentials.api.hologram.configuration.TextHologramConfiguration;
import fr.maxlego08.essentials.api.utils.SafeLocation;
import fr.maxlego08.quests.QuestsPlugin;
import fr.maxlego08.quests.api.Quest;
import fr.maxlego08.quests.api.hologram.HologramConfiguration;
import fr.maxlego08.quests.api.hologram.QuestHologram;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class EssentialsHologram implements QuestHologram {

    private final QuestsPlugin plugin;
    private final Quest quest;
    private final String name;
    private final List<Hologram> holograms = new ArrayList<>();
    private EssentialsPlugin essentialsPlugin;

    public EssentialsHologram(QuestsPlugin plugin, Quest quest, String name) {
        this.plugin = plugin;
        this.quest = quest;
        this.name = name;
    }

    @Override
    public void create() {

        this.holograms.forEach(Hologram::deleteForAllPlayers);

        var hologramManager = this.getEssentialsPlugin().getHologramManager();

        var hologramConfigurations = quest.getHologramConfigurations();
        for (HologramConfiguration hologramConfiguration : hologramConfigurations) {
            var configuration = this.getConfiguration(hologramConfiguration);
            var hologram = hologramManager.createHologram(HologramType.TEXT, configuration, name, name, new SafeLocation(hologramConfiguration.location()));

            for (int i = 0; i < hologramConfiguration.texts().size(); i++) {
                hologram.addLine(new HologramLine(i, hologramConfiguration.texts().get(i), false));
            }

            hologram.create();
            this.holograms.add(hologram);
        }
    }

    @Override
    public void create(Player player) {
        this.holograms.forEach(hologram -> hologram.create(player));
    }

    @Override
    public void delete(Player player) {
        this.holograms.forEach(hologram -> hologram.delete(player));
        this.holograms.clear();
    }

    @Override
    public void update(Player player) {
        this.holograms.forEach(hologram -> hologram.update(player));
    }

    @Override
    public boolean match(String name) {
        return this.name.equals(name);
    }

    private TextHologramConfiguration getConfiguration(HologramConfiguration hologramConfiguration) {
        var configuration = new TextHologramConfiguration();

        configuration.setBillboard(hologramConfiguration.billboard());
        configuration.setScale(hologramConfiguration.scale());
        configuration.setTranslation(hologramConfiguration.translation());
        configuration.setBrightness(hologramConfiguration.brightness());
        configuration.setShadowRadius(hologramConfiguration.shadowRadius());
        configuration.setShadowStrength(hologramConfiguration.shadowStrength());
        configuration.setVisibilityDistance(hologramConfiguration.visibilityDistance());
        configuration.setTextAlignment(hologramConfiguration.textAlignment());
        configuration.setTextShadow(hologramConfiguration.textShadow());
        configuration.setSeeThrough(hologramConfiguration.seeThrough());
        configuration.setBackground(hologramConfiguration.background());

        return configuration;
    }

    private EssentialsPlugin getEssentialsPlugin() {
        return this.essentialsPlugin == null ? this.essentialsPlugin = this.plugin.getProvider(EssentialsPlugin.class) : this.essentialsPlugin;
    }
}
