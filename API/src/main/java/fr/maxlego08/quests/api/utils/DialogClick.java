package fr.maxlego08.quests.api.utils;

/**
 * A click on a button of a zMenu dialog.
 *
 * @param dialog the file name of the dialog, without the {@code .yml} extension
 * @param button the configuration key of the clicked button ({@code button1}, {@code yes}, {@code no}, ...)
 */
public record DialogClick(String dialog, String button) {
}
