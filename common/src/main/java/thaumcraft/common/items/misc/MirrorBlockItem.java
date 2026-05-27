package thaumcraft.common.items.misc;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.linearity.opentc4.Consts.AbstractMirrorBlockEntityTagAccessors.LINKED_DIM;
import static com.linearity.opentc4.Consts.AbstractMirrorBlockEntityTagAccessors.LINKED_POS;

public class MirrorBlockItem extends BlockItem {
    public MirrorBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, level, list, tooltipFlag);
        if (itemStack.hasTag()) {
            var tag = itemStack.getTag();
            if (tag != null){
                if (LINKED_POS.compoundTagHasKey(tag) && LINKED_DIM.compoundTagHasKey(tag)){
                    list.add(Component.translatable("tooltip.thaumcraft.mirror.linked_to"));
                    list.add(Component.literal(LINKED_POS.readFromCompoundTag(tag).toString()));
                    list.add(Component.literal(LINKED_DIM.readFromCompoundTag(tag).toString()));
                }
                if (LINKED_DIM.compoundTagHasKey(tag)){
                }
            }
        }
    }
}
