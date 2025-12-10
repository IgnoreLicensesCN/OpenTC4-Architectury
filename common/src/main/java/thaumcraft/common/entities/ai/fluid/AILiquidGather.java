package thaumcraft.common.entities.ai.fluid;

import net.minecraft.block.Block;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;
import tc4tweak.ConfigurationHandler;
import thaumcraft.common.entities.golems.EntityGolemBase;
import thaumcraft.common.entities.golems.GolemHelper;
import thaumcraft.common.entities.golems.Marker;

import java.util.*;

public class AILiquidGather extends EntityAIBase {
    private EntityGolemBase theGolem;
    private int waterX;
    private int waterY;
    private int waterZ;
    private ForgeDirection markerOrientation;
    private World theWorld;
    private float pumpDist = 0.0F;
    int count = 0;
    HashMap<ChunkCoordinates, ArrayList<SourceBlock>> queue = new HashMap<>();
    ArrayList<ChunkCoordinates> cache = new ArrayList<>();
    ChunkCoordinates origin = null;

    public AILiquidGather(EntityGolemBase par1EntityCreature) {
        this.theGolem = par1EntityCreature;
        this.theWorld = par1EntityCreature.level();
        this.setMutexBits(3);
    }

    public boolean shouldExecute() {
        ArrayList<FluidStack> fluids = GolemHelper.getMissingLiquids(this.theGolem);
        if (fluids == null) {
            return false;
        } else if (this.theGolem.itemWatched != null && !fluids.isEmpty() && this.theGolem.getNavigator().noPath()) {
            ForgeDirection facing = ForgeDirection.getOrientation(this.theGolem.homeFacing);
            ChunkCoordinates home = this.theGolem.getHomePosition();
            int var10000 = home.posX - facing.offsetX;
            var10000 = home.posY - facing.offsetY;
            var10000 = home.posZ - facing.offsetZ;
            int camt = 0;
            if (this.theGolem.fluidCarried != null) {
                camt = this.theGolem.fluidCarried.amount;
            }

            int max = this.theGolem.getFluidCarryLimit();

            for (FluidStack fluid : fluids) {
                for (Marker marker : GolemHelper.getMarkedFluidHandlersAdjacentToGolem(fluid, this.theWorld, this.theGolem)) {
                    TileEntity te = this.theWorld.getTileEntity(marker.x, marker.y, marker.z);
                    if (te instanceof IFluidHandler) {
                        FluidStack fs = ((IFluidHandler) te).drain(ForgeDirection.getOrientation(marker.side), new FluidStack(fluid.getFluid(), max - camt), false);
                        if (fs != null && fs.amount > 0) {
                            return true;
                        }
                    }
                }

                for (ChunkCoordinates loc : GolemHelper.getMarkedBlocksAdjacentToGolem(this.theWorld, this.theGolem, (byte) -1)) {
                    Block bi = this.theWorld.getBlock(loc.posX, loc.posY, loc.posZ);
                    if (FluidRegistry.getFluid(fluid.getFluidID()).getBlock() == bi) {
                        if (bi instanceof IFluidBlock && ((IFluidBlock) bi).canDrain(this.theWorld, loc.posX, loc.posY, loc.posZ)) {
                            FluidStack fs = ((IFluidBlock) bi).drain(this.theWorld, loc.posX, loc.posY, loc.posZ, false);
                            return fs != null && fs.amount <= max - camt;
                        }

                        if (fluid.getFluidID() == FluidRegistry.WATER.getID() || fluid.getFluidID() == FluidRegistry.LAVA.getID()) {
                            return this.theWorld.getBlockMetadata(loc.posX, loc.posY, loc.posZ) == 0;
                        }
                    }
                }
            }

            return false;
        } else {
            return false;
        }
    }

    public boolean continueExecuting() {
        return this.count < 20 && this.theGolem.itemWatched != null;
    }

    public boolean isInterruptible() {
        return false;
    }

    public void startExecuting() {
        this.count = 0;
    }

