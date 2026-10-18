package com.linearity.opentc4.utils.compoundtag.accessors.mc;

import com.linearity.opentc4.mixinaccessors.IAccessorCacheableTag;
import com.linearity.opentc4.utils.compoundtag.accessors.CompoundTagAccessor;
import com.linearity.opentc4.utils.compoundtag.accessors.basic.CompoundTagAccessorImpl;
import net.minecraft.core.HolderGetter;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class BlockStateAccessor extends CompoundTagAccessor<BlockState> {
    private final CompoundTagAccessorImpl accessor;
    public BlockStateAccessor(String tagKey) {
        super(tagKey);
        this.accessor = new CompoundTagAccessorImpl(tagKey + "_blockstate");
    }

    @Override
    @Deprecated(forRemoval = true)
    public BlockState readFromCompoundTag(CompoundTag tag) {
        throw new UnsupportedOperationException("need blockHolderGetter");
    }

    public BlockState readFromCompoundTagWithHolderGetter(HolderGetter<Block> blockHolderGetter, CompoundTag tag) {
        if (tag instanceof IAccessorCacheableTag cacheableTag) {
            var result = cacheableTag.opentc4$getTagCache(this);
            if (result == null) {
                result = NbtUtils.readBlockState(blockHolderGetter,accessor.readFromCompoundTag(tag));
                cacheableTag.opentc4$setTagCache(this,result);
            }
            return result;
        }
        return NbtUtils.readBlockState(blockHolderGetter,accessor.readFromCompoundTag(tag));
    }

    @Override
    public void writeToCompoundTag(CompoundTag tag, BlockState value) {
        accessor.writeToCompoundTag(tag,NbtUtils.writeBlockState(value));
        if (tag instanceof IAccessorCacheableTag cacheableTag){
            cacheableTag.opentc4$setTagCache(this, value);
        }
    }

    @Override
    public boolean compoundTagHasKey(CompoundTag tag) {
        return tag.contains(tagKey);
    }
}
