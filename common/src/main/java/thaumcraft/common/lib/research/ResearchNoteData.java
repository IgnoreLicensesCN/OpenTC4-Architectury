package thaumcraft.common.lib.research;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.CompoundAspect;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.resourcelocations.ResearchItemResourceLocation;
import thaumcraft.common.lib.utils.HexCoord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.linearity.opentc4.Consts.ResearchNoteCompoundTagAccessors.*;

public class ResearchNoteData {
    public ResearchItemResourceLocation key;
    public int color;
    public Map<HexCoord, HexEntry> hexGrid = new HashMap<>();
//    @Deprecated(forRemoval = true)
//    public Map<String, HexCoord> hexes = new HashMap<>();
    public boolean completed;
    public int copies;


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
        data.copies = RESEARCH_NOTE_COPIES_ACCESSOR.readFromCompoundTag(tag);
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
        RESEARCH_NOTE_COPIES_ACCESSOR.writeToCompoundTag(tag, this.copies);
        RESEARCH_NOTE_HEXGRID_ACCESSOR.writeToCompoundTag(tag, this.hexGrid);
    }

    public boolean checkResearchNoteCompletion(ItemStack contents, String username) {
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
                checkConnections(this, coord, checked, main, remains, username);
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

            this.completed = true;
            this.saveTo(contents);
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
            String username
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
                if (Thaumcraft.playerKnowledge.hasDiscoveredAspect(username, aspect1)
                        && Thaumcraft.playerKnowledge.hasDiscoveredAspect(username, aspect2)
                        && canAspectConnectToEachOther(aspect1, aspect2)) {
                    remains.add(target);
                    if (note.hexGrid.get(target)
                            .type() == HexType.GIVEN) {
                        main.remove(target);
                    }
                    checkConnections(note, target, checked, main, remains, username);
                }
            }
        }
    }
}
