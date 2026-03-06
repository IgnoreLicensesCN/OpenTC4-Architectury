package thaumcraft.common.tiles.crafted;

import com.google.common.collect.MapMaker;
import dev.architectury.fluid.FluidStack;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.tile.TileThaumcraft;
import thaumcraft.common.ClientFXUtils;
import thaumcraft.common.blocks.ThaumcraftBlocks;
import thaumcraft.common.blocks.liquid.ThaumcraftFluids;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;

import java.awt.*;
import java.util.Map;

import static dev.architectury.fluid.FluidStack.create;

public class CrucibleBlockEntity extends TileThaumcraft {
    private long counter;
    public static final int ASPECT_CAPACITY = 100;
    public static final int BOILING_HEAT = 150;
    public static final int LIQUID_CAPACITY = 2000;

    public CrucibleBlockEntity(BlockEntityType<? extends CrucibleBlockEntity> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }
    public CrucibleBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(ThaumcraftBlockEntities.CRUCIBLE, blockPos, blockState);
    }
    public int getAspectCapacity() {
        return ASPECT_CAPACITY;
    }
    public int getBoilingHeat() {
        return BOILING_HEAT;
    }
    public int getLiquidCapacity() {
        return LIQUID_CAPACITY;
    }

    protected final @NotNull FluidStack fluidStack = create(Fluids.EMPTY, 0);//keep it a instance

    public @NotNull FluidStack getFluidStack() {
        return fluidStack;
    }

    public long getFluidAmount() {
        return fluidStack.getAmount();
    }

    //return inserted
    public long insertFluid(Fluid fluid,long maxCanInsert){
        return insertFluid(fluid,maxCanInsert,true);
    }
    public long insertFluid(Fluid fluid,long maxCanInsert,boolean doIt) {
        if (!canAcceptFluid(fluid)) {
            return 0;
        }
        if (fluid != fluidStack.getFluid() && !fluidStack.isEmpty()) {
            return 0;
        }
        long currentAmount = fluidStack.getAmount();
        if (currentAmount < 0){
            currentAmount = 0;
            fluidStack.setAmount(0);
        }
        long spaceToInsert = getLiquidCapacity() - currentAmount;
        if (spaceToInsert < 0){
            spaceToInsert = 0;
            fluidStack.setAmount(getLiquidCapacity());
        }
        long inserted = Math.min(spaceToInsert, maxCanInsert);
        if (doIt){
            fluidStack.setAmount(currentAmount + inserted);
        }
        return inserted;
    }

    public long extractFluid(Fluid fluid, long maxCanExtract) {
        return 0L;//no out
    }

    public boolean canAcceptFluid(@NotNull Fluid fluid) {
        return fluid.isSame(Fluids.WATER);
    }


    public final AspectList<Aspect> owningAspects = new AspectList<>();
    protected int heat = 0;

    public boolean isHeating() {
        if (level == null){
            return false;
        }
        var blockStateBelow = level.getBlockState(getBlockPos().below());
        var blockBelow = blockStateBelow.getBlock();
        if (blockBelow == ThaumcraftBlocks.NITOR_BLOCK){
            return true;
        }
        var fluidState = blockStateBelow.getFluidState();
        if (!fluidState.isEmpty()){
            return fluidState.is(ThaumcraftFluids.Tags.CRUCIBLE_HEATER);
        }
        return false;
    }

    public void serverTick(){

    }
    public void entityInside(@NotNull Entity entity) {
        if (level == null){
            return;
        }
        if (entity instanceof ItemEntity itemEntity){

            //TODO
        } else {
            //magma block
            if (!entity.isSteppingCarefully() && entity instanceof LivingEntity && !EnchantmentHelper.hasFrostWalker((LivingEntity)entity)) {
                entity.hurt(level.damageSources().hotFloor(), 1.0F);
            }
        }
    }
    public void clientTick(){
        ClientTickContext.tick(this);
    }

    public static class ClientTickContext {
//        int prevcolor;
        int prevx;
        int prevy;
        private static final Map<CrucibleBlockEntity, ClientTickContext> contexts =
                new MapMaker().weakKeys().makeMap();
        public static void tick(CrucibleBlockEntity be){
            if (!(be.level instanceof ClientLevel level)){return;}
            var ctx = contexts.computeIfAbsent(be,c->new ClientTickContext());
            var pos = be.getBlockPos();
            var xCoord = pos.getX();
            var yCoord = pos.getY();
            var zCoord = pos.getZ();
            if (be.heat > be.getBoilingHeat()) {
                ClientFXUtils.crucibleFroth(
                        level,
                        (float)xCoord + 0.2F + level.random.nextFloat() * 0.6F,
                        (float)yCoord + getFluidHeight(be),
                        (float)zCoord + 0.2F + level.random.nextFloat() * 0.6F
                );
                if (be.owningAspects.visSize() > be.getAspectCapacity()) {
                    for(int a = 0; a < 2; ++a) {
                        ClientFXUtils.crucibleFrothDown(level, (float)xCoord, (float)(yCoord + 1), (float)zCoord + level.random.nextFloat());
                        ClientFXUtils.crucibleFrothDown(level, (float)(xCoord + 1), (float)(yCoord + 1), (float)zCoord + level.random.nextFloat());
                        ClientFXUtils.crucibleFrothDown(level, (float)xCoord + level.random.nextFloat(), (float)(yCoord + 1), (float)zCoord);
                        ClientFXUtils.crucibleFrothDown(level, (float)xCoord + level.random.nextFloat(), (float)(yCoord + 1), (float)(zCoord + 1));
                    }
                }
            }

            if (level.random.nextInt(6) == 0 && !be.owningAspects.isEmpty()) {
                int color = be.owningAspects.randomAspect(level.random).getColor() - 16777216;
                int x = 5 + level.random.nextInt(22);
                int y = 5 + level.random.nextInt(22);
//                ctx.delay = level.random.nextInt(10);
//                ctx.prevcolor = color;
                ctx.prevx = x;
                ctx.prevy = y;
                Color c = new Color(color);
                float r = (float)c.getRed() / 255.0F;
                float g = (float)c.getGreen() / 255.0F;
                float b = (float)c.getBlue() / 255.0F;
                ClientFXUtils.crucibleBubble(
                        level,
                        (float)xCoord + (float)x / 32.0F + 0.015625F,
                        (float)yCoord + 0.05F + getFluidHeight(be),
                        (float)zCoord + (float)y / 32.0F + 0.015625F,
                        r, g, b
                );
            }
        }
        public static float getFluidHeight(CrucibleBlockEntity be) {
            float base = 0.3F + 0.5F * ((float) be.fluidStack.getAmount() / (float)be.getLiquidCapacity());
            float out = base + (float)be.owningAspects.visSize() / 100.0F * (1.0F - base);
            if (out > 1.0F) {
                out = 1.001F;
            }

            if (out == 1.0F) {
                out = 0.9999F;
            }

            return out;
        }

    }

}
