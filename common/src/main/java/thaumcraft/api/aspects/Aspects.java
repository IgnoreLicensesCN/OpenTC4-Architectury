package thaumcraft.api.aspects;

import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.resources.ResourceLocation;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.items.ThaumcraftItems;
import thaumcraft.common.items.displayhelper.AspectItem;
import thaumcraft.common.lib.resourcelocations.AspectResourceLocation;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Aspects {
    public static final LinkedHashMap<ResourceLocation,Aspect> ALL_ASPECTS = new LinkedHashMap<>();
    public static final LinkedHashMap<ResourceLocation,PrimalAspect> PRIMAL_ASPECTS = new LinkedHashMap<>();
    public static final LinkedHashMap<ResourceLocation,CompoundAspect> COMPOUND_ASPECTS = new LinkedHashMap<>();

    //PRIMAL
    public static final PrimalAspect AIR = new PrimalAspect(new AspectResourceLocation(Thaumcraft.MOD_ID, "aer"),0xffff7e,"e",1);
    public static final PrimalAspect EARTH = new PrimalAspect(new AspectResourceLocation(Thaumcraft.MOD_ID, "terra"),0x56c000,"2",1);
    public static final PrimalAspect FIRE = new PrimalAspect(new AspectResourceLocation(Thaumcraft.MOD_ID, "ignis"),0xff5a01,"c",1);
    public static final PrimalAspect WATER = new PrimalAspect(new AspectResourceLocation(Thaumcraft.MOD_ID, "aqua"),0x3cd4fc,"3",1);
    public static final PrimalAspect ORDER = new PrimalAspect(new AspectResourceLocation(Thaumcraft.MOD_ID, "ordo"),0xd5d4ec,"7",1);
    public static final PrimalAspect ENTROPY = new PrimalAspect(new AspectResourceLocation(Thaumcraft.MOD_ID, "perditio"),0x404040,"8",771);

    //SECONDARY  	
    public static final CompoundAspect VOID = new CompoundAspect(new AspectResourceLocation(Thaumcraft.MOD_ID, "vacuos"),0x888888, new AspectComponent(AIR, ENTROPY),771);
    public static final CompoundAspect LIGHT = new CompoundAspect(new AspectResourceLocation(Thaumcraft.MOD_ID, "lux"),0xfff663, new AspectComponent(AIR, FIRE));
    public static final CompoundAspect WEATHER = new CompoundAspect(new AspectResourceLocation(Thaumcraft.MOD_ID, "tempestas"),0xFFFFFF, new AspectComponent(AIR, WATER));
    public static final CompoundAspect MOTION = new CompoundAspect(new AspectResourceLocation(Thaumcraft.MOD_ID, "motus"),0xcdccf4, new AspectComponent(AIR, ORDER));
    public static final CompoundAspect COLD = new CompoundAspect(new AspectResourceLocation(Thaumcraft.MOD_ID, "gelum"),0xe1ffff, new AspectComponent(FIRE, ENTROPY));
    public static final CompoundAspect CRYSTAL = new CompoundAspect(new AspectResourceLocation(Thaumcraft.MOD_ID, "vitreus"),0x80ffff, new AspectComponent(EARTH, ORDER));
    public static final CompoundAspect LIFE = new CompoundAspect(new AspectResourceLocation(Thaumcraft.MOD_ID, "victus"),0xde0005, new AspectComponent(WATER, EARTH));
    public static final CompoundAspect POISON = new CompoundAspect(new AspectResourceLocation(Thaumcraft.MOD_ID, "venenum"),0x89f000,  new AspectComponent(WATER, ENTROPY));
    public static final CompoundAspect ENERGY = new CompoundAspect(new AspectResourceLocation(Thaumcraft.MOD_ID, "potentia"),0xc0ffff, new AspectComponent(ORDER, FIRE));
    public static final CompoundAspect EXCHANGE = new CompoundAspect(new AspectResourceLocation(Thaumcraft.MOD_ID, "permutatio"),0x578357, new AspectComponent(ENTROPY, ORDER));
//		public static final CompoundAspect ?? = new CompoundAspect("??",0xcdccf4, new AspectComponent(AIR, EARTH));
//		public static final CompoundAspect ?? = new CompoundAspect("??",0xcdccf4, new AspectComponent(FIRE, EARTH));
//		public static final CompoundAspect ?? = new CompoundAspect("??",0xcdccf4, new AspectComponent(FIRE, WATER));
//		public static final CompoundAspect ?? = new CompoundAspect("??",0xcdccf4, new AspectComponent(ORDER, WATER));
//		public static final CompoundAspect ?? = new CompoundAspect("??",0xcdccf4, new AspectComponent(EARTH, ENTROPY));

    //TERTIARY  			
    public static final CompoundAspect METAL = new CompoundAspect(new AspectResourceLocation(Thaumcraft.MOD_ID, "metallum"),0xb5b5cd, new AspectComponent(EARTH, CRYSTAL));
    public static final CompoundAspect DEATH = new CompoundAspect(new AspectResourceLocation(Thaumcraft.MOD_ID, "mortuus"),0x887788, new AspectComponent(LIFE, ENTROPY));
    public static final CompoundAspect FLIGHT = new CompoundAspect(new AspectResourceLocation(Thaumcraft.MOD_ID, "volatus"),0xe7e7d7, new AspectComponent(AIR, MOTION));
    public static final CompoundAspect DARKNESS = new CompoundAspect(new AspectResourceLocation(Thaumcraft.MOD_ID, "tenebrae"),0x222222, new AspectComponent(VOID, LIGHT));
    public static final CompoundAspect SOUL = new CompoundAspect(new AspectResourceLocation(Thaumcraft.MOD_ID, "spiritus"),0xebebfb, new AspectComponent(LIFE, DEATH));
    public static final CompoundAspect HEAL = new CompoundAspect(new AspectResourceLocation(Thaumcraft.MOD_ID, "sano"),0xff2f34, new AspectComponent(LIFE, ORDER));
    public static final CompoundAspect TRAVEL = new CompoundAspect(new AspectResourceLocation(Thaumcraft.MOD_ID, "iter"),0xe0585b, new AspectComponent(MOTION, EARTH));
    public static final CompoundAspect ELDRITCH = new CompoundAspect(new AspectResourceLocation(Thaumcraft.MOD_ID, "alienis"),0x805080, new AspectComponent(VOID, DARKNESS));
    public static final CompoundAspect MAGIC = new CompoundAspect(new AspectResourceLocation(Thaumcraft.MOD_ID, "praecantatio"),0x9700c0, new AspectComponent(VOID, ENERGY));
    public static final CompoundAspect AURA = new CompoundAspect(new AspectResourceLocation(Thaumcraft.MOD_ID, "auram"),0xffc0ff, new AspectComponent(MAGIC, AIR));
    public static final CompoundAspect TAINT = new CompoundAspect(new AspectResourceLocation(Thaumcraft.MOD_ID, "vitium"),0x800080, new AspectComponent(MAGIC, ENTROPY));
    public static final CompoundAspect SLIME = new CompoundAspect(new AspectResourceLocation(Thaumcraft.MOD_ID, "limus"),0x01f800, new AspectComponent(LIFE, WATER));
    public static final CompoundAspect PLANT = new CompoundAspect(new AspectResourceLocation(Thaumcraft.MOD_ID, "herba"),0x01ac00, new AspectComponent(LIFE, EARTH));
    public static final CompoundAspect TREE = new CompoundAspect(new AspectResourceLocation(Thaumcraft.MOD_ID, "arbor"),0x876531, new AspectComponent(AIR, PLANT));
    public static final CompoundAspect BEAST = new CompoundAspect(new AspectResourceLocation(Thaumcraft.MOD_ID, "bestia"),0x9f6409, new AspectComponent(MOTION, LIFE));
    public static final CompoundAspect FLESH = new CompoundAspect(new AspectResourceLocation(Thaumcraft.MOD_ID, "corpus"),0xee478d, new AspectComponent(DEATH, BEAST));
    public static final CompoundAspect UNDEAD = new CompoundAspect(new AspectResourceLocation(Thaumcraft.MOD_ID, "exanimis"),0x3a4000, new AspectComponent(MOTION, DEATH));
    public static final CompoundAspect MIND = new CompoundAspect(new AspectResourceLocation(Thaumcraft.MOD_ID, "cognitio"),0xffc2b3, new AspectComponent(FIRE, SOUL));
    public static final CompoundAspect SENSES = new CompoundAspect(new AspectResourceLocation(Thaumcraft.MOD_ID, "sensus"),0x0fd9ff, new AspectComponent(AIR, SOUL));
    public static final CompoundAspect MAN = new CompoundAspect(new AspectResourceLocation(Thaumcraft.MOD_ID, "humanus"),0xffd7c0, new AspectComponent(BEAST, MIND));
    public static final CompoundAspect CROP = new CompoundAspect(new AspectResourceLocation(Thaumcraft.MOD_ID, "messis"),0xe1b371, new AspectComponent(PLANT, MAN));
    public static final CompoundAspect MINE = new CompoundAspect(new AspectResourceLocation(Thaumcraft.MOD_ID, "perfodio"),0xdcd2d8, new AspectComponent(MAN, EARTH));
    public static final CompoundAspect TOOL = new CompoundAspect(new AspectResourceLocation(Thaumcraft.MOD_ID, "instrumentum"),0x4040ee, new AspectComponent(MAN, ORDER));
    public static final CompoundAspect HARVEST = new CompoundAspect(new AspectResourceLocation(Thaumcraft.MOD_ID, "meto"),0xeead82, new AspectComponent(CROP, TOOL));
    public static final CompoundAspect WEAPON = new CompoundAspect(new AspectResourceLocation(Thaumcraft.MOD_ID, "telum"),0xc05050, new AspectComponent(TOOL, FIRE));
    public static final CompoundAspect ARMOR = new CompoundAspect(new AspectResourceLocation(Thaumcraft.MOD_ID, "tutamen"),0x00c0c0, new AspectComponent(TOOL, EARTH));
    public static final CompoundAspect HUNGER = new CompoundAspect(new AspectResourceLocation(Thaumcraft.MOD_ID, "fames"),0x9a0305, new AspectComponent(LIFE, VOID));
    public static final CompoundAspect GREED = new CompoundAspect(new AspectResourceLocation(Thaumcraft.MOD_ID, "lucrum"),0xe6be44, new AspectComponent(MAN, HUNGER));
    public static final CompoundAspect CRAFT = new CompoundAspect(new AspectResourceLocation(Thaumcraft.MOD_ID, "fabrico"),0x809d80, new AspectComponent(MAN, TOOL));
    public static final CompoundAspect CLOTH = new CompoundAspect(new AspectResourceLocation(Thaumcraft.MOD_ID, "pannus"),0xeaeac2, new AspectComponent(TOOL, BEAST));
    public static final CompoundAspect MECHANISM = new CompoundAspect(new AspectResourceLocation(Thaumcraft.MOD_ID, "machina"),0x8080a0, new AspectComponent(MOTION, TOOL));
    public static final CompoundAspect TRAP = new CompoundAspect(new AspectResourceLocation(Thaumcraft.MOD_ID, "vinculum"),0x9a8080, new AspectComponent(MOTION, ENTROPY));
    public static final Aspect EMPTY = new Aspect(
            new AspectResourceLocation(Thaumcraft.MOD_ID,""),
            0x000000,
            new ResourceLocation(Thaumcraft.MOD_ID,"textures/aspects/empty.png"),
            1,
            true) {
        @Override
        public boolean isEmpty() {
            return true;
        }
    };

    public static final Map<ResourceLocation, AspectItem> ASPECT_RESOURCE_LOCATION_TO_ITEM_MAP = new HashMap<>();
    public static final Map<ResourceLocation, RegistrySupplier<AspectItem>> ASPECT_RESOURCE_LOCATION_TO_ITEM_SUPPLIER_MAP = new HashMap<>();
    public static void init(){
        //you can surly call it many times.
        for (var aspectResAndInstanceLoc:ALL_ASPECTS.entrySet()){
            var aspectResLoc = aspectResAndInstanceLoc.getKey();
            var aspectInstance = aspectResAndInstanceLoc.getValue();
            ASPECT_RESOURCE_LOCATION_TO_ITEM_MAP.computeIfAbsent(aspectResLoc,
                    aspResLocation -> {
                var supplier = ASPECT_RESOURCE_LOCATION_TO_ITEM_SUPPLIER_MAP.computeIfAbsent(
                        aspResLocation, aspResLocationToCompute -> ThaumcraftItems.Registry.ITEMS.register(
                                aspResLocationToCompute,() -> new AspectItem(aspectInstance)
                        )
                );
                return supplier.get();
                    }
            );
        }
    }
}
