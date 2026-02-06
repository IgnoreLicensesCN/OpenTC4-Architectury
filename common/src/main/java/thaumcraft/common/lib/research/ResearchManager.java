package thaumcraft.common.lib.research;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.*;
import thaumcraft.api.research.ResearchCategory;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.api.research.interfaces.IResearchWarpOwner;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.Config;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.lib.events.EventHandlerRunic;
import thaumcraft.common.lib.network.playerdata.PacketClueCompleteS2C;
import thaumcraft.common.lib.network.playerdata.PacketResearchCompleteS2C;
import thaumcraft.common.lib.resourcelocations.ClueResourceLocation;
import thaumcraft.common.lib.resourcelocations.ResearchItemResourceLocation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static com.linearity.opentc4.Consts.AspectCompoundTagAccessors.*;
import static com.linearity.opentc4.Consts.ThaumcraftPlayerCompoundTagAccessors.*;
import static com.linearity.opentc4.OpenTC4.LOGGER;
import static com.linearity.opentc4.OpenTC4.platformUtils;
import static thaumcraft.common.ThaumcraftSounds.LEARN;
import static thaumcraft.common.lib.events.EventHandlerEntity.getThaumcraftPlayersDirectory;

public class ResearchManager {
    static ArrayList<ResearchItem> allHiddenResearch = null;
    static ArrayList<ResearchItem> allValidResearch = null;

    @Deprecated(forRemoval = true,since = "should be migrated to ResearchItem or a interface")
    public static boolean createClue(Level world, Player player, ItemStack clue, AspectList<Aspect> aspects) {
        return createClue(world, player, clue.getItem(), aspects);
    }

    @Deprecated(forRemoval = true,since = "should be migrated to ResearchItem or a interface")
    public static boolean createClue(Level world, Player player, Item clue, AspectList<Aspect> aspects) {
        List<ClueResourceLocation> keys = new ArrayList<>();
        for (ResearchCategory rcl : ResearchCategory.researchCategories.values()) {
            label110:
            for (ResearchItem researchItem : rcl.researches.values()) {
                var asClueKey = researchItem.key.convertToResearchItemResLoc();
                boolean valid = !researchItem.tags.isEmpty()
                        && (researchItem.isLost() || researchItem.isHidden())
                        && !isResearchComplete(player.getGameProfile().getName(), researchItem.key
                ) && !isClueComplete(
                        player.getGameProfile()
                                .getName(), asClueKey
                );
                if (valid) {
                    if (researchItem.getItemTriggers() != null) {
                        for (ItemStack stack : researchItem.getItemTriggers()) {
                            if (
                                    Objects.equals(stack.getItem(), clue)
//                             InventoryUtils.areItemStacksEqual(stack, clue, true, true)
                            ) {
                                keys.add(asClueKey);
                                continue label110;
                            }
                        }
                    }

                    if (aspects != null && !aspects.isEmpty() && researchItem.getAspectTriggers() != null) {
                        researchItem.getAspectTriggers();
                        for (Aspect aspect : researchItem.getAspectTriggers()) {
                            if (aspects.getAmount(aspect) > 0) {
                                keys.add(asClueKey);
                                break;
                            }
                        }
                    }
                }
            }
        }

        if (!keys.isEmpty()) {
            var key = keys.get(world.getRandom().nextInt(keys.size()));
            if (player instanceof ServerPlayer serverPlayer) {
                new PacketClueCompleteS2C(key).sendTo(serverPlayer);
            }else {
                LOGGER.warn("createclue:not a server player:{}",player.getGameProfile().getName());
            }
            Thaumcraft.researchManager.completeClue(player, key);
            return true;
        } else {
            return false;
        }
    }

