package com.keenant.tabbed.skin;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.regex.Pattern;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.google.common.base.Charsets;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.io.CharStreams;

import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.comphenix.protocol.wrappers.WrappedSignedProperty;

import com.keenant.tabbed.Tabbed;

/**
 * Some Skin utils.
 */
public class Skins {
    
    private static final Skin DEFAULT_SKIN;

    private static final String profileUrl = "https://sessionserver.mojang.com/session/minecraft/profile/";
    private static final LoadingCache<String, String> profileCache;
    
    private static final Pattern UUID_DASH_PATTERN = Pattern.compile("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})");

    static {
        DEFAULT_SKIN = new Skin("eyJ0aW1lc3RhbXAiOjE0MTEyNjg3OTI3NjUsInByb2ZpbGVJZCI6IjNmYmVjN2RkMGE1ZjQwYmY5ZDExODg1YTU0NTA3MTEyIiwicHJvZmlsZU5hbWUiOiJsYXN0X3VzZXJuYW1lIiwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzg0N2I1Mjc5OTg0NjUxNTRhZDZjMjM4YTFlM2MyZGQzZTMyOTY1MzUyZTNhNjRmMzZlMTZhOTQwNWFiOCJ9fX0=", "u8sG8tlbmiekrfAdQjy4nXIcCfNdnUZzXSx9BE1X5K27NiUvE1dDNIeBBSPdZzQG1kHGijuokuHPdNi/KXHZkQM7OJ4aCu5JiUoOY28uz3wZhW4D+KG3dH4ei5ww2KwvjcqVL7LFKfr/ONU5Hvi7MIIty1eKpoGDYpWj3WjnbN4ye5Zo88I2ZEkP1wBw2eDDN4P3YEDYTumQndcbXFPuRRTntoGdZq3N5EBKfDZxlw4L3pgkcSLU5rWkd5UH4ZUOHAP/VaJ04mpFLsFXzzdU4xNZ5fthCwxwVBNLtHRWO26k/qcVBzvEXtKGFJmxfLGCzXScET/OjUBak/JEkkRG2m+kpmBMgFRNtjyZgQ1w08U6HHnLTiAiio3JswPlW5v56pGWRHQT5XWSkfnrXDalxtSmPnB5LmacpIImKgL8V9wLnWvBzI7SHjlyQbbgd+kUOkLlu7+717ySDEJwsFJekfuR6N/rpcYgNZYrxDwe4w57uDPlwNL6cJPfNUHV7WEbIU1pMgxsxaXe8WSvV87qLsR7H06xocl2C0JFfe2jZR4Zh3k9xzEnfCeFKBgGb4lrOWBu1eDWYgtKV67M2Y+B3W5pjuAjwAxn0waODtEn/3jKPbc/sxbPvljUCw65X+ok0UUN1eOwXV5l2EGzn05t3Yhwq19/GxARg63ISGE8CKw=");

        profileCache = CacheBuilder.newBuilder().maximumSize(500).expireAfterWrite(4, TimeUnit.HOURS).build(new CacheLoader<String, String>() {
            @Override
            public String load(String uuid) throws IOException {
                return getProfileText(uuid);
            }
        });
    }
    
    /**
     * Gets the default player skin
     * 
     * @return the default skin
     */
    public static Skin getDefault() {
    	return DEFAULT_SKIN;
    }

    /**
     * Get a skin from an online player.
     * @param player
     * @return
     */
    public static Skin getPlayer(Player player) {
        WrappedSignedProperty property = DEFAULT_SKIN.getProperty();
        Collection<WrappedSignedProperty> properties = WrappedGameProfile.fromPlayer(player).getProperties().get(Skin.TEXTURE_KEY);
        if (properties != null && properties.size() > 0)
            property = properties.iterator().next();
        return new Skin(property);
    }

    /**
     * Get a Minecraft user's skin by their UUID. <br>
     * If the player is not online the skin is fetched from the cache.
     * If the skin is not cached the Mojang API is queried and the skin
     * is added to the cache.
     * 
     * @param uuid the player uuid
     * @return the player's skin
     */
    public static Skin getPlayer(UUID uuid) {
    	Player player = Bukkit.getPlayer(uuid);
    	if (player != null) {
    		return getPlayer(player);
    	}
        try {
            return downloadSkin(uuid.toString().replace("-", ""));
        } catch (Exception e) {
            e.printStackTrace();
            return DEFAULT_SKIN;
        }
    }

    private static Skin downloadSkin(String uuid) {
        uuid = addUuidDashes(uuid);

        WrappedSignedProperty property = null;

        JSONObject json;
        try {
            json = (JSONObject) new JSONParser().parse(profileCache.get(uuid));
        } catch (Exception e) {
            Tabbed.log(Level.SEVERE, "Unable to fetch skin for uuid " + uuid);
            e.printStackTrace();
            return DEFAULT_SKIN;
        }
        JSONArray properties = (JSONArray) json.get("properties");

        for (Object object : properties) {
            JSONObject jsonObject = (JSONObject) object;
            String name = (String) jsonObject.get("name");
            String value = (String) jsonObject.get("value");
            String signature = (String) jsonObject.get("signature");
            if (name.equals(Skin.TEXTURE_KEY))
                property = new WrappedSignedProperty(name, value, signature);
        }

        if (property == null)
            return DEFAULT_SKIN;

        return new Skin(property);
    }

    private static String addUuidDashes(String uuid) {
    	return UUID_DASH_PATTERN.matcher(uuid).replaceAll("$1-$2-$3-$4-$5");
    }

    private static String getProfileText(String uuid) throws IOException {
        uuid = uuid.replace("-", "");

        URL url = new URL(profileUrl + uuid + "?unsigned=false");
        URLConnection connection = url.openConnection();
        return CharStreams.toString(new InputStreamReader(connection.getInputStream(), Charsets.UTF_8));
    }
}
