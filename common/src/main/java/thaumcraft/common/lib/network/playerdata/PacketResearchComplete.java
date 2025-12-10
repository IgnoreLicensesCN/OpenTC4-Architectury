package thaumcraft.common.lib.network.playerdata;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.StatCollector;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.client.gui.GuiResearchBrowser;
import thaumcraft.client.lib.ClientTickEventsFML;
import thaumcraft.client.lib.PlayerNotifications;
import thaumcraft.common.Thaumcraft;

import java.util.ArrayList;

public class PacketResearchComplete implements IMessage, IMessageHandler<PacketResearchComplete,IMessage> {
   private String key;

   public PacketResearchComplete() {
   }

   public PacketResearchComplete(String key) {
      this.key = key;
   }

   public void toBytes(ByteBuf buffer) {
      ByteBufUtils.writeUTF8String(buffer, this.key);
   }

   public void fromBytes(ByteBuf buffer) {
      this.key = ByteBufUtils.readUTF8String(buffer);
   }

   @SideOnly(Side.CLIENT)
   public IMessage onMessage(PacketResearchComplete message, MessageContext ctx) {
      if (message.key != null && !message.key.isEmpty()) {
         Thaumcraft.proxy.getResearchManager().completeResearch(Minecraft.getMinecraft().thePlayer, message.key);
         if (message.key.startsWith("@")) {
            String text = StatCollector.translateToLocal("tc.addclue");
            PlayerNotifications.addNotification("§a" + text);
            Minecraft.getMinecraft().thePlayer.playSound("thaumcraft:learn", 0.2F, 1.0F + Minecraft.getMinecraft().thePlayer.worldObj.rand.nextFloat() * 0.1F);
         } else if (!ResearchCategories.getResearch(message.key).isVirtual()) {
            ClientTickEventsFML.researchPopup.queueResearchInformation(ResearchCategories.getResearch(message.key));
            GuiResearchBrowser.highlightedItem.add(message.key);
            GuiResearchBrowser.highlightedItem.add(ResearchCategories.getResearch(message.key).category);
         }

         if (Minecraft.getMinecraft().currentScreen instanceof GuiResearchBrowser) {
            ArrayList<String> al = GuiResearchBrowser.completedResearch.get(Minecraft.getMinecraft().thePlayer.getCommandSenderName());
            if (al == null) {
               al = new ArrayList<>();
            }

            al.add(message.key);
            GuiResearchBrowser.completedResearch.put(Minecraft.getMinecraft().thePlayer.getCommandSenderName(), al);
            ((GuiResearchBrowser)Minecraft.getMinecraft().currentScreen).updateResearch();
         }
      }

      return null;
   }
}
