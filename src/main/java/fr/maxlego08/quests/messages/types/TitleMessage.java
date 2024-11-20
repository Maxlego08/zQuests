package fr.maxlego08.quests.messages.types;


import fr.maxlego08.quests.messages.MessageType;

public record TitleMessage(String title, String subtitle, long start, long time,
                           long end) implements ZMessage {

    @Override
    public MessageType messageType() {
        return MessageType.TITLE;
    }
}