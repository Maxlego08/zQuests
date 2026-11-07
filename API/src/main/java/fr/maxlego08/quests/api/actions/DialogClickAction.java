package fr.maxlego08.quests.api.actions;

import fr.maxlego08.quests.api.QuestType;
import fr.maxlego08.quests.api.utils.DialogClick;

public class DialogClickAction extends ActionInfo<DialogClick> {

    public DialogClickAction(DialogClick dialogClick) {
        super(QuestType.DIALOG_CLICK, dialogClick);
    }
}
