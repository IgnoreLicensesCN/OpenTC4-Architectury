package thaumcraft.common.items.misc.jars;

import com.linearity.opentc4.annotations.SoftImplement;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;
import thaumcraft.api.aspects.*;
import thaumcraft.api.aspects.aspectlists.AspectList;
import thaumcraft.api.aspects.aspectlists.UnmodifiableAspectList;
import thaumcraft.api.listeners.aspects.item.bonus.IBonusAspectOwnerItem;
import thaumcraft.common.blocks.ThaumcraftBlocks;
import thaumcraft.common.blocks.abstracts.IAspectContainerItemFillerBlock;
import thaumcraft.common.tiles.crafted.essentiabe.jars.EssentiaJarBlockEntity;

import java.util.List;

import static com.linearity.opentc4.Consts.EssentiaJarTagAccessors.*;

public class EssentiaJarBlockItem extends BlockItem implements IAspectContainerItem<Aspect>,IAspectDisplayItem<Aspect> {
    public EssentiaJarBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    public EssentiaJarBlockItem() {
        this(ThaumcraftBlocks.ESSENTIA_JAR, new Properties().stacksTo(1));
    }

    public void setAspectAndAmount(ItemStack stack, Aspect aspect, int amount) {
        var tag = stack.getOrCreateTag();
        ASPECT.writeToCompoundTag(tag, aspect);
        AMOUNT.writeIntToCompoundTag(tag, amount);
    }

    @Override
    @SuppressWarnings("unchecked")
    public @NotNull InteractionResult useOn(UseOnContext useOnContext) {
        var level = useOnContext.getLevel();
        var pos = useOnContext.getClickedPos();
        var state = level.getBlockState(pos);
        var stack = useOnContext.getItemInHand();
        if (state.getBlock() instanceof IAspectContainerItemFillerBlock<? extends Aspect> fillerNotCasted) {
            var filler = (IAspectContainerItemFillerBlock<Aspect>) fillerNotCasted;
            if (filler.canFillAspectContainerItem(
                    level, pos, state, stack, this, ASPECT_FILTER.readFromCompoundTag(stack.getOrCreateTag())
            )) {
                if (level.isClientSide()) {
                    return InteractionResult.SUCCESS;
                }
                boolean fillResult = filler.fillAspectContainerItem(
                        level, pos, state, stack, this, 1
                );
                if (fillResult) {
                    return InteractionResult.SUCCESS;
                } else {
                    return InteractionResult.FAIL;
                }
            }
        }
        return super.useOn(useOnContext);
    }


    @Override
    public int getAspectTypeSize(ItemStack itemstack) {
        return 1;
    }

    @Override
    public int getAspectMaxVisSize(ItemStack itemstack) {
        return EssentiaJarBlockEntity.ASPECT_CAPACITY;
    }

    @Override
    public void setAspects(ItemStack itemstack, AspectList<Aspect> aspects) {
        if (aspects.size() > 1) {
            throw new IllegalArgumentException("More than one aspect to set");
        }
        var tag = itemstack.getOrCreateTag();
        aspects.forEach((aspect, aspectValue) -> {
            ASPECT.writeToCompoundTag(tag, aspect);
            AMOUNT.writeIntToCompoundTag(tag, aspectValue);
        });
    }


