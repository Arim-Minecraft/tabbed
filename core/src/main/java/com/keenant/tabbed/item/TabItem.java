package com.keenant.tabbed.item;

import com.keenant.tabbed.skin.Skin;

/**
 * Represents a custom tab item.
 */
public interface TabItem {
    /**
     * The text of the tab item (any length, recommended less than ~18). No calculations should be made.
     * 
     * @return the text
     */
    String getText();

    /**
     * The ping of the tab item. No calculations should be made.
     * 
     * @return the ping
     */
    int getPing();

    /**
     * The skin/avatar of the tab item. No calculations should be made.
     * 
     * @return the skin
     */
    Skin getSkin();

    /**
     * Makes any appropriate changes to the text.
     * 
     * @return true if a change has been made, false otherwise
     */
    boolean updateText();

    /**
     * Makes any appropriate changes to the ping.
     * 
     * @return true if a change has been made, false otherwise
     */
    boolean updatePing();

    /**
     * Makes any appropriate changes to the skin.
     * 
     * @return true if a change has been made, false otherwise
     */
    boolean updateSkin();

}
