package thaumcraft.api.wands.focus.upgrade;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.aspects.aspectlists.unmodifiable.UnmodifiableAspectList;
import thaumcraft.api.wands.focus.IWandFocusItem;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.resourcelocations.FocusUpgradeTypeResourceLocation;

public class ThaumcraftFocusUpgradeTypes {
    public static final FocusUpgradeType POTENCY = new FocusUpgradeType(FocusUpgradeTypeResourceLocation.of(Thaumcraft.MOD_ID, "potency"),
            new ResourceLocation(Thaumcraft.MOD_ID, "textures/foci/potency.png"),
            "focus.upgrade.potency.name", "focus.upgrade.potency.text",
            UnmodifiableAspectList.of(Aspects.WEAPON, 1)
	);
    public static final FocusUpgradeType FRUGAL = new FocusUpgradeType(FocusUpgradeTypeResourceLocation.of(Thaumcraft.MOD_ID, "frugal"),
            new ResourceLocation(Thaumcraft.MOD_ID, "textures/foci/frugal.png"),
            "focus.upgrade.frugal.name", "focus.upgrade.frugal.text",
            UnmodifiableAspectList.of(Aspects.HUNGER, 1));
    public static final FocusUpgradeType TREASURE = new FocusUpgradeType(FocusUpgradeTypeResourceLocation.of(Thaumcraft.MOD_ID, "treasure"),
            new ResourceLocation(Thaumcraft.MOD_ID, "textures/foci/treasure.png"),
            "focus.upgrade.treasure.name", "focus.upgrade.treasure.text",
            UnmodifiableAspectList.of(Aspects.GREED, 1));
    public static final FocusUpgradeType ENLARGE = new FocusUpgradeType(FocusUpgradeTypeResourceLocation.of(Thaumcraft.MOD_ID, "enlarge"),
            new ResourceLocation(Thaumcraft.MOD_ID, "textures/foci/enlarge.png"),
            "focus.upgrade.enlarge.name", "focus.upgrade.enlarge.text",
            UnmodifiableAspectList.of(Aspects.TRAVEL, 1));
    public static final FocusUpgradeType ALCHEMISTS_FIRE = new FocusUpgradeType(FocusUpgradeTypeResourceLocation.of(Thaumcraft.MOD_ID, "alchemistsfire"),
            new ResourceLocation(Thaumcraft.MOD_ID, "textures/foci/alchemistsfire.png"),
            "focus.upgrade.alchemistsfire.name", "focus.upgrade.alchemistsfire.text",
            UnmodifiableAspectList.of(Aspects.ENERGY, 1, Aspects.SLIME, 1));
    public static final FocusUpgradeType ALCHEMISTS_FROST = new FocusUpgradeType(FocusUpgradeTypeResourceLocation.of(Thaumcraft.MOD_ID, "alchemistsfrost"),
            new ResourceLocation(Thaumcraft.MOD_ID, "textures/foci/alchemistsfrost.png"),
            "focus.upgrade.alchemistsfrost.name", "focus.upgrade.alchemistsfrost.text",
            UnmodifiableAspectList.of(Aspects.COLD, 1, Aspects.TRAP, 1));
    public static final FocusUpgradeType ARCHITECT = new FocusUpgradeType(FocusUpgradeTypeResourceLocation.of(Thaumcraft.MOD_ID, "architect"),
            new ResourceLocation(Thaumcraft.MOD_ID, "textures/foci/architect.png"),
            "focus.upgrade.architect.name", "focus.upgrade.architect.text",
            UnmodifiableAspectList.of(Aspects.CRAFT, 1));
    public static final FocusUpgradeType EXTEND = new FocusUpgradeType(FocusUpgradeTypeResourceLocation.of(Thaumcraft.MOD_ID, "extend"),
            new ResourceLocation(Thaumcraft.MOD_ID, "textures/foci/extend.png"),
            "focus.upgrade.extend.name", "focus.upgrade.extend.text",
            UnmodifiableAspectList.of(Aspects.EXCHANGE, 1));
    public static final FocusUpgradeType SILKTOUCH = new FocusUpgradeType(FocusUpgradeTypeResourceLocation.of(Thaumcraft.MOD_ID, "silktouch"),
            new ResourceLocation(Thaumcraft.MOD_ID, "textures/foci/silktouch.png"),
            "focus.upgrade.silktouch.name", "focus.upgrade.silktouch.text",
            UnmodifiableAspectList.of(Aspects.GREED, 1)
	){
        @Override
        public boolean canApplyTo(ItemStack focusStack, IWandFocusItem<? extends Aspect> focusItem) {
            return !focusItem.isUpgradedWith(focusStack,this);
        }
    };

    public static void init(){

    }
}
