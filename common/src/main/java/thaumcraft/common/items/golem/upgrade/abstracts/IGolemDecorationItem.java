package thaumcraft.common.items.golem.upgrade.abstracts;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Unmodifiable;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.lib.resourcelocations.DecorationOccupiedTypeResourceLocation;

import java.util.Set;

public interface IGolemDecorationItem extends BaseGolemEntity.IGolemUpgradeItem {
    @Unmodifiable
    Set<DecorationOccupiedTypeResourceLocation> getOccupiedTypes(BaseGolemEntity golem, ItemStack stack);

    class DefaultDecorationTypes {
        public static final DecorationOccupiedTypeResourceLocation HAT = DecorationOccupiedTypeResourceLocation.of(Thaumcraft.MOD_ID ,"hat");
        public static final DecorationOccupiedTypeResourceLocation EYE_LEFT = DecorationOccupiedTypeResourceLocation.of(Thaumcraft.MOD_ID ,"eye_left");
        public static final DecorationOccupiedTypeResourceLocation EYE_RIGHT = DecorationOccupiedTypeResourceLocation.of(Thaumcraft.MOD_ID ,"eye_right");
        public static final DecorationOccupiedTypeResourceLocation ARM_LEFT = DecorationOccupiedTypeResourceLocation.of(Thaumcraft.MOD_ID ,"arm_left");
        public static final DecorationOccupiedTypeResourceLocation ARM_RIGHT = DecorationOccupiedTypeResourceLocation.of(Thaumcraft.MOD_ID ,"arm_right");
        public static final DecorationOccupiedTypeResourceLocation HAND_LEFT = DecorationOccupiedTypeResourceLocation.of(Thaumcraft.MOD_ID ,"hand_left");
        public static final DecorationOccupiedTypeResourceLocation HAND_RIGHT = DecorationOccupiedTypeResourceLocation.of(Thaumcraft.MOD_ID ,"hand_right");
        public static final DecorationOccupiedTypeResourceLocation FACE = DecorationOccupiedTypeResourceLocation.of(Thaumcraft.MOD_ID ,"face");
        public static final DecorationOccupiedTypeResourceLocation NECKLACE = DecorationOccupiedTypeResourceLocation.of(Thaumcraft.MOD_ID ,"necklace");
        public static final DecorationOccupiedTypeResourceLocation CHESTPLATE = DecorationOccupiedTypeResourceLocation.of(Thaumcraft.MOD_ID ,"chestplate");
    }
}
