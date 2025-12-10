package thaumcraft.client.fx.particles.migrated.particles;

import com.linearity.opentc4.utils.vanilla1710.MathHelper;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.metadata.animation.FrameSize;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;

public class FXDrop extends ThaumcraftParticle {
    private static final TextureAtlasSprite DROP112;
    private static final TextureAtlasSprite DROP113;
    private static final TextureAtlasSprite DROP114;

    static {
        // ResourceLocation 可以随便写，只是标识用
        DROP112 = loadSprite( "thaumcraft", "particle/drop112.png");
        DROP113 = loadSprite( "thaumcraft", "particle/drop113.png");
        DROP114 = loadSprite("thaumcraft", "particle/drop114.png");
    }
    @Contract("_, _ -> new")
    static @NotNull TextureAtlasSprite loadSprite(String modid, String path) {
        ResourceLocation rl = new ResourceLocation(modid, path);
        try (InputStream is = FXDrop.class.getClassLoader().getResourceAsStream("assets/"+modid+"/textures/"+path)) {
            if (is == null) throw new IOException("Cannot find resource: "+ modid +":" + path);
            NativeImage image = NativeImage.read(is);
            FrameSize frameSize = new FrameSize(image.getWidth(), image.getHeight());
            SpriteContents spriteContents = new SpriteContents(
                    rl,
                    frameSize,
                    image,
                    null // 如果没有动画元数据
            );
            return new TextureAtlasSprite(rl, spriteContents, image.getWidth(), image.getHeight(), 0, 0){};
        } catch (IOException e) {
            throw new RuntimeException(e);
//            OpenTC4.LOGGER.error(e);
//            e.printStackTrace();
//            return new TextureAtlasSprite(rl, null, 8, 8, 0, 0){};
        }
    }



    int bobTimer;