    @Deprecated(forRemoval = true,since = "should be migrated to ResearchItem or a interface")
    public static boolean createClue(Level world, Player player, ResourceKey<EntityType<?>> clue, AspectList<Aspect> aspects) {
        List<ClueResourceLocation> keys = new ArrayList<>();
        for (ResearchCategory rcl : ResearchCategory.researchCategories.values()) {
            label110:
            for (ResearchItem ri : rcl.researches.values()) {
                var convertedKey = ri.key.convertToResearchItemResLoc();
                boolean valid =
                        !ri.tags.isEmpty()
                                && (ri.isLost() || ri.isHidden()) && !isResearchComplete(
                                player.getGameProfile()
                                        .getName(), ri.key
                        ) && !isClueComplete(
                                player.getGameProfile()
                                        .getName(),
                                convertedKey
                        );
                if (valid) {
                    {
                        if (ri.getEntityTriggers() != null) {
                            ri.getEntityTriggers();
                            for (ResourceKey<EntityType<?>> entity : ri.getEntityTriggers()) {
                                if (clue.equals(entity)) {
                                    keys.add(new ClueResourceLocation(ri.key));
                                    continue label110;
                                }
                            }
                        }
                    }

                    if (aspects != null && !aspects.isEmpty() && ri.getAspectTriggers() != null) {
                        ri.getAspectTriggers();
                        for (Aspect aspect : ri.getAspectTriggers()) {
                            if (aspects.getAmount(aspect) > 0) {
                                keys.add(new ClueResourceLocation(ri.key));
                                break;
                            }
                        }
                    }
                }
            }
        }

        if (!keys.isEmpty()) {
            var key = keys.get(world.getRandom().nextInt(keys.size()));
            if (player instanceof ServerPlayer serverPlayer) {
                new PacketClueCompleteS2C(key).sendTo(serverPlayer);
            }else {
                LOGGER.warn("createclue:not a server playere:{}",player.getGameProfile().getName());
            }
            Thaumcraft.researchManager.completeClue(player, key);
            return true;
        } else {
            return false;
        }
    }

    @Deprecated(forRemoval = true,since = "should be migrated to ResearchItem or a interface,or PERISH")
    public static ResourceLocation findHiddenResearch(Player player) {
        if (allHiddenResearch == null) {
            allHiddenResearch = new ArrayList<>();

            for (ResearchCategory cat : ResearchCategory.researchCategories.values()) {
                for (ResearchItem ri : cat.researches.values()) {
                    if (ri.isHidden() && ri.tags != null && !ri.tags.isEmpty()) {
                        allHiddenResearch.add(ri);
                    }
                }
            }
        }

        ArrayList<ResourceLocation> keys = new ArrayList<>();

        for (ResearchItem research : allHiddenResearch) {
            if (!isResearchComplete(player.getGameProfile().getName(), research.key)
                    && ResearchItem.doesPlayerHaveRequisites(player.getGameProfile().getName(), research.key)
                    && (research.getItemTriggers() != null
                    || research.getEntityTriggers() != null
                    || research.getAspectTriggers() != null
            )
            ) {
                keys.add(research.key);
            }
        }

        Random rand = new Random(player.level().getDayTime() / 10L / 5L);
        if (!keys.isEmpty()) {
            int r = rand.nextInt(keys.size());
            return keys.get(r);
        } else {
            return EMPTY_RESEARCH;
        }
    }
    public static final ResourceLocation EMPTY_RESEARCH = new ResourceLocation("","");

    @Deprecated(forRemoval = true,since = "unused,should be called mental illness")
    public static ResourceLocation findMatchingResearch(Player player, Aspect aspect) {
        ResourceLocation randomMatch = null;
        if (allValidResearch == null) {
            allValidResearch = new ArrayList<>();

            for (ResearchCategory cat : ResearchCategory.researchCategories.values()) {
                for (ResearchItem ri : cat.researches.values()) {
                    boolean secondary = ri.isSecondary() && Config.researchDifficulty == 0 || Config.researchDifficulty == -1;
                    if (!secondary && !ri.isHidden() && !ri.isLost() && !ri.isAutoUnlock() && !ri.isVirtual() && !ri.isStub()) {
                        allValidResearch.add(ri);
                    }
                }
            }
        }

        ArrayList<ResourceLocation> keys = new ArrayList<>();

        for (ResearchItem research : allValidResearch) {
            if (!isResearchComplete(player.getGameProfile().getName(), research.key)
                    && ResearchItem.doesPlayerHaveRequisites(player.getGameProfile().getName(), research.key)
                    && research.tags.getAmount(aspect) > 0) {
                keys.add(research.key);
            }
        }

        if (!keys.isEmpty()) {
            randomMatch = keys.get(player.getRandom().nextInt(keys.size()));
        }

        return randomMatch;
    }

    public static int getAlreadyExistsResearchSlot(Player player, ResearchItemResourceLocation key) {
        var inv = player.getInventory().items;
        for (int a = 0; a < inv.size(); a++) {
            ItemStack stack = inv.get(a);
            if (!stack.isEmpty()
                    && stack.is(ConfigItems.itemResearchNotes)) {
                ResearchNoteData data = getData(stack);
                if (data != null && data.key.equals(key)) {
                    return a;
                }
            }
        }
        return -1;
    }

