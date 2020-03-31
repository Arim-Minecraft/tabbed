This is a fork of the original tabbed to improve features and performance. There are some API changes from the original API.

# Tabbed

## Introduction

Tabbed is an API for configuring the tablist for your users. Each slot is configurable: set the icon, ping, and text
for each tab list item to your liking.

### Requirements

**Spigot** or any derivatives (e.g. Paper) should work. Tabbed supports 1.8.8-1.15.2. It may work on other versions, for which support is not guaranteed.

**ProtocolLib** is required, see [here](https://www.spigotmc.org/resources/protocollib.1997/) for download. Tabbed is built against 4.4.0 of ProtocolLib, but newer versions will probably work (so long as they have no breaking changes).

**Java 8** (or higher) is required.

### Demonstration

![http://i.imgur.com/hbO4Nbq.gif](http://i.imgur.com/hbO4Nbq.gif)

## Usage

### Dependency and Repository

**Dependency:**

```
<dependency>
	<groupId>space.arim.tabbed</groupId>
	<artifactId>tabbed-core</artifactId>
	<version>${INSERT_LATEST_VERSION}</version>
</dependency>
```

The latest version may be found in the *pom.xml* of this git repository.

**Repository:**

```xml
<repository>
	<id>arim-repo</id>
	<url>https://dl.cloudsmith.io/public/anand-beh/arim-repo/maven/</url>
</repository>
```

### Upgrading from the original tabbed

See CHANGES_v2.md for a full description of all additions and breaking changes from 1.x.

## API

### Creating a library instance

Get an instance of Tabbed via `Tabbed#get(JavaPlugin)`. If the instance for your plugin does not exist, it will be created. You do not need to worry about accidentally creating another instance.
```java
Tabbed tabbed = Tabbed.get(this); // e.g., if in plugin main class
```

### Before You Start

* Tab lists are associated per player.
	* I recommend you create a tab list every time a user joins (`PlayerJoinEvent` works fine).
	* If a player already has a tab list, and you create a new one, the old one is removed and discarded.
	* You can get a player's tab list with `tabbed.getTabList(player)` (will return null if it does not exist).
	* Tab lists must be removed when the player leaves. You must therefore listen to `PlayerQuitEvent` and remove a player's tab list with `tabbed.destroyTabList(player)` or `tabbed.destroyTabList(tab)`.

* Tabbed does some things automatically, so you don't have to:
	* Tab lists with dynamic elements are checked for changes and, if a change is found, updated periodically.
	* Packets are only sent when required; tabbed won't bother to send a packet if it doesn't change the tab list display.

### TabLists

**TitledTabList**

A basic implementation of TabList with support for changing headers and footers only.

```java
TitledTabList tablist = tabbed.newTitledTabList(player);

tablist.setHeaderFooter("The tab list header!", "The tab list footer :O");

tablist.resetHeaderFooter(); // reset

// Getters - you probably won't need these, but they're here nonetheless
String header = tablist.getHeader();
String footer = tablist.getFooter();
```

**TableTabList**

This tablist behaves like a table with a specified number of columns and rows. You can set specific items at a column and row. Cells that don't have anything set are automatically filled with BlankTabItem's.

```java
// Creation
TableTabList tablist;
tablist = tabbed.newTableTabList(player); // columns = 4
tablist = tabbed.newTableTabList(player, columns); // min width = -1
tablist = tabbed.newTableTabList(player, columns, minColumnWidth); // max width = -1
tablist = tabbed.newTableTabList(player, columns, minColumnWidth, maxColumnWidth);

tablist.set(col, row, item);
tablist.set(0, 0, item); // top left
tablist.set(new TableCell(0, 0), item); // same as previous, using TableCell wrapper for readability

TabItem item = tablist.get(0, 0);
TabItem item = tablist.get(new TableCell(0, 0)); // again using TableCell

tab.remove(0, 0);
tab.remove(new TableCell(0,0)); // still using TableCell

// Fill a box
List<TabItem> items = new ArrayList<TabItem>();
items.add(new TextTabItem("This will be at 0,0"));
items.add(new TextTabItem("This will be at 1,0"));
items.add(new TextTabItem("This will be at 0,1"));
items.add(new TextTabItem("This will be at 1,1"));
tablist.fill(0, 0, 1, 1, items, TableCorner.TOP_LEFT, FillDirection.HORIZONTAL);
```

**SimpleTabList**

This behaves similarly to how the normal tablist behaves. You can simply add or remove items and Minecraft handles the positioning.

```java
SimpleTabList tablist;
tablist = tabbed.newSimpleTabList(player);
tablist = tabbed.newSimpleTabList(player, maxItems); // limits item count (default is MC maximum, aka 80 or 4x20)
tablist = tabbed.newSimpleTabList(player, maxItems, minColumnWidth); // add spaces to items until min width
tablist = tabbed.newSimpleTabList(player, maxItems, minColumnWidth, maxColumnWidth); // remove characters until max width

tablist.add(item);
tablist.add(0, item); // inserts to first position and shifts over other elements
tablist.set(0, item); // removes item at index 0, inserts this one
tablist.get(4); // gets the item at index 4
```

**DefaultTabList**

This is just an example of how to implement your own custom tablist. It appears identical to vanilla Minecraft. There's really no reason you should use this.

```java
DefaultTabList tab = tabbed.newDefaultTabList(player);
```

### Tab Items

**Simple usage**

```java
new TextTabItem("Custom text!"); // basic text entry
new BlankTabItem(); // empty entry 
new PlayerTabItem(player);
```

**More complex usage**

```java
// Uses the Bukkit API's recognised player tab list name.
// This is what Bukkit does by default.
new PlayerTabItem(player, Player::getPlayerListName);
// No functional differences from previous, we're just explicitly specifying
// how to fetch the player's skin. This is again what Bukkit does by default.
new PlayerTabItem(player, Player::getPlayerListName, Skins::getPlayer);

// Uses the player's display name, according to Bukkit, as their tab list name
// Note that a tab list name and a display name are completely different
new PlayerTabItem(player, Player::getDisplayName);
// Same as previous, but we are overriding the player's skin
// to use a red dot for their skin
new PlayerTabItem(player, Player::getDisplayName, DotSkins.getDot(ChatColor.RED));

// Uses a custom variable for the tab list name shown
new PlayerTabItem(player, (player) -> {
	return myPlugin.getSomeVar(player);
});

// TextTabItem
new TextTabItem("Some text!", 1000); // ping = 1,000 in this case
new TextTabItem("Some text!", 1000, Skins.DEFAULT_SKIN); // explicitly using the default skin

new TextTabItem("Red skin :D", 0, DotSkins.getDot(ChatColor.RED));
new TextTabItem("Yellow skin :O", 0, DotSkins.getDot(ChatColor.YELLOW));
new TextTabItem("An Enderman!", 0, MobSkins.getMob(EntityType.ENDERMAN));

// BlankTabItem
new BlankTabItem(DotSkins.getDot(ChatColor.RED); // an empty entry using a red dot skin
```

### Batch updating

Tabbed sends packets only when it is necessary: it determines whether there are differences between what the client currently sees and what would be sent.

Tabbed doesn't know, on the other hand, when you are sending a bunch of new tab items in a row. For example if you have a loop like:
```java
int i = 0;
for (Player player : Bukkit.getOnlinePlayers()) {
    tabbed.set(i, new PlayerTabItem(player));
    i++;
}
```
It will send up to `2 * Bukkit.getOnlinePlayers().length` packets to the player (update name + ping). This might cause some blinking for the client. It is smarter to batch send these packets and reduce it to a maximum of `4` packets sent like so:

```java
tabbed.setBatchUpdate(true);
int i = 0;
for (Player player : Bukkit.getOnlinePlayers()) {
    tabbed.set(i, new PlayerTabItem(player));
}
tabbed.batchUpdate(); // sends the packets!
tabbed.setBatchUpdate(false); // optional
```
I highly recommend using batch updating. It's simply much more efficient. We strive for perfection, even if we never get there.
