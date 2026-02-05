package thaumcraft.common.lib.events;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchCategory;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.network.playerdata.PacketSyncAspectsS2C;
import thaumcraft.common.lib.network.playerdata.PacketSyncResearchS2C;
import thaumcraft.common.lib.network.playerdata.PacketSyncWarpS2C;
import thaumcraft.common.lib.network.playerdata.PacketWarpMessageS2C;
import thaumcraft.common.lib.research.ResearchManager;

import java.util.Objects;

public class CommandThaumcraft{

   public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context, Commands.CommandSelection selection) {

      // 主命令 /thaumcraft
      dispatcher.register(
              Commands.literal("thaumcraft")
                      .requires(src -> src.hasPermission(2))
                      .then(Commands.literal("help")
                              .executes(ctx -> {
                                 sendHelp(ctx.getSource());
                                 return 1;
                              })
                      )
                      .then(Commands.literal("research")
                              .then(Commands.argument("player", StringArgumentType.word())
                                      .then(Commands.argument("action", StringArgumentType.word())
                                              .executes(ctx -> {
//                                                  String playerArg = StringArgumentType.getString(ctx, "player");
                                                 String playerName = StringArgumentType.getString(ctx, "player");
                                                 if (Objects.equals(playerName, "list")) {
                                                     listResearch(ctx.getSource());
                                                 }
                                                 String action = StringArgumentType.getString(ctx, "action");
                                                 ServerPlayer player = getPlayer(ctx.getSource(), playerName);
                                                 if (player == null) {
                                                    ctx.getSource().sendFailure(Component.literal("Player not found"));
                                                    return 0;
                                                 }
                                                 if (Objects.equals(action,"all")){
                                                     giveAllResearch(ctx.getSource(), player);
                                                 }else if (Objects.equals(action,"reset")){
                                                     resetResearch(ctx.getSource(), player);
                                                 }else{
                                                     if (action == null){
                                                         ctx.getSource().sendFailure(Component.literal("research not found"));
                                                         return 0;
                                                     }
                                                     if (action.startsWith("@")){
                                                         var clueLoc = ResourceLocation.tryParse(action.substring(1));
                                                         if (clueLoc != null) {
                                                             giveClue(ctx.getSource(), player, clueLoc);
                                                             return 1;
                                                         }else {
                                                             ctx.getSource().sendFailure(Component.literal("research not found"));
                                                             return 0;
                                                         }
                                                     }
                                                     var researchLoc = ResourceLocation.tryParse(action);
                                                     if (researchLoc != null) {
                                                         giveResearch(ctx.getSource(), player, researchLoc);
                                                     }else {
                                                         ctx.getSource().sendFailure(Component.literal("research not found"));
                                                         return 0;
                                                     }
                                                 }
                                                 return 1;
                                              })
                                      )
                              )
                      )
                      .then(Commands.literal("aspect")
                              .then(Commands.argument("player", StringArgumentType.word())
                                      .then(Commands.argument("aspect", StringArgumentType.word())
                                              .then(Commands.argument("amount", IntegerArgumentType.integer(1))
                                                      .executes(ctx -> {
                                                         ServerPlayer player = getPlayer(ctx.getSource(), StringArgumentType.getString(ctx, "player"));
                                                         if (player == null) {
                                                            ctx.getSource().sendFailure(Component.literal("Player not found"));
                                                            return 0;
                                                         }
                                                         String aspect = StringArgumentType.getString(ctx, "aspect");
                                                         int amount = IntegerArgumentType.getInteger(ctx, "amount");
                                                         if ("all".equalsIgnoreCase(aspect)){
                                                             giveAllAspect(ctx.getSource(), player, amount);
                                                         }else{
                                                             var resLoc = ResourceLocation.tryParse(aspect);
                                                             if (resLoc == null) {
                                                                 ctx.getSource().sendFailure(Component.literal("aspect not found"));
                                                                 return 0;
                                                             }
                                                             giveAspect(ctx.getSource(), player, resLoc, amount);
                                                         }
                                                         return 1;
                                                      })
                                              )
                                      )
                              )
                      )
                      .then(Commands.literal("warp")
                              .then(Commands.argument("player", StringArgumentType.word())
                                      .then(Commands.argument("mode", StringArgumentType.word()) // add / set
                                              .then(Commands.argument("amount", IntegerArgumentType.integer())
                                                      .executes(ctx -> {
                                                         ServerPlayer player = getPlayer(ctx.getSource(), StringArgumentType.getString(ctx, "player"));
                                                         if (player == null) {
                                                            ctx.getSource().sendFailure(Component.literal("Player not found"));
                                                            return 0;
                                                         }
                                                         String mode = StringArgumentType.getString(ctx, "mode");

                                                         int amount = IntegerArgumentType.getInteger(ctx, "amount");

                                                         String type = StringArgumentType.getString(ctx, "type");
                                                         if (Objects.equals(mode, "add")) {
                                                             addWarp(ctx.getSource(), player, amount, type);
                                                         }else if (Objects.equals(mode, "set")) {
                                                             setWarp(ctx.getSource(), player, amount, type);
                                                         }
                                                         return 1;
                                                      })
                                              )
                                      )
                              )
                      )
      );

      // 别名 /thaum -> /thaumcraft
      dispatcher.register(Commands.literal("thaum")
              .requires(src -> src.hasPermission(2))
              .redirect(dispatcher.getRoot().getChild("thaumcraft")));
      // 别名 /tc -> /thaumcraft
      dispatcher.register(Commands.literal("tc")
              .requires(src -> src.hasPermission(2))
              .redirect(dispatcher.getRoot().getChild("thaumcraft")));
   }

   private static void sendHelp(CommandSourceStack source) {
      source.sendSuccess(() -> Component.literal("§3You can also use /thaum or /tc instead of /thaumcraft."), false);
      source.sendSuccess(() -> Component.literal("§3Use /thaumcraft research <player> <all|reset|<research>>"), false);
      source.sendSuccess(() -> Component.literal("§3Use /thaumcraft aspect <player> <aspect|all> <amount>"), false);
      source.sendSuccess(() -> Component.literal("§3Use /thaumcraft warp <player> <add|set> <amount> <PERM|TEMP>"), false);
   }

   private static ServerPlayer getPlayer(CommandSourceStack source, String name) {
      MinecraftServer server = source.getServer();
      return server.getPlayerList().getPlayerByName(name);
   }

   // -------------------------
   // 以下方法留给你实现具体逻辑
   // -------------------------