    @Deprecated(forRemoval = true)
    public static boolean isResearchComplete(String playername, ResearchItemResourceLocation key) {
        var research = ResearchItem.getResearch(key);
        if (research == null) {
            return false;
        }
        return research.isPlayerCompletedResearch(playername);
    }

    public static boolean isClueComplete(String playername, ClueResourceLocation key) {
        if (ResearchItem.getResearch(new ResearchItemResourceLocation(key)) == null) {
            return false;
        } else {
            var completed = getClueForPlayer(playername);
            return completed != null && !completed.isEmpty() && completed.contains(key);
        }
    }

    public static List<ClueResourceLocation> getClueForPlayer(String playername) {
        var out = Thaumcraft.getCompletedClue().get(playername);

        try {
            var server = platformUtils.getServer();
            if (
                    out == null && Platform.getEnvironment() == Env.SERVER //Thaumcraft.getClientWorld() == null && server != null
            ) {
                Thaumcraft.getCompletedClue().put(playername, new ArrayList<>());
                UUID id = UUID.nameUUIDFromBytes(("OfflinePlayer:" + playername).getBytes(Charsets.UTF_8));


                File dir = getThaumcraftPlayersDirectory(server);
                File file1 = new File(dir, id + ".thaum");
                File file2 = new File(dir, id + ".thaumbak");
                loadPlayerData(playername, file1, file2, false);
                out = Thaumcraft.getCompletedClue().get(playername);
            }
        } catch (Exception e) {
            LOGGER.error("getClueForPlayer", e);
        }

        return out;
    }
    public static List<ResearchItemResourceLocation> getResearchForPlayer(String playername) {
        var out = Thaumcraft.getCompletedResearch().get(playername);

        try {
            var server = platformUtils.getServer();
            if (
                    out == null && Platform.getEnvironment() == Env.SERVER //Thaumcraft.getClientWorld() == null && server != null
            ) {
                Thaumcraft.getCompletedResearch().put(playername, new ArrayList<>());
                UUID id = UUID.nameUUIDFromBytes(("OfflinePlayer:" + playername).getBytes(Charsets.UTF_8));


                File dir = getThaumcraftPlayersDirectory(server);
                File file1 = new File(dir, id + ".thaum");
                File file2 = new File(dir, id + ".thaumbak");
                loadPlayerData(playername, file1, file2, false);
//                ServerPlayer ServerPlayer = new ServerPlayer(server, server.worldServerForDimension(0), new GameProfile(id, playername), new ItemInWorldManager(MinecraftServer.getServer().worldServerForDimension(0)));
//                if (ServerPlayer != null) {
//                    IPlayerFileData playerNBTManagerObj = server.worldServerForDimension(0).getSaveHandler().getSaveHandler();
//                    SaveHandler sh = (SaveHandler) playerNBTManagerObj;
//                    File dir = getThaumcraftPlayersDirectory(server);
//                    File file1 = new File(dir, id + ".thaum");
//                    File file2 = new File(dir, id + ".thaumbak");
//                    loadPlayerData(ServerPlayer, file1, file2, false);
//                }

                out = Thaumcraft.getCompletedResearch().get(playername);
            }
        } catch (Exception e) {
            LOGGER.error("getClueForPlayer", e);
        }

        return out;
    }

    public static List<ResearchItemResourceLocation> getResearchForPlayerSafe(String playername) {
        return Thaumcraft.getCompletedResearch().get(playername);
    }

    public static List<ClueResourceLocation> getClueForPlayerSafe(String playername) {
        return Thaumcraft.getCompletedClue().get(playername);
    }

    public static Aspect getCombinationResult(Aspect aspect1, Aspect aspect2) {
        for (Aspect aspect : Aspects.ALL_ASPECTS.values()) {
            if (aspect instanceof CompoundAspect compoundAspect &&
                    compoundAspect.isCombinedFrom(aspect1,aspect2)) {
                return aspect;
            }
        }
        return null;
    }

    public static AspectList<PrimalAspect> reduceToPrimals(AspectList<Aspect> al) {
        return reduceToPrimals(al, false);
    }

