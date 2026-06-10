package thaumcraft.common.tiles.crafted;

import com.linearity.opentc4.annotations.Modifiable;
import com.linearity.opentc4.annotations.RecommendedLogicalSide;
import com.linearity.opentc4.mixinaccessors.clientbe.ArcaneBoreBlockEntityClientAccessor;
import com.linearity.opentc4.utils.vanilla1710.MathHelper;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.IRepairable;
import thaumcraft.api.aspects.*;
import thaumcraft.api.aspects.aspectlists.CentiVisList;
import thaumcraft.api.aspects.aspectlists.LinkedHashCentiVisList;
import thaumcraft.api.aspects.essentiabe.IEssentiaForceInBlockEntity;
import thaumcraft.api.aspects.essentiabe.IEssentiaTransportInBlockEntity;
import thaumcraft.api.aspects.essentiabe.IEssentiaTransportOutBlockEntity;
import thaumcraft.api.wands.focus.upgrade.ThaumcraftFocusUpgradeTypes;
import thaumcraft.common.tiles.IThaumcraftBEWithMenu;
import thaumcraft.common.tiles.TileThaumcraftWithMenu;
import thaumcraft.api.visnet.VisNetHandler;
import thaumcraft.api.wands.IWandInteractableBlockOrBlockEntity;
import thaumcraft.client.fx.migrated.beams.FXBeamBore;
import thaumcraft.common.ClientFXUtils;
import thaumcraft.common.ThaumcraftSounds;
import thaumcraft.common.items.wands.foci.ExcavationFocusItem;
import thaumcraft.common.lib.enchantment.ThaumcraftEnchantments;
import thaumcraft.common.lib.network.misc.PacketBoreDigS2C;
import thaumcraft.common.lib.utils.InventoryUtils;
import thaumcraft.common.menu.menu.ArcaneBoreMenu;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;
import thaumcraft.common.tiles.abstracts.IDefaultWorldlyContainer;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import static thaumcraft.api.ThaumcraftApiHelper.rayTraceIgnoringSource;
import static thaumcraft.common.blocks.crafted.arcanebore.ArcaneBoreBaseBlock.*;
import static thaumcraft.common.blocks.crafted.arcanebore.ArcaneBoreDrillBlock.DRILL_FACING;

