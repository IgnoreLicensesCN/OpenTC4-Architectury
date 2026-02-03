package thaumcraft.api.aspects;

import com.linearity.opentc4.utils.StatCollector;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.text.WordUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;

public abstract class Aspect {

	
	public final @NotNull ResourceLocation tag;
	public final int color;
	public final @NotNull ResourceLocation image;
	public final int blend;

	/**
	 * Use this constructor to register your own aspects.
	 * @param tag the key that will be used to reference this aspect, as well as its latin display name
	 * @param color color to display the tag in
	 * @param components the aspects this one is formed from -- migrated to CompoundAspect
	 * @param image ResourceLocation pointing to a 32x32 icon of the aspect
	 * @param blend GL11 blendmode (1 or 771). Used for rendering nodes. Default is 1
	 */
	public Aspect(@NotNull ResourceLocation tag, int color, @NotNull ResourceLocation image, int blend) {
		if (Aspects.ALL_ASPECTS.containsKey(tag)) throw new IllegalArgumentException(tag+" already registered!");
		this.tag = tag;
		this.color = color;
		this.image = image;
		this.blend = blend;
		Aspects.ALL_ASPECTS.put(tag, this);
	}
	
	/**
	 * Shortcut constructor I use for the default aspects - you shouldn't be using this.
	 */
	public Aspect(ResourceLocation tag, int color) {
		this(tag,color,new ResourceLocation(tag.getNamespace(),"textures/aspects/"+tag.getPath()+".png"),1);
	}
	
	/**
	 * Shortcut constructor I use for the default aspects - you shouldn't be using this.
	 */
	public Aspect(ResourceLocation tag, int color, int blend) {
		this(tag,color,new ResourceLocation(tag.getNamespace(),"textures/aspects/"+tag.getPath()+".png"),blend);
	}

	
	public int getColor() {
		return color;
	}
	
	public String getName() {
		return WordUtils.capitalizeFully(tag.getPath());
	}
	
	public String getLocalizedDescription() {
		return StatCollector.translateToLocal("tc.aspect."+tag);
	}
	
	public ResourceLocation getTag() {
		return tag;
	}
	
	public static Aspect getAspect(ResourceLocation tag) {
		return Aspects.ALL_ASPECTS.get(tag);
	}
	
	///////////////////////////////
	public static Collection<PrimalAspect> getPrimalAspects() {
		return Aspects.PRIMAL_ASPECTS.values();
	}
	
	public static Collection<CompoundAspect> getCompoundAspects() {
		return Aspects.COMPOUND_ASPECTS.values();
	}


	//		public static final CompoundAspect ?? = new CompoundAspect("??",0xcdccf4, new AspectComponent(AIR, EARTH));
//		public static final CompoundAspect ?? = new CompoundAspect("??",0xcdccf4, new AspectComponent(FIRE, EARTH));
//		public static final CompoundAspect ?? = new CompoundAspect("??",0xcdccf4, new AspectComponent(FIRE, WATER));
//		public static final CompoundAspect ?? = new CompoundAspect("??",0xcdccf4, new AspectComponent(ORDER, WATER));
//		public static final CompoundAspect ?? = new CompoundAspect("??",0xcdccf4, new AspectComponent(EARTH, ENTROPY));


}