    public void resetTask() {
        this.count = 0;
        this.theGolem.itemWatched = null;
        super.resetTask();
    }

    public void updateTask() {
        ++this.count;
        if (this.count >= 10) {
            ForgeDirection facing = ForgeDirection.getOrientation(this.theGolem.homeFacing);
            ChunkCoordinates home = this.theGolem.getHomePosition();
            int var10000 = home.posX - facing.offsetX;
            var10000 = home.posY - facing.offsetY;
            var10000 = home.posZ - facing.offsetZ;
            int camt = 0;
            if (this.theGolem.fluidCarried != null) {
                camt = this.theGolem.fluidCarried.amount;
            }

            int max = this.theGolem.getFluidCarryLimit();
            ArrayList<FluidStack> fluids = GolemHelper.getMissingLiquids(this.theGolem);
            if (fluids != null) {
                for (FluidStack fluidstack : fluids) {
                    for (Marker marker : GolemHelper.getMarkedFluidHandlersAdjacentToGolem(fluidstack, this.theWorld, this.theGolem)) {
                        TileEntity te = this.theWorld.getTileEntity(marker.x, marker.y, marker.z);
                        if (te instanceof IFluidHandler) {
                            FluidStack fs = ((IFluidHandler) te).drain(ForgeDirection.getOrientation(marker.side), new FluidStack(fluidstack.getFluid(), max - camt), true);
                            if (fs != null && fs.amount > 0) {
                                if (this.theGolem.fluidCarried != null) {
                                    FluidStack var31 = this.theGolem.fluidCarried;
                                    var31.amount += fs.amount;
                                } else {
                                    this.theGolem.fluidCarried = fs.copy();
                                }

                                if (fs.amount > 200) {
                                    this.theWorld.playSoundAtEntity(this.theGolem, "game.neutral.swim", 0.2F * ((float) fs.amount / (float) max), 1.0F + (this.theworld.getRandom().nextFloat() - this.theworld.getRandom().nextFloat()) * 0.3F);
                                }

                                this.theGolem.updateCarried();
                                if (this.theGolem.fluidCarried.amount >= this.theGolem.getFluidCarryLimit()) {
                                    this.theGolem.itemWatched = null;
                                }

                                this.count = 0;
                            }
                        }
                    }

                    for (ChunkCoordinates loc : GolemHelper.getMarkedBlocksAdjacentToGolem(this.theWorld, this.theGolem, (byte) -1)) {
                        Block bi = this.theWorld.getBlock(loc.posX, loc.posY, loc.posZ);
                        this.theWorld.getBlockMetadata(loc.posX, loc.posY, loc.posZ);
                        int i = loc.posX;
                        int j = loc.posY;
                        int k = loc.posZ;
                        if (this.theGolem.getUpgradeAmount(5) > 0) {
                            if (!this.queue.containsKey(loc) || this.queue.get(loc).isEmpty()) {
                                this.rebuildQueue(loc, fluidstack.getFluid());
                            }

                            if (this.queue.containsKey(loc) && !this.queue.get(loc).isEmpty()) {
                                ArrayList<SourceBlock> t = this.queue.get(loc);

                                do {
                                    ChunkCoordinates current = t.get(0).loc;
                                    i = current.posX;
                                    j = current.posY;
                                    k = current.posZ;
                                    t.remove(0);
                                } while (!t.isEmpty() && !this.validFluidBlock(fluidstack.getFluid(), i, j, k));

                                this.queue.put(loc, t);
                            }
                        }

                        if (FluidRegistry.getFluid(fluidstack.getFluidID()).getBlock() == bi) {
                            if (bi instanceof BlockFluidBase && ((IFluidBlock) bi).canDrain(this.theWorld, i, j, k)) {
                                FluidStack fs = ((IFluidBlock) bi).drain(this.theWorld, i, j, k, false);
                                if (fs != null && fs.amount <= max - camt) {
                                    ((IFluidBlock) bi).drain(this.theWorld, i, j, k, true);
                                    if (this.theGolem.fluidCarried != null) {
                                        FluidStack var33 = this.theGolem.fluidCarried;
                                        var33.amount += fs.amount;
                                    } else {
                                        this.theGolem.fluidCarried = fs.copy();
                                    }

                                    this.theWorld.setBlockToAir(i, j, k);
                                    this.theWorld.playSoundAtEntity(this.theGolem, "game.neutral.swim", 0.2F, 1.0F + (this.theworld.getRandom().nextFloat() - this.theworld.getRandom().nextFloat()) * 0.3F);
                                    this.theGolem.updateCarried();
                                    if (this.theGolem.fluidCarried.amount > this.theGolem.getFluidCarryLimit() - 1000) {
                                        this.theGolem.itemWatched = null;
                                    }

                                    this.count = 0;
                                }
                            } else if (fluidstack.getFluidID() == FluidRegistry.WATER.getID() || fluidstack.getFluidID() == FluidRegistry.LAVA.getID()) {
                                int wmd = this.theWorld.getBlockMetadata(i, j, k);
                                if ((FluidRegistry.lookupFluidForBlock(bi) == FluidRegistry.WATER && fluidstack.getFluidID() == FluidRegistry.WATER.getID() || FluidRegistry.lookupFluidForBlock(bi) == FluidRegistry.LAVA && fluidstack.getFluidID() == FluidRegistry.LAVA.getID()) && wmd == 0) {
                                    FluidStack fs = new FluidStack(fluidstack.getFluidID(), 1000);
                                    if (this.theGolem.fluidCarried != null) {
                                        FluidStack var32 = this.theGolem.fluidCarried;
                                        var32.amount += fs.amount;
                                    } else {
                                        this.theGolem.fluidCarried = fs.copy();
                                    }

                                    this.theWorld.setBlockToAir(i, j, k);
                                    this.theWorld.playSoundAtEntity(this.theGolem, "game.neutral.swim", 0.2F, 1.0F + (this.theworld.getRandom().nextFloat() - this.theworld.getRandom().nextFloat()) * 0.3F);
                                    this.theGolem.updateCarried();
                                    if (this.theGolem.fluidCarried.amount > this.theGolem.getFluidCarryLimit() - 1000) {
                                        this.theGolem.itemWatched = null;
                                    }

                                    this.count = 0;
                                }
                            }
                        }
                    }
                }

            }
        }
    }

