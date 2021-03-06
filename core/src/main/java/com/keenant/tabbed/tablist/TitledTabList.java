package com.keenant.tabbed.tablist;

import com.comphenix.protocol.PacketType.Play.Server;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import lombok.Getter;
import lombok.ToString;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

/**
 * A very basic tab list. It doesn't modify the items, only the header/footer.
 */
@ToString
public class TitledTabList implements TabList {
    @Getter protected final Player player;
    @Getter private String header;
    @Getter private String footer;
    
    volatile boolean enable;

    public TitledTabList(Player player) {
        this.player = player;
    }

    @Override
    public void enable() {
    	enable = true;
    }

    @Override
    public void disable() {
        enable = false;
    }

    @Override
	public boolean setHeaderAndFooter(String header, String footer) {
        if (!Objects.equals(this.header, header) || !Objects.equals(this.footer, footer)) {
        	this.header = header;
            this.footer = footer;
            updateHeaderFooter();
            return true;
        }
        return false;
    }

    @Override
	public boolean resetHeaderAndFooter() {
    	return setHeaderAndFooter(null, null);
    }

    private void updateHeaderFooter() {
    	if (enable) {
            PacketContainer packet = new PacketContainer(Server.PLAYER_LIST_HEADER_FOOTER);
            packet.getChatComponents().write(0, WrappedChatComponent.fromText(this.header == null ? "" : this.header));
            packet.getChatComponents().write(1, WrappedChatComponent.fromText(this.footer == null ? "" : this.footer));
            try {
                ProtocolLibrary.getProtocolManager().sendServerPacket(this.player, packet);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
    	}
    }
}
