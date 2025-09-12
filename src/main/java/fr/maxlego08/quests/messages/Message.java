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

    INVENTORY_CLONE_NULL("#ff3535The inventory clone is null!"),
    INVENTORY_OPEN_ERROR("#ff3535An error occurred with the opening of the inventory &6%id%#ff3535."),
    TIME_DAY("%02d %day% %02d %hour% %02d %minute% %02d %second%"),
    TIME_HOUR("%02d %hour% %02d minute(s) %02d %second%"),
    TIME_MINUTE("%02d %minute% %02d %second%"),
    TIME_SECOND("%02d %second%"),

    FORMAT_SECOND("second"), FORMAT_SECONDS("seconds"),

    FORMAT_MINUTE("minute"), FORMAT_MINUTES("minutes"),

    FORMAT_HOUR("hour"), FORMAT_HOURS("hours"),

    FORMAT_DAY("d"), FORMAT_DAYS("days"),

    COMMAND_SYNTAX_ERROR("#ff3535You must execute the command like this&7: #77ff77%syntax%"),
    COMMAND_NO_PERMISSION("#ff3535You do not have permission to run this command."),
    COMMAND_NO_CONSOLE("#ff3535Only one player can execute this command."),
    COMMAND_NO_ARG("#ff3535Impossible to find the command with its arguments."),
    COMMAND_SYNTAX_HELP("&f%syntax% &7» &7%description%"),

    RELOAD("#77ff77You have just reloaded the configuration files."),
    RELOAD_INVENTORIES("#77ff77You have just reloaded the inventories files."),

    DESCRIPTION_HELP("Send commands"),
    DESCRIPTION_RELOAD("Reload configuration files"),
    DESCRIPTION_RELOAD_INVENTORIES("Reload inventories files"),
    DESCRIPTION_START("Activate a quest for a player"),
    DESCRIPTION_START_ALL("Activate a quest group for a player"),
    DESCRIPTION_COMPLETE("Complete a quest for a player"),
    DESCRIPTION_COMPLETE_ALL("Complete a quest group for a player"),
    DESCRIPTION_DELETE_ALL("Delete all quests for a player"),
    DESCRIPTION_DELETE("Delete a quest for a player"),
    DESCRIPTION_RESTART("Restart a quest for a player"),
    DESCRIPTION_SET_PROGRESS("Set the progress of a quest for a player"),
    DESCRIPTION_SET_FAVORITE("Set a quest as favorite for a player"),
    DESCRIPTION_SET_FAVORITE_LIMIT("Set the quest favorite limit for a player"),
    DESCRIPTION_SET_FAVORITE_TYPE("Set the quest favorite type placeholder for a player"),
    DESCRIPTION_ADD_PROGRESS("Add a number to the progress of a quest for a player"),
    DESCRIPTION_PROGRESS_INVENTORY("Progress inventory content quests for a player"),
    DESCRIPTION_REFRESH_HOLOGRAM("Update player holograms"),
    DESCRIPTION_ADD_FAVORITE("Allows a player to bookmark a quest, use this command in a message with a click."),
    DESCRIPTION_SHOW("Show player's quests"),

    GROUP_NOT_FOUND("#ff3535Unable to find the group &f%name%#ff3535."),
    QUEST_NOT_FOUND("#ff3535Unable to find the quest &f%name%#ff3535."),
    QUEST_START_ERROR_ACTIVE("#ff3535The quest &f%name% #ff3535is already active for the player &f%player%#ff3535."),
    QUEST_START_ERROR_COMPLETED("#ff3535The quest &f%name% #ff3535is already completed for the player &f%player%#ff3535."),
    QUEST_START_SUCCESS("#77ff77You have just activated the quest &f%name%#77ff77 for the player &f%player%#77ff77."),
    QUEST_START_GROUP("#77ff77You have just activated the quest group &f%name%#77ff77 for the player &f%player%#77ff77."),

    QUEST_REWARD("#77ff77You have just given the rewards of the quest &f%name% #77ff77to the player&f %player%#77ff77."),

    GROUP_COMPLETE_SUCCESS("#77ff77The player &f%player% #77ff77has just completed the quest group &f%name%#77ff77!"),
    QUEST_COMPLETE_SUCCESS("#77ff77The player &f%player% #77ff77has just completed the quest &f%name%#77ff77!"),
    QUEST_DELETE_SUCCESS("#77ff77The quest &f%name% #77ff77has just been deleted#77ff77."),
    QUEST_DELETE_ALL_SUCCESS("#77ff77All the quests have just been deleted#77ff77."),
    QUEST_SET_PROGRESS_SUCCESS("#77ff77The player &f%player% #77ff77has just set the progress of the quest &f%name% #77ff77to &f%progress%#77ff77."),
    QUEST_ADD_PROGRESS_SUCCESS("#77ff77The player &f%player% #77ff77has just added &f%progress% #77ff77to the progress of the quest &f%name%#77ff77."),
    QUEST_SET_FAVORITE_LIMIT_SUCCESS("#77ff77The player &f%player% #77ff77has just set the favorite limit of the quests #77ff77to &f%limit%#77ff77."),
    QUEST_SET_FAVORITE_TYPE_SUCCESS("#77ff77The player &f%player% #77ff77has just set the favorite placeholder type #77ff77to &f%type%#77ff77."),
    INVENTORY_NOT_FOUND("#ff3535Unable to find the inventory &f%inventory%#ff3535."),

    PROGRESS_INVENTORY_INFO("#77ff77The player &f%player% #77ff77has finished &f%amount% #77ff77quests in the inventory#77ff77."),
    QUEST_CANT_CHANGE_FAVORITE("#ff3535You can not change the favorite of the quest &f%name%#ff3535."),
    QUEST_SET_FAVORITE_SUCCESS("#77ff77You have just set the favorite of the quest &f%name%#77ff77 for the player &f%player%#77ff77."),
    QUEST_RESTART_SUCCESS("#77ff77The player &f%player% #77ff77has just restarted the quest &f%name%#77ff77."),
    HOLOGRAM_REFRESH("#77ff77You have just refreshed the &f%player%#77ff77's hologram#77ff77."),

    INVENTORY_CONTENT("#edacfcx%amount% #ff94d7<lang:%material-key%>"),
    INVENTORY_AND(" #77ff77and "),
    INVENTORY_REMOVE("#77ff77you just gave %items%#77ff77."),

    SHOW_LOAD_USER("&7Load &f%player% &7quests..."),
    SHOW_HEADER("&f%player% #edacfc%active-quests% &7ongoing and #edacfc%completed-quests% &7completed quests"),
    SHOW_ELEMENT("<hover:show_text:\"%quest-description%<newline><white>%quest-display-name%<newline><newline><white>Start at %started-at%<newline><yellow>%limit%<gray>/<gold>%quest-objective%<newline><white>Favorite: %is-favorite%\">%quest-name%</hover>"),
    SHOW_INFOS(MessageType.WITHOUT_PREFIX, "%quests%"),

    CRAFT_OFFHAND("#ff3535You cannot use your secondary hand to craft items.");

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

