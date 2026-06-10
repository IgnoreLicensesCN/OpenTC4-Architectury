package thaumcraft.common.items.equipment.elemental;

import com.linearity.opentc4.utils.vanilla1710.MathHelper;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import thaumcraft.common.ClientFXUtils;
import thaumcraft.common.ThaumcraftSounds;
import thaumcraft.common.items.ThaumcraftItems;
import thaumcraft.common.lib.utils.Utils;

public class ElementalSwordItem extends SwordItem {
    public ElementalSwordItem(Tier tier, int i, float f, Properties properties) {
        super(tier, i, f, properties);
    }

    public ElementalSwordItem() {
        this(
                ThaumcraftItems.ToolAndArmorMaterial.TOOL_THAUMIUM_ELEMENTAL,
                3, -2.4F, new Item.Properties().stacksTo(1).rarity(Rarity.RARE)
        );
    }

    protected boolean canAttackNearEntity(ItemStack stack, LivingEntity victim, LivingEntity user) {
        return !victim.isDeadOrDying() && victim.isAlive()
                && (!(victim instanceof OwnableEntity ownableEntity && ownableEntity.getOwner() == user));
    }

    @Override
    public boolean hurtEnemy(ItemStack itemStack, LivingEntity victim, LivingEntity user) {
        if (victim.isAlive() && !user.level().isClientSide && user instanceof Player player) {
            var targets = victim.level().getEntitiesOfClass(LivingEntity.class, victim.getBoundingBox().inflate(1.2, 1.1, 1.2), e -> e != user && e != victim);
            int count = 0;
            if (!targets.isEmpty()) {
                for (var target : targets) {
                    if (canAttackNearEntity(itemStack, target, user)) {
                        player.resetAttackStrengthTicker();
                        player.attack(target);
                        player.resetAttackStrengthTicker();
                        ++count;
                    }
                }

                if (count > 0) {
                    user.level().playSound(
                            user,
                            user.blockPosition(),
                            ThaumcraftSounds.SWING,
                            SoundSource.PLAYERS
                            , 1.0F, 0.9F + user.level().random.nextFloat() * 0.2F);
                }
            }
        }


        return super.hurtEnemy(itemStack, victim, user);
    }

    @Override
    public int getUseDuration(ItemStack itemStack) {
        return Integer.MAX_VALUE;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        player.startUsingItem(interactionHand);
        return InteractionResultHolder.sidedSuccess(player.getItemInHand(interactionHand), level.isClientSide);
    }

    protected boolean canPushEntity(ItemStack stack, Entity victim, LivingEntity user) {
        return !(victim instanceof Player)
                && !(victim instanceof LivingEntity living && living.isDeadOrDying())
                && (user.getControllingPassenger() != victim);
    }

    @Override
    public void onUseTick(Level level, LivingEntity user, ItemStack itemStack, int i) {
        super.onUseTick(level, user, itemStack, i);


        int ticksPassed = this.getUseDuration(itemStack) - i;
        var selfSpeed = user.getDeltaMovement();
        double selfSpeedY = selfSpeed.y;
        if (selfSpeedY < (double) 0.0F) {
            selfSpeedY /= 1.2F;
            user.fallDistance /= 1.2F;
        }
        selfSpeedY += 0.08F;
        if (selfSpeedY > 0.5F) {
            selfSpeedY = 0.2F;
        }
        selfSpeed = new Vec3(selfSpeed.x,selfSpeedY,selfSpeed.z);
        user.setDeltaMovement(selfSpeed);

        if (user instanceof ServerPlayer serverPlayer) {
            Utils.resetAboveGroundCounter(serverPlayer);
        }

        var targets = user.level().getEntities(user, user.getBoundingBox().inflate(2.5F, 2.5F, 2.5F));
        if (!targets.isEmpty()) {
            for (var target : targets) {
                if (canPushEntity(itemStack,target,user)
                ) {
                    var userPos = user.position();
                    var targetPos = target.position();
                    var vecUserToTarget = targetPos.subtract(userPos);
                    var distance = vecUserToTarget.length();
                    target.addDeltaMovement(vecUserToTarget.multiply(
                            1./(2.5*vecUserToTarget.x*distance),
                            1./(2.5*vecUserToTarget.y*distance),
                            1./(2.5*vecUserToTarget.z*distance)
                            )
                    );
                }
            }
        }

        if (level.isClientSide) {
            if (level instanceof ClientLevel clientLevel){
                var boundingBoxMinY = user.getBoundingBox().minY;
                int miny = (int) (boundingBoxMinY - (double) 2.0F);
                if (user.onGround()) {
                    miny = MathHelper.floor_double(boundingBoxMinY);
                }

                for (int a = 0; a < 5; ++a) {
                    ClientFXUtils.smokeSpiral(clientLevel,
                            user.position().x,
                            boundingBoxMinY + (double) (user.getEyeHeight() / 2.0F),
                            user.position().z, 1.5F,
                            user.level().random.nextInt(360),
                            miny,
                            0xdddddd);
                }

                if (user.onGround()) {
                    float r1 = user.level().random.nextFloat() * 360.0F;
                    float mx = -MathHelper.sin(r1 / 180.0F * (float) Math.PI) / 5.0F;
                    float mz = MathHelper.cos(r1 / 180.0F * (float) Math.PI) / 5.0F;
                    user.level().addParticle(ParticleTypes.SMOKE, user.position().x, boundingBoxMinY + (double) 0.1F, user.position().z, mx, 0.0F, mz);
                }
            }
        } else if (ticksPassed % 20 == 0) {
            user.level().playSound(user, user.blockPosition(),ThaumcraftSounds.WIND,SoundSource.PLAYERS, 0.5F, 0.9F + user.level().random.nextFloat() * 0.2F);
        }

        if (!level.isClientSide) {
            if (ticksPassed%20==0){
                itemStack.hurtAndBreak(1, user, living -> living.broadcastBreakEvent(living.getUsedItemHand()));
            }
        }

    }
}
