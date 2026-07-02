package thaumcraft.common.entities;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import thaumcraft.api.aspects.aspect.IAspectReducibleToPrimal;
import thaumcraft.api.damagesource.ThaumcraftDamageSources;
import thaumcraft.api.listeners.aspects.entity.basic.EntityBasicAspectGetters;
import thaumcraft.common.entities.abstracts.ITaintConvertableEntity;
import thaumcraft.common.entities.monster.tainted.ThaumicSlimeEntity;
import thaumcraft.common.items.abstracts.ISpecialDamageCalculationEquipmentItem;
import thaumcraft.common.items.abstracts.armorcomponents.IAttackOthersListenerArmor;
import thaumcraft.common.items.abstracts.armorcomponents.IBeingAttackedListenerArmor;
import thaumcraft.common.lib.effects.ThaumcraftEffects;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static com.linearity.opentc4.utils.equip.bauble.BaubleUtils.forEachBauble;
import static thaumcraft.common.items.ThaumcraftItemInstances.CRYSTAL_ESSENCE;
import static thaumcraft.common.items.ThaumcraftItems.ItemTags.UNNATURAL_HUNGER_NEEDED;
import static thaumcraft.common.lib.utils.EntityUtils.ThaumcraftAttributeCategoryInstances.*;

public class ThaumcraftEntityEvents {

    public static void convertTaintMobOnRemoved(Entity self, Entity.RemovalReason removalReason) {
        if (self instanceof LivingEntity){
            if (removalReason == Entity.RemovalReason.KILLED){
                var level = self.level();
                if (self instanceof ITaintConvertableEntity taintConvertable && taintConvertable.canConvertToTaintedMob() && !level.isClientSide) {
                    taintConvertable.convertToTaintedMob();
                } else {
                    if (!level.isClientSide){
                        var entity = new ThaumicSlimeEntity(level);
                        entity.setSize((int) (1.0F + Math.min(self instanceof LivingEntity living ? living.getMaxHealth() : 0 / 10.0F, 6.0F)), true);
                        entity.setPos(self.position());
                        level.addFreshEntity(entity);
                    }
                }
            }
        }
    }

    public static class DropEvents {

        public static void onDropAllDeathLoot(LivingEntity living, DamageSource damageSource) {
            onDropCrystalEssence(living, damageSource);
            onDropVisOrb(living, damageSource);
        }

        public static void onDropCrystalEssence(LivingEntity living, DamageSource damageSource) {
            if (damageSource.is(
                    ThaumcraftDamageSources.Tags.DROPS_CRYSTAL_ESSENCE
            )) {
                var aspectOwning = EntityBasicAspectGetters.getBasicAspectsForEntityType(living.getType());
                if (!aspectOwning.isEmpty()) {
                    var random = living.getRandom();
                    IAspectReducibleToPrimal.reduceToPrimalsAndCast(aspectOwning).forEach(
                            (aspect, amount) -> {
                                if (random.nextBoolean()) {
                                    int size = 1 + random.nextInt(amount);
                                    size = Math.max(1, size / 2);
                                    var stack = new ItemStack(CRYSTAL_ESSENCE(),size);
                                    CRYSTAL_ESSENCE().setOwningAspect(stack, aspect);
                                    living.spawnAtLocation(
                                            stack
                                    );
                                }
                            }
                    );
                }
            }
        }

        public static void onDropVisOrb(LivingEntity living, DamageSource damageSource) {
            var aspectOwning = EntityBasicAspectGetters.getBasicAspectsForEntityType(living.getType());
            if (!aspectOwning.isEmpty()){
                IAspectReducibleToPrimal.reduceToPrimalsAndCast(aspectOwning).forEach(
                        (aspect,amount) -> {
                            //TODO:Drop vis(primal) orb.
                        }
                );
            }
        }
    }
    public static class DamageEvents {

