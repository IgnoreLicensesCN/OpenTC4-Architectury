package thaumcraft.api;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IEssentiaTransport;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.items.wands.wandtypes.WandCastingItem;
import thaumcraft.common.items.wands.WandManager;
import thaumcraft.common.lib.crafting.ThaumcraftCraftingManager;
import thaumcraft.common.lib.research.ResearchManager;
import thaumcraft.common.tiles.TileMagicWorkbench;

import java.util.HashMap;
import java.util.Objects;


public class ThaumcraftApiHelper {

    public static AspectList cullTags(AspectList temp) {
        AspectList temp2 = new AspectList();
        for (Aspect tag : temp.getAspectTypes()) {
            if (tag != null)
                temp2.addAll(tag, temp.getAmount(tag));
        }
        while (temp2.size() > 6) {
            Aspect lowest = null;
            float low = Short.MAX_VALUE;
            for (Aspect tag : temp2.getAspectTypes()) {
                if (tag == null) continue;
                float ta = temp2.getAmount(tag);
                if (tag.isPrimal()) {
                    ta *= .9f;
                } else {
                    if (!tag.getComponents()[0].isPrimal()) {
                        ta *= 1.1f;
                        if (!tag.getComponents()[0].getComponents()[0].isPrimal()) {
                            ta *= 1.05f;
                        }
                        if (!tag.getComponents()[0].getComponents()[1].isPrimal()) {
                            ta *= 1.05f;
                        }
                    }
                    if (!tag.getComponents()[1].isPrimal()) {
                        ta *= 1.1f;
                        if (!tag.getComponents()[1].getComponents()[0].isPrimal()) {
                            ta *= 1.05f;
                        }
                        if (!tag.getComponents()[1].getComponents()[1].isPrimal()) {
                            ta *= 1.05f;
                        }
                    }
                }

                if (ta < low) {
                    low = ta;
                    lowest = tag;
                }
            }
            temp2.reduceAndRemoveIfNegative(lowest);
        }
        return temp2;
    }

//    public static boolean areItemsEqual(ItemStack s1, ItemStack s2) {
//        if (s1.isDamageableItem() && s2.isDamageableItem()) {
//            return s1.getItem() == s2.getItem();
//        } else
//            return s1.getItem() == s2.getItem() && s1.getDamageValue() == s2.getDamageValue();
//    }

    public static boolean isResearchComplete(String username, String researchkey) {
        return ResearchManager.isResearchComplete(username, researchkey);
    }

    public static boolean hasDiscoveredAspect(String username, Aspect aspect) {
        return Thaumcraft.playerKnowledge.hasDiscoveredAspect(username, aspect);
    }

    public static AspectList getDiscoveredAspects(String username) {
        return Thaumcraft.playerKnowledge.getAspectsDiscovered(username);
    }

    public static ItemStack getStackInRowAndColumn(TileMagicWorkbench instance, int row, int column) {
        return instance.getStackInRowAndColumn(row, column);
    }

    public static AspectList getObjectAspects(ItemStack is) {
        return ThaumcraftCraftingManager.getObjectTags(is);
    }

    public static AspectList getBonusObjectTags(ItemStack is, AspectList ot) {
        return ThaumcraftCraftingManager.getBonusTags(is, ot);
    }

