package thaumcraft.common.items.dispensebehavior;

import net.minecraft.world.level.block.DispenserBlock;
import thaumcraft.common.items.ThaumcraftItemInstances;

public class ThaumcraftDispenseBehaviors {

    public static void init(){
        DispenserBlock.registerBehavior(
                ThaumcraftItemInstances.ALUMENTUM(),
                new BehaviorDispenseAlumentum());

    }
}
