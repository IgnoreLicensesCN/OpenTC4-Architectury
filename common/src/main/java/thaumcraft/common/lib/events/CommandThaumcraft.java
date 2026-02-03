package thaumcraft.common.lib.events;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchCategoryList;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.playerdata.PacketSyncAspects;
import thaumcraft.common.lib.network.playerdata.PacketSyncResearch;
import thaumcraft.common.lib.network.playerdata.PacketSyncWarp;
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
                                                     giveResearch(ctx.getSource(), player, action);
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
                                                         giveAspect(ctx.getSource(), player, aspect, amount);
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
   private static void giveAspect(CommandSourceStack icommandsender, ServerPlayer player, String string, int i) {
      if (string.equalsIgnoreCase("all")) {
         for(Aspect aspect : Aspects.ALL_ASPECTS.values()) {
            Thaumcraft.playerKnowledge.addAspectPool(player.getGameProfile().getName(), aspect, (short)i);
         }

         ResearchManager.scheduleSave(player);
         player.displayClientMessage(Component.literal("§5" + icommandsender.getTextName() + " gave you " + i + " of all the aspects."),false);
          icommandsender.sendSuccess(() -> Component.literal("§5Success!"),false);
         PacketHandler.INSTANCE.sendTo(new PacketSyncAspects(player), player);
      } else {
         Aspect aspect = Aspect.getAspect(string);
         if (aspect == null) {
            for(Aspect a : Aspects.ALL_ASPECTS.values()) {
               if (string.equalsIgnoreCase(a.getName())) {
                  aspect = a;
                  break;
               }
            }
         }

         if (aspect != null) {
            Thaumcraft.playerKnowledge.addAspectPool(player.getGameProfile().getName(), aspect, (short)i);
            ResearchManager.scheduleSave(player);
            PacketHandler.INSTANCE.sendTo(new PacketSyncAspects(player), player);
            player.displayClientMessage(Component.literal("§5" + icommandsender.getTextName() + " gave you " + i + " " + aspect.getName()),false);
            icommandsender.sendSuccess(() -> Component.literal("§5Success!"),false);
         } else {
            icommandsender.sendSuccess(() -> Component.literal("§cAspect does not exist."),false);
         }
      }

   }

   private static void setWarp(CommandSourceStack icommandsender, ServerPlayer player, int i, String type) {
      if (type.equalsIgnoreCase("PERM")) {
         Thaumcraft.playerKnowledge.setWarpPerm(player.getGameProfile().getName(), i);
         ResearchManager.scheduleSave(player);
         PacketHandler.INSTANCE.sendTo(new PacketSyncWarp(player, (byte)0), player);
      } else if (type.equalsIgnoreCase("TEMP")) {
         Thaumcraft.playerKnowledge.setWarpTemp(player.getGameProfile().getName(), i);
         ResearchManager.scheduleSave(player);
         PacketHandler.INSTANCE.sendTo(new PacketSyncWarp(player, (byte)2), player);
      } else {
         Thaumcraft.playerKnowledge.setWarpSticky(player.getGameProfile().getName(), i);
         ResearchManager.scheduleSave(player);
         PacketHandler.INSTANCE.sendTo(new PacketSyncWarp(player, (byte)1), player);
      }

      player.displayClientMessage(Component.literal("§5" + icommandsender.getTextName() + " set your warp to " + i),false);
      icommandsender.sendSuccess(() -> Component.literal("§5Success!"),false);
   }

   private static void addWarp(CommandSourceStack icommandsender, ServerPlayer player, int i, String type) {
      if (type.equalsIgnoreCase("PERM")) {
         Thaumcraft.playerKnowledge.addWarpPerm(player.getGameProfile().getName(), i);
         ResearchManager.scheduleSave(player);
         PacketHandler.INSTANCE.sendTo(new PacketSyncWarp(player, (byte)0), player);
         PacketHandler.INSTANCE.sendTo(new PacketWarpMessageS2C(player, (byte)0, i), player);
      } else if (type.equalsIgnoreCase("TEMP")) {
         Thaumcraft.playerKnowledge.addWarpTemp(player.getGameProfile().getName(), i);
         ResearchManager.scheduleSave(player);
         PacketHandler.INSTANCE.sendTo(new PacketSyncWarp(player, (byte)2), player);
         PacketHandler.INSTANCE.sendTo(new PacketWarpMessageS2C(player, (byte)2, i), player);
      } else {
         Thaumcraft.playerKnowledge.addWarpSticky(player.getGameProfile().getName(), i);
         ResearchManager.scheduleSave(player);
         PacketHandler.INSTANCE.sendTo(new PacketSyncWarp(player, (byte)1), player);
         PacketHandler.INSTANCE.sendTo(new PacketWarpMessageS2C(player, (byte)1, i), player);
      }

      player.displayClientMessage(Component.literal("§5" + icommandsender.getTextName() + " added " + i + " warp to your total."),false);
      icommandsender.sendSuccess(() -> Component.literal("§5Success!"),false);
   }

    static void listResearch(CommandSourceStack icommandsender) {
      for(ResearchCategoryList cat : ResearchCategories.researchCategories.values()) {
         for(ResearchItem ri : cat.research.values()) {
            icommandsender.sendSuccess(() -> Component.literal("§5" + ri.key),false);
         }
      }
   }

   static void giveResearch(CommandSourceStack icommandsender, ServerPlayer player, String research) {
      if (ResearchCategories.getResearch(research) != null) {
         giveRecursiveResearch(player, research);
         PacketHandler.INSTANCE.sendTo(new PacketSyncResearch(player), player);
         player.sendSystemMessage(Component.literal("§5" + icommandsender.getTextName() + " gave you " + research + " research and its requisites."));
         icommandsender.sendSuccess(() -> Component.literal("§5Success!"),false);
      } else {
         icommandsender.sendSuccess(() -> Component.literal("§cResearch does not exist."),false);
      }

   }

   static void giveRecursiveResearch(ServerPlayer player, String research) {
      if (!ResearchManager.isResearchComplete(player.getGameProfile().getName(), research)) {
         Thaumcraft.researchManager.completeResearch(player, research);
         if (ResearchCategories.getResearch(research).parents != null) {
            for(String rsi : ResearchCategories.getResearch(research).parents) {
               giveRecursiveResearch(player, rsi);
            }
         }

         if (ResearchCategories.getResearch(research).parentsHidden != null) {
            for(String rsi : ResearchCategories.getResearch(research).parentsHidden) {
               giveRecursiveResearch(player, rsi);
            }
         }

         if (ResearchCategories.getResearch(research).siblings != null) {
            for(String rsi : ResearchCategories.getResearch(research).siblings) {
               giveRecursiveResearch(player, rsi);
            }
         }
      }

   }

   static void giveAllResearch(CommandSourceStack icommandsender, ServerPlayer player) {
      for(ResearchCategoryList cat : ResearchCategories.researchCategories.values()) {
         for(ResearchItem ri : cat.research.values()) {
            if (!ResearchManager.isResearchComplete(player.getGameProfile().getName(), ri.key)) {
               Thaumcraft.researchManager.completeResearch(player, ri.key);
            }
         }
      }

      player.sendSystemMessage(Component.literal("§5" + icommandsender.getTextName() + " has given you all research."),false);
      icommandsender.sendSuccess(() -> Component.literal("§5Success!"),false);
      PacketHandler.INSTANCE.sendTo(new PacketSyncResearch(player), player);
   }

   static void resetResearch(CommandSourceStack icommandsender, ServerPlayer player) {
      Thaumcraft.playerKnowledge.researchCompleted.remove(player.getGameProfile().getName());

      for(ResearchCategoryList cat : ResearchCategories.researchCategories.values()) {
         for(ResearchItem ri : cat.research.values()) {
            if (ri.isAutoUnlock()) {
               Thaumcraft.researchManager.completeResearch(player, ri.key);
            }
         }
      }

      player.sendSystemMessage(Component.literal("§5" + icommandsender.getTextName() + " has reset you research."),false);
      icommandsender.sendSuccess(() -> Component.literal("§5Success!"),false);
      PacketHandler.INSTANCE.sendTo(new PacketSyncResearch(player), player);
   }
}
