package thaumcraft.common.items.consumable;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.research.ResearchAndScannedInfo;

public class KnowledgeFragmentItem extends Item {
    public KnowledgeFragmentItem() {
        super(new Properties());
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand interactionHand) {
        var stack = player.getItemInHand(interactionHand);
        if (!player.isCreative()) {
            stack.shrink(1);
        }

        if (player instanceof ServerPlayer serverPlayer) {
            for(Aspect a : Aspects.getPrimalAspects()) {
                short q = (short)(world.getRandom().nextInt(2) + 1);
                var info = ResearchAndScannedInfo.getFromPlayer(player);
                info.addResearchAspectAndSyncToPlayer(a,q,serverPlayer);
            }
        }
        return InteractionResultHolder.sidedSuccess(stack, world.isClientSide());
    }
}
