package fr.maxlego08.quests.api.utils;

/**
 * A parameter is a key/value pair. It's used to identify specific information
 * that can be associated with a quest.
 *
 * @param <V> the type of the value
 * @author Maxime LEGRAND (MaxCalc)
 */
public class Parameter<V> {

    private final String key;
    private final V value;

    private Parameter(String key, V value) {
        this.key = key;
        this.value = value;
    }

    public static <V> Parameter<V> of(String key, V value) {
        return new Parameter<>(key, value);
    }

    /**
     * Gets the key of the parameter.
     *
     * @return the key
     */
    public String getKey() {
        return this.key;
    }

    /**
     * Gets the value of the parameter.
     *
     * @return the value
     */
    public V getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return "Parameter{" +
                "key='" + key + '\'' +
                ", value=" + value +
                '}';
    }
}