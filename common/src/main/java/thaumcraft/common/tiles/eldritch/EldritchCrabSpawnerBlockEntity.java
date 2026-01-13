package thaumcraft.common.tiles.eldritch;

import com.linearity.colorannotation.annotation.RGBColor;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.AABB;
import thaumcraft.common.ClientFXUtils;
import thaumcraft.common.ThaumcraftSounds;
import thaumcraft.common.blocks.ThaumcraftBlocks;
import thaumcraft.common.blocks.worldgenerated.eldritch.EldritchCrabSpawnerBlock;
import thaumcraft.common.entities.monster.EntityEldritchCrab;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;

import java.util.List;
// todo
public class EldritchCrabSpawnerBlockEntity extends BlockEntity {
    public EldritchCrabSpawnerBlockEntity(BlockEntityType<EldritchCrabSpawnerBlockEntity> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }
    public EldritchCrabSpawnerBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(ThaumcraftBlockEntities.ELDRITCH_CRAB_SPAWNER, blockPos, blockState);
    }
    
    public int ticks = 0;
    public int venting = 0;
    public int count = 150;
    
    public void serverTick() {
        if (this.level == null){return;}
        final var pos = getBlockPos();
        if (this.ticks == 0) {
            this.ticks = this.level.random.nextInt(500);
        }
        ticks += 1;

        --this.count;
        if (this.count < 0) {
            this.count = 50 + this.level.random.nextInt(50);
        } else {
            if (this.count == 15 && this.isActivated() && this.maxEntitiesNotReached()) {
                this.level.blockEvent(pos, ThaumcraftBlocks.ELDRITCH_CRAB_SPAWNER, 1, 0);
                this.level.playSound(null,pos,SoundEvents.LAVA_EXTINGUISH, SoundSource.BLOCKS, 0.5F, 1.0F);
            }

            if (this.count <= 0 && this.isActivated() && this.maxEntitiesNotReached()) {
                this.count = 150 + this.level.random.nextInt(100);
                this.spawnCrab();
                this.level.playSound(null,pos, ThaumcraftSounds.GORE,SoundSource.BLOCKS, 0.5F, 1.0F);
            }
        }
    }
    public boolean maxEntitiesNotReached() {
        final var pos = getBlockPos();
        final var posX = pos.getX();
        final var posY = pos.getY();
        final var posZ = pos.getZ();
        List<EntityEldritchCrab> ents = this.level.getEntitiesOfClass(
                EntityEldritchCrab.class, AABB.of(new BoundingBox(
                        posX,posY,posZ,
                        posX+1,posY+1,posZ+1
                ).inflatedBy(32)));
        return ents.size() <= 5;
    }
    public boolean isActivated() {
        if (this.level == null){return false;}
        final var pos = getBlockPos();
        final var posX = pos.getX();
        final var posY = pos.getY();
        final var posZ = pos.getZ();
        return this.level.getNearestPlayer(posX + 0.5, posY + 0.5, posZ + 0.5, 16.0F,true) != null;
    }
    
    public void clientTick() {
        if (this.level == null){return;}
        if (this.ticks == 0) {
            this.ticks = this.level.random.nextInt(500);
        }
        ticks += 1;

        if (this.venting > 0) {
            --this.venting;

            for(int a = 0; a < 3; ++a) {
                this.drawVent();
            }
        } else if (this.level.random.nextInt(20) == 0) {
            this.drawVent();
        }
    }

    private static final @RGBColor int ventParticleColor = 0x9988aa;
    private void drawVent() {
        if (this.level == null){return;}
        if (!this.level.isClientSide) {return;}
        if (!(this.level instanceof ClientLevel clientLevel)){return;}
        final var pos = getBlockPos();
        final var posX = pos.getX();
        final var posY = pos.getY();
        final var posZ = pos.getZ();
        Direction dir = this.level.getBlockState(getBlockPos()).getValue(EldritchCrabSpawnerBlock.FACING);
//        Direction dir = Direction.getOrientation(this.facing);
        float fx = 0.15F - this.level.random.nextFloat() * 0.3F;
        float fz = 0.15F - this.level.random.nextFloat() * 0.3F;
        float fy = 0.15F - this.level.random.nextFloat() * 0.3F;
        float fx2 = 0.1F - this.level.random.nextFloat() * 0.2F;
        float fz2 = 0.1F - this.level.random.nextFloat() * 0.2F;
        float fy2 = 0.1F - this.level.random.nextFloat() * 0.2F;

        ClientFXUtils.drawVentParticles(clientLevel,
                posX + 0.5F + fx + dir.getStepX() / 2.1F,
                posY + 0.5F + fy + dir.getStepY() / 2.1F,
                posZ + 0.5F + fz + dir.getStepZ() / 2.1F,
                dir.getStepX() / 3.0F + fx2,
                dir.getStepY() / 3.0F + fy2,
                (float)dir.getStepZ() / 3.0F + fz2,
                ventParticleColor,
                2.0F);
    }
    private void spawnCrab() {
        if (this.level == null){return;}
        if (!this.level.isClientSide) {return;}
        if (!(this.level instanceof ClientLevel clientLevel)){return;}
        final var pos = getBlockPos();
        final var posX = pos.getX();
        final var posY = pos.getY();
        final var posZ = pos.getZ();
        Direction dir = this.level.getBlockState(getBlockPos()).getValue(EldritchCrabSpawnerBlock.FACING);
//        Direction dir = Direction.getOrientation(this.facing);
        EntityEldritchCrab crab = new EntityEldritchCrab(this.level);
        double x = posX + dir.getStepX();
        double y = posY + dir.getStepY();
        double z = posZ + dir.getStepZ();
        crab.setLocationAndAngles(x + (double)0.5F, y + (double)0.5F, z + (double)0.5F, 0.0F, 0.0F);
        crab.onSpawnWithEgg(null);
        crab.setHelm(false);
        crab.motionX = (float)dir.getStepX() * 0.2F;
        crab.motionY = (float)dir.getStepY() * 0.2F;
        crab.motionZ = (float)dir.getStepZ() * 0.2F;
        this.level.addFreshEntity(crab);
    }
}
