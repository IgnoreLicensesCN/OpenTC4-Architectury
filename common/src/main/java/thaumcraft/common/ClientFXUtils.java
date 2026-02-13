package thaumcraft.common;

import com.linearity.opentc4.utils.vanilla1710.MathHelper;
import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.minecraft.client.Minecraft;
import net.minecraft.client.ParticleStatus;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.client.fx.migrated.particles.*;
import thaumcraft.client.fx.migrated.beams.*;
import thaumcraft.client.fx.migrated.bolt.*;
import thaumcraft.client.fx.migrated.other.*;
import thaumcraft.common.tiles.TileCrucible;

import java.awt.*;

//all method here should check platform
@SuppressWarnings("resource unused")
public class ClientFXUtils {

    public static ClientLevel getClientWorld() {
        if (!checkPlatformClient()) {
            throw new RuntimeException("shouldn't getClientWorld() inside a server");
        }
        return Minecraft.getInstance().level;
    }

    public static boolean checkPlatformClient() {
        return Platform.getEnvironment() == Env.CLIENT;
    }
    public static void arcLightning(ClientLevel world, double x, double y, double z, double tx, double ty, double tz, float r, float g, float b, float h) {
        if (!checkPlatformClient()){return;}
        FXSparkle ef2 = new FXSparkle(world, tx, ty, tz, tx, ty, tz, 3.0F, 6, 2);
        ef2.setGravity(0.0F);
        ef2.setNoClip(true);
        ef2.setRBGColorF(r, g, b);
        Minecraft.getInstance().particleEngine.add(ef2);

        FXArc efa = new FXArc(world, x, y, z, tx, ty, tz, r, g, b, h);
        Minecraft.getInstance().particleEngine.add(efa);
    }
    public static void blockSparkle(ClientLevel world, BlockPos pos, int c, int count){
        blockSparkle(world, pos.getX(),pos.getY(),pos.getZ(), count, c);
    }
    public static void blockSparkle(ClientLevel world, int x, int y, int z, int c, int count) {
        if (!checkPlatformClient()) {
            return;
        }
        Color color = new Color(c);
        float r = (float) color.getRed() / 255.0F;
        float g = (float) color.getGreen() / 255.0F;
        float b = (float) color.getBlue() / 255.0F;

        for (int a = 0; a < particleCount(count); ++a) {
            if (c == -9999) {
                r = 0.33F + world.getRandom().nextFloat() * 0.67F;
                g = 0.33F + world.getRandom().nextFloat() * 0.67F;
                b = 0.33F + world.getRandom().nextFloat() * 0.67F;
            }

            drawGenericParticles(
                    world,
                    (float) x - 0.1F + world.getRandom().nextFloat() * 1.2F,
                    (float) y - 0.1F + world.getRandom().nextFloat() * 1.2F,
                    (float) z - 0.1F + world.getRandom().nextFloat() * 1.2F,
                    0.0F,
                    (double) world.getRandom().nextFloat() * 0.02,
                    0.0F,
                    r - 0.2F + world.getRandom().nextFloat() * 0.4F,
                    g - 0.2F + world.getRandom().nextFloat() * 0.4F,
                    b - 0.2F + world.getRandom().nextFloat() * 0.4F,
                    0.9F,
                    false,
                    112,
                    9,
                    1,
                    5 + world.getRandom().nextInt(8),
                    world.getRandom().nextInt(10),
                    0.7F + world.getRandom().nextFloat() * 0.4F
            );
        }

    }

    public static void sparkle(float x, float y, float z, float size, int color, float gravity) {
        if (!checkPlatformClient()) {
            return;
        }
        if (getClientWorld() != null
                && getClientWorld().getRandom().nextInt(6) < particleCount(2)) {
            FXSparkle fx = new FXSparkle(
                    getClientWorld(),
                    x,
                    y,
                    z,
                    size,
                    color,
                    6
            );
            fx.setNoClip(true);
            fx.setGravity(gravity);
            Minecraft.getInstance().particleEngine.add(fx);

        }

    }

    public static void sparkle(float x, float y, float z, int color) {
        if (!checkPlatformClient()) {
            return;
        }
        if (getClientWorld() != null && getClientWorld().getRandom().nextInt(6) < particleCount(2)) {
            FXSparkle fx = new FXSparkle(
                    getClientWorld(),
                    x,
                    y,
                    z,
                    1.5F,
                    color,
                    6
            );
            fx.setNoClip(true);
            Minecraft.getInstance().particleEngine.add(fx);
        }

    }

    public static void spark(float x, float y, float z, float size, float r, float g, float b, float a) {
        if (!checkPlatformClient()) {
            return;
        }
        if (getClientWorld() != null) {
            FXSpark fx = new FXSpark(
                    getClientWorld(),
                    x,
                    y,
                    z,
                    size
            );
            fx.setRBGColorF(
                    r,
                    g,
                    b
            );
            fx.setAlphaF(a);
            Minecraft.getInstance().particleEngine.add(fx);

        }

    }

