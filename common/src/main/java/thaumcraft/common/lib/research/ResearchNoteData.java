package thaumcraft.common.lib.research;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.aspects.CompoundAspect;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.api.research.interfaces.IResearchNoteCreatable;
import thaumcraft.api.research.interfaces.IThemedAspectOwner;
import thaumcraft.common.items.ThaumcraftItems;
import thaumcraft.common.lib.resourcelocations.ResearchItemResourceLocation;
import thaumcraft.common.lib.utils.HexCoord;
import thaumcraft.common.lib.utils.HexCoordUtils;

import java.util.*;

import static com.linearity.opentc4.Consts.ResearchNoteCompoundTagAccessors.*;

public class ResearchNoteData {
    public ResearchItemResourceLocation key;
    public int color;
    public Map<HexCoord, HexEntry> hexGrid = new HashMap<>();
//    @Deprecated(forRemoval = true)
//    public Map<String, HexCoord> hexes = new HashMap<>();
    public boolean completed;
    public int copiedCount;

    public static ItemStack createResearchNote(
            RandomSource randomSource,
            ResearchItem researchItem
    ){
        if (!(researchItem instanceof IResearchNoteCreatable noteCreatable)) {
            throw new IllegalArgumentException("research cannot create ResearchNote:"+researchItem);
        }
        var stack = ThaumcraftItems.RESEARCH_NOTE.getDefaultInstance();
        var researchKey = researchItem.key;
        Aspect researchThemedAspect = Aspects.EMPTY;
        if (researchItem instanceof IThemedAspectOwner themedAspectOwner){
            researchThemedAspect = themedAspectOwner.getResearchThemedAspect();
        }

        CompoundTag tag = stack.getOrCreateTag();
        RESEARCH_NOTE_RESEARCH_ACCESSOR.writeToCompoundTag(tag, researchKey);
        RESEARCH_NOTE_COLOR_ACCESSOR.writeToCompoundTag(tag, researchThemedAspect.getColor());
        RESEARCH_NOTE_COMPLETE_ACCESSOR.writeToCompoundTag(tag, false);
        RESEARCH_NOTE_COPIES_ACCESSOR.writeToCompoundTag(tag, 0);

        int radius = 1 + noteCreatable.getComplexity();
        var hexLocs = HexCoordUtils.generateHexGridWithRadius(radius);
        List<HexCoord> outerRing = HexCoordUtils.distributeRingRandomly(radius, noteCreatable.getResearchGivenAspects().size(), randomSource);

        int placedAspectCount = 0;

        for (HexCoord hex : outerRing) {
            hexLocs.put(hex,new HexEntry(noteCreatable.getResearchGivenAspects().getAspectTypes().stream().toList().get(placedAspectCount), HexType.GIVEN));
            ++placedAspectCount;
        }

        if (noteCreatable.getComplexity() > 1) {
            removeRandomBlanks(randomSource, researchItem, hexLocs);
        }

        //got angry with some research with some node blocked and whole note can't be finished
        ensureEndpointsConnected(hexLocs);

        RESEARCH_NOTE_HEXGRID_ACCESSOR.writeToCompoundTag(tag, hexLocs);

        return stack;
    }

