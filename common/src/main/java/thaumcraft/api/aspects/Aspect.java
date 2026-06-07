package thaumcraft.api.aspects;

import com.linearity.colorannotation.annotation.RGBColor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.resourcelocations.AspectResourceLocation;
import thaumcraft.common.researches.ResearchAndScannedInfo;

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
	
	public Component getName() {
		return Component.translatable(aspectKey.getNamespace()+".aspect."+aspectKey.getPath());
	}
	public String getNameString() {
		return Component.translatable(aspectKey.getNamespace()+".aspect."+aspectKey.getPath()).getString();
	}
	
	public Component getLocalizedDescription() {
		return Component.translatable(aspectKey.getNamespace()+".aspect."+aspectKey.getPath());
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
		return ResearchAndScannedInfo.getFromPlayer(player).hasResearchAspect(this);
	}
}
