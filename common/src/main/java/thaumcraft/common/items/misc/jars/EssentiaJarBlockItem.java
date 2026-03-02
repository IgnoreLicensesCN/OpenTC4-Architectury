package thaumcraft.common.items.misc.jars;

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
import thaumcraft.api.aspects.*;
import thaumcraft.common.blocks.ThaumcraftBlocks;
import thaumcraft.common.blocks.abstracts.IAspectContainerItemFillerBlock;
import thaumcraft.common.tiles.crafted.jars.EssentiaJarBlockEntity;

import java.util.List;
import java.util.Objects;

import static com.linearity.opentc4.Consts.EssentiaJarTagAccessors.*;
import static thaumcraft.api.aspects.AspectList.addAspectDescriptionToList;

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
                if (level.isClientSide()) {
                    return InteractionResult.SUCCESS;
                }
                boolean fillResult = filler.fillAspectContainerItem(
                        level,pos,state,stack,this,1
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
        for (var entry:aspects.entrySet()) {
            ASPECT.writeToCompoundTag(tag, entry.getKey());
            AMOUNT.writeToCompoundTag(tag, entry.getValue());
        }
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
        var tag = itemStack.getTag();
        if (tag == null){return;}
        var aspCurrent = ASPECT.readFromCompoundTag(tag);
        var asmAmountCurrent = AMOUNT.readFromCompoundTag(tag);
        var filterAspect = ASPECT_FILTER.readFromCompoundTag(tag);
        var player = Minecraft.getInstance().player;
        addAspectDescriptionToList(UnmodifiableAspectList.of(aspCurrent,asmAmountCurrent),player,list);
        if (!filterAspect.isEmpty()) {
            Component filterComponent;
            if (player != null && !filterAspect.hasPlayerDiscovered(player)) {
                filterComponent = Component.translatable("tc.aspect.unknown").withStyle(ChatFormatting.DARK_PURPLE);
            }else {
                filterComponent = filterAspect.getName().copy().withStyle(ChatFormatting.DARK_PURPLE);

            }
            list.add(filterComponent);
        }

    }

    public Aspect getFilter(ItemStack stack){
        if (stack.isEmpty()) return Aspects.EMPTY;
        var tag = stack.getTag();
        if (tag == null) return Aspects.EMPTY;
        return ASPECT_FILTER.readFromCompoundTag(tag);
    }
    public void setFilter(ItemStack stack,Aspect aspect){
        if (!stack.isEmpty()){
            var tag = stack.getOrCreateTag();
            ASPECT_FILTER.writeToCompoundTag(tag,aspect);
        }
    }

    public Aspect getAspect(ItemStack stack){
        if (stack.isEmpty()) return Aspects.EMPTY;
        var tag = stack.getTag();
        if (tag == null) return Aspects.EMPTY;
        return ASPECT.readFromCompoundTag(tag);
    }
    public void setAspect(ItemStack stack,Aspect aspect){
        if (!stack.isEmpty()){
            var tag = stack.getOrCreateTag();
            ASPECT.writeToCompoundTag(tag,aspect);
        }
    }

    public int getAspectAmount(ItemStack stack){
        if (stack.isEmpty()) return 0;
        var tag = stack.getTag();
        if (tag == null) return 0;
        return AMOUNT.readFromCompoundTag(tag);
    }
    public void setAspectAmount(ItemStack stack,int amount){
        if (!stack.isEmpty()){
            var tag = stack.getOrCreateTag();
            AMOUNT.writeToCompoundTag(tag,amount);
        }
    }

    public void setAspectAmdAmount(ItemStack stack,Aspect aspect,int amount){
        if (!stack.isEmpty()){
            var tag = stack.getOrCreateTag();
            ASPECT.writeToCompoundTag(tag,aspect);
            AMOUNT.writeToCompoundTag(tag,amount);
        }
    }


    public JarInfo getJarInfo(ItemStack stack){
        if (stack.isEmpty()) return JarInfo.EMPTY;
        var tag = stack.getTag();
        if (tag == null) return JarInfo.EMPTY;
        return new JarInfo(
                ASPECT.readFromCompoundTag(tag),
                AMOUNT.readFromCompoundTag(tag),
                ASPECT_FILTER.readFromCompoundTag(tag)
                );
    }
    public record JarInfo(Aspect aspect,int amount, Aspect filter){
        @Override
        public boolean equals(Object o) {
            if (!(o instanceof JarInfo jarInfo)) return false;
            return amount == jarInfo.amount && Objects.equals(aspect, jarInfo.aspect) && Objects.equals(
                    filter, jarInfo.filter);
        }

        @Override
        public int hashCode() {
            return Objects.hash(aspect, amount, filter);
        }

        @Override
        public String toString() {
            return "JarInfo{" +
                    "aspect=" + aspect +
                    ", amount=" + amount +
                    ", filter=" + filter +
                    '}';
        }
        public static final JarInfo EMPTY = new JarInfo(Aspects.EMPTY,0,Aspects.EMPTY);
    }

    @Override
    public AspectList<Aspect> getAspects(ItemStack itemstack) {
        var tag = itemstack.getTag();
        if (tag == null){
            return UnmodifiableAspectList.EMPTY;
        }
        return UnmodifiableAspectList.of(
                ASPECT.readFromCompoundTag(tag),
                AMOUNT.readFromCompoundTag(tag)
        );
    }
}
