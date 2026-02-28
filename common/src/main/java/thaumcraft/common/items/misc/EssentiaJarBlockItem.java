package thaumcraft.common.items.misc;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IAspectContainerItem;
import thaumcraft.api.aspects.UnmodifiableAspectList;
import thaumcraft.common.blocks.ThaumcraftBlocks;
import thaumcraft.common.blocks.abstracts.IAspectContainerItemFillerBlock;
import thaumcraft.common.tiles.crafted.EssentiaJarBlockEntity;

import static com.linearity.opentc4.Consts.EssentiaJarTagAccessors.*;

public class EssentiaJarBlockItem extends BlockItem implements IAspectContainerItem<Aspect> {
    public EssentiaJarBlockItem(Block block, Properties properties) {
        super(block, properties);
    }
    public EssentiaJarBlockItem() {
        this(ThaumcraftBlocks.ESSENTIA_JAR, new Properties().stacksTo(1));
    }
    public void setAspectAndAmount(ItemStack stack, Aspect aspect, int amount) {
        var tag = stack.getOrCreateTag();
        ASPECT.writeToCompoundTag(tag, aspect);
        AMOUNT.writeToCompoundTag(tag, amount);
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext useOnContext) {
        var level = useOnContext.getLevel();
        var pos = useOnContext.getClickedPos();
        var state = level.getBlockState(pos);
        var stack = useOnContext.getItemInHand();
        if (state.getBlock() instanceof IAspectContainerItemFillerBlock<? extends Aspect> fillerNotCasted){
            var filler = (IAspectContainerItemFillerBlock<Aspect>) fillerNotCasted;
            if (filler.canFillAspectContainerItem(
                    level,pos,state,stack,this,ASPECT_FILTER.readFromCompoundTag(stack.getOrCreateTag())
            )){
                filler.fillAspectContainerItem(
                        level,pos,state,stack,this,1
                );
            }
        }
        return super.useOn(useOnContext);
    }

    @Override
    public AspectList<Aspect> getAspects(ItemStack itemstack) {
        var tag = itemstack.getOrCreateTag();
        return UnmodifiableAspectList.of(
                ASPECT.readFromCompoundTag(tag),
                AMOUNT.readFromCompoundTag(tag)
        );
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
        for (var entry:aspects.entrySet()) {

            ASPECT.writeToCompoundTag(tag, entry.getKey());
            AMOUNT.writeToCompoundTag(tag, entry.getValue());
        }
    }



    @Override
    public int storeAspect(@NotNull Level level, @NotNull BlockPos pos, @NotNull ItemStack itemstack, @NotNull("null -> empty") Aspect aspect, int amountCanFill) {
        var remaining = amountCanFill;
        var tag = itemstack.getOrCreateTag();
        var aspCurrent = ASPECT.readFromCompoundTag(tag);
        var asmAmountCurrent = AMOUNT.readFromCompoundTag(tag);
        var filter = ASPECT_FILTER.readFromCompoundTag(tag);
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
}
