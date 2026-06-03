package thaumcraft.common.tiles.abstracts;

import com.google.common.collect.MapMaker;
import com.linearity.opentc4.utils.CubeChunkedWeakLookups;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

//oh even more AE2 tricks can be performed like "provide all items needed in a container"
public interface IInfusionComponentStackProvider extends Container {
    static Map<Level, CubeChunkedWeakLookups<IInfusionComponentStackProvider>> INFUSION_COMPONENT_PROVIDERS = new MapMaker().weakKeys().makeMap();
    default void registerComponentStackProvider(Level level, @Nullable Level levelBefore){
        unregisterComponentStackProvider(levelBefore);
        INFUSION_COMPONENT_PROVIDERS.computeIfAbsent(level,_ignored -> new CubeChunkedWeakLookups<>((byte) 4)).store(getBlockPos(),this);
    }
    default void unregisterComponentStackProvider(@Nullable Level levelToUnregister){
        if (levelToUnregister == null){
            return;
        }
        var levelledChunkCache = INFUSION_COMPONENT_PROVIDERS.get(levelToUnregister);
        if (levelledChunkCache != null){
            levelledChunkCache.remove(getBlockPos(),this);
        }
    }
    BlockPos getBlockPos();
}
