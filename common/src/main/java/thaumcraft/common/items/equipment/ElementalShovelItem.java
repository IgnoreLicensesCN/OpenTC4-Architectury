package thaumcraft.common.items.equipment;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.IArchitect;
import thaumcraft.api.IRepairEnchantable;
import thaumcraft.common.ClientFXUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static thaumcraft.api.ThaumcraftApi.TOOL_THAUMIUM_ELEMENTAL;

public class ElementalShovelItem extends ShovelItem /*ItemSpade*/ implements IRepairEnchantable, IArchitect {
    public static final Set<Block> effectiveBlockForElementalShovel = ConcurrentHashMap.newKeySet();
    public static final Set<TagKey<Block>> effectiveTagForElementalShovel = ConcurrentHashMap.newKeySet();

    static {
        // --- 单个 block ---
        effectiveBlockForElementalShovel.add(Blocks.GRASS_BLOCK);
        effectiveBlockForElementalShovel.add(Blocks.DIRT);
        effectiveBlockForElementalShovel.add(Blocks.COARSE_DIRT);
        effectiveBlockForElementalShovel.add(Blocks.PODZOL);
        effectiveBlockForElementalShovel.add(Blocks.ROOTED_DIRT);
        effectiveBlockForElementalShovel.add(Blocks.MUD);

        effectiveBlockForElementalShovel.add(Blocks.SAND);
        effectiveBlockForElementalShovel.add(Blocks.RED_SAND);

        effectiveBlockForElementalShovel.add(Blocks.GRAVEL);

        effectiveBlockForElementalShovel.add(Blocks.SNOW);
        effectiveBlockForElementalShovel.add(Blocks.SNOW_BLOCK);

        effectiveBlockForElementalShovel.add(Blocks.CLAY);

        effectiveBlockForElementalShovel.add(Blocks.FARMLAND);
        effectiveBlockForElementalShovel.add(Blocks.SOUL_SAND);
        effectiveBlockForElementalShovel.add(Blocks.SOUL_SOIL);

        effectiveBlockForElementalShovel.add(Blocks.MYCELIUM);

        // --- Tag ---
        effectiveTagForElementalShovel.add(BlockTags.DIRT);
        effectiveTagForElementalShovel.add(BlockTags.SAND);
        effectiveTagForElementalShovel.add(BlockTags.SNOW);
        effectiveTagForElementalShovel.add(BlockTags.MINEABLE_WITH_SHOVEL);//yeah crazy
//        isEffective = new Block[]{Blocks.grass, Blocks.dirt, Blocks.sand, Blocks.gravel, Blocks.snow_layer, Blocks.snow, Blocks.clay, Blocks.farmland, Blocks.soul_sand, Blocks.mycelium};
    }

    public ElementalShovelItem() {
        super(TOOL_THAUMIUM_ELEMENTAL,3,-2.8f,new Properties().stacksTo(1).rarity(Rarity.RARE));
//        this.setCreativeTab(Thaumcraft.tabTC);
    }

//    @Override
//    public Set<ToolType> getToolTypes(ItemStack stack) {
//        return ImmutableSet.of(ToolType.SHOVEL);
//    }

//    @Override
//    public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack) {
//        return par2ItemStack.isItemEqual(new ItemStack(ThaumcraftItems.THAUMIUM_INGOT)) || super.getIsRepairable(par1ItemStack, par2ItemStack);
//    }


//    @Override
//    @SideOnly(Side.CLIENT)
//    public void registerIcons(IIconRegister ir) {
//        this.icon = ir.registerIcon("thaumcraft:elementalshovel");
//    }
//
//    @Override
//    @SideOnly(Side.CLIENT)
//    public IIcon getIconFromDamage(int par1) {
//        return this.icon;
//    }

//    @Override
//    public EnumRarity getRarity(ItemStack itemstack) {
//        return EnumRarity.rare;
//    }
    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        if (player == null){
            return InteractionResult.PASS;
        }
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Direction face = context.getClickedFace();
        InteractionHand hand = context.getHand();
        ItemStack stack = context.getItemInHand();
        boolean isCrouching = player.isCrouching();


        boolean didPlace = false;

