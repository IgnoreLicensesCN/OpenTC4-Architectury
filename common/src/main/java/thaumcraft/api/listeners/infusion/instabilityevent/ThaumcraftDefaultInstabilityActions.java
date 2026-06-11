package thaumcraft.api.listeners.infusion.instabilityevent;

import com.linearity.opentc4.utils.EntityTypeTests;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import thaumcraft.api.warp.WarpInfo;
import thaumcraft.common.blocks.liquid.ThaumcraftFluids;
import thaumcraft.common.lib.effects.ThaumcraftEffects;
import thaumcraft.common.lib.network.fx.PacketFXBlockZapS2C;
import thaumcraft.common.tiles.abstracts.IInfusionComponentStackProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class ThaumcraftDefaultInstabilityActions {

    public static final BiConsumer<Level, IInfusionComponentStackProvider> IGNORE = (level, iInfusionComponentStackProvider) -> {};
    public static final BiConsumer<Level, IInfusionComponentStackProvider> CONTAINER_CLEAR_CONTENT = (level, container) -> container.clearContent();
    public static final BiConsumer<Level,IInfusionComponentStackProvider> CONTAINER_DROP_STACK = (level, container) -> Containers.dropContents(level,container.getBlockPos().above(),container);
    public static final BiConsumer<Level,IInfusionComponentStackProvider> PLACE_FLUX_GOO = (level,container) -> {
        var containerPos = container.getBlockPos();
        var containerPosAbove = containerPos.above();
        if (level.getBlockState(containerPosAbove).isAir()){
            level.setBlockAndUpdate(containerPosAbove, ThaumcraftFluids.ThaumcraftFluidInstances.FLUX_GOO_FLUID.defaultFluidState().setValue(ThaumcraftFluids.ThaumcraftFluidInstances.FLUX_GOO_FLUID.liquidLevel,7).createLegacyBlock());
        }
        level.playSound(null,containerPos, SoundEvents.PLAYER_SWIM, SoundSource.BLOCKS,0.3F,1.F);
    };
    public static final BiConsumer<Level,IInfusionComponentStackProvider> PLACE_FLUX_GAS = (level,container) -> {
        var containerPos = container.getBlockPos();
        var containerPosAbove = containerPos.above();
        if (level.getBlockState(containerPosAbove).isAir()){
            level.setBlockAndUpdate(containerPosAbove, ThaumcraftFluids.ThaumcraftFluidInstances.FLUX_GAS_FLUID.defaultFluidState().setValue(ThaumcraftFluids.ThaumcraftFluidInstances.FLUX_GAS_FLUID.liquidLevel,7).createLegacyBlock());
        }
        level.playSound(null,containerPos, SoundEvents.LAVA_EXTINGUISH, SoundSource.BLOCKS,0.3F,1.F);
    };
    public static final BiConsumer<Level,IInfusionComponentStackProvider> EXPLODE = (level,container) -> {
        var containerPos = container.getBlockPos();
        level.explode(
                null,
                containerPos.getX() + 0.5F,
                containerPos.getY() + 1.5F,
                containerPos.getZ() + 0.5F,
                1,
                Level.ExplosionInteraction.MOB);
    };
    public static void infusionEjectItem(
            InfusionInstabilityEventListener.InfusionInstabilityEventContext ctx,
            BiConsumer<Level,IInfusionComponentStackProvider> containerAction,
            BiConsumer<Level,IInfusionComponentStackProvider> placeFluxFluidAction
    ){
        var be = ctx.infusionMatrix;
        var bePos = be.getBlockPos();
        var level = ctx.level;
        if (level instanceof ServerLevel serverLevel){
            var containerPosOffsets = be.getComponentProviderPosOffsets();
            for (var posOffset : containerPosOffsets) {
                var containerPos = posOffset.offset(bePos);
                var containerBE = level.getBlockEntity(containerPos);
                if (containerBE instanceof IInfusionComponentStackProvider componentProviderBE && !componentProviderBE.isEmpty()){
                    containerAction.accept(level,componentProviderBE);
                    placeFluxFluidAction.accept(level,componentProviderBE);
//                            this.level().addBlockEvent(cc.posX, cc.posY, cc.posZ, ConfigBlocks.blockStoneDevice, 11, 0);
                    new PacketFXBlockZapS2C(
                            bePos.getX() + 0.5F, bePos.getY() + 0.5F, bePos.getX() + 0.5F,
                            containerPos.getX() + 0.5F, containerPos.getY() + 1.5F, containerPos.getZ() + 0.5F)
                            .sendToAllAround(serverLevel,bePos,32*32);
                    break;
                }
            }
        }
    }

    public static List<LivingEntity> getLivingNear(Level level,BlockPos pos,int maxAmount){
        return getLivingNear(level,pos,maxAmount, EntityTypeTests.LIVING_TEST);
    }
    public static <T extends Entity> List<T> getLivingNear(Level level, BlockPos pos, int maxAmount, EntityTypeTest<Entity, T> entityTypeTest){
        List<T> list = new ArrayList<>(Math.min(maxAmount, 10));
        AABB box = new AABB(pos).inflate(10, 10, 10);
        level.getEntities(
                entityTypeTest,
                box,
                _ignored -> true,
                list,
                maxAmount
        );
        return list;
    }

    public static void infusionZapOne(InfusionInstabilityEventListener.InfusionInstabilityEventContext ctx) {
        getLivingNear(ctx.level,ctx.matrixPos,1).forEach(entity -> infusionZapEntity(entity,ctx.matrixPos));
    }
    public static void infusionZapAll(InfusionInstabilityEventListener.InfusionInstabilityEventContext ctx) {
        getLivingNear(ctx.level,ctx.matrixPos,Integer.MAX_VALUE).forEach(entity -> infusionZapEntity(entity,ctx.matrixPos));
    }
    public static void infusionZapEntity(LivingEntity living, BlockPos matrixPos){
        if (living.level() instanceof ServerLevel serverLevel){
            living.hurt(serverLevel.damageSources().magic(), (float) (4 + serverLevel.random.nextInt(4)));
            new PacketFXBlockZapS2C(
                    matrixPos.getX() + 0.5F,  matrixPos.getY() + 0.5F,  matrixPos.getZ() + 0.5F,
                    (float) living.getX(), (float) (living.getY() + (living.getEyeHeight()/2)), (float) living.getZ()
            )
                    .sendToAllAround(serverLevel,matrixPos,32*32.);
        }
    }
    public static void infusionFluxOne(InfusionInstabilityEventListener.InfusionInstabilityEventContext ctx) {
        getLivingNear(ctx.level,ctx.matrixPos,1).forEach(ThaumcraftDefaultInstabilityActions::infusionFluxEntity);
    }
    public static void infusionFluxAll(InfusionInstabilityEventListener.InfusionInstabilityEventContext ctx) {
        getLivingNear(ctx.level,ctx.matrixPos,Integer.MAX_VALUE).forEach(ThaumcraftDefaultInstabilityActions::infusionFluxEntity);
    }
    public static void infusionFluxEntity(LivingEntity living){
        if (!living.level().isClientSide){
            if (living.level().random.nextBoolean()) {
                living.addEffect(new MobEffectInstance(ThaumcraftEffects.ThaumcraftEffectTypeInstances.FLUX_TAINT, 120, 0/*, false*/));
            } else {
                living.addEffect(new MobEffectInstance(ThaumcraftEffects.ThaumcraftEffectTypeInstances.VIS_EXHAUST, 2400, 0/*, true*/));
            }
        }
    }
    public static void infusionExplodeSmall(InfusionInstabilityEventListener.InfusionInstabilityEventContext ctx) {
        var level = ctx.level;
        var pos = ctx.matrixPos;
        level.explode(
                null,
                pos.getX() + 0.5F,
                pos.getY() + 1.5F,
                pos.getZ() + 0.5F,
                1,
                Level.ExplosionInteraction.MOB);
    }
    public static void infusionWarpPlayer(InfusionInstabilityEventListener.InfusionInstabilityEventContext ctx) {
        getLivingNear(ctx.level,ctx.matrixPos,1, EntityTypeTests.PLAYER_TEST).forEach(player -> {
            if (ctx.level.random.nextInt(4) == 0) {
                WarpInfo.getFromPlayer(player).addStickyWarp(1);
            } else {
                WarpInfo.getFromPlayer(player).addTempWarp(1 + ctx.level.random.nextInt(5));
            }
        });
    }
}
