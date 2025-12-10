package thaumcraft.common;

import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import thaumcraft.api.aspects.AspectList;
import net.minecraft.client.Minecraft;
import thaumcraft.client.fx.particles.FXBlockRunes;
import thaumcraft.common.items.wands.WandManager;
import thaumcraft.common.lib.FakeThaumcraftPlayer;
import thaumcraft.common.lib.events.EventHandlerRunic;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.playerdata.PacketSyncWarp;
import thaumcraft.common.lib.network.playerdata.PacketSyncWarpS2C;
import thaumcraft.common.lib.network.playerdata.PacketWarpMessageS2C;
import thaumcraft.common.lib.research.PlayerKnowledge;
import thaumcraft.common.lib.research.ResearchManager;
import thaumcraft.common.lib.world.ThaumcraftWorldGenerator;

import java.util.List;
import java.util.Map;

public class Thaumcraft {
    public static final String MOD_ID = "thaumcraft";

    public static final Thaumcraft instance = new Thaumcraft();
    public static ResearchManager researchManager;
    public static ThaumcraftWorldGenerator worldGen;
//    public EventHandlerLevel worldEventHandler;
//    public EventHandlerNetwork networkEventHandler;
//    public ServerTickEventsFML serverTickEvents;
//    public EventHandlerEntity entityEventHandler;
    public EventHandlerRunic runicEventHandler;
//    public RenderEventHandler renderEventHandler;

    public static PlayerKnowledge playerKnowledge;
    public static final WandManager wandManager = new WandManager();


    public static Map<String, List<String>> getCompletedResearch(){
        return playerKnowledge.researchCompleted;
    }

    public static Map<String, List<String>> getScannedObjects(){
        return playerKnowledge.objectsScanned;
    }
    public static Map<String, List<String>> getScannedEntities(){
        return playerKnowledge.entitiesScanned;
    }
    public static Map<String, List<String>> getScannedPhenomena(){
        return playerKnowledge.phenomenaScanned;
    }

    public static Map<String, AspectList> getKnownAspects() {
        return playerKnowledge.aspectsDiscovered;
    }

    public static void addWarpToPlayer(Player _player, int amount, boolean temporary) {
        if (_player instanceof FakeThaumcraftPlayer) {
            return;
        }
        if (!(_player instanceof ServerPlayer player)) {return;}
        if (playerKnowledge != null) {
            if (temporary || amount >= 0) {
                if (amount != 0) {
                    if (temporary) {
                        if (amount < 0 && playerKnowledge.getWarpTemp(player.getName().getString()) <= 0) {
                            return;
                        }

                        playerKnowledge.addWarpTemp(player.getName().getString(), amount);
                        PacketHandler.INSTANCE.sendTo(new PacketSyncWarp(player, (byte)2), player);
                        PacketHandler.INSTANCE.sendTo(new PacketWarpMessageS2C(player, (byte)2, amount), player);
                    } else {
                        playerKnowledge.addWarpPerm(player.getName().getString(), amount);
                        PacketHandler.INSTANCE.sendTo(new PacketSyncWarp(player, (byte)0), player);
                        PacketHandler.INSTANCE.sendTo(new PacketWarpMessageS2C(player, (byte)0, amount), player);
                    }

                    playerKnowledge.setWarpCounter(player.getName().getString(), playerKnowledge.getWarpTotal(player.getName().getString()));
                }
            }
        }
    }

    public static void addStickyWarpToPlayer(Player _player, int amount) {
        if (_player instanceof FakeThaumcraftPlayer) {
            return;
        }
        if (!(_player instanceof ServerPlayer player)) {return;}
        if (Platform.getEnvironment() != Env.SERVER){return;}
        if (playerKnowledge != null) {
            if (amount != 0) {
                if (amount >= 0 || playerKnowledge.getWarpSticky(player.getName().getString()) > 0) {
                    playerKnowledge.addWarpSticky(player.getName().getString(), amount);
                    PacketHandler.INSTANCE.sendTo(new PacketSyncWarpS2C(player, (byte)1), player);
                    PacketHandler.INSTANCE.sendTo(new PacketWarpMessageS2C(player, (byte)1, amount), player);
                    playerKnowledge.setWarpCounter(player.getName().getString(), playerKnowledge.getWarpTotal(player.getName().getString()));
                }
            }
        }
    }

    public static void blockRunes(Level world, double x, double y, double z, float r, float g, float b, int dur, float grav) {
        if (Platform.getEnvironment() != Env.CLIENT) {return;}
        FXBlockRunes fb = new FXBlockRunes(world, x + (double)0.5F, y + (double)0.5F, z + (double)0.5F, r, g, b, dur);
        fb.setGravity(grav);
        Minecraft.getInstance().particleEngine.add(fb);

    }
}
