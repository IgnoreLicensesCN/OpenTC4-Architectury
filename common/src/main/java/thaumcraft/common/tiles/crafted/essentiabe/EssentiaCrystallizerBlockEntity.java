package thaumcraft.common.tiles.crafted.essentiabe;

import com.linearity.colorannotation.annotation.RGBColor;
import com.linearity.opentc4.Color;
import com.linearity.opentc4.mixinaccessors.clientbe.EssentiaCrystallizerBlockEntityClientAccessor;
import com.linearity.opentc4.utils.LevelBlockEntityAccessing;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.IValueContainerBasedComparatorSignalProviderBlockEntity;
import thaumcraft.api.aspects.*;
import thaumcraft.api.aspects.essentiabe.IEssentiaForceInBlockEntity;
import thaumcraft.api.aspects.essentiabe.IEssentiaTransportInBlockEntity;
import thaumcraft.api.aspects.essentiabe.IEssentiaTransportOutBlockEntity;
import thaumcraft.common.tiles.TileThaumcraft;
import thaumcraft.api.visnet.VisNetHandler;
import thaumcraft.common.ClientFXUtils;
import thaumcraft.common.blocks.ThaumcraftBlocks;
import thaumcraft.common.blocks.crafted.essentia.EssentiaCrystallizerBlock;
import thaumcraft.common.lib.utils.InventoryUtils;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;


import static com.linearity.opentc4.Consts.EssentiaCrystallizerBlockEntityTagAccessors.ASPECT_CRYSTALLIZING;
import static com.linearity.opentc4.utils.LevelBlockEntityAccessing.getExistingBlockEntity;
import static thaumcraft.common.items.ThaumcraftItemInstances.CRYSTAL_ESSENCE;

