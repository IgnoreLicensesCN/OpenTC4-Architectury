package thaumcraft.client.fx.particles.migrated.particles;

import com.linearity.opentc4.utils.vanilla1710.MathHelper;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.lwjgl.opengl.GL11;
import thaumcraft.common.ThaumcraftSounds;
import thaumcraft.common.config.ConfigBlocks;

import java.util.ArrayList;

public class FXSwarm extends ThaumcraftParticle {

    private Entity target;
    private float turnSpeed;
    private float speed;
    int deathtimer;
    private static ArrayList<Long> buzzcount = new ArrayList<>();
    public int particle;

    public FXSwarm(ClientLevel par1World, double x, double y, double z, Entity target, float r, float g, float b) {
        super(par1World, x, y, z, 0.0F, 0.0F, 0.0F);
        this.turnSpeed = 10.0F;
        this.speed = 0.2F;
        this.deathtimer = 0;
        this.particle = 40;
        this.rCol = r;
        this.gCol = g;
        this.bCol = b;
        this.quadSize = this.random.nextFloat() * 0.5F + 1.0F;
        this.target = target;
        float f3 = 0.2F;
        this.xd = (this.random.nextFloat() - this.random.nextFloat()) * f3;
        this.yd = (this.random.nextFloat() - this.random.nextFloat()) * f3;
        this.zd = (this.random.nextFloat() - this.random.nextFloat()) * f3;
        this.gravity = 0.1F;
        this.hasPhysics = true;
        removeIfTooFar();

    }

    public FXSwarm(ClientLevel par1World, double x, double y, double z, Entity target, float r, float g, float b, float sp, float ts, float pg) {
        this(par1World, x, y, z, target, r, g, b);
        this.speed = sp;
        this.turnSpeed = ts;
        this.gravity = pg;
    }

    @Override
    public void render(VertexConsumer consumer, Camera camera, float partialTicks) {
        var rightVec = camera.getLeftVector().negate();
        var f1 = rightVec.x;
        var f3 = rightVec.z;
        var upVec = camera.getUpVector();
        var f4 = upVec.x;
        var f2 = upVec.y;
        var f5 = upVec.z;
        var f = partialTicks;
        var cameraPos = camera.getPosition();
        var interpPosX = cameraPos.x;
        var interpPosY = cameraPos.y;
        var interpPosZ = cameraPos.z;

        float bob = MathHelper.sin((float)this.age / 3.0F) * 0.25F + 1.0F;
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.75F);
        int part = 7 + this.age % 8;
        float var8 = (float)part / 16.0F;
        float var9 = var8 + 0.0624375F;
        float var10 = 0.25F;
        float var11 = var10 + 0.0624375F;
        float var12 = 0.1F * this.quadSize * bob;
        float var13 = (float)(this.xo + (this.x - this.xo) * (double)f - interpPosX);
        float var14 = (float)(this.yo + (this.y - this.yo) * (double)f - interpPosY);
        float var15 = (float)(this.zo + (this.z - this.zo) * (double)f - interpPosZ);
        float trans = (50.0F - (float)this.deathtimer) / 50.0F;
        var r = this.rCol;
        var g = this.gCol  / 2.0F;
        var b = this.bCol  / 2.0F;
        if (this.target instanceof LivingEntity livingEntity && livingEntity.hurtTime <= 0) {
            r = this.rCol;
            g = this.gCol;
            b = this.bCol ;
        }
        consumer.vertex(
                        var13 - f1 * var12 - f4 * var12,
                        var14 - f2 * var12,
                        var15 - f3 * var12 - f5 * var12
                )
                .color(r, g, b, trans)
                .uv(var9, var11)
                .uv2(LightTexture.FULL_BRIGHT)//TODO:Bright
                .endVertex();
        consumer.vertex(
                        var13 - f1 * var12 + f4 * var12,
                        var14 + f2 * var12,
                        var15 - f3 * var12 + f5 * var12
                )
                .color(r, g, b, trans)
                .uv(var9, var10)
                .uv2(LightTexture.FULL_BRIGHT)
                .endVertex();
        consumer.vertex(var13 + f1 * var12 + f4 * var12, var14 + f2 * var12, var15 + f3 * var12 + f5 * var12
                )
                .color(r, g, b, trans)
                .uv(var8, var10)
                .uv2(LightTexture.FULL_BRIGHT)
                .endVertex();
        consumer.vertex(var13 + f1 * var12 - f4 * var12, var14 - f2 * var12, var15 + f3 * var12 - f5 * var12)
                .color(r, g, b, trans)
                .uv(var8, var11)
                .uv2(LightTexture.FULL_BRIGHT)
                .endVertex();
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        ++this.age;
        if (this.target == null || this.target.isRemoved() || this.target instanceof LivingEntity livingEntity && livingEntity.deathTime > 0) {
            ++this.deathtimer;
            this.yd -= this.gravity / 2.0F;
            if (this.deathtimer > 50) {
                this.setDead();
            }
        } else {
            this.yd += this.gravity;
        }

