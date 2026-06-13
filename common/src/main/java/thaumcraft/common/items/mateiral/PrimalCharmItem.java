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
import thaumcraft.common.entities.EntityAspectOrb;
import thaumcraft.common.lib.network.playerdata.updatedata.PacketClueCompleteS2C;

import java.util.List;
import java.util.Random;

import static thaumcraft.api.research.ThaumcraftResearches.FOCUS_PRIMAL;

public class PrimalCharmItem extends Item {
    public PrimalCharmItem() {
        super(new Properties().stacksTo(1));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flag){
        super.appendHoverText(stack, world, tooltip, flag);
        var player = Minecraft.getInstance().player;
        if (player == null) return;

        Random rand = new Random(stack.hashCode() + player.tickCount / 120);
        int r = rand.nextInt(200);
        if (r < 25) {
            tooltip.add(Component.literal("tc.primalcharm." + rand.nextInt(5)).withStyle(ChatFormatting.GOLD));
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
                };//but dont add other primal aspect(if there is)here

                if (aspect != null) {
                    EntityAspectOrb orb = new EntityAspectOrb(world, entity.getX(), entity.getY(), entity.getZ(), aspect, 1);
                    world.addFreshEntity(orb);
                }
            } else if (r == 42
                    && entity instanceof ServerPlayer player
                    && !FOCUS_PRIMAL.isPlayerCompletedResearch(player)
                    && !FOCUS_PRIMAL.playerHasClue(player)
            ) {
                var clueLoc = FOCUS_PRIMAL.key.convertToClueResLoc();
                player.sendSystemMessage(
                        Component.translatable("tc.primalcharm.trigger")
                                .withStyle(ChatFormatting.DARK_PURPLE, ChatFormatting.ITALIC),
                        true //maybe cool?
                );
                new PacketClueCompleteS2C(clueLoc).sendTo(player);
                FOCUS_PRIMAL.giveClueToPlayer(player);
            }
        }
    }
}
