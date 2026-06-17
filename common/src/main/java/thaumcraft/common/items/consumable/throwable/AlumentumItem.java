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
import thaumcraft.common.entities.projectile.AlumentumEntity;
import thaumcraft.common.items.abstracts.IAlchemicalFurnaceSpeederFuel;

public class AlumentumItem extends Item implements IAlchemicalFurnaceSpeederFuel {
    public AlumentumItem() {
        this(new Properties());
    }
    public AlumentumItem(Properties properties) {
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
            var toThrow = new AlumentumEntity(player, world);
            toThrow.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 0.75F, 1.0F);
            world.addFreshEntity(toThrow);
        }
        return InteractionResultHolder.sidedSuccess(stack, world.isClientSide());
    }
}
