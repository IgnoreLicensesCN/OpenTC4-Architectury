package thaumcraft.api;

import net.minecraft.world.item.ItemStack;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.lib.utils.HexCoordUtils;

public interface IResearchTableAspectWriteTool {
    boolean canWrite(ItemStack writeToolStack, ItemStack researchNoteStack, Aspect aspect, HexCoordUtils.HexCoord placedAt);
}
