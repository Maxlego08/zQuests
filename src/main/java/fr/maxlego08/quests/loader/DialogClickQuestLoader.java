package fr.maxlego08.quests.loader;

import fr.maxlego08.menu.api.utils.TypedMapAccessor;
import fr.maxlego08.quests.actions.DialogClickAction;
import fr.maxlego08.quests.api.QuestAction;
import fr.maxlego08.quests.api.QuestActionLoader;
import fr.maxlego08.quests.api.QuestType;

import java.io.File;
import java.util.List;

/**
 * <pre>
 * actions:
 *   - dialog: "welcome"     # or dialogs: [...], "*" or nothing for every dialog
 *     button: "button1"     # or buttons: [...], nothing for every button
 * </pre>
 */
public class DialogClickQuestLoader implements QuestActionLoader {

    private static List<String> readValues(TypedMapAccessor accessor, String single, String multiple) {
        if (accessor.contains(multiple)) return accessor.getStringList(multiple);
        if (accessor.contains(single)) return List.of(accessor.getString(single));
        return List.of();
    }

    @Override
    public QuestAction load(TypedMapAccessor accessor, QuestType questType, File file) {
        return new DialogClickAction(readValues(accessor, "dialog", "dialogs"), readValues(accessor, "button", "buttons"));
    }

    @Override
    public List<QuestType> getSupportedTypes() {
        return List.of(QuestType.DIALOG_CLICK);
    }
}
