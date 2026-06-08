package thaumcraft.common.lib.network.playerdata.updatedata;

import dev.architectury.networking.NetworkManager;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import thaumcraft.common.lib.network.ThaumcraftBaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.network.FriendlyByteBuf;
import thaumcraft.client.lib.PlayerNotifications;
import thaumcraft.client.gui.GuiResearchBrowser;
import thaumcraft.common.Thaumcraft;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.lib.resourcelocations.AspectResourceLocation;
import thaumcraft.common.researches.ResearchAndScannedInfo;

import static thaumcraft.common.researches.ThaumcraftResearchCategories.BASICS;
import static thaumcraft.common.researches.ThaumcraftResearches.ASPECTS;


public class PacketAspectDiscoveryS2C extends ThaumcraftBaseS2CMessage {
    public static final String ID = Thaumcraft.MOD_ID + ":aspect_discovery";
    public static MessageType messageType;

    private final Aspect aspect;

    public PacketAspectDiscoveryS2C(Aspect aspect) {
        this.aspect = aspect;
    }

    // 编码
    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeResourceLocation(aspect.aspectKey);
    }

    // 解码
    public static PacketAspectDiscoveryS2C decode(FriendlyByteBuf buf) {
        return new PacketAspectDiscoveryS2C(Aspect.getAspect(AspectResourceLocation.of(buf.readResourceLocation())));
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        if (!context.getPlayer().level().isClientSide) return;

        var player = context.getPlayer();
        if (player == null) return;
        if (aspect == null) return;

        ResearchAndScannedInfo.getFromPlayer(context.getPlayer()).addResearchAspect(aspect,0);

        Component notification = Component.translatable("tc.addaspectdiscovery", aspect.getName())
                .withStyle(ChatFormatting.GOLD);
        PlayerNotifications.addNotification(notification, aspect);

        player.playSound(
                net.minecraft.sounds.SoundEvents.EXPERIENCE_ORB_PICKUP, // 原 "random.orb" 替换为 Minecraft 1.20+
                0.2F,
                0.5F + player.getRandom().nextFloat() * 0.2F
        );
        GuiResearchBrowser.highlightedResearch.add(ASPECTS.key);
        GuiResearchBrowser.highlightedCategory.add(BASICS.categoryKey);
    }

    @Override
    public MessageType getType() {
        return messageType;
    }
}
