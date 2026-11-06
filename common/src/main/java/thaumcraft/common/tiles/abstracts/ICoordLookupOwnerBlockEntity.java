package thaumcraft.common.tiles.abstracts;

import com.linearity.opentc4.annotations.UtilityLikeAbstraction;
import com.linearity.opentc4.utils.collectionlike.coordlookups.CoordLookup;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@UtilityLikeAbstraction(reason = "registerNodeType something to my weak lookup")
public interface ICoordLookupOwnerBlockEntity<StoredItemClass extends ICoordLookupOwnerBlockEntity<StoredItemClass>> {
    //call before BlockEntity(.super.)#setLevel
    default void registerToCubeLookup(@Nullable Level level, @Nullable Level levelBefore){
        unregisterFromWeakLookup(levelBefore);
        getSelfLookupInLevel(level).store(getBlockPos(), getStoreItemForLookup());
    }
    //call before BlockEntity(.super.).setRemoved
    default void unregisterFromWeakLookup(@Nullable Level levelToUnregister){
        if (levelToUnregister == null){
            return;
        }
        getSelfLookupInLevel(levelToUnregister).remove(getBlockPos(),getStoreItemForLookup());
    }
    @NotNull CoordLookup<StoredItemClass> getSelfLookupInLevel(Level level);
    @NotNull StoredItemClass getStoreItemForLookup();
    BlockPos getBlockPos();
}
