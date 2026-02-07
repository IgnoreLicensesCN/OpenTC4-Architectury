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
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.network.playerdata.PacketAspectPoolS2C;
import thaumcraft.common.lib.research.ResearchManager;

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
                Thaumcraft.playerKnowledge.addAspectPool(player.getGameProfile().getName(), a, q);
                ResearchManager.scheduleSave(serverPlayer.getGameProfile().getName());
                new PacketAspectPoolS2C(a.getAspectKey(), q, Thaumcraft.playerKnowledge.getAspectPoolFor(player.getGameProfile().getName(), a)).sendTo(serverPlayer);
            }
        }
        return InteractionResultHolder.sidedSuccess(stack, world.isClientSide());
    }
}
