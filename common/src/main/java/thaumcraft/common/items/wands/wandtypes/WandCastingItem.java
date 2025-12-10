package thaumcraft.common.items.wands.wandtypes;

import com.google.common.collect.MapMaker;
import com.linearity.opentc4.AttackBlockListener;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.BlockCoordinates;
import thaumcraft.api.IArchitect;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.wands.*;
import thaumcraft.common.items.wands.WandManager;

import java.util.*;

import static com.linearity.opentc4.Consts.WandCastingCompoundTagAccessors.*;
import static com.linearity.opentc4.OpenTC4.platformUtils;
import static thaumcraft.api.wands.WandUtils.appendWandHoverText;

//maybe just an example,you can also make you own one.
//i will use interface instead of (item instanceof WandCastingItem wandCasting)
public class WandCastingItem extends Item
        implements
        WandSpellEventListenable,
        WandCapOwner,
        WandRodOwner,
        EnchantmentRepairVisProvider,
        ArcaneCraftingVisProvider,
        ArcaneCraftingWand,
        WandFocusEngine,
        VisContainer,
        WandComponentsOwner,
        WandComponentNameOwner,
        AttackBlockListener,
        IArchitect {



    public WandCastingItem() {
        super(new Properties().stacksTo(1).rarity(Rarity.UNCOMMON));
        platformUtils.registerOnLeftClickBlockForItem(this,this);
    }

    @Override
    @Nullable
    public Item getWandCapAsItem(@NotNull ItemStack stack) {
        if (!stack.hasTag()){
            return null;
        }
        var tag = stack.getTag();
        if (tag == null){
            return null;
        }
        if (!WAND_CAP_ACCESSOR.compoundTagHasKey(tag)){
            return null;
        }
        var capString = WAND_CAP_ACCESSOR.readFromCompoundTag(tag);
        ResourceLocation key = new ResourceLocation(capString);
        return BuiltInRegistries.ITEM.get(key);
    }

    @Override
    @Nullable
    public Item getWandRodAsItem(@NotNull ItemStack stack) {
        if (!stack.hasTag()){
            return null;
        }
        var tag = stack.getTag();
        if (tag == null){
            return null;
        }
        if (!WAND_ROD_ACCESSOR.compoundTagHasKey(tag)){
            return null;
        }
        var rodString = WAND_ROD_ACCESSOR.readFromCompoundTag(tag);
        ResourceLocation key = new ResourceLocation(rodString);
        return BuiltInRegistries.ITEM.get(key);
    }

    @Override
    public void onWandSpellEvent(WandSpellEventType event, Player player, ItemStack usingWand, BlockPos atBlockPos, Vec3 atVec3) {
        var components = getWandComponents(usingWand);
        for (var component : components){
            if (component instanceof WandSpellEventListenable listener){
                listener.onWandSpellEvent(event, player, usingWand, atBlockPos, atVec3);
            }
        }
    }

    @Override
    public void inventoryTick(ItemStack usingWand, Level level, Entity entity, int i, boolean bl) {
        var components = getWandComponents(usingWand);
        for (var component : components){
            if (component instanceof InventoryTickableComponent listener){
                listener.tickAsComponent(usingWand, level, entity, i, bl);
            }
        }
    }

    @Override
    public float getCraftingVisMultiplier(ItemStack usingWand, Aspect aspect) {
        float result = 1.0F;
        var components = getWandComponents(usingWand);
        for (var component : components){
            if (component instanceof ArcaneCraftingVisProvider provider){
                result *= provider.getCraftingVisMultiplier(usingWand, aspect);
            }
        }

        return result;
    }

    @Override
    public boolean canProvideVisForRepair() {
        return true;
    }

    @Override
    public ItemStack getFocusItemStack(ItemStack wand) {
        if (!wand.hasTag()){
            return null;
        }
        var tag = wand.getTag();
        if (tag == null){
            return null;
        }
        if (!WAND_FOCUS_ACCESSOR.compoundTagHasKey(tag)){
            return null;
        }
        return WAND_FOCUS_ACCESSOR.readFromCompoundTag(tag);
    }

    @Override
    public ItemStack changeFocusItemStack(ItemStack wand, ItemStack focus) {
        if (!canApplyFocus()){
            throw new IllegalStateException("cannot change focus but called changeFocusItemStack!");
        }
        var tag = wand.getTag();
        if (tag == null){
            return null;
        }
        if (!WAND_FOCUS_ACCESSOR.compoundTagHasKey(tag)){
            WAND_FOCUS_ACCESSOR.writeToCompoundTag(tag, focus);
            return null;
        }
        var result = WAND_FOCUS_ACCESSOR.readFromCompoundTag(tag);
        WAND_FOCUS_ACCESSOR.writeToCompoundTag(tag, focus);
        return result;
    }

    @Override
    public Map<Aspect, Integer> getAllVisOwning(ItemStack usingWand) {
        CompoundTag tag = usingWand.getOrCreateTag();
        return WAND_OWING_VIS_ACCESSOR.readFromCompoundTag(tag);
    }
    @Override
    public void storeVisOwning(ItemStack itemStack, Map<Aspect, Integer> aspects) {
        CompoundTag tag = itemStack.getOrCreateTag();
        WAND_OWING_VIS_ACCESSOR.writeToCompoundTag(tag, aspects);
    }

    @Override
    public Map<Aspect, Integer> getAllVisCapacity(ItemStack usingWand) {
        Map<Aspect, Integer> result = new HashMap<>();
        var components = getWandComponents(usingWand);
        for (var component : components){
            if (component instanceof AspectCapacityOwner owner){
                owner.getAspectCapacity().forEach(
                        (aspect,integer) -> result.merge(aspect,integer * getVisCapacityMultiplier(),Integer::sum));
            }
        }
        return result;
    }


    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
        appendWandHoverText(this,stack, level, list, tooltipFlag);
    }

    @Override
    public List<Item> getWandComponents(ItemStack stack) {
        List<Item> items = new ArrayList<>(2);
        var cap = getWandCapAsItem(stack);
        if (cap != null) {
            items.add(cap);
        }
        var rod = getWandRodAsItem(stack);
        if (rod != null) {
            items.add(rod);
        }
        return Collections.unmodifiableList(items);
    }

    @Override
    public @NotNull Component getName(ItemStack itemStack) {
        StringBuilder wandComponentNames = new StringBuilder();
        var components = getWandComponents(itemStack);
        for (var component : components){
            if (component instanceof WandComponentNameOwner owner){
                wandComponentNames.append(owner.getComponentName().getString());
            }
        }
        wandComponentNames.append(this.getComponentName());
        return Component.literal(wandComponentNames.toString());
    }

    @Override
    public Component getComponentName() {
        return Component.translatable("item.Wand.wand.obj");
    }

    @Override
    public int getUseDuration(ItemStack itemStack) {
        return Integer.MAX_VALUE;
    }

    public static Map<LivingEntity, BlockPos> entityUsingBlockMapping = new MapMaker().weakKeys().concurrencyLevel(2).makeMap();
    @Override
    public @NotNull InteractionResult useOn(UseOnContext useOnContext) {
        var player = useOnContext.getPlayer();
        if (player != null){
            var onBlockState = player.level().getBlockState(useOnContext.getClickedPos());
            if (!onBlockState.isAir()){
                if (onBlockState.getBlock() instanceof WandInteractableBlock interactableBlock){
                    var result = interactableBlock.useOnWandInteractable(useOnContext);
                    if (result == InteractionResult.CONSUME){
                        entityUsingBlockMapping.put(useOnContext.getPlayer(), useOnContext.getClickedPos());
                    }
                    return result;
                }
            }
        }
        return super.useOn(useOnContext);
    }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack usingWand, int useCount) {
        var usingBlockPos = entityUsingBlockMapping.getOrDefault(livingEntity,null);
        if (usingBlockPos != null){
            var blockEntity = level.getBlockEntity(usingBlockPos);
            if (blockEntity instanceof WandInteractableBlock wandInteractableBlock){
                wandInteractableBlock.interact(level, livingEntity, usingWand, useCount);
            }else {
                entityUsingBlockMapping.remove(livingEntity);
            }
        }

        if (canApplyFocus()){
            var focusStack = getFocusItemStack(usingWand);
            if (focusStack != null){
                var focusItem = focusStack.getItem();
                if (focusItem instanceof IWandFocusItem focus && !WandManager.isOnCooldown(livingEntity)){
                    focus.onUsingFocusTick(usingWand,focusStack,livingEntity,useCount);
                }
            }
        }
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {

        if (canApplyFocus()){
            var usingWand = interactionHand == InteractionHand.MAIN_HAND?player.getMainHandItem():player.getOffhandItem();
            var focusStack = getFocusItemStack(usingWand);
            if (focusStack != null){
                var focusItem = focusStack.getItem();
                if (focusItem instanceof IWandFocusItem focus && !WandManager.isOnCooldown(player)){
                    return focus.onFocusRightClick(usingWand,focusStack,level,player,interactionHand);
                }
            }
        }
        return super.use(level, player, interactionHand);
    }

    @Override
    public boolean useOnRelease(ItemStack itemStack) {
        return true;
    }

    @Override
    public @NotNull ItemStack finishUsingItem(ItemStack itemStack, Level level, LivingEntity livingEntity) {
        entityUsingBlockMapping.remove(livingEntity);
        return super.finishUsingItem(itemStack, level, livingEntity);
    }

    @Override
    public void releaseUsing(ItemStack usingWand, Level level, LivingEntity user, int useCount) {
        entityUsingBlockMapping.remove(user);
        if (canApplyFocus()){
            var focusStack = getFocusItemStack(usingWand);
            if (focusStack != null){
                var focusItem = focusStack.getItem();
                if (focusItem instanceof IWandFocusItem focus && !WandManager.isOnCooldown(user)){
                    focus.onPlayerStoppedUsingFocus(usingWand,focusStack,level,user,useCount);
                }
            }
        }
    }

    @Override
    public InteractionResult onLeftClickBlock(Player user, Level level, InteractionHand interactionHand, BlockPos blockPos, Direction direction) {
        if (canApplyFocus()){
            var usingWand = interactionHand == InteractionHand.MAIN_HAND?user.getMainHandItem():user.getOffhandItem();
            var focusStack = getFocusItemStack(usingWand);
            if (focusStack != null){
                var focusItem = focusStack.getItem();
                if (focusItem instanceof IWandFocusItem focus && !WandManager.isOnCooldown(user)){
                    focus.onLeftClickBlock(usingWand,focusStack,user,interactionHand);
                }
            }
        }
        return InteractionResult.PASS;
    }

    public List<BlockCoordinates> getArchitectBlocks(ItemStack usingWand, Level world, int x, int y, int z, Direction side, Player player) {
        if (canApplyFocus()){
            var focusStack = getFocusItemStack(usingWand);
            if (focusStack != null){
                var focusItem = focusStack.getItem();
                if (focusItem instanceof IWandFocusItem focus
                        && focus.isUpgradedWith(focusStack, FocusUpgradeType.architect)
                        && focus instanceof IArchitect architect
                ){
                    return architect.getArchitectBlocks(usingWand, world, x, y, z, side, player);
                }
            }
        }
        return null;
    }

    public boolean showAxis(ItemStack usingWand, Level world, Player player, Direction side, EnumAxis axis) {
        if (canApplyFocus()){
            var focusStack = getFocusItemStack(usingWand);
            if (focusStack != null){
                var focusItem = focusStack.getItem();
                if (focusItem instanceof IWandFocusItem focus
                        && focus.isUpgradedWith(focusStack, FocusUpgradeType.architect)
                        && focus instanceof IArchitect architect
                ){
                    return architect.showAxis(usingWand,world,player,side,axis);
                }
            }
        }
        return false;
    }

    @Override
    public @NotNull UseAnim getUseAnimation(ItemStack itemStack) {
        return UseAnim.BOW;
    }
}
