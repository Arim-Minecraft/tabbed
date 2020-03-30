package com.keenant.tabbed.util;

import java.lang.reflect.InvocationTargetException;

import org.bukkit.entity.Player;

import space.arim.api.platform.spigot.nms.NMS;

/**
 * Reflection util.
 */
public class Reflection {
    
    public static int getPing(Player player) {
    	try {
			return NMS.getPing(player);
		} catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException ex) {
			ex.printStackTrace();
			return 0;
		}
    }
    
}