        this.pushOutOfBlocksUnified(pos -> {

            var state = level.getBlockState(pos);
            var block = state.getBlock();
            boolean airFlag = state.isAir();
            boolean fluidFlag = state.getFluidState().isEmpty();
            return !airFlag && !fluidFlag && block == ConfigBlocks.blockTaintFibres;
        });
        this.moveEntity(this.xd, this.yd, this.zd);
        this.xd *= 0.985;
        this.yd *= 0.985;
        this.zd *= 0.985;
        if (this.target != null && !this.target.isRemoved() && (!(this.target instanceof LivingEntity livingEntity) || livingEntity.deathTime <= 0)) {
            boolean hurt = false;
            if (this.target instanceof LivingEntity livingEntity) {
                hurt = livingEntity.hurtTime > 0;
            }
            var targetBoundingBox = this.target.getBoundingBox();
            var width = targetBoundingBox.maxZ - targetBoundingBox.minZ;
            if (this.getDistanceSqToEntity(this.target) >  width && !hurt) {
                this.faceEntity(this.target, this.turnSpeed / 2.0F + (float) this.random.nextInt((int)(this.turnSpeed / 2.0F)), this.turnSpeed / 2.0F + (float) this.random.nextInt((int)(this.turnSpeed / 2.0F)));
            } else {
                this.faceEntity(this.target, -(this.turnSpeed / 2.0F + (float) this.random.nextInt((int)(this.turnSpeed / 2.0F))), -(this.turnSpeed / 2.0F + (float) this.random.nextInt((int)(this.turnSpeed / 2.0F))));
            }

            this.xd = -MathHelper.sin(this.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(
                    this.rotationPitch / 180.0F * (float) Math.PI);
            this.zd = MathHelper.cos(this.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(
                    this.rotationPitch / 180.0F * (float) Math.PI);
            this.yd = -MathHelper.sin(this.rotationPitch / 180.0F * (float) Math.PI);
            this.setHeading(this.xd, this.yd, this.zd, this.speed, 15.0F);
        }

        if (buzzcount.size() < 3 && this.random.nextInt(50) == 0 && getClosestPlayerToEntity(this.level, 8.0F) != null) {
            this.level.playLocalSound(this.x, this.y, this.z,
                    ThaumcraftSounds.FLY,
                    SoundSource.AMBIENT, 0.03F, 0.5F + this.random.nextFloat() * 0.4F, false);
            buzzcount.add(System.nanoTime() + 1500000L);
        }

        if (buzzcount.size() >= 3 && buzzcount.getFirst() < System.nanoTime()) {
            buzzcount.removeFirst();
        }

    }
    private float rotationPitch = 0.f;
    private float rotationYaw = 0.f;
    public void faceEntity(Entity par1Entity, float par2, float par3) {
        double d0 = par1Entity.getX() - this.x;
        double d1 = par1Entity.getZ() - this.z;
        var boundingBox = par1Entity.getBoundingBox();
        var thisBoundingBox = this.getBoundingBox();
        double d2 = (boundingBox.minY + boundingBox.maxY) / (double)2.0F - (thisBoundingBox.minY + thisBoundingBox.maxY) / (double)2.0F;
        double d3 = MathHelper.sqrt_double(d0 * d0 + d1 * d1);
        float f2 = (float)(Math.atan2(d1, d0) * (double)180.0F / Math.PI) - 90.0F;
        float f3 = (float)(-(Math.atan2(d2, d3) * (double)180.0F / Math.PI));

        this.rotationPitch = this.updateRotation(this.rotationPitch, f3, par3);
        this.rotationYaw = this.updateRotation(this.rotationYaw, f2, par2);
    }
}
