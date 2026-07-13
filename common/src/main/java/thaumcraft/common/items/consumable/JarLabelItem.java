package thaumcraft.common.items.consumable;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.blocks.abstracts.IAspectLabelAttachableBlock;

import static com.linearity.opentc4.Consts.JarLabelTagAccessors.LABEL_ASPECT;

public class JarLabelItem extends Item {
    public JarLabelItem(Properties properties) {
        super(properties);
    }
    public JarLabelItem() {
        super(new Properties());
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext useOnContext) {
        var level = useOnContext.getLevel();
        var pos = useOnContext.getClickedPos();
        var state = level.getBlockState(pos);
        var stack = useOnContext.getItemInHand();
        var aspect = getLabelAspect(stack);
        if (level.getBlockState(pos).getBlock() instanceof IAspectLabelAttachableBlock aspectLabelAttachableBlock
                && !aspect.isEmpty()
        ) {
            if (!level.isClientSide){
                var attached = aspectLabelAttachableBlock.attemptAttachAspectLabel(level,pos,state,aspect);
                if (attached){
                    stack.shrink(1);
                    return InteractionResult.SUCCESS;
                }
                return InteractionResult.FAIL;
            }
            return InteractionResult.sidedSuccess(true);
        }
        return super.useOn(useOnContext);
    }

    public void setLabelAspect(ItemStack stack, Aspect labelAspect) {
        LABEL_ASPECT.writeToCompoundTag(stack.getOrCreateTag(),labelAspect);
    }
    public @NotNull("could be empty") Aspect getLabelAspect(ItemStack stack) {
        if (!stack.hasTag()) {
            return Aspect.EMPTY;
        }
        var stackTag = stack.getTag();
        if (stackTag == null) {
            return Aspect.EMPTY;
        }
        return LABEL_ASPECT.readFromCompoundTag(stackTag);
    }
}
