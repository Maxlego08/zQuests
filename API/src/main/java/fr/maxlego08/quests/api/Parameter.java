package fr.maxlego08.quests.api;

public class Parameter<V> {

    private final String key;
    private final V value;

    public static <V> Parameter<V> of(String key, V value) {
        return new Parameter<>(key, value);
    }

    private Parameter(String key, V value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return this.key;
    }

    public V getValue() {
        return this.value;
    }


}