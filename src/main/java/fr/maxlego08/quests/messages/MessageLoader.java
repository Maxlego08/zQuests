package fr.maxlego08.quests.messages;


import fr.maxlego08.quests.messages.types.BossBarMessage;
import fr.maxlego08.quests.messages.types.ClassicMessage;
import fr.maxlego08.quests.messages.types.TitleMessage;
import fr.maxlego08.quests.messages.types.ZMessage;
import fr.maxlego08.quests.zcore.ZPlugin;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;

public class MessageLoader {

    private final Locale locale = Locale.getDefault();
    private final ZPlugin plugin;
    private final List<Message> loadedMessages = new ArrayList<>();

    public MessageLoader(ZPlugin plugin) {
        this.plugin = plugin;
    }

    public void load() {

        File file = new File(plugin.getDataFolder(), "messages.yml");
        this.copyFile();

        this.loadMessages(YamlConfiguration.loadConfiguration(file));

        if (this.loadedMessages.size() != Message.values().length) {
            this.plugin.getLogger().log(Level.SEVERE, "Messages were not loaded correctly.");
            for (Message value : Message.values()) {
                if (!this.loadedMessages.contains(value)) {
                    value.setPlugin(plugin);
                    this.plugin.getLogger().log(Level.SEVERE, value + " was not loaded.");

                    List<ZMessage> newMessages = new ArrayList<>();
                    for (ZMessage message : value.getMessages()) {
                        if (message instanceof ClassicMessage classicMessage) {
                            newMessages.add(new ClassicMessage(classicMessage.messageType(), new ArrayList<>(classicMessage.messages())));
                        } else if (message instanceof BossBarMessage bossBarMessage) {
                            newMessages.add(new BossBarMessage(bossBarMessage.text(), bossBarMessage.color(), bossBarMessage.overlay(), bossBarMessage.flags(), bossBarMessage.duration(), bossBarMessage.isStatic()));
                        } else if (message instanceof TitleMessage titleMessage) {
                            newMessages.add(new TitleMessage(titleMessage.title(), titleMessage.subtitle(), titleMessage.start(), titleMessage.time(), titleMessage.end()));
                        }
                    }
                    value.setMessages(newMessages);
                }
            }
        }
    }

    private void copyFile() {

        this.save();

        /*String messageFileName = "messages";
        String localMessageName = "messages_" + locale.getLanguage();
        if (this.plugin.resourceExist("messages/" + localMessageName + ".yml")) {
            messageFileName = localMessageName;
        }

        this.plugin.saveOrUpdateConfiguration("messages/" + messageFileName + ".yml", "messages.yml", false);*/
    }

