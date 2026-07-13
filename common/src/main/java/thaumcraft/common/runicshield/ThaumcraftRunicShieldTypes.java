package thaumcraft.common.runicshield;

import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.resourcelocations.RunicShieldTypeResourceLocation;
import thaumcraft.common.runicshield.shieldtypes.*;

public class ThaumcraftRunicShieldTypes {
    public static final CommonRunicShield COMMON = new CommonRunicShield(RunicShieldTypeResourceLocation.of(Thaumcraft.MOD_ID,"common"),0);
    public static final ChargedRunicShield CHARGED = new ChargedRunicShield(RunicShieldTypeResourceLocation.of(Thaumcraft.MOD_ID,"charged"),100);
    public static final HealingRunicShield HEALING = new HealingRunicShield(RunicShieldTypeResourceLocation.of(Thaumcraft.MOD_ID,"healing"),200);
    public static final EmergencyRunicShield EMERGENCY = new EmergencyRunicShield(RunicShieldTypeResourceLocation.of(Thaumcraft.MOD_ID,"emergency"),300);
    public static final KineticRunicShield KINETIC = new KineticRunicShield(RunicShieldTypeResourceLocation.of(Thaumcraft.MOD_ID,"kinetic"),400);
    public static void init(){

    }
}