    public static AspectList generateTags(Item item) {
        return ThaumcraftCraftingManager.generateTags(item);
    }

//    public static boolean containsMatch(boolean strict, ItemStack[] inputs, ItemStack... targets) {
//        for (ItemStack input : inputs) {
//            for (ItemStack target : targets) {
//                if (itemMatches(target, input, strict)) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }

//    public static boolean areItemStackTagsEqualForCrafting(ItemStack slotItem, ItemStack recipeItem) {
//        if (recipeItem == null || slotItem == null) return false;
//        if (recipeItem.stackTagCompound != null && slotItem.stackTagCompound == null) return false;
//        if (recipeItem.stackTagCompound == null) return true;
//
//        for (Object o : recipeItem.stackTagCompound.func_150296_c()) {//public Set func_150296_c(){return this.tagMap.keySet();}
//            String s = (String) o;
//            if (slotItem.stackTagCompound.hasKey(s)) {
//                if (!slotItem.stackTagCompound.getTag(s).toString().equals(
//                        recipeItem.stackTagCompound.getTag(s).toString())) {
//                    return false;
//                }
//            } else {
//                return false;
//            }
//
//        }
//        return true;
//    }
    public static boolean areItemStackTagsEqualForCrafting(ItemStack slotItem, ItemStack recipeItem) {
        if (recipeItem == null || slotItem == null) return false;

        CompoundTag recipeTag = recipeItem.getTag();
        CompoundTag slotTag = slotItem.getTag();

        if (recipeTag != null && slotTag == null) return false;
        if (recipeTag == null) return true;

        for (String key : recipeTag.getAllKeys()) {
            if (!slotTag.contains(key)) return false;
            if (!Objects.equals(slotTag.get(key),recipeTag.get(key))) return false;
        }

        return true;
    }

//    public static boolean itemMatches(ItemStack target, ItemStack input, boolean strict) {
//        if (input == null && target != null || input != null && target == null) {return false;}
//        if (target == null && input == null) {return true;}
//        return (Objects.equals(target.getItem(),input.getItem()) &&
//                ((ignoresDamage(target) && !strict) || target.getDamageValue() == input.getDamageValue()));
//    }


//    public static BlockEntity getConnectableTile(Level world, int x, int y, int z, Direction face) {
//        BlockEntity te = world.getBlockEntity(new BlockPos(x + face.getStepX(), y + face.getStepY(), z + face.getStepZ()));
//        if (te instanceof IEssentiaTransport && ((IEssentiaTransport) te).isConnectable(face.getOpposite()))
//            return te;
//        else
//            return null;
//    }

    public static BlockEntity getConnectableTile(BlockGetter world, int x, int y, int z, Direction face) {
        BlockEntity te = world.getBlockEntity(new BlockPos(x + face.getStepX(), y + face.getStepY(), z + face.getStepZ()));
        if (te instanceof IEssentiaTransport && ((IEssentiaTransport) te).isConnectable(face.getOpposite()))
            return te;
        else
            return null;
    }

    private static HashMap<Integer, AspectList> allAspects = new HashMap<>();
    private static HashMap<Integer, AspectList> allCompoundAspects = new HashMap<>();

    public static AspectList getAllAspects(int amount) {
        if (allAspects.get(amount) == null) {
            AspectList al = new AspectList();
            for (Aspect aspect : Aspect.aspects.values()) {
                al.addAll(aspect, amount);
            }
            allAspects.put(amount, al);
        }
        return allAspects.get(amount);
    }

    public static AspectList getAllCompoundAspects(int amount) {
        if (allCompoundAspects.get(amount) == null) {
            AspectList al = new AspectList();
            for (Aspect aspect : Aspect.getCompoundAspects()) {
                al.addAll(aspect, amount);
            }
            allCompoundAspects.put(amount, al);
        }
        return allCompoundAspects.get(amount);
    }


    /**
     * Use to subtract vis from a wand for most operations
     * Wands store vis differently so "real" vis costs need to be multiplied by 100 before calling this method
     *
     * @param wand     the wand itemstack
     * @param player   the player using the wand
     * @param cost     the cost of the operation.
     * @param doit     actually subtract the vis from the wand if true - if false just simulate the result
     * @param crafting is this a crafting operation or not - if
     *                 false then things like frugal and potency will apply to the costs
     * @return was the vis successfully subtracted
     */
    public static boolean consumeVisFromWand(ItemStack wand, Player player,
                                             AspectList cost, boolean doit, boolean crafting) {
        return wand.getItem() instanceof WandCastingItem && ((WandCastingItem) wand.getItem()).consumeAllVis(wand, player, cost, doit, crafting);
    }

