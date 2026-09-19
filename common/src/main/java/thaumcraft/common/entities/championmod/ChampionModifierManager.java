package thaumcraft.common.entities.championmod;

import net.minecraft.world.entity.LivingEntity;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.entities.championmod.impl.*;
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
    public static final SpineChampionModifier SPINE_CHAMPION_MODIFIER = new SpineChampionModifier(
            UUID.fromString("365eead5-3f15-42a8-9e68-36100faef945"),
            ChampionModifierResourceLocation.of(Thaumcraft.MOD_ID, "spine")
    );//1
    public static final ArmoredChampionModifier ARMORED_CHAMPION_MODIFIER = new ArmoredChampionModifier(
            UUID.fromString("4e23758d-348e-42a8-8de6-08ae0a59033c"),
            ChampionModifierResourceLocation.of(Thaumcraft.MOD_ID, "armored")
    );//2
    public static final MightyChampionModifier MIGHTY_CHAMPION_MODIFIER = new MightyChampionModifier(
            UUID.fromString("6d2ffe79-f034-4a06-b288-e1916c21e385"),
            ChampionModifierResourceLocation.of(Thaumcraft.MOD_ID, "mighty")
    );//3
    public static final GrimChampionModifier GRIM_CHAMPION_MODIFIER = new GrimChampionModifier(
            UUID.fromString("0f23321e-f921-4246-90b8-21ef202de224"),
            ChampionModifierResourceLocation.of(Thaumcraft.MOD_ID, "grim")
    );//4
    public static final WardedChampionModifier WARDED_CHAMPION_MODIFIER = new WardedChampionModifier(
            UUID.fromString("b622c4d8-abc6-4db7-b3ee-5cf71b8e5286"),
            ChampionModifierResourceLocation.of(Thaumcraft.MOD_ID, "warded")
    );//5
    public static final WarpChampionModifier WARP_CHAMPION_MODIFIER = new WarpChampionModifier(
            UUID.fromString("107da049-af7a-4409-989a-6de23c8fe036"),
            ChampionModifierResourceLocation.of(Thaumcraft.MOD_ID, "warp")
    );//6
    public static final UndyingChampionModifier UNDYING_CHAMPION_MODIFIER = new UndyingChampionModifier(
            UUID.fromString("cb9484d3-6255-4893-a4f2-3ecc375692ee"),
            ChampionModifierResourceLocation.of(Thaumcraft.MOD_ID, "undying")
    );//7
    public static final FieryChampionModifier FIERY_CHAMPION_MODIFIER = new FieryChampionModifier(
            UUID.fromString("6b567fdf-9245-48f5-8314-f93fe5db1427"),
            ChampionModifierResourceLocation.of(Thaumcraft.MOD_ID, "fiery")
    );//8
    public static final SicklyChampionModifier SICKLY_CHAMPION_MODIFIER = new SicklyChampionModifier(
            UUID.fromString("b5718868-9ab0-424c-af1f-8b35e836b46e"),
            ChampionModifierResourceLocation.of(Thaumcraft.MOD_ID, "sickly")
    );//9
    public static final VenomousChampionModifier VENOMOUS_CHAMPION_MODIFIER = new VenomousChampionModifier(
            UUID.fromString("ab9a132e-c619-4c0a-a103-10cbbcfba1a2"),
            ChampionModifierResourceLocation.of(Thaumcraft.MOD_ID, "venomous")
    );//10
    public static final VampiricChampionModifier VAMPIRIC_CHAMPION_MODIFIER = new VampiricChampionModifier(
            UUID.fromString("3412251e-af81-4c3c-93ba-2e1c33b049ea"),
            ChampionModifierResourceLocation.of(Thaumcraft.MOD_ID, "vampiric")
    );//11
    public static final InfestedChampionModifier INFESTED_CHAMPION_MODIFIER = new InfestedChampionModifier(
            UUID.fromString("9c577fbe-ddbc-4ea2-a661-770ea775f43b"),
            ChampionModifierResourceLocation.of(Thaumcraft.MOD_ID, "infested")
    );

    public static void init() {
        registerModifiers();
        addModifiersForRandomPickup();
    }

    private static void registerModifiers() {
        registerModifier(BOLD_CHAMPION_MODIFIER);
        registerModifier(SPINE_CHAMPION_MODIFIER);
        registerModifier(ARMORED_CHAMPION_MODIFIER);
        registerModifier(MIGHTY_CHAMPION_MODIFIER);
        registerModifier(GRIM_CHAMPION_MODIFIER);
        registerModifier(WARDED_CHAMPION_MODIFIER);
        registerModifier(WARP_CHAMPION_MODIFIER);
        registerModifier(UNDYING_CHAMPION_MODIFIER);
        registerModifier(FIERY_CHAMPION_MODIFIER);
        registerModifier(SICKLY_CHAMPION_MODIFIER);
        registerModifier(VENOMOUS_CHAMPION_MODIFIER);
        registerModifier(VAMPIRIC_CHAMPION_MODIFIER);
        registerModifier(INFESTED_CHAMPION_MODIFIER);
    }

    private static void addModifiersForRandomPickup() {
        CHAMPION_MODIFIERS_FOR_RANDOM.add(BOLD_CHAMPION_MODIFIER);
        CHAMPION_MODIFIERS_FOR_RANDOM.add(SPINE_CHAMPION_MODIFIER);
        CHAMPION_MODIFIERS_FOR_RANDOM.add(ARMORED_CHAMPION_MODIFIER);
        CHAMPION_MODIFIERS_FOR_RANDOM.add(MIGHTY_CHAMPION_MODIFIER);
        CHAMPION_MODIFIERS_FOR_RANDOM.add(GRIM_CHAMPION_MODIFIER);
        CHAMPION_MODIFIERS_FOR_RANDOM.add(WARDED_CHAMPION_MODIFIER);
        CHAMPION_MODIFIERS_FOR_RANDOM.add(WARP_CHAMPION_MODIFIER);
        CHAMPION_MODIFIERS_FOR_RANDOM.add(UNDYING_CHAMPION_MODIFIER);
        CHAMPION_MODIFIERS_FOR_RANDOM.add(FIERY_CHAMPION_MODIFIER);
        CHAMPION_MODIFIERS_FOR_RANDOM.add(SICKLY_CHAMPION_MODIFIER);
        CHAMPION_MODIFIERS_FOR_RANDOM.add(VENOMOUS_CHAMPION_MODIFIER);
        CHAMPION_MODIFIERS_FOR_RANDOM.add(VAMPIRIC_CHAMPION_MODIFIER);
        CHAMPION_MODIFIERS_FOR_RANDOM.add(INFESTED_CHAMPION_MODIFIER);
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
