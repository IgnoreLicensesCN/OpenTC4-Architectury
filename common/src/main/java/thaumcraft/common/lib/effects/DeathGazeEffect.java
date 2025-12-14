package thaumcraft.common.lib.effects;

import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.entity.EntityTypeTest;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.effects.PreventMilkRemoveEffect;
import thaumcraft.common.lib.utils.EntityUtils;

import java.util.List;

public class DeathGazeEffect extends MobEffect implements PreventMilkRemoveEffect {

    public static final int DEATH_GAZE_EFFECT_COLOR = 0x664433;
    public static int warpVignette = 0;
    protected DeathGazeEffect() {
        super(MobEffectCategory.HARMFUL, DEATH_GAZE_EFFECT_COLOR);
    }

    @Override
    public void applyEffectTick(LivingEntity livingWatcher, int i) {
        if (livingWatcher == null) {
            return;
        }
        if (Platform.getEnvironment() == Env.SERVER) {
            MobEffectInstance pe = livingWatcher.getEffect(this);
            if (pe != null) {
                int level = pe.getAmplifier();
                int range = Math.min(8 + level * 3, 24);

                var box = livingWatcher.getBoundingBox().inflate(range);

                List<LivingEntity> list = livingWatcher.level().getEntities(
                        EntityTypeTest.forClass(LivingEntity.class),
                        box,
                        entity -> {
                            if (entity == null){
                                return false;
                            }
                            if (entity instanceof ServerPlayer beenSeenPlayer && livingWatcher instanceof ServerPlayer playerWatchingEntity){
                                var server = beenSeenPlayer.getServer();
                                if (server == null){
                                    server = playerWatchingEntity.getServer();
                                }
                                if (server != null){
                                    if (!server.isPvpAllowed()){
                                        return false;
                                    }
                                }
                            }
                            if (entity.hasEffect(MobEffects.WITHER)) {
                                return false;
                            }
                            return entity.canBeCollidedWith()
                                    && entity.isAlive()
                                    && EntityUtils.isVisibleTo(0.75F, livingWatcher, entity, (float) range)
                                    && livingWatcher.hasLineOfSight(entity);
                        }
                );

                for (var livingVictim : list) {
                    livingVictim.setLastHurtByMob(livingWatcher);
                    if (livingWatcher instanceof Player player) {
                        livingVictim.setLastHurtByPlayer(player);
                    }

                    if (livingVictim instanceof Mob mob) {
                        mob.setTarget(livingWatcher);
                    }

                    livingVictim.addEffect(new MobEffectInstance(MobEffects.WITHER, 80));
                }
            }
        }
    }

    @Override
    public void applyInstantenousEffect(@Nullable Entity entity, @Nullable Entity entity2, LivingEntity livingEntity, int i, double d) {}
}
