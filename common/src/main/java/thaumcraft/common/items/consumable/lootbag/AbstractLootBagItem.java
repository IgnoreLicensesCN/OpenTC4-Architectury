package thaumcraft.common.items.consumable.lootbag;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.internal.WeightedRandomCollection;
import thaumcraft.common.ThaumcraftSounds;

import java.util.List;
import java.util.function.Function;

public abstract class AbstractLootBagItem extends Item {
    protected final WeightedRandomCollection<Function<RandomSource,ItemStack>> lootSource;

    public AbstractLootBagItem(Properties properties,WeightedRandomCollection<Function<RandomSource,ItemStack>> lootSource) {
        super(properties);
        this.lootSource = lootSource;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, level, list, tooltipFlag);
        list.add(Component.translatable("tc.lootbag"));
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        if (!level.isClientSide) {
            int dropTimes = 8 + level.getRandom().nextInt(5);

            for(int a = 0; a < dropTimes; ++a) {
                //math problem:calculate the chance to get a prime pearl from a bag(or at least from a drop)
                ItemStack drop = lootSource.getRandom(level.random).apply(level.random);
                level.addFreshEntity(new ItemEntity(
                        level,player.getX(),player.getY(),player.getZ(),drop
                ));
            }

            level.playSound(player,player.blockPosition(), ThaumcraftSounds.COINS, SoundSource.PLAYERS, 0.75F, 1.0F);
        }
        var stack = player.getItemInHand(interactionHand);
        stack.shrink(1);

        return new  InteractionResultHolder<>(InteractionResult.sidedSuccess(level.isClientSide), stack);
    }
}
