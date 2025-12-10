package thaumcraft.common.lib.events;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.ServerPlayer;
import net.minecraft.util.ChatComponentTranslation;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchCategoryList;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.playerdata.PacketSyncAspects;
import thaumcraft.common.lib.network.playerdata.PacketSyncResearch;
import thaumcraft.common.lib.network.playerdata.PacketSyncWarp;
import thaumcraft.common.lib.network.playerdata.PacketWarpMessage;
import thaumcraft.common.lib.research.ResearchManager;

import java.util.ArrayList;
import java.util.List;

public class CommandThaumcraft extends CommandBase {
   private List<String> aliases = new ArrayList<>();

   public CommandThaumcraft() {
      this.aliases.add("thaumcraft");
      this.aliases.add("thaum");
      this.aliases.add("tc");
   }

   public String getCommandName() {
      return "thaumcraft";
   }

   public String getCommandUsage(ICommandSender icommandsender) {
      return "/thaumcraft <action> [<player> [<params>]]";
   }

   public List<String> getCommandAliases() {
      return this.aliases;
   }

   public int getRequiredPermissionLevel() {
      return 2;
   }

   public List<String> addTabCompletionOptions(ICommandSender icommandsender, String[] astring) {
      return null;
   }

   public boolean isUsernameIndex(String[] astring, int i) {
      return i == 1;
   }

   public void processCommand(ICommandSender icommandsender, String[] astring) {
      if (astring.length == 0) {
         icommandsender.addChatMessage(new ChatComponentTranslation("§cInvalid arguments"));
         icommandsender.addChatMessage(new ChatComponentTranslation("§cUse /thaumcraft help to get help"));
      } else {
         if (astring[0].equalsIgnoreCase("help")) {
            icommandsender.addChatMessage(new ChatComponentTranslation("§3You can also use /thaum or /tc instead of /thaumcraft."));
            icommandsender.addChatMessage(new ChatComponentTranslation("§3Use this to give research to a player."));
            icommandsender.addChatMessage(new ChatComponentTranslation("  /thaumcraft research <list|player> <all|reset|<research>>"));
            icommandsender.addChatMessage(new ChatComponentTranslation("§3Use this to give aspect research points to a player."));
            icommandsender.addChatMessage(new ChatComponentTranslation("  /thaumcraft aspect <player> <aspect|all> <amount>"));
            icommandsender.addChatMessage(new ChatComponentTranslation("§3Use this to give set a players warp level."));
            icommandsender.addChatMessage(new ChatComponentTranslation("  /thaumcraft warp <player> <add|set> <amount> <PERM|TEMP>"));
            icommandsender.addChatMessage(new ChatComponentTranslation("  not specifying perm or temp will just add normal warp"));
         } else if (astring.length >= 2) {
            if (astring[0].equalsIgnoreCase("research") && astring[1].equalsIgnoreCase("list")) {
               this.listResearch(icommandsender);
            } else {
               ServerPlayer ServerPlayer = getPlayer(icommandsender, astring[1]);
               if (astring[0].equalsIgnoreCase("research")) {
                  if (astring.length == 3) {
                     if (astring[2].equalsIgnoreCase("all")) {
                        this.giveAllResearch(icommandsender, ServerPlayer);
                     } else if (astring[2].equalsIgnoreCase("reset")) {
                        this.resetResearch(icommandsender, ServerPlayer);
                     } else {
                        this.giveResearch(icommandsender, ServerPlayer, astring[2]);
                     }
                  } else {
                     icommandsender.addChatMessage(new ChatComponentTranslation("§cInvalid arguments"));
                     icommandsender.addChatMessage(new ChatComponentTranslation("§cUse /thaumcraft research <list|player> <all|reset|<research>>"));
                  }
               } else if (astring[0].equalsIgnoreCase("aspect")) {
                  if (astring.length == 4) {
                     int i = parseIntWithMin(icommandsender, astring[3], 1);
                     this.giveAspect(icommandsender, ServerPlayer, astring[2], i);
                  } else {
                     icommandsender.addChatMessage(new ChatComponentTranslation("§cInvalid arguments"));
                     icommandsender.addChatMessage(new ChatComponentTranslation("§cUse /thaumcraft aspect <player> <aspect|all> <amount>"));
                  }
               } else if (astring[0].equalsIgnoreCase("warp")) {
                  if (astring.length >= 4 && astring[2].equalsIgnoreCase("set")) {
                     int i = parseIntWithMin(icommandsender, astring[3], 0);
                     this.setWarp(icommandsender, ServerPlayer, i, astring.length == 5 ? astring[4] : "");
                  } else if (astring.length >= 4 && astring[2].equalsIgnoreCase("add")) {
                     int i = parseIntBounded(icommandsender, astring[3], -100, 100);
                     this.addWarp(icommandsender, ServerPlayer, i, astring.length == 5 ? astring[4] : "");
                  } else {
                     icommandsender.addChatMessage(new ChatComponentTranslation("§cInvalid arguments"));
                     icommandsender.addChatMessage(new ChatComponentTranslation("§cUse /thaumcraft warp <player> <add|set> <amount> <PERM|TEMP>"));
                  }
               } else {
                  icommandsender.addChatMessage(new ChatComponentTranslation("§cInvalid arguments"));
                  icommandsender.addChatMessage(new ChatComponentTranslation("§cUse /thaumcraft help to get help"));
               }
            }
         } else {
            icommandsender.addChatMessage(new ChatComponentTranslation("§cInvalid arguments"));
            icommandsender.addChatMessage(new ChatComponentTranslation("§cUse /thaumcraft help to get help"));
         }

      }
   }

