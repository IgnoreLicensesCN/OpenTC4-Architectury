package thaumcraft.common.blocks.crafted.pavingstone;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import thaumcraft.common.ClientFXUtils;

public class PavingStoneTravelBlock extends Block {
    public PavingStoneTravelBlock(Properties properties) {
        super(properties);
    }
    public PavingStoneTravelBlock() {
        super(BlockBehaviour.Properties.copy(Blocks.STONE)
                .lightLevel(s->9)
                .strength(2.f,10.f)
        );
    }

    @Override
    public void stepOn(Level level, BlockPos blockPos, BlockState blockState, Entity entity) {
        if (level instanceof ClientLevel clientLevel) {
            ClientFXUtils.blockSparkle(clientLevel, blockPos, 0x008000, 5);
        }
        else if(entity instanceof LivingEntity livingEntity){
            MobEffectInstance speedEffect = new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 40, 1);
            MobEffectInstance jumpEffect = new MobEffectInstance(MobEffects.JUMP, 40, 1);
            livingEntity.addEffect(speedEffect);
            livingEntity.addEffect(jumpEffect);
        }

    }
}
