package thaumcraft.common.blocks.crafted.ownedblock;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.GlassBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.wands.IWandInteractableOwnedBlock;
import thaumcraft.common.ClientFXUtils;

import static thaumcraft.api.ThaumcraftApiHelper.rayTraceIgnoringSource;

public class WardedGlassBlock extends GlassBlock implements IWandInteractableOwnedBlock {
    public WardedGlassBlock(Properties properties) {
        super(properties);
    }
    public WardedGlassBlock() {
        super(
                BlockBehaviour.Properties.of()
                        .strength(-1,999)
                        .sound(SoundType.STONE)
        );
    }

    @Override
    protected void spawnDestroyParticles(Level level, Player player, BlockPos blockPos, BlockState blockState) {
        if (!(level instanceof ClientLevel clientLevel)) return;
        var lookVec = player.getLookAngle();

        var blockHitResult = rayTraceIgnoringSource
                (level,
                        player.getEyePosition(),
                        player.getEyePosition().add(lookVec.normalize().multiply(new Vec3(10,10,10))),
                        true,
                        player
                );
        if (blockHitResult == null) {return;}
        var hitVec = blockHitResult.getLocation();

        
        float f = (float) (hitVec.x - blockPos.getX());
        float f1 = (float) (hitVec.y - blockPos.getY());
        float f2 = (float) (hitVec.z - blockPos.getZ());
        ClientFXUtils.blockWard(clientLevel, blockPos.getX(), blockPos.getY(), blockPos.getZ(), blockHitResult.getDirection(), f, f1, f2);
    }


    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState blockState, @Nullable LivingEntity livingEntity, ItemStack itemStack) {
        super.setPlacedBy(level, pos, blockState, livingEntity, itemStack);
        setPlacedOwnedBlockBy(level, pos, blockState, livingEntity, itemStack);
    }
}
