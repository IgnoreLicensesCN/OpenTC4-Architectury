package thaumcraft.common.items.mateiral;


import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.entities.EntityAspectOrb;
import thaumcraft.common.lib.network.playerdata.PacketClueCompleteS2C;
import thaumcraft.common.lib.network.playerdata.PacketResearchCompleteS2C;
import thaumcraft.common.lib.research.ResearchManager;

import java.util.List;
import java.util.Random;

import static thaumcraft.common.researches.ThaumcraftResearches.FOCUS_PRIMAL;

public class PrimalCharmItem extends Item {
    public PrimalCharmItem() {
        super(new Properties().stacksTo(1));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flag){
        var player = Minecraft.getInstance().player;
        if (player == null) return;

        Random rand = new Random(stack.hashCode() + player.tickCount / 120);
        int r = rand.nextInt(200);
        if (r < 25) {
            tooltip.add(Component.literal("tc.primalcharm." + rand.nextInt(5)).withStyle(style -> style.withColor(
                    ChatFormatting.GOLD)));
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int i, boolean bl) {

        if (Platform.getEnvironment() != Env.CLIENT) {
            int r = world.getRandom().nextInt(20000);

            if (r < 20) {
                Aspect aspect = switch (world.getRandom()
                        .nextInt(6)) {
                    case 0 -> Aspects.AIR;
                    case 1 -> Aspects.EARTH;
                    case 2 -> Aspects.FIRE;
                    case 3 -> Aspects.WATER;
                    case 4 -> Aspects.ORDER;
                    case 5 -> Aspects.ENTROPY;
                    default -> null;
                };

                if (aspect != null) {
                    EntityAspectOrb orb = new EntityAspectOrb(world, entity.getX(), entity.getY(), entity.getZ(), aspect, 1);
                    world.addFreshEntity(orb);
                }
            } else if (r == 42
                    && entity instanceof ServerPlayer player
                    && FOCUS_PRIMAL.isPlayerCompletedResearch(player.getGameProfile().getName())
                    && !ResearchManager.isClueComplete(player.getGameProfile().getName(), FOCUS_PRIMAL.key.convertToClueResLoc())
            ) {
                var clueLoc = FOCUS_PRIMAL.key.convertToClueResLoc();
                player.sendSystemMessage(
                        Component.translatable("tc.primalcharm.trigger")
                                .withStyle(ChatFormatting.DARK_PURPLE, ChatFormatting.ITALIC)
                );
                new PacketClueCompleteS2C(clueLoc).sendTo(player);
                Thaumcraft.researchManager.completeClue(player, clueLoc);
            }
        }
    }
}
