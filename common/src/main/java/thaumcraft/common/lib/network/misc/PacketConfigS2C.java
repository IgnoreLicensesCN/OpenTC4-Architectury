package thaumcraft.common.lib.network.misc;

import com.linearity.opentc4.OpenTC4;
import dev.architectury.networking.NetworkManager;
import thaumcraft.common.lib.network.ThaumcraftBaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.network.FriendlyByteBuf;
import tc4tweak.ConfigurationHandler;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.Config;

public class PacketConfigS2C extends ThaumcraftBaseS2CMessage {
    public static final String ID = Thaumcraft.MOD_ID + ":config";
    public static MessageType messageType;

    private boolean allowCheatSheet;
    private boolean wardedStone;
    private boolean allowMirrors;
    private boolean hardNode;
    private boolean wuss;
    private byte researchDifficulty;
    private int aspectTotalCap;
    private boolean checkWorkbenchRecipes;
    private boolean smallerJars;

    public PacketConfigS2C() {
        this.allowCheatSheet = Config.allowCheatSheet;
        this.wardedStone = Config.wardedStone;
        this.allowMirrors = Config.allowMirrors;
        this.hardNode = Config.hardNode;
        this.wuss = Config.wuss;
        this.researchDifficulty = Config.researchDifficulty;
        this.aspectTotalCap = Config.aspectTotalCap;
        this.checkWorkbenchRecipes = ConfigurationHandler.INSTANCE.isCheckWorkbenchRecipes();
        this.smallerJars = ConfigurationHandler.INSTANCE.isSmallerJars();
    }

    public static void encode(PacketConfigS2C msg, FriendlyByteBuf buf) {
        buf.writeBoolean(msg.allowCheatSheet);
        buf.writeBoolean(msg.wardedStone);
        buf.writeBoolean(msg.allowMirrors);
        buf.writeBoolean(msg.hardNode);
        buf.writeBoolean(msg.wuss);
        buf.writeByte(msg.researchDifficulty);
        buf.writeInt(msg.aspectTotalCap);
        buf.writeBoolean(msg.checkWorkbenchRecipes);
        buf.writeBoolean(msg.smallerJars);
    }

    public static PacketConfigS2C decode(FriendlyByteBuf buf) {
        PacketConfigS2C packet = new PacketConfigS2C();
        packet.allowCheatSheet = buf.readBoolean();
        packet.wardedStone = buf.readBoolean();
        packet.allowMirrors = buf.readBoolean();
        packet.hardNode = buf.readBoolean();
        packet.wuss = buf.readBoolean();
        packet.researchDifficulty = buf.readByte();
        packet.aspectTotalCap = buf.readInt();
        packet.checkWorkbenchRecipes = buf.readBoolean();
        packet.smallerJars = buf.readBoolean();
        return packet;
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
        Config.allowCheatSheet = this.allowCheatSheet;
        Config.wardedStone = this.wardedStone;
        Config.allowMirrors = this.allowMirrors;
        Config.hardNode = this.hardNode;
        Config.wuss = this.wuss;
        Config.researchDifficulty = this.researchDifficulty;
        Config.aspectTotalCap = this.aspectTotalCap;
        ConfigurationHandler.INSTANCE.setCheckWorkbenchRecipes(this.checkWorkbenchRecipes);
        ConfigurationHandler.INSTANCE.setSmallerJars(this.smallerJars);
        OpenTC4.LOGGER.info(
                "Client received server config settings.CHEAT_SHEET[{}], WARDED_STONE[{}], MIRRORS[{}], HARD_NODES[{}], WUSS_MODE[{}], RESEARCH_DIFFICULTY[{}], ASPECT_TOTAL_CAP[{}], CHECK_WORKBENCH_RECIPES[{}], SMALLER_JARS[{}]",
                Config.allowCheatSheet,
                Config.wardedStone,
                Config.allowMirrors,
                Config.hardNode,
                Config.wuss,
                Config.researchDifficulty,
                Config.aspectTotalCap,
                ConfigurationHandler.INSTANCE.isCheckWorkbenchRecipes(),
                ConfigurationHandler.INSTANCE.isSmallerJars()
        );
    }
}