        public static float getDamageAfterArmorAbsorb(LivingEntity living,float originalOut,DamageSource damageSource,float originalIn) {
            AtomicReference<Float> modifiedOut = new AtomicReference<>(originalOut);
            living.getArmorSlots().forEach(stack -> {
                if (stack.getItem() instanceof ISpecialDamageCalculationEquipmentItem equipment) {
                    modifiedOut.updateAndGet(out -> equipment.modifyDamageAfterCalculatedArmorAbsorb(living,stack,out,damageSource,originalIn));

                }
            });
            forEachBauble(living, ISpecialDamageCalculationEquipmentItem.class,((slot, stack, equipment) -> {
                modifiedOut.updateAndGet(out -> equipment.modifyDamageAfterCalculatedArmorAbsorb(living,stack,out,damageSource,originalIn));
                return false;
            }));
            return modifiedOut.get();
        }
        public static float getDamageAfterMagicAbsorb(LivingEntity living,float originalOut,DamageSource damageSource,float originalIn) {
            AtomicReference<Float> modifiedOut = new AtomicReference<>(originalOut);
            living.getArmorSlots().forEach(stack -> {
                if (stack.getItem() instanceof ISpecialDamageCalculationEquipmentItem equipment) {
                    modifiedOut.updateAndGet(out -> equipment.modifyDamageAfterCalculatedMagicAbsorb(living,stack,out,damageSource,originalIn));

                }
            });
            forEachBauble(living, ISpecialDamageCalculationEquipmentItem.class,((slot, stack, equipment) -> {
                modifiedOut.updateAndGet(out -> equipment.modifyDamageAfterCalculatedMagicAbsorb(living,stack,out,damageSource,originalIn));
                return false;
            }));
            return modifiedOut.get();
        }

        public static void onBeingDamaged(LivingEntity living,DamageSource damageSource, float damageCausedNoArmorReduce) {
            var entityCausedDamage = damageSource.getEntity();
            if (entityCausedDamage != null) {

                for (var stack : entityCausedDamage.getArmorSlots()) {
                    if (stack.getItem() instanceof IAttackOthersListenerArmor armor) {
                        armor.onAttackOtherEntity(
                                stack,
                                entityCausedDamage,
                                living,
                                damageSource,
                                damageCausedNoArmorReduce
                        );
                    }
                }
            }
            for (var stack:living.getArmorSlots()) {
                if (stack.getItem() instanceof IBeingAttackedListenerArmor armor) {
                    armor.onBeingAttackedByOtherEntity(
                            stack,
                            living,
                            damageSource,
                            damageCausedNoArmorReduce
                    );
                }
            }
        }
    }

    public static List<Attribute> attributesToAdd = new ArrayList<>();
    static {
        attributesToAdd.add(JUMP_Y_VELOCITY_ADDITION_NOT_SNEAKING());
        attributesToAdd.add(STEP_HEIGHT_ADDITION_NOT_SNEAKING());
        attributesToAdd.add(FLYING_SPEED_CONTROL_OVERRIDE());
        attributesToAdd.add(HARNESS_FLYING_SPEED_ADD_PERCENT());
        attributesToAdd.add(HARNESS_FUEL_DURATION_ADD_PERCENT());

    }

    public static AttributeSupplier.Builder injectLivingAttributes(AttributeSupplier.Builder builder) {
        attributesToAdd.forEach(builder::add);
        return builder;
    }

    public static void onHandlingUnnaturalHungerForEating(LivingEntity living,ItemStack itemStack) {
        Item item = itemStack.getItem();
        var unnaturalHungerInstance = living.getEffect(ThaumcraftEffects.ThaumcraftEffectTypeInstances.UNNATURAL_HUNGER());
        if (unnaturalHungerInstance != null){
            if (itemStack.is(UNNATURAL_HUNGER_NEEDED)){
                int amp = unnaturalHungerInstance.getAmplifier() - 1;
                int duration = unnaturalHungerInstance.getDuration() - 600;
                living.removeEffect(ThaumcraftEffects.ThaumcraftEffectTypeInstances.UNNATURAL_HUNGER());
                if (duration > 0 && amp >= 0) {
                    living.addEffect(new MobEffectInstance(ThaumcraftEffects.ThaumcraftEffectTypeInstances.UNNATURAL_HUNGER(), duration, amp, true,true));
                }

                living.sendSystemMessage((Component.translatable("warp.text.hunger.2")
                        .withStyle(ChatFormatting.ITALIC)
                        .withStyle(ChatFormatting.DARK_GREEN)));
            }
            else if (item.getFoodProperties() != null) {
                living.sendSystemMessage(
                        Component.translatable("warp.text.hunger.1")
                                .withStyle(ChatFormatting.ITALIC)
                                .withStyle(ChatFormatting.DARK_RED)
                );
            }
        }
    }
}
