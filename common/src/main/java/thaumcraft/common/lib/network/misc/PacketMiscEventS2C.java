package thaumcraft.common.lib.network.misc;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import thaumcraft.client.lib.ClientTickEventsFML;
import thaumcraft.client.lib.RenderEventHandler;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.ThaumcraftSounds;
import thaumcraft.common.tiles.TileArcaneBore;

public class PacketMiscEventS2C  extends BaseS2CMessage {
    public static final String ID = Thaumcraft.MOD_ID + ":misc_event";
    private final short type;
    public static final short WARP_EVENT = 0;
    public static final short MIST_EVENT = 1;
    public static final short MIST_EVENT_SHORT = 2;

    public PacketMiscEventS2C(short type){
        this.type = type;
    }

    public static PacketMiscEventS2C decode(FriendlyByteBuf buf) {
        short type = buf.readShort();
        return new PacketMiscEventS2C(type);
    }

    public static void encode(PacketMiscEventS2C msg, FriendlyByteBuf buf) {
        buf.writeShort(msg.type);
    }

    public static MessageType messageType;

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
        Player p = Minecraft.getInstance().player;
        switch (type) {
            case 0:
                ClientTickEventsFML.warpVignette = 100;
                p.level().playSound(
                        p,
                        p.getX(), p.getY(), p.getZ(),
                        ThaumcraftSounds.HEARTBEAT, SoundSource.AMBIENT,
                        1.0F, 1.0F);
                break;
            case 1:
                RenderEventHandler.fogFiddled = true;
                RenderEventHandler.fogDuration = 2400;
                break;
            case 2:
                RenderEventHandler.fogFiddled = true;
                if (RenderEventHandler.fogDuration < 200) {
                    RenderEventHandler.fogDuration = 200;
                }
            case 3:
        }
    }
}