    /**
     * Subtract vis for use by a crafting mechanic. Costs are calculated slightly
     * differently and things like the frugal enchant is ignored
     * Must NOT be multiplied by 100 - send the actual vis cost
     *
     * @param wand   the wand itemstack
     * @param player the player using the wand
     * @param cost   the cost of the operation.
     * @param doit   actually subtract the vis from the wand if true - if false just simulate the result
     * @return was the vis successfully subtracted
     */
    public static boolean consumeVisFromWandCrafting(ItemStack wand, Player player,
                                                     AspectList cost, boolean doit) {
        return wand.getItem() instanceof WandCastingItem && ((WandCastingItem) wand.getItem()).consumeAllVisCrafting(wand, player, cost, doit);
    }

    /**
     * Subtract vis from a wand the player is carrying. Works like consumeVisFromWand in that actual vis
     * costs should be multiplied by 100. The costs are handled like crafting however and things like
     * frugal don't effect them
     *
     * @param player the player using the wand
     * @param cost   the cost of the operation.
     * @return was the vis successfully subtracted
     */
    public static boolean consumeVisFromInventory(Player player, AspectList cost) {
        return WandManager.consumeVisFromInventory(player, cost);
    }


    /**
     * This adds permanents or temporary warp to a player. It will automatically be synced clientside
     *
     * @param player    the player using the wand
     * @param amount    how much warp to add. Negative amounts are only valid for temporary warp
     * @param temporary add temporary warp instead of permanent
     */
    public static void addWarpToPlayer(Player player, int amount, boolean temporary) {
        Thaumcraft.addWarpToPlayer(player, amount, temporary);
    }

    /**
     * This "sticky" warp to a player. Sticky warp is permanent warp that can be removed.
     * It will automatically be synced clientside
     *
     * @param player the player using the wand
     * @param amount how much warp to add. Can have negative amounts.
     */
    public static void addStickyWarpToPlayer(Player player, int amount) {
        Thaumcraft.addStickyWarpToPlayer(player, amount);
    }

