package thaumcraft.common.items.consumable.throwable;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import thaumcraft.common.entities.projectile.TaintBottleEntity;

public class TaintBottleItem extends Item {
    public TaintBottleItem() {
        this(new Properties());
    }
    public TaintBottleItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand interactionHand) {
        var stack = player.getItemInHand(interactionHand);
        if (!player.isCreative()) {
            stack.shrink(1);
        }

        world.playSound(
                player,
                player,
                SoundEvents.ARROW_SHOOT,
                SoundSource.PLAYERS,
                0.3F,
                0.4F / (world.random.nextFloat() * 0.4F + 0.8F)
        );
        if (!world.isClientSide) {
            var toThrow = new TaintBottleEntity(player, world);
            toThrow.shootFromRotation(player, player.getXRot()-20, player.getYRot(), 0.0F, 0.5F, 1.0F);
            world.addFreshEntity(toThrow);
        }
        return InteractionResultHolder.sidedSuccess(stack, world.isClientSide());
    }
}
