package thaumcraft.api.wands.focus.upgrade;

import net.minecraft.world.item.ItemStack;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.aspects.aspectlists.unmodifiable.UnmodifiableAspectList;
import thaumcraft.api.wands.focus.IWandFocusItem;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.resourcelocations.FocusUpgradeTypeResourceLocation;

public class ThaumcraftFocusUpgradeTypes {
    public static final FocusUpgradeType POTENCY = new FocusUpgradeType(FocusUpgradeTypeResourceLocation.of(Thaumcraft.MOD_ID, "potency"),
            UnmodifiableAspectList.of(Aspects.WEAPON, 1)
	);
    public static final FocusUpgradeType FRUGAL = new FocusUpgradeType(FocusUpgradeTypeResourceLocation.of(Thaumcraft.MOD_ID, "frugal"),
            UnmodifiableAspectList.of(Aspects.HUNGER, 1));
    public static final FocusUpgradeType TREASURE = new FocusUpgradeType(FocusUpgradeTypeResourceLocation.of(Thaumcraft.MOD_ID, "treasure"),
            UnmodifiableAspectList.of(Aspects.GREED, 1));
    public static final FocusUpgradeType ENLARGE = new FocusUpgradeType(FocusUpgradeTypeResourceLocation.of(Thaumcraft.MOD_ID, "enlarge"),
            UnmodifiableAspectList.of(Aspects.TRAVEL, 1));
    public static final FocusUpgradeType ALCHEMISTS_FIRE = new FocusUpgradeType(FocusUpgradeTypeResourceLocation.of(Thaumcraft.MOD_ID, "alchemistsfire"),
            UnmodifiableAspectList.of(Aspects.ENERGY, 1, Aspects.SLIME, 1));
    public static final FocusUpgradeType ALCHEMISTS_FROST = new FocusUpgradeType(FocusUpgradeTypeResourceLocation.of(Thaumcraft.MOD_ID, "alchemistsfrost"),
            UnmodifiableAspectList.of(Aspects.COLD, 1, Aspects.TRAP, 1));
    public static final FocusUpgradeType ARCHITECT = new FocusUpgradeType(FocusUpgradeTypeResourceLocation.of(Thaumcraft.MOD_ID, "architect"),
            UnmodifiableAspectList.of(Aspects.CRAFT, 1));
    public static final FocusUpgradeType EXTEND = new FocusUpgradeType(FocusUpgradeTypeResourceLocation.of(Thaumcraft.MOD_ID, "extend"),
            UnmodifiableAspectList.of(Aspects.EXCHANGE, 1));
    public static final FocusUpgradeType SILKTOUCH = new FocusUpgradeType(FocusUpgradeTypeResourceLocation.of(Thaumcraft.MOD_ID, "silktouch"),
            UnmodifiableAspectList.of(Aspects.GREED, 1)
	){
        @Override
        public boolean canApplyTo(ItemStack focusStack, IWandFocusItem<? extends Aspect> focusItem) {
            return !focusItem.isUpgradedWith(focusStack,this);
        }
    };
    public static final FocusUpgradeType DOWSING = new FocusUpgradeType(
            FocusUpgradeTypeResourceLocation.of(Thaumcraft.MOD_ID,"dowsing"),
            UnmodifiableAspectList.of(Aspects.MINE, 1)
    );
    public static final FocusUpgradeType FIREBALL = new FocusUpgradeType(
            FocusUpgradeTypeResourceLocation.of(Thaumcraft.MOD_ID,"fireball"),
            UnmodifiableAspectList.of(Aspects.DARKNESS, 1));
    public static final FocusUpgradeType FIRE_BEAM = new FocusUpgradeType(
            FocusUpgradeTypeResourceLocation.of(Thaumcraft.MOD_ID,"fire_beam"),
            UnmodifiableAspectList.of(
                    Aspects.ENERGY, 1,
                    Aspects.AIR,1
            )
    );

    public static void init(){

    }
}