    public static AspectList<PrimalAspect> reduceToPrimals(AspectList<Aspect> al, boolean merge) {
        AspectList<PrimalAspect> out = new AspectList<>();

        for (var aspect : al.getAspectTypes()) {
            var aspAmount = al.getAmount(aspect);
            if (aspect != null) {
                if (aspect instanceof PrimalAspect primalAspect) {
                    if (merge) {
                        out.mergeWithHighest(primalAspect, aspAmount);
                    } else {
                        out.addAll(primalAspect, aspAmount);
                    }
                } else if (aspect instanceof CompoundAspect compoundAspect) {
                    AspectList<PrimalAspect> send = new AspectList<>();
                    send.addAll(
                            reduceToPrimals(
                                    AspectList.of(
                                            Map.of(compoundAspect.components.aspectA(), aspAmount))
                                    ,merge)
                    );
                    send.addAll(
                            reduceToPrimals(
                                    AspectList.of(
                                            Map.of(compoundAspect.components.aspectB(), aspAmount))
                                    ,merge)
                    );

                    for (var a : send.getAspectTypes()) {
                        if (merge) {
                            out.mergeWithHighest(a, send.getAmount(a));
                        } else {
                            out.addAll(a, send.getAmount(a));
                        }
                    }
                }
            }
        }

        return out;
    }

    public static boolean completeClueUnsaved(String username, ClueResourceLocation key) {
        var completed = getClueForPlayerSafe(username);
        if (completed != null && completed.contains(key)) {
            return false;
        } else {
            if (completed == null) {
                completed = new ArrayList<>();
            }

            completed.add(key);
            Thaumcraft.getCompletedClue().put(username, completed);
            return true;
        }
    }
    public static boolean completeResearchUnsaved(String username, ResearchItemResourceLocation key) {
        var completed = getResearchForPlayerSafe(username);
        if (completed != null && completed.contains(key)) {
            return false;
        } else {
            if (completed == null) {
                completed = new ArrayList<>();
            }

            completed.add(key);
            Thaumcraft.getCompletedResearch().put(username, completed);
            return true;
        }
    }

    public static void unlockResearchForPlayer(Level world, ServerPlayer player, ResearchItemResourceLocation research, ResearchItemResourceLocation... preRequisites) {
        for (var preReq : preRequisites) {
            if (!isResearchComplete(player.getGameProfile().getName(), preReq)){return;}
        }
        if (isResearchComplete(player.getGameProfile().getName(), research)){return;}
        new PacketResearchCompleteS2C(research).sendTo(player);
        Thaumcraft.researchManager.completeResearch(player, research);
        player.playSound(LEARN);//,.75f,1.f
    }

    public void completeClue(Player player, ClueResourceLocation key){

        String playerName = player.getGameProfile().getName();
        if (completeClueUnsaved(playerName, key)) {
            int warp = ThaumcraftApi.getClueWarp(key);
            addKindsOfWarps(player, warp);
            scheduleSave(playerName);
        }
    }
    public void completeResearch(Player player, ResearchItemResourceLocation key) {
        String playerName = player.getGameProfile().getName();
        if (completeResearchUnsaved(playerName, key)) {
            int warp;
            var research = ResearchItem.getResearch(key);
            if (research instanceof IResearchWarpOwner warpOwner){
                warp = warpOwner.getWarp();
                addKindsOfWarps(player, warp);
            }
            scheduleSave(playerName);
        }

    }

    public static void addKindsOfWarps(Player player, int warp) {
        if (warp > 0 && !Config.wuss && Platform.getEnvironment() != Env.CLIENT) {
            if (warp > 1) {
                int halved = warp / 2;
                if (warp - halved > 0) {
                    Thaumcraft.addWarpToPlayer(player, warp - halved, false);
                }
                Thaumcraft.addStickyWarpToPlayer(player, halved);
            } else {
                Thaumcraft.addWarpToPlayer(player, warp, false);
            }
        }
    }

    public static boolean completeAspectUnsaved(String username, Aspect aspect, int amount) {
        if (aspect == null) {
            return false;
        } else {
            Thaumcraft.playerKnowledge.addDiscoveredAspect(username, aspect);
            Thaumcraft.playerKnowledge.setAspectPool(username, aspect, amount);
            return true;
        }
    }

    public void completeAspect(String playerName, Aspect aspect, int amount) {
        if (completeAspectUnsaved(playerName, aspect, amount)) {
            scheduleSave(playerName);
        }
    }

