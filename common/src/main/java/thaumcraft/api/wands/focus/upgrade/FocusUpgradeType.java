package thaumcraft.api.wands.focus.upgrade;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.linearity.opentc4.OpenTC4;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import org.jetbrains.annotations.UnmodifiableView;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.IAspectReducibleToPrimal;
import thaumcraft.api.aspects.aspectlists.AspectList;
import thaumcraft.api.aspects.aspectlists.CentiVisList;
import thaumcraft.api.aspects.aspectlists.LinkedHashCentiVisList;
import thaumcraft.api.wands.focus.IWandFocusItem;
import thaumcraft.common.lib.resourcelocations.FocusUpgradeTypeResourceLocation;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;


//maybe i should evolve this part.
public class FocusUpgradeType {
    //idk if anyone want to tag upgrade but place it here
    public static final Multimap<ResourceLocation, FocusUpgradeType> FOCUS_UPGRADE_TYPE_TAGGED = MultimapBuilder.hashKeys().hashSetValues().build();

    private static final Map<FocusUpgradeTypeResourceLocation, FocusUpgradeType> types = new ConcurrentHashMap<>();
    public static final Map<FocusUpgradeTypeResourceLocation, FocusUpgradeType> typesView = Collections.unmodifiableMap(types);

    @UnmodifiableView
    public static Map<FocusUpgradeTypeResourceLocation, FocusUpgradeType> getTypes() {
        return typesView;
    }

    public static FocusUpgradeType getType(FocusUpgradeTypeResourceLocation id) {
        FocusUpgradeType type = types.get(id);
        if (type == null) {
            OpenTC4.LOGGER.error("Unknown FocusUpgradeType: " + id);
        }
        return type;
    }

    public boolean is(ResourceLocation tag) {
        return FOCUS_UPGRADE_TYPE_TAGGED.get(tag).contains(this);
    }

    public void registerToTag(ResourceLocation tag) {
        FOCUS_UPGRADE_TYPE_TAGGED.put(tag, this);
    }

    public FocusUpgradeType(
            FocusUpgradeTypeResourceLocation id,
            ResourceLocation icon,
            String name,
            String text,
            AspectList<Aspect> aspects) {
        this.id = id;
        this.icon = icon;
        this.name = name;
        this.text = text;
        this.aspects = aspects;

        var upgradeType = types.put(id, this);
        if (upgradeType != null) {
            throw new IllegalStateException("Duplicate id " + upgradeType + " " + this);
        }
    }

	public boolean canApplyTo(ItemStack focusStack, IWandFocusItem<? extends Aspect> focusItem) {
		return true;
	}

    @Override
    public @NotNull String toString() {
        return "FocusUpgradeType{" +
                "id='" + id + '\'' +
                ", icon=" + icon +
                ", name='" + name + '\'' +
                ", text='" + text + '\'' +
                ", aspects=" + aspects +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof FocusUpgradeType) {
            return Objects.equals(this.id, ((FocusUpgradeType) obj).id);
        }
        return false;
    }

    public Component getLocalizedName() {
        return Component.translatable(name);
    }

    public Component getLocalizedText() {
        return Component.translatable(text);
    }

    public FocusUpgradeTypeResourceLocation id() {
        return id;
    }

    public ResourceLocation icon() {
        return icon;
    }

    public String name() {
        return name;
    }

    public String text() {
        return text;
    }

    public AspectList<Aspect> aspects() {
        return aspects;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, icon, name, text, aspects);
    }


    private final FocusUpgradeTypeResourceLocation id;
    private final ResourceLocation icon;
    private final String name;
    private final String text;
    private final AspectList<Aspect> aspects;

    private final Int2ObjectMap<CentiVisList<Aspect>> costCache = new Int2ObjectOpenHashMap<>();

    public CentiVisList<Aspect> getCentiVisRequiring(ItemStack focusStack, IWandFocusItem<? extends Aspect> focusItem) {
        return getCentiVisRequiring(focusStack, focusItem,focusItem.getRank(focusStack));
    }
    @Unmodifiable
    public CentiVisList<Aspect> getCentiVisRequiring(ItemStack focusStack, IWandFocusItem<? extends Aspect> focusItem,int rank) {
        return costCache.computeIfAbsent(
                rank,_rank -> {
                    CentiVisList<Aspect> result = new LinkedHashCentiVisList<>();
                    result.addAll(aspects);
                    int multiplier = 1;
                    for (int i = 1; i < _rank; i++) {
                        multiplier *= 2;
                    }
                    result.multiply(multiplier);
                    var reduced = IAspectReducibleToPrimal.reduceToPrimalsAndCast(result);
                    result.clear();
                    result.addAll(reduced);
                    return result;
                }
        );
    }
}