//TODO[Maybe wont finished]:faster this shit
public class ArcaneBoreBlockEntity
        extends TileThaumcraftWithMenu<ArcaneBoreMenu,ArcaneBoreBlockEntity>
        implements
        IDefaultWorldlyContainer,
        IEssentiaTransportInBlockEntity,
        IEssentiaForceInBlockEntity<PrimalAspect>,
        IWandInteractableBlockOrBlockEntity
{

    public static final int FOCUS_SLOT = 0;
    public static final int PICKAXE_SLOT = 1;
    public static final int MAX_RADIUS = 2;

    private int speedyTime = 0;
    private int aspectAmount = 1;
    private int repairCounter = 0;
    private int cooldownTick = 0;
    private boolean toDig;
    private float currentRadius = 0.0F;
    private float radInc = 0.0F;
    private int lastX = 0;
    private int lastY = 0;
    private int lastZ = 0;
    private @Nullable BlockPos digPos = null;
    private @NotNull CentiVisList<PrimalAspect> repairCost = new LinkedHashCentiVisList<>();
    private final @Modifiable CentiVisList<Aspect> currentRepairVis = new LinkedHashCentiVisList<>();
    public static final int[] SLOTS = { FOCUS_SLOT,PICKAXE_SLOT };
    private final NonNullList<ItemStack> inventory = NonNullList.withSize(SLOTS.length, ItemStack.EMPTY);
    private int spiral = 0;

    public ArcaneBoreBlockEntity(BlockEntityType<? extends ArcaneBoreBlockEntity> blockEntityType, BlockPos blockPos, BlockState blockState, IThaumcraftBEWithMenu.IThaumcraftBEWithMenuFactory<ArcaneBoreMenu, ArcaneBoreBlockEntity> menuFactory) {
        super(blockEntityType, blockPos, blockState, menuFactory);
    }
    public ArcaneBoreBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(ThaumcraftBlockEntities.ARCANE_BORE, blockPos, blockState, ArcaneBoreMenu::new);
    }
    public @NotNull BlockPos getDrillPos(){
        var pos = this.getBlockPos();
        var selfState = getBlockState();
        var facingToDrill = selfState.getValue(FACING_TO_DRILL);
        return pos.relative(facingToDrill);
    }
    public @NotNull Direction getDigDirection(){
        var selfState = getBlockState();
        var facingToDrill = selfState.getValue(FACING_TO_DRILL);
        var drillPos =getDrillPos();
        if (this.level == null){
            return facingToDrill;
        }
        var drillState = level.getBlockState(drillPos);
        return drillState.getValue(DRILL_FACING);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("block.thaumcraft.arcane_bore");//TODO:Separate a new name
    }

    public PrimalAspect getRequiredAspect() {
        return Aspects.ENTROPY;
    }

    public void serverTick(){
        if (this.level == null){return;}
        gainSpeedyTime();
        if (this.receivedSignal() && this.areItemsValid()) {
            this.dig();
        }
        repairPickaxe();
    }

    protected int getFortuneLevel(){
        int result = 0;
        var focusStack = getFocus();
        if (!focusStack.isEmpty() && focusStack.getItem() instanceof ExcavationFocusItem excavationFocusItem) {
            var upgrades = excavationFocusItem.getWandUpgradesWithWandModifiers(focusStack,null);
            result += upgrades.getInt(ThaumcraftFocusUpgradeTypes.TREASURE);
        }
        var pickaxeStack = getPickaxe();
        if (!pickaxeStack.isEmpty() && pickaxeStack.getItem() instanceof PickaxeItem) {
            result += EnchantmentHelper.getEnchantments(pickaxeStack).getOrDefault(Enchantments.BLOCK_FORTUNE, 0);
        }
        return result;
    }
    protected int getEnlargeLevel(){
        int result = 0;
        var focusStack = getFocus();
        if (!focusStack.isEmpty() && focusStack.getItem() instanceof ExcavationFocusItem excavationFocusItem) {
            var upgrades = excavationFocusItem.getWandUpgradesWithWandModifiers(focusStack,null);
            result += upgrades.getInt(ThaumcraftFocusUpgradeTypes.ENLARGE);
        }
        return result;
    }
    protected int getSpeedLevel(){
        int result = 0;
        var focusStack = getFocus();
        if (!focusStack.isEmpty() && focusStack.getItem() instanceof ExcavationFocusItem excavationFocusItem) {
            var upgrades = excavationFocusItem.getWandUpgradesWithWandModifiers(focusStack,null);
            result += upgrades.getInt(ThaumcraftFocusUpgradeTypes.POTENCY);
        }
        var pickaxeStack = getPickaxe();
        if (!pickaxeStack.isEmpty() && pickaxeStack.getItem() instanceof PickaxeItem) {
            result += EnchantmentHelper.getEnchantments(pickaxeStack).getOrDefault(Enchantments.BLOCK_EFFICIENCY, 0);
        }
        return result;
    }

    protected void dig(){
        if (this.level == null){return;}
        boolean dug = false;
//        if (this.base == null) {
//            this.base = (TileArcaneBoreBase) this.level.getBlockEntity(new BlockPos(pos.getX(), pos.getY() + this.baseOrientation.getOpposite().getStepY(), pos.getZ()));
//        }

        if (--cooldownTick > 0) {
            return;
        }

        if (this.toDig) {
            this.toDig = false;
            generateDrop();
            damagePickaxe();
            if (digPos != null) {
//                TODO:Add break particle(method below works only in client side?and im lazy)
//                this.level.addDestroyBlockEffect(digPos, this.level.getBlockState(digPos));
                this.level.setBlockAndUpdate(digPos, Blocks.AIR.defaultBlockState());
            }
//            if (this.base != null) {
//                for (int lb = 2; lb < 6; ++lb) {
//                    Direction lbd = Direction.getOrientation(lb);
//                    TileEntity lbte = this.level().getTileEntity(this.base.xCoord + lbd.offsetX, this.base.yCoord, this.base.zCoord + lbd.offsetZ);
//                    if (lbte instanceof TileArcaneLamp) {
//                        int d = this.level().rand.nextInt(32) * 2;
//                        int xx = this.xCoord + this.orientation.offsetX + this.orientation.offsetX * d;
//                        int yy = this.yCoord + this.orientation.offsetY + this.orientation.offsetY * d;
//                        int zz = this.zCoord + this.orientation.offsetZ + this.orientation.offsetZ * d;
//                        int p = d / 2 % 4;
//                        if (this.orientation.offsetX != 0) {
//                            zz += p == 0 ? 3 : (p != 1 && p != 3 ? -3 : 0);
//                        } else {
//                            xx += p == 0 ? 3 : (p != 1 && p != 3 ? -3 : 0);
//                        }
//
//                        if (p == 3 && this.orientation.offsetY == 0) {
//                            yy -= 2;
//                        }
//
//                        if (this.level().isAirBlock(xx, yy, zz) && this.level().getBlock(xx, yy, zz) != ConfigBlocks.blockAiry && this.level().getBlockLightValue(xx, yy, zz) < 15) {
//                            this.level().setBlock(xx, yy, zz, ConfigBlocks.blockAiry, 3, 3);
//                        }
//                        break;
//                    }
//                }
//            }

            dug = true;
        }

        this.findNextBlockToDig();
        if (dug && this.speedyTime > 0.0F) {
            --this.speedyTime;
        }
    }

    public int getMaxRadius(){
        return MAX_RADIUS;
    }

    public int getMaxRadiusWithEnlargeLevel(){
        return getEnlargeLevel() + getMaxRadius();
    }

    private void findNextBlockToDig() {
        if (this.level == null) {return;}
        int radiusWithEnlarge = getMaxRadiusWithEnlargeLevel();
        if (this.radInc == 0.0F) {
            this.radInc = (radiusWithEnlarge) / 360.0F;
        }

        int x = this.lastX;
        int z = this.lastZ;
        int y = this.lastY;
        Vec3 vres;
        var digDir = getDigDirection();
        while (x == this.lastX && y == this.lastY && z == this.lastZ) {
            this.spiral += 2;
            if (this.spiral >= 360) {
                this.spiral -= 360;
            }

            this.currentRadius += this.radInc;
            if (this.currentRadius > (float) (radiusWithEnlarge) || this.currentRadius < (float) (-(radiusWithEnlarge))) {
                this.radInc *= -1.0F;
            }
            var vsource = getDrillPos().relative(digDir).getCenter();
//            Vec3 vsource = TCVec3.createVectorHelper(
//                    (double) (this.xCoord + this.orientation.offsetX) + (double) 0.5F, (double) (this.yCoord + this.orientation.offsetY) + (double) 0.5F, (double) (this.zCoord + this.orientation.offsetZ) + (double) 0.5F);
            var vtar = new Vec3(0.0F, this.currentRadius, 0.0F);
            vtar = vtar.zRot((float) this.spiral / 180.0F * (float) Math.PI);
            vtar = vtar.yRot(((float) Math.PI / 2F) *  digDir.getStepX());
            vtar = vtar.xRot(((float) Math.PI / 2F) *  digDir.getStepY());
            vres = vsource.add(vtar);
            x = MathHelper.floor_double(vres.x);
            y = MathHelper.floor_double(vres.y);
            z = MathHelper.floor_double(vres.z);
        }

        this.lastX = x;
        this.lastZ = z;
        this.lastY = y;
        x += this.getDigDirection().getStepX();
        y += this.getDigDirection().getStepY();
        z += this.getDigDirection().getStepZ();

        for (int depth = 0; depth < 64; ++depth) {
            x += this.getDigDirection().getStepX();
            y += this.getDigDirection().getStepY();
            z += this.getDigDirection().getStepZ();
            var pickPos = new BlockPos(x, y, z);
            var blockState = this.level.getBlockState(pickPos);
//            int md = this.level().getBlockMetadata(x, y, z);
            var destroyTime = blockState.getBlock().defaultDestroyTime();
            if (destroyTime < 0.0F) {
                break;
            }

            if (!blockState.isAir()
                    && blockState.isCollisionShapeFullBlock(level, pickPos)
            ) {
                digPos = pickPos;
                int speedLvl = getSpeedLevel();
                this.cooldownTick =  Math.max(10 - speedLvl,(int) (destroyTime- speedLvl) * 2);
                if (this.speedyTime < 1.0F) {
                    this.cooldownTick *= 4;
                }

                this.toDig = true;
//                Vec3 var13 = Vec3.createVectorHelper(
//                        (double) this.xCoord + (double) 0.5F + (double) this.orientation.offsetX,
//                        (double) this.yCoord + (double) 0.5F + (double) this.orientation.offsetY,
//                        (double) this.zCoord + (double) 0.5F + (double) this.orientation.offsetZ
//                );
//                Vec3 var14 = Vec3.createVectorHelper((double) this.digX + (double) 0.5F, (double) this.digY + (double) 0.5F, (double) this.digZ + (double) 0.5F);
//                HitResult mop = this.level().func_147447_a(var13, var14, false, true, false);
//                if (mop != null) {
//                    block = this.level().getBlock(mop.blockX, mop.blockY, mop.blockZ);
//                    this.level().getBlockMetadata(mop.blockX, mop.blockY, mop.blockZ);
//                    if (block.getBlockHardness(this.level(), mop.blockX, mop.blockY, mop.blockZ) > -1.0F && block.getCollisionBoundingBoxFromPool(this.level(), mop.blockX, mop.blockY, mop.blockZ) != null) {
//                        this.count = Math.max(10 - this.speed, (int) (block.getBlockHardness(this.level(), mop.blockX, mop.blockY, mop.blockZ) * 2.0F) - this.speed * 2);
//                        if (this.speedyTime < 1.0F) {
//                            this.count *= 4;
//                        }
//
//                        this.digX = mop.blockX;
//                        this.digY = mop.blockY;
//                        this.digZ = mop.blockZ;
//                    }
//                }

                this.sendDigEvent();
                break;
            }
        }

    }
    public void sendDigEvent() {
        var selfPos = getBlockPos();
        if (!(this.level instanceof ServerLevel serverLevel)) {return;}
        new PacketBoreDigS2C(selfPos,this.digPos).sendToAllAround(serverLevel,selfPos,64);
    }

    private void damagePickaxe() {
        var pickaxe = getPickaxe();
        if (!pickaxe.isEmpty() && pickaxe.isDamageableItem()){
            var setToDamage = pickaxe.getDamageValue()+1;
            pickaxe.setDamageValue(pickaxe.getDamageValue()+1);
            if (pickaxe.getMaxDamage() > setToDamage){
                setItem(PICKAXE_SLOT,ItemStack.EMPTY);
            }
        }
    }

    private void generateDrop() {
        if (this.level == null) {return;}
        BlockPos pos = getBlockPos();
        if (digPos != null) {
            var digState = this.level.getBlockState(digPos);
            if (!digState.isAir()) {

//                    this.level.blockEvent(
//                            pos,
//                            getBlockState().getBlock(), 99, Block.getIdFromBlock(bi) + (md << 12));
                List<ItemStack> items = new ArrayList<>();
                var focusStack = getFocus();
                if (focusStack.getItem() instanceof ExcavationFocusItem focusItem) {
                    focusItem.getResourceFromBlock(
                            focusStack,
                            null,
                            this.level,
                            digState,
                            digPos,
                            items::add
                    );
                }

//                    TODO[if arcane bore produces more itemEntity than expected]:remove itemEntity
//                    List<EntityItem> targets = this.level().getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(this.digX, this.digY, this.digZ, this.digX + 1, this.digY + 1, this.digZ + 1).expand(1.0F, 1.0F, 1.0F));
//                    if (!targets.isEmpty()) {
//                        for (EntityItem e : targets) {
//                            items.add(e.getEntityItem().copy());
//                            e.setDead();
//                        }
//                    }

                if (!items.isEmpty()) {
                    for (ItemStack is : items) {
                        ItemStack dropped = is.copy();
                        //TODO:Migrate to drop listener
//                        boolean canDowsing = hasDowsing();
//                        if (!silktouch && canDowsing) {
//                            dropped = Utils.findSpecialMiningResult(
//                                    is,
//                                    0.2F + (float) fortuneLevel * 0.075F,
//                                    this.level.random
//                            );
//                        }
                        var outDir = getOutputDirection();
                        var outPos = pos.relative(outDir);
                        if (this.level.getBlockEntity(outPos) instanceof WorldlyContainer container) {
                            dropped = InventoryUtils.placeItemStackIntoInventory(
                                    dropped, container, outDir.getOpposite(), true);
                        }
                        if (!dropped.isEmpty()) {
                            var outCenter = outPos.getCenter();
                            var itemEntity = new ItemEntity(
                                    this.level,
                                    outCenter.x, outCenter.y, outCenter.z,
                                    dropped
                            );
                            this.level.addFreshEntity(itemEntity);
                        }
                    }
                }
            }
        }
    }

    protected void gainSpeedyTime(){
        var selfPos = getBlockPos();
        if (this.speedyTime < 20){
            this.speedyTime +=  Math.ceilDiv(VisNetHandler.drainCentiVis(this.level, selfPos, getRequiredAspect(), 100) , 5);
        }
        if (this.speedyTime < 20.0F) {
            drawEssentia();
            this.speedyTime += 20 * aspectAmount;
            aspectAmount = 0;
        }
    }
    protected boolean areItemsValid() {
        boolean hasPickaxeFlag = hasPickaxe();
        var pickaxeStack = getPickaxe();
        boolean notNearBroken = (!hasPickaxeFlag || pickaxeStack.getDamageValue() + 1 < pickaxeStack.getMaxDamage());

        return hasFocus() && hasPickaxeFlag && notNearBroken;
    }
    protected boolean hasFocus(){
        var focusStack = getFocus();
        return !focusStack.isEmpty() && focusStack.getItem() instanceof ExcavationFocusItem;
    }
    protected boolean hasPickaxe(){
        var pickaxeStack = getPickaxe();
        return !pickaxeStack.isEmpty() && pickaxeStack.getItem() instanceof PickaxeItem;
    }
    protected void repairPickaxe(){
        var selfPos = getBlockPos();
        var pickaxeStack = getPickaxe();
        if (!pickaxeStack.isEmpty()) {
            if (this.repairCounter++ % 40L == 0L && pickaxeStack.isDamaged()) {
                this.doRepair(pickaxeStack);
            }

            if (!this.repairCost.isEmpty() && this.repairCounter % 5L == 0L) {
                this.repairCost.forEach(
                        (aspect,requiredAmount) -> {
                            if (this.currentRepairVis.get(aspect) < requiredAmount) {
                                this.currentRepairVis.addAll(aspect,
                                        VisNetHandler.drainCentiVis(this.level, selfPos, aspect, requiredAmount)
                                );
                            }
                        }
                );
            }
        }
    }
    @RecommendedLogicalSide(RecommendedLogicalSide.LogicalSide.SERVER)
    private void doRepair(ItemStack is) {
        int enchantmentRepairLevel = EnchantmentHelper.getEnchantments(is).getOrDefault(ThaumcraftEnchantments.REPAIR,0);
        if (enchantmentRepairLevel > 0) {
            CentiVisList<PrimalAspect> repairCostForStack = null;
            if (is.getItem() instanceof IRepairable repairable){
                repairCostForStack = repairable.getRepairCost(is,enchantmentRepairLevel);
            }else {
                repairCostForStack = IRepairable.getRepairCostDefault(is,enchantmentRepairLevel);
            }

            if (!repairCostForStack.isEmpty()) {
                this.repairCost = repairCostForStack;
                if (!repairCostForStack.forEachWithBreak(
                        ((primalAspect, requiredAmount)
                                -> this.currentRepairVis.get(primalAspect) < requiredAmount)
                )){
                    repairCostForStack.forEach(this.currentRepairVis::reduceAndRemoveIfNotPositive);
                    is.setDamageValue(is.getDamageValue()-enchantmentRepairLevel);
                    this.setChanged();
                }
            }else {
                this.repairCost.clear();
            }
        }
    }

    public ItemStack getPickaxe() {
        return getItem(PICKAXE_SLOT);
    }
    public ItemStack getFocus(){
        return getItem(FOCUS_SLOT);
    }

    @Override
    public @NotNull ItemStack getItem(int i) {
        return inventory.get(i);
    }

    @Override
    public void setItem(int i, ItemStack itemStack) {
        inventory.set(i, itemStack);
    }

    protected void drawEssentia() {
        if (aspectAmount < 0){
            aspectAmount = 0;
        }
        if (aspectAmount > 0){
            return;
        }
        var requiredAspect = getRequiredAspect();
        if (this.level != null){
            var selfPos = getBlockPos();
            for (var dir:Direction.values()){
                var pickPos = selfPos.relative(dir);
                if (level.getBlockEntity(pickPos) instanceof IEssentiaTransportOutBlockEntity outBE){
                    aspectAmount += outBE.takeEssentiaWithSuction(getSuctionAmount(dir),requiredAspect,1,dir.getOpposite());
                }
            }
        }
    }

    public void clientTick(){
        ClientTickContext.tickBE(this);
    }

    @Override
    public boolean isConnectable(@NotNull Direction face) {
        return true;
    }

    @Override
    public boolean canInputFrom(@NotNull Direction face) {
        return isConnectable(face);
    }

    @Override
    public @NotNull Aspect getSuctionType(@NotNull Direction face) {
        return getRequiredAspect();
    }

    @Override
    public int getSuctionAmount(@NotNull Direction face) {
        return 128;
    }

    @Override
    public boolean doesContainerAccept(PrimalAspect aspect) {
        return aspect == getRequiredAspect();
    }

    @Override
    public int addIntoContainer(PrimalAspect aspect, int amount) {
        if (aspect != getRequiredAspect()){
            return 0;
        }
        if (aspectAmount <= 0){
            aspectAmount += amount;
            return amount;
        }
        return 0;
    }

    @Override
    public int addEssentia(@NotNull Aspect aspect, int amount, @NotNull Direction fromDirection) {
        if (!(canInputFrom(fromDirection))) {
            return 0;
        }
        var required = getRequiredAspect();
        if (aspect != required) {
            return 0;
        }
        return addIntoContainerIfAccept(required,amount);
    }

    @Override
    public int getEssentiaAmount(@NotNull Direction face) {
        return 0;
    }

    @Override
    public @NotNull Aspect getEssentiaType(@NotNull Direction face) {
        return getRequiredAspect();
    }

    @Override
    public void setSuction(@NotNull Aspect aspect, int amount) {
    }

    public boolean receivedSignal(){
        return getBlockState().getValue(POWERED);
    }

    @Override
    public NonNullList<ItemStack> getInventory() {
        return inventory;
    }

    @Override
    public int[] getSlots() {
        return SLOTS;
    }

    public void clearRotation() {
        this.radInc = 0;
        this.currentRadius = 0;
        this.lastX = 0;
        this.lastZ = 0;
        this.lastY = 0;
        this.digPos = null;
        this.toDig = false;
    }

    public static class ClientTickContext {



        public ClientTickContext(ArcaneBoreBlockEntity be) {
            beWeakRef = new WeakReference<>(be);
            var drillDigDirection = be.getDigDirection();
            initRotationForDigDirection(drillDigDirection);
            blockPos = be.getBlockPos();
        }
        public void initRotationForDigDirection(Direction drillDigDirection){
            updateRotationForDigDirection(drillDigDirection);
            rotationManagerX.setRotation(rotationManagerX.targetRotation);
            rotationManagerZ.setRotation(rotationManagerZ.targetRotation);
        }
        public void updateRotationForDigDirection(Direction drillDigDirection){
            switch (drillDigDirection) {
                case Direction.DOWN:
                    this.setTargetRotationZ(180);
                    this.setTargetRotationX(0);
                    break;
                case Direction.UP:
                    this.setTargetRotationZ(0);
                    this.setTargetRotationX(0);
                    break;
                case Direction.NORTH:
                    this.setTargetRotationZ(90);
                    this.setTargetRotationX(270);
                    break;
                case Direction.SOUTH:
                    this.setTargetRotationZ(90);
                    this.setTargetRotationX(90);
                    break;
                case Direction.WEST:
                    this.setTargetRotationZ(90);
                    this.setTargetRotationX(0);
                    break;
                case Direction.EAST:
                    this.setTargetRotationZ(90);
                    this.setTargetRotationX(180);
            }

            this.toDig = false;
            this.paused = 100;
            this.tRadX = 0.0F;
            this.tRadZ = 0.0F;
            this.mRadX = 0.0F;
            this.mRadZ = 0.0F;
        }

        public void setTargetRotationX(int targetRotation){
            this.rotationManagerX.setTargetRotation(targetRotation);
        }
        public void setTargetRotationZ(int targetRotation){
            this.rotationManagerZ.setTargetRotation(targetRotation);
        }
        public void setRotationX(int rotation){
            this.rotationManagerX.setRotation(rotation);
        }
        public void setRotationZ(int rotation){
            this.rotationManagerZ.setRotation(rotation);
        }
        private FXBeamBore beam1;
        private FXBeamBore beam2;
        private float vRadX = 0.0F;
        private float vRadZ = 0.0F;
        private float tRadX = 0.0F;
        private float tRadZ = 0.0F;
        private float mRadX = 0.0F;
        private float mRadZ = 0.0F;
//        private int digX = 0;
//        private int digY = 0;
//        private int digZ = 0;
        private boolean toDig = false;
        private int paused = 100;
        private int topRotation = 0;
        private int maxPause = 100;
        private long soundDelay = 0L;
        private int beamLength = 0;
        private final WeakReference<ArcaneBoreBlockEntity> beWeakRef;
        public final BlockPos blockPos;
        private static class RotationManager {
            private int rotation = 0;
            private int targetRotation = 0;
            private int rotationSpeed = 0;

            public int getRotation() {
                return rotation;
            }

            public int getTargetRotation() {
                return targetRotation;
            }

            public void setTargetRotation(int targetRotation) {
                this.targetRotation = targetRotation;
            }

            public int getRotationSpeed() {
                return rotationSpeed;
            }

            public void setRotation(int rotation) {
                this.rotation = rotation;
            }

            public void moveRotation(){

                if (this.rotation < this.targetRotation) {
                    this.rotation += this.rotationSpeed;
                    if (this.rotation < this.targetRotation) {
                        ++this.rotationSpeed;
                    } else {
                        this.rotationSpeed = (int) ((float) this.rotationSpeed / 3.0F);
                    }
                } else if (this.rotation > this.targetRotation) {
                    this.rotation += this.rotationSpeed;
                    if (this.rotation > this.targetRotation) {
                        --this.rotationSpeed;
                    } else {
                        this.rotationSpeed = (int) ((float) this.rotationSpeed / 3.0F);
                    }
                } else {
                    this.rotationSpeed = 0;
                }
            }

            public boolean reachedGoal() {
                return this.rotation == this.targetRotation;
            }
        }
        private final RotationManager rotationManagerX = new RotationManager();
        private final RotationManager rotationManagerZ = new RotationManager();

        public FXBeamBore getBeam1() {
            return beam1;
        }

        public FXBeamBore getBeam2() {
            return beam2;
        }

        public float getvRadX() {
            return vRadX;
        }

        public float getvRadZ() {
            return vRadZ;
        }

        public float gettRadX() {
            return tRadX;
        }

        public float gettRadZ() {
            return tRadZ;
        }

        public float getmRadX() {
            return mRadX;
        }

        public float getmRadZ() {
            return mRadZ;
        }

        public boolean isToDig() {
            return toDig;
        }

        public int getPaused() {
            return paused;
        }

        public int getTopRotation() {
            return topRotation;
        }

        public int getMaxPause() {
            return maxPause;
        }

        public long getSoundDelay() {
            return soundDelay;
        }

        public int getBeamLength() {
            return beamLength;
        }

        public WeakReference<ArcaneBoreBlockEntity> getBEWeakRef() {
            return beWeakRef;
        }

        public BlockPos getBlockPos() {
            return blockPos;
        }

        public int getRotX(){
            return rotationManagerX.rotation;
        }

        public int getRotZ(){
            return rotationManagerZ.rotation;
        }

        public static void tickBE(ArcaneBoreBlockEntity be){
            ((ArcaneBoreBlockEntityClientAccessor)be).opentc4$getClientTickContext().tick();
        }
        private void tick() {
            rotationManagerX.moveRotation();
            rotationManagerZ.moveRotation();

            boolean reachedRotationGoal = (rotationManagerZ.reachedGoal() && rotationManagerX.reachedGoal());
            var be = beWeakRef.get();
            if (be == null){
                return;
            }

            if (!(be.level instanceof ClientLevel level)){
                return;
            }
            if ((!be.receivedSignal() && !be.areItemsValid())
                    || !(reachedRotationGoal)
            ){
                if (this.topRotation % 90 != 0) {
                    this.topRotation += Math.min(10, 90 - this.topRotation % 90);
                }

                this.vRadX *= 0.9F;
                this.vRadZ *= 0.9F;
            }
            var random = level.random;
            if (reachedRotationGoal){
                ++this.paused;

                if (this.paused < this.maxPause && this.soundDelay < System.currentTimeMillis()) {
                    this.soundDelay = System.currentTimeMillis() + 1200L +  random.nextInt(100);
                    level.playSound(
                            null,
                            blockPos,
                            ThaumcraftSounds.RUMBLE,
                            SoundSource.BLOCKS,
                            0.25F,
                            0.9F + random.nextFloat() * 0.2F
                    );
                }

                if (this.beamLength > 0 && this.paused > this.maxPause) {
                    --this.beamLength;
                }

                if (this.toDig && be.digPos != null) {
                    this.paused = 0;
                    this.beamLength = 64;
                    var diggingState = level.getBlockState(be.digPos);
                    Block block = diggingState.getBlock();
                    if (!diggingState.isAir()) {
                        int speedLvl = be.getSpeedLevel();
                        this.maxPause = 10 + Math.max(
                                10 - speedLvl,
                                (int) (block.defaultDestroyTime() * 2.0F)
                                        - speedLvl * 2);
                    } else {
                        this.maxPause = 20;
                    }

                    if (be.speedyTime <= 0.0F) {
                        this.maxPause *= 4;
                    }

                    this.toDig = false;
                    var vecFromSelfToDig = blockPos.subtract(be.digPos);
                    var xd = vecFromSelfToDig.getX();
                    var yd = vecFromSelfToDig.getY();
                    var zd = vecFromSelfToDig.getZ();
                    double var12 = MathHelper.sqrt_double(
                            xd*xd
                            + zd*zd
                    );
                    float rx = (float) (Math.atan2(zd, xd) * (double) 180.0F / Math.PI);
                    float rz = (float) (-(Math.atan2(yd, var12) * (double) 180.0F / Math.PI)) + 90.0F;
                    this.tRadX = MathHelper.wrapAngleTo180_float((float) this.rotationManagerX.getRotation()) + rx;
                    var digDirection = be.getDigDirection();
                    if (digDirection == Direction.EAST) {
                        if (this.tRadX > 180.0F) {
                            this.tRadX -= 360.0F;
                        }

                        if (this.tRadX < -180.0F) {
                            this.tRadX += 360.0F;
                        }
                    }

                    this.tRadZ = rz - (float) this.rotationManagerZ.rotation;
                    if (digDirection.getStepY() != 0) {
                        this.tRadZ += 180.0F;
                        if (this.vRadX - this.tRadX >= 180.0F) {
                            this.vRadX -= 360.0F;
                        }

                        if (this.vRadX - this.tRadX <= -180.0F) {
                            this.vRadX += 360.0F;
                        }
                    }

                    this.mRadX = Math.abs((this.vRadX - this.tRadX) / 6.0F);
                    this.mRadZ = Math.abs((this.vRadZ - this.tRadZ) / 6.0F);
//                    if (this.speedyTime > 0.0F) {
//                        --this.speedyTime;
//                    }
                }

                if (this.paused < this.maxPause) {
                    if (this.vRadX < this.tRadX) {
                        this.vRadX += this.mRadX;
                    } else if (this.vRadX > this.tRadX) {
                        this.vRadX -= this.mRadX;
                    }

                    if (this.vRadZ < this.tRadZ) {
                        this.vRadZ += this.mRadZ;
                    } else if (this.vRadZ > this.tRadZ) {
                        this.vRadZ -= this.mRadZ;
                    }
                } else {
                    this.vRadX *= 0.9F;
                    this.vRadZ *= 0.9F;
                }

                this.mRadX *= 0.9F;
                this.mRadZ *= 0.9F;
                float vx = (float) (this.rotationManagerX.rotation + 90) - this.vRadX;
                float vz = (float) (this.rotationManagerZ.rotation + 90) - this.vRadZ;
                float var3 = 1.0F;
                float dX = MathHelper.sin(vx / 180.0F * (float) Math.PI) * MathHelper.cos(vz / 180.0F * (float) Math.PI) * var3;
                float dZ = MathHelper.cos(vx / 180.0F * (float) Math.PI) * MathHelper.cos(vz / 180.0F * (float) Math.PI) * var3;
                float dY = MathHelper.sin(vz / 180.0F * (float) Math.PI) * var3;
                var rotOffsetVec = new Vec3(dX, dY, dZ);

                Vec3 start = blockPos.getCenter().add(rotOffsetVec);
                Vec3 end = blockPos.getCenter().add(rotOffsetVec.multiply(beamLength,beamLength,beamLength));
//                HitResult mop = this.level().func_147447_a(start, end, false, true, false);
                var mop = rayTraceIgnoringSource(
                        level,
                        start,
                        end,
                        true
                );
                int impact = 0;
//                float length = 64.0F;
                if (mop != null){
                    var hitVec = mop.getLocation();
                    var offsetVec = start.subtract(hitVec);
                    double xd = offsetVec.x;
                    double yd = offsetVec.y;
                    double zd = offsetVec.z;
                    double bx = hitVec.x;
                    double by = hitVec.y;
                    double bz = hitVec.z;
//                length = MathHelper.sqrt_double(xd * xd + yd * yd + zd * zd) + 0.5F;
                    impact = 5;
                    int x = MathHelper.floor_double(bx);
                    int y = MathHelper.floor_double(by);
                    int z = MathHelper.floor_double(bz);
                    var drillPosWithOffset = blockPos.relative(be.getDigDirection());
                    var genDigParticleBlockState = level.getBlockState(new BlockPos(x, y, z));
                    if (!genDigParticleBlockState.isAir()) {
                        ClientFXUtils.boreDigFx(
                                level,
                                x, y, z,
                                drillPosWithOffset.getX(),
                                drillPosWithOffset.getY(),
                                drillPosWithOffset.getZ(),
                                genDigParticleBlockState.getBlock()
                        );
                    }

                    this.topRotation += this.beamLength / 6;
                    this.beam1 = ClientFXUtils.beamBore(
                            level,
                            blockPos.getX() +  0.5,
                            blockPos.getY() +  0.5,
                            blockPos.getZ() +  0.5F,
                            bx,
                            by,
                            bz,
                            1,
                            0x00ff66,
                            true,
                            2.0F,
                            this.beam1,
                            impact
                    );
                    this.beam2 = ClientFXUtils.beamBore(
                            level,
                            blockPos.getX() +  0.5,
                            blockPos.getY() +  0.5,
                            blockPos.getZ() +  0.5,
                            bx,
                            by,
                            bz,
                            2,
                            0xff8855,
                            false,
                            2.0F,
                            this.beam2,
                            impact
                    );

                    if (be.digPos != null) {
                        var digBlockState = level.getBlockState(be.digPos);

                        if (!digBlockState.isAir()) {
                            level.playSound(
                                    null,
                                    be.digPos,
                                    digBlockState.getSoundType().getHitSound(),
                                    SoundSource.BLOCKS,
                                    (digBlockState.getSoundType().getVolume() + 1.0F) / 2.0F,
                                    digBlockState.getSoundType().getPitch() * 0.8F
                            );

                            for (int a = 0; a < ClientFXUtils.particleCount(10); ++a) {
                                ClientFXUtils.boreDigFx(
                                        level,
                                        be.digPos.getX(),
                                        be.digPos.getY(),
                                        be.digPos.getZ(),
                                        drillPosWithOffset.getX(),
                                        drillPosWithOffset.getY(),
                                        drillPosWithOffset.getZ(),
                                        digBlockState.getBlock()
                                );
                            }
                        }
                    }
                }
            }
        }

    }

    @Override
    public @NotNull InteractionResult useOnWandInteractable(UseOnContext useOnContext) {
        if (this.level != null && !this.level.isClientSide()) {
            setBlockStateAndUpdate(getBlockState().setValue(OUTPUT_FACING,useOnContext.getClickedFace().getOpposite()));
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.CONSUME;
    }

    public Direction getOutputDirection(){
        return getBlockState().getValue(OUTPUT_FACING);
    }


    public void getDigEvent(BlockPos digPos) {
        this.digPos = digPos;
        this.toDig = true;
    }
}
