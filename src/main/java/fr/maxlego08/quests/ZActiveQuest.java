package fr.maxlego08.quests;

import fr.maxlego08.menu.api.requirement.Permissible;
import fr.maxlego08.menu.api.utils.Placeholders;
import fr.maxlego08.quests.api.ActiveQuest;
import fr.maxlego08.quests.api.CompletedQuest;
import fr.maxlego08.quests.api.Quest;
import fr.maxlego08.quests.api.QuestType;
import org.bukkit.Bukkit;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class ZActiveQuest implements ActiveQuest {

    private final QuestsPlugin plugin;
    private final UUID playerUUID;
    private final Quest quest;
    private final Date createdAt;
    private final long startPlayTime;
    private long amount;
    private boolean isFavorite;

    public ZActiveQuest(QuestsPlugin plugin, UUID playerUUID, Quest quest, Date createdAt, long amount, boolean isFavorite, long startPlayTime) {
        this.plugin = plugin;
        this.playerUUID = playerUUID;
        this.quest = quest;
        this.createdAt = createdAt;
        this.amount = amount;
        this.isFavorite = isFavorite;
        this.startPlayTime = startPlayTime;
    }

    @Override
    public UUID getPlayerUUID() {
        return this.playerUUID;
    }

    @Override
    public Quest getQuest() {
        return this.quest;
    }

    @Override
    public long getAmount() {
        return this.amount;
    }

    @Override
    public void setAmount(long amount) {
        this.amount = amount;
    }

    @Override
    public void addAmount(long amount) {
        this.amount += amount;
    }

    @Override
    public boolean isComplete() {
        return this.amount >= this.quest.getGoal();
    }

    @Override
    public boolean isType(QuestType type) {
        return this.quest.getType() == type;
    }

    @Override
    public boolean owningBy(UUID uniqueId) {
        return this.playerUUID.equals(uniqueId);
    }

    private boolean postCheck() {

        if (this.isComplete() || this.canForceCompleteConditions()) {
            this.quest.onComplete(this);
            return true;
        }

        return false;
    }

    @Override
    public boolean increment(long amount) {
        if (this.isComplete()) return false;

        this.amount += amount;
        return postCheck();
    }

    @Override
    public boolean incrementStatic(long amount) {
        if (this.isComplete()) return false;

        this.amount = amount;
        return postCheck();
    }

    @Override
    public CompletedQuest complete() {
        return new CompletedQuest(this.quest, new Date(), this.createdAt, this.isFavorite, this.startPlayTime, this.plugin.getPlayTimeHelper().getPlayTime(this.playerUUID));
    }

    @Override
    public boolean isQuestAction(Object object) {
        return this.quest.getActions().stream().anyMatch(questAction -> questAction.isAction(object));
    }

    @Override
    public boolean isFavorite() {
        return this.isFavorite;
    }

    @Override
    public void setFavorite(boolean favorite) {
        this.isFavorite = favorite;
    }

    @Override
    public Date getCreatedAt() {
        return this.createdAt;
    }

    @Override
    public boolean canComplete() {
        return this.checkPermissible(this.quest.getActionPermissibles());
    }

    @Override
    public boolean canForceCompleteConditions() {

        if (this.quest.getForceConditions().isEmpty()) return false;
        
        return this.checkPermissible(this.quest.getForceConditions());
    }

    private boolean checkPermissible(List<Permissible> permissibles) {
        if (permissibles.isEmpty()) return true;

        var player = Bukkit.getPlayer(this.playerUUID);
        if (player == null) return false;

        var fakeInventory = this.plugin.getInventoryManager().getFakeInventory();

        var placeholders = new Placeholders();
        placeholders.register("player", player.getName());
        return permissibles.stream().allMatch(permissible -> {
            var result = permissible.isValid() && permissible.hasPermission(player, null, fakeInventory, placeholders);
            var actions = result ? permissible.getSuccessActions() : permissible.getDenyActions();
            actions.forEach(action -> action.preExecute(player, null, fakeInventory, placeholders));
            return result;
        });
    }

    @Override
    public long getStartPlayTime() {
        return this.startPlayTime;
    }
}