    private void save() {

        File file = new File(this.plugin.getDataFolder(), "messages.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }

        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
        for (Message message : Message.values()) {
            String path = message.name().toLowerCase().replace("_", "-");

            if (configuration.contains(path)) continue;

            var messages = message.getMessages();
            if (messages.size() == 1) {

                var zMessage = messages.getFirst();
                if (zMessage instanceof ClassicMessage classicMessage) {

                    var currentMessages = classicMessage.messages();
                    switch (classicMessage.messageType()) {
                        case NONE -> configuration.set(path + ".type", zMessage.messageType().name());
                        case ACTION -> {
                            configuration.set(path + ".type", zMessage.messageType().name());
                            configuration.set(path + ".message", currentMessages.getFirst());
                        }
                        case WITHOUT_PREFIX, CENTER -> {
                            configuration.set(path + ".type", zMessage.messageType().name());
                            configuration.set(path + (currentMessages.size() == 1 ? ".message" : ".messages"), currentMessages.size() == 1 ? currentMessages.getFirst() : currentMessages);
                        }
                        default ->
                                configuration.set(path, currentMessages.size() == 1 ? currentMessages.getFirst() : currentMessages);
                    }


                } else if (zMessage instanceof BossBarMessage bossBarMessage) {

                    configuration.set(path + ".type", zMessage.messageType().name());
                    configuration.set(path + ".text", bossBarMessage.text());
                    configuration.set(path + ".color", bossBarMessage.color());
                    configuration.set(path + ".overlay", bossBarMessage.overlay());
                    configuration.set(path + ".flags", bossBarMessage.flags());
                    configuration.set(path + ".duration", bossBarMessage.duration());
                    configuration.set(path + ".static", bossBarMessage.isStatic());

                } else if (zMessage instanceof TitleMessage titleMessage) {

                    configuration.set(path + ".type", zMessage.messageType().name());
                    configuration.set(path + ".title", titleMessage.title());
                    configuration.set(path + ".subtitle", titleMessage.subtitle());
                    configuration.set(path + ".start", titleMessage.start());
                    configuration.set(path + ".time", titleMessage.time());
                    configuration.set(path + ".end", titleMessage.end());
                }

            } else {

                List<Map<String, Object>> listMap = new ArrayList<>();
                for (var zMessage : message.getMessages()) {
                    if (zMessage instanceof ClassicMessage classicMessage) {
                        Map<String, Object> messageMap = new HashMap<>();
                        switch (zMessage.messageType()) {
                            case NONE -> messageMap.put("type", zMessage.messageType().name());
                            case ACTION -> {
                                messageMap.put("type", zMessage.messageType().name());
                                messageMap.put("message", classicMessage.messages().getFirst());
                            }
                            case WITHOUT_PREFIX, CENTER -> {
                                messageMap.put("type", zMessage.messageType().name());
                                messageMap.put(classicMessage.messages().size() == 1 ? "message" : "messages", classicMessage.messages().size() == 1 ? classicMessage.messages().getFirst() : classicMessage.messages());
                            }
                            default -> messageMap.put("messages", classicMessage.messages());
                        }
                        listMap.add(messageMap);
                    } else if (zMessage instanceof BossBarMessage bossBarMessage) {
                        Map<String, Object> messageMap = new HashMap<>();
                        messageMap.put("type", zMessage.messageType().name());
                        messageMap.put("text", bossBarMessage.text());
                        messageMap.put("color", bossBarMessage.color());
                        messageMap.put("overlay", bossBarMessage.overlay());
                        messageMap.put("flags", bossBarMessage.flags());
                        messageMap.put("duration", bossBarMessage.duration());
                        messageMap.put("static", bossBarMessage.isStatic());
                        listMap.add(messageMap);
                    } else if (zMessage instanceof TitleMessage titleMessage) {
                        Map<String, Object> messageMap = new HashMap<>();
                        messageMap.put("type", zMessage.messageType().name());
                        messageMap.put("title", titleMessage.title());
                        messageMap.put("subtitle", titleMessage.subtitle());
                        messageMap.put("start", titleMessage.start());
                        messageMap.put("time", titleMessage.time());
                        messageMap.put("end", titleMessage.end());
                        listMap.add(messageMap);
                    }
                }
                configuration.set(path, listMap);

            }
        }

        try {
            configuration.save(file);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private void loadMessages(YamlConfiguration configuration) {

        this.loadedMessages.clear();

        for (String key : configuration.getKeys(false)) {

            String messageKey = key.replace("-", "_").toUpperCase();
            try {

                Message message = Message.fromString(messageKey);
                if (message == null) {
                    plugin.getLogger().severe("Impossible to find the message " + key + ", it does not exist, you must delete it.");
                    continue;
                }

                message.setPlugin(this.plugin);

                List<ZMessage> essentialsMessages = new ArrayList<>();
                List<Map<?, ?>> mapList = configuration.getMapList(key);

                if (!mapList.isEmpty()) {

                    for (int index = 0; index != mapList.size(); index++) {

                        String path = key + " and index " + (index + 1);
                        Map<?, ?> map = mapList.get(index);
                        MessageType messageType = map.containsKey("type") ? MessageType.fromString((String) map.get("type")) : MessageType.TCHAT;
                        if (messageType == null) {
                            messageType = MessageType.TCHAT;
                            plugin.getLogger().severe("Message type was not found for " + path + ", use TCHAT by default.");
                        }

                        if (messageType == MessageType.BOSSBAR) {

                            String text = getValue(map, "text", path, "Default text", true);
                            String color = getValue(map, "color", path, "WHITE", false);
                            String overlay = getValue(map, "overlay", path, "PROGRESS", false);
                            List<String> flags = getValue(map, "flags", path, new ArrayList<>(), false);
                            long duration = getValue(map, "duration", path, 60L, false);
                            boolean isStatic = getValue(map, "static", path, false, false);

                            BossBarMessage bossBarMessage = new BossBarMessage(text, color, overlay, flags, duration, isStatic);

                            if (bossBarMessage.isValid(this.plugin)) {
                                essentialsMessages.add(bossBarMessage);
                            }

                        } else if (messageType == MessageType.TITLE) {

                            String title = getValue(map, "title", path, "Default title", true);
                            String subtitle = getValue(map, "subtitle", path, "Default subtitle", true);
                            long start = getValue(map, "start", path, 100L, false);
                            long time = getValue(map, "time", path, 2800L, false);
                            long end = getValue(map, "end", path, 100L, false);

                            ZMessage essentialsMessage = new TitleMessage(title, subtitle, start, time, end);
                            essentialsMessages.add(essentialsMessage);
                        } else {

                            List<String> messages = getMessage(map);

                            messages.removeIf(Objects::isNull);
                            if (messages.isEmpty()) {

                                plugin.getLogger().severe("Message is empty for " + key + " and index " + index + ", use default configuration.");
                            } else {

                                ZMessage essentialsMessage = new ClassicMessage(messageType, messages);
                                essentialsMessages.add(essentialsMessage);
                            }
                        }
                    }
                } else if (configuration.contains(key + ".type")) {

                    MessageType messageType = MessageType.fromString(configuration.getString(key + ".type", "TCHAT"));
                    if (messageType == null) {
                        messageType = MessageType.TCHAT;
                        plugin.getLogger().severe("Message type was not found for " + key + ", use TCHAT by default.");
                    }

                    if (messageType == MessageType.TITLE) {

                        String title = configuration.getString(key + ".title", "Default title");
                        String subtitle = configuration.getString(key + ".subtitle", "Default subtitle");
                        long start = configuration.getLong(key + ".start", 100);
                        long time = configuration.getLong(key + ".time", 2800);
                        long end = configuration.getLong(key + ".end", 100);

                        ZMessage essentialsMessage = new TitleMessage(title, subtitle, start, time, end);
                        essentialsMessages.add(essentialsMessage);

                    } else if (messageType == MessageType.BOSSBAR) {

                        String text = configuration.getString(key + ".text", "Default Text");
                        String color = configuration.getString("color", "WHITE");
                        String overlay = configuration.getString("overlay", "PROGRESS");
                        List<String> flags = configuration.getStringList("flags");
                        long duration = configuration.getLong("duration", 60);
                        boolean isStatic = configuration.getBoolean("static", false);

                        BossBarMessage bossBarMessage = new BossBarMessage(text, color, overlay, flags, duration, isStatic);

                        if (bossBarMessage.isValid(this.plugin)) {
                            essentialsMessages.add(bossBarMessage);
                        }

                    } else {

                        List<String> messages = configuration.getStringList(key + ".messages");
                        if (messages.isEmpty()) {
                            messages.add(configuration.getString(key + ".message"));
                        } else {
                            messages = new ArrayList<>(messages);
                        }

                        messages.removeIf(Objects::isNull);
                        if (messages.isEmpty()) {

                            plugin.getLogger().severe("Message is empty for " + key + ", use default configuration.");
                        } else {

                            ZMessage essentialsMessage = new ClassicMessage(messageType, messages);
                            essentialsMessages.add(essentialsMessage);
                        }
                    }

                } else {

                    List<String> messages = configuration.getStringList(key);
                    if (messages.isEmpty()) {
                        messages.add(configuration.getString(key));
                    } else {
                        messages = new ArrayList<>(messages);
                    }

                    messages.removeIf(Objects::isNull);
                    if (messages.isEmpty()) {

                        plugin.getLogger().severe("Message is empty for " + key + ", use default configuration.");
                    } else {

                        ZMessage essentialsMessage = new ClassicMessage(MessageType.TCHAT, messages);
                        essentialsMessages.add(essentialsMessage);
                    }
                }

                message.setMessages(essentialsMessages);
                this.loadedMessages.add(message);

            } catch (Exception exception) {
                exception.printStackTrace();
                this.plugin.getLogger().log(Level.SEVERE, messageKey + " key was not found !");
            }
        }

    }

    private List<String> getMessage(Map<?, ?> map) {
        List<String> messages = new ArrayList<>();

        for (String key : new String[]{"messages", "message"}) {
            Object value = map.get(key);
            if (value instanceof List<?>) {
                for (Object item : (List<?>) value) {
                    if (item != null) {
                        messages.add(item.toString());
                    }
                }
            } else if (value != null) {
                messages.add(value.toString());
            }
        }

        return messages;
    }

    private <T> T getValue(Map<?, ?> map, String key, String path, T defaultValue, boolean isRequired) {
        if (map.containsKey(key)) {
            Object value = map.get(key);

            // Check for specific types and attempt to cast/convert
            if (defaultValue instanceof Integer && value instanceof Number number) {
                return (T) Integer.valueOf(number.intValue());
            } else if (defaultValue instanceof Long && value instanceof Number number) {
                return (T) Long.valueOf(number.longValue());
            } else if (defaultValue instanceof Double && value instanceof Number number) {
                return (T) Double.valueOf(number.doubleValue());
            } else if (defaultValue instanceof Float && value instanceof Number number) {
                return (T) Float.valueOf(number.floatValue());
            } else if (defaultValue instanceof Boolean && value instanceof Boolean) {
                return (T) value;
            } else if (defaultValue instanceof String && value instanceof String) {
                return (T) value;
            } else if (defaultValue != null && defaultValue.getClass().isInstance(value)) {
                return (T) value;
            } else if (defaultValue == null && value != null) {
                try {
                    return (T) value;
                } catch (ClassCastException ignored) {
                    this.plugin.getLogger().severe("Type mismatch for the key " + key + " for the message " + path);
                }
            } else {
                this.plugin.getLogger().severe("Type mismatch for the key " + key + " for the message " + path);
            }
        }

        if (isRequired) {
            this.plugin.getLogger().severe("Unable to find the key " + key + " for the message " + path);
        }

        return defaultValue;
    }


}