        // 你的3x3放置逻辑
        if (!isCrouching && world.getBlockEntity(pos) == null) {
            BlockState clickedState = world.getBlockState(pos);
            Block clickedBlock = clickedState.getBlock();

            BlockPos offsetPos = pos.relative(face);
            Direction facing = player.getDirection();
            boolean pitchUp = player.getXRot() < -45f;
            boolean pitchDown = player.getXRot() > 45f;
            var digDir = getDigDirection(player);

            for (int aa = -1; aa <= 1; aa++) {
                for (int bb = -1; bb <= 1; bb++) {
                    BlockPos target = offsetBySide(offsetPos, digDir, aa, bb);
                    BlockState state = world.getBlockState(target);
                    Block targetBlock = state.getBlock();

                    if (state.isAir() || state.canBeReplaced() || targetBlock instanceof BushBlock || targetBlock == Blocks.WATER) {
                        world.setBlock(target, clickedState, 3);
                        stack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
                        didPlace = true;
                        if (world instanceof ClientLevel clientLevel){
                            ClientFXUtils.blockSparkle(clientLevel, target.getX(),target.getY(),target.getZ(), 8401408, 4);
                        }
                    }
                }
            }
        }

        return didPlace ? InteractionResult.sidedSuccess(world.isClientSide) : InteractionResult.PASS;
    }


//    private boolean isEffectiveAgainst(Block block) {
//        for (Block value : isEffective) {
//            if (value == block) {
//                return true;
//            }
//        }
//
//        return false;
//    }

//    public boolean onBlockStartBreak(ItemStack itemstack, int X, int Y, int Z, Player player) {
//        HitResult HitResult = BlockUtils.getTargetBlock(player.level(), player, true);
//        if (HitResult != null && HitResult.typeOfHit == MovingObjectType.BLOCK) {
//            this.side = HitResult.sideHit;
//        }
//
//        return super.onBlockStartBreak(itemstack, X, Y, Z, player);
//    }

