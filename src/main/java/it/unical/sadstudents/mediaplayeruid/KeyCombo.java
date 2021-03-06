package it.unical.sadstudents.mediaplayeruid;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

public class KeyCombo {
    public static final KeyCombination skipBack = new KeyCodeCombination(KeyCode.LEFT, KeyCombination.ALT_DOWN);
    public static final KeyCombination skipForward = new KeyCodeCombination(KeyCode.RIGHT, KeyCombination.ALT_DOWN);
    public static final KeyCombination volumeUp = new KeyCodeCombination(KeyCode.UP, KeyCombination.ALT_DOWN);
    public static final KeyCombination volumeDown = new KeyCodeCombination(KeyCode.DOWN, KeyCombination.ALT_DOWN);
    public static final KeyCombination quit = new KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN);

}
