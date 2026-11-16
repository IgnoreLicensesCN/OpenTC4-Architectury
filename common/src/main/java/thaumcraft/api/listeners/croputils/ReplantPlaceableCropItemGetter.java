package thaumcraft.api.listeners.croputils;

import com.linearity.opentc4.utils.collectionlike.ListenerManager;
import lombok.AllArgsConstructor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CropBlock;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.IdentityHashMap;
import java.util.Map;

import static thaumcraft.common.lib.utils.CropUtils.useWithFakePlayerAt;


@SuppressWarnings("UnusedDeclaration")
@AllArgsConstructor
public abstract class ReplantPlaceableCropItemGetter implements Comparable<ReplantPlaceableCropItemGetter> {

    public static final IReplantPlaceableCropItem PLACE_AT_GROUND = (level, pos, stack, planter) -> useWithFakePlayerAt(stack, level, pos, Direction.DOWN);
    public static final Map<Item,IReplantPlaceableCropItem> registeredImpls = new IdentityHashMap<>();
    public static final ListenerManager<ReplantPlaceableCropItemGetter> REPLANT_GETTERS = new ListenerManager<>();
    public final int weight;

    public static @NotNull IReplantPlaceableCropItem getReplantWayFromItem(Level world, BlockPos pos, @Nullable Entity planter, ItemStack stack) {
        for (var getter:REPLANT_GETTERS.getListeners()){
            var result = getter.get(world, pos,planter , stack);
            if (result != null) {
                return result;
            }
        }
        return PLACE_AT_GROUND;
    }

    public abstract @Nullable IReplantPlaceableCropItem get(Level world, BlockPos pos, @Nullable Entity planter, ItemStack stack);

    @Override
    public int compareTo(@NotNull ReplantPlaceableCropItemGetter o) {
        return Integer.compare(weight, o.weight);
    }

    @AllArgsConstructor
    public enum DefaultImpls {
        FROM_ITEM(new ReplantPlaceableCropItemGetter(100) {
            @Override
            public @Nullable IReplantPlaceableCropItem get(Level world, BlockPos pos, @Nullable Entity planter, ItemStack stack) {
                return getByItem(world, pos,planter, stack);
            }
        }),
        FROM_REGISTERED(new ReplantPlaceableCropItemGetter(200) {
            @Override
            public @Nullable IReplantPlaceableCropItem get(Level world, BlockPos pos, @Nullable Entity planter, ItemStack stack) {
                return getFromRegistered(world, pos,planter, stack);
            }
        }),
        FROM_BLOCK_ITEM(new ReplantPlaceableCropItemGetter(300) {
            @Override
            public @Nullable IReplantPlaceableCropItem get(Level world, BlockPos pos, @Nullable Entity planter, ItemStack stack) {
                return getFromBlockItem(world, pos,planter, stack);
            }
        }),
        ;

        public final ReplantPlaceableCropItemGetter checker;

        public static @Nullable IReplantPlaceableCropItem getByItem(Level world, BlockPos pos,Entity planter, ItemStack stack) {
            if (stack.getItem() instanceof IReplantPlaceableCropItem replantPlaceableCropItem) {
                return replantPlaceableCropItem;
            }
            return null;
        }

        public static @Nullable IReplantPlaceableCropItem getFromRegistered(Level world, BlockPos pos,Entity planter, ItemStack stack) {
            return registeredImpls.get(stack.getItem());
        }

        public static @Nullable IReplantPlaceableCropItem getFromBlockItem(Level world, BlockPos pos,Entity planter, ItemStack stack) {
            if (stack.getItem() instanceof BlockItem blockItem) {
                var block = blockItem.getBlock();
                if (block instanceof CropBlock) {
                    return PLACE_AT_GROUND;
                }
            }
            return null;
        }
    }
    public static void init(){
        for (var defaultImpl : DefaultImpls.values()){
            REPLANT_GETTERS.registerListener(defaultImpl.checker);
        }
    }
}
