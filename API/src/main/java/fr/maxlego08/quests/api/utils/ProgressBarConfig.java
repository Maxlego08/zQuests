package fr.maxlego08.quests.api.utils;

import com.google.common.base.Strings;

public record ProgressBarConfig(String symbol, String notCompletedSymbol, String progressColor,
                                String notCompletedColor, int size) {

    public String getProgressBar(double current, double max) {
        float percent = (float) current / (float) max;
        int progressBars = (int) ((float) size * percent);
        return Strings.repeat(progressColor + symbol, progressBars) + Strings.repeat(notCompletedColor + notCompletedSymbol, size - progressBars);
    }


}