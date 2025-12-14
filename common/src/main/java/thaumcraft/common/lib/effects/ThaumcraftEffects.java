package thaumcraft.common.lib.effects;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import thaumcraft.common.Thaumcraft;

public class ThaumcraftEffects {

    public static final DeathGazeEffect DEATH_GAZE = Registry.SUPPLIER_DEATH_GAZE.get();
    public static final BlurredVisionEffect BLURRED_VISION = Registry.SUPPLIER_BLURRED_VISION.get();
    public static final SunScornedEffect SUN_SCORNED = Registry.SUPPLIER_SUN_SCORNED.get();
    public static final VisExhaustEffect VIS_EXHAUST = Registry.SUPPLIER_VIS_EXHAUST.get();
    public static final InfectiousVisExhaustEffect INFECTIOUS_VIS_EXHAUST = Registry.SUPPLIER_INFECTIOUS_VIS_EXHAUST_EFFECT.get();
    public static final FluxTaintEffect FLUX_TAINT = Registry.SUPPLIER_FLUX_TAINT.get();
    public static final ThaumarhiaEffect THAUMARHIA = Registry.SUPPLIER_THAUMARHIA.get();
    public static final WarpWardEffect WARP_WARD = Registry.SUPPLIER_WARP_WARD.get();
    public static final UnnaturalHungerEffect UNNATURAL_HUNGER = Registry.SUPPLIER_UNNATURAL_HUNGER.get();

    public static class Registry{
        public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(Thaumcraft.MOD_ID, Registries.MOB_EFFECT);

        //found unused icon(named soul_shatter.png now) and translation key called "potion.soulshatter"(renamed into "effect.thaumcraft.soul_shatter")
        //anazor may forget something.
        public static final RegistrySupplier<DeathGazeEffect> SUPPLIER_DEATH_GAZE = EFFECTS.register("death_gaze",DeathGazeEffect::new);
        public static final RegistrySupplier<BlurredVisionEffect> SUPPLIER_BLURRED_VISION = EFFECTS.register("blurred_vision",BlurredVisionEffect::new);
        public static final RegistrySupplier<SunScornedEffect> SUPPLIER_SUN_SCORNED = EFFECTS.register("sun_scorned",SunScornedEffect::new);
        public static final RegistrySupplier<VisExhaustEffect> SUPPLIER_VIS_EXHAUST = EFFECTS.register("vis_exhaust",VisExhaustEffect::new);
        public static final RegistrySupplier<InfectiousVisExhaustEffect> SUPPLIER_INFECTIOUS_VIS_EXHAUST_EFFECT = EFFECTS.register("inf_vis_exhaust",InfectiousVisExhaustEffect::new);
        public static final RegistrySupplier<FluxTaintEffect> SUPPLIER_FLUX_TAINT = EFFECTS.register("flux_taint",FluxTaintEffect::new);
        public static final RegistrySupplier<ThaumarhiaEffect> SUPPLIER_THAUMARHIA = EFFECTS.register("thaumarhia",ThaumarhiaEffect::new);
        public static final RegistrySupplier<WarpWardEffect> SUPPLIER_WARP_WARD = EFFECTS.register("warp_ward",WarpWardEffect::new);
        public static final RegistrySupplier<UnnaturalHungerEffect> SUPPLIER_UNNATURAL_HUNGER = EFFECTS.register("unnatural_hunger",UnnaturalHungerEffect::new);
    }


    public static void init(){
        Registry.EFFECTS.register();
    }
}
