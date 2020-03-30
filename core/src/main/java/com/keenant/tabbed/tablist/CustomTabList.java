package com.keenant.tabbed.tablist;

import com.keenant.tabbed.item.TabItem;

/**
 * Represents a tab list that can have custom and dynamic TabItem's.
 */
public interface CustomTabList extends TabList {
	
    /**
     * Gets the maximum number of items supported
     * 
     * @return max number of items supported
     */
    int getMaxItems();

    /**
     * Checks for updates to all tab items (text, ping, skin) and then sends packets if necessary.
     * 
     */
    void update();

    /**
     * Checks for updates to a specific tab item (text, ping, skin) and then sends packets if necessary.
     * 
     * @param index the index of the tab item
     */
    void update(int index);

    /**
     * Gets the item at the given index.
     * 
     * @param index the index
     * @return The item or null if it isn't present.
     */
    TabItem get(int index);

    /**
     * Checks if the tab list contains an item at the given index.
     * 
     * @param index
     * @return true if it contains an item, false otherwise
     */
    boolean contains(int index);

    /**
     * Adds an item to the next available index.
     * 
     * @param item the item to add
     */
    void add(TabItem item);

    /**
     * Inserts an item at the specified index.
     * 
     * @param index the index at which to insert
     * @param item the item to add
     */
    void add(int index, TabItem item);

    /**
     * Replaces an item at the given index.
     * 
     * @param index the index at which to set
     * @param item the item to set
     * @return
     */
    TabItem set(int index, TabItem item);

    /**
     * Removes the item that resides at the given index.
     * 
     * @param index
     * @return The item removed or null if it isn't present.
     */
    TabItem remove(int index);

    /**
     * Removes the given item from the tab list.
     * @param item
     * @param <T>
     * @return
     */
    <T extends TabItem> T remove(T item);

    /**
     * Get the next available index.
     * @return The index or -1 if the list is full.
     */
    int getNextIndex();
}
