package thaumcraft.common.tiles.crafted.mirror;

import com.linearity.opentc4.utils.LevelBlockEntityAccessing;
import com.linearity.opentc4.utils.compoundtag.accessors.resourcelocation.ResourceLocationTagAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.tiles.TileThaumcraft;

import static com.linearity.opentc4.Consts.AbstractMirrorBlockEntityTagAccessors.*;
import static com.linearity.opentc4.OpenTC4.platformUtils;
import static com.linearity.opentc4.utils.LevelBlockEntityAccessing.getExistingBlockEntity;
import static com.linearity.opentc4.utils.compoundtag.accessors.mc.BlockPosAccessor.NULL_POS_TO_WRITE;

public class AbstractMirrorBlockEntity extends TileThaumcraft {
    protected int tickCount = System.identityHashCode(this) & 63;
    protected int inc = 40;

    public AbstractMirrorBlockEntity(BlockEntityType<? extends AbstractMirrorBlockEntity> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }
    public @Nullable BlockPos linkedPos = null;
    public @Nullable ResourceKey<Level> linkedDim = null;

    @Override
    public void readCustomNBT(CompoundTag compoundTag) {
        super.readCustomNBT(compoundTag);
        linkedPos = LINKED_POS.readFromCompoundTag(compoundTag);
        if (linkedPos == NULL_POS_TO_WRITE){
            linkedDim = null;
        }
        readDimFromTag(compoundTag);
    }
    protected void readDimFromTag(CompoundTag compoundTag) {
        var dimResLoc = LINKED_DIM.readFromCompoundTag(compoundTag);

        platformUtils.getServer().registryAccess()//sorry guys i have to use server registry not level(although they should be same in usual)
                .registry(Registries.DIMENSION).ifPresent(dimRegistry -> {
                    var level = dimRegistry.get(dimResLoc);
                    if (level != null) {
                        linkedDim = level.dimension();
                    }
                });
    }

    @Override
    public void writeCustomNBT(CompoundTag compoundTag) {
        super.writeCustomNBT(compoundTag);
        LINKED_POS.writeToCompoundTag(compoundTag,linkedPos != null ? linkedPos : NULL_POS_TO_WRITE);
        LINKED_DIM.writeToCompoundTag(compoundTag,linkedDim != null ? linkedDim.location() : ResourceLocationTagAccessor.EMPTY);
    }

    @Override
    public void setLevel(Level level) {
        super.setLevel(level);
        if (this.level != null) {
            setLinkedInfoForLinked();
        }
    }

    protected void setLinkedInfoForLinked(){
        var mirror = getValidLinkedMirror();
        if (mirror != null && mirror.checkHasValidLinkTo(this)) {
            mirror.linkedPos = getBlockPos();
            assert this.level != null;
            mirror.linkedDim = this.level.dimension();
            mirror.markDirtyAndUpdateSelf();
        }
    }
    protected void setUnlinkedInfoForLinked(){
        var mirror = getValidLinkedMirror();
        if (mirror != null) {
            mirror.linkedPos = null;
            mirror.linkedDim = null;
            mirror.markDirtyAndUpdateSelf();
        }
        this.linkedDim = null;
        this.linkedPos = null;
    }

    public @Nullable AbstractMirrorBlockEntity getValidLinkedMirror(){
        if (linkedPos == null || linkedDim == null || this.level == null) {
            return null;
        }
        var registry = this.level.registryAccess()
                .registry(Registries.DIMENSION);
        if (registry.isEmpty()){
            return null;
        }
        var linkedToLevel = registry.get().get(this.linkedDim);
        if (linkedToLevel == null) {
            return null;
        }
        if (!(LevelBlockEntityAccessing.getExistingBlockEntity(linkedToLevel, this.linkedPos) instanceof AbstractMirrorBlockEntity mirror)) {
            return null;
        }
        return mirror;
    }

    //current has link now
    public boolean checkHasValidLinkTo(){
        var mirror = getValidLinkedMirror();
        if (mirror == null) {
            return false;
        }
        return checkHasValidLinkTo(mirror);
    }
    public boolean checkHasValidLinkTo(AbstractMirrorBlockEntity anotherMirror) {
        if (this.level == null){
            return false;
        }
        return anotherMirror.linkedPos == this.getBlockPos()
                && anotherMirror.linkedDim == this.level.dimension();
    }
    public void blockOnRemoved(){
        setUnlinkedInfoForLinked();
        onDropStack();
    }
    protected void onDropStack(){
        if (this.level == null) {
            return;
        }
        var pos = getBlockPos();
        Containers.dropItemStack(this.level,pos.getX(),pos.getY(),pos.getZ(),getDroppedSelfStack());
    }
    public void writeLinkedToTag(@NotNull CompoundTag tag){
        if (this.linkedDim != null && this.linkedPos != null) {
            LINKED_POS.writeToCompoundTag(tag,this.linkedPos);
            LINKED_DIM.writeToCompoundTag(tag,this.linkedDim.location());
        }
    }
    public void readLinkedFromTag(@NotNull CompoundTag tag){
        this.linkedPos = LINKED_POS.readFromCompoundTag(tag);
        readDimFromTag(tag);
    }
    protected ItemStack getDroppedSelfStack(){
        var stack = new ItemStack(getBlockState().getBlock().asItem());
        if (this.linkedDim != null && this.linkedPos != null) {
            writeLinkedToTag(stack.getOrCreateTag());
        }
        return stack;
    }

    public void serverTick(){
        this.tickCount += 1;
        if (this.tickCount % this.inc == 0) {
            if (!this.checkHasValidLinkTo()) {
                if (this.inc < 600) {
                    this.inc += 20;
                }

                this.setLinkedInfoForLinked();
            } else {
                this.inc = 40;
            }
        }
    }
}
