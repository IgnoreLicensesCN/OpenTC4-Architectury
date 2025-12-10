package thaumcraft.common.items.wands;

import baubles.api.BaublesApi;
import cpw.mods.fml.common.network.NetworkRegistry;
import net.minecraft.block.Block;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.oredict.OreDictionary;
import org.jetbrains.annotations.NotNull;
import com.linearity.opentc4.simpleutils.bauble.BaubleConsumer;
import thaumcraft.api.IArchitect;
import thaumcraft.api.IVisDiscountGear;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.nodes.INode;
import thaumcraft.api.nodes.NodeModifier;
import thaumcraft.api.nodes.NodeType;
import thaumcraft.api.wands.FocusUpgradeType;
import thaumcraft.api.wands.IWandTriggerManager;
import thaumcraft.api.wands.ItemFocusBasic;
import thaumcraft.api.wands.WandFocusEngine;
import thaumcraft.common.config.Config;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.entities.EntitySpecialItem;
import thaumcraft.common.items.baubles.ItemAmuletVis;
import thaumcraft.common.items.wands.foci.ItemFocusTrade;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.fx.PacketFXBlockSparkle;
import thaumcraft.common.lib.research.ResearchManager;
import thaumcraft.common.tiles.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import static baubles.api.expanded.BaubleExpandedSlots.slotLimit;
import static com.linearity.opentc4.simpleutils.bauble.BaubleUtils.forEachBauble;

public class WandManager implements IWandTriggerManager {
    static Map<Entity, Long> cooldownServer = new WeakHashMap<>();
    static Map<Entity, Long> cooldownClient = new WeakHashMap<>();

    public static float getTotalVisDiscount(@NotNull LivingEntity user, Aspect aspect) {
        
//        now we dont have gadomancy in 1.20.1
//        int cheatGadomancy = 0;//Gadomancy used tricks to modify java bytecode.deleting this will cause error with no stacktrace point to it.
        final AtomicInteger total = new AtomicInteger(0);
        if (user == null) {
            return 0.0F;
        } else {
            BaubleConsumer<IVisDiscountGear> visDiscountGearBaubleConsumer = (slot, stack, visDiscountGear) -> {
                total.addAndGet(visDiscountGear.getVisDiscount(stack, user, aspect));
                return false;
            };
            forEachBauble(user, IVisDiscountGear.class, visDiscountGearBaubleConsumer);

            for (int a = 0; a < 4; ++a) {
                ItemStack stack = player.inventory.getStackInSlot(a);
                if (player.inventory.armorItemInSlot(a) != null) {
                    Item item = player.inventory.armorItemInSlot(a).getItem();
                    if (item instanceof IVisDiscountGear) {
                        visDiscountGearBaubleConsumer.accept(a, stack, ((IVisDiscountGear) item));
                    }
                }
            }

            if (user.isPotionActive(Config.potionVisExhaustID) || user.isPotionActive(Config.potionInfVisExhaustID)) {
                int level1 = 0;
                int level2 = 0;
                if (user.isPotionActive(Config.potionVisExhaustID)) {
                    level1 = user.getActivePotionEffect(Potion.potionTypes[Config.potionVisExhaustID]).getAmplifier();
                }

                if (user.isPotionActive(Config.potionInfVisExhaustID)) {
                    level2 = user.getActivePotionEffect(Potion.potionTypes[Config.potionInfVisExhaustID]).getAmplifier();
                }

                total.addAndGet((Math.max(level1, level2) + 1) * 10);
            }

            return (float) total.get() / 100.0F;
        }
    }

    public static boolean consumeVisFromInventory(Player player, AspectList cost){
        return consumeVisFromInventory(player, cost, ignore -> true);
    }
    public static boolean consumeVisFromInventory(Player player, AspectList cost, Function<ItemStack,Boolean> additionalCondition) {
        BaubleConsumer<ItemAmuletVis> amuletVisBaubleConsumer = (slot, stack, itemAmuletVis)
                -> {
            if (!additionalCondition.apply(stack)) {return false;}
            return itemAmuletVis.consumeAllVis(stack, player, cost, true, true);
        };
        if (forEachBauble(player, ItemAmuletVis.class, amuletVisBaubleConsumer)) {
            return true;
        }


        BaubleConsumer<WandCastingItem> wandCastingBaubleConsumer = (slot, stack, WandCastingItem) ->
        {
            if (!additionalCondition.apply(stack)) {return false;}
            return WandCastingItem.consumeAllVis(stack, player, cost, true, true);
        };
        return forEachBauble(player, WandCastingItem.class, wandCastingBaubleConsumer);

//      for(int a = player.inventory.mainInventory.length - 1; a >= 0; --a) {
//         ItemStack item = player.inventory.mainInventory[a];
//         if (item != null && item.getItem() instanceof WandCastingItem) {
//            boolean done = ((WandCastingItem)item.getItem()).consumeAllVis(item, player, cost, true, true);
//            if (done) {
//               return true;
//            }
//         }
//      }
    }

    public static boolean createCrucible(ItemStack is, Player player, Level world, int x, int y, int z) {
        WandCastingItem wand = (WandCastingItem) is.getItem();
        if (Platform.getEnvironment() != Env.CLIENT) {
            world.playSoundEffect((double) x + (double) 0.5F, (double) y + (double) 0.5F, (double) z + (double) 0.5F, "thaumcraft:wand", 1.0F, 1.0F);
            world.setBlockToAir(x, y, z);
            world.setBlock(x, y, z, ConfigBlocks.blockMetalDevice, 0, 3);
            world.notifyBlocksOfNeighborChange(x, y, z, world.getBlock(x, y, z));
            world.markBlockForUpdate(x, y, z);
            world.addBlockEvent(x, y, z, ConfigBlocks.blockMetalDevice, 1, 1);
            return true;
        } else {
            return false;
        }
    }

