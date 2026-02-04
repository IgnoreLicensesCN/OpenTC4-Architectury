package thaumcraft.common;

import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import net.minecraft.client.Minecraft;
import thaumcraft.client.fx.migrated.particles.FXBlockRunes;
import thaumcraft.common.items.wands.WandManager;
import thaumcraft.common.lib.FakeThaumcraftPlayer;
import thaumcraft.common.lib.events.EventHandlerRunic;
import thaumcraft.common.lib.network.PacketHandler;
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


    public static Map<String, List<ResourceLocation>> getCompletedResearch(){
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

    public static Map<String, AspectList<Aspect>> getKnownAspects() {
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
                        if (amount < 0 && playerKnowledge.getWarpTemp(player.getGameProfile().getName()) <= 0) {
                            return;
                        }

                        playerKnowledge.addWarpTemp(player.getGameProfile().getName(), amount);
                        new PacketSyncWarpS2C(player, (byte)2).sendTo(player);
                        new PacketWarpMessageS2C((byte)2, amount).sendTo(player);
                    } else {
                        playerKnowledge.addWarpPerm(player.getGameProfile().getName(), amount);
                        new PacketSyncWarpS2C(player, (byte)0).sendTo(player);
                        new PacketWarpMessageS2C((byte)0, amount).sendTo(player);
                    }

                    playerKnowledge.setWarpCounter(player.getGameProfile().getName(), playerKnowledge.getWarpTotal(player.getGameProfile().getName()));
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
                if (amount >= 0 || playerKnowledge.getWarpSticky(player.getGameProfile().getName()) > 0) {
                    playerKnowledge.addWarpSticky(player.getGameProfile().getName(), amount);
                    new PacketSyncWarpS2C(player, (byte)1).sendTo(player);
                    new PacketWarpMessageS2C((byte)1, amount).sendTo(player);
                    playerKnowledge.setWarpCounter(player.getGameProfile().getName(), playerKnowledge.getWarpTotal(player.getGameProfile().getName()));
                }
            }
        }
    }

}
