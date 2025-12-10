package thaumcraft.common.lib.network.misc;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.Config;

public class PacketConfig implements IMessage, IMessageHandler<PacketConfig,IMessage> {
   boolean b1;
   boolean b2;
   boolean b3;
   boolean b4;
   boolean b5;
   byte by1;
   int bi2;

   public void toBytes(ByteBuf dos) {
      dos.writeBoolean(Config.allowCheatSheet);
      dos.writeBoolean(Config.wardedStone);
      dos.writeBoolean(Config.allowMirrors);
      dos.writeBoolean(Config.hardNode);
      dos.writeBoolean(Config.wuss);
      dos.writeByte(Config.researchDifficulty);
      dos.writeInt(Config.aspectTotalCap);
   }

   public void fromBytes(ByteBuf dat) {
      this.b1 = dat.readBoolean();
      this.b2 = dat.readBoolean();
      this.b3 = dat.readBoolean();
      this.b4 = dat.readBoolean();
      this.b5 = dat.readBoolean();
      this.by1 = dat.readByte();
      this.bi2 = dat.readInt();
   }

   public IMessage onMessage(PacketConfig message, MessageContext ctx) {
      Config.allowCheatSheet = message.b1;
      Config.wardedStone = message.b2;
      Config.allowMirrors = message.b3;
      Config.hardNode = message.b4;
      Config.wuss = message.b5;
      Config.researchDifficulty = message.by1;
      Config.aspectTotalCap = message.bi2;
      Thaumcraft.log.info("Client received server config settings.");
       Thaumcraft.log.info("CHEAT_SHEET[{}], WARDED_STONE[{}], MIRRORS[{}], HARD_NODES[{}], WUSS_MODE[{}], RESEARCH_DIFFICULTY[{}], ASPECT_TOTAL_CAP[{}", Config.allowCheatSheet, Config.wardedStone, Config.allowMirrors, Config.hardNode, Config.wuss, Config.researchDifficulty, Config.aspectTotalCap);
      return null;
   }
}