    public static void smokeSpiral(ClientLevel world, double x, double y, double z, float rad, int start, int miny, int color) {

        if (!checkPlatformClient()) {
            return;
        }
        FXSmokeSpiral fx = new FXSmokeSpiral(
                getClientWorld(),
                x,
                y,
                z,
                rad,
                start,
                miny
        );
        Color c = new Color(color);
        fx.setRBGColorF(
                (float) c.getRed() / 255.0F,
                (float) c.getGreen() / 255.0F,
                (float) c.getBlue() / 255.0F
        );
        Minecraft.getInstance().particleEngine.add(fx);

    }

    public static void crucibleBoilSound(ClientLevel world, int xCoord, int yCoord, int zCoord) {

        world.playLocalSound(
                (float) xCoord + 0.5F,
                (float) yCoord + 0.5F,
                (float) zCoord + 0.5F,
                ThaumcraftSounds.SPILL,
                SoundSource.BLOCKS,
                0.2F,
                1.0F,
                false
        );
    }

    public static void crucibleBoil(ClientLevel world, int xCoord, int yCoord, int zCoord, TileCrucible tile, int j) {

        if (!checkPlatformClient()) {
            return;
        }
        for (int a = 0; a < particleCount(1); ++a) {
            FXBubble fb = new FXBubble(
                    world,
                    (float) xCoord + 0.2F + world.getRandom().nextFloat() * 0.6F,
                    (float) yCoord + 0.1F + tile.getFluidHeight(),
                    (float) zCoord + 0.2F + world.getRandom().nextFloat() * 0.6F,
                    0.0F,
                    0.0F,
                    0.0F,
                    3
            );
            if (tile.aspects.isEmpty()) {
                fb.setRBGColorF(
                        1.0F,
                        1.0F,
                        1.0F
                );
            } else {
                var aspects = tile.aspects.keySet().toArray(new Aspect[0]);

                Color color = new Color(aspects[world.getRandom().nextInt(aspects.length)].getColor());
                fb.setRBGColorF(
                        (float) color.getRed() / 255.0F,
                        (float) color.getGreen() / 255.0F,
                        (float) color.getBlue() / 255.0F
                );
            }
            fb.bubblespeed = 0.003 * (double) j;
            Minecraft.getInstance().particleEngine.add(fb);
        }

    }

    public static void crucibleBubble(ClientLevel world, float x, float y, float z, float cr, float cg, float cb) {

        if (!checkPlatformClient()) {
            return;
        }
        FXBubble fb = new FXBubble(
                world,
                x,
                y,
                z,
                0.0F,
                0.0F,
                0.0F,
                1
        );
        fb.setRBGColorF(
                cr,
                cg,
                cb
        );
        Minecraft.getInstance().particleEngine.add(fb);

    }

    public static void crucibleFroth(ClientLevel world, float x, float y, float z) {

        if (!checkPlatformClient()) {
            return;
        }
        FXBubble fb = new FXBubble(
                world,
                x,
                y,
                z,
                0.0F,
                0.0F,
                0.0F,
                -4
        );
        fb.setRBGColorF(
                0.5F,
                0.5F,
                0.7F
        );
        fb.setFroth();
        Minecraft.getInstance().particleEngine.add(fb);

    }

    public static void crucibleFrothDown(ClientLevel world, float x, float y, float z) {

        if (!checkPlatformClient()) {
            return;
        }
        FXBubble fb = new FXBubble(
                world,
                x,
                y,
                z,
                0.0F,
                0.0F,
                0.0F,
                -4
        );
        fb.setRBGColorF(
                0.5F,
                0.5F,
                0.7F
        );
        fb.setFroth2();
        Minecraft.getInstance().particleEngine.add(fb);

    }

    public static void wispFX(ClientLevel worldObj, double posX, double posY, double posZ, float f, float g, float h, float i) {


        if (!checkPlatformClient()) {
            return;
        }
        FXWisp ef = new FXWisp(
                worldObj,
                posX,
                posY,
                posZ,
                f,
                g,
                h,
                i
        );
        ef.setGravity(0.02F);
        Minecraft.getInstance().particleEngine.add(ef);

    }

    public static void wispFX2(ClientLevel worldObj, double posX, double posY, double posZ, float size, int type, boolean shrink, boolean clip, float gravity) {

        if (!checkPlatformClient()) {
            return;
        }
        FXWisp ef = new FXWisp(
                worldObj,
                posX,
                posY,
                posZ,
                size,
                type
        );
        ef.setGravity(gravity);
        ef.shrink = shrink;
        ef.setNoClip(clip);
        Minecraft.getInstance().particleEngine.add(ef);

    }

    public static void wispFXEG(ClientLevel worldObj, double posX, double posY, double posZ, Entity target) {

        if (!checkPlatformClient()) {
            return;
        }
        for (int a = 0; a < particleCount(1); ++a) {
            FXWispEG ef = new FXWispEG(
                    worldObj,
                    posX,
                    posY,
                    posZ,
                    target
            );
            Minecraft.getInstance().particleEngine.add(ef);

        }

    }

