package thaumcraft.common.items.misc.dispensebehavior;

import net.minecraft.world.level.block.DispenserBlock;
import thaumcraft.common.config.ConfigItems;

public class ThaumcraftDispenseBehaviors {

    public static void init(){
        DispenserBlock.registerBehavior(ConfigItems.itemResource
                , new BehaviorDispenseAlumentum());//TODO

    }
}
