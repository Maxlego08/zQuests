package fr.maxlego08.quests.messages;

import fr.maxlego08.quests.messages.types.ClassicMessage;
import fr.maxlego08.quests.messages.types.ZMessage;
import fr.maxlego08.quests.zcore.ZPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum Message {

    PREFIX("&8(#3dffa1zQuests&8) "),

    INVENTORY_CLONE_NULL("&cThe inventory clone is null!"),
    INVENTORY_OPEN_ERROR("&cAn error occurred with the opening of the inventory &6%id%&c."),
    TIME_DAY("%02d %day% %02d %hour% %02d %minute% %02d %second%"),
    TIME_HOUR("%02d %hour% %02d minute(s) %02d %second%"),
    TIME_MINUTE("%02d %minute% %02d %second%"),
    TIME_SECOND("%02d %second%"),

    FORMAT_SECOND("second"), FORMAT_SECONDS("seconds"),

    FORMAT_MINUTE("minute"), FORMAT_MINUTES("minutes"),

    FORMAT_HOUR("hour"), FORMAT_HOURS("hours"),

    FORMAT_DAY("d"), FORMAT_DAYS("days"),

    COMMAND_SYNTAX_ERROR("&cYou must execute the command like this&7: &a%syntax%"),
    COMMAND_NO_PERMISSION("&cYou do not have permission to run this command."),
    COMMAND_NO_CONSOLE("&cOnly one player can execute this command."),
    COMMAND_NO_ARG("&cImpossible to find the command with its arguments."),
    COMMAND_SYNTAX_HELP("&f%syntax% &7» &7%description%"),

    RELOAD("&aYou have just reloaded the configuration files."),

    DESCRIPTION_HELP("Send commands"),
    DESCRIPTION_RELOAD("Reload configuration files"),
    DESCRIPTION_START("Activate a quest for a player"),
    DESCRIPTION_COMPLETE("Complete a quest for a player"),
    DESCRIPTION_DELETE_ALL("Delete all quests for a player"),
    DESCRIPTION_DELETE("Delete a quest for a player"),
    DESCRIPTION_SET_PROGRESS("Set the progress of a quest for a player"),
    DESCRIPTION_ADD_PROGRESS("Add a number to the progress of a quest for a player"),

    QUEST_NOT_FOUND("&cUnable to find the quest &f%name%&c."),
    QUEST_START_ERROR("&cThe quest &f%name% &cis already completed or active for the player &f%player%&c."),
    QUEST_START_SUCCESS("&aYou have just activated the quest &f%name%&a for the player &f%player%&a."),

    QUEST_COMPLETE_SUCCESS("&aThe player &f%player% &ahas just completed the quest &f%name%&a!"),
    QUEST_DELETE_SUCCESS("&aThe quest &f%name% &ahas just been deleted&a."),
    QUEST_DELETE_ALL_SUCCESS("&aAll the quests have just been deleted&a."),
    QUEST_SET_PROGRESS_SUCCESS("&aThe player &f%player% &ahas just set the progress of the quest &f%name% &ato &f%progress%&a."),
    QUEST_ADD_PROGRESS_SUCCESS("&aThe player &f%player% &ahas just added &f%progress% &ato the progress of the quest &f%name%&a."),
    ;

    private ZPlugin plugin;
    private List<ZMessage> messages = new ArrayList<>();

    Message(String message) {
        this(MessageType.TCHAT, message);
    }

    Message(MessageType messageType, String message) {
        this.messages.add(new ClassicMessage(messageType, Collections.singletonList(message)));
    }

    Message(String... message) {
        this(MessageType.TCHAT, message);
    }

    Message(MessageType messageType, String... messages) {
        this.messages.add(new ClassicMessage(messageType, Arrays.asList(messages)));
    }

    Message(ZMessage... essentialsMessages) {
        this.messages = Arrays.asList(essentialsMessages);
    }

    public static Message fromString(String string) {
        try {
            return valueOf(string);
        } catch (Exception ignored) {
            return null;
        }
    }

    public List<ZMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<ZMessage> messages) {
        this.messages = messages;
    }

    public String toConfigurationName() {
        return name().replace("_", "-").toLowerCase();
    }

    public String getMessageAsString() {
        String configurationName = this.toConfigurationName();
        if (this.messages.isEmpty()) {
            this.plugin.getLogger().severe(configurationName + " is empty ! Check your configuration");
            return "Error with " + configurationName + ", check your console";
        }
        ZMessage essentialsMessage = this.messages.getFirst();
        if (essentialsMessage instanceof ClassicMessage classicMessage) {

            if (classicMessage.messages().isEmpty()) {
                this.plugin.getLogger().severe(configurationName + " message is empty ! Check your configuration");
                return "Error with " + configurationName + ", check your console";
            }

            return classicMessage.messages().getFirst();
        }

        this.plugin.getLogger().severe(configurationName + " is not a tchat message ! Check your configuration");
        return "Error with " + configurationName + ", check your console";
    }

    public void setPlugin(ZPlugin plugin) {
        this.plugin = plugin;
    }

    public List<String> getMessageAsStringList() {
        return this.messages.stream().filter(message -> message instanceof ClassicMessage).map(essentialsMessage -> (ClassicMessage) essentialsMessage).map(ClassicMessage::messages).flatMap(List::stream).toList();
    }

    public String msg() {
        return getMessageAsString();
    }
}

