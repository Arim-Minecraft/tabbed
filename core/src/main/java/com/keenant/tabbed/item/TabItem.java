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
     * Attempts to update the text of this tab item. <br>
     * The updateXXX (updateText(), updatePing(), updateSkin()) methods are called
     * when enclosing <code>TabList</code>s desires to check for updates.
     * <code>true</code> indicates a change has been made and the tab list should be updated.
     * 
     * @return true if a change has been made, false otherwise
     */
    boolean updateText();

    /**
     * Attempts to update the ping of this tab item. <br>
     * See {@link #updateText()}
     * 
     * @return true if a change has been made, false otherwise
     */
    boolean updatePing();

    /**
     * Attempts to update the skin of this tab item. <br>
     * See {@link #updateSkin()}
     * 
     * @return true if a change has been made, false otherwise
     */
    boolean updateSkin();

}