    @Override
    public int storeAspect(@NotNull Level level, @NotNull BlockPos pos, @NotNull ItemStack itemstack, @NotNull("null -> empty") Aspect aspect, int amountCanFill) {
        var remaining = amountCanFill;
        var jarInfo = getJarInfo(itemstack);
        var aspCurrent = jarInfo.aspect();
        var asmAmountCurrent = jarInfo.amount();
        var filter = jarInfo.filter();
        if (!filter.isEmpty() && filter != aspect) {
            return remaining;
        }
        if (!aspCurrent.isEmpty() && aspCurrent != aspect) {
            return remaining;
        }
        itemstack.shrink(1);
        var newStack = new ItemStack(this);
        var canFillSpace = getAspectMaxVisSize(newStack) - asmAmountCurrent;
        if (amountCanFill <= canFillSpace) {
            remaining = 0;
            setAspectAndAmount(newStack, aspect, amountCanFill);
        } else {
            remaining = amountCanFill - canFillSpace;
            setAspectAndAmount(newStack, aspect, asmAmountCurrent + canFillSpace);
        }
        var bPosCenter = pos.getCenter();
        level.addFreshEntity(new ItemEntity(level, bPosCenter.x(), bPosCenter.y(), bPosCenter.z(), newStack));
        return remaining;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, level, list, tooltipFlag);
        if (level == null || !level.isClientSide){
            return;
        }
        var tag = itemStack.getTag();
        if (tag == null) {
            return;
        }
        var filterAspect = ASPECT_FILTER.readFromCompoundTag(tag);
        var player = Minecraft.getInstance().player;
        if (!filterAspect.isEmpty()) {
            Component filterComponent;
            if (player != null && !filterAspect.hasPlayerDiscovered(player)) {
                filterComponent = Component.translatable("tc.aspect.unknown")
                        .withStyle(ChatFormatting.DARK_PURPLE);
            } else {
                filterComponent = filterAspect.getName()
                        .copy()
                        .withStyle(ChatFormatting.DARK_PURPLE);

            }
            list.add(filterComponent);
        }

    }

    @Override
    public @NotNull @UnmodifiableView AspectList<Aspect> getAspectsToDisplay(ItemStack stack) {
        var tag = stack.getTag();
        if (tag == null) {
            return UnmodifiableAspectList.EMPTY;
        }
        var aspCurrent = ASPECT.readFromCompoundTag(tag);
        int asmAmountCurrent = AMOUNT.readIntFromCompoundTag(tag);
        return UnmodifiableAspectList.of(aspCurrent, asmAmountCurrent);
    }

    //maybe we would have a golem core "assemble" to stick filter on.or with blocks or something?
    //or even fill directly into item?
    // but in many situations we need more than filter information,idk if it's suitable to remove this method.
    public Aspect getFilter(ItemStack stack) {
        if (stack.isEmpty()) return Aspects.EMPTY;
        var tag = stack.getTag();
        if (tag == null) return Aspects.EMPTY;
        return ASPECT_FILTER.readFromCompoundTag(tag);
    }

    public void setFilter(ItemStack stack, Aspect aspect) {
        if (!stack.isEmpty()) {
            var tag = stack.getOrCreateTag();
            ASPECT_FILTER.writeToCompoundTag(tag, aspect);
        }
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

    public int getAspectAmount(ItemStack stack) {
        if (stack.isEmpty()) return 0;
        var tag = stack.getTag();
        if (tag == null) return 0;
        return AMOUNT.readIntFromCompoundTag(tag);
    }

    public void setAspectAmount(ItemStack stack, int amount) {
        if (!stack.isEmpty()) {
            var tag = stack.getOrCreateTag();
            AMOUNT.writeIntToCompoundTag(tag, amount);
        }
    }

    public void setAspectAmdAmount(ItemStack stack, Aspect aspect, int amount) {
        if (!stack.isEmpty()) {
            var tag = stack.getOrCreateTag();
            ASPECT.writeToCompoundTag(tag, aspect);
            AMOUNT.writeIntToCompoundTag(tag, amount);
        }
    }


    public EssentiaJarInfo getJarInfo(ItemStack stack) {
        if (stack.isEmpty()) return EssentiaJarInfo.EMPTY;
        var tag = stack.getTag();
        if (tag == null) return EssentiaJarInfo.EMPTY;
        return new EssentiaJarInfo(
                ASPECT.readFromCompoundTag(tag),
                AMOUNT.readIntFromCompoundTag(tag),
                ASPECT_FILTER.readFromCompoundTag(tag)
        );
    }

    public record EssentiaJarInfo(Aspect aspect, int amount,
                                  Aspect filter) {//TODO:[maybe wont finished]if put into a single class,which package?
        public static final EssentiaJarInfo EMPTY = new EssentiaJarInfo(Aspects.EMPTY, 0, Aspects.EMPTY);
    }

    @Override
    public @NotNull AspectList<Aspect> getAspects(ItemStack itemstack) {
        var tag = itemstack.getTag();
        if (tag == null) {
            return UnmodifiableAspectList.EMPTY;
        }
        return UnmodifiableAspectList.of(
                ASPECT.readFromCompoundTag(tag),
                AMOUNT.readIntFromCompoundTag(tag)
        );
    }

    //it may cause performance issue to make impl on fabric so i won't impl for now.
    //If anyone really wants,please tell me(Issue/PR is best).
    @SoftImplement("IForgeItem")
    public int getMaxStackSize(ItemStack stack) {
        var tag = stack.getTag();
        if (tag == null) {
            return 64;
        }
        if (!ASPECT.compoundTagHasKey(tag) || !AMOUNT.compoundTagHasKey(tag)) {
            return 64;
        }
        if (ASPECT.readFromCompoundTag(tag)
                .isEmpty() || AMOUNT.readIntFromCompoundTag(tag) == 0) {
            return 64;
        }
        return 1;
    }
}
