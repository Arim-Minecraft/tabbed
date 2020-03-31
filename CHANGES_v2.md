There are a number of API changes in tabbed 2.x from the original tabbed.

### Additions

**Support for overriding of existing player tab lists**

Previously, if you created a tablist for a player (with e.g. `tabbed.newSimpleTabList(player)`), and you attempted to create yet another tablist, the operation would fail with an unchecked exception.

In tabbed 2.x, if you create a tablist for a player who already has a tablist, the previous tablist will be overidden and automatically cleaned up.

**Concurrent creation**

The library instance is thread safe. Note, however, that individual tab list objects are not necessarily thread safe themselves.

For example, this allows dependents to use a "thread-per-player" model for creating tab lists. (A thread per player is likely overkill – it would make more sense to have a single thread manage all of your tab lists).

### Breaking Changes

**Tabbed** instances can only be acquired through `Tabbed.get(JavaPlugin)`. This will create an instance for your plugin if it does not exist. There is no more direct instantiation.

**PlayerProvider<T>**, whose `T get(Player)` returns some relevant information for a player, has been changed so that it extends `Function<Player, T>`. The method is thus `T apply(Player)`. For dependents which utilised lambdas in 1.x, a simple recompile will allow 2.x compatibility in this respect.

**Skins** contained many fun/useful skins, skin getters, etc. It allowed fetching a mob skin, dot skins for each chat color, and much more. Many of these have been refactored to `MobSkins`, `DotSkins`, `BlockSkins`, and `DecorSkins`.

**Various internals** which were previously exposed have been hidden, including the Packets class, which is considered an internal, even though the original author considered it a utility.

**PlayerQuitEvent** is no longer listened to by tabbed. This means you have to listen to `PlayerQuitEvent` itself and ensure the player has no more tablists via `tabbed.destroyTabList(player)` or `tabbed.destroyTabList(tabList)`.