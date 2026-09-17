package thaumcraft.common.entities.championmod;

import net.minecraft.world.entity.LivingEntity;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.entities.championmod.impl.ArmoredChampionModifier;
import thaumcraft.common.entities.championmod.impl.BoldChampionModifier;
import thaumcraft.common.entities.championmod.impl.MightyChampionModifier;
import thaumcraft.common.entities.championmod.impl.WardedChampionModifier;
import thaumcraft.common.lib.resourcelocations.ChampionModifierResourceLocation;

import java.util.*;
import java.util.function.Consumer;

import static com.linearity.opentc4.OpenTC4.throwDuplicate;
import static thaumcraft.common.lib.utils.EntityUtils.ThaumcraftAttributeCategoryInstances.CHAMPION_MOD;

public class ChampionModifierManager {
    public static final Map<UUID, ChampionModifier> REGISTERED_MODIFIERS = new HashMap<>();
    public static final List<ChampionModifier> CHAMPION_MODIFIERS_FOR_RANDOM = new ArrayList<>();
    public static final Set<UUID> MODIFIER_UUIDS = new HashSet<>();
    public static final Set<ChampionModifierResourceLocation> MODIFIER_IDS = new HashSet<>();

    public static final BoldChampionModifier BOLD_CHAMPION_MODIFIER = new BoldChampionModifier(
            UUID.fromString("40289aa1-907f-4ac6-ad79-e6681efe2cbc"),
            ChampionModifierResourceLocation.of(Thaumcraft.MOD_ID, "bold")
    );//0

    public static final ArmoredChampionModifier ARMORED_CHAMPION_MODIFIER = new ArmoredChampionModifier(
            UUID.fromString("4e23758d-348e-42a8-8de6-08ae0a59033c"),
            ChampionModifierResourceLocation.of(Thaumcraft.MOD_ID, "armored")
    );//2
    public static final MightyChampionModifier MIGHTY_CHAMPION_MODIFIER = new MightyChampionModifier(
            UUID.fromString("6d2ffe79-f034-4a06-b288-e1916c21e385"),
            ChampionModifierResourceLocation.of(Thaumcraft.MOD_ID, "mighty")
    );//3

    public static final WardedChampionModifier WARDED_CHAMPION_MODIFIER = new WardedChampionModifier(
            UUID.fromString("b622c4d8-abc6-4db7-b3ee-5cf71b8e5286"),
            ChampionModifierResourceLocation.of(Thaumcraft.MOD_ID, "warded")
    );//5

    public static void init() {
        registerModifier(BOLD_CHAMPION_MODIFIER);

        registerModifier(ARMORED_CHAMPION_MODIFIER);
        registerModifier(MIGHTY_CHAMPION_MODIFIER);

        registerModifier(WARDED_CHAMPION_MODIFIER);
    }

    public static void registerModifier(ChampionModifier modifier) {
        if (MODIFIER_UUIDS.contains(modifier.uuid) || MODIFIER_IDS.contains(modifier.id)) {
            if (throwDuplicate) {
                throw new RuntimeException("Duplicate key found in modifier registry");
            }
        }
        MODIFIER_UUIDS.add(modifier.uuid);
        MODIFIER_IDS.add(modifier.id);
        REGISTERED_MODIFIERS.put(modifier.uuid, modifier);
        CHAMPION_MODIFIERS_FOR_RANDOM.add(modifier);
    }

    public static void forEachChampionModifierOnEntity(LivingEntity living, Consumer<ChampionModifier> consumer) {
        var attributeInstance = living.getAttribute(CHAMPION_MOD());
        if (attributeInstance != null) {
            for (var attributeModifier : attributeInstance.getModifiers()) {
                var modifier = REGISTERED_MODIFIERS.get(attributeModifier.getId());
                if (modifier != null) {
                    consumer.accept(modifier);
                }
            }
        }
    }

}
