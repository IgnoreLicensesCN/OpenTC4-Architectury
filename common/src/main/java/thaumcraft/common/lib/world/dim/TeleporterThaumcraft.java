package thaumcraft.common.lib.world.dim;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.util.LongHashMap;
import com.linearity.opentc4.utils.vanilla1710.MathHelper;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;
import thaumcraft.common.config.ConfigBlocks;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class TeleporterThaumcraft extends Teleporter {
    private final WorldServer worldServerInstance;
    private final Random random;
    private static final LongHashMap destinationCoordinateCache = new LongHashMap();
    private static final List<Long> destinationCoordinateKeys = new ArrayList<>();

    public TeleporterThaumcraft(ServerLevel par1WorldServer) {
        super(par1WorldServer);
        this.worldServerInstance = par1WorldServer;
        this.random = new Random(par1WorldServer.getSeed());
    }

    @Override
    public void placeInPortal(Entity par1Entity, double par2, double par4, double par6, float par8) {
        if (this.worldServerInstance.dimension() != 1/*TheEnd*/) {
            if (!this.placeInExistingPortal(par1Entity, par2, par4, par6, par8)) {
                this.makePortal(par1Entity);
                this.placeInExistingPortal(par1Entity, par2, par4, par6, par8);
            }
        } else if (!this.placeInExistingPortal(par1Entity, par2, par4, par6, par8)) {
            int i = MathHelper.floor_double(par1Entity.posX);
            int k = MathHelper.floor_double(par1Entity.posZ);
            int j = this.worldServerInstance.getHeight(Heightmap.Types.WORLD_SURFACE,i, k);
            byte b0 = 1;
            byte b1 = 0;
            par1Entity.setLocationAndAngles(i, (double) j + (double) 4.0F, k, par1Entity.rotationYaw, 0.0F);
            par1Entity.motionX = par1Entity.motionY = par1Entity.motionZ = 0.0F;
        }

    }

    @Override
    public boolean placeInExistingPortal(Entity par1Entity, double par2, double par4, double par6, float par8) {
        short short1 = 128;
        double d3 = -1.0F;
        int i = 0;
        int j = 0;
        int k = 0;
        int l = MathHelper.floor_double(par1Entity.posX);
        int i1 = MathHelper.floor_double(par1Entity.posZ);
        int chunkX = l >> 4;
        int chunkZ = i1 >> 4;
        String hs = chunkX + ":" + chunkZ + ":" + this.worldServerInstance.dimension();
        long j1 = hs.hashCode();
        boolean flag = true;
        if (destinationCoordinateCache.containsItem(j1)) {
            Teleporter.PortalPosition portalposition = (Teleporter.PortalPosition) destinationCoordinateCache.getValueByKey(j1);
            d3 = 0.0F;
            i = portalposition.posX;
            j = portalposition.posY;
            k = portalposition.posZ;
            portalposition.lastUpdateTime = this.worldServerInstance.getTotalWorldTime();
            flag = false;
        } else {
            for (int k1 = l - short1; k1 <= l + short1; ++k1) {
                double d5 = (double) k1 + (double) 0.5F - par1Entity.posX;

                for (int l1 = i1 - short1; l1 <= i1 + short1; ++l1) {
                    double d6 = (double) l1 + (double) 0.5F - par1Entity.posZ;

                    for (int i2 = this.worldServerInstance.getActualHeight() - 1; i2 >= 0; --i2) {
                        if (this.worldServerInstance.getBlock(k1, i2, l1) == ConfigBlocks.blockEldritchPortal) {
                            double d4 = (double) i2 + (double) 0.5F - par1Entity.posY;
                            double d7 = d5 * d5 + d4 * d4 + d6 * d6;
                            if (d3 < (double) 0.0F || d7 < d3) {
                                d3 = d7;
                                i = k1;
                                j = i2;
                                k = l1;
                            }
                        }
                    }
                }
            }
        }

        if (d3 >= 0.0) {
            if (flag) {
                //TODO:Find out why 'this' arg appears
                destinationCoordinateCache.add(j1, new Teleporter.PortalPosition(i, j, k, this.worldServerInstance.getTotalWorldTime()));
//            destinationCoordinateCache.add(j1, new Teleporter.PortalPosition(this, i, j, k, this.worldServerInstance.getTotalWorldTime()));
                destinationCoordinateKeys.add(j1);
            }

            double d8 = (double) i + (double) 0.5F + (double) (this.worldServerInstance.rand.nextBoolean() ? 1 : -1);
            double d4 = (double) k + (double) 0.5F + (double) (this.worldServerInstance.rand.nextBoolean() ? 1 : -1);
            par1Entity.motionX = par1Entity.motionY = par1Entity.motionZ = 0.0F;
            par1Entity.setLocationAndAngles(d8, j, d4, par1Entity.rotationYaw, par1Entity.rotationPitch);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean makePortal(Entity par1Entity) {
        return true;
    }

    @Override
    public void removeStalePortalLocations(long par1) {
        if (par1 % 100L == 0L) {
            Iterator<Long> iterator = destinationCoordinateKeys.iterator();
            long j = par1 - 600L;

            while (iterator.hasNext()) {
                Long olong = iterator.next();
                Teleporter.PortalPosition portalposition = (Teleporter.PortalPosition) destinationCoordinateCache.getValueByKey(olong);
                if (portalposition == null || portalposition.lastUpdateTime < j) {
                    iterator.remove();
                    destinationCoordinateCache.remove(olong);
                }
            }
        }

    }
}
