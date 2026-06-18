package thaumcraft.api.aspects;

import com.linearity.colorannotation.annotation.RGBColor;
import com.linearity.opentc4.chatcomponent.AspectChatComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.resourcelocations.AspectResourceLocation;
import thaumcraft.api.research.ResearchAndScannedInfo;

public abstract class Aspect {

	public final @NotNull AspectResourceLocation aspectKey;
	public final @RGBColor int color;
	public final int blend;
	private final int hash;

	/**
	 * Use this constructor to register your own aspects.
	 * @param aspectKey the key that will be used to reference this aspect, as well as its latin display name
	 * @param color color to display the tag in
	 * @param blend GL11 blendmode (1 or 771). Used for rendering nodes. Default is 1
	 */
	public Aspect(
			@NotNull AspectResourceLocation aspectKey,
			@RGBColor int color,
			int blend
	) {
		if (Aspects.ALL_ASPECTS.containsKey(aspectKey)) throw new IllegalArgumentException(aspectKey +" already registered!");
		this.aspectKey = aspectKey;
		this.color = color;
		this.blend = blend;
		Aspects.ALL_ASPECTS.put(aspectKey, this);
		this.hash = Aspects.ALL_ASPECTS.size();//all aspect with same key should be same
	}
	@SuppressWarnings("unused")
	//CONSTRUCTOR FOR EMPTY
    Aspect(
            @NotNull AspectResourceLocation aspectKey,
            @RGBColor int color,
            int blend,
            boolean noRegisterArg
    ) {
		this.aspectKey = aspectKey;
		this.color = color;
		this.blend = blend;
		this.hash = 0;
	}

	public static final Aspect EMPTY = new Aspect(
			AspectResourceLocation.of(Thaumcraft.MOD_ID,""),
			0x000000,
			1,
			true) {
		@Override
		public boolean isEmpty() {
			return true;
		}

		@Override
		public Component getName() {
			return Component.translatable("aspect.thaumcraft.empty");
		}
	};
	public static final Aspect UNKNOWN = new Aspect(
			AspectResourceLocation.of(Thaumcraft.MOD_ID,"_unknown"),
			0x000000,
			1,
			true) {
		@Override
		public boolean isEmpty() {
			return true;
		}
	};
	
	/**
	 * Shortcut constructor I use for the default aspects - you shouldn't be using this.
	 */
	public Aspect(AspectResourceLocation aspectKey, @RGBColor int color) {
		this(aspectKey,color,1);
	}

	public @RGBColor int getColor() {
		return color;
	}

	protected Component name;
	public Component getName() {
		if (name == null) {
			name = Component.translatable("aspect."+aspectKey.getNamespace()+"."+aspectKey.getPath());
		}
		return name;
	}
	protected Component nameColored;
	public Component getNameColored() {
		if (nameColored == null) {
			nameColored = getName().copy().withStyle(style -> style.withColor(color));
		}
		return nameColored;
	}
	protected String nameString;
	public String getNameString() {
		if (nameString == null) {
			nameString = getName().getString();
		}
		return nameString;
	}
	protected Component localizedDescription;
	public Component getLocalizedDescription() {
		if (localizedDescription == null) {
			localizedDescription = Component.translatable("aspect."+aspectKey.getNamespace()+".helper."+aspectKey.getPath());
		}
		return localizedDescription;
	}
	protected Component imageComponent;
	public Component getImageComponent(){
		if (imageComponent == null) {
			imageComponent = MutableComponent.create(new AspectChatComponent(this.aspectKey));
		}
		return imageComponent;
	}
	
	public @NotNull AspectResourceLocation getAspectKey() {
		return aspectKey;
	}

	public boolean isEmpty() {
		return false;
	}

	@Nullable
	public static Aspect getAspect(AspectResourceLocation tag) {
		return Aspects.ALL_ASPECTS.get(tag);
	}


	@Override
	public String toString() {
		return "Aspect{" +
				"aspectKey=" + aspectKey +
				", color=" + color +
				", blend=" + blend +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Aspect aspect)) return false;
        return this == o;
	}

	@Override
	public int hashCode() {
		return hash;
	}

	public boolean hasPlayerDiscovered(@NotNull Player player) {
		return ResearchAndScannedInfo.getFromLiving(player).hasResearchAspect(this);
	}
}
