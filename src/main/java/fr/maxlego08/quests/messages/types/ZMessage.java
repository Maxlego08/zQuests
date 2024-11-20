
package fr.maxlego08.quests.messages.types;

import fr.maxlego08.quests.messages.MessageType;

/**
 * An interface representing an essential message with a specific type.
 */
public interface ZMessage {

    /**
     * Retrieves the type of the message.
     *
     * @return the {@link MessageType} of the message
     */
    MessageType messageType();
}