    public static boolean completeScannedObjectUnsaved(String username, String object) {
        List<String> completed = Thaumcraft.getScannedObjects().get(username);
        if (completed == null) {
            completed = new ArrayList<>();
        }

        if (!completed.contains(object)) {
            completed.add(object);
            String t = object.replaceFirst("#", "@");
            if (object.startsWith("#") && completed.contains(t)) {
                completed.remove(t);
            }

            Thaumcraft.getScannedObjects().put(username, completed);
        }

        return true;
    }

    public static boolean completeScannedEntityUnsaved(String username, String key) {
        List<String> completed = Thaumcraft.getScannedEntities().get(username);
        if (completed == null) {
            completed = new ArrayList<>();
        }

        if (!completed.contains(key)) {
            completed.add(key);
            String t = key.replaceFirst("#", "@");
            if (key.startsWith("#") && completed.contains(t) && completed.remove(t)) {
            }

            Thaumcraft.getScannedEntities().put(username, completed);
        }

        return true;
    }

    public static boolean completeScannedPhenomenaUnsaved(String username, String key) {
        List<String> completed = Thaumcraft.getScannedPhenomena().get(username);
        if (completed == null) {
            completed = new ArrayList<>();
        }

        if (!completed.contains(key)) {
            completed.add(key);
            String replacedKey = key.replaceFirst("#", "@");
            if (key.startsWith("#") && completed.contains(replacedKey)) {
                completed.remove(replacedKey);
            }

            Thaumcraft.getScannedPhenomena().put(username, completed);
        }

        return true;
    }

    public void completeScannedObject(String playerName, String object) {
        if (completeScannedObjectUnsaved(playerName, object)) {
            scheduleSave(playerName);
        }

    }

    public void completeScannedEntity(String playerName, String key) {
        if (completeScannedEntityUnsaved(playerName, key)) {
            scheduleSave(playerName);
        }

    }

    public void completeScannedPhenomena(String playerName, String key) {
        if (completeScannedPhenomenaUnsaved(playerName, key)) {
            scheduleSave(playerName);
        }

    }

