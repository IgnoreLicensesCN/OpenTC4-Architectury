package thaumcraft.common.tiles.abstracts;

import com.linearity.opentc4.annotations.UtilityLikeAbstraction;
import com.linearity.opentc4.utils.collectionlike.CubeChunkedWeakLookups;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

@UtilityLikeAbstraction(reason = "register something to my weak lookup")
public interface ICubeChunkBasedWeakLookupOwner<StoredItemClass extends ICubeChunkBasedWeakLookupOwner<StoredItemClass>> {
    //call before BlockEntity(.super.)#setLevel
    default void registerToCubeLookup(@Nullable Level level, @Nullable Level levelBefore){
        unregisterFromWeakLookup(levelBefore);
        var levelledMap = getSelfLookupMap();
        if (level != null && levelledMap != null) {
            getSelfLookupMap()
                    .computeIfAbsent(level, _ignored -> new CubeChunkedWeakLookups<>(getChunkSizeBits()))
                    .store(getBlockPos(), getStoreItemForLookup());
        }
    }
    //call before BlockEntity(.super.).setRemoved
    default void unregisterFromWeakLookup(@Nullable Level levelToUnregister){
        var levelledMap = getSelfLookupMap();
        if (levelToUnregister == null || levelledMap == null){
            return;
        }
        var levelledChunkCache = levelledMap.get(levelToUnregister);
        if (levelledChunkCache != null){
            (levelledChunkCache).remove(getBlockPos(), getStoreItemForLookup());
        }
    }
    @Nullable Map<Level, CubeChunkedWeakLookups<StoredItemClass>> getSelfLookupMap();
    @NotNull StoredItemClass getStoreItemForLookup();
    default byte getChunkSizeBits(){
        return (byte) 4;
    }
    BlockPos getBlockPos();

}
