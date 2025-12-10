package thaumcraft.common.lib.network.misc;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.Player;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import thaumcraft.common.entities.golems.ItemGolemBell;
import thaumcraft.common.items.equipment.ItemElementalShovel;
import thaumcraft.common.items.wands.ItemWandCasting;
import thaumcraft.common.items.wands.WandManager;

public class PacketItemKeyToServer implements IMessage, IMessageHandler<PacketItemKeyToServer,IMessage> {
   private int dim;
   private int playerid;
   private byte key;

   public PacketItemKeyToServer() {
   }

   public PacketItemKeyToServer(Player player, int key) {
      this.dim = player.worldObj.provider.dimensionId;
      this.playerid = player.getEntityId();
      this.key = (byte)key;
   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeInt(this.dim);
      buffer.writeInt(this.playerid);
      buffer.writeByte(this.key);
   }

   public void fromBytes(ByteBuf buffer) {
      this.dim = buffer.readInt();
      this.playerid = buffer.readInt();
      this.key = buffer.readByte();
   }

   public IMessage onMessage(PacketItemKeyToServer message, MessageContext ctx) {
      World world = DimensionManager.getWorld(message.dim);
       if (world != null) {
           Entity player = world.getEntityByID(message.playerid);
           if (player instanceof Player && ((Player) player).getHeldItem() != null) {
               if (message.key == 0 && ((Player) player).getHeldItem().getItem() instanceof ItemGolemBell) {
                   ItemGolemBell.resetMarkers(((Player) player).getHeldItem(), world, (Player) player);
               }

               if (message.key == 1 && ((Player) player).getHeldItem().getItem() instanceof ItemWandCasting) {
                   WandManager.toggleMisc(((Player) player).getHeldItem(), world, (Player) player);
               }

               if (message.key == 1 && ((Player) player).getHeldItem().getItem() instanceof ItemElementalShovel) {
                   ItemElementalShovel var10000 = (ItemElementalShovel) ((Player) player).getHeldItem().getItem();
                   byte b = ItemElementalShovel.getOrientation(((Player) player).getHeldItem());
                   var10000 = (ItemElementalShovel) ((Player) player).getHeldItem().getItem();
                   ItemElementalShovel.setOrientation(((Player) player).getHeldItem(), (byte) (b + 1));
               }
           }

       }
       return null;
   }
}
