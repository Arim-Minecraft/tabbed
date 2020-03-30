package com.keenant.tabbed.tablist;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

class DefaultTabListListener implements Listener {

	private final DefaultTabList tabList;
	
	DefaultTabListListener(DefaultTabList tabList) {
		this.tabList = tabList;
	}
	
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        tabList.addPlayer(event.getPlayer());
    }

    @EventHandler
    public void onPlayerJoin(PlayerQuitEvent event) {
        tabList.remove(tabList.getTabItemIndex(event.getPlayer()));
    }
	
}
