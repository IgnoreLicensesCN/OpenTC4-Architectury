package thaumcraft.api.wands;

import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.resourcelocations.WandSpellEventTypeResourceLocation;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


//i wanted enum but maybe other mods wants its type
public class WandSpellEventType {
    public static final WandSpellEventType ON_BLOCK = new WandSpellEventType(WandSpellEventTypeResourceLocation.of(Thaumcraft.MOD_ID,"on_block"));
    public static final WandSpellEventType WAND_FOCUS = new WandSpellEventType(WandSpellEventTypeResourceLocation.of(Thaumcraft.MOD_ID,"wand_focus"));
    public static final WandSpellEventType CRAFTING = new WandSpellEventType(WandSpellEventTypeResourceLocation.of(Thaumcraft.MOD_ID,"crafting"));

    public final WandSpellEventTypeResourceLocation name;
    public WandSpellEventType(WandSpellEventTypeResourceLocation name) {
        this.name = name;
        SPELL_TYPES.put(this.name,this);
    }

    private final Map<WandSpellEventTypeResourceLocation,WandSpellEventType> SPELL_TYPES = new ConcurrentHashMap<>();
    private final Map<WandSpellEventTypeResourceLocation,WandSpellEventType> SPELL_TYPES_VIEW = Collections.unmodifiableMap(SPELL_TYPES);
}
