package thaumcraft.common.lib.network.playerdata.updatedata;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.MessageType;
import lombok.RequiredArgsConstructor;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.client.lib.PlayerNotifications;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.network.ThaumcraftBaseS2CMessage;
import thaumcraft.api.research.ResearchAndScannedInfo;

import static thaumcraft.common.lib.network.ByteBufUtil.readAspect;
import static thaumcraft.common.lib.network.ByteBufUtil.writeAspect;

@RequiredArgsConstructor
public class PacketUpdateAspectS2C extends ThaumcraftBaseS2CMessage {
    public static final String ID = Thaumcraft.MOD_ID + ":aspect_pool";
    public static MessageType messageType;

    private final Aspect key;
    private final int amountChanged;
    private final int total;
    private static long lastSound = 0L;
    public static final long SOUND_DELAY = 100L;

    public static void encode(PacketUpdateAspectS2C msg, FriendlyByteBuf buf) {
        writeAspect(buf, msg.key);
        buf.writeInt(msg.amountChanged);
        buf.writeInt(msg.total);
    }

    public static PacketUpdateAspectS2C decode(FriendlyByteBuf buf) {
        var key = readAspect(buf);
        int amount = buf.readInt();
        int total = buf.readInt();
        return new PacketUpdateAspectS2C(key, amount, total);
    }

    @Override
    public MessageType getType() {
        return messageType;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        encode(this, buf);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        Aspect aspect = this.key;
        var player = Minecraft.getInstance().player;
        if (aspect != null && player != null) {
            var info = ResearchAndScannedInfo.getFromLiving(player);
            info.setResearchAspect(
                    aspect,
                    this.total
            );

            if (this.amountChanged > 0) {
                var text = Component.translatable(
                        "tc.addaspectpool",
                        Component.literal(String.valueOf(this.amountChanged)),
                        aspect.getImageComponent().copy().append(aspect.getName())
                );

                PlayerNotifications.addNotification(text);

                for (int i = 0; i < this.amountChanged; i++) {
                    PlayerNotifications.addAspectNotification(aspect);
                }

                if (System.currentTimeMillis() > lastSound) {
                    player.playSound(
                            SoundEvents.EXPERIENCE_ORB_PICKUP,
                            0.1F,
                            0.9F + player.getRandom()
                                    .nextFloat() * 0.2F
                    );
                    lastSound = System.currentTimeMillis() + SOUND_DELAY;
                }
            }
        }
    }
}
