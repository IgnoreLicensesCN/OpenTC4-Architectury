package thaumcraft.common.items.misc;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static thaumcraft.common.lib.research.ResearchManager.unlockResearchForPlayer;

public class CrimsonRitesItem extends Item {
    public CrimsonRitesItem() {
        super(new Properties().stacksTo(1).rarity(Rarity.UNCOMMON));
    }


    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, world, tooltip, flag);
        tooltip.add(Component.translatable("item.CrimsonRitesItem.text.1")
                .withStyle(ChatFormatting.DARK_PURPLE));
        tooltip.add(Component.translatable("item.CrimsonRitesItem.text.2")
                .withStyle(ChatFormatting.DARK_BLUE));
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {

        if (player instanceof ServerPlayer serverPlayer) {
            unlockResearchForPlayer(level, serverPlayer, "CRIMSON");
        }
        return super.use(level, player, interactionHand);
    }
}
