package thaumcraft.common.items.clusters;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.function.Supplier;

import static com.linearity.opentc4.OpenTC4.platformUtils;
import static thaumcraft.common.items.abstracts.IDowsingTool.addDowsingResultForTag;

public class ClusterItem extends Item {
    public final Set<TagKey<Item>> forTags;
    public final Supplier<@NotNull ItemStack> willBurnInto;
    public ClusterItem(Properties properties, Set<TagKey<Item>> forTags, Supplier<@NotNull ItemStack> willBurnInto) {
        super(properties);
        this.forTags = forTags;
        this.willBurnInto = willBurnInto;
    }
    public ClusterItem(Set<TagKey<Item>> forTags, Supplier<@NotNull ItemStack> willBurnInto) {
        this(new Properties(), forTags, willBurnInto);
    }
    public ClusterItem(Set<TagKey<Item>> forTags, TagKey<Item> willBurnInto) {
        this(new Properties(), forTags, () -> {
            var foundItems = platformUtils.getItemsFromTag(willBurnInto);
            if (foundItems.isEmpty()) {
                return ItemStack.EMPTY;
            }
            return new ItemStack(foundItems.getFirst(),2).copy();
        });
    }
    public void registerDowsing(){
        forTags.forEach(tag -> addDowsingResultForTag(tag,this.getDefaultInstance(),1));
    }
    public ItemStack getOutputs(){
        return willBurnInto.get();
    }
}
