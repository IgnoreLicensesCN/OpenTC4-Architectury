package thaumcraft.common.lib.network.misc;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.world.biome.BiomeGenBase;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.utils.Utils;

public class PacketBiomeChange implements IMessage, IMessageHandler<PacketBiomeChange,IMessage> {
   private int x;
   private int z;
   private short biome;

   public PacketBiomeChange() {
   }

   public PacketBiomeChange(int x, int z, short biome) {
      this.x = x;
      this.z = z;
      this.biome = biome;
   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeInt(this.x);
      buffer.writeInt(this.z);
      buffer.writeShort(this.biome);
   }

   public void fromBytes(ByteBuf buffer) {
      this.x = buffer.readInt();
      this.z = buffer.readInt();
      this.biome = buffer.readShort();
   }

   public IMessage onMessage(PacketBiomeChange message, MessageContext ctx) {
      Utils.setBiomeAt(Thaumcraft.proxy.getClientWorld(), message.x, message.z, BiomeGenBase.getBiome(message.biome));
      return null;
   }
}
