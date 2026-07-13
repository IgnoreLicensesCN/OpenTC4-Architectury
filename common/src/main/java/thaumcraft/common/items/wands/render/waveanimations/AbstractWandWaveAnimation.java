package thaumcraft.common.items.wands.render.waveanimations;

import net.minecraft.world.item.Item;
import thaumcraft.common.lib.resourcelocations.WandWaveAnimationResourceLocation;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//TODO:store render logic here
public abstract class AbstractWandWaveAnimation {
    private static final Map<WandWaveAnimationResourceLocation, AbstractWandWaveAnimation> WAND_WAVE_ANIMATIONS = new ConcurrentHashMap<>();
    public static final Map<WandWaveAnimationResourceLocation, AbstractWandWaveAnimation> WAND_WAVE_ANIMATIONS_VIEW = Collections.unmodifiableMap(WAND_WAVE_ANIMATIONS);

    public final WandWaveAnimationResourceLocation id;
    public AbstractWandWaveAnimation(WandWaveAnimationResourceLocation id) {
        this.id = id;
        if (!WAND_WAVE_ANIMATIONS.containsKey(id)) {
            WAND_WAVE_ANIMATIONS.put(id, this);
        }else {
            throw new IllegalArgumentException("WandWaveAnimationResourceLocation " + id + " already exists");
        }
    }
    public final Map<Item,AbstractWandWaveAnimation> itemAnimationOverrides =  new ConcurrentHashMap<>();
}
