package fromhodgepodge.util;

import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class AspectNameSorter implements Comparator<Aspect> {

    private static final Comparator<Aspect> INSTANCE = new AspectNameSorter();

    @Override
    public int compare(Aspect o1, Aspect o2) {
        if (o1 == null) {
            return o2 == null ? 0 : -1;
        }
        return o2 == null ? 1 : o1.getName().compareToIgnoreCase(o2.getName());
    }

    public static <Asp extends Aspect> List<Asp> sort(AspectList<Asp> list) {
        return list.getAspects().keySet().stream().sorted(INSTANCE).collect(Collectors.toList());
    }

}