    private boolean validFluidBlock(Fluid fluid, int i, int j, int k) {
        Block bi = this.theWorld.getBlock(i, j, k);
        if (FluidRegistry.lookupFluidForBlock(bi) != fluid) {
            return false;
        } else {
            if (bi instanceof BlockFluidBase && ((IFluidBlock) bi).canDrain(this.theWorld, i, j, k)) {
                FluidStack fs = ((IFluidBlock) bi).drain(this.theWorld, i, j, k, false);
                if (fs != null) {
                    return true;
                }
            }

            return (FluidRegistry.lookupFluidForBlock(bi) == FluidRegistry.WATER && fluid == FluidRegistry.WATER || FluidRegistry.lookupFluidForBlock(bi) == FluidRegistry.LAVA && fluid == FluidRegistry.LAVA) && this.theWorld.getBlockMetadata(i, j, k) == 0;
        }
    }

    private void rebuildQueue(ChunkCoordinates loc, Fluid fluid) {
        this.pumpDist = this.theGolem.getRange() * this.theGolem.getRange();
        this.cache.clear();
        this.origin = loc;
        ArrayList<SourceBlock> sources = new ArrayList<>();
        getConnectedFluidBlocks_tweak(this.theWorld, loc.posX, loc.posY, loc.posZ, fluid, sources, pumpDist);
        sources.sort(Collections.reverseOrder());
        this.queue.put(loc, sources);
    }

