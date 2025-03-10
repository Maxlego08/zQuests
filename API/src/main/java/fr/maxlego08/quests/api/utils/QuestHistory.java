package fr.maxlego08.quests.api.utils;

import fr.maxlego08.quests.api.ActiveQuest;
import fr.maxlego08.quests.api.CompletedQuest;
import fr.maxlego08.quests.api.Quest;

import java.text.SimpleDateFormat;
import java.util.Date;

public record QuestHistory(ActiveQuest activeQuest, CompletedQuest completedQuest) {

        public int sortFav() {
            return this.activeQuest != null && this.activeQuest.isFavorite() ? 1 : 0;
        }

        public int sortActive() {
            return this.activeQuest != null ? 1 : 0;
        }

        public int sortCompletedDate() {
            return this.completedQuest != null ? (int) this.completedQuest.completedAt().getTime() : Integer.MIN_VALUE;
        }

        public Quest getQuest() {
            return this.activeQuest != null ? this.activeQuest.getQuest() : this.completedQuest.quest();
        }

        public boolean isActive() {
            return this.activeQuest != null;
        }

        public String getStartedAt(SimpleDateFormat dateFormat) {
            return dateFormat.format(this.isActive() ? this.activeQuest.getCreatedAt() : this.completedQuest.startedAt());
        }

        public String getFinishedAt(SimpleDateFormat dateFormat) {
            return dateFormat.format(this.isActive() ? new Date() : this.completedQuest.completedAt());
        }
    }