    public static void wispFX3(ClientLevel worldObj, BlockPos fromPos, BlockPos toPos, float size, int type, boolean shrink, float gravity){
        wispFX3(worldObj,
                fromPos.getX(),
                fromPos.getY(),
                fromPos.getZ(),
                toPos.getX(),
                toPos.getY(),
                toPos.getZ(),
                size,
                type,
                shrink,
                gravity
                );
    }
    public static void wispFX3(ClientLevel worldObj, double posX, double posY, double posZ, double posX2, double posY2, double posZ2, float size, int type, boolean shrink, float gravity) {
        if (!checkPlatformClient()) {
            return;
        }
        FXWisp ef = new FXWisp(
                worldObj,
                posX,
                posY,
                posZ,
                posX2,
                posY2,
                posZ2,
                size,
                type
        );
        ef.setGravity(gravity);
        ef.shrink = shrink;
        Minecraft.getInstance().particleEngine.add(ef);
    }

    public static void wispFX4(ClientLevel worldObj, double posX, double posY, double posZ, Entity target, int type, boolean shrink, float gravity) {
        if (!checkPlatformClient()) {
            return;
        }
        FXWisp ef = new FXWisp(
                worldObj,
                posX,
                posY,
                posZ,
                target,
                type
        );
        ef.setGravity(gravity);
        ef.shrink = shrink;
        Minecraft.getInstance().particleEngine.add(ef);

    }

    public static void burst(ClientLevel worldObj, double sx, double sy, double sz, float size) {
        if (!checkPlatformClient()) {
            return;
        }
        FXBurst ef = new FXBurst(
                worldObj,
                sx,
                sy,
                sz,
                size
        );
        Minecraft.getInstance().particleEngine.add(ef);
    }

    public static void sourceStreamFX(ClientLevel worldObj, double sx, double sy, double sz, float tx, float ty, float tz, int tagColor) {
        if (!checkPlatformClient()) {
            return;
        }
        Color c = new Color(tagColor);
        FXWispArcing ef = new FXWispArcing(
                worldObj,
                tx,
                ty,
                tz,
                sx,
                sy,
                sz,
                0.1F,
                (float) c.getRed() / 255.0F,
                (float) c.getGreen() / 255.0F,
                (float) c.getBlue() / 255.0F
        );
        ef.setGravity(0.0F);
        Minecraft.getInstance().particleEngine.add(ef);

    }

    public static void bolt(ClientLevel worldObj, Entity sourceEntity, Entity targetedEntity) {
        if (!checkPlatformClient()) {
            return;
        }
        FXLightningBolt bolt = new FXLightningBolt(
                worldObj,
                sourceEntity,
                targetedEntity,
                worldObj.getRandom().nextLong(),
                4
        );
        bolt.defaultFractal();
        bolt.setType(0);
        bolt.finalizeBolt();
    }

    public static void nodeBolt(ClientLevel worldObj, float x, float y, float z, Entity targetedEntity) {
        if (!checkPlatformClient()) {
            return;
        }
        FXLightningBolt bolt = new FXLightningBolt(
                worldObj,
                x,
                y,
                z,
                targetedEntity.getX(),
                targetedEntity.getY(),
                targetedEntity.getZ(),
                worldObj.getRandom().nextLong(),
                10,
                4.0F,
                5
        );
        bolt.defaultFractal();
        bolt.setType(3);
        bolt.finalizeBolt();
    }

    public static void nodeBolt(ClientLevel worldObj, float x, float y, float z, float x2, float y2, float z2) {
        if (!checkPlatformClient()) {
            return;
        }
        FXLightningBolt bolt = new FXLightningBolt(
                worldObj,
                x,
                y,
                z,
                x2,
                y2,
                z2,
                worldObj.getRandom().nextLong(),
                10,
                4.0F,
                5
        );
        bolt.defaultFractal();
        bolt.setType(0);
        bolt.finalizeBolt();
    }