    public static boolean createInfusionAltar(ItemStack itemstack, Player player, World world, int x, int y, int z) {
        WandCastingItem wand = (WandCastingItem) itemstack.getItem();

        for (int xx = x - 2; xx <= x; ++xx) {
            for (int yy = y - 2; yy <= y; ++yy) {
                for (int zz = z - 2; zz <= z; ++zz) {
                    if (fitInfusionAltar(world, xx, yy, zz) && wand.consumeAllVisCrafting(itemstack, player, (new AspectList()).add(Aspect.FIRE, 25).add(Aspect.EARTH, 25).add(Aspect.ORDER, 25).add(Aspect.AIR, 25).add(Aspect.ENTROPY, 25).add(Aspect.WATER, 25), true)) {
                        if (Platform.getEnvironment() != Env.CLIENT) {
                            replaceInfusionAltar(world, xx, yy, zz);
                            return true;
                        }

                        return false;
                    }
                }
            }
        }

        return false;
    }

    public static boolean fitInfusionAltar(World world, int x, int y, int z) {
        ItemStack br1 = new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 6);
        ItemStack br2 = new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 7);
        ItemStack bs = new ItemStack(ConfigBlocks.blockStoneDevice, 1, 2);
        new ItemStack(ConfigBlocks.blockStoneDevice, 1, 1);
        ItemStack[][][] blueprint = new ItemStack[][][]{{{null, null, null}, {null, bs, null}, {null, null, null}}, {{br1, null, br1}, {null, null, null}, {br1, null, br1}}, {{br2, null, br2}, {null, null, null}, {br2, null, br2}}};

        for (int yy = 0; yy < 3; ++yy) {
            for (int xx = 0; xx < 3; ++xx) {
                for (int zz = 0; zz < 3; ++zz) {
                    if (blueprint[yy][xx][zz] == null) {
                        if (xx == 1 && zz == 1 && yy == 2) {
                            TileEntity t = world.getTileEntity(x + xx, y - yy + 2, z + zz);
                            if (!(t instanceof TilePedestal)) {
                                return false;
                            }
                        } else if (!world.isAirBlock(x + xx, y - yy + 2, z + zz)) {
                            return false;
                        }
                    } else {
                        Block block = world.getBlock(x + xx, y - yy + 2, z + zz);
                        int md = world.getBlockMetadata(x + xx, y - yy + 2, z + zz);
                        if (!(new ItemStack(block, 1, md)).isItemEqual(blueprint[yy][xx][zz])) {
                            return false;
                        }
                    }
                }
            }
        }

        return true;
    }

    public static void replaceInfusionAltar(World world, int x, int y, int z) {
        int[][][] blueprint = new int[][][]{{{0, 0, 0}, {0, 9, 0}, {0, 0, 0}}, {{1, 0, 1}, {0, 0, 0}, {1, 0, 1}}, {{2, 0, 3}, {0, 0, 0}, {4, 0, 5}}};

        for (int yy = 0; yy < 3; ++yy) {
            for (int xx = 0; xx < 3; ++xx) {
                for (int zz = 0; zz < 3; ++zz) {
                    if (blueprint[yy][xx][zz] != 0) {
                        if (blueprint[yy][xx][zz] == 1) {
                            world.setBlock(x + xx, y - yy + 2, z + zz, ConfigBlocks.blockStoneDevice, 4, 3);
                            world.addBlockEvent(x + xx, y - yy + 2, z + zz, ConfigBlocks.blockStoneDevice, 1, 0);
                        }

                        if (blueprint[yy][xx][zz] > 1 && blueprint[yy][xx][zz] < 9) {
                            world.setBlock(x + xx, y - yy + 2, z + zz, ConfigBlocks.blockStoneDevice, 3, 3);
                            TileInfusionPillar tip = (TileInfusionPillar) world.getTileEntity(x + xx, y - yy + 2, z + zz);
                            tip.orientation = (byte) blueprint[yy][xx][zz];
                            world.markBlockForUpdate(x + xx, y - yy + 2, z + zz);
                            world.addBlockEvent(x + xx, y - yy + 2, z + zz, ConfigBlocks.blockStoneDevice, 1, 0);
                        }

                        if (blueprint[yy][xx][zz] == 9) {
                            TileInfusionMatrix tis = (TileInfusionMatrix) world.getTileEntity(x + xx, y - yy + 2, z + zz);
                            tis.active = true;
                            world.markBlockForUpdate(x + xx, y - yy + 2, z + zz);
                        }
                    }
                }
            }
        }

        world.playSoundEffect((double) x + (double) 0.5F, (double) y + (double) 0.5F, (double) z + (double) 0.5F, "thaumcraft:wand", 1.0F, 1.0F);
    }

    public static boolean createNodeJar(ItemStack itemstack, Player player, World world, int x, int y, int z) {
        WandCastingItem wand = (WandCastingItem) itemstack.getItem();

        for (int xx = x - 2; xx <= x; ++xx) {
            for (int yy = y - 3; yy <= y; ++yy) {
                for (int zz = z - 2; zz <= z; ++zz) {
                    if (fitNodeJar(world, xx, yy, zz) && wand.consumeAllVisCrafting(itemstack, player, (new AspectList()).add(Aspect.FIRE, 70).add(Aspect.EARTH, 70).add(Aspect.ORDER, 70).add(Aspect.AIR, 70).add(Aspect.ENTROPY, 70).add(Aspect.WATER, 70), true)) {
                        if (Platform.getEnvironment() != Env.CLIENT) {
                            replaceNodeJar(world, xx, yy, zz);
                            return true;
                        }

                        return false;
                    }
                }
            }
        }

        return false;
    }

    public static boolean createThaumatorium(ItemStack itemstack, Player player, World world, int x, int y, int z, int side) {
        WandCastingItem wand = (WandCastingItem) itemstack.getItem();
        if (world.getBlock(x, y + 1, z) != ConfigBlocks.blockMetalDevice || world.getBlockMetadata(x, y + 1, z) != 9 || world.getBlock(x, y - 1, z) != ConfigBlocks.blockMetalDevice || world.getBlockMetadata(x, y - 1, z) != 0) {
            if (world.getBlock(x, y - 1, z) != ConfigBlocks.blockMetalDevice || world.getBlockMetadata(x, y - 1, z) != 9 || world.getBlock(x, y - 2, z) != ConfigBlocks.blockMetalDevice || world.getBlockMetadata(x, y - 2, z) != 0) {
                return false;
            }

            --y;
        }

        if (wand.consumeAllVisCrafting(itemstack, player, (new AspectList()).add(Aspect.FIRE, 15).add(Aspect.ORDER, 30).add(Aspect.WATER, 30), true) && Platform.getEnvironment() != Env.CLIENT) {
            world.setBlock(x, y, z, ConfigBlocks.blockMetalDevice, 10, 0);
            world.setBlock(x, y + 1, z, ConfigBlocks.blockMetalDevice, 11, 0);
            TileEntity tile = world.getTileEntity(x, y, z);
            if (tile instanceof TileThaumatorium) {
                ((TileThaumatorium) tile).facing = ForgeDirection.getOrientation(side);
            }

            world.markBlockForUpdate(x, y, z);
            world.markBlockForUpdate(x, y + 1, z);
            world.notifyBlockChange(x, y, z, ConfigBlocks.blockMetalDevice);
            world.notifyBlockChange(x, y + 1, z, ConfigBlocks.blockMetalDevice);
            PacketHandler.INSTANCE.sendToAllAround(new PacketFXBlockSparkle(x, y, z, -9999), new NetworkRegistry.TargetPoint(world.dimension(), x, y, z, 32.0F));
            PacketHandler.INSTANCE.sendToAllAround(new PacketFXBlockSparkle(x, y + 1, z, -9999), new NetworkRegistry.TargetPoint(world.dimension(), x, y, z, 32.0F));
            world.playSoundEffect((double) x + (double) 0.5F, (double) y + (double) 0.5F, (double) z + (double) 0.5F, "thaumcraft:wand", 1.0F, 1.0F);
            return true;
        } else {
            return false;
        }
    }

    static boolean containsMatch(boolean strict, List<ItemStack> inputs, ItemStack... targets) {
        for (ItemStack input : inputs) {
            for (ItemStack target : targets) {
                if (OreDictionary.itemMatches(input, target, strict)) {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean fitNodeJar(World world, int x, int y, int z) {
        int[][][] blueprint = new int[][][]{{{1, 1, 1}, {1, 1, 1}, {1, 1, 1}}, {{2, 2, 2}, {2, 2, 2}, {2, 2, 2}}, {{2, 2, 2}, {2, 3, 2}, {2, 2, 2}}, {{2, 2, 2}, {2, 2, 2}, {2, 2, 2}}};

        for (int yy = 0; yy < 4; ++yy) {
            for (int xx = 0; xx < 3; ++xx) {
                for (int zz = 0; zz < 3; ++zz) {
                    Block block = world.getBlock(x + xx, y - yy + 2, z + zz);
                    int md = world.getBlockMetadata(x + xx, y - yy + 2, z + zz);
                    if (blueprint[yy][xx][zz] == 1 && !containsMatch(false, OreDictionary.getOres("slabWood"), new ItemStack(block, 1, md))) {
                        return false;
                    }

                    if (blueprint[yy][xx][zz] == 2 && block != Blocks.glass) {
                        return false;
                    }

                    if (blueprint[yy][xx][zz] == 3) {
                        TileEntity tile = world.getTileEntity(x + xx, y - yy + 2, z + zz);
                        if (!(tile instanceof INode) || tile instanceof TileJarNode) {
                            return false;
                        }
                    }
                }
            }
        }

        return true;
    }

    public static void replaceNodeJar(World world, int x, int y, int z) {
        if (Platform.getEnvironment() != Env.CLIENT) {
            int[][][] blueprint = new int[][][]{{{1, 1, 1}, {1, 1, 1}, {1, 1, 1}}, {{2, 2, 2}, {2, 2, 2}, {2, 2, 2}}, {{2, 2, 2}, {2, 3, 2}, {2, 2, 2}}, {{2, 2, 2}, {2, 2, 2}, {2, 2, 2}}};

            for (int yy = 0; yy < 4; ++yy) {
                for (int xx = 0; xx < 3; ++xx) {
                    for (int zz = 0; zz < 3; ++zz) {
                        if (blueprint[yy][xx][zz] == 3) {
                            TileEntity tile = world.getTileEntity(x + xx, y - yy + 2, z + zz);
                            INode node = (INode) tile;
                            AspectList na = node.getAspects().copy();
                            int nt = node.getNodeType().ordinal();
                            int nm = -1;
                            if (node.getNodeModifier() != null) {
                                nm = node.getNodeModifier().ordinal();
                            }

                            if (world.getRandom().nextFloat() < 0.75F) {
                                if (node.getNodeModifier() == null) {
                                    nm = NodeModifier.PALE.ordinal();
                                } else if (node.getNodeModifier() == NodeModifier.BRIGHT) {
                                    nm = -1;
                                } else if (node.getNodeModifier() == NodeModifier.PALE) {
                                    nm = NodeModifier.FADING.ordinal();
                                }
                            }

                            String nid = node.getId();
                            node.setAspects(new AspectList());
                            world.removeTileEntity(x + xx, y - yy + 2, z + zz);
                            world.setBlock(x + xx, y - yy + 2, z + zz, ConfigBlocks.blockJar, 2, 3);
                            tile = world.getTileEntity(x + xx, y - yy + 2, z + zz);
                            TileJarNode jar = (TileJarNode) tile;
                            jar.setAspects(na);
                            if (nm >= 0) {
                                jar.setNodeModifier(NodeModifier.values()[nm]);
                            }

                            jar.setNodeType(NodeType.values()[nt]);
                            jar.setId(nid);
                            world.addBlockEvent(x + xx, y - yy + 2, z + zz, ConfigBlocks.blockJar, 9, 0);
                        } else {
                            world.setBlockToAir(x + xx, y - yy + 2, z + zz);
                        }
                    }
                }
            }

            world.playSoundEffect((double) x + (double) 0.5F, (double) y + (double) 0.5F, (double) z + (double) 0.5F, "thaumcraft:wand", 1.0F, 1.0F);
        }
    }

    public static boolean createArcaneFurnace(ItemStack itemstack, Player player, World world, int x, int y, int z) {
        WandCastingItem wand = (WandCastingItem) itemstack.getItem();

        for (int xx = x - 2; xx <= x; ++xx) {
            for (int yy = y - 2; yy <= y; ++yy) {
                for (int zz = z - 2; zz <= z; ++zz) {
                    if (fitArcaneFurnace(world, xx, yy, zz) && wand.consumeAllVisCrafting(itemstack, player, (new AspectList()).add(Aspect.FIRE, 50).add(Aspect.EARTH, 50), true)) {
                        if (Platform.getEnvironment() != Env.CLIENT) {
                            replaceArcaneFurnace(world, xx, yy, zz);
                            return true;
                        }

                        return false;
                    }
                }
            }
        }

        return false;
    }

    public static boolean fitArcaneFurnace(World world, int x, int y, int z) {
        Block bo = Blocks.obsidian;
        Block bn = Blocks.nether_brick;
        Block bf = Blocks.iron_bars;
        Block bl = Blocks.lava;
        Block[][][] blueprint = new Block[][][]{{{bn, bo, bn}, {bo, Blocks.air, bo}, {bn, bo, bn}}, {{bn, bo, bn}, {bo, bl, bo}, {bn, bo, bn}}, {{bn, bo, bn}, {bo, bo, bo}, {bn, bo, bn}}};
        boolean fencefound = false;

        for (int yy = 0; yy < 3; ++yy) {
            for (int xx = 0; xx < 3; ++xx) {
                for (int zz = 0; zz < 3; ++zz) {
                    Block block = world.getBlock(x + xx, y - yy + 2, z + zz);
                    if (world.isAirBlock(x + xx, y - yy + 2, z + zz)) {
                        block = Blocks.air;
                    }

                    if (block != blueprint[yy][xx][zz]) {
                        if (yy != 1 || fencefound || block != bf || xx == zz || xx != 1 && zz != 1) {
                            return false;
                        }

                        fencefound = true;
                    }
                }
            }
        }

        return fencefound;
    }

    public static boolean replaceArcaneFurnace(World world, int x, int y, int z) {
        boolean fencefound = false;

        for (int yy = 0; yy < 3; ++yy) {
            int step = 1;

            for (int zz = 0; zz < 3; ++zz) {
                for (int xx = 0; xx < 3; ++xx) {
                    int md = step;
                    if (world.getBlock(x + xx, y + yy, z + zz) == Blocks.lava || world.getBlock(x + xx, y + yy, z + zz) == Blocks.flowing_lava) {
                        md = 0;
                    }

                    if (world.getBlock(x + xx, y + yy, z + zz) == Blocks.iron_bars) {
                        md = 10;
                    }

                    if (!world.isAirBlock(x + xx, y + yy, z + zz)) {
                        world.setBlock(x + xx, y + yy, z + zz, ConfigBlocks.blockArcaneFurnace, md, 0);
                        world.addBlockEvent(x + xx, y + yy, z + zz, ConfigBlocks.blockArcaneFurnace, 1, 4);
                    }

                    ++step;
                }
            }
        }

        for (int yy = 0; yy < 3; ++yy) {
            for (int zz = 0; zz < 3; ++zz) {
                for (int xx = 0; xx < 3; ++xx) {
                    world.markBlockForUpdate(x + xx, y + yy, z + zz);
                }
            }
        }

        world.playSoundEffect((double) x + (double) 0.5F, (double) y + (double) 0.5F, (double) z + (double) 0.5F, "thaumcraft:wand", 1.0F, 1.0F);
        return fencefound;
    }

    public static boolean createThaumonomicon(ItemStack itemstack, Player player, Level world, int x, int y, int z) {
        if (Platform.getEnvironment() != Env.CLIENT) {
            var wand = itemstack.getItem();
            if (wand instanceof WandFocusEngine engine && engine.canApplyFocus() && engine.getFocusItemStack(itemstack) != null) {
                return false;
            } else {
                world.setBlockAndUpdate(new BlockPos(x, y, z),Blocks.AIR.defaultBlockState());
                EntitySpecialItem entityItem = new EntitySpecialItem(world, (float) x + 0.5F, (float) y + 0.3F, (float) z + 0.5F, new ItemStack(ConfigItems.itemThaumonomicon));
                entityItem.motionY = 0.0F;
                entityItem.motionX = 0.0F;
                entityItem.motionZ = 0.0F;
                world.spawnEntityInWorld(entityItem);
                PacketHandler.INSTANCE.sendToAllAround(new PacketFXBlockSparkle(x, y, z, -9999), new NetworkRegistry.TargetPoint(world.dimension(), x, y, z, 32.0F));
                world.playSoundEffect((double) x + (double) 0.5F, (double) y + (double) 0.5F, (double) z + (double) 0.5F, "thaumcraft:wand", 1.0F, 1.0F);
                return true;
            }
        } 
        else {
            return false;
        }
    }

    private static boolean createOculus(ItemStack itemstack, Player player, World world, int x, int y, int z, int side) {
        if (Platform.getEnvironment() != Env.CLIENT) {
            TileEntity tile = world.getTileEntity(x, y, z);
            TileEntity node = world.getTileEntity(x, y + 1, z);
            if (node != null && tile instanceof TileEldritchAltar && ((TileEldritchAltar) tile).getEyes() == 4 && !((TileEldritchAltar) tile).isOpen() && node instanceof TileNode && ((TileNode) node).getNodeType() == NodeType.DARK && ((TileEldritchAltar) tile).checkForMaze()) {
                WandCastingItem wand = (WandCastingItem) itemstack.getItem();
                if (wand.consumeAllVisCrafting(itemstack, player, (new AspectList()).add(Aspect.AIR, 100).add(Aspect.FIRE, 100).add(Aspect.EARTH, 100).add(Aspect.WATER, 100).add(Aspect.ORDER, 100).add(Aspect.ENTROPY, 100), true)) {
                    world.playSoundEffect((double) x + (double) 0.5F, (double) y + (double) 0.5F, (double) z + (double) 0.5F, "thaumcraft:wand", 1.0F, 1.0F);
                    ((TileEldritchAltar) tile).setOpen(true);
                    world.setBlockToAir(x, y + 1, z);
                    world.setBlock(x, y + 1, z, ConfigBlocks.blockEldritchPortal);
                    tile.markDirty();
                    world.markBlockForUpdate(x, y, z);
                }
            }
        }

        return false;
    }

    private static BaubleConsumer<ItemFocusPouch> getItemFocusPouchBaubleConsumer(
            int offset,
            int[] pouchcountPointer,
            HashMap<Integer, Integer> pouches,
            TreeMap<String, Integer> foci
    ) {
        return (slot, stack, itemFocusPouch) -> {
            ++pouchcountPointer[0];
            pouches.put(pouchcountPointer[0], slot + offset);
            ItemStack[] inv = itemFocusPouch.getInventory(stack);

            for (int q = 0; q < inv.length; ++q) {
                ItemStack probablyFocusBasicStack = inv[q];
                if (probablyFocusBasicStack != null && probablyFocusBasicStack.getItem() instanceof ItemFocusBasic) {
                    foci.put(((ItemFocusBasic) probablyFocusBasicStack.getItem()).getSortingHelper(probablyFocusBasicStack), q + pouchcountPointer[0] * 1000);
                }
            }
            return false;
        };
    }

    public static void changeFocus(ItemStack is, World w, Player player, String focus) {
        WandCastingItem wand = (WandCastingItem) is.getItem();
        TreeMap<String, Integer> foci = new TreeMap<>();
        HashMap<Integer, Integer> pouches = new HashMap<>();
        final int[] pouchcount = {0};
        final ItemStack[] item = {null};
        IInventory baubles = BaublesApi.getBaubles(player);

        BaubleConsumer<ItemFocusPouch> itemFocusPouchInventoryConsumer = getItemFocusPouchBaubleConsumer(0, pouchcount, pouches, foci);
        BaubleConsumer<ItemFocusPouch> itemFocusPouchBaubleConsumer = getItemFocusPouchBaubleConsumer(-slotLimit, pouchcount, pouches, foci);
        forEachBauble(player, ItemFocusPouch.class, itemFocusPouchBaubleConsumer);

        for (int a = 0; a < 36; ++a) {
            item[0] = player.inventory.mainInventory[a];
            if (item[0] != null && item[0].getItem() instanceof ItemFocusBasic) {
                foci.put(((ItemFocusBasic) item[0].getItem()).getSortingHelper(item[0]), a);
            }

            if (item[0] != null && item[0].getItem() instanceof ItemFocusPouch) {
                itemFocusPouchInventoryConsumer.accept(a, item[0], (ItemFocusPouch) item[0].getItem());
            }
        }

        if (focus != null && !focus.equals("REMOVE") && !foci.isEmpty()
        ) {
            {
                String newkey = focus;
                if (foci.get(focus) == null) {
                    newkey = foci.higherKey(focus);
                }

                if (newkey == null || foci.get(newkey) == null) {
                    newkey = foci.firstKey();
                }

                if (foci.get(newkey) < 1000 && foci.get(newkey) >= 0) {
                    item[0] = player.inventory.mainInventory[foci.get(newkey)].copy();
                } else {
                    int pid = foci.get(newkey) / 1000;
                    if (pouches.containsKey(pid)) {
                        int pouchslot = pouches.get(pid);
                        int focusslot = foci.get(newkey) - pid * 1000;
                        ItemStack tmp = null;
                        if (pouchslot >= 0) {
                            tmp = player.inventory.mainInventory[pouchslot].copy();
                        } else {
                            tmp = baubles.getStackInSlot(pouchslot + slotLimit).copy();
                        }

                        item[0] = fetchFocusFromPouch(player, focusslot, tmp, pouchslot);
                    }
                }

                if (item[0] == null) {
                    return;
                }

                if (foci.get(newkey) < 1000 && foci.get(newkey) >= 0) {
                    player.inventory.setInventorySlotContents(foci.get(newkey), null);
                }

                w.playSoundAtEntity(player, "thaumcraft:cameraticks", 0.3F, 1.0F);
                if (wand.getFocus(is) != null && (addFocusToPouch(player, wand.getFocusItem(is).copy(), pouches) || player.inventory.addItemStackToInventory(wand.getFocusItem(is).copy()))) {
                    wand.setFocus(is, null);
                }

                if (wand.getFocus(is) == null) {
                    wand.setFocus(is, item[0]);
                } else if (!addFocusToPouch(player, item[0], pouches)) {
                    player.inventory.addItemStackToInventory(item[0]);
                }
            }

        } else {
            if (wand.getFocus(is) != null
                    && (addFocusToPouch(player, wand.getFocusItem(is).copy(), pouches)
                    || player.inventory.addItemStackToInventory(wand.getFocusItem(is).copy()))
            ) {
                wand.setFocus(is, null);
                w.playSoundAtEntity(player, "thaumcraft:cameraticks", 0.3F, 0.9F);
            }

        }
    }

    private static ItemStack fetchFocusFromPouch(Player player, int focusid, ItemStack pouch, int pouchslot) {
        ItemStack focus = null;
        ItemStack[] inv = ((ItemFocusPouch) pouch.getItem()).getInventory(pouch);
        ItemStack contents = inv[focusid];
        if (contents != null && contents.getItem() instanceof ItemFocusBasic) {
            focus = contents.copy();
            inv[focusid] = null;
            ((ItemFocusPouch) pouch.getItem()).setInventory(pouch, inv);
            if (pouchslot >= 0) {
                player.inventory.setInventorySlotContents(pouchslot, pouch);
                player.inventory.markDirty();
            } else {
                IInventory baubles = BaublesApi.getBaubles(player);
                baubles.setInventorySlotContents(pouchslot + 4, pouch);
                baubles.markDirty();
            }
        }

        return focus;
    }

    private static boolean addFocusToPouch(Player player, ItemStack focus, HashMap<Integer, Integer> pouches) {
        IInventory baubles = BaublesApi.getBaubles(player);

        for (Integer pouchslot : pouches.values()) {
            ItemStack pouch;
            if (pouchslot >= 0) {
                pouch = player.inventory.mainInventory[pouchslot];
            } else {
                pouch = baubles.getStackInSlot(pouchslot + 4);
            }

            ItemStack[] inv = ((ItemFocusPouch) pouch.getItem()).getInventory(pouch);

            for (int q = 0; q < inv.length; ++q) {
                ItemStack contents = inv[q];
                if (contents == null) {
                    inv[q] = focus.copy();
                    ((ItemFocusPouch) pouch.getItem()).setInventory(pouch, inv);
                    if (pouchslot >= 0) {
                        player.inventory.setInventorySlotContents(pouchslot, pouch);
                        player.inventory.markDirty();
                    } else {
                        baubles.setInventorySlotContents(pouchslot + 4, pouch);
                        baubles.markDirty();
                    }

                    return true;
                }
            }
        }

        return false;
    }

    public static void toggleMisc(ItemStack itemstack, Level world, Player player) {
        if (itemstack.getItem() instanceof WandCastingItem) {
            WandCastingItem wand = (WandCastingItem) itemstack.getItem();
            if (wand.getFocus(itemstack) != null && wand.getFocus(itemstack) instanceof IArchitect && wand.getFocus(itemstack).isUpgradedWith(wand.getFocusItem(itemstack), FocusUpgradeType.architect)) {
                int dim = getAreaDim(itemstack);
                IArchitect fa = (IArchitect) wand.getFocus(itemstack);
                if (player.isSneaking()) {
                    ++dim;
                    if (dim > (wand.getFocusItem(itemstack).getItem() instanceof ItemFocusTrade ? 2 : 3)) {
                        dim = 0;
                    }

                    setAreaDim(itemstack, dim);
                } else {
                    int areax = getAreaX(itemstack);
                    int areay = getAreaY(itemstack);
                    int areaz = getAreaZ(itemstack);
                    if (dim == 0) {
                        ++areax;
                        ++areaz;
                        ++areay;
                    } else if (dim == 1) {
                        ++areax;
                    } else if (dim == 2) {
                        ++areaz;
                    } else if (dim == 3) {
                        ++areay;
                    }

                    if (areax > wand.getFocus(itemstack).getMaxAreaSize(wand.getFocusItem(itemstack))) {
                        areax = 0;
                    }

                    if (areaz > wand.getFocus(itemstack).getMaxAreaSize(wand.getFocusItem(itemstack))) {
                        areaz = 0;
                    }

                    if (areay > wand.getFocus(itemstack).getMaxAreaSize(wand.getFocusItem(itemstack))) {
                        areay = 0;
                    }

                    setAreaX(itemstack, areax);
                    setAreaY(itemstack, areay);
                    setAreaZ(itemstack, areaz);
                }
            }

        }
    }

    public static int getAreaDim(ItemStack stack) {
        return stack.hasTagCompound() && stack.stackTagCompound.hasKey("aread") ? stack.stackTagCompound.getInteger("aread") : 0;
    }

    public static int getAreaX(ItemStack stack) {
        WandCastingItem wand = (WandCastingItem) stack.getItem();
        if (stack.hasTagCompound() && stack.stackTagCompound.hasKey("areax")) {
            int a = stack.stackTagCompound.getInteger("areax");
            if (a > wand.getFocus(stack).getMaxAreaSize(wand.getFocusItem(stack))) {
                a = wand.getFocus(stack).getMaxAreaSize(wand.getFocusItem(stack));
            }

            return a;
        } else {
            return wand.getFocus(stack).getMaxAreaSize(wand.getFocusItem(stack));
        }
    }

    public static int getAreaY(ItemStack stack) {
        WandCastingItem wand = (WandCastingItem) stack.getItem();
        if (stack.hasTagCompound() && stack.stackTagCompound.hasKey("areay")) {
            int a = stack.stackTagCompound.getInteger("areay");
            if (a > wand.getFocus(stack).getMaxAreaSize(wand.getFocusItem(stack))) {
                a = wand.getFocus(stack).getMaxAreaSize(wand.getFocusItem(stack));
            }

            return a;
        } else {
            return wand.getFocus(stack).getMaxAreaSize(wand.getFocusItem(stack));
        }
    }

    public static int getAreaZ(ItemStack stack) {
        WandCastingItem wand = (WandCastingItem) stack.getItem();
        if (stack.hasTagCompound() && stack.stackTagCompound.hasKey("areaz")) {
            int a = stack.stackTagCompound.getInteger("areaz");
            if (a > wand.getFocus(stack).getMaxAreaSize(wand.getFocusItem(stack))) {
                a = wand.getFocus(stack).getMaxAreaSize(wand.getFocusItem(stack));
            }

            return a;
        } else {
            return wand.getFocus(stack).getMaxAreaSize(wand.getFocusItem(stack));
        }
    }

    public static void setAreaX(ItemStack stack, int area) {
        if (stack.hasTagCompound()) {
            stack.stackTagCompound.setInteger("areax", area);
        }

    }

    public static void setAreaY(ItemStack stack, int area) {
        if (stack.hasTagCompound()) {
            stack.stackTagCompound.setInteger("areay", area);
        }

    }

    public static void setAreaZ(ItemStack stack, int area) {
        if (stack.hasTagCompound()) {
            stack.stackTagCompound.setInteger("areaz", area);
        }

    }

    public static void setAreaDim(ItemStack stack, int dim) {
        if (stack.hasTagCompound()) {
            stack.stackTagCompound.setInteger("aread", dim);
        }

    }

    public static boolean isOnCooldown(LivingEntity entityLiving) {
        if (entityLiving.level().isClientSide() && cooldownClient.containsKey(entityLiving.getId())) {
            return cooldownClient.get(entityLiving.getId()) > System.currentTimeMillis();
        }
        else if (Platform.getEnvironment() != Env.CLIENT && cooldownServer.containsKey(entityLiving.getId())) {
            return cooldownServer.get(entityLiving.getId()) > System.currentTimeMillis();
        }
        else {
            return false;
        }
    }

    public static float getCooldown(LivingEntity entityLiving) {
        if (entityLiving.level().isClientSide()) {
            return 0.f;
        }
        return (float) (cooldownClient.getOrDefault(entityLiving,System.currentTimeMillis()) - System.currentTimeMillis()) / 1000.0F;
    }

    public static void setCooldown(LivingEntity entityLiving, int cd) {
        if (cd == 0) {
            cooldownClient.remove(entityLiving);
            cooldownServer.remove(entityLiving);
        } else if (entityLiving.level().isClientSide()) {
            cooldownClient.put(entityLiving, System.currentTimeMillis() + (long) cd);
        } else {
            cooldownServer.put(entityLiving, System.currentTimeMillis() + (long) cd);
        }

    }

    @Override
    public boolean performTrigger(Level world, ItemStack wand, Player player, int x, int y, int z, Direction side) {
        boolean returnFlag = false;
        returnFlag = createThaumonomicon(wand, player, world, x, y, z);
        if (returnFlag) {return true;}
        returnFlag = createCrucible(wand, player, world, x, y, z);
        if (returnFlag) {return true;}
        
        return false;
        switch (event) {
            case 2:
                if (ResearchManager.isResearchComplete(player.getCommandSenderName(), "INFERNALFURNACE")) {
                    return createArcaneFurnace(wand, player, world, x, y, z);
                }
                break;
            case 3:
                if (ResearchManager.isResearchComplete(player.getCommandSenderName(), "INFUSION")) {
                    return createInfusionAltar(wand, player, world, x, y, z);
                }
                break;
            case 4:
                if (ResearchManager.isResearchComplete(player.getCommandSenderName(), "NODEJAR")) {
                    return createNodeJar(wand, player, world, x, y, z);
                }
                break;
            case 5:
                if (ResearchManager.isResearchComplete(player.getCommandSenderName(), "THAUMATORIUM")) {
                    return createThaumatorium(wand, player, world, x, y, z, side);
                }
                break;
            case 6:
                if (ResearchManager.isResearchComplete(player.getCommandSenderName(), "OCULUS")) {
                    return createOculus(wand, player, world, x, y, z, side);
                }
                break;
            case 7:
                if (ResearchManager.isResearchComplete(player.getCommandSenderName(), "ADVALCHEMYFURNACE")) {
                    return createAdvancedAlchemicalFurnace(wand, player, world, x, y, z, side);
                }
        }

        return false;
    }

    private static boolean createAdvancedAlchemicalFurnace(ItemStack itemstack, Player player, Level world, int x, int y, int z, int side) {
        if (Platform.getEnvironment() != Env.CLIENT) {
            int[][][] blueprint = new int[][][]{{{4, 4, 4}, {4, 3, 4}, {4, 4, 4}}, {{1, 2, 1}, {2, 0, 2}, {1, 2, 1}}};

            for (int a = -1; a <= 1; ++a) {
                for (int b = -1; b <= 1; ++b) {
                    for (int c = -1; c <= 1; ++c) {
                        if (world.getBlock(x + a, y + b, z + c) == ConfigBlocks.blockStoneDevice && world.getBlockMetadata(x + a, y + b, z + c) == 0) {
                            for (int aa = -1; aa <= 1; ++aa) {
                                for (int bb = 0; bb <= 1; ++bb) {
                                    int cc = -1;

                                    while (cc <= 1) {
                                        if (blueprint[bb][aa + 1][cc + 1] != 1 || world.getBlock(x + a + aa, y + b + bb, z + c + cc) == ConfigBlocks.blockMetalDevice && world.getBlockMetadata(x + a + aa, y + b + bb, z + c + cc) == 1) {
                                            if (blueprint[bb][aa + 1][cc + 1] != 2 || world.getBlock(x + a + aa, y + b + bb, z + c + cc) == ConfigBlocks.blockMetalDevice && world.getBlockMetadata(x + a + aa, y + b + bb, z + c + cc) == 9) {
                                                if (blueprint[bb][aa + 1][cc + 1] != 4 || world.getBlock(x + a + aa, y + b + bb, z + c + cc) == ConfigBlocks.blockMetalDevice && world.getBlockMetadata(x + a + aa, y + b + bb, z + c + cc) == 3) {
                                                    if (blueprint[bb][aa + 1][cc + 1] != 3 || world.getBlock(x + a + aa, y + b + bb, z + c + cc) == ConfigBlocks.blockStoneDevice && world.getBlockMetadata(x + a + aa, y + b + bb, z + c + cc) == 0) {
                                                        ++cc;
                                                        continue;
                                                    }

                                                    return false;
                                                }

                                                return false;
                                            }

                                            return false;
                                        }

                                        return false;
                                    }
                                }
                            }

                            WandCastingItem wand = (WandCastingItem) itemstack.getItem();
                            if (!wand.consumeAllVisCrafting(itemstack, player, (new AspectList()).add(Aspect.FIRE, 50).add(Aspect.WATER, 50).add(Aspect.ORDER, 50), true)) {
                                return false;
                            }

                            world.setBlock(x + a, y + b, z + c, ConfigBlocks.blockAlchemyFurnace);
                            world.setBlock(x + a - 1, y + b, z + c, ConfigBlocks.blockAlchemyFurnace, 1, 3);
                            world.setBlock(x + a + 1, y + b, z + c, ConfigBlocks.blockAlchemyFurnace, 1, 3);
                            world.setBlock(x + a, y + b, z + c - 1, ConfigBlocks.blockAlchemyFurnace, 1, 3);
                            world.setBlock(x + a, y + b, z + c + 1, ConfigBlocks.blockAlchemyFurnace, 1, 3);
                            world.setBlock(x + a - 1, y + b, z + c - 1, ConfigBlocks.blockAlchemyFurnace, 4, 3);
                            world.setBlock(x + a + 1, y + b, z + c + 1, ConfigBlocks.blockAlchemyFurnace, 4, 3);
                            world.setBlock(x + a + 1, y + b, z + c - 1, ConfigBlocks.blockAlchemyFurnace, 4, 3);
                            world.setBlock(x + a - 1, y + b, z + c + 1, ConfigBlocks.blockAlchemyFurnace, 4, 3);
                            world.setBlock(x + a - 1, y + b + 1, z + c, ConfigBlocks.blockAlchemyFurnace, 3, 3);
                            world.setBlock(x + a + 1, y + b + 1, z + c, ConfigBlocks.blockAlchemyFurnace, 3, 3);
                            world.setBlock(x + a, y + b + 1, z + c - 1, ConfigBlocks.blockAlchemyFurnace, 3, 3);
                            world.setBlock(x + a, y + b + 1, z + c + 1, ConfigBlocks.blockAlchemyFurnace, 3, 3);
                            world.setBlock(x + a - 1, y + b + 1, z + c - 1, ConfigBlocks.blockAlchemyFurnace, 2, 3);
                            world.setBlock(x + a + 1, y + b + 1, z + c + 1, ConfigBlocks.blockAlchemyFurnace, 2, 3);
                            world.setBlock(x + a + 1, y + b + 1, z + c - 1, ConfigBlocks.blockAlchemyFurnace, 2, 3);
                            world.setBlock(x + a - 1, y + b + 1, z + c + 1, ConfigBlocks.blockAlchemyFurnace, 2, 3);
                            world.playSoundEffect((double) (x + a) + (double) 0.5F, (double) (y + b) + (double) 0.5F, (double) (z + c) + (double) 0.5F, "thaumcraft:wand", 1.0F, 1.0F);

                            for (int aa = -1; aa <= 1; ++aa) {
                                for (int bb = 0; bb <= 1; ++bb) {
                                    for (int cc = -1; cc <= 1; ++cc) {
                                        PacketHandler.INSTANCE.sendToAllAround(new PacketFXBlockSparkle(x + a + aa, y + b + bb, z + c + cc, -9999), new NetworkRegistry.TargetPoint(world.dimension(), x + a, y + b, z + c, 32.0F));
                                    }
                                }
                            }

                            return true;
                        }
                    }
                }
            }

        }
        return false;
    }
}
