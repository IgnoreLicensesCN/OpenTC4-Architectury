package thaumcraft.common.tiles.crafted.mirror;

import com.linearity.opentc4.utils.NonNullListWithConstructor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.visnet.VisNetHandler;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;

import java.util.ArrayList;

import static com.linearity.opentc4.Consts.MirrorBlockEntityTagAccessors.*;
import static thaumcraft.common.blocks.crafted.mirror.AbstractMirrorBlock.FACING;

public class MirrorBlockEntity extends AbstractMirrorBlockEntity {
    public MirrorBlockEntity(BlockEntityType<? extends MirrorBlockEntity> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }
    public MirrorBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(ThaumcraftBlockEntities.BlockEntityTypeInstances.MIRROR, blockPos, blockState);
    }
    public @NotNull NonNullList<ItemStack> storedItems = new NonNullListWithConstructor<>(new ArrayList<>(),ItemStack.EMPTY);
    public int instability = 0;

    @Override
    public void readCustomNBT(CompoundTag compoundTag) {
        super.readCustomNBT(compoundTag);
        storedItems = new NonNullListWithConstructor<>(STORED_ITEMS_ACCESSOR.readFromCompoundTag(compoundTag),ItemStack.EMPTY);
        instability = INSTABILITY.readIntFromCompoundTag(compoundTag);
    }

    @Override
    public void writeCustomNBT(CompoundTag compoundTag) {
        super.writeCustomNBT(compoundTag);
        STORED_ITEMS_ACCESSOR.writeToCompoundTag(compoundTag,storedItems);
        INSTABILITY.writeIntToCompoundTag(compoundTag,instability);
    }


    public void transportItemEntity(ItemEntity itemEntity) {
        var stack = itemEntity.getItem();
        var targetedAbstractMirror = getValidLinkedMirror();
        if (targetedAbstractMirror instanceof MirrorBlockEntity targetedMirror
                && targetedMirror.checkHasValidLinkTo(this)
                && this.checkHasValidLinkTo(targetedMirror)
        ) {
            targetedMirror.addStack(stack);
            this.addSelfAndLinkedInstability(stack.getCount());
            itemEntity.remove(Entity.RemovalReason.DISCARDED);
            this.setChanged();
            targetedMirror.setChanged();
            if (this.level != null){
                this.level.blockEvent(getBlockPos(), getBlockState().getBlock(), 1, 0);
            }
        }
    }
    public void eject() {
        if (this.level == null) {return;}
        if (!this.storedItems.isEmpty() && this.tickCount > 20) {
//            int i = this.level.random.nextInt(this.storedItems.size());
            var firstStack = this.storedItems.getFirst();
            if (firstStack.isEmpty()) {
                this.storedItems.removeFirst();
                return;
            }
            var outStack = firstStack.copy();
            outStack.setCount(1);
            if (this.spawnItem(outStack)) {
                firstStack.setCount(firstStack.getCount() - 1);
                this.addSelfAndLinkedInstability(1);//TODO:[maybe wont finished]azanor did something strange?Vote in democracy to decide if change to "addSelfInstability"
                this.level.blockEvent(getBlockPos(), ConfigBlocks.blockMirror, 1, 0);
                if (firstStack.isEmpty()) {
                    this.storedItems.removeFirst();
                }
                this.setChanged();
            }
        }
    }
    //"EnumMap"
    public static final Vec3[] SPAWN_ITEM_DELTA_MOVEMENT = new Vec3[Direction.values().length];
    static {
        for (int i=0;i<SPAWN_ITEM_DELTA_MOVEMENT.length;i++){
            SPAWN_ITEM_DELTA_MOVEMENT[i] = new Vec3(Direction.values()[i].step()).multiply(0.15F,0.15F,0.15F);
        }
    }
    public static final Vec3[] SPAWN_ITEM_POS_OFFSET = new Vec3[Direction.values().length];
    static {
        for (int i=0;i<SPAWN_ITEM_POS_OFFSET.length;i++){
            SPAWN_ITEM_POS_OFFSET[i] = new Vec3(Direction.values()[i].step()).multiply(-.3F,-.3F,-.3F);
        }
    }
    protected boolean spawnItem(ItemStack stack) {
        if (this.level == null){
            return false;
        }
        Direction face = getBlockState().getValue(FACING);
        var entityPos = getBlockPos().getCenter()
                .add(SPAWN_ITEM_POS_OFFSET[face.ordinal()]);
        ItemEntity toDrop = new ItemEntity(
                this.level,
                entityPos.x,entityPos.y,entityPos.z, stack);
        toDrop.setDeltaMovement(SPAWN_ITEM_DELTA_MOVEMENT[face.ordinal()]);
        toDrop.setPortalCooldown(20);
        this.level.addFreshEntity(toDrop);
        return true;
    }

    public void addStack(ItemStack stack) {
        this.storedItems.add(stack);
    }

    @Override
    protected void onDropStack(){
        super.onDropStack();
        if (this.level == null) {
            return;
        }
        var pos = getBlockPos();
        Containers.dropContents(this.level,pos,this.storedItems);
    }

    public void checkInstability() {
        if (this.instability > 0 && this.tickCount % 20 == 0) {
            --this.instability;
            this.setChanged();
        }
        if (this.instability > 0) {
            int amt = VisNetHandler.drainCentiVis(this.level, getBlockPos(), Aspects.ORDER, Math.min(this.instability, 1));
            if (amt > 0) {
                addSelfAndLinkedInstability(-amt);
            }
        }
    }

    public void addSelfInstability(int amount){
        this.instability += amount;
        if (this.instability < 0){
            this.instability = 0;
        }
    }
    protected void addSelfAndLinkedInstability(int amt) {
        addSelfInstability(amt);
        var linkedToMirror = getValidLinkedMirror();
        if (linkedToMirror instanceof MirrorBlockEntity linkedMirror) {
            linkedMirror.addSelfInstability(amt);
        }

    }
    public void serverTick(){
        super.serverTick();
        this.checkInstability();
        int tickRate = this.instability / 50;
        if (tickRate == 0
                || this.tickCount % (tickRate * tickRate) == 0) {
            this.eject();
        }
    }
}
