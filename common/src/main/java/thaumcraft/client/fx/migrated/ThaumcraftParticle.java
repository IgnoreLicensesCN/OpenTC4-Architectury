package thaumcraft.client.fx.migrated;

import com.linearity.opentc4.OpenTC4CommonProxy;
import com.linearity.opentc4.utils.vanilla1710.MathHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.nodes.IRevealer;

import java.util.function.Predicate;

import static thaumcraft.client.fx.migrated.Particles.PARTICLE_SPRITE;

public abstract class ThaumcraftParticle extends TextureSheetParticle {

    public double getXD(){
        return xd;
    }
    public double getYD(){
        return yd;
    }
    public double getZD(){
        return zd;
    }

    public boolean isDead(){
        return removed;
    }

    public double entityHeight(Entity entity) {
        var boundingBox = entity.getBoundingBox();
        return boundingBox.maxY - boundingBox.minY;
    }

    public float getOpModViaPlayer(float defaultValue) {
        var v = OpenTC4CommonProxy.INSTANCE.getLocalPlayer();
        if (v != null) {
            var equippeds = v.getArmorSlots();

            for (var equipped:equippeds) {
                if (equipped.getItem() instanceof IRevealer){
                    return 1.0F;
                }
            }

            if (v.getItemBySlot(EquipmentSlot.MAINHAND).getItem() instanceof IRevealer){
                return 1.0F;
            }
            if (v.getItemBySlot(EquipmentSlot.OFFHAND).getItem() instanceof IRevealer){
                return 1.0F;
            }
        }
        return defaultValue;
    }
    protected Player getClosestPlayerToEntity(Level level, float searchDistance) {
        return level.getNearestPlayer(x,y,z,searchDistance,false);
    }


    protected float updateRotation(float par1, float par2, float par3) {
        float f3 = MathHelper.wrapAngleTo180_float(par2 - par1);
        if (f3 > par3) {
            f3 = par3;
        }

        if (f3 < -par3) {
            f3 = -par3;
        }

        return par1 + f3;
    }

