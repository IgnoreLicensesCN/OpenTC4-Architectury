package thaumcraft.common.tiles.abstracts;

import com.google.common.collect.MapMaker;
import com.linearity.opentc4.utils.CubeChunkedWeakLookups;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

//oh even more AE2 tricks can be performed like "provide all items needed in a container"
public interface IInfusionComponentStackProvider extends Container, ICubeChunkBasedWeakLookupOwner<IInfusionComponentStackProvider> {
}
