package fr.maxlego08.quests.actions;

import fr.maxlego08.quests.api.QuestAction;
import fr.maxlego08.quests.api.QuestType;
import fr.maxlego08.quests.api.utils.DialogClick;

import java.util.List;

/**
 * Matches a click on a button of a zMenu dialog.
 * An empty list of dialogs (or {@code *}) matches every dialog, an empty list of buttons matches every button.
 */
public class DialogClickAction implements QuestAction {

    private final List<String> dialogs;
    private final List<String> buttons;

    public DialogClickAction(List<String> dialogs, List<String> buttons) {
        this.dialogs = dialogs;
        this.buttons = buttons;
    }

    private static boolean matches(List<String> values, String value) {
        return values.isEmpty() || values.stream().anyMatch(expected -> expected.equals("*") || expected.equalsIgnoreCase(value));
    }

    @Override
    public boolean isAction(Object target) {
        return target instanceof DialogClick click && matches(this.dialogs, click.dialog()) && matches(this.buttons, click.button());
    }

    @Override
    public QuestType getQuestType() {
        return QuestType.DIALOG_CLICK;
    }
}
