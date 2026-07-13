package thaumcraft.common.items.mateiral;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import thaumcraft.common.entities.ThaumcraftEntities;
import thaumcraft.common.lib.effects.ThaumcraftEffects;

public class DegradableTaintedMaterialItem extends Item {
    public DegradableTaintedMaterialItem(Properties properties) {
        super(properties);
    }
    public DegradableTaintedMaterialItem() {
        this(new Properties());
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int i, boolean bl) {
        if (!level.isClientSide
                && entity instanceof LivingEntity living
                && !living.getType().is(ThaumcraftEntities.EntityTags.UNDEAD)
                && living.getMobType() != MobType.UNDEAD
                && !living.hasEffect(ThaumcraftEffects.ThaumcraftEffectTypeInstances.FLUX_TAINT())
                && level.getRandom().nextInt(4321) <= stack.getCount()
        ) {
            living.addEffect(new MobEffectInstance(ThaumcraftEffects.ThaumcraftEffectTypeInstances.FLUX_TAINT(), 120, 0));
            if (entity instanceof Player p) {
                p.sendSystemMessage(
                        Component.translatable(
                                "tc.taint_item_poison",
                                        stack.getDisplayName()
                                )
                                .withStyle(ChatFormatting.ITALIC)
                                .withStyle(ChatFormatting.DARK_PURPLE)
                );
            }
            stack.shrink(1);
        }
    }
}