    private static void removeRandomBlanks(
            RandomSource random,
            ResearchItem rr,
            Map<HexCoord, HexEntry> hexGird
    ) {
        if (!(rr instanceof IResearchNoteCreatable noteCreatable)){
            throw new IllegalArgumentException("research cannot create ResearchNote:"+rr);
        }
        int blanks = noteCreatable.getComplexity() * 2;
        var temp = hexGird.keySet().toArray(new HexCoord[0]);

        while (blanks > 0) {
            int pickIndex = random.nextInt(temp.length);
            var pickEntry = hexGird.get(temp[pickIndex]);
            if (pickEntry != null
                    && pickEntry.type() == HexType.NONE
            ) {
                boolean gtg = true;

                for (int n = 0; n < 6; ++n) {
                    HexCoord neighbour = temp[pickIndex].getNeighbour(n);
                    var neighborEntry = hexGird.get(neighbour);
                    if (neighborEntry != null
                            && neighborEntry.type() == HexType.WRITTEN
                    ) {
                        int cc = 0;

                        for (int q = 0; q < 6; ++q) {

                            if (hexGird.containsKey(
                                    neighbour.getNeighbour(q)
                            )
                            ) {
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
                    hexGird.remove(temp[pickIndex]);
                    temp = hexGird.keySet().toArray(new HexCoord[0]);
                    --blanks;
                }
            }
        }
    }

    private static void ensureEndpointsConnected(
            Map<HexCoord, HexEntry> hexGrid
    ) {
        // 找出端点 hex
        List<HexCoord> endpoints = hexGrid.entrySet().stream()
                .filter(e -> e.getValue().type() == HexType.GIVEN)
                .map(Map.Entry::getKey)
                .filter(Objects::nonNull)
                .toList();

        if (endpoints.size() < 2) return;

        // BFS helper
        Set<HexCoord> visited = new HashSet<>();
        for (int i = 0; i < endpoints.size() - 1; i++) {
            HexCoord start = endpoints.get(i);
            HexCoord end = endpoints.get(i + 1);

            if (!isConnected(start, end, hexGrid, visited)) {
                // 找最短路径恢复
                List<HexCoord> path = findPathAllowRemoved(start, end, hexGrid);
                for (HexCoord key : path) {
                    if (!hexGrid.containsKey(key)) {
                        hexGrid.put(key, HexEntry.EMPTY);
                    }
                }
            }
        }
    }

    // 检查两个端点是否连通
    //author:ChatGPT
    private static boolean isConnected(
            HexCoord start,
            HexCoord end,
            Map<HexCoord, HexEntry> hexGrid,
            Set<HexCoord> visited) {
        visited.clear();
        Queue<HexCoord> queue = new ArrayDeque<>();
        queue.add(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            HexCoord current = queue.poll();
            if (current.equals(end)) return true;

            for (int i = 0; i < 6; i++) {
                HexCoord key = current.getNeighbour(i);
                if (!visited.contains(key) && hexGrid.containsKey(key)) {
                    queue.add(key);
                    visited.add(key);
                }
            }
        }
        return false;
    }

    // 找到 start -> end 的路径，允许穿过被移除 hex
    //author:ChatGPT
    private static List<HexCoord> findPathAllowRemoved(
            HexCoord start, HexCoord end,
            Map<HexCoord, HexEntry> hexGrid
    ) {
        Map<HexCoord, HexCoord> cameFromMap = new HashMap<>();
        Queue<HexCoord> queue = new ArrayDeque<>();
        queue.add(start);
        cameFromMap.put(start, null);

        while (!queue.isEmpty()) {
            HexCoord current = queue.poll();
            if (current.equals(end)) break;

            for (int i = 0; i < 6; i++) {
                HexCoord key = current.getNeighbour(i);
                if (!cameFromMap.containsKey(key)) {
                    queue.add(key);
                    cameFromMap.put(key, current);
                }
            }
        }

        // 回溯路径
        List<HexCoord> path = new ArrayList<>();
        HexCoord cursor = end;
        while (cursor != null && !cursor.equals(start)) {
            path.add(cursor);
            cursor = cameFromMap.get(cursor);
        }
        Collections.reverse(path);
        return path;
    }


    public boolean isCompleted() {
        return this.completed;
    }


    public static ResearchNoteData readFrom(ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            return null;
        }
        CompoundTag tag = stack.getOrCreateTag();
        ResearchNoteData data = new ResearchNoteData();
        data.key = RESEARCH_NOTE_RESEARCH_ACCESSOR.readFromCompoundTag(tag);
        data.color = RESEARCH_NOTE_COLOR_ACCESSOR.readFromCompoundTag(tag);
        data.completed = RESEARCH_NOTE_COMPLETE_ACCESSOR.readFromCompoundTag(tag);
        data.copiedCount = RESEARCH_NOTE_COPIES_ACCESSOR.readFromCompoundTag(tag);
        data.hexGrid = RESEARCH_NOTE_HEXGRID_ACCESSOR.readFromCompoundTag(tag);
        stack.setTag(tag);
        return data;
    }

    public void saveTo(@NotNull ItemStack stack) {
        if (stack.isEmpty()) return;
        CompoundTag tag = stack.getOrCreateTag();
        RESEARCH_NOTE_RESEARCH_ACCESSOR.writeToCompoundTag(tag, this.key);
        RESEARCH_NOTE_COLOR_ACCESSOR.writeToCompoundTag(tag, this.color);
        RESEARCH_NOTE_COMPLETE_ACCESSOR.writeToCompoundTag(tag, this.completed);
        RESEARCH_NOTE_COPIES_ACCESSOR.writeToCompoundTag(tag, this.copiedCount);
        RESEARCH_NOTE_HEXGRID_ACCESSOR.writeToCompoundTag(tag, this.hexGrid);
    }

    public boolean checkResearchNoteCompletion(Player player) {
        List<HexCoord> checked = new ArrayList<>();
        List<HexCoord> main = new ArrayList<>();
        List<HexCoord> remains = new ArrayList<>();

        for (var hexPair : this.hexGrid.entrySet()) {
            var coord = hexPair.getKey();
            var entry = hexPair.getValue();
            if (entry.type() == HexType.GIVEN) {
                main.add(coord);
            }
        }

        for (var hexPair : this.hexGrid.entrySet()) {
            var coord = hexPair.getKey();
            var entry = hexPair.getValue();
            if (entry.type() == HexType.GIVEN) {
                main.remove(coord);
                checkConnections(this, coord, checked, main, remains, player);
                break;
            }
        }

        if (!main.isEmpty()) {
            return false;
        } else {
            List<HexCoord> remove = new ArrayList<>();

            for (var hexPair : this.hexGrid.entrySet()) {
                var hexCoord = hexPair.getKey();
                var entry = hexPair.getValue();
                if (entry.type() != HexType.GIVEN && !remains.contains(hexCoord)) {
                    remove.add(hexCoord);
                }
            }

            for (var s : remove) {
                this.hexGrid.remove(s);
            }

            return true;
        }
    }

    private static boolean canAspectConnectToEachOther(Aspect aspectA, Aspect aspectB) {
        if (aspectA instanceof CompoundAspect compoundAspectA) {
            if (compoundAspectA.components.aspectA() == aspectB) {
                return true;
            }
            if (compoundAspectA.components.aspectB() == aspectB) {
                return true;
            }
        }
        if (aspectB instanceof CompoundAspect compoundAspectB) {
            if (compoundAspectB.components.aspectA() == aspectA) {
                return true;
            }
            return compoundAspectB.components.aspectB() == aspectA;
        }
        return false;

    }

    private static void checkConnections(
            ResearchNoteData note,
            HexCoord hex,
            List<HexCoord> checked,
            List<HexCoord> main,
            List<HexCoord> remains,
            Player player
    ) {
        checked.add(hex);

        for (int a = 0; a < 6; ++a) {
            HexCoord target = hex.getNeighbour(a);
            var targetType = note.hexGrid.get(target)
                    .type();
            if (!checked.contains(target)
                    && note.hexGrid.containsKey(target)
                    && (targetType == HexType.GIVEN || targetType == HexType.WRITTEN)
            ) {
                Aspect aspect1 = note.hexGrid.get(hex)
                        .aspect();
                Aspect aspect2 = note.hexGrid.get(target)
                        .aspect();
                if (aspect1.hasPlayerDiscovered(player)
                        && aspect2.hasPlayerDiscovered(player)
                        && canAspectConnectToEachOther(aspect1, aspect2)) {
                    remains.add(target);
                    if (note.hexGrid.get(target)
                            .type() == HexType.GIVEN) {
                        main.remove(target);
                    }
                    checkConnections(note, target, checked, main, remains, player);
                }
            }
        }
    }
}
