package thaumcraft.common;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class ThaumcraftSounds {
    public static class ResourceLocations {
        public static final ResourceLocation RES_RUNIC_SHIELD_CHARGE = new ResourceLocation(Thaumcraft.MOD_ID,"runic_shield_charge");
        public static final ResourceLocation RES_CRYSTAL = new ResourceLocation(Thaumcraft.MOD_ID,"crystal");
        public static final ResourceLocation RES_GORE = new ResourceLocation(Thaumcraft.MOD_ID,"gore");
        public static final ResourceLocation RES_LEARN = new ResourceLocation(Thaumcraft.MOD_ID,"learn");
        public static final ResourceLocation RES_CAMERA_TICKS = new ResourceLocation(Thaumcraft.MOD_ID,"cameraticks");
        public static final ResourceLocation RES_RUMBLE = new ResourceLocation(Thaumcraft.MOD_ID,"rumble");
        public static final ResourceLocation RES_FLY = new ResourceLocation(Thaumcraft.MOD_ID,"fly");
        public static final ResourceLocation RES_SPILL = new ResourceLocation(Thaumcraft.MOD_ID,"spill");
        public static final ResourceLocation RES_HEARTBEAT = new ResourceLocation(Thaumcraft.MOD_ID,"heartbeat");
        public static final ResourceLocation RES_ZAP = new ResourceLocation(Thaumcraft.MOD_ID,"zap");
        public static final ResourceLocation RES_TOOL = new ResourceLocation(Thaumcraft.MOD_ID,"tool");
        public static final ResourceLocation RES_CRAFT_FAIL = new ResourceLocation(Thaumcraft.MOD_ID,"craftfail");
        public static final ResourceLocation RES_ROOTS = new ResourceLocation(Thaumcraft.MOD_ID,"roots");
        public static final ResourceLocation RES_DOOR_FAIL = new ResourceLocation(Thaumcraft.MOD_ID,"doorfail");
        public static final ResourceLocation RES_PUMP = new ResourceLocation(Thaumcraft.MOD_ID,"pump");
        public static final ResourceLocation RES_ICE = new ResourceLocation(Thaumcraft.MOD_ID,"ice");
        public static final ResourceLocation RES_JACOBS = new ResourceLocation(Thaumcraft.MOD_ID,"jacobs");
        public static final ResourceLocation RES_ALEMBIC_KNOCK = new ResourceLocation(Thaumcraft.MOD_ID,"alembicknock");
        public static final ResourceLocation RES_PAGE = new ResourceLocation(Thaumcraft.MOD_ID,"page");
        public static final ResourceLocation RES_BUBBLE = new ResourceLocation(Thaumcraft.MOD_ID,"bubble");
        public static final ResourceLocation RES_JAR = new ResourceLocation(Thaumcraft.MOD_ID,"jar");
        public static final ResourceLocation RES_WHISPERS = new ResourceLocation(Thaumcraft.MOD_ID,"whispers");
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
    public static final SoundEvent ROOTS = SoundEvent.createVariableRangeEvent(ResourceLocations.RES_ROOTS);
    public static final SoundEvent DOOR_FAIL = SoundEvent.createVariableRangeEvent(ResourceLocations.RES_DOOR_FAIL);
    public static final SoundEvent PUMP = SoundEvent.createVariableRangeEvent(ResourceLocations.RES_PUMP);
    public static final SoundEvent ICE = SoundEvent.createVariableRangeEvent(ResourceLocations.RES_ICE);
    public static final SoundEvent JACOBS = SoundEvent.createVariableRangeEvent(ResourceLocations.RES_JACOBS);
    public static final SoundEvent ALEMBIC_KNOCK = SoundEvent.createVariableRangeEvent(ResourceLocations.RES_ALEMBIC_KNOCK);
    public static final SoundEvent PAGE = SoundEvent.createVariableRangeEvent(ResourceLocations.RES_PAGE);
    public static final SoundEvent BUBBLE = SoundEvent.createVariableRangeEvent(ResourceLocations.RES_BUBBLE);
    public static final SoundEvent JAR = SoundEvent.createVariableRangeEvent(ResourceLocations.RES_JAR);
    public static final SoundEvent WHISPERS = SoundEvent.createVariableRangeEvent(ResourceLocations.RES_WHISPERS);
}
