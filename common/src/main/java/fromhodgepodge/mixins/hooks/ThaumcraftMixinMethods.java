package fromhodgepodge.mixins.hooks;

import net.minecraft.nbt.CompoundTag;
import thaumcraft.common.entities.golems.Marker;

import java.util.ArrayList;

public class ThaumcraftMixinMethods {

    public static ArrayList<Marker> overwriteMarkersDimID(NBTTagList nbtTagList, ArrayList<Marker> markers) {
        for (int i = 0; i < nbtTagList.tagCount(); i++) {
            markers.get(i).dim = nbtTagList.getCompoundTagAt(i).getInteger("dim");
        }
        return markers;
    }

}
