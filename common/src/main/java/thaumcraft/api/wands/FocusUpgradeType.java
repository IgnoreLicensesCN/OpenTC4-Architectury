package thaumcraft.api.wands;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import org.jetbrains.annotations.UnmodifiableView;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.resourcelocations.FocusUpgradeTypeResourceLocation;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @param aspects What aspects are used to calculate the cost ofAspectVisList this upgrade.
 *                The amounts given is ignored,
 *                just the type is used for the calculation.
 */
public record FocusUpgradeType(FocusUpgradeTypeResourceLocation id, ResourceLocation icon, String name, String text, AspectList<? extends Aspect>aspects) {

	private static final Map<FocusUpgradeTypeResourceLocation, FocusUpgradeType> types = new ConcurrentHashMap<>();
	public static final Map<FocusUpgradeTypeResourceLocation, FocusUpgradeType> typesView = Collections.unmodifiableMap(types);

	@UnmodifiableView
	public static Map<FocusUpgradeTypeResourceLocation, FocusUpgradeType> getTypes() {
		return typesView;
	}

	public static FocusUpgradeType getType(FocusUpgradeTypeResourceLocation id) {
		FocusUpgradeType type = types.get(id);
		if (type == null) {
			throw new IllegalArgumentException("Unknown FocusUpgradeType: " + id);
		}
		return type;
	}

	public FocusUpgradeType(FocusUpgradeTypeResourceLocation id, ResourceLocation icon, String name, String text, AspectList<? extends Aspect>aspects) {
		this.id = id;
		this.icon = icon;
		this.name = name;
		this.text = text;
		this.aspects = aspects;

		var upgradeType = types.put(id, this);
		if (upgradeType != null) {
			throw new IllegalStateException("Duplicate id " + upgradeType + " " + this);
		}
		;
//		if (id<types.length && types[id]!=null) {
//            LogManager.getLogger("THAUMCRAFT").fatal("Focus Upgrade id {} already occupied. Ignoring.", id);
//			return;
//		}
//
//		// allocate space
//		if (id>=types.length) {
//			FocusUpgradeType[] temp = new FocusUpgradeType[id+1];
//			System.arraycopy(types, 0, temp, 0, types.length);//anazor forgot to change it on github
//			types = temp;
//		}
//
//		types[id] = this;
	}

	@Override
	public String toString() {
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


	// basic upgrade types
	public static FocusUpgradeType potency = new FocusUpgradeType(FocusUpgradeTypeResourceLocation.of(Thaumcraft.MOD_ID,"potency"),
			new ResourceLocation(Thaumcraft.MOD_ID, "textures/foci/potency.png"),
			"focus.upgrade.potency.name", "focus.upgrade.potency.text",
			new AspectList<>(Aspects.WEAPON, 1));

	public static FocusUpgradeType frugal = new FocusUpgradeType(FocusUpgradeTypeResourceLocation.of(Thaumcraft.MOD_ID,"frugal"),
			new ResourceLocation(Thaumcraft.MOD_ID, "textures/foci/frugal.png"),
			"focus.upgrade.frugal.name", "focus.upgrade.frugal.text",
			new AspectList<>(Aspects.HUNGER, 1));

	public static FocusUpgradeType treasure = new FocusUpgradeType(FocusUpgradeTypeResourceLocation.of(Thaumcraft.MOD_ID,"treasure"),
			new ResourceLocation(Thaumcraft.MOD_ID, "textures/foci/treasure.png"),
			"focus.upgrade.treasure.name", "focus.upgrade.treasure.text",
			new AspectList<>(Aspects.GREED, 1));

	public static FocusUpgradeType enlarge = new FocusUpgradeType(FocusUpgradeTypeResourceLocation.of(Thaumcraft.MOD_ID,"enlarge"),
			new ResourceLocation(Thaumcraft.MOD_ID, "textures/foci/enlarge.png"),
			"focus.upgrade.enlarge.name", "focus.upgrade.enlarge.text",
			new AspectList<>(Aspects.TRAVEL, 1));

	public static FocusUpgradeType alchemistsfire = new FocusUpgradeType(FocusUpgradeTypeResourceLocation.of(Thaumcraft.MOD_ID,"alchemistsfire"),
			new ResourceLocation(Thaumcraft.MOD_ID, "textures/foci/alchemistsfire.png"),
			"focus.upgrade.alchemistsfire.name", "focus.upgrade.alchemistsfire.text",
			new AspectList<>(Aspects.ENERGY, 1).addAll(Aspects.SLIME, 1));

	public static FocusUpgradeType alchemistsfrost = new FocusUpgradeType(FocusUpgradeTypeResourceLocation.of(Thaumcraft.MOD_ID,"alchemistsfrost"),
			new ResourceLocation(Thaumcraft.MOD_ID, "textures/foci/alchemistsfrost.png"),
			"focus.upgrade.alchemistsfrost.name", "focus.upgrade.alchemistsfrost.text",
			new AspectList<>(Aspects.COLD, 1).addAll(Aspects.TRAP, 1));

	public static FocusUpgradeType architect = new FocusUpgradeType(FocusUpgradeTypeResourceLocation.of(Thaumcraft.MOD_ID,"architect"),
			new ResourceLocation(Thaumcraft.MOD_ID, "textures/foci/architect.png"),
			"focus.upgrade.architect.name", "focus.upgrade.architect.text",
			new AspectList<>(Aspects.CRAFT, 1));

	public static FocusUpgradeType extend = new FocusUpgradeType(FocusUpgradeTypeResourceLocation.of(Thaumcraft.MOD_ID,"extend"),
			new ResourceLocation(Thaumcraft.MOD_ID, "textures/foci/extend.png"),
			"focus.upgrade.extend.name", "focus.upgrade.extend.text",
			new AspectList<>(Aspects.EXCHANGE, 1));

	public static FocusUpgradeType silktouch = new FocusUpgradeType(FocusUpgradeTypeResourceLocation.of(Thaumcraft.MOD_ID,"silktouch"),
			new ResourceLocation(Thaumcraft.MOD_ID, "textures/foci/silktouch.png"),
			"focus.upgrade.silktouch.name", "focus.upgrade.silktouch.text",
			new AspectList<>(Aspects.GREED, 1));


}