    public static void excavateFX(BlockPos pos, LivingEntity living, ResourceLocation block, int progress){
        excavateFX(pos.getX(),pos.getY(),pos.getZ(),living,block,progress);
    }
    public static void excavateFX(int x, int y, int z, LivingEntity living, ResourceLocation block, int progress) {
        if (!checkPlatformClient()) {
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        LevelRenderer renderer = mc.levelRenderer;

        renderer.destroyBlockProgress(
                living.getId(),                 // breakerId（实体 id）
                new BlockPos(x, y, z),     // BlockPos
                progress                  // 0–9
        );

    }

    public static void beam(ClientLevel worldObj, double sx, double sy, double sz, double tx, double ty, double tz, int type, int color, boolean reverse, float endmod, int age) {
        if (!checkPlatformClient()) {
            return;
        }
        FXBeam beamcon = null;
        Color c = new Color(color);
        beamcon = new FXBeam(
                worldObj,
                sx,
                sy,
                sz,
                tx,
                ty,
                tz,
                (float) c.getRed() / 255.0F,
                (float) c.getGreen() / 255.0F,
                (float) c.getBlue() / 255.0F,
                age
        );
        beamcon.setType(type);
        beamcon.setEndMod(endmod);
        beamcon.setReverse(reverse);
        beamcon.setPulse(false);
        Minecraft.getInstance().particleEngine.add(beamcon);
    }

    public static FXBeamWand beamCont(ClientLevel worldObj, LivingEntity living, double tx, double ty, double tz, int type, int color, boolean reverse, float endmod, Object input, int impact) {
        if (!checkPlatformClient()) {
            throw new RuntimeException("not avaliable in server");
        }
        FXBeamWand beamcon = null;
        Color c = new Color(color);
        if (input instanceof FXBeamWand) {
            beamcon = (FXBeamWand) input;
        }

        if (beamcon != null && !beamcon.isDead()) {
            beamcon.updateBeam(
                    tx,
                    ty,
                    tz
            );
            beamcon.setEndMod(endmod);
            beamcon.impact = impact;
        } else {
            beamcon = new FXBeamWand(
                    worldObj,
                    living,
                    tx,
                    ty,
                    tz,
                    (float) c.getRed() / 255.0F,
                    (float) c.getGreen() / 255.0F,
                    (float) c.getBlue() / 255.0F,
                    8
            );
            beamcon.setType(type);
            beamcon.setEndMod(endmod);
            beamcon.setReverse(reverse);
            Minecraft.getInstance().particleEngine.add(beamcon);
        }

        return beamcon;
    }

    public static FXBeamBore beamBore(ClientLevel worldObj, double px, double py, double pz, double tx, double ty, double tz, int type, int color, boolean reverse, float endmod, Object input, int impact) {

        if (!checkPlatformClient()) {
            throw new RuntimeException("not avaliable in server");
        }
        FXBeamBore beamcon = null;
        Color c = new Color(color);
        if (input instanceof FXBeamBore) {
            beamcon = (FXBeamBore) input;
        }

        if (beamcon != null && !beamcon.isDead()) {
            beamcon.updateBeam(
                    tx,
                    ty,
                    tz
            );
            beamcon.setEndMod(endmod);
            beamcon.impact = impact;
        } else {
            beamcon = new FXBeamBore(
                    worldObj,
                    px,
                    py,
                    pz,
                    tx,
                    ty,
                    tz,
                    (float) c.getRed() / 255.0F,
                    (float) c.getGreen() / 255.0F,
                    (float) c.getBlue() / 255.0F,
                    8
            );
            beamcon.setType(type);
            beamcon.setEndMod(endmod);
            beamcon.setReverse(reverse);
            Minecraft.getInstance().particleEngine.add(beamcon);
        }

        return beamcon;
    }

    public static void boreDigFx(ClientLevel worldObj, int x, int y, int z, int x2, int y2, int z2, Block bi) {
        if (!checkPlatformClient()) {
            return;
        }
        if (worldObj.getRandom().nextInt(10) == 0) {
            FXBoreSparkle fb = new FXBoreSparkle(
                    worldObj,
                    (float) x + worldObj.getRandom().nextFloat(),
                    (float) y + worldObj.getRandom().nextFloat(),
                    (float) z + worldObj.getRandom().nextFloat(),
                    (double) x2 + (double) 0.5F,
                    (double) y2 + (double) 0.5F,
                    (double) z2 + (double) 0.5F
            );
            Minecraft.getInstance().particleEngine.add(fb);

        } else {
            Direction randomDir = Direction.values()[worldObj.getRandom().nextInt(Direction.values().length)];
            FXBoreParticles fb = (new FXBoreParticles(
                    worldObj,
                    (float) x + worldObj.getRandom().nextFloat(),
                    (float) y + worldObj.getRandom().nextFloat(),
                    (float) z + worldObj.getRandom().nextFloat(),
                    (double) x2 + (double) 0.5F,
                    (double) y2 + (double) 0.5F,
                    (double) z2 + (double) 0.5F,
                    bi,
                    randomDir
            )).applyColourMultiplier(
                    x,
                    y,
                    z
            );
            Minecraft.getInstance().particleEngine.add(fb);
        }

    }

    public static void essentiaTrailFx(ClientLevel worldObj, int x, int y, int z, int x2, int y2, int z2, int count, int color, float scale) {
        if (!checkPlatformClient()) {
            return;
        }
        FXEssentiaTrail fb = new FXEssentiaTrail(
                worldObj,
                (double) x + (double) 0.5F,
                (double) y + (double) 0.5F,
                (double) z + (double) 0.5F,
                (double) x2 + (double) 0.5F,
                (double) y2 + (double) 0.5F,
                (double) z2 + (double) 0.5F,
                count,
                color,
                scale
        );
        Minecraft.getInstance().particleEngine.add(fb);

    }

    public static void soulTrail(ClientLevel world, Entity source, Entity target, float r, float g, float b) {
        if (!checkPlatformClient()) {
            return;
        }
        var boundingBox = source.getBoundingBox();
        var entityWidth = boundingBox.maxZ - boundingBox.minZ;
        var entityHeight = boundingBox.maxY - boundingBox.minY;
        for (int a = 0; a < particleCount(2); ++a) {
            if (world.getRandom().nextInt(10) == 0) {
                FXSparkleTrail st = new FXSparkleTrail(
                        world,
                        source.getX() - (double) (entityWidth / 2.0F) + (double) (world.getRandom().nextFloat() * entityWidth),
                        source.getY() + (double) (world.getRandom().nextFloat() * entityHeight),
                        source.getZ() - (double) (entityWidth / 2.0F) + (double) (world.getRandom().nextFloat() * entityWidth),
                        target,
                        r,
                        g,
                        b
                );
                st.setNoClip(true);
                Minecraft.getInstance().particleEngine.add(st);

            } else {
                FXSmokeTrail st = new FXSmokeTrail(
                        world,
                        source.getX() - (double) (entityWidth / 2.0F) + (double) (world.getRandom().nextFloat() * entityWidth),
                        source.getY() + (double) (world.getRandom().nextFloat() * entityHeight),
                        source.getZ() - (double) (entityWidth / 2.0F) + (double) (world.getRandom().nextFloat() * entityWidth),
                        target,
                        r,
                        g,
                        b
                );
                st.setNoClip(true);
                Minecraft.getInstance().particleEngine.add(st);

            }
        }

    }

    public static int particleCount(int base) {
        if (!checkPlatformClient()) {
            throw new RuntimeException("not avaliable in server");
        }
        ParticleStatus status = Minecraft.getInstance().options.particles().get();

        return switch (status) {
            case ALL -> base * 2;      // 全部：多一点
            case DECREASED -> base;    // 少：正常
            case MINIMAL -> base / 2;  // 最少：极少（不是 0）
        };
    }

    public static void furnaceLavaFx(ClientLevel level, int x, int y, int z, int facingX, int facingZ) {
        if (!checkPlatformClient()) {
            return;
        }
        RandomSource rand = level.random;

        double px = x + 0.5
                + (rand.nextFloat() - rand.nextFloat()) * 0.3
                + facingX * 0.5;
        double py = y + 0.3;
        double pz = z + 0.5
                + (rand.nextFloat() - rand.nextFloat()) * 0.3
                + facingZ * 0.5;

        double vy = 0.2 * rand.nextFloat();

        double vx = facingX == 0
                ? (rand.nextFloat() - rand.nextFloat()) * 0.15
                : facingX * rand.nextFloat() * 0.15;
        double vz = facingZ == 0
                ? (rand.nextFloat() - rand.nextFloat()) * 0.15
                : facingZ * rand.nextFloat() * 0.15;

        level.addParticle(
                ParticleTypes.LAVA,
                px, py, pz,
                vx, vy, vz
        );
    }

    public static void blockRunes(Level world, double x, double y, double z, float r, float g, float b, int dur, float grav) {
        if (!(world instanceof ClientLevel clientLevel)) {return;}
        FXBlockRunes fb = new FXBlockRunes(clientLevel, x + (double)0.5F, y + (double)0.5F, z + (double)0.5F, r, g, b, dur);
        fb.setGravity(grav);
        Minecraft.getInstance().particleEngine.add(fb);

    }
    public static void blockRunes(ClientLevel world, double x, double y, double z, float r, float g, float b, int dur, float grav) {
        if (!checkPlatformClient()) {
            return;
        }

        FXBlockRunes fb = new FXBlockRunes(
                world,
                x + (double) 0.5F,
                y + (double) 0.5F,
                z + (double) 0.5F,
                r,
                g,
                b,
                dur
        );
        fb.setGravity(grav);
        Minecraft.getInstance().particleEngine.add(fb);

    }

    public static void blockWard(ClientLevel world, double x, double y, double z, Direction side, float f, float f1, float f2) {
        if (!checkPlatformClient()) {
            return;
        }
        FXBlockWard fb = new FXBlockWard(
                world,
                x + (double) 0.5F,
                y + (double) 0.5F,
                z + (double) 0.5F,
                side,
                f,
                f1,
                f2
        );
        Minecraft.getInstance().particleEngine.add(fb);
    }

    public static FXSwarm swarmParticleFX(ClientLevel worldObj, Entity targetedEntity, float f1, float f2, float pg) {
        if (!checkPlatformClient()) {
            throw new RuntimeException("not avaliable in server");
        }
        FXSwarm fx = new FXSwarm(
                worldObj,
                targetedEntity.getX() + (double) ((worldObj.getRandom().nextFloat() - worldObj.getRandom().nextFloat()) * 2.0F),
                targetedEntity.getY() + (double) ((worldObj.getRandom().nextFloat() - worldObj.getRandom().nextFloat()) * 2.0F),
                targetedEntity.getZ() + (double) ((worldObj.getRandom().nextFloat() - worldObj.getRandom().nextFloat()) * 2.0F),
                targetedEntity,
                0.8F + worldObj.getRandom().nextFloat() * 0.2F,
                worldObj.getRandom().nextFloat() * 0.4F,
                1.0F - worldObj.getRandom().nextFloat() * 0.2F,
                f1,
                f2,
                pg
        );
        Minecraft.getInstance().particleEngine.add(fx);

        return fx;
    }

    public static void splooshFX(Entity e) {
        if (!checkPlatformClient()) {
            return;
        }
        float f = e.level().getRandom().nextFloat() * (float) Math.PI * 2.0F;
        float f1 = e.level().getRandom().nextFloat() * 0.5F + 0.5F;
        float f2 = MathHelper.sin(f) * 2.0F * 0.5F * f1;
        float f3 = MathHelper.cos(f) * 2.0F * 0.5F * f1;
        var boundingBox = e.getBoundingBox();
        var entityHeight = boundingBox.maxY - boundingBox.minY;
        FXBreaking fx = new FXBreaking(
                (ClientLevel) e.level(),
                e.getX() + (double) f2,
                e.getY() + (double) (e.level().getRandom().nextFloat() * entityHeight),
                e.getZ() + (double) f3,
                Items.SLIME_BALL
        );
        if (e.level().getRandom().nextBoolean()) {
            fx.setRBGColorF(
                    0.6F,
                    0.0F,
                    0.3F
            );
            fx.setAlphaF(0.4F);
        } else {
            fx.setRBGColorF(
                    0.3F,
                    0.0F,
                    0.3F
            );
            fx.setAlphaF(0.6F);
        }

        fx.setParticleMaxAge((int) (66.0F / (e.level().getRandom().nextFloat() * 0.9F + 0.1F)));
        Minecraft.getInstance().particleEngine.add(fx);
    }

    public static void splooshFX(ClientLevel worldObj, int x, int y, int z) {
        if (!checkPlatformClient()) {
            return;
        }
        float f = worldObj.getRandom().nextFloat() * (float) Math.PI * 2.0F;
        float f1 = worldObj.getRandom().nextFloat() * 0.5F + 0.5F;
        float f2 = MathHelper.sin(f) * 2.0F * 0.5F * f1;
        float f3 = MathHelper.cos(f) * 2.0F * 0.5F * f1;
        FXBreaking fx = new FXBreaking(
                worldObj,
                (double) x + (double) f2 + (double) 0.5F,
                (float) y + worldObj.getRandom().nextFloat(),
                (double) z + (double) f3 + (double) 0.5F,
                Items.SLIME_BALL
        );
        if (worldObj.getRandom().nextBoolean()) {
            fx.setRBGColorF(
                    0.6F,
                    0.0F,
                    0.3F
            );
            fx.setAlphaF(0.4F);
        } else {
            fx.setRBGColorF(
                    0.3F,
                    0.0F,
                    0.3F
            );
            fx.setAlphaF(0.6F);
        }

        fx.setParticleMaxAge((int) (66.0F / (worldObj.getRandom().nextFloat() * 0.9F + 0.1F)));
        Minecraft.getInstance().particleEngine.add(fx);
    }

    public static void taintsplosionFX(Entity e) {
        if (!checkPlatformClient()) {
            return;
        }
        var boundingBox = e.getBoundingBox();
        var entityHeight = boundingBox.maxY - boundingBox.minY;
        FXBreaking fx = new FXBreaking(
                (ClientLevel) e.level(),
                e.getX(),
                e.getY() + (double) (e.level().getRandom().nextFloat() * entityHeight),
                e.getZ(),
                Items.SLIME_BALL
        );
        if (e.level().getRandom().nextBoolean()) {
            fx.setRBGColorF(
                    0.6F,
                    0.0F,
                    0.3F
            );
            fx.setAlphaF(0.4F);
        } else {
            fx.setRBGColorF(
                    0.3F,
                    0.0F,
                    0.3F
            );
            fx.setAlphaF(0.6F);
        }

        fx.setXD( (Math.random() * (double) 2.0F - (double) 1.0F));
        fx.setYD( (Math.random() * (double) 2.0F - (double) 1.0F));
        fx.setZD( (Math.random() * (double) 2.0F - (double) 1.0F));
        float f = (float) (Math.random() + Math.random() + (double) 1.0F) * 0.15F;
        float f1 = MathHelper.sqrt_double(fx.getXD() * fx.getXD() + fx.getYD() * fx.getYD() + fx.getZD() * fx.getZD());
        fx.setXD(fx.getXD() / (double) f1 * (double) f * 0.9640000000596046);
        fx.setYD(fx.getYD() / (double) f1 * (double) f * 0.9640000000596046 + (double) 0.1F);
        fx.setZD(fx.getZD() / (double) f1 * (double) f * 0.9640000000596046);
        fx.setParticleMaxAge((int) (66.0F / (e.level().getRandom().nextFloat() * 0.9F + 0.1F)));
        Minecraft.getInstance().particleEngine.add(fx);
    }

    public static void tentacleAriseFX(Entity e) {
        if (!checkPlatformClient()) {
            return;
        }
        int xx = MathHelper.floor_double(e.getX());
        int yy = MathHelper.floor_double(e.getY()) - 1;
        int zz = MathHelper.floor_double(e.getZ());

        var boundingBox = e.getBoundingBox();
        var entityHeight = (float) (boundingBox.maxY - boundingBox.minY);
        for (int j = 0; (float) j < 2.0F * entityHeight; ++j) {
            float f = (float) (e.level().getRandom().nextFloat() * (float) Math.PI * entityHeight);
            float f1 = e.level().getRandom().nextFloat() * 0.5F + 0.5F;
            float f2 = MathHelper.sin(f) * entityHeight * 0.25F * f1;
            float f3 = MathHelper.cos(f) * entityHeight * 0.25F * f1;
            FXBreaking fx = new FXBreaking(
                    (ClientLevel) e.level(),
                    e.getX() + (double) f2,
                    e.getY(),
                    e.getZ() + (double) f3,
                    Items.SLIME_BALL
            );
            fx.setRBGColorF(
                    0.4F,
                    0.0F,
                    0.4F
            );
            fx.setAlphaF(0.5F);
            fx.setParticleMaxAge((int) (66.0F / (e.level().getRandom().nextFloat() * 0.9F + 0.1F)));
            Minecraft.getInstance().particleEngine.add(fx);
            var bstate = e.level().getBlockState(new BlockPos(xx,yy,zz));
            if (!bstate.isAir()) {
                f = e.level().getRandom().nextFloat() * (float) Math.PI * entityHeight;
                f1 = e.level().getRandom().nextFloat() * 0.5F + 0.5F;
                f2 = MathHelper.sin(f) * entityHeight * 0.25F * f1;
                f3 = MathHelper.cos(f) * entityHeight * 0.25F * f1;
                double px = e.getX() + f2;
                double py = e.getY();
                double pz = e.getZ() + f3;

                e.level().addParticle(
                        new BlockParticleOption(ParticleTypes.BLOCK, bstate),
                        px, py, pz,
                        0.0, 0.0, 0.0
                );
//                EntityDiggingFX fx2 = (new EntityDiggingFX(
//                        e.level(),
//                        e.posX + (double) f2,
//                        e.posY,
//                        e.posZ + (double) f3,
//                        0.0F,
//                        0.0F,
//                        0.0F,
//                        e.level().getBlock(
//                                xx,
//                                yy,
//                                zz
//                        ),
//                        e.level().getBlockMetadata(
//                                xx,
//                                yy,
//                                zz
//                        ),
//                        1
//                )).applyColourMultiplier(
//                        xx,
//                        yy,
//                        zz
//                );
//                Minecraft.getInstance().particleEngine.add(fx2);
            }
        }

    }

    public static void slimeJumpFX(Entity e, int i) {
        if (!checkPlatformClient()) {
            return;
        }
        float f = e.level().getRandom().nextFloat() * (float) Math.PI * 2.0F;
        float f1 = e.level().getRandom().nextFloat() * 0.5F + 0.5F;
        float f2 = MathHelper.sin(f) * (float) i * 0.5F * f1;
        float f3 = MathHelper.cos(f) * (float) i * 0.5F * f1;
        FXBreaking fx = new FXBreaking(
                (ClientLevel) e.level(),
                e.getX() + (double) f2,
                (e.getBoundingBox().minY + e.getBoundingBox().maxY) / (double) 2.0F,
                e.getZ() + (double) f3,
                Items.SLIME_BALL
        );
        fx.setRBGColorF(
                0.7F,
                0.0F,
                1.0F
        );
        fx.setAlphaF(0.4F);
        fx.setParticleMaxAge((int) (66.0F / (e.level().getRandom().nextFloat() * 0.9F + 0.1F)));
        Minecraft.getInstance().particleEngine.add(fx);
    }

    public static void dropletFX(ClientLevel world, float i, float j, float k, float r, float g, float b) {
        FXDrop obj = new FXDrop(
                world,
                i,
                j,
                k,
                r,
                g,
                b
        );
        Minecraft.getInstance().particleEngine.add(obj);
    }

    public static void taintLandFX(Entity e) {
        if (!checkPlatformClient()) {
            return;
        }
        float f = e.level().getRandom().nextFloat() * (float) Math.PI * 2.0F;
        float f1 = e.level().getRandom().nextFloat() * 0.5F + 0.5F;
        float f2 = MathHelper.sin(f) * 2.0F * 0.5F * f1;
        float f3 = MathHelper.cos(f) * 2.0F * 0.5F * f1;
        if (e.level().isClientSide()) {
            FXBreaking fx = new FXBreaking(
                    (ClientLevel) e.level(),
                    e.getX() + (double) f2,
                    (e.getBoundingBox().minY + e.getBoundingBox().maxY) / (double) 2.0F,
                    e.getZ() + (double) f3,
                    Items.SLIME_BALL
            );
            fx.setRBGColorF(
                    0.1F,
                    0.0F,
                    0.1F
            );
            fx.setAlphaF(0.4F);
            fx.setParticleMaxAge((int) (66.0F / (e.level().getRandom().nextFloat() * 0.9F + 0.1F)));
            Minecraft.getInstance().particleEngine.add(fx);
        }

    }

    public static void hungryNodeFX(ClientLevel worldObj, int sourceX, int sourceY, int sourceZ, int targetX, int targetY, int targetZ, Block block) {
        if (!checkPlatformClient()) {
            return;
        }
        Direction randomDirection = Direction.values()[worldObj.random.nextInt(Direction.values().length)];
        FXBoreParticles fb = (new FXBoreParticles(
                worldObj,
                (float) sourceX + worldObj.getRandom().nextFloat(),
                (float) sourceY + worldObj.getRandom().nextFloat(),
                (float) sourceZ + worldObj.getRandom().nextFloat(),
                (double) targetX + (double) 0.5F,
                (double) targetY + (double) 0.5F,
                (double) targetZ + (double) 0.5F,
                block,
                randomDirection
        )).applyColourMultiplier(
                sourceX,
                sourceY,
                sourceZ
        );
        Minecraft.getInstance().particleEngine.add(fb);
    }

    public static void drawInfusionParticles1(ClientLevel worldObj, double x, double y, double z, int x2, int y2, int z2, Item id, int md) {
        if (!checkPlatformClient()) {
            return;
        }
        Direction randomDirection = Direction.values()[worldObj.random.nextInt(Direction.values().length)];
        FXBoreParticles fb = (new FXBoreParticles(
                worldObj,
                x,
                y,
                z,
                (double) x2 + (double) 0.5F,
                (double) y2 - (double) 0.5F,
                (double) z2 + (double) 0.5F,
                id,
                randomDirection
        )).applyColourMultiplier(
                x2,
                y2,
                z2
        );
        fb.setAlphaF(0.3F);
        fb.setXD((float) worldObj.getRandom().nextGaussian() * 0.03F);
        fb.setYD((float) worldObj.getRandom().nextGaussian() * 0.03F);
        fb.setZD((float) worldObj.getRandom().nextGaussian() * 0.03F);
        Minecraft.getInstance().particleEngine.add(fb);
    }

    public static void drawInfusionParticles2(ClientLevel worldObj, double x, double y, double z, int x2, int y2, int z2, Block id, int md) {
        if (!checkPlatformClient()) {
            return;
        }
        Direction randomDirection = Direction.values()[worldObj.random.nextInt(Direction.values().length)];
        FXBoreParticles fb = (new FXBoreParticles(
                worldObj,
                x,
                y,
                z,
                (double) x2 + (double) 0.5F,
                (double) y2 - (double) 0.5F,
                (double) z2 + (double) 0.5F,
                id,
                randomDirection
        )).applyColourMultiplier(
                x2,
                y2,
                z2
        );
        fb.setAlphaF(0.3F);
        Minecraft.getInstance().particleEngine.add(fb);
    }

    public static void drawInfusionParticles3(ClientLevel worldObj, double x, double y, double z, int x2, int y2, int z2) {
        if (!checkPlatformClient()) {
            return;
        }
        FXBoreSparkle fb = new FXBoreSparkle(
                worldObj,
                x,
                y,
                z,
                (double) x2 + (double) 0.5F,
                (double) y2 - (double) 0.5F,
                (double) z2 + (double) 0.5F
        );
        fb.setRBGColorF(
                0.4F + worldObj.getRandom().nextFloat() * 0.2F,
                0.2F,
                0.6F + worldObj.getRandom().nextFloat() * 0.3F
        );
        Minecraft.getInstance().particleEngine.add(fb);

    }

    public static void drawInfusionParticles4(ClientLevel worldObj, double x, double y, double z, int x2, int y2, int z2) {
        if (!checkPlatformClient()) {
            return;
        }
        FXBoreSparkle fb = new FXBoreSparkle(
                worldObj,
                x,
                y,
                z,
                (double) x2 + (double) 0.5F,
                (double) y2 - (double) 0.5F,
                (double) z2 + (double) 0.5F
        );
        fb.setRBGColorF(
                0.2F,
                0.6F + worldObj.getRandom().nextFloat() * 0.3F,
                0.3F
        );
        Minecraft.getInstance().particleEngine.add(fb);

    }

    public static void drawVentParticles(ClientLevel worldObj, double x, double y, double z, double x2, double y2, double z2, int color) {
        if (!checkPlatformClient()) {
            return;
        }
        FXVent fb = new FXVent(
                worldObj,
                x,
                y,
                z,
                x2,
                y2,
                z2,
                color
        );
        fb.setAlphaF(0.4F);
        Minecraft.getInstance().particleEngine.add(fb);

    }

    public static void drawGenericParticles(ClientLevel worldObj, double x, double y, double z, double x2, double y2, double z2, float r, float g, float b, float alpha, boolean loop, int start, int num, int inc, int age, int delay, float scale) {
        if (!checkPlatformClient()) {
            return;
        }
        FXGeneric fb = new FXGeneric(
                worldObj,
                x,
                y,
                z,
                x2,
                y2,
                z2
        );
        fb.setMaxAge(
                age,
                delay
        );
        fb.setRBGColorF(
                r,
                g,
                b
        );
        fb.setAlphaF(alpha);
        fb.setLoop(loop);
        fb.setParticles(
                start,
                num,
                inc
        );
        fb.setScale(scale);
        Minecraft.getInstance().particleEngine.add(fb);

    }

    public static void drawVentParticles(ClientLevel worldObj, double x, double y, double z, double x2, double y2, double z2, int color, float scale) {
        if (!checkPlatformClient()) {
            return;
        }
        FXVent fb = new FXVent(
                worldObj,
                x,
                y,
                z,
                x2,
                y2,
                z2,
                color
        );
        fb.setAlphaF(0.4F);
        fb.setScale(scale);
        Minecraft.getInstance().particleEngine.add(fb);
    }

    public static FXBeamPower beamPower(ClientLevel worldObj, double px, double py, double pz, double tx, double ty, double tz, float r, float g, float b, boolean pulse, Object input) {
        if (!checkPlatformClient()) {
            throw new RuntimeException("not avaliable in server");
        }
        FXBeamPower beamcon = null;
        if (input instanceof FXBeamPower) {
            beamcon = (FXBeamPower) input;
        }

        if (beamcon != null && !beamcon.isDead()) {
            beamcon.updateBeam(
                    px,
                    py,
                    pz,
                    tx,
                    ty,
                    tz
            );
            beamcon.setPulse(
                    pulse,
                    r,
                    g,
                    b
            );
        } else {
            beamcon = new FXBeamPower(
                    worldObj,
                    px,
                    py,
                    pz,
                    tx,
                    ty,
                    tz,
                    r,
                    g,
                    b,
                    8
            );
            Minecraft.getInstance().particleEngine.add(beamcon);
        }

        return beamcon;
    }
}