    public static void loadPlayerData(String playerName, File file1, File file2, boolean legacy) {
        try {
            CompoundTag data = null;
            if (file1 != null && file1.exists()) {
                try {
                    FileInputStream fileinputstream = new FileInputStream(file1);
                    data = NbtIo.readCompressed(fileinputstream);
                    fileinputstream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (file1 == null || !file1.exists() || data == null || data.isEmpty()) {
                LOGGER.warn("Thaumcraft data not found for {}. Trying to load backup Thaumcraft data.", playerName);
                if (file2 != null && file2.exists()) {
                    try {
                        FileInputStream fileinputstream = new FileInputStream(file2);
                        data = NbtIo.readCompressed(fileinputstream);
                        fileinputstream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            if (data != null) {
                loadResearchNBT(data, playerName);
                loadClueNBT(data, playerName);
                loadAspectNBT(data, playerName);
                loadScannedNBT(data, playerName);


                if (THAUMCRAFT_PLAYER_SHIELDING_ACCESSOR.compoundTagHasKey(data)) {
                    int shielding = THAUMCRAFT_PLAYER_SHIELDING_ACCESSOR.readFromCompoundTag(data);
                    EventHandlerRunic.runicCharge.put(playerName, shielding);
                    EventHandlerRunic.isDirty = true;
                }

                if (THAUMCRAFT_PLAYER_WARP_PERM_ACCESSOR.compoundTagHasKey(data)) {
                    int warp = THAUMCRAFT_PLAYER_WARP_PERM_ACCESSOR.readFromCompoundTag(data);
                    if (legacy && !THAUMCRAFT_PLAYER_WARP_STICKY_ACCESSOR.compoundTagHasKey(data)) {
                        warp /= 2;
                        Thaumcraft.playerKnowledge.setWarpSticky(playerName, warp);
                    }
                    Thaumcraft.playerKnowledge.setWarpPerm(playerName, warp);
                }

                if (THAUMCRAFT_PLAYER_WARP_TEMP_ACCESSOR.compoundTagHasKey(data)) {
                    int warpTemp = THAUMCRAFT_PLAYER_WARP_TEMP_ACCESSOR.readFromCompoundTag(data);
                    Thaumcraft.playerKnowledge.setWarpTemp(playerName, warpTemp);
                }

                if (THAUMCRAFT_PLAYER_WARP_STICKY_ACCESSOR.compoundTagHasKey(data)) {
                    int warpSticky = THAUMCRAFT_PLAYER_WARP_STICKY_ACCESSOR.readFromCompoundTag(data);
                    Thaumcraft.playerKnowledge.setWarpSticky(playerName, warpSticky);
                }

                if (THAUMCRAFT_PLAYER_WARP_COUNTER_ACCESSOR.compoundTagHasKey(data)) {
                    int warpCounter = THAUMCRAFT_PLAYER_WARP_COUNTER_ACCESSOR.readFromCompoundTag(data);
                    Thaumcraft.playerKnowledge.setWarpCounter(playerName, warpCounter);
                } else {
                    Thaumcraft.playerKnowledge.setWarpCounter(playerName, 0);
                }

            } else {
                for (Aspect aspect : Aspects.ALL_ASPECTS.values()) {
                    if (!(aspect instanceof CompoundAspect)) {
//                        Thaumcraft.researchManager;
                        completeAspectUnsaved(playerName, aspect, (short) (15 + ThreadLocalRandom.current().nextInt(5)));
                    }
                }

                scheduleSave(playerName);
                LOGGER.info("Assigning initial aspects to {}", playerName);
            }
        } catch (Exception exception1) {
            exception1.printStackTrace();
            LOGGER.fatal("Error loading Thaumcraft data");
        }

    }

    public static void loadResearchNBT(CompoundTag entityData, String playerName) {

        ListTag list = THAUMCRAFT_PLAYER_RESEARCH_ACCESSOR
                .readFromCompoundTag(entityData);

        if (list == null) return;

        for (int i = 0; i < list.size(); ++i) {
            CompoundTag rs = list.getCompound(i);
            var key = LIST_TAG_RESEARCH_ACCESSOR.readFromCompoundTag(rs);
            if (key != null) {
                completeResearchUnsaved(playerName, key);
            }
        }
    }
    public static void loadClueNBT(CompoundTag entityData, String playerName) {

        ListTag list = THAUMCRAFT_PLAYER_CLUE_ACCESSOR
                .readFromCompoundTag(entityData);

        if (list == null) return;

        for (int i = 0; i < list.size(); ++i) {
            CompoundTag rs = list.getCompound(i);
            var key = LIST_TAG_CLUE_ACCESSOR.readFromCompoundTag(rs);
            if (key != null) {
                completeClueUnsaved(playerName, key);
            }
        }
    }
    public static void loadAspectNBT(CompoundTag entityData, String playerName) {

        ListTag list = THAUMCRAFT_PLAYER_ASPECTS_ACCESSOR
                .readFromCompoundTag(entityData);

        if (list == null) return;

        for (int i = 0; i < list.size(); ++i) {
            CompoundTag rs = list.getCompound(i);

            var key = LIST_TAG_ASPECT_ACCESSOR.readFromCompoundTag(rs);
            if (key == null) continue;

            Aspect aspect = Aspect.getAspect(key);
            if (aspect == null) continue;

            int amount = LIST_TAG_ASPECT_INT_ACCESSOR.readFromCompoundTag(rs);

            completeAspectUnsaved(playerName, aspect, amount);
        }
    }


//    public static void loadResearchNBT(CompoundTag entityData, String playerName) {
//        NBTTagList tagList = entityData.getTagList("THAUMCRAFT.RESEARCH", 10);
//
//        for (int j = 0; j < tagList.tagCount(); ++j) {
//            CompoundTag rs = tagList.getCompoundTagAt(j);
//            if (rs.hasKey("key")) {
//                completeResearchUnsaved(playerName, rs.getString("key"));
//            }
//        }
//
//    }
//
//    public static void loadAspectNBT(CompoundTag entityData, String playerName) {
//        if (entityData.hasKey("THAUMCRAFT.ASPECTS")) {
//            NBTTagList tagList = entityData.getTagList("THAUMCRAFT.ASPECTS", 10);
//
//            for (int j = 0; j < tagList.tagCount(); ++j) {
//                CompoundTag rs = tagList.getCompoundTagAt(j);
//                if (rs.hasKey("key")) {
//                    Aspect aspect = Aspect.getAspect(rs.getString("key"));
//                    short amount = rs.getShort("amount");
//                    if (aspect != null) {
//                        completeAspectUnsaved(playerName, aspect, amount);
//                    }
//                }
//            }
//        }
//    }

    public static void loadScannedNBT(CompoundTag entityData, String playerName) {

        // ---- THAUMCRAFT.SCAN.OBJECTS ----
        ListTag objList = THAUMCRAFT_PLAYER_SCAN_OBJECTS_ACCESSOR.readFromCompoundTag(entityData);

        if (objList != null) {
            for (int i = 0; i < objList.size(); ++i) {
                CompoundTag rs = objList.getCompound(i);
                String key = LIST_TAG_SCANNED_OBJECT_ACCESSOR.readFromCompoundTag(rs);
                if (key != null) {
                    completeScannedObjectUnsaved(playerName, key);
                }
            }
        }

        // ---- THAUMCRAFT.SCAN.ENTITIES ----
        ListTag entList = THAUMCRAFT_PLAYER_SCAN_ENTITIES_ACCESSOR.readFromCompoundTag(entityData);

        if (entList != null) {
            for (int i = 0; i < entList.size(); ++i) {
                CompoundTag rs = entList.getCompound(i);
                String key = LIST_TAG_SCANNED_ENTITY_ACCESSOR.readFromCompoundTag(rs);
                if (key != null) {
                    completeScannedEntityUnsaved(playerName, key);
                }
            }
        }

        // ---- THAUMCRAFT.SCAN.PHENOMENA ----
        ListTag pheList = THAUMCRAFT_PLAYER_SCAN_PHENOMENA_ACCESSOR.readFromCompoundTag(entityData);

        if (pheList != null) {
            for (int i = 0; i < pheList.size(); ++i) {
                CompoundTag rs = pheList.getCompound(i);
                String key = LIST_TAG_SCANNED_PHENOMENA_ACCESSOR.readFromCompoundTag(rs);
                if (key != null) {
                    completeScannedPhenomenaUnsaved(playerName, key);
                }
            }
        }
    }

    public static void scheduleSave(String playerName) {
        if (Platform.getEnvironment() != Env.SERVER){return;}
        //TODO:Impl or remove
    }

    public static boolean savePlayerData(String playerName, File file1, File file2) {
        boolean success = true;

        try {
            CompoundTag data = new CompoundTag();
            saveResearchNBT(data, playerName);
            saveClueNBT(data, playerName);
            saveAspectNBT(data, playerName);
            saveScannedNBT(data, playerName);


            // runic shielding
            if (EventHandlerRunic.runicCharge.containsKey(playerName)) {
                THAUMCRAFT_PLAYER_SHIELDING_ACCESSOR
                        .writeToCompoundTag(data, EventHandlerRunic.runicCharge.get(playerName));
            }

            // warp values
            THAUMCRAFT_PLAYER_WARP_PERM_ACCESSOR.writeToCompoundTag(data, Thaumcraft.playerKnowledge.getWarpPerm(playerName));
            THAUMCRAFT_PLAYER_WARP_TEMP_ACCESSOR.writeToCompoundTag(data, Thaumcraft.playerKnowledge.getWarpTemp(playerName));
            THAUMCRAFT_PLAYER_WARP_STICKY_ACCESSOR.writeToCompoundTag(data, Thaumcraft.playerKnowledge.getWarpSticky(playerName));
            THAUMCRAFT_PLAYER_WARP_COUNTER_ACCESSOR.writeToCompoundTag(data, Thaumcraft.playerKnowledge.getWarpCounter(playerName));

//            if (Thaumcraft.instance.runicEventHandler.runicCharge.containsKey(playerName)) {
//                data.setTag("Thaumcraft.shielding", new NBTTagInt(Thaumcraft.instance.runicEventHandler.runicCharge.get(playerName)));
//            }
//
//            data.setTag("Thaumcraft.eldritch", new NBTTagInt(Thaumcraft.playerKnowledge.getWarpPerm(playerName)));
//            data.setTag("Thaumcraft.eldritch.temp", new NBTTagInt(Thaumcraft.playerKnowledge.getWarpTemp(playerName)));
//            data.setTag("Thaumcraft.eldritch.sticky", new NBTTagInt(Thaumcraft.playerKnowledge.getWarpSticky(playerName)));
//            data.setTag("Thaumcraft.eldritch.counter", new NBTTagInt(Thaumcraft.playerKnowledge.getWarpCounter(playerName)));


            if (file1 != null && file1.exists()) {
                try {
                    Files.copy(file1, file2);
                } catch (Exception var8) {
                    LOGGER.error("Could not backup old research file for player {}", playerName, var8);
                }
            }

            try {
                if (file1 != null) {
                    FileOutputStream fileoutputstream = new FileOutputStream(file1);
                    NbtIo.writeCompressed(data, fileoutputstream);
                    fileoutputstream.close();
                }
            } catch (Exception var9) {
                LOGGER.error("Could not save research file for player {}", playerName);
                if (file1.exists()) {
                    try {
                        file1.delete();
                    } catch (Exception ignored) {
                        LOGGER.error("Could not delete old research file for player {}", playerName);
                    }
                }

                success = false;
            }
        } catch (Exception exception1) {
            LOGGER.fatal("Error saving Thaumcraft data",exception1);
            success = false;
        }

        return success;
    }

    public static void saveResearchNBT(CompoundTag entityData, String playerName) {
        var res = getResearchForPlayer(playerName);
        ListTag tagList = new ListTag();

        if (res != null && !res.isEmpty()) {
            for (var playerResearch : res) {
                CompoundTag f = new CompoundTag();
                LIST_TAG_RESEARCH_ACCESSOR.writeToCompoundTag(f, playerResearch);
                tagList.add(f);
            }
        }

        THAUMCRAFT_PLAYER_RESEARCH_ACCESSOR.writeToCompoundTag(entityData, tagList);
    }


    public static void saveClueNBT(CompoundTag entityData, String playerName) {
        var res = getClueForPlayer(playerName);
        ListTag tagList = new ListTag();

        if (res != null && !res.isEmpty()) {
            for (var key : res) {
                CompoundTag f = new CompoundTag();
                LIST_TAG_CLUE_ACCESSOR.writeToCompoundTag(f, key);
                tagList.add(f);
            }
        }

        THAUMCRAFT_PLAYER_CLUE_ACCESSOR.writeToCompoundTag(entityData, tagList);
    }

    public static void saveAspectNBT(CompoundTag entityData, String playerName) {
        AspectList<Aspect> res = Thaumcraft.getKnownAspects().get(playerName);
        ListTag tagList = new ListTag();

        if (res != null && !res.isEmpty()) {
            for (Aspect aspect : res.getAspectTypes()) {
                if (aspect != null) {
                    CompoundTag f = new CompoundTag();
                    // 用 Accessor 写入 key 和 amount
                    ASPECT_KEY_ACCESSOR.writeToCompoundTag(f, aspect.getAspectKey());
                    ASPECT_AMOUNT_ACCESSOR.writeToCompoundTag(f, res.getAmount(aspect));
                    tagList.add(f);
                }
            }
        }
        THAUMCRAFT_PLAYER_ASPECTS_ACCESSOR.writeToCompoundTag(entityData, tagList);
    }

    public static void saveScannedNBT(CompoundTag entityData, String playerName) {
        List<String> objects = Thaumcraft.getScannedObjects().get(playerName);
        ListTag objectTagList = new ListTag();
        if (objects != null && !objects.isEmpty()) {
            for (String obj : objects) {
                if (obj != null) {
                    CompoundTag f = new CompoundTag();
                    LIST_TAG_SCANNED_OBJECT_ACCESSOR.writeToCompoundTag(f, obj); // 这里 f 只需要 key
                    objectTagList.add(f);
                }
            }
        }
        THAUMCRAFT_PLAYER_SCAN_OBJECTS_ACCESSOR.writeToCompoundTag(entityData, objectTagList);

        List<String> entities = Thaumcraft.getScannedEntities().get(playerName);
        ListTag entityTagList = new ListTag();
        if (entities != null && !entities.isEmpty()) {
            for (String e : entities) {
                if (e != null) {
                    CompoundTag f = new CompoundTag();
                    LIST_TAG_SCANNED_ENTITY_ACCESSOR.writeToCompoundTag(f, e);
                    entityTagList.add(f);
                }
            }
        }
        THAUMCRAFT_PLAYER_SCAN_ENTITIES_ACCESSOR.writeToCompoundTag(entityData, entityTagList);

        List<String> phenomena = Thaumcraft.getScannedPhenomena().get(playerName);
        ListTag phenomenaTagList = new ListTag();
        if (phenomena != null && !phenomena.isEmpty()) {
            for (String p : phenomena) {
                if (p != null) {
                    CompoundTag f = new CompoundTag();
                    LIST_TAG_SCANNED_PHENOMENA_ACCESSOR.writeToCompoundTag(f, p);
                    phenomenaTagList.add(f);
                }
            }
        }
        THAUMCRAFT_PLAYER_SCAN_PHENOMENA_ACCESSOR.writeToCompoundTag(entityData, phenomenaTagList);
    }
}
