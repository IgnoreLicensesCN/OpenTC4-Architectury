package thaumcraft.api.aspects;

import com.linearity.colorannotation.annotation.RGBColor;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.resourcelocations.AspectResourceLocation;

import java.util.Objects;

public abstract class Aspect {

	public final @NotNull AspectResourceLocation aspectKey;
	public final @RGBColor int color;
	public final @NotNull ResourceLocation image;
	public final int blend;

	/**
	 * Use this constructor to register your own aspects.
	 * @param aspectKey the key that will be used to reference this aspect, as well as its latin display name
	 * @param color color to display the tag in
	 * @param image ResourceLocation pointing to a 32x32 icon ofAspectVisList the aspect
	 * @param blend GL11 blendmode (1 or 771). Used for rendering nodes. Default is 1
	 */
	public Aspect(@NotNull AspectResourceLocation aspectKey, @RGBColor int color, @NotNull ResourceLocation image, int blend) {
		if (Aspects.ALL_ASPECTS.containsKey(aspectKey)) throw new IllegalArgumentException(aspectKey +" already registered!");
		this.aspectKey = aspectKey;
		this.color = color;
		this.image = image;
		this.blend = blend;
		Aspects.ALL_ASPECTS.put(aspectKey, this);
	}
	@SuppressWarnings("unused")
	protected Aspect(@NotNull AspectResourceLocation aspectKey, @RGBColor int color, @NotNull ResourceLocation image, int blend, boolean noRegisterArg) {
		this.aspectKey = aspectKey;
		this.color = color;
		this.image = image;
		this.blend = blend;
	}
	
	/**
	 * Shortcut constructor I use for the default aspects - you shouldn't be using this.
	 */
	public Aspect(AspectResourceLocation aspectKey, @RGBColor int color) {
		this(aspectKey,color,AspectResourceLocation.of(aspectKey.getNamespace(),"textures/aspects/"+ aspectKey.getPath()+".png"),1);
	}
	
	/**
	 * Shortcut constructor I use for the default aspects - you shouldn't be using this.
	 */
	public Aspect(AspectResourceLocation aspectKey, @RGBColor int color, int blend) {
		this(aspectKey,color,AspectResourceLocation.of(aspectKey.getNamespace(),"textures/aspects/"+ aspectKey.getPath()+".png"),blend);
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
	
	public static Aspect getAspect(AspectResourceLocation tag) {
		return Aspects.ALL_ASPECTS.get(tag);
	}


	@Override
	public String toString() {
		return "Aspect{" +
				"aspectKey=" + aspectKey +
				", color=" + color +
				", image=" + image +
				", blend=" + blend +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Aspect aspect)) return false;
        return color == aspect.color && blend == aspect.blend && Objects.equals(
				aspectKey, aspect.aspectKey) && Objects.equals(image, aspect.image);
	}

	@Override
	public int hashCode() {
		return Objects.hash(aspectKey, color, image, blend);
	}

	public boolean hasPlayerDiscovered(Player player) {
		return Thaumcraft.playerKnowledge.hasDiscoveredAspect(player, this);
	}
}
