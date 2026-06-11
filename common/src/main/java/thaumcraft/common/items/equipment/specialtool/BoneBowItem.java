package thaumcraft.common.items.equipment.specialtool;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import thaumcraft.common.items.abstracts.IInfinityEnchantmentChanceCostArrowItem;

//oh im lazy to write this i just write PrimalArrow it's fine
//so there's a change that infinity wont provide discount for PrimalArrows in normal bows
public class BoneBowItem extends BowItem {
    public BoneBowItem(Properties properties) {
        super(properties);
    }
    public BoneBowItem() {
        this(new Properties().stacksTo(1).durability(512));
    }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack itemStack, int remainingTicks) {
        super.onUseTick(level, livingEntity, itemStack, remainingTicks);
        int usedTicks = this.getUseDuration(itemStack) - remainingTicks;
        if (usedTicks > 18){
            livingEntity.releaseUsingItem();
        }
    }

    @Override
    public void releaseUsing(ItemStack itemStack, Level level, LivingEntity livingEntity, int i) {
        boolean hasInfinityEnchantment = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, itemStack) > 0;//different from vanilla
        if (livingEntity instanceof Player player) {
            boolean creativePlayerFlag = player.getAbilities().instabuild;
            boolean creativeOrInfinityEnchantment = creativePlayerFlag || hasInfinityEnchantment;
            ItemStack arrowStack = player.getProjectile(itemStack);
            if (!arrowStack.isEmpty() || creativeOrInfinityEnchantment) {
                if (arrowStack.isEmpty()) {
                    arrowStack = new ItemStack(Items.ARROW);
                }

                int usedTicks = this.getUseDuration(itemStack) - i;
                float power = getPowerForTicks(usedTicks);
                if (!(power < 0.1)) {
                    boolean generatedArrow = creativeOrInfinityEnchantment && arrowStack.is(Items.ARROW);
                    if (!level.isClientSide) {
                        generateArrow(itemStack, level, player, arrowStack, power, generatedArrow);
                    }

                    level.playSound(
                            null,
                            player.getX(),
                            player.getY(),
                            player.getZ(),
                            SoundEvents.ARROW_SHOOT,
                            SoundSource.PLAYERS,
                            1.0F,
                            1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + power * 0.5F
                    );

                    if (!generatedArrow && !creativePlayerFlag) {
                        costArrow(itemStack, level, player, arrowStack);
                    }

                    player.awardStat(Stats.ITEM_USED.get(this));
                }
            }
        }
    }

    private static void costArrow(ItemStack itemStack, Level level, Player player, ItemStack itemStack2) {
        if (itemStack2.getItem() instanceof IInfinityEnchantmentChanceCostArrowItem chanceCostArrowItem //different
                && level.random.nextFloat() < chanceCostArrowItem.getCostChance(itemStack)
        ){
            itemStack2.shrink(1);
            if (itemStack2.isEmpty()) {
                player.getInventory().removeItem(itemStack2);
            }
        }
    }

    private static void generateArrow(ItemStack itemStack, Level level, Player player, ItemStack itemStack2, float arrowPower, boolean bl2) {
        ArrowItem arrowItem = (ArrowItem)(itemStack2.getItem() instanceof ArrowItem ? itemStack2.getItem() : Items.ARROW);
        AbstractArrow abstractArrow = arrowItem.createArrow(level, itemStack2, player);
        abstractArrow.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, arrowPower * 3.0F, 1.0F);
        if (arrowPower == 1.0F) {
            abstractArrow.setCritArrow(true);
        }

        int k = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.POWER_ARROWS, itemStack);
        if (k > 0) {
            abstractArrow.setBaseDamage(abstractArrow.getBaseDamage() + k * 0.5 + 0.5);
        }

        int l = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PUNCH_ARROWS, itemStack);
        if (l > 0) {
            abstractArrow.setKnockback(l);
        }

        if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FLAMING_ARROWS, itemStack) > 0) {
            abstractArrow.setSecondsOnFire(100);
        }

        itemStack.hurtAndBreak(1, player, player2 -> player2.broadcastBreakEvent(player.getUsedItemHand()));
        if (bl2 || player.getAbilities().instabuild && (itemStack2.is(Items.SPECTRAL_ARROW) || itemStack2.is(Items.TIPPED_ARROW))) {
            abstractArrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
        }

        level.addFreshEntity(abstractArrow);
    }


    protected float getPowerForTicks(int i) {
        float f = i / 10.F;
        f = (f * f + f * 2.0F) / 3.0F;
        if (f > 1.0F) {
            f = 1.0F;
        }

        return f;
    }

}
