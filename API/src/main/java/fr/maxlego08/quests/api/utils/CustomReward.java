package fr.maxlego08.quests.api.utils;

import fr.maxlego08.menu.api.requirement.Action;

import java.util.List;

public record CustomReward(List<String> quests, List<Action> actions) {
}
