package thaumcraft.common.items.misc;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.expands.listeners.researchtable.RemoveAspectContext;
import thaumcraft.api.expands.listeners.researchtable.WriteAspectContext;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.api.researchtable.IResearchNoteDataOwner;
import thaumcraft.common.items.ThaumcraftItems;
import thaumcraft.common.lib.research.HexEntry;
import thaumcraft.common.lib.research.HexType;
import thaumcraft.common.lib.research.ResearchNoteData;
import thaumcraft.common.lib.utils.HexCoord;
import thaumcraft.common.lib.utils.HexCoordUtils;

import java.util.*;

import static com.linearity.opentc4.Consts.ResearchNoteCompoundTagAccessors.*;

public class ResearchNoteItem extends Item implements IResearchNoteDataOwner {
    public ResearchNoteItem(Properties props) {
        super(props);
    }
    public ResearchNoteItem() {
        super(new Properties().stacksTo(1));
    }

    @Override
    public boolean onWriteAspect(WriteAspectContext context) {
        if (context.aspectToWrite.isEmpty()) {return false;}
        var existing = context.noteData.hexGrid.get(context.coordToWrite);
        if (existing == null) {return false;}
        if (existing.type() == HexType.GIVEN){
            return false;
        }
        if (existing.type() == HexType.WRITTEN && !existing.aspect().isEmpty()){
            return false;
        }
        context.noteData.hexGrid.put(context.coordToWrite,new HexEntry(context.aspectToWrite, HexType.WRITTEN));
        return true;
    }

    @Override
    public boolean onRemoveAspect(RemoveAspectContext context) {
        if (context.aspectToRemove.isEmpty()) {return false;}
        var existing = context.noteData.hexGrid.get(context.coordToRemove);
        if (existing.type() == HexType.GIVEN || existing.type() == HexType.NONE){
            return false;
        }
        if (existing.type() == HexType.WRITTEN){
            if (existing.aspect().isEmpty()){
                return false;
            }
        }

        context.noteData.hexGrid.put(context.coordToRemove,HexEntry.EMPTY);
        updateResearchNoteData(context.noteStack,context.noteData);
        return true;
    }

    @Override
    public @Nullable ResearchNoteData getResearchNoteData(ItemStack stack) {

        if (stack == null || stack.isEmpty()) {
            return null;
        }
        CompoundTag tag = stack.getOrCreateTag();
        ResearchNoteData data = new ResearchNoteData();
        data.key = RESEARCH_NOTE_RESEARCH_ACCESSOR.readFromCompoundTag(tag);
        data.color = RESEARCH_NOTE_COLOR_ACCESSOR.readFromCompoundTag(tag);
        data.completed = RESEARCH_NOTE_COMPLETE_ACCESSOR.readFromCompoundTag(tag);
        data.copies = RESEARCH_NOTE_COPIES_ACCESSOR.readFromCompoundTag(tag);
        data.hexGrid = RESEARCH_NOTE_HEXGRID_ACCESSOR.readFromCompoundTag(tag);
        stack.setTag(tag);
        return data;
    }

    public void updateResearchNoteData(ItemStack stack, ResearchNoteData data) {
        if (stack == null || stack.isEmpty() || data == null) return;

        CompoundTag tag = stack.getOrCreateTag();
        RESEARCH_NOTE_RESEARCH_ACCESSOR.writeToCompoundTag(tag, data.key);
        RESEARCH_NOTE_COLOR_ACCESSOR.writeToCompoundTag(tag, data.color);
        RESEARCH_NOTE_COMPLETE_ACCESSOR.writeToCompoundTag(tag, data.completed);
        RESEARCH_NOTE_COPIES_ACCESSOR.writeToCompoundTag(tag, data.copies);
        RESEARCH_NOTE_HEXGRID_ACCESSOR.writeToCompoundTag(tag, data.hexGrid);
    }
    @Override
    public boolean canWriteAspect(Level atLevel, BlockPos researchTableBEPos, ItemStack writeToolStack, ItemStack researchNoteStack, Player player, Aspect aspect, HexCoord placedAt) {
        var data = getResearchNoteData(researchNoteStack);
        if (data == null) {return false;}
        return !data.completed;
    }

    @Override
    public void beforeWriteAspect(WriteAspectContext context) {

    }

    @Override
    public void afterWriteAspect(WriteAspectContext context) {

    }

    @Override
    public void beforeRemoveAspect(RemoveAspectContext context) {

    }

    @Override
    public void afterRemoveAspect(RemoveAspectContext context) {

    }

    public static ItemStack createResearchNote(
            RandomSource randomSource,
            ResearchItem researchItem
    ){
        var stack = ThaumcraftItems.RESEARCH_NOTE.getDefaultInstance();
        var researchKey = researchItem.key;
        Aspect researchThemedAspect = researchItem.getResearchThemedAspect();
        if (researchThemedAspect.isEmpty()) {
            return ItemStack.EMPTY;
        }

        CompoundTag tag = stack.getOrCreateTag();

        RESEARCH_NOTE_RESEARCH_ACCESSOR.writeToCompoundTag(tag, researchKey);
        RESEARCH_NOTE_COLOR_ACCESSOR.writeToCompoundTag(tag, researchThemedAspect.getColor());
        RESEARCH_NOTE_COMPLETE_ACCESSOR.writeToCompoundTag(tag, false);
        RESEARCH_NOTE_COPIES_ACCESSOR.writeToCompoundTag(tag, 0);

        int radius = 1 + Math.min(3, researchItem.getComplexity());
        var hexLocs = HexCoordUtils.generateHexGridWithRadius(radius);
        List<HexCoord> outerRing = HexCoordUtils.distributeRingRandomly(radius, researchItem.tags.size(), randomSource);

        int count = 0;

        for (HexCoord hex : outerRing) {
            hexLocs.put(hex,new HexEntry(researchItem.tags.getAspectTypes().get(count), HexType.GIVEN));
            ++count;
        }

        if (researchItem.getComplexity() > 1) {
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
        int blanks = rr.getComplexity() * 2;
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
}
