package thaumcraft.client.renderers.item;

import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.linearity.opentc4.OpenTC4.platformUtils;
import static thaumcraft.common.items.ThaumcraftItems.THAUMOMETER;

public class RenderUtils {

    public static final ListMultimap<Item, IThaumcraftItemRenderer> ITEM_RENDERERS = MultimapBuilder.hashKeys().linkedListValues().build();

    private static boolean inited = false;

    public static void init(){
        if (inited){return;}
        inited = true;
        ITEM_RENDERERS.put(THAUMOMETER,ThaumometerItemRenderer.INSTANCE);
        platformUtils.registerModelForItem(THAUMOMETER, ResourceLocation.tryParse("thaumcraft:models/item/scanner.obj"));
        platformUtils.registerModel(ResourceLocation.tryParse("thaumcraft:models/special/hemis.obj"));
    }
}
