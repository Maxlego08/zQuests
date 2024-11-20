package fr.maxlego08.quests.messages;

public enum MessageType {

	ACTION,
	TCHAT,
	TITLE,
	CENTER,
	NONE,
	WITHOUT_PREFIX,
	BOSSBAR,
	;

	public static MessageType fromString(String string) {
		try {
			return MessageType.valueOf(string.toUpperCase());
		} catch (Exception ignored) {
			return null;
		}
	}
	
}