//    public boolean onBlockDestroyed(ItemStack stack, World world, Block bi, int x, int y, int z, EntityLivingBase ent) {
//        if (ent.isSneaking()) {
//            return super.onBlockDestroyed(stack, world, bi, x, y, z, ent);
//        } else {
//            if (Platform.getEnvironment() != Env.CLIENT) {
//                int md = world.getBlockMetadata(x, y, z);
//                if (ForgeHooks.isToolEffective(stack, bi, md) || this.isEffectiveAgainst(bi)) {
//                    for (int aa = -1; aa <= 1; ++aa) {
//                        for (int bb = -1; bb <= 1; ++bb) {
//                            int xx = 0;
//                            int yy = 0;
//                            int zz = 0;
//                            if (this.side <= 1) {
//                                xx = aa;
//                                zz = bb;
//                            } else if (this.side <= 3) {
//                                xx = aa;
//                                yy = bb;
//                            } else {
//                                zz = aa;
//                                yy = bb;
//                            }
//
//                            if (!(ent instanceof Player) || world.canMineBlock((Player) ent, x + xx, y + yy, z + zz)) {
//                                Block bl = world.getBlock(x + xx, y + yy, z + zz);
//                                md = world.getBlockMetadata(x + xx, y + yy, z + zz);
//                                if (bl.getBlockHardness(world, x + xx, y + yy, z + zz) >= 0.0F && (ForgeHooks.isToolEffective(stack, bl, md) || this.isEffectiveAgainst(bl))) {
//                                    stack.damageItem(1, ent);
//                                    BlockUtils.harvestBlock(world, (Player) ent, x + xx, y + yy, z + zz, true, 3);
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//
//            return true;
//        }
//    }

    public List<BlockPos> getArchitectBlocks(ItemStack focusStack, Level world, BlockPos pos, Direction side, Player player) {
        List<BlockPos> result = new ArrayList<>();

        // Architect 只在潜行时启用
        if (!player.isCrouching()) {
            return result;
        }

        // side offset (替代 Direction.offsetX/Y/Z)
        BlockPos offset = pos.relative(side);

        // 玩家水平朝向 (用于替代 NBT 的 orientation)
        Direction facing = player.getDirection();

        for (int aa = -1; aa <= 1; aa++) {
            for (int bb = -1; bb <= 1; bb++) {

                int dx = 0;
                int dy = 0;
                int dz = 0;

                // ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
                // 完整还原原版 TC4 Architect Mode 方向逻辑
                // ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

                if (side == Direction.UP || side == Direction.DOWN) {
                    // 上下：使用水平面旋转（旧版依赖 yaw l=0..3）
                    if (facing == Direction.NORTH || facing == Direction.SOUTH) {
                        dx = aa;
                    } else {
                        dz = aa;
                    }
                    dy = bb;
                }
                else if (side == Direction.NORTH || side == Direction.SOUTH) {
                    // 前后：使用 X-Y 面
                    dx = aa;
                    dy = bb;
                }
                else if (side == Direction.EAST || side == Direction.WEST) {
                    // 左右：使用 Z-Y 面
                    dz = aa;
                    dy = bb;
                }

                // 实际目标位置（在方块前面一格 + 平面偏移）
                BlockPos target = offset.offset(dx, dy, dz);

                BlockState state = world.getBlockState(target);

                // 判定空位或可替换（对应 vine, tallgrass, water 等）
                if (state.isAir()
                        || state.getBlock() instanceof BushBlock
                        || state.canBeReplaced()) {

                    result.add(target);
                }
            }
        }

        return result;
    }


    public boolean showAxis(ItemStack stack, Level world, Player player, Direction side, EnumAxis axis) {
        return false;
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level world, BlockState minedState, BlockPos pos, LivingEntity living) {

        // 按住潜行时使用普通挖掘（与旧版一致）
        if (living.isCrouching()) {
            return super.mineBlock(stack, world, minedState, pos, living);
        }

        // 确保只在服务器执行（Platform.getEnvironment == SERVER）
        if (!world.isClientSide) {

            // 旧版的 bi = blockState

            // 判断是否有效方块
            if (isEffective(stack, minedState)) {

                // 决定 3x3 挖掘方向 → 替代 this.side
                Direction digDir = getDigDirection(living);
                // ↑ 你可以自定义，例如 pitch/yaw 判断

                for (int ax = -1; ax <= 1; ax++) {
                    for (int ay = -1; ay <= 1; ay++) {

                        BlockPos off = offsetBySide(pos, digDir, ax, ay);

                        if (living instanceof Player player) {
                            if (!world.mayInteract(player, off)) continue;
                        }

                        BlockState st = world.getBlockState(off);

                        if (isEffective(stack, st)) {
                            // 扣耐久
                            stack.hurtAndBreak(1, living, e -> {
                                e.broadcastBreakEvent(living.getUsedItemHand());
                            });

                            // 破坏方块 + destroyBlockWithDrops掉落物
                            destroyBlockWithDrops(world, off, living);
                        }
                    }
                }
            }
        }

        return true; // 旧版的 return true；
    }


    private boolean isEffective(ItemStack stack, BlockState state) {
        return state.is(BlockTags.MINEABLE_WITH_SHOVEL)
                || effectiveTagForElementalShovel.stream().anyMatch(state::is)
                || effectiveBlockForElementalShovel.contains(state.getBlock());
    }

    private Direction getDigDirection(LivingEntity living) {
        // pitch > 45: 看地面 → 挖垂直向下
        if (living.getXRot() > 45) return Direction.DOWN;
        // pitch < -45: 看天空 → 垂直向上
        if (living.getXRot() < -45) return Direction.UP;

        // 否则：使用水平面方向
        return living.getDirection();
    }
    private BlockPos offsetBySide(BlockPos base, Direction dir, int a, int b) {

        // 和旧版逻辑对应：dir决定是偏移 XY / XZ / YZ
        return switch (dir) {
            case UP, DOWN -> base.offset(a, 0, b);
            case NORTH, SOUTH -> base.offset(a, b, 0);
            case EAST, WEST -> base.offset(0, b, a);
        };
    }
    private void destroyBlockWithDrops(Level world, BlockPos pos,LivingEntity living) {
        BlockState st = world.getBlockState(pos);
        if (st.getDestroySpeed(world, pos) >= 0 && !st.isAir()) {
            world.destroyBlock(pos, true,living);
        }
    }

}