public class EssentiaCrystallizerBlockEntity extends TileThaumcraft
        implements
        IEssentiaTransportInBlockEntity,
        IEssentiaForceInBlockEntity<Aspect>,
        IValueContainerBasedComparatorSignalProviderBlockEntity {
    public EssentiaCrystallizerBlockEntity(BlockEntityType<? extends EssentiaCrystallizerBlockEntity> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }
    public EssentiaCrystallizerBlockEntity( BlockPos blockPos, BlockState blockState) {
        this(ThaumcraftBlockEntities.BlockEntityTypeInstances.ESSENTIA_CRYSTALLIZER(), blockPos, blockState);
    }
    protected @NotNull("null -> empty") Aspect crystallizingAspect = Aspects.EMPTY;

    @Override
    public void readCustomNBT(CompoundTag compoundTag) {
        super.readCustomNBT(compoundTag);
        crystallizingAspect = ASPECT_CRYSTALLIZING.readFromCompoundTag(compoundTag);
    }

    @Override
    public void writeCustomNBT(CompoundTag compoundTag) {
        super.writeCustomNBT(compoundTag);
        ASPECT_CRYSTALLIZING.writeToCompoundTag(compoundTag, crystallizingAspect);
    }

    protected int tickCount;
    protected int progressTickCount;
    public static final int MAX_PROGRESS = 200;
    public int getMaxProgress(){
        return MAX_PROGRESS;
    }
    public void serverTick() {
        if (this.level == null){
            return;
        }
        var pos = getBlockPos();
        int maxProgress = getMaxProgress();
        this.tickCount += 1;
        if (this.tickCount % 5 == 0 && !this.hasNeighbourSignal()) {
            if (this.crystallizingAspect.isEmpty()) {
                this.fillReservoir();
            } else {
                this.progressTickCount += 1 + VisNetHandler.drainCentiVis(
                        this.level, pos.getX(),pos.getY(),pos.getZ(),
                        Aspects.EARTH,
                        Math.min(20, Math.max(1, (maxProgress - this.progressTickCount) / 2))) * 2;
            }
        }

        if (!this.crystallizingAspect.isEmpty() && this.progressTickCount >= maxProgress) {
            this.finishCrystallizingAspect();
            markDirtyAndUpdateSelf();
        }
    }

    protected void fillReservoir() {
        if (this.level == null){
            return;
        }
        var facing = getFacing();
        var pos = getBlockPos();
        var pickPos = pos.relative(facing);
        if (this.level.getBlockState(pickPos).getBlock() instanceof IEssentiaTransportOutBlockEntity outBE) {
            var outFacing = facing.getOpposite();
            if (!outBE.canOutputTo(outFacing)) {
                return;
            }

            Aspect ta = outBE.getEssentiaType(outFacing);

            if (!ta.isEmpty()) {
                this.addEssentia(ta, outBE.takeEssentiaWithSuction(this.getSuctionAmount(facing),ta, 1, outFacing),facing);
                progressTickCount = 0;
            }
        }

    }

    public Direction getOutFacing() {
        return getFacing().getOpposite();
    }

    protected void finishCrystallizingAspect() {
        if (this.level == null || crystallizingAspect.isEmpty()) {
            return;
        }
        var pos = getBlockPos();
        var selfOutputFacing = getOutFacing();
        var outToPos = pos.relative(selfOutputFacing);
        var outStack = CRYSTAL_ESSENCE().ofAspect(crystallizingAspect);
        if (LevelBlockEntityAccessing.getExistingBlockEntity(this.level, outToPos) instanceof Container container) {
            outStack = InventoryUtils.placeItemStackIntoInventory(
                    outStack,
                    container,
                    selfOutputFacing.getOpposite(),
                    true
            );
        }
        if (!outStack.isEmpty()) {
            if (!this.dropStack(outStack)){
                return;
            }
        }

        this.level.playSound(null,pos, SoundEvents.LAVA_EXTINGUISH, SoundSource.BLOCKS
                , 0.25F, 2.6F + (this.level.random.nextFloat() - this.level.random.nextFloat()) * 0.8F
        );

        this.crystallizingAspect = Aspects.EMPTY;
        this.progressTickCount = 0;
    }

    @Override
    public int currentValueForComparatorSignal() {
        return crystallizingAspect.isEmpty()?0:1;
    }

    @Override
    public int comparatorSignalCapacity() {
        return 1;
    }

    public Direction getFacing() {
        return Direction.DOWN;//the only direction now
    }

    public final boolean hasNeighbourSignal(){
        return getBlockState().getValue(EssentiaCrystallizerBlock.POWERED);
    }

    //return dropped
    protected boolean dropStack(ItemStack stack) {
        if (this.level == null){
            return false;
        }
        var bpos = getBlockPos();
        var offsetVec = getFacing().getOpposite().getNormal();
        var dropPos = bpos.offset(offsetVec);

        var ie2 = new ItemEntity(this.level,
                dropPos.getX() + offsetVec.getX() * 0.65,
                dropPos.getY() + offsetVec.getY() * 0.65,
                dropPos.getZ() + offsetVec.getZ() * 0.65,
                stack);
        ie2.setDeltaMovement(offsetVec.getX()*0.04F,offsetVec.getY()*0.04F,offsetVec.getZ()*0.04F);

        this.level.blockEvent(bpos, ThaumcraftBlocks.ThaumcraftBlockInstances.ESSENTIA_CRYSTALLIZER(), 0, 0);
        return this.level.addFreshEntity(ie2);
    }


    public @NotNull Aspect getSuctionType(@NotNull Direction loc) {
        return Aspects.EMPTY;
    }

    @Override
    public int addEssentia(@NotNull Aspect aspect, int amount, @NotNull Direction fromDirection) {
        if (!canInputFrom(fromDirection)) {
            return 0;
        }
        return amount - this.addIntoContainer(aspect, amount);
    }

    @Override
    public @NotNull Aspect getEssentiaType(@NotNull Direction face) {
        return crystallizingAspect;
    }

    @Override
    public int getEssentiaAmount(@NotNull Direction face) {
        return crystallizingAspect.isEmpty()?0:1;
    }

    public int getSuctionAmount(@NotNull Direction loc) {
        return this.hasNeighbourSignal() ? 0 : (loc == getFacing() && this.crystallizingAspect.isEmpty() ? 128 : 64);
    }

    @Override
    public boolean isConnectable(@NotNull Direction face) {
        return face == getFacing();
    }

    @Override
    public boolean canInputFrom(@NotNull Direction face) {
        return isConnectable(face);
    }

    @Override
    public void setSuction(@NotNull Aspect aspect, int amount) {

    }


    public void clientTick() {
        ClientTickContext.tickBE(this);
    }

    @Override
    public int addIntoContainer(Aspect aspect, int amount) {
        if (!crystallizingAspect.isEmpty()) {
            return amount;
        }
        if (amount <= 0) {
            return amount;
        }
        crystallizingAspect = aspect;
        return amount-1;
    }

    @Override
    public boolean doesContainerAccept(Aspect aspect) {
        return true;
    }

    @Override
    public int getComparatorSignal() {
        return crystallizingAspect.isEmpty()?0:15;
    }

    public static class ClientTickContext {

        private float tr = 1;
        private float tg = 1;
        private float tb = 1;
        private float cr = 1;
        private float cg = 1;
        private float cb = 1;
        private float spin = 0;
        private float spinInc = 0;
        private int ventingTicks = 0;

        public int getVentingTicks() {
            return ventingTicks;
        }

        public float getTr() {
            return tr;
        }

        public float getTg() {
            return tg;
        }

        public float getTb() {
            return tb;
        }

        public float getCr() {
            return cr;
        }

        public float getCg() {
            return cg;
        }

        public float getCb() {
            return cb;
        }

        public static void doVenting(EssentiaCrystallizerBlockEntity be) {
            var ctx = ((EssentiaCrystallizerBlockEntityClientAccessor)be).opentc4$getClientTickContext();
            ctx.ventingTicks = 7;
        }

        public static void tickBE(EssentiaCrystallizerBlockEntity be) {
            var ctx = ((EssentiaCrystallizerBlockEntityClientAccessor)be).opentc4$getClientTickContext();
            if (!(be.level instanceof ClientLevel clientLevel)) {
                return;
            }
            var pos = be.getBlockPos();
            var facingOpposite = be.getFacing().getOpposite();
            if (be.crystallizingAspect.isEmpty()) {
                ctx.tr = ctx.tg = ctx.tb = 1.0F;
            } else {
                Color c = new Color(be.crystallizingAspect.getColor());
                ctx.tr = (float)c.getRed() / 220.0F;
                ctx.tg = (float)c.getGreen() / 220.0F;
                ctx.tb = (float)c.getBlue() / 220.0F;
            }

            if (ctx.cr < ctx.tr) {
                ctx.cr += 0.05F;
            }

            if (ctx.cr > ctx.tr) {
                ctx.cr -= 0.05F;
            }

            if (ctx.cg < ctx.tg) {
                ctx.cg += 0.05F;
            }

            if (ctx.cg > ctx.tg) {
                ctx.cg -= 0.05F;
            }

            if (ctx.cb < ctx.tb) {
                ctx.cb += 0.05F;
            }

            if (ctx.cb > ctx.tb) {
                ctx.cb -= 0.05F;
            }

            ctx.spin += ctx.spinInc;
            if (ctx.spin > 360.0F) {
                ctx.spin -= 360.0F;
            }

            if (!be.crystallizingAspect.isEmpty() && ctx.spinInc < 20.0F && !be.hasNeighbourSignal()) {
                ctx.spinInc += 0.1F;
                if (ctx.spinInc > 20.0F) {
                    ctx.spinInc = 20.0F;
                }
            } else if ((be.crystallizingAspect.isEmpty() || be.hasNeighbourSignal()) && ctx.spinInc > 0.0F) {
                ctx.spinInc -= 0.2F;
                if (ctx.spinInc < 0.0F) {
                    ctx.spinInc = 0.0F;
                }
            }

            if (ctx.ventingTicks > 0) {
                --ctx.ventingTicks;
                float fx = 0.1F - be.level.random.nextFloat() * 0.2F;
                float fz = 0.1F - be.level.random.nextFloat() * 0.2F;
                float fy = 0.1F - be.level.random.nextFloat() * 0.2F;
                float fx2 = 0.1F - be.level.random.nextFloat() * 0.2F;
                float fz2 = 0.1F - be.level.random.nextFloat() * 0.2F;
                float fy2 = 0.1F - be.level.random.nextFloat() * 0.2F;
                @RGBColor int color = 0xffffff;
                ClientFXUtils.drawVentParticles(clientLevel,
                        pos.getX() + 0.5F + fx + facingOpposite.getStepX() / 2.1F,
                        pos.getY() + fy + facingOpposite.getStepY() / 2.1F,
                        pos.getZ() + 0.5F + fz + facingOpposite.getStepZ() / 2.1F,
                        facingOpposite.getStepX() / 4.0F + fx2,
                        facingOpposite.getStepY() / 4.0F + fy2,
                        facingOpposite.getStepZ() / 4.0F + fz2,
                        color
                );
            }
        }
    }
}