    private static boolean validFluidBlock_tweak(World world, Fluid fluid, int i, int j, int k) {
        Block bi = world.getBlock(i, j, k);
        if (FluidRegistry.lookupFluidForBlock(bi) != fluid) {
            return false;
        } else {
            if (bi instanceof BlockFluidBase && ((IFluidBlock) bi).canDrain(world, i, j, k)) {
                FluidStack fs = ((IFluidBlock) bi).drain(world, i, j, k, false);
                if (fs != null) {
                    return true;
                }
            }

            return (FluidRegistry.lookupFluidForBlock(bi) == FluidRegistry.WATER && fluid == FluidRegistry.WATER || FluidRegistry.lookupFluidForBlock(bi) == FluidRegistry.LAVA && fluid == FluidRegistry.LAVA) && world.getBlockMetadata(i, j, k) == 0;
        }
    }

    public static void getConnectedFluidBlocks_tweak(World world, int x, int y, int z, Fluid fluid, ArrayList<SourceBlock> sources, float pumpDist) {
        if (fluid == null) return;
        Set<ChunkCoordinates> seen = new HashSet<>();  // ChunkCoordinates has quite terrible hash function, but there are multiple different optimization mod that optimize this. given the popularity of those I'd say it's fine to use it
        Queue<ChunkCoordinates> toVisit = new ArrayDeque<>();
        ChunkCoordinates origin = new ChunkCoordinates(x, y, z);
        toVisit.add(origin);
        while (!toVisit.isEmpty()) {
            ChunkCoordinates v = toVisit.poll();
            if (seen.contains(v)) continue;
            seen.add(v);
            float dist = v.getDistanceSquared(x, y, z);
            if (dist > pumpDist) continue;
            Block block = world.getBlock(v.posX, v.posY, v.posZ);
            if (block == Blocks.flowing_lava)
                block = Blocks.lava;
            else if (block == Blocks.flowing_water)
                block = Blocks.water;
            Fluid f = FluidRegistry.lookupFluidForBlock(block);
            if (f != fluid) continue;
            if (validFluidBlock_tweak(world, fluid, v.posX, v.posY, v.posZ)) {
                sources.add(new SourceBlock(v, dist));
                if (sources.size() >= ConfigurationHandler.INSTANCE.getDecantMaxBlocks())
                    return;
            }
            for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {
                toVisit.add(new ChunkCoordinates(v.posX + direction.offsetX, v.posY + direction.offsetY, v.posZ + direction.offsetZ));
            }
        }
    }

    private void getConnectedFluidBlocks(World world, int x, int y, int z, Fluid fluid, ArrayList<SourceBlock> sources) {
        try {
            if (this.cache.contains(new ChunkCoordinates(x, y, z))) {
                return;
            }

            this.cache.add(new ChunkCoordinates(x, y, z));

            for (int a = -1; a <= 1; ++a) {
                for (int b = -1; b <= 1; ++b) {
                    for (int c = -1; c <= 1; ++c) {
                        if (a != 0 || b != 0 || c != 0) {
                            int xx = x + a;
                            int yy = y + b;
                            int zz = z + c;
                            ChunkCoordinates cc = new ChunkCoordinates(xx, yy, zz);
                            float dist = cc.getDistanceSquaredToChunkCoordinates(this.origin);
                            if (!(dist > this.pumpDist)) {
                                Block bi = world.getBlock(xx, yy, zz);
                                if (bi == Blocks.flowing_lava) {
                                    bi = Blocks.lava;
                                }

                                if (bi == Blocks.flowing_water) {
                                    bi = Blocks.water;
                                }

                                Fluid fi = FluidRegistry.lookupFluidForBlock(bi);
                                if (fi != null && fi == fluid) {
                                    if (this.validFluidBlock(fluid, xx, yy, zz)) {
                                        sources.add(new SourceBlock(cc, dist));
                                    }

                                    this.getConnectedFluidBlocks(world, xx, yy, zz, fluid, sources);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception ignored) {
        }

    }

    public static class SourceBlock implements Comparable<SourceBlock> {
        ChunkCoordinates loc;
        float dist;

        public SourceBlock(ChunkCoordinates loc, float dist) {
            this.loc = loc;
            this.dist = dist;
        }

        @Override
        public int compareTo(SourceBlock target) {
            return Float.compare(this.dist, target.dist);
        }

    }
}
