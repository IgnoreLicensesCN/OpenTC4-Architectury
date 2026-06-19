package thaumcraft.common.items.wands.wandtypes;

import com.google.common.collect.MapMaker;
import com.google.common.util.concurrent.AtomicDouble;
import com.linearity.opentc4.utils.LevelBlockEntityAccessing;
import com.linearity.opentc4.utils.collectionlike.obj2intcalc.CalcCacheableCentiVisList;
import thaumcraft.api.aspects.aspectlists.baseimpl.centivis.ArrayCentiVisList;
import thaumcraft.common.items.abstracts.wandabstraction.component.*;
import thaumcraft.common.items.abstracts.wandabstraction.wand.*;
import thaumcraft.common.items.abstracts.wandabstraction.wandinteractable.IWandInteractableBlockOrBlockEntity;
import thaumcraft.common.items.abstracts.IAttackBlockListenerItem;
import com.linearity.opentc4.annotations.forvalue.PercentageFloatValue;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
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
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.aspectlists.CentiVisList;
import thaumcraft.api.wands.*;
import thaumcraft.common.items.abstracts.wandabstraction.focus.IWandFocusItem;

import java.util.*;

import static com.linearity.opentc4.Consts.WandCastingCompoundTagAccessors.*;
import static com.linearity.opentc4.OpenTC4.platformUtils;
import static thaumcraft.api.wands.WandUtils.appendWandHoverText;

