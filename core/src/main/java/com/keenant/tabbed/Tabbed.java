package com.keenant.tabbed;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.keenant.tabbed.tablist.DefaultTabList;
import com.keenant.tabbed.tablist.SimpleTabList;
import com.keenant.tabbed.tablist.TabList;
import com.keenant.tabbed.tablist.TableTabList;
import com.keenant.tabbed.tablist.TitledTabList;

import lombok.Getter;
import lombok.Setter;

public class Tabbed {
    private static final ConcurrentHashMap<Plugin,Tabbed> instances = new ConcurrentHashMap<>();
    @Getter @Setter static Level logLevel = Level.WARNING;

    /**
     * The plugin associated with this library instance. <br>
     * This is the same plugin passed to {@link Tabbed#get(JavaPlugin)}.
     * 
     */
    @Getter
    private final Plugin plugin;
    private final ConcurrentHashMap<UUID,TabList> tabLists = new ConcurrentHashMap<>();

    private Tabbed(Plugin plugin) {
        this.plugin = plugin;
    }

    public static void log(Level level, String message) {
        if (level.intValue() >= logLevel.intValue())
            System.out.println("[" + level.getName() + "] " + message);
    }

    /**
     * Gets an instance of Tabbed from a plugin. <br>
     * Creates an instance if it does not exist.
     * 
     * @param plugin the plugin to use
     * @return the tabbed instance
     */
    public static Tabbed get(JavaPlugin plugin) {
        return instances.computeIfAbsent(plugin, Tabbed::new);
    }
    
    /**
     * Checks if an instance exists. This will not be necessary
     * in most cases because {@link #get(Plugin)} will create an instance
     * automatically if it does not.
     * 
     * @param plugin the plugin to use
     * @return whether the instance exists
     */
    public static boolean hasInstance(Plugin plugin) {
    	return instances.containsKey(plugin);
    }
    
    /**
     * Get the current tab list of the player.
     * @param player
     * @return The tab list, or null if it wasn't present.
     */
    public TabList getTabList(Player player) {
        return this.tabLists.get(player.getUniqueId());
    }

    /**
     * Disables the tab list of a player.
     * @param player
     * @return The tab list removed (or null if it wasn't present).
     */
    public TabList destroyTabList(Player player) {
    	TabList tabList = this.tabLists.remove(player.getUniqueId());
    	if (tabList != null) {
    		tabList.disable();
    	}
    	return tabList;
    }

    /**
     * Disables a tab list.
     * @param tabList
     * @return The tab list removed.
     */
    public TabList destroyTabList(TabList tabList) {
        return destroyTabList(tabList.getPlayer());
    }

    /**
     * Creates a new TitledTabList with the given parameters.
     * @param player
     * @return
     */
    public TitledTabList newTitledTabList(Player player) {
        return put(player, new TitledTabList(player));
    }

    /**
     * Creates a new DefaultTabList.
     * @param player
     * @return
     */
    public DefaultTabList newDefaultTabList(Player player) {
        return put(player, new DefaultTabList(this, player, -1));
    }

    /**
     * Creates a new CustomTabList with the given parameters.
     * @param player
     * @return
     */
    public SimpleTabList newSimpleTabList(Player player) {
        return newSimpleTabList(player, SimpleTabList.MAXIMUM_ITEMS);
    }

    /**
     * Creates a new CustomTabList with the given parameters.
     * @param player
     * @param maxItems
     * @return
     */
    public SimpleTabList newSimpleTabList(Player player, int maxItems) {
        return newSimpleTabList(player, maxItems, -1);
    }

    /**
     * Creates a new CustomTabList with the given parameters.
     * @param player
     * @param maxItems
     * @param minColumnWidth
     * @return
     */
    public SimpleTabList newSimpleTabList(Player player, int maxItems, int minColumnWidth) {
        return newSimpleTabList(player, maxItems, minColumnWidth, -1);
    }

    /**
     * Creates a new CustomTabList with the given parameters.
     * @param player
     * @param maxItems
     * @param minColumnWidth
     * @param maxColumnWidth
     * @return
     */
    public SimpleTabList newSimpleTabList(Player player, int maxItems, int minColumnWidth, int maxColumnWidth) {
        return put(player, new SimpleTabList(this, player, maxItems, minColumnWidth, maxColumnWidth));
    }

    /**
     * Creates a new TableTabList with the given parameters.
     * @param player
     * @return
     */
    public TableTabList newTableTabList(Player player) {
        return newTableTabList(player, 4);
    }

    /**
     * Creates a new TableTabList with the given parameters.
     * @param player
     * @param columns
     * @return
     */
    public TableTabList newTableTabList(Player player, int columns) {
        return newTableTabList(player, columns, -1);
    }

    /**
     * Creates a new TableTabList with the given parameters.
     * @param player
     * @param columns
     * @param minColumnWidth
     * @return
     */
    public TableTabList newTableTabList(Player player, int columns, int minColumnWidth) {
        return newTableTabList(player, columns, minColumnWidth, -1);
    }

    /**
     * Creates a new TableTabList with the given parameters.
     * @param player
     * @param columns
     * @param minColumnWidth
     * @param maxColumnWidth
     * @return
     */
    public TableTabList newTableTabList(Player player, int columns, int minColumnWidth, int maxColumnWidth) {
        return put(player, new TableTabList(this, player, columns, minColumnWidth, maxColumnWidth));
    }
    
    /**
     * Creates a TabList of arbitrary type and pairs it with the player. <br>
     * This is provided for convenience, so that {@link #getTabList(Player)} and
     * {@link #destroyTabList(Player)} may recognise custom tab list implementations.
     * 
     * @param <T> the type of the tab list
     * @param player the player
     * @param tabList the tab list constructed
     * @return false if the player already had a tablist and it was the same tablist, true otherwise
     */
    public <T extends TabList> boolean newArbitraryTabList(Player player, T tabList) {
    	TabList previous = this.tabLists.put(player.getUniqueId(), tabList);
    	if (!tabList.equals(previous)) {
    		if (previous != null) {
    			previous.disable(); // disable the previous before enabling the current
    		}
    		tabList.enable();
    		return true;
    	}
    	return false;
    }
    
    private <T extends TabList> T put(Player player, T tabList) {
    	TabList previous = this.tabLists.put(player.getUniqueId(), tabList);
    	if (previous != null) {
			previous.disable(); // disable the previous before enabling the current
		}
		tabList.enable();
        return tabList;
    }
}
