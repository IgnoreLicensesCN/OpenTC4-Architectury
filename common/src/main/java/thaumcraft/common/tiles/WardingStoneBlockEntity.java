package thaumcraft.common.tiles;

import com.linearity.opentc4.utils.vanilla1710.MathHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import thaumcraft.common.blocks.ThaumcraftBlocks;

import java.util.concurrent.atomic.AtomicInteger;

import static thaumcraft.common.blocks.crafted.PavingStoneWardingBlock.LIT;

public class WardingStoneBlockEntity extends BlockEntity {
    public final AtomicInteger tickCounter = new AtomicInteger();
    public WardingStoneBlockEntity(BlockEntityType blockEntityType, BlockPos blockPos, BlockState blockState){
        super(blockEntityType, blockPos, blockState);
    }
    public WardingStoneBlockEntity(BlockPos blockPos, BlockState blockState){
        super(ThaumcraftBlockEntities.WARDING_STONE, blockPos, blockState);
    }
}