    public FXDrop(ClientLevel par1World, double par2, double par4, double par6, float r, float g, float b){
        super(par1World,par2,par4,par6,0,0,0);
        this.setColor(r,g,b);
        this.xd=this.yd=this.zd=0;
        this.gravity = 0.06F;
        this.bobTimer = 40;
        this.lifetime = (int)((double)64.0F / (Math.random() * 0.8 + 0.2));
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        this.yd -= this.gravity;
        if (this.bobTimer-- > 0) {
            this.xd *= 0.02;
            this.yd *= 0.02;
            this.zd *= 0.02;
            setSprite(DROP113);
        } else {
            setSprite(DROP112);
        }
        this.move(this.xd, this.yd, this.zd);
        this.xd *= 0.98F;
        this.yd *= 0.98F;
        this.zd *= 0.98F;
        if (this.lifetime-- <= 0) {
            this.remove();
        }

        if (this.onGround) {
            setSprite(DROP114);
            this.xd *= 0.7F;
            this.zd *= 0.7F;
        }

        BlockPos pos = new BlockPos(MathHelper.floor_double(this.x), MathHelper.floor_double(this.y), MathHelper.floor_double(this.z));
        var state = level.getBlockState(pos);
        var fluidState = state.getFluidState();
        if (!state.is(Blocks.GRASS) && (!state.getFluidState().isEmpty() || !state.getCollisionShape(level, pos).isEmpty())) {
            double liquidHeight = pos.getY() + 1.0;
            if (!fluidState.isEmpty()) {
                liquidHeight -= fluidState.getHeight(level, pos);
            }
            if (this.y < liquidHeight) {
                this.remove();
            }
        }

    }

//    go f**k off
//    public void moveEntity(double par1, double par3, double par5) {
//        int x = MathHelper.floor_double(this.x);
//        int y = MathHelper.floor_double(this.y);
//        int z = MathHelper.floor_double(this.z);
//        var pos = new BlockPos(x,y,z);
//        if (this.hasPhysics && this.level.getBlockState(pos).getBlock()!=(ConfigBlocks.blockJar)) {
//            this.level.theProfiler.startSection("move");
//            this.ySize *= 0.4F;
//            double d3 = this.x;
//            double d4 = this.y;
//            double d5 = this.z;
//            if (this.isInWeb) {
//                this.isInWeb = false;
//                par1 *= 0.25F;
//                par3 *= 0.05F;
//                par5 *= 0.25F;
//                this.xd = 0.0F;
//                this.yd = 0.0F;
//                this.zd = 0.0F;
//            }
//
//            double d6 = par1;
//            double d7 = par3;
//            double d8 = par5;
//            AxisAlignedBB axisalignedbb = this.boundingBox.copy();
//            boolean flag = this.onGround && this.isSneaking();
//            if (flag) {
//                double d9;
//                for(d9 = 0.05; par1 != (double)0.0F && this.level().getCollidingBoundingBoxes(this, this.boundingBox.getOffsetBoundingBox(par1, -1.0F, 0.0F)).isEmpty(); d6 = par1) {
//                    if (par1 < d9 && par1 >= -d9) {
//                        par1 = 0.0F;
//                    } else if (par1 > (double)0.0F) {
//                        par1 -= d9;
//                    } else {
//                        par1 += d9;
//                    }
//                }
//
//                for(; par5 != (double)0.0F && this.level().getCollidingBoundingBoxes(this, this.boundingBox.getOffsetBoundingBox(0.0F, -1.0F, par5)).isEmpty(); d8 = par5) {
//                    if (par5 < d9 && par5 >= -d9) {
//                        par5 = 0.0F;
//                    } else if (par5 > (double)0.0F) {
//                        par5 -= d9;
//                    } else {
//                        par5 += d9;
//                    }
//                }
//
//                while(par1 != (double)0.0F && par5 != (double)0.0F && this.level().getCollidingBoundingBoxes(this, this.boundingBox.getOffsetBoundingBox(par1, -1.0F, par5)).isEmpty()) {
//                    if (par1 < d9 && par1 >= -d9) {
//                        par1 = 0.0F;
//                    } else if (par1 > (double)0.0F) {
//                        par1 -= d9;
//                    } else {
//                        par1 += d9;
//                    }
//
//                    if (par5 < d9 && par5 >= -d9) {
//                        par5 = 0.0F;
//                    } else if (par5 > (double)0.0F) {
//                        par5 -= d9;
//                    } else {
//                        par5 += d9;
//                    }
//
//                    d6 = par1;
//                    d8 = par5;
//                }
//            }
//
//            List list = this.level().getCollidingBoundingBoxes(this, this.boundingBox.addCoord(par1, par3, par5));
//
//            for (Object o2 : list) {
//                par3 = ((AxisAlignedBB) o2).calculateYOffset(this.boundingBox, par3);
//            }
//
//            this.boundingBox.offset(0.0F, par3, 0.0F);
//            if (!this.field_70135_K && d7 != par3) {
//                par5 = 0.0F;
//                par3 = 0.0F;
//                par1 = 0.0F;
//            }
//
//            boolean flag1 = this.onGround || d7 != par3 && d7 < (double)0.0F;
//
//            for (Object o1 : list) {
//                par1 = ((AxisAlignedBB) o1).calculateXOffset(this.boundingBox, par1);
//            }
//
//            this.boundingBox.offset(par1, 0.0F, 0.0F);
//            if (!this.field_70135_K && d6 != par1) {
//                par5 = 0.0F;
//                par3 = 0.0F;
//                par1 = 0.0F;
//            }
//
//            for (Object element : list) {
//                par5 = ((AxisAlignedBB) element).calculateZOffset(this.boundingBox, par5);
//            }
//
//            this.boundingBox.offset(0.0F, 0.0F, par5);
//            if (!this.field_70135_K && d8 != par5) {
//                par5 = 0.0F;
//                par3 = 0.0F;
//                par1 = 0.0F;
//            }
//
//            if (this.stepHeight > 0.0F && flag1 && (flag || this.ySize < 0.05F) && (d6 != par1 || d8 != par5)) {
//                double d12 = par1;
//                double d10 = par3;
//                double d11 = par5;
//                par1 = d6;
//                par3 = this.stepHeight;
//                par5 = d8;
//                AxisAlignedBB axisalignedbb1 = this.boundingBox.copy();
//                this.boundingBox.setBB(axisalignedbb);
//                list = this.level().getCollidingBoundingBoxes(this, this.boundingBox.addCoord(d6, par3, d8));
//
//                for (Object item : list) {
//                    par3 = ((AxisAlignedBB) item).calculateYOffset(this.boundingBox, par3);
//                }
//
//                this.boundingBox.offset(0.0F, par3, 0.0F);
//                if (!this.field_70135_K && d7 != par3) {
//                    par5 = 0.0F;
//                    par3 = 0.0F;
//                    par1 = 0.0F;
//                }
//
//                for (Object value : list) {
//                    par1 = ((AxisAlignedBB) value).calculateXOffset(this.boundingBox, par1);
//                }
//
//                this.boundingBox.offset(par1, 0.0F, 0.0F);
//                if (!this.field_70135_K && d6 != par1) {
//                    par5 = 0.0F;
//                    par3 = 0.0F;
//                    par1 = 0.0F;
//                }
//
//                for (Object object : list) {
//                    par5 = ((AxisAlignedBB) object).calculateZOffset(this.boundingBox, par5);
//                }
//
//                this.boundingBox.offset(0.0F, 0.0F, par5);
//                if (!this.field_70135_K && d8 != par5) {
//                    par5 = 0.0F;
//                    par3 = 0.0F;
//                    par1 = 0.0F;
//                }
//
//                if (!this.field_70135_K && d7 != par3) {
//                    par5 = 0.0F;
//                    par3 = 0.0F;
//                    par1 = 0.0F;
//                } else {
//                    par3 = -this.stepHeight;
//
//                    for (Object o : list) {
//                        par3 = ((AxisAlignedBB) o).calculateYOffset(this.boundingBox, par3);
//                    }
//
//                    this.boundingBox.offset(0.0F, par3, 0.0F);
//                }
//
//                if (d12 * d12 + d11 * d11 >= par1 * par1 + par5 * par5) {
//                    par1 = d12;
//                    par3 = d10;
//                    par5 = d11;
//                    this.boundingBox.setBB(axisalignedbb1);
//                }
//            }
//
//            this.level.theProfiler.endSection();
//            this.level.theProfiler.startSection("rest");
//            this.x = (this.boundingBox.minX + this.boundingBox.maxX) / (double)2.0F;
//            this.y = this.boundingBox.minY + (double)this.yOffset - (double)this.ySize;
//            this.z = (this.boundingBox.minZ + this.boundingBox.maxZ) / (double)2.0F;
//            this.isCollidedHorizontally = d6 != par1 || d8 != par5;
//            this.isCollidedVertically = d7 != par3;
//            this.onGround = d7 != par3 && d7 < (double)0.0F;
//            this.isCollided = this.isCollidedHorizontally || this.isCollidedVertically;
//            this.updateFallState(par3, this.onGround);
//            if (d6 != par1) {
//                this.xd = 0.0F;
//            }
//
//            if (d7 != par3) {
//                this.yd = 0.0F;
//            }
//
//            if (d8 != par5) {
//                this.zd = 0.0F;
//            }
//
//            double d12 = this.x - d3;
//            double d10 = this.y - d4;
//            double d11 = this.z - d5;
//            if (this.canTriggerWalking() && !flag && this.ridingEntity == null) {
//                int l = MathHelper.floor_double(this.x);
//                int k = MathHelper.floor_double(this.y - (double)0.2F - (double)this.yOffset);
//                int i1 = MathHelper.floor_double(this.z);
//                var pos2 = new BlockPos(l, k, i1);
//                BlockState j1 = this.level.getBlockState(pos2);
//                if (j1.isAir()) {
//                    var downPos2 = pos2.above(-1);
//                    int k1 = this.level.getBlockState(downPos2).getRenderType();//getRenderType
//                    if (k1 == 11 || k1 == 32 || k1 == 21) {
//                        j1 = this.level.getBlockState(downPos2);
//                    }
//                }
//
//                if (j1.getBlock() != Blocks.LADDER) {
//                    d10 = 0.0F;
//                }
//
//                this.distanceWalkedModified = (float)((double)this.distanceWalkedModified + (double)MathHelper.sqrt_double(d12 * d12 + d11 * d11) * 0.6);
//                this.distanceWalkedOnStepModified = (float)((double)this.distanceWalkedOnStepModified + (double)MathHelper.sqrt_double(d12 * d12 + d10 * d10 + d11 * d11) * 0.6);
//            }
//
//            try {
//                this.func_145775_I();
//            } catch (Throwable throwable) {
//                CrashReport crashreport = CrashReport.forThrowable(throwable, "Checking entity tile collision");
//                CrashReportCategory crashreportcategory = crashreport.addCategory("Entity being checked for collision");
////                this.addEntityCrashInfo(crashreportcategory);
//                throw new ReportedException(crashreport);
//            }
//
//            this.level.theProfiler.endSection();
//        } else {
//            this.boundingBox.offset(par1, par3, par5);
//            this.x = (this.boundingBox.minX + this.boundingBox.maxX) / (double)2.0F;
//            this.y = this.boundingBox.minY + (double)this.yOffset - (double)this.ySize;
//            this.z = (this.boundingBox.minZ + this.boundingBox.maxZ) / (double)2.0F;
//            x = MathHelper.floor_double(this.x);
//            y = MathHelper.floor_double(this.y);
//            y = MathHelper.floor_double(this.y);
//            if (this.level.getBlockState(pos.above(1)).getBlock() == ConfigBlocks.blockJar) {
//                this.x = this.xo;
//                this.y = this.yo;
//                this.z = this.zo;
//                this.yd = 0.0F;
//                this.onGround = true;
//            }
//        }
//
//    }
}
