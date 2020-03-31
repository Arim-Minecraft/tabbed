package com.keenant.tabbed.item;

import com.keenant.tabbed.skin.Skin;
import com.keenant.tabbed.skin.Skins;

import lombok.ToString;

/**
 * A blank TextTabItem
 */
@ToString
public class BlankTabItem extends TextTabItem {
    public BlankTabItem(Skin skin) {
        super("", 1000, skin);
    }

    public BlankTabItem() {
        this(Skins.getDefault());
    }
}
