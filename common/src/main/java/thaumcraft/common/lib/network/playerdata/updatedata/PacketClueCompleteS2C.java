package thaumcraft.common.lib.network.playerdata.updatedata;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import thaumcraft.client.lib.PlayerNotifications;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.ThaumcraftSounds;
import thaumcraft.common.lib.network.ThaumcraftBaseS2CMessage;
import thaumcraft.common.lib.resourcelocations.ClueResourceLocation;
import thaumcraft.api.research.ResearchAndScannedInfo;

public class PacketClueCompleteS2C extends ThaumcraftBaseS2CMessage {
    public static final String ID = Thaumcraft.MOD_ID + ":clue_complete";

    public static MessageType messageType;

    private final ClueResourceLocation key;

    public PacketClueCompleteS2C(ClueResourceLocation key) {
        this.key = key;
    }

    public static void encode(PacketClueCompleteS2C msg, FriendlyByteBuf buf) {
        buf.writeResourceLocation(msg.key);
    }

    public static PacketClueCompleteS2C decode(FriendlyByteBuf buf) {
        return new PacketClueCompleteS2C(ClueResourceLocation.of(buf.readResourceLocation()));
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
        Player player = context.getPlayer();

        ResearchAndScannedInfo.getFromLiving(player).addClue(this.key);

        PlayerNotifications.addNotification(Component.translatable("tc.addclue").withStyle(ChatFormatting.GREEN));
        player.playSound(ThaumcraftSounds.LEARN, 0.2F, 1.0F + player.getRandom().nextFloat() * 0.1F);

        //TODO:[maybe wont finished]resourcepack defined clue hint behavior and show it
//        if (Minecraft.getInstance().screen instanceof GuiResearchBrowser gui) {
//            var al = GuiResearchBrowser.completedClue.get(player.getGameProfile().getName());
//            if (al == null) al = new ArrayList<>();
//            al.add(key);
//            GuiResearchBrowser.completedClue.put(player.getGameProfile().getName(), al);
//            gui.updateResearch();
//        }
    }
}
