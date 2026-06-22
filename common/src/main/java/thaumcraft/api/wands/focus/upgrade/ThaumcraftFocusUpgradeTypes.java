package thaumcraft.api.wands.focus.upgrade;

import net.minecraft.world.item.ItemStack;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.aspects.aspectlists.unmodifiable.UnmodifiableAspectList;
import thaumcraft.api.wands.focus.upgrade.impl.*;
import thaumcraft.api.wands.focus.upgrade.impl.excavationfocus.DowsingFocusUpgrade;
import thaumcraft.api.wands.focus.upgrade.impl.excavationfocus.SilkTouchFocusUpgrade;
import thaumcraft.api.wands.focus.upgrade.impl.excavationfocus.TreasureFocusUpgrade;
import thaumcraft.api.wands.focus.upgrade.impl.firefocus.AlchemistsFireFocusUpgrade;
import thaumcraft.api.wands.focus.upgrade.impl.firefocus.FireBeamFocusUpgrade;
import thaumcraft.api.wands.focus.upgrade.impl.firefocus.FireballFocusUpgrade;
import thaumcraft.api.wands.focus.upgrade.impl.frostfocus.AlchemistsFrostFocusUpgrade;
import thaumcraft.api.wands.focus.upgrade.impl.frostfocus.IceBoulderFocusUpgrade;
import thaumcraft.api.wands.focus.upgrade.impl.frostfocus.ScatterShotFocusUpgrade;
import thaumcraft.api.wands.focus.upgrade.impl.hellbatfocus.BatBombsFocusUpgrade;
import thaumcraft.api.wands.focus.upgrade.impl.hellbatfocus.DevilBatsFocusUpgrade;
import thaumcraft.api.wands.focus.upgrade.impl.hellbatfocus.VampireBatsFocusUpgrade;
import thaumcraft.api.wands.focus.upgrade.impl.pechfocus.NightShadeFocusUpgrade;
import thaumcraft.api.wands.focus.upgrade.impl.shockfocus.ChainLightingFocusUpgrade;
import thaumcraft.api.wands.focus.upgrade.impl.shockfocus.EarthShockFocusUpgrade;
import thaumcraft.common.items.abstracts.wandabstraction.focus.IWandFocusItem;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.resourcelocations.FocusUpgradeTypeResourceLocation;

//TODO:Make inner class into another outer class
public class ThaumcraftFocusUpgradeTypes {
    public static final PotencyFocusUpgrade POTENCY = PotencyFocusUpgrade.INSTANCE;
    public static final FrugalFocusUpgrade FRUGAL = FrugalFocusUpgrade.INSTANCE;
    public static final TreasureFocusUpgrade TREASURE = TreasureFocusUpgrade.INSTANCE;
    public static final EnlargeFocusUpgrade ENLARGE = EnlargeFocusUpgrade.INSTANCE;
    public static final ArchitectFocusUpgrade ARCHITECT = ArchitectFocusUpgrade.INSTANCE;
    public static final ExtendFocusUpgrade EXTEND = ExtendFocusUpgrade.INSTANCE;

    public static final SilkTouchFocusUpgrade SILKTOUCH = SilkTouchFocusUpgrade.INSTANCE;
    public static final DowsingFocusUpgrade DOWSING = DowsingFocusUpgrade.INSTANCE;
    //fire focus upgrades
    public static final FireballFocusUpgrade FIREBALL = FireballFocusUpgrade.INSTANCE;
    public static final AlchemistsFireFocusUpgrade ALCHEMISTS_FIRE = AlchemistsFireFocusUpgrade.INSTANCE;
    public static final FireBeamFocusUpgrade FIRE_BEAM = FireBeamFocusUpgrade.INSTANCE;
    //shock focus upgrades
    public static final ChainLightingFocusUpgrade CHAIN_LIGHTING = ChainLightingFocusUpgrade.INSTANCE;
    public static final EarthShockFocusUpgrade EARTH_SHOCK = EarthShockFocusUpgrade.INSTANCE;
    //frost
    public static final ScatterShotFocusUpgrade SCATTER_SHOT = ScatterShotFocusUpgrade.INSTANCE;;
    public static final IceBoulderFocusUpgrade ICE_BOULDER = IceBoulderFocusUpgrade.INSTANCE;

    public static final AlchemistsFrostFocusUpgrade ALCHEMISTS_FROST = AlchemistsFrostFocusUpgrade.INSTANCE;

    //hellBat
    public static final BatBombsFocusUpgrade BAT_BOMBS = BatBombsFocusUpgrade.INSTANCE;
    public static final DevilBatsFocusUpgrade DEVIL_BATS = DevilBatsFocusUpgrade.INSTANCE;
    public static final VampireBatsFocusUpgrade VAMPIRE_BATS = VampireBatsFocusUpgrade.INSTANCE;
    //pech
    public static final NightShadeFocusUpgrade NIGHT_SHADE = NightShadeFocusUpgrade.INSTANCE;

    public static void init() {

    }
}
