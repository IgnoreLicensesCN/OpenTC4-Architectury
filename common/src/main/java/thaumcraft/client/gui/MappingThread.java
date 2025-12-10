package thaumcraft.client.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import tc4tweak.ConfigurationHandler;
import thaumcraft.common.lib.research.ScanManager;

import static tc4tweak.ClientUtils.cacheUsed;
import static tc4tweak.ClientUtils.priorityChanged;
import static tc4tweak.ClientUtils.start;
import static thaumcraft.common.Thaumcraft.log;

public class MappingThread implements Runnable {
    Map<String, Integer> idMappings = null;

    public MappingThread(Map<String, Integer> idMappings) {
        this.idMappings = idMappings;
    }

    public static void onMappingStart(Map<String, Integer> mapping) {
        if (ConfigurationHandler.INSTANCE.isMappingThreadNice()) {
            Thread.currentThread().setPriority(1);
        } else {
            priorityChanged = true;
        }
        log.info("TC4 Mapping start. {} entries to work with.", mapping.size());
        start = System.nanoTime();
    }

    public static void onMappingDidWork() {
        if (!priorityChanged && cacheUsed.get()) {
            Thread.currentThread().setPriority(Thread.NORM_PRIORITY);
            priorityChanged = true;
        }
    }

   public static void onMappingFinished() {
      if (ConfigurationHandler.INSTANCE.isMappingThreadNice())
         log.info("TC4 Mapping finish. Took {}ns.", System.nanoTime() - start);
      else
         log.info("TC4 Mapping finish. Took {}ns. Priority boosted: {}", System.nanoTime() - start, priorityChanged);
   }
    public void run() {
        onMappingStart(idMappings);

        for (Integer id : idMappings.values()) {
            onMappingDidWork();
            try {
                Item i = Item.getItemById(id);
                if (i != null) {
                    List<ItemStack> q = new ArrayList<>();
                    i.getSubItems(i, i.getCreativeTab(), q);
                    if (!q.isEmpty()) {
                        for (ItemStack stack : q) {
                            GuiResearchRecipe.putToCache(ScanManager.generateItemHash(i, stack.getItemDamage()), stack.copy());
                        }
                    }
                } else {
                    Block b = Block.getBlockById(id);

                    for (int a = 0; a < 16; ++a) {
                        GuiResearchRecipe.putToCache(ScanManager.generateItemHash(Item.getItemFromBlock(b), a), new ItemStack(b, 1, a));
                    }
                }
            } catch (Exception ignored) {
            }
        }
       onMappingFinished();
    }
}
