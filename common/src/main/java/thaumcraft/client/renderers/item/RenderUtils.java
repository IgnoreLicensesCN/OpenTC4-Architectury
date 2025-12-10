package thaumcraft.client.renderers.item;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.linearity.opentc4.OpenTC4.platformUtils;
import static thaumcraft.common.items.ThaumcraftItems.THAUMOMETER;

public class RenderUtils {

    public static final Map<Item,ThaumcraftItemRenderer> ITEM_RENDERERS = new ConcurrentHashMap<>();

    private static boolean inited = false;

    public static void init(){
        if (inited){return;}
        inited = true;
        ITEM_RENDERERS.put(THAUMOMETER,ThaumometerItemRenderer.INSTANCE);
        platformUtils.registerModelForItem(THAUMOMETER, ResourceLocation.tryParse("thaumcraft:models/item/scanner.obj"));
        platformUtils.registerModel(ResourceLocation.tryParse("thaumcraft:models/special/hemis.obj"));
    }
}
