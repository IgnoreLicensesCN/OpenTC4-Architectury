package thaumcraft.common.blocks.technique;

import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import thaumcraft.api.entities.IEldritchMob;
import thaumcraft.client.fx.migrated.particles.FXSpark;
import thaumcraft.common.ThaumcraftSounds;
import thaumcraft.common.entities.projectile.EntityShockOrb;
//tile.blockAiry.11
public class SappingFieldBlock extends Block {
    public SappingFieldBlock(Properties properties) {
        super(properties);
    }
    public SappingFieldBlock(){
        this(
                Properties.of()
                        .noCollission()
                        .replaceable()
                        .randomTicks()
                        .strength(100.0F,50)
                        .lightLevel(s -> 8)
        );
    }
    public static final VoxelShape SHAPE = Shapes.empty();

    @Override
    public VoxelShape getVisualShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return SHAPE;
    }

    @Override
    public VoxelShape getOcclusionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return SHAPE;
    }

    @Override
    public void animateTick(BlockState blockState, Level level, BlockPos blockPos, RandomSource random) {
        if (!(level instanceof ClientLevel clientLevel)) {return;}
        var x = blockPos.getX();
        var y = blockPos.getY();
        var z = blockPos.getZ();
        float h = random.nextFloat() * 0.33F;
        FXSpark ef = new FXSpark(clientLevel,
                (float)x + clientLevel.random.nextFloat(),
                (float)y + 0.1515F + h / 2.0F,
                (float)z + clientLevel.random.nextFloat(), 0.33F + h);
        ef.setRBGColorF(0.65F + clientLevel.random.nextFloat() * 0.1F, 1.0F, 1.0F);
        ef.setAlphaF(0.8F);
        Minecraft.getInstance().particleEngine.add(ef);

        if (random.nextInt(50) == 0) {
            clientLevel.playLocalSound(
                    blockPos,
                    ThaumcraftSounds.JACOBS,
                    SoundSource.BLOCKS,
                    0.5F,
                    1.0F + (random.nextFloat() - random.nextFloat()) * 0.2F,
                    false
            );
        }
    }

    @Override
    public void entityInside(BlockState blockState, Level level, BlockPos blockPos, Entity entity) {
        if (Platform.getEnvironment() != Env.SERVER){
            return;
        }
        if (!(entity instanceof IEldritchMob)) {
            if (level.getRandom().nextInt(100) == 0) {
                entity.hurt(level.damageSources().wither(),1.F);
            }
            var currentVelocity = entity.getDeltaMovement();
            entity.setDeltaMovement(currentVelocity.x * 0.66,currentVelocity.y,currentVelocity.z * 0.66);

            if (entity instanceof Player player) {
                player.getFoodData().addExhaustion(0.05F);
            }

            if (entity instanceof LivingEntity living) {
                var weaknessEffect = new MobEffectInstance(MobEffects.WEAKNESS, 100, 1,true,true);
                living.addEffect(weaknessEffect);
            }
        }
    }
    @Override
    public void tick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {
        serverLevel.setBlockAndUpdate(blockPos, Blocks.AIR.defaultBlockState());
    }

}
