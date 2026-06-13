package thaumcraft.common.items.consumable;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.aspects.IAspectDisplayItem;
import thaumcraft.api.aspects.IEssentiaContainerItem;
import thaumcraft.api.aspects.aspectlists.AspectList;
import thaumcraft.api.aspects.aspectlists.unmodifiable.UnmodifiableAspectList;
import thaumcraft.common.blocks.abstracts.IEssentiaContainerItemFillableBlock;
import thaumcraft.common.blocks.abstracts.IEssentiaContainerItemFillerBlock;

import static com.linearity.opentc4.Consts.EssentiaPhialTagAccessors.ASPECT;

public class EssentiaPhialItem extends Item implements IEssentiaContainerItem<Aspect>, IAspectDisplayItem<Aspect> {
    public EssentiaPhialItem(Properties properties) {
        super(properties);
    }
    public EssentiaPhialItem() {
        super(new Properties());
    }


    @Override
    @SuppressWarnings("unchecked")
    public @NotNull InteractionResult useOn(UseOnContext useOnContext) {
        var level = useOnContext.getLevel();
        var pos = useOnContext.getClickedPos();
        var state = level.getBlockState(pos);
        var stack = useOnContext.getItemInHand();
        var essentiaOwning = getAspect(stack);
        if (essentiaOwning.isEmpty()){
            if (state.getBlock() instanceof IEssentiaContainerItemFillerBlock<? extends Aspect> fillerNotCasted) {
                var filler = (IEssentiaContainerItemFillerBlock<Aspect>) fillerNotCasted;
                if (filler.canFillEssentiaContainerItem(
                        level, pos, state, stack, this, Aspects.EMPTY)
                ) {
                    if (level.isClientSide()) {
                        return InteractionResult.sidedSuccess(level.isClientSide());
                    }
                    boolean fillResult = filler.fillEssentiaContainerItem(
                            level, pos, state, stack, this, getAspectMaxVisSize(stack)
                    );
                    if (fillResult) {
                        return InteractionResult.SUCCESS;
                    } else {
                        return InteractionResult.FAIL;
                    }
                }
            }
        }else {
            if (state.getBlock() instanceof IEssentiaContainerItemFillableBlock<? extends Aspect> fillableNotCasted) {
                var fillable = (IEssentiaContainerItemFillableBlock<Aspect>) fillableNotCasted;
                if (fillable.canBeFilledWithEssentiaContainerItem(
                        level, pos, state, stack, this, essentiaOwning)){
                    if (level.isClientSide()) {
                        return InteractionResult.sidedSuccess(level.isClientSide());
                    }
                    boolean fillResult = fillable.fillWithEssentiaContainerItem(
                            level, pos, state, stack, this,essentiaOwning, getAspectMaxVisSize(stack)
                    );
                    if (fillResult) {
                        stack.shrink(1);
                        level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), this.getDefaultInstance()));
                        return InteractionResult.SUCCESS;
                    } else {
                        return InteractionResult.FAIL;
                    }
                }
            }
        }
        return super.useOn(useOnContext);
    }


    @Override
    public int getAspectTypeSize(ItemStack itemstack) {
        return 1;
    }

    public static final int ASPECT_CAPACITY = 8;
    @Override
    public int getAspectMaxVisSize(ItemStack itemstack) {
        return ASPECT_CAPACITY;
    }

    @Override
    public void setEssentiaOwning(ItemStack itemstack, AspectList<Aspect> aspects) {
        if (aspects.size() > 1) {
            throw new IllegalArgumentException("More than one aspect to set");
        }
        var tag = itemstack.getOrCreateTag();
        aspects.forEach((aspect, aspectValue) -> {
            if (aspectValue != 8){
                throw new IllegalArgumentException("not satisfied to item");
            }
            ASPECT.writeToCompoundTag(tag, aspect);
        });
    }


    @Override
    public int storeEssentia(@NotNull Level level, @NotNull BlockPos pos, @NotNull ItemStack itemstack, @NotNull("null -> empty") Aspect aspect, int amountCanFill) {
        if (amountCanFill != this.getAspectMaxVisSize(itemstack)) {
            return amountCanFill;
        }
        var essentiaOwning = getAspect(itemstack);
        if (!essentiaOwning.isEmpty() && aspect != essentiaOwning){
            return amountCanFill;
        }
        if (aspect.isEmpty()){
            return amountCanFill;
        }
        var newStack = itemstack.split(1);
        setAspect(newStack, aspect);
        level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), newStack));
        return 0;
    }


    public Aspect getAspect(ItemStack stack) {
        if (stack.isEmpty()) return Aspects.EMPTY;
        var tag = stack.getTag();
        if (tag == null) return Aspects.EMPTY;
        return ASPECT.readFromCompoundTag(tag);
    }

    public void setAspect(ItemStack stack, Aspect aspect) {
        if (!stack.isEmpty()) {
            var tag = stack.getOrCreateTag();
            ASPECT.writeToCompoundTag(tag, aspect);
        }
    }


    @Override
    public @NotNull AspectList<Aspect> getEssentiaOwning(ItemStack itemstack) {
        var tag = itemstack.getTag();
        if (tag == null) {
            return UnmodifiableAspectList.EMPTY;
        }
        return UnmodifiableAspectList.of(
                ASPECT.readFromCompoundTag(tag),
                8
        );
    }

    @Override
    public @NotNull @UnmodifiableView AspectList<Aspect> getAspectsToDisplay(ItemStack stack) {
        return getEssentiaOwning(stack);
    }
}
