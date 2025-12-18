package thaumcraft.common.tiles.node;

import com.linearity.opentc4.utils.BlockPosWithDim;
import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.TileThaumcraft;
import thaumcraft.api.WorldCoordinates;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.nodes.INode;
import thaumcraft.api.nodes.NodeModifier;
import thaumcraft.api.nodes.NodeType;
import thaumcraft.api.research.ScanResult;
import thaumcraft.api.wands.INodeHarmfulComponent;
import thaumcraft.api.wands.IVisContainer;
import thaumcraft.api.wands.IWandComponentsOwner;
import thaumcraft.api.wands.WandInteractableBlock;
import thaumcraft.common.ClientFXUtils;
import thaumcraft.common.blocks.BlockTaintFibres;
import thaumcraft.common.blocks.ThaumcraftBlocks;
import thaumcraft.common.config.Config;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.entities.EntityAspectOrb;
import thaumcraft.common.entities.monster.EntityGiantBrainyZombie;
import thaumcraft.common.items.misc.ItemCompassStone;
import thaumcraft.common.lib.network.fx.PacketFXBlockZapS2C;
import thaumcraft.common.lib.research.ResearchManager;
import thaumcraft.common.lib.research.ScanManager;
import thaumcraft.common.lib.utils.EntityUtils;
import thaumcraft.common.lib.utils.Utils;
import thaumcraft.common.lib.world.ThaumcraftWorldGenerator;
import thaumcraft.common.tiles.AbstractNodeBlockEntity;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.linearity.opentc4.Consts.NodeBlockEntityCompoundTagAccessors.*;
import static thaumcraft.api.nodes.NodeModifier.*;
import static thaumcraft.api.wands.IVisContainer.CENTIVIS_MULTIPLIER;

public class NodeBlockEntity extends AbstractNodeBlockEntity {
    public NodeBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(ThaumcraftBlockEntities.AURA_NODE, blockPos, blockState);
    }

    public NodeBlockEntity(BlockEntityType blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    @Override
    public boolean nodeLockApplicable() {
        return true;
    }

    @Override
    public void removeNode() {
        if (this.level != null) {
            this.level.setBlock(this.getBlockPos(), Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
        }
    }
}
