package thaumcraft.api.wands;

import net.minecraft.resources.ResourceLocation;
import com.linearity.opentc4.utils.StatCollector;
import org.jetbrains.annotations.UnmodifiableView;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.Thaumcraft;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @param aspects What aspects are used to calculate the cost of this upgrade.
 *                The amounts given is ignored,
 *                just the type is used for the calculation.
 */
public record FocusUpgradeType(String id, ResourceLocation icon, String name, String text, AspectList aspects) {

	private static final Map<String, FocusUpgradeType> types = new ConcurrentHashMap<>();
	public static final Map<String, FocusUpgradeType> typesView = Collections.unmodifiableMap(types);

	@UnmodifiableView
	public static Map<String, FocusUpgradeType> getTypes() {
		return typesView;
	}

	public static FocusUpgradeType getType(String id) {
		FocusUpgradeType type = types.get(id);
		if (type == null) {
			throw new IllegalArgumentException("Unknown FocusUpgradeType: " + id);
		}
		return type;
	}

	public FocusUpgradeType(String id, ResourceLocation icon, String name, String text, AspectList aspects) {
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

	public String getLocalizedName() {
		return StatCollector.translateToLocal(name);
	}

	public String getLocalizedText() {
		return StatCollector.translateToLocal(text);
	}


	// basic upgrade types
	public static FocusUpgradeType potency = new FocusUpgradeType("potency",
			new ResourceLocation(Thaumcraft.MOD_ID, "textures/foci/potency.png"),
			"focus.upgrade.potency.name", "focus.upgrade.potency.text",
			new AspectList().add(Aspect.WEAPON, 1));

	public static FocusUpgradeType frugal = new FocusUpgradeType("frugal",
			new ResourceLocation(Thaumcraft.MOD_ID, "textures/foci/frugal.png"),
			"focus.upgrade.frugal.name", "focus.upgrade.frugal.text",
			new AspectList().add(Aspect.HUNGER, 1));

	public static FocusUpgradeType treasure = new FocusUpgradeType("treasure",
			new ResourceLocation(Thaumcraft.MOD_ID, "textures/foci/treasure.png"),
			"focus.upgrade.treasure.name", "focus.upgrade.treasure.text",
			new AspectList().add(Aspect.GREED, 1));

	public static FocusUpgradeType enlarge = new FocusUpgradeType("enlarge",
			new ResourceLocation(Thaumcraft.MOD_ID, "textures/foci/enlarge.png"),
			"focus.upgrade.enlarge.name", "focus.upgrade.enlarge.text",
			new AspectList().add(Aspect.TRAVEL, 1));

	public static FocusUpgradeType alchemistsfire = new FocusUpgradeType("alchemistsfire",
			new ResourceLocation(Thaumcraft.MOD_ID, "textures/foci/alchemistsfire.png"),
			"focus.upgrade.alchemistsfire.name", "focus.upgrade.alchemistsfire.text",
			new AspectList().add(Aspect.ENERGY, 1).add(Aspect.SLIME, 1));

	public static FocusUpgradeType alchemistsfrost = new FocusUpgradeType("alchemistsfrost",
			new ResourceLocation(Thaumcraft.MOD_ID, "textures/foci/alchemistsfrost.png"),
			"focus.upgrade.alchemistsfrost.name", "focus.upgrade.alchemistsfrost.text",
			new AspectList().add(Aspect.COLD, 1).add(Aspect.TRAP, 1));

	public static FocusUpgradeType architect = new FocusUpgradeType("architect",
			new ResourceLocation(Thaumcraft.MOD_ID, "textures/foci/architect.png"),
			"focus.upgrade.architect.name", "focus.upgrade.architect.text",
			new AspectList().add(Aspect.CRAFT, 1));

	public static FocusUpgradeType extend = new FocusUpgradeType("extend",
			new ResourceLocation(Thaumcraft.MOD_ID, "textures/foci/extend.png"),
			"focus.upgrade.extend.name", "focus.upgrade.extend.text",
			new AspectList().add(Aspect.EXCHANGE, 1));

	public static FocusUpgradeType silktouch = new FocusUpgradeType("silktouch",
			new ResourceLocation(Thaumcraft.MOD_ID, "textures/foci/silktouch.png"),
			"focus.upgrade.silktouch.name", "focus.upgrade.silktouch.text",
			new AspectList().add(Aspect.GREED, 1));


}