//maybe just an example,you can also make you own one.
//i will use interface instead of (item instanceof WandCastingItem wandCasting)
public class WandCastingItem extends Item
        implements
        //oh it's too looooooong. but it's reasonable i have to say
        IEnchantmentRepairVisProviderItem,//if someone wants
        IArcaneCraftingVisMultiplierProviderItem,//Staff should make this not work
        IVisCostModifierOwnerItem,
        IArcaneCraftingWandItem,//Staff should make this not work
        IWandFocusEngineItem,//SceptreCastingItem should make this not work
        ICentiVisContainerItem<Aspect>,//i think this should be impl for every wand--stores centiVis
        IWandComponentsOwnerItem,//anyone wants more than cap&rod?
        IWandComponentNameOwnerItem,//get name "iron cap&wood rod wand"
        IAttackBlockListenerItem,//maybe some focus would use in some cases
        IInventoryTickableComponentItem,//ticking in inventory.add warp randomly or more?
        IWandBonusAspectOwner//easier to change(override) than what in listeners
{

    public WandCastingItem(
            Properties properties
    ){
        super(properties);
    }
    public WandCastingItem() {
        this(
                new Properties().stacksTo(1)
                        .rarity(Rarity.UNCOMMON)
        );
        platformUtils.registerOnLeftClickBlockListenerForItem(this, this);
    }

    @NotNull("null -> empty")
    protected ItemStack getWandCapAsItemStack(@NotNull ItemStack stack) {
        if (!stack.hasTag()) {
            return ItemStack.EMPTY;
        }
        var tag = stack.getTag();
        if (tag == null) {
            return ItemStack.EMPTY;
        }
        if (!WAND_CAP_ACCESSOR.compoundTagHasKey(tag)) {
            return ItemStack.EMPTY;
        }
        return WAND_CAP_ACCESSOR.readFromCompoundTag(tag);
    }

    @NotNull("null -> empty")
    protected ItemStack getWandRodAsItemStack(@NotNull ItemStack stack) {
        if (!stack.hasTag()) {
            return ItemStack.EMPTY;
        }
        var tag = stack.getTag();
        if (tag == null) {
            return ItemStack.EMPTY;
        }
        if (!WAND_ROD_ACCESSOR.compoundTagHasKey(tag)) {
            return ItemStack.EMPTY;
        }
        return WAND_ROD_ACCESSOR.readFromCompoundTag(tag);
    }

    @Override
    public List<ItemStack> getWandComponents(ItemStack componentOwnerStack) {
        int initCapacity = 2;
        if (this.canApplyFocus()){
            initCapacity += 1;
        }
        List<ItemStack> items = new ArrayList<>(initCapacity);
        var cap = getWandCapAsItemStack(componentOwnerStack);
        if (!cap.isEmpty()) {
            items.add(cap);
        }
        var rod = getWandRodAsItemStack(componentOwnerStack);
        if (!rod.isEmpty()) {
            items.add(rod);
        }
        if (this.canApplyFocus()){
            var focus = getFocusItemStack(componentOwnerStack);
            if (!focus.isEmpty()) {
                items.add(focus);
            }
        }
        return Collections.unmodifiableList(items);
    }


    //a wand wont be component for something in vanilla tc4 
    // but i want it tick in a bag.(can be a big one from thaumicTinker?)
    @Override
    public void tickAsComponent(
            @NotNull ItemStack finalParentStack,
            @NotNull ItemStack directParentStack,
            @NotNull ItemStack selfStack,
            Level level,
            Entity owner,
            int finalParentAtContainerIndex,
            boolean parentSelected
    ) {
        wandComponentsForEach(selfStack,component -> {
            if (component.getItem() instanceof IInventoryTickableComponentItem listener) {
                listener.tickAsComponent(
                        finalParentStack, selfStack, component, level, owner, finalParentAtContainerIndex, parentSelected);
            }
        });
    }

    @Override
    public void inventoryTick(ItemStack usingWand, Level level, Entity entity, int i, boolean bl) {
        this.tickAsComponent(usingWand, usingWand, usingWand, level, entity, i, bl);
    }

    @Override
    public float getCraftingVisMultiplier(ItemStack usingWand, Aspect aspect) {
        AtomicDouble result = new AtomicDouble(1);
        wandComponentsForEach(usingWand,component -> {
            if (component.getItem() instanceof IArcaneCraftingVisMultiplierProviderComponentItem provider) {
                result.updateAndGet(v ->  (v * provider.getCraftingVisMultiplier(usingWand, aspect)));
            }
        });
        return (float) result.get();
    }

    @Override
    public @NotNull ItemStack getFocusItemStack(ItemStack wand) {
        if (!wand.hasTag()) {
            return ItemStack.EMPTY;
        }
        var tag = wand.getTag();
        if (tag == null) {
            return ItemStack.EMPTY;
        }
        if (!WAND_FOCUS_ACCESSOR.compoundTagHasKey(tag)) {
            return ItemStack.EMPTY;
        }
        return WAND_FOCUS_ACCESSOR.readFromCompoundTag(tag);
    }

    @Override
    public ItemStack changeFocusItemStack(ItemStack wand, ItemStack focus) {
        if (!canApplyFocus()) {
            throw new IllegalStateException("cannot change focus but called changeFocusItemStack!");
        }
        var tag = wand.getTag();
        if (tag == null) {
            return null;
        }
        if (!WAND_FOCUS_ACCESSOR.compoundTagHasKey(tag)) {
            WAND_FOCUS_ACCESSOR.writeToCompoundTag(tag, focus);
            return null;
        }
        var result = WAND_FOCUS_ACCESSOR.readFromCompoundTag(tag);
        WAND_FOCUS_ACCESSOR.writeToCompoundTag(tag, focus);
        return result;
    }

    @Override
    public boolean tryCastAspectClass(Class<? extends Aspect> aspClass) {
        return Aspect.class.isAssignableFrom(aspClass);
    }

    @Override
    public CentiVisList<Aspect> getAllCentiVisOwning(ItemStack usingWand) {
        CompoundTag tag = usingWand.getOrCreateTag();
        return WAND_OWING_VIS_ACCESSOR.readFromCompoundTag(tag);
    }

    @Override
    public void storeCentiVisOwning(ItemStack itemStack, CentiVisList<Aspect> aspects) {
        CompoundTag tag = itemStack.getOrCreateTag();
        WAND_OWING_VIS_ACCESSOR.writeToCompoundTag(tag, aspects);
    }

    //costs high and maybe should be cached
    @Override
    public CentiVisList<Aspect> getAllCentiVisCapacity(ItemStack usingWand) {
        final CalcCacheableCentiVisList<Aspect>[] result = new CalcCacheableCentiVisList[]{CalcCacheableCentiVisList.emptySingleton()};
        wandComponentsForEach(usingWand,component -> {
            var componentItem = component.getItem();
            if (componentItem instanceof IAspectCapacityOwnerComponentItem<? extends Aspect> owner) {
                result[0] = result[0].add(
                        (CalcCacheableCentiVisList<Aspect>) owner.getCentiVisCapacity(),
                        ArrayCentiVisList::new
                );
            }
        });

        return result[0].wrapped;
    }


    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
        appendWandHoverText(this, stack, level, list, tooltipFlag, Minecraft.getInstance().player);
    }


    @Override
    public @NotNull Component getName(ItemStack itemStack) {
        var wandComponentNames = Component.empty();
        var components = getWandComponents(itemStack);
        for (var component : components) {
            if (component.getItem() instanceof IWandComponentNameOwnerComponentItem owner) {
                wandComponentNames = wandComponentNames.append(owner.getComponentName()
                        .getString());
            }
        }
        wandComponentNames = wandComponentNames.append(this.getComponentName());
        return wandComponentNames;
    }

    @Override
    public Component getComponentName() {
        return Component.translatable("item.Wand.wand.obj");
    }

    @Override
    public int getUseDuration(ItemStack itemStack) {
        return 1728000;
    }

    public static Map<LivingEntity, BlockPos> entityUsingBlockMapping = new MapMaker().weakKeys()
            .concurrencyLevel(2)
            .makeMap();

    @Override
    public @NotNull InteractionResult useOn(UseOnContext useOnContext) {
        var usingWand = useOnContext.getItemInHand();

        var focusStack = getFocusItemStack(usingWand);
        if (!focusStack.isEmpty()) {
            var focusItem = focusStack.getItem();
            if (focusItem instanceof IWandFocusItem<? extends Aspect> focus) {
                var result = focus.onFocusUseOn(useOnContext);
                if (result != null) {
                    return result;
                }
            }
        }

        var player = useOnContext.getPlayer();
        if (player != null) {
            var onBlockState = player.level()
                    .getBlockState(useOnContext.getClickedPos());
            InteractionResult result = InteractionResult.PASS;
            if (onBlockState.getBlock() instanceof IWandInteractableBlockOrBlockEntity interactableBlock) {
                if (interactableBlock.useOnWandInteractable(useOnContext) == InteractionResult.CONSUME) {
                    result = InteractionResult.CONSUME;
                    entityUsingBlockMapping.put(useOnContext.getPlayer(), useOnContext.getClickedPos());
                }
            }
            if (LevelBlockEntityAccessing.getExistingBlockEntity(player.level(), useOnContext.getClickedPos())
                    instanceof IWandInteractableBlockOrBlockEntity interactableBlock) {
                if (interactableBlock.useOnWandInteractable(useOnContext) == InteractionResult.CONSUME) {
                    result = InteractionResult.CONSUME;
                    entityUsingBlockMapping.put(useOnContext.getPlayer(), useOnContext.getClickedPos());
                }
            }
            return result;
        }
        return super.useOn(useOnContext);
    }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack usingWand, int useRemainingCount) {
        var usingBlockPos = entityUsingBlockMapping.getOrDefault(livingEntity, null);
        if (usingBlockPos != null) {
            var blockState = level.getBlockState(usingBlockPos);
            var blockEntity = LevelBlockEntityAccessing.getExistingBlockEntity(level, usingBlockPos);
            var interacting = false;
            if (blockState.getBlock() instanceof IWandInteractableBlockOrBlockEntity wandInteractableBlock) {
                wandInteractableBlock.interactOnWandInteractable(level, livingEntity, usingWand, useRemainingCount);
                interacting = true;
            }
            if (blockEntity instanceof IWandInteractableBlockOrBlockEntity wandInteractableBlock) {
                wandInteractableBlock.interactOnWandInteractable(level, livingEntity, usingWand, useRemainingCount);
                interacting = true;
            }
            if (!interacting) {
                entityUsingBlockMapping.remove(livingEntity);
            }
        } else if (canApplyFocus()) {
            var focusStack = getFocusItemStack(usingWand);
            if (!focusStack.isEmpty()) {
                var focusItem = focusStack.getItem();
                if (focusItem instanceof IWandFocusItem<? extends Aspect> focus) {
                    focus.onUsingFocusTick(usingWand, focusStack, livingEntity, useRemainingCount);
                }
            }
        }
    }

    //hope you can replace player with LivingEntity (TLM again?)
    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {

        if (canApplyFocus()) {
            var usingWand = interactionHand == InteractionHand.MAIN_HAND ? player.getMainHandItem() : player.getOffhandItem();
            var focusStack = getFocusItemStack(usingWand);
            if (!focusStack.isEmpty()) {
                var focusItem = focusStack.getItem();

                if (focusItem instanceof IWandFocusItem<? extends Aspect> focus) {
                    return focus.onFocusRightClick(usingWand, focusStack, level, player, interactionHand);
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
    public void releaseUsing(ItemStack usingWand, Level level, LivingEntity user, int useRemainingTicks) {
        entityUsingBlockMapping.remove(user);
        if (canApplyFocus()) {
            var focusStack = getFocusItemStack(usingWand);
            if (!focusStack.isEmpty()) {
                var focusItem = focusStack.getItem();
                if (focusItem instanceof IWandFocusItem<? extends Aspect> focus) {
                    focus.onStoppedUsingFocus(usingWand, focusStack, level, user, useRemainingTicks);
                }
            }
        }
    }

    @Override
    public InteractionResult onLeftClickBlock(Player user, Level level, InteractionHand interactionHand, BlockPos blockPos, Direction direction) {
        if (canApplyFocus()) {
            var usingWand = interactionHand == InteractionHand.MAIN_HAND ? user.getMainHandItem() : user.getOffhandItem();
            var focusStack = getFocusItemStack(usingWand);
            if (!focusStack.isEmpty()) {
                var focusItem = focusStack.getItem();
                if (focusItem instanceof IWandFocusItem<? extends Aspect> focus) {
                    focus.onLeftClickBlock(usingWand, focusStack, user, interactionHand);
                }
            }
        }
        return InteractionResult.PASS;
    }

//    public List<BlockPos> getArchitectBlocks(ItemStack usingWand, Level world, BlockPos pos, Direction side, Player player) {
//        if (canApplyFocus()) {
//            var focusStack = getFocusItemStack(usingWand);
//            if (!focusStack.isEmpty()) {
//                var focusItem = focusStack.getItem();
//                if (focusItem instanceof IWandFocusItem<? extends Aspect> focus
//                        && focus.isUpgradedWith(focusStack, ThaumcraftFocusUpgradeTypes.ARCHITECT)
//                        && focus instanceof IArchitectDisplayItem architect
//                ) {
//                    return architect.getArchitectBlocks(usingWand, world, pos, side, player);
//                }
//            }
//        }
//        return null;
//    }
//
//    public boolean showAxis(ItemStack usingWand, Level world, Player player, Direction side, EnumAxis axis) {
//        if (canApplyFocus()) {
//            var focusStack = getFocusItemStack(usingWand);
//            if (!focusStack.isEmpty()) {
//                var focusItem = focusStack.getItem();
//                if (focusItem instanceof IWandFocusItem<? extends Aspect> focus
//                        && focus.isUpgradedWith(focusStack, ThaumcraftFocusUpgradeTypes.ARCHITECT)
//                        && focus instanceof IArchitectDisplayItem architect
//                ) {
//                    return architect.showAxis(usingWand, world, player, side, axis);
//                }
//            }
//        }
//        return false;
//    }

    @Override
    public @NotNull UseAnim getUseAnimation(ItemStack itemStack) {
        return UseAnim.BOW;
    }

    @Override
    public @PercentageFloatValue float getCostDiscountForAspect(ItemStack wandStack, Aspect aspect) {
        var cap = getWandComponents(wandStack);
        if (cap instanceof IVisCostModifierOwnerComponentItem visCostModifierOwner) {
            return visCostModifierOwner.getSpecialCostModifierAspects().getOrDefault(aspect,visCostModifierOwner.getBaseCostModifier());
        }
        return 0;
    }
}