    public void setHeading(double par1, double par3, double par5, float par7, float par8) {
        float f2 = MathHelper.sqrt_double(par1 * par1 + par3 * par3 + par5 * par5);
        par1 /= f2;
        par3 /= f2;
        par5 /= f2;
        par1 += this.random.nextGaussian() * (double)(this.random.nextBoolean() ? -1 : 1) * (double)0.0075F * (double)par8;
        par3 += this.random.nextGaussian() * (double)(this.random.nextBoolean() ? -1 : 1) * (double)0.0075F * (double)par8;
        par5 += this.random.nextGaussian() * (double)(this.random.nextBoolean() ? -1 : 1) * (double)0.0075F * (double)par8;
        par1 *= par7;
        par3 *= par7;
        par5 *= par7;
        this.xd = par1;
        this.yd = par3;
        this.zd = par5;
    }
    public boolean removeIfTooFar(){
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            // 可见距离，根据 fancyGraphics 调整
            int chunkDistance = Minecraft.getInstance().options.getEffectiveRenderDistance(); // 区块数
            double visibleDistance = chunkDistance * 16;           // 转成方块数

            // 计算粒子和玩家距离平方（避免开根号）
            double distX = this.x - player.getX();
            double distY = this.y - player.getY();
            double distZ = this.z - player.getZ();
            double distSq = distX * distX + distY * distY + distZ * distZ;
            if (distSq > visibleDistance * visibleDistance) {
                this.remove();
                return true;
            }
        }
        return false;

    }

    protected ThaumcraftParticle(ClientLevel clientLevel, double x, double y, double z) {
        this(
                clientLevel,
                x,
                y,
                z,0,0,0
        );
        this.setSprite(PARTICLE_SPRITE);
    }

    protected ThaumcraftParticle(ClientLevel clientLevel, double x, double y, double z, double dx, double dy, double dz) {
        super(
                clientLevel,
                x,y,z,dx,dy,dz
        );
        this.setSprite(PARTICLE_SPRITE);
    }

    protected boolean pushOutOfBlocksUnified() {
        return pushOutOfBlocksUnified(pos -> {
            var state = level.getBlockState(pos);
            boolean airFlag = state.isAir();
            boolean fluidFlag = state.getFluidState().isEmpty();
            return !airFlag && !fluidFlag;
        });
    }

    protected boolean pushOutOfBlocksUnified(@NotNull Predicate<BlockPos> shouldPush) {
        int bx = MathHelper.floor_double(this.x);
        int by = MathHelper.floor_double(this.y);
        int bz = MathHelper.floor_double(this.z);

        double dx = this.x - bx;
        double dy = this.y - by;
        double dz = this.z - bz;

        BlockPos pos = new BlockPos(bx, by, bz);
        if (!shouldPush.test(pos)) {
            return false;
        }

        boolean negX = !level.isLoaded(pos.west()) || !level.getBlockState(pos.west()).isCollisionShapeFullBlock(level, pos.west());
        boolean posX = !level.isLoaded(pos.east()) || !level.getBlockState(pos.east()).isCollisionShapeFullBlock(level, pos.east());
        boolean negY = !level.isLoaded(pos.below()) || !level.getBlockState(pos.below()).isCollisionShapeFullBlock(level, pos.below());
        boolean posY = !level.isLoaded(pos.above()) || !level.getBlockState(pos.above()).isCollisionShapeFullBlock(level, pos.above());
        boolean negZ = !level.isLoaded(pos.north()) || !level.getBlockState(pos.north()).isCollisionShapeFullBlock(level, pos.north());
        boolean posZ = !level.isLoaded(pos.south()) || !level.getBlockState(pos.south()).isCollisionShapeFullBlock(level, pos.south());


        byte dir = -1;
        double minDist = 9999.0;

        if (negX && dx < minDist) { minDist = dx; dir = 0; }
        if (posX && 1.0 - dx < minDist) { minDist = 1.0 - dx; dir = 1; }
        if (negY && dy < minDist) { minDist = dy; dir = 2; }
        if (posY && 1.0 - dy < minDist) { minDist = 1.0 - dy; dir = 3; }
        if (negZ && dz < minDist) { minDist = dz; dir = 4; }
        if (posZ && 1.0 - dz < minDist) { minDist = 1.0 - dz; dir = 5; }

        float speed = this.random.nextFloat() * 0.05F + 0.025F;
        float spread = (this.random.nextFloat() - this.random.nextFloat()) * 0.1F;

        switch (dir) {
            case 0 -> {
                this.xd = -speed;
                this.yd = this.zd = spread; }
            case 1 -> {
                this.xd = speed;
                this.yd = this.zd = spread; }
            case 2 -> {
                this.yd = -speed;
                this.xd = this.zd = spread; }
            case 3 -> {
                this.yd = speed;
                this.xd = this.zd = spread; }
            case 4 -> {
                this.zd = -speed;
                this.yd = this.xd = spread; }
            case 5 -> {
                this.zd = speed;
                this.yd = this.xd = spread; }
        }

        return true;
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public void setGravity(float value) {
        this.gravity = value;
    }
    public void setNoClip(boolean value) {
        setPhysics(!value);
    }
    public void setPhysics(boolean value) {
        this.hasPhysics = value;
    }
    public void setParticleMaxAge(int particleMaxAge) {
        this.lifetime = particleMaxAge;
    }
    public void setRGB(float r, float g, float b) {
        this.rCol = r;
        this.gCol = g;
        this.bCol = b;
    }
    public void setRBGColorF(float r, float g, float b) {
        setRGB(r, g, b);
    }
    public void setAlphaF(float a) {
        this.alpha = a;
    }
    public void setXD(double x) {
        this.xd = x;
    }
    public void setYD(double y) {
        this.yd = y;
    }
    public void setZD(double z) {
        this.zd = z;
    }
    public void setScale(float scale) {
        this.quadSize = scale;
    }
    public void setMaxAge(int max) {
        this.lifetime = max;
    }
    public void setDead(){
        this.remove();
    }

    public double getDistanceSqToEntity(Entity entity) {
        var x = this.x - entity.getX();
        var y = this.y - entity.getY();
        var z = this.z - entity.getZ();
        return x*x + y*y + z*z;
    }

    public void moveEntity(double x, double y, double z) {
        move(x, y, z);
    }


    protected void renderBakedModel(PoseStack pose, VertexConsumer vc, float alpha, BakedModel model) {
        RandomSource rand = RandomSource.create(42);
        PoseStack.Pose entry = pose.last();
        float[] colors = new float[]{
                1.0F, 1.0F, 1.0F, alpha,
                1.0F, 1.0F, 1.0F, alpha,
                1.0F, 1.0F, 1.0F, alpha,
                1.0F, 1.0F, 1.0F, alpha
        };

        int fullBright = LightTexture.FULL_BRIGHT;
        int[] lights = new int[]{
                fullBright, fullBright, fullBright, fullBright
        };

        for (Direction dir : Direction.values()) {
            for (BakedQuad quad : model.getQuads(null, dir, rand)) {
                vc.putBulkData(
                        entry,
                        quad,
                        colors,
                        1.0F, 1.0F, 1.0F,
                        lights,
                        OverlayTexture.NO_OVERLAY,
                        false
                );
            }
        }

        for (BakedQuad quad : model.getQuads(null, null, rand)) {
            vc.putBulkData(
                    entry,
                    quad,
                    colors,
                    1.0F, 1.0F, 1.0F,
                    lights,
                    OverlayTexture.NO_OVERLAY,
                    false
            );
        }
    }
    public void setPosition(double x,double y,double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public void setVelocity(double x, double y, double z) {
        this.xd = x;
        this.yd = y;
        this.zd = z;
    }
}