   private void giveAspect(ICommandSender icommandsender, ServerPlayer player, String string, int i) {
      if (string.equalsIgnoreCase("all")) {
         for(Aspect aspect : Aspect.aspects.values()) {
            Thaumcraft.proxy.playerKnowledge.addAspectPool(player.getCommandSenderName(), aspect, (short)i);
         }

         ResearchManager.scheduleSave(player);
         player.addChatMessage(new ChatComponentTranslation("§5" + icommandsender.getCommandSenderName() + " gave you " + i + " of all the aspects."));
         icommandsender.addChatMessage(new ChatComponentTranslation("§5Success!"));
         PacketHandler.INSTANCE.sendTo(new PacketSyncAspects(player), player);
      } else {
         Aspect aspect = Aspect.getAspect(string);
         if (aspect == null) {
            for(Aspect a : Aspect.aspects.values()) {
               if (string.equalsIgnoreCase(a.getName())) {
                  aspect = a;
                  break;
               }
            }
         }

         if (aspect != null) {
            Thaumcraft.proxy.playerKnowledge.addAspectPool(player.getCommandSenderName(), aspect, (short)i);
            ResearchManager.scheduleSave(player);
            PacketHandler.INSTANCE.sendTo(new PacketSyncAspects(player), player);
            player.addChatMessage(new ChatComponentTranslation("§5" + icommandsender.getCommandSenderName() + " gave you " + i + " " + aspect.getName()));
            icommandsender.addChatMessage(new ChatComponentTranslation("§5Success!"));
         } else {
            icommandsender.addChatMessage(new ChatComponentTranslation("§cAspect does not exist."));
         }
      }

   }

   private void setWarp(ICommandSender icommandsender, ServerPlayer player, int i, String type) {
      if (type.equalsIgnoreCase("PERM")) {
         Thaumcraft.proxy.playerKnowledge.setWarpPerm(player.getCommandSenderName(), i);
         ResearchManager.scheduleSave(player);
         PacketHandler.INSTANCE.sendTo(new PacketSyncWarp(player, (byte)0), player);
      } else if (type.equalsIgnoreCase("TEMP")) {
         Thaumcraft.proxy.playerKnowledge.setWarpTemp(player.getCommandSenderName(), i);
         ResearchManager.scheduleSave(player);
         PacketHandler.INSTANCE.sendTo(new PacketSyncWarp(player, (byte)2), player);
      } else {
         Thaumcraft.proxy.playerKnowledge.setWarpSticky(player.getCommandSenderName(), i);
         ResearchManager.scheduleSave(player);
         PacketHandler.INSTANCE.sendTo(new PacketSyncWarp(player, (byte)1), player);
      }

      player.addChatMessage(new ChatComponentTranslation("§5" + icommandsender.getCommandSenderName() + " set your warp to " + i));
      icommandsender.addChatMessage(new ChatComponentTranslation("§5Success!"));
   }