    public static BlockHitResult rayTraceIgnoringSource(Level world, Vec3 start, Vec3 end, boolean ignoreSource) {
        // 构造射线上下文
        ClipContext context = new ClipContext(
                start,
                end,
                ClipContext.Block.OUTLINE, // 检测方块碰撞
                ClipContext.Fluid.NONE,    // 不检测液体
                null                       // 射线来源实体，可为空
        );

        BlockHitResult hit = world.clip(context);

        if (hit == null) return null;

        // 如果忽略起点方块
        if (ignoreSource) {
            BlockPos startPos = new BlockPos(Mth.floor(start.x()), Mth.floor(start.y()), Mth.floor(start.z()));
            if (hit.getBlockPos().equals(startPos)) {
                // 射线击中了起点方块，忽略
                return null;
            }
        }

        return hit;
    }

//    public static HitResult rayTraceIgnoringSource(Level world, Vec3 v1, Vec3 v2,
//                                                              boolean bool1, boolean bool2, boolean bool3) {
//        if (!Double.isNaN(v1.xCoord) && !Double.isNaN(v1.yCoord) && !Double.isNaN(v1.zCoord)) {
//            if (!Double.isNaN(v2.xCoord) && !Double.isNaN(v2.yCoord) && !Double.isNaN(v2.zCoord)) {
//                int i = MathHelper.floor_double(v2.xCoord);
//                int j = MathHelper.floor_double(v2.yCoord);
//                int k = MathHelper.floor_double(v2.zCoord);
//                int l = MathHelper.floor_double(v1.xCoord);
//                int i1 = MathHelper.floor_double(v1.yCoord);
//                int j1 = MathHelper.floor_double(v1.zCoord);
//                Block block = world.getBlock(l, i1, j1);
//                int k1 = world.getBlockMetadata(l, i1, j1);
//
//                HitResult HitResult2 = null;
//                k1 = 200;
//
//                while (k1-- >= 0) {
//                    if (Double.isNaN(v1.xCoord) || Double.isNaN(v1.yCoord) || Double.isNaN(v1.zCoord)) {
//                        return null;
//                    }
//
//                    if (l == i && i1 == j && j1 == k) {
//                        continue;
//                    }
//
//                    boolean flag6 = true;
//                    boolean flag3 = true;
//                    boolean flag4 = true;
//                    double d0 = 999.0D;
//                    double d1 = 999.0D;
//                    double d2 = 999.0D;
//
//                    if (i > l) {
//                        d0 = (double) l + 1.0D;
//                    } else if (i < l) {
//                        d0 = (double) l + 0.0D;
//                    } else {
//                        flag6 = false;
//                    }
//
//                    if (j > i1) {
//                        d1 = (double) i1 + 1.0D;
//                    } else if (j < i1) {
//                        d1 = (double) i1 + 0.0D;
//                    } else {
//                        flag3 = false;
//                    }
//
//                    if (k > j1) {
//                        d2 = (double) j1 + 1.0D;
//                    } else if (k < j1) {
//                        d2 = (double) j1 + 0.0D;
//                    } else {
//                        flag4 = false;
//                    }
//
//                    double d3 = 999.0D;
//                    double d4 = 999.0D;
//                    double d5 = 999.0D;
//                    double d6 = v2.xCoord - v1.xCoord;
//                    double d7 = v2.yCoord - v1.yCoord;
//                    double d8 = v2.zCoord - v1.zCoord;
//
//                    if (flag6) {
//                        d3 = (d0 - v1.xCoord) / d6;
//                    }
//
//                    if (flag3) {
//                        d4 = (d1 - v1.yCoord) / d7;
//                    }
//
//                    if (flag4) {
//                        d5 = (d2 - v1.zCoord) / d8;
//                    }
//
//                    boolean flag5 = false;
//                    byte b0;
//
//                    if (d3 < d4 && d3 < d5) {
//                        if (i > l) {
//                            b0 = 4;
//                        } else {
//                            b0 = 5;
//                        }
//
//                        v1.xCoord = d0;
//                        v1.yCoord += d7 * d3;
//                        v1.zCoord += d8 * d3;
//                    } else if (d4 < d5) {
//                        if (j > i1) {
//                            b0 = 0;
//                        } else {
//                            b0 = 1;
//                        }
//
//                        v1.xCoord += d6 * d4;
//                        v1.yCoord = d1;
//                        v1.zCoord += d8 * d4;
//                    } else {
//                        if (k > j1) {
//                            b0 = 2;
//                        } else {
//                            b0 = 3;
//                        }
//
//                        v1.xCoord += d6 * d5;
//                        v1.yCoord += d7 * d5;
//                        v1.zCoord = d2;
//                    }
//
//                    Vec3 vec32 = new Vec3(v1.xCoord, v1.yCoord, v1.zCoord);
//                    l = (int) (vec32.xCoord = MathHelper.floor_double(v1.xCoord));
//
//                    if (b0 == 5) {
//                        --l;
//                        ++vec32.xCoord;
//                    }
//
//                    i1 = (int) (vec32.yCoord = MathHelper.floor_double(v1.yCoord));
//
//                    if (b0 == 1) {
//                        --i1;
//                        ++vec32.yCoord;
//                    }
//
//                    j1 = (int) (vec32.zCoord = MathHelper.floor_double(v1.zCoord));
//
//                    if (b0 == 3) {
//                        --j1;
//                        ++vec32.zCoord;
//                    }
//
//                    Block block1 = world.getBlock(l, i1, j1);
//                    int l1 = world.getBlockMetadata(l, i1, j1);
//
//                    if (!bool2 || block1.getCollisionBoundingBoxFromPool(world, l, i1, j1) != null) {
//                        if (block1.canCollideCheck(l1, bool1)) {
//                            HitResult HitResult1 = block1.collisionRayTrace(world, l, i1, j1, v1, v2);
//
//                            if (HitResult1 != null) {
//                                return HitResult1;
//                            }
//                        } else {
//                            HitResult2 = new HitResult(l, i1, j1, b0, v1, false);
//                        }
//                    }
//                }
//
//                return bool3 ? HitResult2 : null;
//            } else {
//                return null;
//            }
//        } else {
//            return null;
//        }
//    }
}
