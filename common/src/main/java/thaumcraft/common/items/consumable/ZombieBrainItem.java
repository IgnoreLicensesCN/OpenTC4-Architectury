package thaumcraft.common.items.consumable;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import thaumcraft.common.Thaumcraft;

public class ZombieBrainItem extends Item {
    public ZombieBrainItem() {
        super(new Item.Properties()
                .food(new FoodProperties.Builder()
                        .nutrition(4)
                        .saturationMod(0.2F)
                        .effect(
                                new MobEffectInstance(
                                        MobEffects.HUNGER,
                                        30 * 20, // 秒 → tick
                                        0
                                ),
                                0.8F
                        )
                        .build()
                )
        );
    }
    @Override
    public @NotNull ItemStack finishUsingItem(
            ItemStack stack,
            Level level,
            LivingEntity entity
    ) {
        if (!level.isClientSide && entity instanceof ServerPlayer player) {
            if (level.getRandom().nextFloat() < 0.1F) {
                Thaumcraft.addStickyWarpToPlayer(player, 1);
            } else {
                Thaumcraft.addWarpToPlayer(
                        player,
                        1 + level.getRandom().nextInt(3),
                        true
                );
            }
        }

        return super.finishUsingItem(stack, level, entity);
    }
}

}