   private void addWarp(ICommandSender icommandsender, ServerPlayer player, int i, String type) {
      if (type.equalsIgnoreCase("PERM")) {
         Thaumcraft.proxy.playerKnowledge.addWarpPerm(player.getCommandSenderName(), i);
         ResearchManager.scheduleSave(player);
         PacketHandler.INSTANCE.sendTo(new PacketSyncWarp(player, (byte)0), player);
         PacketHandler.INSTANCE.sendTo(new PacketWarpMessage(player, (byte)0, i), player);
      } else if (type.equalsIgnoreCase("TEMP")) {
         Thaumcraft.proxy.playerKnowledge.addWarpTemp(player.getCommandSenderName(), i);
         ResearchManager.scheduleSave(player);
         PacketHandler.INSTANCE.sendTo(new PacketSyncWarp(player, (byte)2), player);
         PacketHandler.INSTANCE.sendTo(new PacketWarpMessage(player, (byte)2, i), player);
      } else {
         Thaumcraft.proxy.playerKnowledge.addWarpSticky(player.getCommandSenderName(), i);
         ResearchManager.scheduleSave(player);
         PacketHandler.INSTANCE.sendTo(new PacketSyncWarp(player, (byte)1), player);
         PacketHandler.INSTANCE.sendTo(new PacketWarpMessage(player, (byte)1, i), player);
      }

      player.addChatMessage(new ChatComponentTranslation("§5" + icommandsender.getCommandSenderName() + " added " + i + " warp to your total."));
      icommandsender.addChatMessage(new ChatComponentTranslation("§5Success!"));
   }

   private void listResearch(ICommandSender icommandsender) {
      for(ResearchCategoryList cat : ResearchCategories.researchCategories.values()) {
         for(ResearchItem ri : cat.research.values()) {
            icommandsender.addChatMessage(new ChatComponentTranslation("§5" + ri.key));
         }
      }

   }

   void giveResearch(ICommandSender icommandsender, ServerPlayer player, String research) {
      if (ResearchCategories.getResearch(research) != null) {
         this.giveRecursiveResearch(player, research);
         PacketHandler.INSTANCE.sendTo(new PacketSyncResearch(player), player);
         player.addChatMessage(new ChatComponentTranslation("§5" + icommandsender.getCommandSenderName() + " gave you " + research + " research and its requisites."));
         icommandsender.addChatMessage(new ChatComponentTranslation("§5Success!"));
      } else {
         icommandsender.addChatMessage(new ChatComponentTranslation("§cResearch does not exist."));
      }

   }

   void giveRecursiveResearch(ServerPlayer player, String research) {
      if (!ResearchManager.isResearchComplete(player.getCommandSenderName(), research)) {
         Thaumcraft.proxy.getResearchManager().completeResearch(player, research);
         if (ResearchCategories.getResearch(research).parents != null) {
            for(String rsi : ResearchCategories.getResearch(research).parents) {
               this.giveRecursiveResearch(player, rsi);
            }
         }

         if (ResearchCategories.getResearch(research).parentsHidden != null) {
            for(String rsi : ResearchCategories.getResearch(research).parentsHidden) {
               this.giveRecursiveResearch(player, rsi);
            }
         }

         if (ResearchCategories.getResearch(research).siblings != null) {
            for(String rsi : ResearchCategories.getResearch(research).siblings) {
               this.giveRecursiveResearch(player, rsi);
            }
         }
      }

   }

   void giveAllResearch(ICommandSender icommandsender, ServerPlayer player) {
      for(ResearchCategoryList cat : ResearchCategories.researchCategories.values()) {
         for(ResearchItem ri : cat.research.values()) {
            if (!ResearchManager.isResearchComplete(player.getCommandSenderName(), ri.key)) {
               Thaumcraft.proxy.getResearchManager().completeResearch(player, ri.key);
            }
         }
      }

      player.addChatMessage(new ChatComponentTranslation("§5" + icommandsender.getCommandSenderName() + " has given you all research."));
      icommandsender.addChatMessage(new ChatComponentTranslation("§5Success!"));
      PacketHandler.INSTANCE.sendTo(new PacketSyncResearch(player), player);
   }

   void resetResearch(ICommandSender icommandsender, ServerPlayer player) {
      Thaumcraft.proxy.getPlayerKnowledge().researchCompleted.remove(player.getCommandSenderName());

      for(ResearchCategoryList cat : ResearchCategories.researchCategories.values()) {
         for(ResearchItem ri : cat.research.values()) {
            if (ri.isAutoUnlock()) {
               Thaumcraft.proxy.getResearchManager().completeResearch(player, ri.key);
            }
         }
      }

      player.addChatMessage(new ChatComponentTranslation("§5" + icommandsender.getCommandSenderName() + " has reset you research."));
      icommandsender.addChatMessage(new ChatComponentTranslation("§5Success!"));
      PacketHandler.INSTANCE.sendTo(new PacketSyncResearch(player), player);
   }
}
