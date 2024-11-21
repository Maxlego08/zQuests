package fr.maxlego08.quests.api.utils;

import com.google.common.base.Strings;

/**
 * Represents a configuration for a progress bar.
 *
 * @param symbol             the symbol used for completed progress
 * @param notCompletedSymbol the symbol used for not completed progress
 * @param progressColor      the color used for completed progress
 * @param notCompletedColor  the color used for not completed progress
 * @param size               the size of the progress bar
 */
public record ProgressBarConfig(String symbol, String notCompletedSymbol, String progressColor,
                                String notCompletedColor, int size) {

    /**
     * Returns a string representation of the progress bar based on the given current and max values.
     *
     * @param current the current value
     * @param max     the maximum value
     * @return a string representation of the progress bar
     */
    public String getProgressBar(double current, double max) {
        float percent = (float) current / (float) max;
        int progressBars = (int) ((float) size * percent);
        return Strings.repeat(progressColor + symbol, progressBars) + Strings.repeat(notCompletedColor + notCompletedSymbol, size - progressBars);
    }


}