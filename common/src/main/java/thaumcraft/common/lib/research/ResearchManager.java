package thaumcraft.common.lib.research;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import thaumcraft.api.IScribeTools;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchCategoryList;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.Config;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.lib.network.playerdata.PacketResearchCompleteS2C;
import thaumcraft.common.lib.utils.HexUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static com.linearity.opentc4.Consts.AspectCompoundTagAccessors.*;
import static com.linearity.opentc4.Consts.ResearchNoteCompoundTagAccessors.*;
import static com.linearity.opentc4.Consts.ThaumcraftPlayerCompoundTagAccessors.*;
import static com.linearity.opentc4.OpenTC4.LOGGER;
import static com.linearity.opentc4.OpenTC4.platformUtils;
import static thaumcraft.common.ThaumcraftSounds.LEARN;
import static thaumcraft.common.lib.events.EventHandlerEntity.getThaumcraftPlayersDirectory;

public class ResearchManager {
    static ArrayList<ResearchItem> allHiddenResearch = null;
    static ArrayList<ResearchItem> allValidResearch = null;
    private static final String RESEARCH_TAG = "THAUMCRAFT.RESEARCH";
    private static final String ASPECT_TAG = "THAUMCRAFT.ASPECTS";
    private static final String SCANNED_OBJ_TAG = "THAUMCRAFT.SCAN.OBJECTS";
    private static final String SCANNED_ENT_TAG = "THAUMCRAFT.SCAN.ENTITIES";
    private static final String SCANNED_PHE_TAG = "THAUMCRAFT.SCAN.PHENOMENA";

    public static boolean createClue(Level world, Player player, ItemStack clue, AspectList aspects) {
        return createClue(world, player, clue.getItem(), aspects);
    }

    public static boolean createClue(Level world, Player player, Item clue, AspectList aspects) {
        ArrayList<String> keys = new ArrayList<>();
        for (ResearchCategoryList rcl : ResearchCategories.researchCategories.values()) {
            label110:
            for (ResearchItem ri : rcl.research.values()) {
                boolean valid = ri.tags != null && ri.tags.size() > 0 && (ri.isLost() || ri.isHidden()) && !isResearchComplete(player.getName().getString(), ri.key) && !isResearchComplete(player.getName().getString(), "@" + ri.key);
                if (valid) {
                    if (ri.getItemTriggers() != null) {
                        for (ItemStack stack : ri.getItemTriggers()) {
                            if (
                                    Objects.equals(stack.getItem(), clue)
//                             InventoryUtils.areItemStacksEqual(stack, clue, true, true)
                            ) {
                                keys.add(ri.key);
                                continue label110;
                            }
                        }
                    }

                    if (aspects != null && aspects.size() > 0 && ri.getAspectTriggers() != null) {
                        ri.getAspectTriggers();
                        for (Aspect aspect : ri.getAspectTriggers()) {
                            if (aspects.getAmount(aspect) > 0) {
                                keys.add(ri.key);
                                break;
                            }
                        }
                    }
                }
            }
        }

        if (!keys.isEmpty()) {
            String key = keys.get(world.getRandom().nextInt(keys.size()));
            if (player instanceof ServerPlayer serverPlayer) {
                new PacketResearchCompleteS2C("@" + key).sendTo(serverPlayer);
            }else {
                LOGGER.warn("createclue:not a server playere:{}",player.getName().getString());
            }
            Thaumcraft.researchManager.completeResearch(player, "@" + key);
            return true;
        } else {
            return false;
        }
    }

    public static boolean createClue(Level world, Player player, ResourceKey<EntityType<?>> clue, AspectList aspects) {
        ArrayList<String> keys = new ArrayList<>();
        for (ResearchCategoryList rcl : ResearchCategories.researchCategories.values()) {
            label110:
            for (ResearchItem ri : rcl.research.values()) {
                boolean valid = ri.tags != null && ri.tags.size() > 0 && (ri.isLost() || ri.isHidden()) && !isResearchComplete(player.getName().getString(), ri.key) && !isResearchComplete(player.getName().getString(), "@" + ri.key);
                if (valid) {
                    {
                        if (ri.getEntityTriggers() != null) {
                            ri.getEntityTriggers();
                            for (ResourceKey<EntityType<?>> entity : ri.getEntityTriggers()) {
                                if (clue.equals(entity)) {
                                    keys.add(ri.key);
                                    continue label110;
                                }
                            }
                        }
                    }

                    if (aspects != null && aspects.size() > 0 && ri.getAspectTriggers() != null) {
                        ri.getAspectTriggers();
                        for (Aspect aspect : ri.getAspectTriggers()) {
                            if (aspects.getAmount(aspect) > 0) {
                                keys.add(ri.key);
                                break;
                            }
                        }
                    }
                }
            }
        }

        if (!keys.isEmpty()) {
            String key = keys.get(world.getRandom().nextInt(keys.size()));
            if (player instanceof ServerPlayer serverPlayer) {
                new PacketResearchCompleteS2C("@" + key).sendTo(serverPlayer);
            }else {
                LOGGER.warn("createclue:not a server playere:{}",player.getName().getString());
            }
            Thaumcraft.researchManager.completeResearch(player, "@" + key);
            return true;
        } else {
            return false;
        }
    }

