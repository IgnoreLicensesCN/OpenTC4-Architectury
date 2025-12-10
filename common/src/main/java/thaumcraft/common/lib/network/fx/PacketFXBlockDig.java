package thaumcraft.common.lib.network.fx;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import thaumcraft.client.fx.particles.FXBoreParticles;
import thaumcraft.common.Thaumcraft;

public class PacketFXBlockDig implements IMessage, IMessageHandler<PacketFXBlockDig,IMessage> {
   private int x;
   private int y;
   private int z;
   private int bi;
   private int md;
   private byte dx;
   private byte dy;
   private byte dz;

   public PacketFXBlockDig() {
   }

   public PacketFXBlockDig(int x, int y, int z, byte xd, byte xy, byte xz, int bi, int md) {
      this.x = x;
      this.y = y;
      this.z = z;
      this.bi = bi;
      this.md = md;
      this.dx = xd;
      this.dy = xy;
      this.dz = xz;
   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeInt(this.x);
      buffer.writeInt(this.y);
      buffer.writeInt(this.z);
      buffer.writeInt(this.bi);
      buffer.writeInt(this.md);
      buffer.writeByte(this.dx);
      buffer.writeByte(this.dy);
      buffer.writeByte(this.dz);
   }

   public void fromBytes(ByteBuf buffer) {
      this.x = buffer.readInt();
      this.y = buffer.readInt();
      this.z = buffer.readInt();
      this.bi = buffer.readInt();
      this.md = buffer.readInt();
      this.dx = buffer.readByte();
      this.dy = buffer.readByte();
      this.dz = buffer.readByte();
   }

   @SideOnly(Side.CLIENT)
   public IMessage onMessage(PacketFXBlockDig message, MessageContext ctx) {
      Item item = Item.getItemById(message.bi);
      if ((new ItemStack(item, 1, message.md)).getItemSpriteNumber() == 0 && item instanceof ItemBlock) {
         Block block = Block.getBlockById(message.bi);
         if (block != null) {
            for(int a = 0; a < Thaumcraft.proxy.particleCount(20); ++a) {
               FXBoreParticles fb = (new FXBoreParticles(Thaumcraft.proxy.getClientWorld(), (float)message.dx + Thaumcraft.proxy.getClientWorld().rand.nextFloat(), (float)message.dy + Thaumcraft.proxy.getClientWorld().rand.nextFloat(), (float)message.dz + Thaumcraft.proxy.getClientWorld().rand.nextFloat(), (double)message.x + (double)0.5F, (double)message.y + (double)0.5F, (double)message.z + (double)0.5F, block, Thaumcraft.proxy.getClientWorld().rand.nextInt(6), message.md)).applyColourMultiplier(message.x, message.y, message.z);
               FMLClientHandler.instance().getClient().effectRenderer.addEffect(fb);
            }

            Thaumcraft.proxy.getClientWorld().playSound((float)message.dx + 0.5F, (float)message.dy + 0.5F, (float)message.dz + 0.5F, block.stepSound.getBreakSound(), (block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getPitch() * 0.8F, false);
         }
      } else {
         for(int a = 0; a < Thaumcraft.proxy.particleCount(20); ++a) {
            FXBoreParticles fb = (new FXBoreParticles(Thaumcraft.proxy.getClientWorld(), (float)message.dx + Thaumcraft.proxy.getClientWorld().rand.nextFloat(), (float)message.dy + Thaumcraft.proxy.getClientWorld().rand.nextFloat(), (float)message.dz + Thaumcraft.proxy.getClientWorld().rand.nextFloat(), (double)message.x + (double)0.5F, (double)message.y + (double)0.5F, (double)message.z + (double)0.5F, item, Thaumcraft.proxy.getClientWorld().rand.nextInt(6), message.md)).applyColourMultiplier(message.x, message.y, message.z);
            FMLClientHandler.instance().getClient().effectRenderer.addEffect(fb);
         }

         Thaumcraft.proxy.getClientWorld().playSound((float)message.dx + 0.5F, (float)message.dy + 0.5F, (float)message.dz + 0.5F, Blocks.stone.stepSound.getBreakSound(), (Blocks.stone.stepSound.getVolume() + 1.0F) / 2.0F, Blocks.stone.stepSound.getPitch() * 0.8F, false);
      }

      return null;
   }
}
