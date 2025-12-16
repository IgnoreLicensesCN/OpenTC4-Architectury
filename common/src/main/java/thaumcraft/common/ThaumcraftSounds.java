package thaumcraft.common;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class ThaumcraftSounds {
    public static class ResourceLocations {
        public static final ResourceLocation RES_RUNIC_SHIELD_CHARGE = new ResourceLocation("thaumcraft:runic_shield_charge");
        public static final ResourceLocation RES_CRYSTAL = new ResourceLocation("thaumcraft:crystal");
        public static final ResourceLocation RES_GORE = new ResourceLocation("thaumcraft:gore");
        public static final ResourceLocation RES_LEARN = new ResourceLocation("thaumcraft:learn");
        public static final ResourceLocation RES_CAMERA_TICKS = new ResourceLocation("thaumcraft:cameraticks");
        public static final ResourceLocation RES_RUMBLE = new ResourceLocation("thaumcraft:rumble");
        public static final ResourceLocation RES_FLY = new ResourceLocation("thaumcraft:fly");
        public static final ResourceLocation RES_SPILL = new ResourceLocation("thaumcraft:spill");
        public static final ResourceLocation RES_HEARTBEAT = new ResourceLocation("thaumcraft:heartbeat");
        public static final ResourceLocation RES_ZAP = new ResourceLocation("thaumcraft:zap");
        public static final ResourceLocation RES_TOOL = new ResourceLocation("thaumcraft:tool");
        public static final ResourceLocation RES_CRAFT_FAIL = new ResourceLocation("thaumcraft:craftfail");
    }

    public static final SoundEvent RUNIC_SHIELD_CHARGE = SoundEvent.createFixedRangeEvent(ResourceLocations.RES_RUNIC_SHIELD_CHARGE,16.F);
    public static final SoundEvent CRYSTAL = SoundEvent.createFixedRangeEvent(ResourceLocations.RES_CRYSTAL,16.F);
    public static final SoundEvent GORE = SoundEvent.createFixedRangeEvent(ResourceLocations.RES_GORE,16.F);
    public static final SoundEvent LEARN = SoundEvent.createFixedRangeEvent(ResourceLocations.RES_LEARN,16.F);
    public static final SoundEvent CAMERA_TICKS = SoundEvent.createVariableRangeEvent(ResourceLocations.RES_CAMERA_TICKS);
    public static final SoundEvent RUMBLE = SoundEvent.createVariableRangeEvent(ResourceLocations.RES_RUMBLE);
    public static final SoundEvent FLY = SoundEvent.createVariableRangeEvent(ResourceLocations.RES_FLY);
    public static final SoundEvent SPILL = SoundEvent.createVariableRangeEvent(ResourceLocations.RES_SPILL);
    public static final SoundEvent HEARTBEAT = SoundEvent.createVariableRangeEvent(ResourceLocations.RES_HEARTBEAT);
    public static final SoundEvent ZAP = SoundEvent.createVariableRangeEvent(ResourceLocations.RES_ZAP);
    public static final SoundEvent TOOL = SoundEvent.createVariableRangeEvent(ResourceLocations.RES_TOOL);
    public static final SoundEvent CRAFT_FAIL = SoundEvent.createVariableRangeEvent(ResourceLocations.RES_CRAFT_FAIL);
}