//   private static void handleResearch(CommandSourceStack source, String listOrPlayer, String action) {
//       if ("list".equalsIgnoreCase(action)) {
//           listAllResearch(source);
//       } else if ("all".equalsIgnoreCase(argument)) {
//           giveAllResearch(source, player);
//       } else if ("reset".equalsIgnoreCase(argument)) {
//           resetResearch(source, player);
//       } else {
//           giveResearchRecursive(source, player, argument);
//       }
////      source.sendSuccess(() -> Component.literal("Research command executed for " + player.getGameProfile().getName() + " action=" + action), false);
//   }

   private static void handleAspect(CommandSourceStack source, ServerPlayer player, String aspect, int amount) {
      source.sendSuccess(() -> Component.literal("Aspect command executed for " + player.getGameProfile().getName() + " aspect=" + aspect + " amount=" + amount), false);
   }

   private static void handleWarp(CommandSourceStack source, ServerPlayer player, String mode, int amount, String type) {
      source.sendSuccess(() -> Component.literal("Warp command executed for " + player.getGameProfile().getName() + " mode=" + mode + " amount=" + amount + " type=" + type), false);
   }
//
//   private List<String> aliases = new ArrayList<>();
//
//   public CommandThaumcraft() {
//      this.aliases.add("thaumcraft");
//      this.aliases.add("thaum");
//      this.aliases.add("tc");
//   }
//
//   public String getCommandName() {
//      return "thaumcraft";
//   }
//
//   public String getCommandUsage(ICommandSender icommandsender) {
//      return "/thaumcraft <action> [<player> [<params>]]";
//   }
//
//   public List<String> getCommandAliases() {
//      return this.aliases;
//   }
//
//   public int getRequiredPermissionLevel() {
//      return 2;
//   }
//
//   public List<String> addTabCompletionOptions(ICommandSender icommandsender, String[] astring) {
//      return null;
//   }
//
//   public boolean isUsernameIndex(String[] astring, int i) {
//      return i == 1;
//   }
//
//   public void processCommand(ICommandSender icommandsender, String[] astring) {
//      if (astring.length == 0) {
//         icommandsender.displayClientMessage(new ChatComponentTranslation("§cInvalid arguments"));
//         icommandsender.displayClientMessage(new ChatComponentTranslation("§cUse /thaumcraft help to get help"));
//      } else {
//         if (astring[0].equalsIgnoreCase("help")) {
//            icommandsender.displayClientMessage(new ChatComponentTranslation("§3You can also use /thaum or /tc instead of /thaumcraft."));
//            icommandsender.displayClientMessage(new ChatComponentTranslation("§3Use this to give research to a player."));
//            icommandsender.displayClientMessage(new ChatComponentTranslation("  /thaumcraft research <list|player> <all|reset|<research>>"));
//            icommandsender.displayClientMessage(new ChatComponentTranslation("§3Use this to give aspect research points to a player."));
//            icommandsender.displayClientMessage(new ChatComponentTranslation("  /thaumcraft aspect <player> <aspect|all> <amount>"));
//            icommandsender.displayClientMessage(new ChatComponentTranslation("§3Use this to give set a players warp level."));
//            icommandsender.displayClientMessage(new ChatComponentTranslation("  /thaumcraft warp <player> <add|set> <amount> <PERM|TEMP>"));
//            icommandsender.displayClientMessage(new ChatComponentTranslation("  not specifying perm or temp will just add normal warp"));
//         } else if (astring.length >= 2) {
//            if (astring[0].equalsIgnoreCase("research") && astring[1].equalsIgnoreCase("list")) {
//               this.listResearch(icommandsender);
//            } else {
//               ServerPlayer ServerPlayer = getPlayer(icommandsender, astring[1]);
//               if (astring[0].equalsIgnoreCase("research")) {
//                  if (astring.length == 3) {
//                     if (astring[2].equalsIgnoreCase("all")) {
//                        this.giveAllResearch(icommandsender, ServerPlayer);
//                     } else if (astring[2].equalsIgnoreCase("reset")) {
//                        this.resetResearch(icommandsender, ServerPlayer);
//                     } else {
//                        this.giveResearch(icommandsender, ServerPlayer, astring[2]);
//                     }
//                  } else {
//                     icommandsender.displayClientMessage(new ChatComponentTranslation("§cInvalid arguments"));
//                     icommandsender.displayClientMessage(new ChatComponentTranslation("§cUse /thaumcraft research <list|player> <all|reset|<research>>"));
//                  }
//               } else if (astring[0].equalsIgnoreCase("aspect")) {
//                  if (astring.length == 4) {
//                     int i = parseIntWithMin(icommandsender, astring[3], 1);
//                     this.giveAspect(icommandsender, ServerPlayer, astring[2], i);
//                  } else {
//                     icommandsender.displayClientMessage(new ChatComponentTranslation("§cInvalid arguments"));
//                     icommandsender.displayClientMessage(new ChatComponentTranslation("§cUse /thaumcraft aspect <player> <aspect|all> <amount>"));
//                  }
//               } else if (astring[0].equalsIgnoreCase("warp")) {
//                  if (astring.length >= 4 && astring[2].equalsIgnoreCase("set")) {
//                     int i = parseIntWithMin(icommandsender, astring[3], 0);
//                     this.setWarp(icommandsender, ServerPlayer, i, astring.length == 5 ? astring[4] : "");
//                  } else if (astring.length >= 4 && astring[2].equalsIgnoreCase("add")) {
//                     int i = parseIntBounded(icommandsender, astring[3], -100, 100);
//                     this.addWarp(icommandsender, ServerPlayer, i, astring.length == 5 ? astring[4] : "");
//                  } else {
//                     icommandsender.displayClientMessage(new ChatComponentTranslation("§cInvalid arguments"));
//                     icommandsender.displayClientMessage(new ChatComponentTranslation("§cUse /thaumcraft warp <player> <add|set> <amount> <PERM|TEMP>"));
//                  }
//               } else {
//                  icommandsender.displayClientMessage(new ChatComponentTranslation("§cInvalid arguments"));
//                  icommandsender.displayClientMessage(new ChatComponentTranslation("§cUse /thaumcraft help to get help"));
//               }
//            }
//         } else {
//            icommandsender.displayClientMessage(new ChatComponentTranslation("§cInvalid arguments"));
//            icommandsender.displayClientMessage(new ChatComponentTranslation("§cUse /thaumcraft help to get help"));
//         }
//
//      }
//   }
//
    private static void giveAllAspect(CommandSourceStack icommandsender, ServerPlayer player, int i) {
        for(Aspect aspect : Aspects.ALL_ASPECTS.values()) {
            Thaumcraft.playerKnowledge.addAspectPool(player.getGameProfile().getName(), aspect, (short)i);
        }

        ResearchManager.scheduleSave(player.getGameProfile().getName());
        player.displayClientMessage(Component.literal("§5" + icommandsender.getTextName() + " gave you " + i + " of all the aspects."),false);
        icommandsender.sendSuccess(() -> Component.literal("§5Success!"),false);
        new PacketSyncAspectsS2C(player).sendTo(player);
    }
   private static void giveAspect(CommandSourceStack icommandsender, ServerPlayer player, ResourceLocation aspectTag, int i) {

         Aspect aspect = Aspect.getAspect(aspectTag);
         if (aspect == null) {
            for(var a : Aspects.ALL_ASPECTS.values()) {
               if (aspectTag.equals(a.getAspectKey())) {
                  aspect = a;
                  break;
               }
            }
         }

         if (aspect != null) {
            Thaumcraft.playerKnowledge.addAspectPool(player.getGameProfile().getName(), aspect, (short)i);
            ResearchManager.scheduleSave(player.getGameProfile().getName());
            new PacketSyncAspectsS2C(player).sendTo(player);
            player.displayClientMessage(Component.literal("§5" + icommandsender.getTextName() + " gave you " + i + " " + aspect.getName()),false);
            icommandsender.sendSuccess(() -> Component.literal("§5Success!"),false);
         } else {
            icommandsender.sendSuccess(() -> Component.literal("§cAspect does not exist."),false);
         }


   }

   private static void setWarp(CommandSourceStack icommandsender, ServerPlayer player, int i, String type) {
      if (type.equalsIgnoreCase("PERM")) {
         Thaumcraft.playerKnowledge.setWarpPerm(player.getGameProfile().getName(), i);
         ResearchManager.scheduleSave(player.getGameProfile().getName());
          new PacketSyncWarpS2C(player, (byte)0).sendTo(player);
      } else if (type.equalsIgnoreCase("TEMP")) {
         Thaumcraft.playerKnowledge.setWarpTemp(player.getGameProfile().getName(), i);
         ResearchManager.scheduleSave(player.getGameProfile().getName());
          new PacketSyncWarpS2C(player, (byte)2).sendTo(player);
      } else {
         Thaumcraft.playerKnowledge.setWarpSticky(player.getGameProfile().getName(), i);
         ResearchManager.scheduleSave(player.getGameProfile().getName());
          new PacketSyncWarpS2C(player, (byte)1).sendTo(player);
      }

      player.displayClientMessage(Component.literal("§5" + icommandsender.getTextName() + " set your warp to " + i),false);
      icommandsender.sendSuccess(() -> Component.literal("§5Success!"),false);
   }

   private static void addWarp(CommandSourceStack icommandsender, ServerPlayer player, int i, String type) {
      if (type.equalsIgnoreCase("PERM")) {
         Thaumcraft.playerKnowledge.addWarpPerm(player.getGameProfile().getName(), i);
         ResearchManager.scheduleSave(player.getGameProfile().getName());
          new PacketSyncWarpS2C(player, (byte)0).sendTo(player);
          new PacketWarpMessageS2C((byte)0, i).sendTo(player);
      } else if (type.equalsIgnoreCase("TEMP")) {
         Thaumcraft.playerKnowledge.addWarpTemp(player.getGameProfile().getName(), i);
         ResearchManager.scheduleSave(player.getGameProfile().getName());
          new PacketSyncWarpS2C(player, (byte)2).sendTo(player);
          new PacketWarpMessageS2C((byte)2, i).sendTo(player);
      } else {
         Thaumcraft.playerKnowledge.addWarpSticky(player.getGameProfile().getName(), i);
          ResearchManager.scheduleSave(player.getGameProfile().getName());
          new PacketSyncWarpS2C(player, (byte)1).sendTo(player);
          new PacketWarpMessageS2C((byte)1, i).sendTo(player);
      }

      player.displayClientMessage(Component.literal("§5" + icommandsender.getTextName() + " added " + i + " warp to your total."),false);
      icommandsender.sendSuccess(() -> Component.literal("§5Success!"),false);
   }

    static void listResearch(CommandSourceStack icommandsender) {
      for(ResearchCategory cat : ResearchCategories.researchCategories.values()) {
         for(ResearchItem ri : cat.researches.values()) {
            icommandsender.sendSuccess(() -> Component.literal("§5" + ri.key),false);
         }
      }
   }

    static void giveClue(CommandSourceStack icommandsender, ServerPlayer player, ResourceLocation clueForResearch) {
        if (ResearchItem.getResearch(clueForResearch) != null) {
            Thaumcraft.researchManager.completeClue(player, clueForResearch);
            new PacketSyncResearchS2C(player).sendTo(player);
            player.sendSystemMessage(Component.literal("§5" + icommandsender.getTextName() + " gave you clue " + clueForResearch));
            icommandsender.sendSuccess(() -> Component.literal("§5Success!"),false);
        } else {
            icommandsender.sendSuccess(() -> Component.literal("§cResearch does not exist."),false);
        }
    }
   static void giveResearch(CommandSourceStack icommandsender, ServerPlayer player, ResourceLocation research) {
      if (ResearchItem.getResearch(research) != null) {
         giveRecursiveResearch(player, research);
          new PacketSyncResearchS2C(player).sendTo(player);
         player.sendSystemMessage(Component.literal("§5" + icommandsender.getTextName() + " gave you " + research + " research and its requisites."));
         icommandsender.sendSuccess(() -> Component.literal("§5Success!"),false);
      } else {
         icommandsender.sendSuccess(() -> Component.literal("§cResearch does not exist."),false);
      }
   }

   static void giveRecursiveResearch(ServerPlayer player, ResourceLocation research) {
      if (!ResearchManager.isResearchComplete(player.getGameProfile().getName(), research)) {
         Thaumcraft.researchManager.completeResearch(player, research);
         if (ResearchItem.getResearch(research).parents != null) {
            for(var rsi : ResearchItem.getResearch(research).parents) {
               giveRecursiveResearch(player, rsi);
            }
         }

         if (ResearchItem.getResearch(research).parentsHidden != null) {
            for(var rsi : ResearchItem.getResearch(research).parentsHidden) {
               giveRecursiveResearch(player, rsi);
            }
         }

         if (ResearchItem.getResearch(research).siblings != null) {
            for(var rsi : ResearchItem.getResearch(research).siblings) {
               giveRecursiveResearch(player, rsi);
            }
         }
      }

   }

   static void giveAllResearch(CommandSourceStack icommandsender, ServerPlayer player) {
      for(ResearchCategory cat : ResearchCategories.researchCategories.values()) {
         for(ResearchItem ri : cat.researches.values()) {
            if (!ResearchManager.isResearchComplete(player.getGameProfile().getName(), ri.key)) {
               Thaumcraft.researchManager.completeResearch(player, ri.key);
            }
         }
      }

      player.sendSystemMessage(Component.literal("§5" + icommandsender.getTextName() + " has given you all research."),false);
      icommandsender.sendSuccess(() -> Component.literal("§5Success!"),false);
       new PacketSyncResearchS2C(player).sendTo(player);
   }

   static void resetResearch(CommandSourceStack icommandsender, ServerPlayer player) {
      Thaumcraft.playerKnowledge.researchCompleted.remove(player.getGameProfile().getName());

      for(ResearchCategory cat : ResearchCategories.researchCategories.values()) {
         for(ResearchItem ri : cat.researches.values()) {
            if (ri.isAutoUnlock()) {
               Thaumcraft.researchManager.completeResearch(player, ri.key);
            }
         }
      }

      player.sendSystemMessage(Component.literal("§5" + icommandsender.getTextName() + " has reset you research."),false);
      icommandsender.sendSuccess(() -> Component.literal("§5Success!"),false);
       new PacketSyncResearchS2C(player).sendTo(player);
   }
}
