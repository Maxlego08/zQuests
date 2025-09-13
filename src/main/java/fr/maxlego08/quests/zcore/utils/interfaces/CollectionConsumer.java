package fr.maxlego08.quests.zcore.utils.interfaces;

import java.util.Collection;

@FunctionalInterface
public interface CollectionConsumer<T> {

    /**
     * Processes the given input of type T and returns a collection of strings.
     *
     * @param t the input to be processed
     * @return a collection of strings as the result of processing the input
     */
    Collection<String> accept(T t);

}
