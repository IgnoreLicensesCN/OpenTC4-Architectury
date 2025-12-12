package thaumcraft.common.items.consumable;

import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import thaumcraft.common.entities.projectile.EntityAlumentum;

public class AlumentumItem extends Item {
    public AlumentumItem() {
        super(new Properties());
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand interactionHand) {
        var stack = player.getItemInHand(interactionHand);
        if (!player.isCreative()) {
            stack.shrink(1);
        }

        world.playSound(player,player, SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 0.3F, 0.4F / (world.random.nextFloat() * 0.4F + 0.8F));
        if (Platform.getEnvironment() != Env.CLIENT) {
            world.addFreshEntity(new EntityAlumentum(player, world));
        }
        return InteractionResultHolder.sidedSuccess(stack, world.isClientSide());
    }
}
