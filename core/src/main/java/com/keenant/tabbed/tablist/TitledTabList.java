package com.keenant.tabbed.tablist;

import com.comphenix.protocol.PacketType.Play.Server;

import com.google.common.base.Objects;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import lombok.Getter;
import lombok.ToString;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;

/**
 * A very basic tab list. It doesn't modify the items, only the header/footer.
 */
@ToString
public class TitledTabList implements TabList {
    @Getter protected final Player player;
    @Getter private String header;
    @Getter private String footer;

    public TitledTabList(Player player) {
        this.player = player;
    }

    @Override
    public void enable() {

    }

    @Override
    public void disable() {
        resetHeaderAndFooter();
    }

    @Override
	public void setHeaderAndFooter(String header, String footer) {
        if (!Objects.equal(this.header, header) || !Objects.equal(this.footer, footer)) {
        	this.header = header;
            this.footer = footer;
            updateHeaderFooter();
        }
    }

    @Override
	public void resetHeaderAndFooter() {
    	setHeaderAndFooter(null, null);
    }

    private void updateHeaderFooter() {
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
