package thaumcraft.common.items;

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
import thaumcraft.common.entities.projectile.EntityAlumentum;

public class AlumentumItem extends Item {
    public AlumentumItem() {
        super(new Properties());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand interactionHand) {
        if (!player.isCreative()) {
            player.getItemInHand(interactionHand).shrink(1);
        }

        world.playSound(player,player, SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 0.3F, 0.4F / (world.random.nextFloat() * 0.4F + 0.8F));
        if (Platform.getEnvironment() != Env.CLIENT) {
            world.addFreshEntity(new EntityAlumentum(world, player));
        }
        return super.use(world, player, interactionHand);
    }
}
