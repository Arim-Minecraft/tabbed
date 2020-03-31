package com.keenant.tabbed.tablist;

import org.bukkit.entity.Player;

/**
 * The highest level of a tab list.
 */
public interface TabList {
	
	/**
	 * Gets the player to whom the tab list is shown
	 * 
	 * @return the player
	 */
    Player getPlayer();
    
    /**
     * Sets the header and footer of the tablist.
     * 
     * @param header the header
     * @param footer the footer
     */
    void setHeaderAndFooter(String header, String footer);
    
    /**
     * Resets the header and footer to blank/empty.
     * 
     */
    void resetHeaderAndFooter();

    /**
     * Enables the tab list, starts any necessary listeners/schedules.
     * 
     * @return The tab list.
     */
    void enable();

    /**
     * Disables the tab list: stops existing listeners/schedules.
     * 
     * @return The tab list.
     */
    void disable();
}
