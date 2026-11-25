package thaumcraft.common.runicshield;

import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import org.jetbrains.annotations.ApiStatus;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.resourcelocations.RunicShieldTypeResourceLocation;
import thaumcraft.common.runicshield.shieldtypes.*;

import java.util.Collections;
import java.util.Map;

public class ThaumcraftRunicShieldTypes {
    public static final CommonRunicShield COMMON = new CommonRunicShield(RunicShieldTypeResourceLocation.of(Thaumcraft.MOD_ID,"common"),0);
    public static final ChargedRunicShield CHARGED = new ChargedRunicShield(RunicShieldTypeResourceLocation.of(Thaumcraft.MOD_ID,"charged"),100);
    public static final HealingRunicShield HEALING = new HealingRunicShield(RunicShieldTypeResourceLocation.of(Thaumcraft.MOD_ID,"healing"),200);
    public static final EmergencyRunicShield EMERGENCY = new EmergencyRunicShield(RunicShieldTypeResourceLocation.of(Thaumcraft.MOD_ID,"emergency"),300);
    public static final KineticRunicShield KINETIC = new KineticRunicShield(RunicShieldTypeResourceLocation.of(Thaumcraft.MOD_ID,"kinetic"),400);
    public static void init(){

    }
    @ApiStatus.Internal
    public static final Map<RunicShieldTypeResourceLocation,AbstractRunicShieldType<?>> RUNIC_SHIELD_TYPES = Maps.newHashMap();
    public static final Map<RunicShieldTypeResourceLocation,AbstractRunicShieldType<?>> RUNIC_SHIELD_TYPES_VIEW = Collections.unmodifiableMap(RUNIC_SHIELD_TYPES);

    @ApiStatus.Internal
    public static final Object2IntMap<RunicShieldTypeResourceLocation> RUNIC_SHIELD_WRITE_PALETTE = new Object2IntOpenHashMap<>();
    public static final Object2IntMap<RunicShieldTypeResourceLocation> RUNIC_SHIELD_WRITE_PALETTE_VIEW = Object2IntMaps.unmodifiable(RUNIC_SHIELD_WRITE_PALETTE);
    @ApiStatus.Internal
    public static final Int2ObjectMap<RunicShieldTypeResourceLocation> RUNIC_SHIELD_READ_PALETTE = new Int2ObjectOpenHashMap<>();
    public static final Int2ObjectMap<RunicShieldTypeResourceLocation> RUNIC_SHIELD_READ_PALETTE_VIEW = Int2ObjectMaps.unmodifiable(RUNIC_SHIELD_READ_PALETTE);
}
