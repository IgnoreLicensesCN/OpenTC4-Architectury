package thaumcraft.api.damage.type;

import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class DamageTypeCategories {

    public static class InvulnerableTime {


        public static final Map<ResourceLocation, InvulnerableTimeCategory> INVULNERABLE_TIME_CATEGORIES_REGISTERED = new HashMap<>();
        public static final InvulnerableTimeCategory DEFAULT = InvulnerableTimeCategory.builder().build();
        public static final InvulnerableTimeCategory THORNS = InvulnerableTimeCategory.builder().timeChangeScale(0).build();

        public static void init(){
            INVULNERABLE_TIME_CATEGORIES_REGISTERED.put(ResourceLocation.tryBuild("minecraft","thorns"),THORNS);

        }
    }

    public static void init(){
        }
}
