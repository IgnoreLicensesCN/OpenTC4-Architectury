package thaumcraft.common.blocks.crafted.visdevice.rechargepedestal;

import com.linearity.opentc4.annotations.forvalue.VisAmount;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.aspect.IAspectReducibleToPrimal;
import thaumcraft.api.aspects.aspectlists.CentiVisList;
import thaumcraft.api.wands.ICentiVisContainerItem;
import thaumcraft.common.blocks.abstracts.SuppressedWarningBlock;
import thaumcraft.common.tiles.abstracts.AbstractNodeBlockEntity;
import thaumcraft.common.tiles.abstracts.IWandRechargePedestalUpgradeBlock;

import java.util.concurrent.atomic.AtomicInteger;

import static thaumcraft.api.wands.ICentiVisContainerItem.CENTIVIS_MULTIPLIER;

public class CompoundRechargeFocusBlock extends SuppressedWarningBlock implements IWandRechargePedestalUpgradeBlock {
    public CompoundRechargeFocusBlock(Properties properties) {
        super(properties);
    }
    public CompoundRechargeFocusBlock() {
        this(
                Properties.of()
                        .strength(3,25)
                        .sound(SoundType.STONE)
                        .requiresCorrectToolForDrops()
                        .noOcclusion()
        );
    }

    public static final VoxelShape SHAPE = Shapes.box(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.4375F, 0.9375F);

    @Override
    public @NotNull VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return SHAPE;
    }

    @Override
    public @VisAmount int onDrainingMeetAspectNotRequired(
            Level atLevel,
            BlockPos pos,
            BlockState state,
            Aspect metAspect,
            int metVisAmountCanDrain,
            ItemStack containerStack,
            ICentiVisContainerItem<? extends Aspect> container,
            CentiVisList<? extends Aspect> requiringCentiVis,
            AbstractNodeBlockEntity nodeBE
    ) {
        if (metAspect instanceof IAspectReducibleToPrimal reducible){
            var reduced = reducible.reduceToPrimal();
            AtomicInteger maxDrained = new AtomicInteger(0);
            reduced.forEach((aspect, amount) -> {
                int required = requiringCentiVis.get(aspect);
                if (required != 0){
                    int maxDrainable = Math.min(1 + additionalMaxDrainAmountOnce(atLevel, pos, state),amount);
                    if (maxDrainable > 0){
                        addCentiVisForContainer(aspect,maxDrainable*CENTIVIS_MULTIPLIER,containerStack,container,requiringCentiVis);
                        maxDrained.set(
                                Math.max(maxDrained.get(), maxDrainable)
                        );
                    }
                }
            });
            return maxDrained.get();
        }
        return 0;
    }
}