    public static ItemStack createResearchNoteForPlayer(Level world, Player player, String key) {
        ItemStack note = ItemStack.EMPTY;
        int slot = getResearchSlot(player, key);

        if (slot >= 0) {
            // 玩家已有笔记
            note = player.getInventory().getItem(slot);
        } else {
            // 检查玩家是否有墨水和纸
            if (consumeInkFromPlayer(player, false) && consumePaperFromPlayer(player)) {
                consumeInkFromPlayer(player, true);

                note = createNote(new ItemStack(ConfigItems.itemResearchNotes), key, world);

                // 尝试放入背包，放不下则掉落
                if (!player.getInventory().add(note)) {
                    player.drop(note, false);
                }

                // 更新容器界面（客户端同步）
                player.containerMenu.broadcastChanges();
            }
        }

        return note;
    }

    // 1.20.1 获取纸张的方法（Inventory.consumeInventoryItem 已废弃）
    private static boolean consumePaperFromPlayer(Player player) {
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.is(Items.PAPER)) {
                stack.shrink(1);
                if (stack.isEmpty()) {
                    player.getInventory().setItem(i, ItemStack.EMPTY);
                }
                return true;
            }
        }
        return false;
    }

    public static String findHiddenResearch(Player player) {
        if (allHiddenResearch == null) {
            allHiddenResearch = new ArrayList<>();

            for (ResearchCategoryList cat : ResearchCategories.researchCategories.values()) {
                for (ResearchItem ri : cat.research.values()) {
                    if (ri.isHidden() && ri.tags != null && ri.tags.size() > 0) {
                        allHiddenResearch.add(ri);
                    }
                }
            }
        }

        ArrayList<String> keys = new ArrayList<>();

        for (ResearchItem research : allHiddenResearch) {
            if (!isResearchComplete(player.getName().getString(), research.key)
                    && doesPlayerHaveRequisites(player.getName().getString(), research.key)
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
            return "FAIL";
        }
    }

    public static String findMatchingResearch(Player player, Aspect aspect) {
        String randomMatch = null;
        if (allValidResearch == null) {
            allValidResearch = new ArrayList<>();

            for (ResearchCategoryList cat : ResearchCategories.researchCategories.values()) {
                for (ResearchItem ri : cat.research.values()) {
                    boolean secondary = ri.isSecondary() && Config.researchDifficulty == 0 || Config.researchDifficulty == -1;
                    if (!secondary && !ri.isHidden() && !ri.isLost() && !ri.isAutoUnlock() && !ri.isVirtual() && !ri.isStub()) {
                        allValidResearch.add(ri);
                    }
                }
            }
        }

        ArrayList<String> keys = new ArrayList<>();

        for (ResearchItem research : allValidResearch) {
            if (!isResearchComplete(player.getName().getString(), research.key) && doesPlayerHaveRequisites(player.getName().getString(), research.key) && research.tags.getAmount(aspect) > 0) {
                keys.add(research.key);
            }
        }

        if (!keys.isEmpty()) {
            randomMatch = keys.get(player.getRandom().nextInt(keys.size()));
        }

        return randomMatch;
    }

    public static int getResearchSlot(Player player, String key) {
        NonNullList<ItemStack> inv = player.getInventory().items;
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
//    public static int getResearchSlot(Player player, String key) {
//        ItemStack[] inv = player.inventory.mainInventory;
//        if (inv != null && inv.length != 0) {
//            for (int a = 0; a < inv.length; ++a) {
//                if (inv[a] != null && inv[a].getItem() != null
//                        && inv[a].getItem() == ConfigItems.itemResearchNotes
//                        && getData(inv[a]) != null
//                        && getData(inv[a]).key.equals(key)
//                ) {
//                    return a;
//                }
//            }
//
//        }
//        return -1;
//    }

    public static boolean consumeInkFromPlayer(Player player, boolean doit) {
        NonNullList<ItemStack> inv = player.getInventory().items;
        for (ItemStack stack : inv) {
            if (!stack.isEmpty() && stack.getItem() instanceof IScribeTools && stack.getDamageValue() < stack.getMaxDamage()) {
                if (doit) {
                    stack.hurtAndBreak(1, player, p -> {}); // 玩家对象回调
                }
                return true;
            }
        }
        return false;
    }

//    public static boolean consumeInkFromPlayer(Player player, boolean doit) {
//        ItemStack[] inv = player.inventory.mainInventory;
//
//        for (ItemStack itemStack : inv) {
//            if (itemStack != null && itemStack.getItem() instanceof IScribeTools && itemStack.getDamageValue() < itemStack.getMaxDamage()) {
//                if (doit) {
//                    itemStack.damageItem(1, player);
//                }
//
//                return true;
//            }
//        }
//
//        return false;
//    }

    public static boolean consumeInkFromTable(ItemStack stack, boolean doit) {
        if (!stack.isEmpty() && stack.getItem() instanceof IScribeTools && stack.getDamageValue() < stack.getMaxDamage()) {
            if (doit) {
                //yeah it's on the table
                stack.hurt(1, RandomSource.create(), null); // 无玩家回调
            }
            return true;
        }
        return false;
    }
    
//    public static boolean consumeInkFromTable(ItemStack stack, boolean doit) {
//        if (stack != null && stack.getItem() instanceof IScribeTools && stack.getDamageValue() < stack.getMaxDamage()) {
//            if (doit) {
//                stack.setItemDamage(stack.getDamageValue() + 1);
//            }
//
//            return true;
//        } else {
//            return false;
//        }
//    }

    public static boolean checkResearchCompletion(ItemStack contents, ResearchNoteData note, String username) {
        ArrayList<String> checked = new ArrayList<>();
        ArrayList<String> main = new ArrayList<>();
        ArrayList<String> remains = new ArrayList<>();

        for (HexUtils.Hex hex : note.hexes.values()) {
            if (note.hexEntries.get(hex.toString()).type == 1) {
                main.add(hex.toString());
            }
        }

        for (HexUtils.Hex hex : note.hexes.values()) {
            if (note.hexEntries.get(hex.toString()).type == 1) {
                main.remove(hex.toString());
                checkConnections(note, hex, checked, main, remains, username);
                break;
            }
        }

        if (!main.isEmpty()) {
            return false;
        } else {
            ArrayList<String> remove = new ArrayList<>();

            for (HexUtils.Hex hex : note.hexes.values()) {
                if (note.hexEntries.get(hex.toString()).type != 1 && !remains.contains(hex.toString())) {
                    remove.add(hex.toString());
                }
            }

            for (String s : remove) {
                note.hexEntries.remove(s);
                note.hexes.remove(s);
            }

            note.complete = true;
            updateData(contents, note);
            return true;
        }
    }

    private static void checkConnections(ResearchNoteData note, HexUtils.Hex hex, ArrayList checked, ArrayList main, ArrayList remains, String username) {
        checked.add(hex.toString());

        for (int a = 0; a < 6; ++a) {
            HexUtils.Hex target = hex.getNeighbour(a);
            if (!checked.contains(target.toString()) && note.hexEntries.containsKey(target.toString()) && note.hexEntries.get(target.toString()).type >= 1) {
                Aspect aspect1 = note.hexEntries.get(hex.toString()).aspect;
                Aspect aspect2 = note.hexEntries.get(target.toString()).aspect;
                if (Thaumcraft.playerKnowledge.hasDiscoveredAspect(username, aspect1) && Thaumcraft.playerKnowledge.hasDiscoveredAspect(username, aspect2) && (!aspect1.isPrimal() && (aspect1.getComponents()[0] == aspect2 || aspect1.getComponents()[1] == aspect2) || !aspect2.isPrimal() && (aspect2.getComponents()[0] == aspect1 || aspect2.getComponents()[1] == aspect1))) {
                    remains.add(target.toString());
                    if (note.hexEntries.get(target.toString()).type == 1) {
                        main.remove(target.toString());
                    }

                    checkConnections(note, target, checked, main, remains, username);
                }
            }
        }

    }

    public static ItemStack createNote(ItemStack stack, String key, Level world) {
        ResearchItem rr = ResearchCategories.getResearch(key);
        Aspect primaryAspect = rr.getResearchPrimaryTag();
        if (primaryAspect == null) {
            return ItemStack.EMPTY;
        }

        CompoundTag tag = stack.getOrCreateTag();
        RESEARCH_NOTE_KEY_ACCESSOR.writeToCompoundTag(tag, key);
        RESEARCH_NOTE_COLOR_ACCESSOR.writeToCompoundTag(tag, primaryAspect.getColor());
        RESEARCH_NOTE_COMPLETE_ACCESSOR.writeToCompoundTag(tag, false);
        RESEARCH_NOTE_COPIES_ACCESSOR.writeToCompoundTag(tag, 0);

        int radius = 1 + Math.min(3, rr.getComplexity());
        HashMap<String, HexUtils.Hex> hexLocs = HexUtils.generateHexes(radius);
        List<HexUtils.Hex> outerRing = HexUtils.distributeRingRandomly(radius, rr.tags.size(), world.getRandom());
        HashMap<String, HexEntry> hexEntries = new HashMap<>();
        HashMap<String, HexUtils.Hex> hexes = new HashMap<>();

        for (HexUtils.Hex hex : hexLocs.values()) {
            hexes.put(hex.toString(), hex);
            hexEntries.put(hex.toString(), new HexEntry(null, 0));
        }

        int count = 0;

        for (HexUtils.Hex hex : outerRing) {
            hexes.put(hex.toString(), hex);
            hexEntries.put(hex.toString(), new HexEntry(rr.tags.getAspectTypes()[count], 1));
            ++count;
        }

        if (rr.getComplexity() > 1) {
            removeRandomBlanks(world.getRandom(), rr, hexes, hexEntries);
        }

        //got angry with some research with some node blocked and whole note can't be finished
        ensureEndpointsConnected(hexes, hexEntries);

        ListTag gridTag = new ListTag();
        for (HexUtils.Hex hex : hexes.values()) {
            CompoundTag gt = new CompoundTag();
            RESEARCH_NOTE_HEX_Q_ACCESSOR.writeToCompoundTag(gt, (byte) hex.q);
            RESEARCH_NOTE_HEX_R_ACCESSOR.writeToCompoundTag(gt, (byte) hex.r);
            RESEARCH_NOTE_HEX_TYPE_ACCESSOR.writeToCompoundTag(gt, (byte) hexEntries.get(hex.toString()).type);

            if (hexEntries.get(hex.toString()).aspect != null) {
                RESEARCH_NOTE_HEX_ASPECT_ACCESSOR.writeToCompoundTag(gt,
                        hexEntries.get(hex.toString()).aspect.getTag());
            }
            gridTag.add(gt);
        }

        RESEARCH_NOTE_HEXGRID_ACCESSOR.writeToCompoundTag(tag, gridTag);

        return stack;

    }

    private static void removeRandomBlanks(RandomSource random, ResearchItem rr, HashMap<String, HexUtils.Hex> hexes, HashMap<String, HexEntry> hexEntries) {
        int blanks = rr.getComplexity() * 2;
        HexUtils.Hex[] temp = hexes.values().toArray(new HexUtils.Hex[0]);

        while (blanks > 0) {
            int indx = random.nextInt(temp.length);
            if (hexEntries.get(temp[indx].toString()) != null && hexEntries.get(temp[indx].toString()).type == 0) {
                boolean gtg = true;

                for (int n = 0; n < 6; ++n) {
                    HexUtils.Hex neighbour = temp[indx].getNeighbour(n);
                    if (hexes.containsKey(neighbour.toString()) && hexEntries.get(neighbour.toString()).type == 1) {
                        int cc = 0;

                        for (int q = 0; q < 6; ++q) {
                            if (hexes.containsKey(hexes.get(neighbour.toString()).getNeighbour(q).toString())) {
                                ++cc;
                            }

                            if (cc >= 2) {
                                break;
                            }
                        }

                        if (cc < 2) {
                            gtg = false;
                            break;
                        }
                    }
                }

                if (gtg) {
                    hexes.remove(temp[indx].toString());
                    hexEntries.remove(temp[indx].toString());
                    temp = hexes.values().toArray(new HexUtils.Hex[0]);
                    --blanks;
                }
            }
        }
    }

    private static void ensureEndpointsConnected(
            Map<String, HexUtils.Hex> hexes,
            Map<String, HexEntry> hexEntries
    ) {
        // 找出端点 hex
        List<HexUtils.Hex> endpoints = hexEntries.entrySet().stream()
                .filter(e -> e.getValue().type == 1)
                .map(e -> hexes.get(e.getKey()))
                .filter(Objects::nonNull)
                .toList();

        if (endpoints.size() < 2) return;

        // BFS helper
        Set<String> visited = new HashSet<>();
        for (int i = 0; i < endpoints.size() - 1; i++) {
            HexUtils.Hex start = endpoints.get(i);
            HexUtils.Hex end = endpoints.get(i + 1);

            if (!isConnected(start, end, hexes, hexEntries, visited)) {
                // 找最短路径恢复
                List<HexUtils.Hex> path = findPathAllowRemoved(start, end, hexes, hexEntries);
                for (HexUtils.Hex h : path) {
                    String key = h.toString();
                    if (!hexEntries.containsKey(key)) {
                        hexEntries.put(key, new HexEntry(null, 0)); // 恢复空 hex
                        hexes.put(key, h);
                    }
                }
            }
        }
    }

    // 检查两个端点是否连通
    //author:ChatGPT
    private static boolean isConnected(HexUtils.Hex start, HexUtils.Hex end,
                                       Map<String, HexUtils.Hex> hexes,
                                       Map<String, HexEntry> hexEntries,
                                       Set<String> visited) {
        visited.clear();
        Queue<HexUtils.Hex> queue = new ArrayDeque<>();
        queue.add(start);
        visited.add(start.toString());

        while (!queue.isEmpty()) {
            HexUtils.Hex current = queue.poll();
            if (current.equals(end)) return true;

            for (int i = 0; i < 6; i++) {
                HexUtils.Hex n = current.getNeighbour(i);
                String key = n.toString();
                if (!visited.contains(key) && hexes.containsKey(key)) {
                    queue.add(n);
                    visited.add(key);
                }
            }
        }
        return false;
    }

    // 找到 start -> end 的路径，允许穿过被移除 hex
    //author:ChatGPT
    private static List<HexUtils.Hex> findPathAllowRemoved(HexUtils.Hex start, HexUtils.Hex end,
                                                           Map<String, HexUtils.Hex> hexes,
                                                           Map<String, HexEntry> hexEntries) {
        Map<String, HexUtils.Hex> cameFrom = new HashMap<>();
        Queue<HexUtils.Hex> queue = new ArrayDeque<>();
        queue.add(start);
        cameFrom.put(start.toString(), null);

        while (!queue.isEmpty()) {
            HexUtils.Hex current = queue.poll();
            if (current.equals(end)) break;

            for (int i = 0; i < 6; i++) {
                HexUtils.Hex n = current.getNeighbour(i);
                String key = n.toString();
                if (!cameFrom.containsKey(key)) {
                    queue.add(n);
                    cameFrom.put(key, current);
                }
            }
        }

        // 回溯路径
        List<HexUtils.Hex> path = new ArrayList<>();
        HexUtils.Hex cursor = end;
        while (cursor != null && !cursor.equals(start)) {
            path.add(cursor);
            cursor = cameFrom.get(cursor.toString());
        }
        Collections.reverse(path);
        return path;
    }

    public static ResearchNoteData getData(ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            return null;
        }

        CompoundTag tag = stack.getOrCreateTag();
        ResearchNoteData data = new ResearchNoteData();

        // 基本信息
        data.key = RESEARCH_NOTE_KEY_ACCESSOR.readFromCompoundTag(tag);
        data.color = RESEARCH_NOTE_COLOR_ACCESSOR.readFromCompoundTag(tag);
        data.complete = RESEARCH_NOTE_COMPLETE_ACCESSOR.readFromCompoundTag(tag);
        data.copies = RESEARCH_NOTE_COPIES_ACCESSOR.readFromCompoundTag(tag);

        // Hex 网格
        ListTag gridTag = RESEARCH_NOTE_HEXGRID_ACCESSOR.readFromCompoundTag(tag);
        data.hexEntries = new HashMap<>();
        data.hexes = new HashMap<>();

        for (int i = 0; i < gridTag.size(); i++) {
            CompoundTag hexTag = gridTag.getCompound(i);

            int q = RESEARCH_NOTE_HEX_Q_ACCESSOR.readFromCompoundTag(hexTag);
            int r = RESEARCH_NOTE_HEX_R_ACCESSOR.readFromCompoundTag(hexTag);
            int type = RESEARCH_NOTE_HEX_TYPE_ACCESSOR.readFromCompoundTag(hexTag);
            String aspectTag = RESEARCH_NOTE_HEX_ASPECT_ACCESSOR.readFromCompoundTag(hexTag);
            Aspect aspect = aspectTag != null ? Aspect.getAspect(aspectTag) : null;

            HexUtils.Hex hex = new HexUtils.Hex(q, r);
            data.hexEntries.put(hex.toString(), new HexEntry(aspect, type));
            data.hexes.put(hex.toString(), hex);
        }

        stack.setTag(tag);

        return data;
    }
    public static void updateData(ItemStack stack, ResearchNoteData data) {
        if (stack == null || stack.isEmpty() || data == null) return;

        CompoundTag tag = stack.getOrCreateTag();

        // 基本信息
        RESEARCH_NOTE_KEY_ACCESSOR.writeToCompoundTag(tag, data.key);
        RESEARCH_NOTE_COLOR_ACCESSOR.writeToCompoundTag(tag, data.color);
        RESEARCH_NOTE_COMPLETE_ACCESSOR.writeToCompoundTag(tag, data.complete);
        RESEARCH_NOTE_COPIES_ACCESSOR.writeToCompoundTag(tag, data.copies);

        // Hex 网格
        ListTag gridTag = new ListTag();
        for (HexUtils.Hex hex : data.hexes.values()) {
            CompoundTag hexTag = new CompoundTag();
            HexEntry entry = data.hexEntries.get(hex.toString());

            RESEARCH_NOTE_HEX_Q_ACCESSOR.writeToCompoundTag(hexTag, (byte) hex.q);
            RESEARCH_NOTE_HEX_R_ACCESSOR.writeToCompoundTag(hexTag, (byte) hex.r);
            RESEARCH_NOTE_HEX_TYPE_ACCESSOR.writeToCompoundTag(hexTag, (byte) entry.type);

            if (entry.aspect != null) {
                RESEARCH_NOTE_HEX_ASPECT_ACCESSOR.writeToCompoundTag(hexTag, entry.aspect.getTag());
            }

            gridTag.add(hexTag);
        }

        RESEARCH_NOTE_HEXGRID_ACCESSOR.writeToCompoundTag(tag, gridTag);
    }

    public static boolean isResearchComplete(String playername, String key) {
        if (!key.startsWith("@") && ResearchCategories.getResearch(key) == null) {
            return false;
        } else {
            List<String> completed = getResearchForPlayer(playername);
            return completed != null && !completed.isEmpty() && completed.contains(key);
        }
    }

    public static List<String> getResearchForPlayer(String playername) {
        List<String> out = Thaumcraft.getCompletedResearch().get(playername);

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
        } catch (Exception ignored) {
        }

        return out;
    }

    public static List<String> getResearchForPlayerSafe(String playername) {
        return Thaumcraft.getCompletedResearch().get(playername);
    }

    public static boolean doesPlayerHaveRequisites(String playername, String key) {
        boolean out = true;
        String[] parents = ResearchCategories.getResearch(key).parents;
        if (parents != null && parents.length > 0) {
            out = false;
            List<String> completed = getResearchForPlayer(playername);
            if (completed != null && !completed.isEmpty()) {
                out = true;

                for (String item : parents) {
                    if (!completed.contains(item)) {
                        return false;
                    }
                }
            }
        }

        parents = ResearchCategories.getResearch(key).parentsHidden;
        if (parents != null && parents.length > 0) {
            out = false;
            List<String> completed = getResearchForPlayer(playername);
            if (completed != null && !completed.isEmpty()) {
                out = true;

                for (String item : parents) {
                    if (!completed.contains(item)) {
                        return false;
                    }
                }
            }
        }

        return out;
    }

    public static Aspect getCombinationResult(Aspect aspect1, Aspect aspect2) {
        for (Aspect aspect : Aspect.aspects.values()) {
            if (aspect.getComponents() != null && (aspect.getComponents()[0] == aspect1 && aspect.getComponents()[1] == aspect2 || aspect.getComponents()[0] == aspect2 && aspect.getComponents()[1] == aspect1)) {
                return aspect;
            }
        }

        return null;
    }

    public static AspectList reduceToPrimals(AspectList al) {
        return reduceToPrimals(al, false);
    }

    public static AspectList reduceToPrimals(AspectList al, boolean merge) {
        AspectList out = new AspectList();

        for (Aspect aspect : al.getAspectTypes()) {
            if (aspect != null) {
                if (aspect.isPrimal()) {
                    if (merge) {
                        out.mergeWithHighest(aspect, al.getAmount(aspect));
                    } else {
                        out.addAll(aspect, al.getAmount(aspect));
                    }
                } else {
                    AspectList send = new AspectList();
                    send.addAll(aspect.getComponents()[0], al.getAmount(aspect));
                    send.addAll(aspect.getComponents()[1], al.getAmount(aspect));
                    send = reduceToPrimals(send, merge);

                    for (Aspect a : send.getAspectTypes()) {
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

    public static boolean completeResearchUnsaved(String username, String key) {
        List<String> completed = getResearchForPlayerSafe(username);
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

    public static void unlockResearchForPlayer(Level world, ServerPlayer player, String research, String... preRequsites) {
        for (String preReq : preRequsites) {
            if (!isResearchComplete(player.getName().getString(), preReq)){return;}
        }
        if (isResearchComplete(player.getName().getString(), research)){return;}
        new PacketResearchCompleteS2C(research).sendTo(player);
        Thaumcraft.researchManager.completeResearch(player, research);
        player.playSound(LEARN);//,.75f,1.f
    }

    public void completeResearch(Player player, String key) {
        String playerName = player.getName().getString();
        if (completeResearchUnsaved(playerName, key)) {
            int warp = ThaumcraftApi.getWarp(key);
            if (warp > 0 && !Config.wuss && Platform.getEnvironment() != Env.CLIENT) {
                if (warp > 1) {
                    int w2 = warp / 2;
                    if (warp - w2 > 0) {
                        Thaumcraft.addWarpToPlayer(player, warp - w2, false);
                    }

                    if (w2 > 0) {//what anazor did?
                        Thaumcraft.addStickyWarpToPlayer(player, w2);
                    }
                } else {
                    Thaumcraft.addWarpToPlayer(player, warp, false);
                }
            }

            scheduleSave(playerName);
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
            if (object.startsWith("#") && completed.contains(t) && completed.remove(t)) {
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
            String t = key.replaceFirst("#", "@");
            if (key.startsWith("#") && completed.contains(t) && completed.remove(t)) {
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
                loadAspectNBT(data, playerName);
                loadScannedNBT(data, playerName);


                if (THAUMCRAFT_PLAYER_SHIELDING_ACCESSOR.compoundTagHasKey(data)) {
                    int shielding = THAUMCRAFT_PLAYER_SHIELDING_ACCESSOR.readFromCompoundTag(data);
                    Thaumcraft.instance.runicEventHandler.runicCharge.put(playerName, shielding);
                    Thaumcraft.instance.runicEventHandler.isDirty = true;
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
                for (Aspect aspect : Aspect.aspects.values()) {
                    if (aspect.getComponents() == null) {
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
            String key = ASPECT_KEY_ACCESSOR.readFromCompoundTag(rs);
            if (key != null) {
                completeResearchUnsaved(playerName, key);
            }
        }
    }
    public static void loadAspectNBT(CompoundTag entityData, String playerName) {

        ListTag list = THAUMCRAFT_PLAYER_ASPECTS_ACCESSOR
                .readFromCompoundTag(entityData);

        if (list == null) return;

        for (int i = 0; i < list.size(); ++i) {
            CompoundTag rs = list.getCompound(i);

            String key = ASPECT_KEY_ACCESSOR.readFromCompoundTag(rs);
            if (key == null) continue;

            Aspect aspect = Aspect.getAspect(key);
            if (aspect == null) continue;

            int amount = ASPECT_AMOUNT_ACCESSOR.readFromCompoundTag(rs);

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
                String key = ASPECT_KEY_ACCESSOR.readFromCompoundTag(rs);
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
                String key = ASPECT_KEY_ACCESSOR.readFromCompoundTag(rs);
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
                String key = ASPECT_KEY_ACCESSOR.readFromCompoundTag(rs);
                if (key != null) {
                    completeScannedPhenomenaUnsaved(playerName, key);
                }
            }
        }
    }
//    public static void loadScannedNBT(CompoundTag entityData, String playerName) {
//        NBTTagList tagList = entityData.getTagList("THAUMCRAFT.SCAN.OBJECTS", 10);
//
//        for (int j = 0; j < tagList.tagCount(); ++j) {
//            CompoundTag rs = tagList.getCompoundTagAt(j);
//            if (rs.hasKey("key")) {
//                completeScannedObjectUnsaved(playerName, rs.getString("key"));
//            }
//        }
//
//        tagList = entityData.getTagList("THAUMCRAFT.SCAN.ENTITIES", 10);
//
//        for (int j = 0; j < tagList.tagCount(); ++j) {
//            CompoundTag rs = tagList.getCompoundTagAt(j);
//            if (rs.hasKey("key")) {
//                completeScannedEntityUnsaved(playerName, rs.getString("key"));
//            }
//        }
//
//        tagList = entityData.getTagList("THAUMCRAFT.SCAN.PHENOMENA", 10);
//
//        for (int j = 0; j < tagList.tagCount(); ++j) {
//            CompoundTag rs = tagList.getCompoundTagAt(j);
//            if (rs.hasKey("key")) {
//                completeScannedPhenomenaUnsaved(playerName, rs.getString("key"));
//            }
//        }
//
//    }

    public static void scheduleSave(String playerName) {
        if (Platform.getEnvironment() != Env.SERVER){return;}
        //TODO:Impl or remove
    }

    public static boolean savePlayerData(String playerName, File file1, File file2) {
        boolean success = true;

        try {
            CompoundTag data = new CompoundTag();
            saveResearchNBT(data, playerName);
            saveAspectNBT(data, playerName);
            saveScannedNBT(data, playerName);


            // runic shielding
            if (Thaumcraft.instance.runicEventHandler.runicCharge.containsKey(playerName)) {
                THAUMCRAFT_PLAYER_SHIELDING_ACCESSOR
                        .writeToCompoundTag(data, Thaumcraft.instance.runicEventHandler.runicCharge.get(playerName));
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
        List<String> res = getResearchForPlayer(playerName);
        ListTag tagList = new ListTag();

        if (res != null && !res.isEmpty()) {
            for (String key : res) {
                if (key.startsWith("@") && isResearchComplete(playerName, key.substring(1))) {
                    continue;
                }
                ResearchItem rr = ResearchCategories.getResearch(key);
                if (rr == null || !rr.isAutoUnlock()) {
                    CompoundTag f = new CompoundTag();
                    ASPECT_KEY_ACCESSOR.writeToCompoundTag(f, key);
                    tagList.add(f);
                }
            }
        }

        THAUMCRAFT_PLAYER_RESEARCH_ACCESSOR.writeToCompoundTag(entityData, tagList);
    }
//    public static void saveResearchNBT(CompoundTag entityData, String playerName) {
//        NBTTagList tagList = new NBTTagList();
//        List res = getResearchForPlayer(playerName);
//        if (res != null && !res.isEmpty()) {
//            for (Object key : res) {
//                if (key != null && (((String) key).startsWith("@") || ResearchCategories.getResearch((String) key) != null)) {
//                    if (((String) key).startsWith("@")) {
//                        String k = ((String) key).substring(1);
//                        if (isResearchComplete(playerName, k)) {
//                            continue;
//                        }
//                    }
//
//                    if (ResearchCategories.getResearch((String) key) == null || !ResearchCategories.getResearch((String) key).isAutoUnlock()) {
//                        CompoundTag f = new CompoundTag();
//                        f.setString("key", (String) key);
//                        tagList.appendTag(f);
//                    }
//                }
//            }
//        }
//
//        entityData.setTag("THAUMCRAFT.RESEARCH", tagList);
//    }

    public static void saveAspectNBT(CompoundTag entityData, String playerName) {
        AspectList res = Thaumcraft.getKnownAspects().get(playerName);
        ListTag tagList = new ListTag();

        if (res != null && res.size() > 0) {
            for (Aspect aspect : res.getAspectTypes()) {
                if (aspect != null) {
                    CompoundTag f = new CompoundTag();
                    // 用 Accessor 写入 key 和 amount
                    ASPECT_KEY_ACCESSOR.writeToCompoundTag(f, aspect.getTag());
                    ASPECT_AMOUNT_ACCESSOR.writeToCompoundTag(f, (int) res.getAmount(aspect));
                    tagList.add(f);
                }
            }
        }

        // 用 Accessor 写入整个 ListTag
        THAUMCRAFT_PLAYER_ASPECTS_ACCESSOR.writeToCompoundTag(entityData, tagList);
    }
//    public static void saveAspectNBT(CompoundTag entityData, String playerName) {
//        NBTTagList tagList = new NBTTagList();
//        AspectList res = Thaumcraft.getKnownAspects().get(playerName);
//        if (res != null && res.size() > 0) {
//            for (Aspect aspect : res.getAspectTypes()) {
//                if (aspect != null) {
//                    CompoundTag f = new CompoundTag();
//                    f.setString("key", aspect.getTag());
//                    f.setShort("amount", (short) res.getAmount(aspect));
//                    tagList.appendTag(f);
//                }
//            }
//        }
//
//        entityData.setTag("THAUMCRAFT.ASPECTS", tagList);
//    }

    public static void saveScannedNBT(CompoundTag entityData, String playerName) {
        List<String> objects = Thaumcraft.getScannedObjects().get(playerName);
        ListTag objectTagList = new ListTag();
        if (objects != null && !objects.isEmpty()) {
            for (String obj : objects) {
                if (obj != null) {
                    CompoundTag f = new CompoundTag();
                    ASPECT_KEY_ACCESSOR.writeToCompoundTag(f, obj); // 这里 f 只需要 key
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
                    ASPECT_KEY_ACCESSOR.writeToCompoundTag(f, e);
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
                    ASPECT_KEY_ACCESSOR.writeToCompoundTag(f, p);
                    phenomenaTagList.add(f);
                }
            }
        }
        THAUMCRAFT_PLAYER_SCAN_PHENOMENA_ACCESSOR.writeToCompoundTag(entityData, phenomenaTagList);
    }
//    public static void saveScannedNBT(CompoundTag entityData, String playerName) {
//        NBTTagList tagList = new NBTTagList();
//        List<String> obj = Thaumcraft.getScannedObjects().get(playerName);
//        if (obj != null && !obj.isEmpty()) {
//            for (String object : obj) {
//                if (object != null) {
//                    CompoundTag f = new CompoundTag();
//                    f.setString("key", object);
//                    tagList.appendTag(f);
//                }
//            }
//        }
//
//        entityData.setTag("THAUMCRAFT.SCAN.OBJECTS", tagList);
//        tagList = new NBTTagList();
//        List<String> ent = Thaumcraft.getScannedEntities().get(playerName);
//        if (ent != null && !ent.isEmpty()) {
//            for (String key : ent) {
//                if (key != null) {
//                    CompoundTag f = new CompoundTag();
//                    f.setString("key", key);
//                    tagList.appendTag(f);
//                }
//            }
//        }
//
//        entityData.setTag("THAUMCRAFT.SCAN.ENTITIES", tagList);
//        tagList = new NBTTagList();
//        List<String> phe = Thaumcraft.getScannedPhenomena().get(playerName);
//        if (phe != null && !phe.isEmpty()) {
//            for (String key : phe) {
//                if (key != null) {
//                    CompoundTag f = new CompoundTag();
//                    f.setString("key", key);
//                    tagList.appendTag(f);
//                }
//            }
//        }
//
//        entityData.setTag("THAUMCRAFT.SCAN.PHENOMENA", tagList);
//    }

    public static class HexEntry {
        public Aspect aspect;
        public int type;

        public HexEntry(Aspect aspect, int type) {
            this.aspect = aspect;
            this.type = type;
        }
    }
}
