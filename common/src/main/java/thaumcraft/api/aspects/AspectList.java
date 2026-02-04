package thaumcraft.api.aspects;

import com.linearity.opentc4.utils.CompoundTagHelper;
import com.linearity.opentc4.utils.StatCollector;
import fromhodgepodge.util.AspectNameSorter;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.common.Thaumcraft;

import java.io.Serializable;
import java.util.*;
import java.util.function.BiFunction;

import static com.linearity.opentc4.Consts.AspectCompoundTagAccessors.*;

//but usually i use Map<Aspect,Integer>
//why Serializable?idk.
public class AspectList implements Serializable {
	
	protected final LinkedHashMap<Aspect,Integer> aspects = new LinkedHashMap<>();//aspects associated with this object

	
	/**
	 * this creates a new aspect list with preloaded values based off the aspects of the given item.
	 * @param stack the itemstack of the given item
	 */
	public AspectList(ItemStack stack) {
		AspectList temp = ThaumcraftApiHelper.getObjectAspects(stack);
		if (temp!=null) {
			for (Aspect tag : temp.getAspectTypes()) {
				addAll(tag, temp.getAmount(tag));
			}
		}
	}
	
	public AspectList() {
	}

	public AspectList(AspectList another) {
		this.aspects.putAll(another.aspects);
	}

    public static void addAspectDescriptionToList(AspectList aspects, Player player, List<String> aspectDescriptions) {
       if (aspects != null && !aspects.aspects.isEmpty()) {
          for(Aspect tag : aspects.getAspectsSorted()) {
             if (Thaumcraft.playerKnowledge.hasDiscoveredAspect(player.getGameProfile().getName(), tag)) {
                aspectDescriptions.add(tag.getName() + " x " + aspects.getAmount(tag));
             } else {
                aspectDescriptions.add(StatCollector.translateToLocal("tc.aspect.unknown"));
             }
          }
       }
    }

    public AspectList copy() {
		AspectList out = new AspectList();
		for (Aspect a:this.getAspectTypes())
			out.addAll(a, this.getAmount(a));
		return out;
	}

	public static AspectList of(Map<Aspect,Integer> aspects) {
		AspectList out = new AspectList();
		for (var entry:aspects.entrySet()) {
			var key = entry.getKey();
			var value = entry.getValue();
			out.addAll(key, value);
		}
		return out;
	}
	
	/**
	 * @return the amount of different aspects in this collection
	 */
	public int size() {
		return aspects.size();
	}
	
	/**
	 * @return the amount of total vis in this collection
	 */
	public int visSize() {
		int q = 0;
		
		for (Aspect as:aspects.keySet()) {
			q+=this.getAmount(as);
		}
		
		return q;
	}
	
	/**
	 * @return an array of all the aspects in this collection
	 */
	public Aspect[] getAspectTypes() {
		Aspect[] q = new Aspect[1];
		return aspects.keySet().toArray(q);
	}

	@UnmodifiableView
	private final Map<Aspect,Integer> aspectView = Collections.unmodifiableMap(aspects);
	@UnmodifiableView
	public Map<Aspect,Integer> getAspects() {
		return aspectView;
	}
	
	/**
	 * @return an array of all the aspects in this collection
	 */
	public Aspect[] getPrimalAspects() {
		AspectList t = new AspectList();
		for (Aspect as:aspects.keySet()) {
			if (as instanceof PrimalAspect) {
				t.addAll(as,1);
			}
		}
		Aspect[] q = new Aspect[1];
		return t.aspects.keySet().toArray(q);
	}
	
	/**
	 * @return an array of all the aspects in this collection sorted by name
	 */
	public Aspect[] getAspectsSorted() {
		return AspectNameSorter.sort(this);
//		try {
//			Aspect[] out = aspects.keySet().toArray(new Aspect[]{});
//			boolean change=false;
//			do {
//				change=false;
//				for(int a=0;a<out.length-1;a++) {
//					Aspect e1 = out[a];
//					Aspect e2 = out[a+1];
//					if (e1!=null && e2!=null && e1.getTag().compareTo(e2.getTag())>0) {
//						out[a] = e2;
//						out[a+1] = e1;
//						change = true;
//						break;
//					}
//				}
//			} while (change);
//			return out;
//		} catch (Exception e) {
//			return this.getAspects();
//		}
	}
	
	/**
	 * @return an array of all the aspects in this collection sorted by amount
	 */
	public Aspect[] getAspectsSortedAmount() {
		try {
			Aspect[] out = aspects.keySet().toArray(new Aspect[1]);
			boolean change=false;
			do {
				change=false;
				for(int a=0;a<out.length-1;a++) {
					int e1 = getAmount(out[a]); 
					int e2 = getAmount(out[a+1]);
					if (e1>0 && e2>0 && e2>e1) {
						Aspect ea = out[a];
						Aspect eb = out[a+1];
						out[a] = eb;
						out[a+1] = ea;
						change = true;
						break;
					}
				}
			} while (change);
			return out;
		} catch (Exception e) {
			return this.getAspectTypes();
		}
	}
	
	/**
	 * @param key aspect as key
	 * @return the amount associated with the given aspect in this collection
	 */
	public int getAmount(Aspect key) {
		return  aspects.get(key)==null?0:aspects.get(key);
	}
	
	/**
	 * Reduces the amount of an aspect in this collection by the given amount. 
	 * @param key to remove
	 * @param amount to remove
	 * @return succeed(false if will lead to negative)
	 */
	public boolean reduce(Aspect key, int amount) {
		if (getAmount(key)>=amount) {
			int am = getAmount(key)-amount;
			aspects.put(key, am);
			return true;
		}
		return false;
	}
	
	/**
	 * Reduces the amount of an aspect in this collection by the given amount. 
	 * If reduced to 0 or less the aspect will be removed completely. 
	 * @param key to remove
	 * @param amount to remove
	 * @return slef
	 */
	public AspectList reduceAndRemoveIfNegative(Aspect key, int amount) {
		int am = getAmount(key) - amount;
		if (am<=0) {
			aspects.remove(key);
		}
		else {
			this.aspects.put(key, am);
		}
		return this;
	}
	
	/**
	 * Simply removes the aspect from the list
	 * @param key
//	 * @param amount
	 * @return self
	 */
	public AspectList reduceAndRemoveIfNegative(Aspect key) {
		aspects.remove(key); 
		return this;
	}
	
	/**
	 * Adds this aspect and amount to the collection. 
	 * If the aspect exists then its value will be increased by the given amount.
	 * @param aspect to add
	 * @param amount to add
	 * @return self
	 */
	public AspectList addAll(Aspect aspect, int amount) {
		if (aspect == null){
			throw new NullPointerException("aspect is null");
		}
		if (this.aspects.containsKey(aspect)) {
			int oldamount = this.aspects.get(aspect);
			amount += oldamount;
		}
		this.aspects.put( aspect, amount );
		return this;
	}
	public AspectList set(Aspect aspect, int amount) {
		if (aspect == null){
			throw new NullPointerException("aspect is null");
		}
		this.aspects.put( aspect, amount );
		return this;
	}

	
	/**
	 * Adds this aspect and amount to the collection. 
	 * If the aspect exists then only the highest of the old or new amount will be used.
	 * @param aspect to merge
	 * @param amount to merge
	 * @return self
	 */
	public AspectList mergeWithHighest(Aspect aspect, int amount) {
		if (this.aspects.containsKey(aspect)) {
			int oldamount = this.aspects.get(aspect);
			if (amount<oldamount) amount=oldamount;
			
		}
		this.aspects.put( aspect, amount );
		return this;
	}
	
	public AspectList addAll(AspectList in) {
		for (Aspect a:in.getAspectTypes())
			this.addAll(a, in.getAmount(a));
		return this;
	}
	
	public AspectList mergeWithHighest(AspectList in) {
		for (Aspect a:in.getAspectTypes())
			this.mergeWithHighest(a, in.getAmount(a));
		return this;
	}



	public static AspectList generateFromNBT(CompoundTag tag) {
		AspectList out = new AspectList();
		ListTag tlist = ASPECT_ASPECTS_ACCESSOR.readFromCompoundTag(tag);
		for (int i = 0; i < tlist.size(); i++) {
			CompoundTag rs = tlist.getCompound(i);
			if (rs.contains(ASPECT_KEY_ACCESSOR.tagKey)) {
				out.addAll(Aspect.getAspect(ASPECT_KEY_ACCESSOR.readFromCompoundTag(rs)), ASPECT_AMOUNT_ACCESSOR.readFromCompoundTag(rs));
			}
		}
		return out;
	}

	public void loadFrom(CompoundTag tag) {
		loadFrom(tag,ASPECT_ASPECTS_ACCESSOR);
	}
	public void loadFrom(CompoundTag tag, CompoundTagHelper.ListTagAccessor accessor) {
		aspects.clear();
		ListTag tlist = accessor.readFromCompoundTag(tag); // 10 = CompoundTag
		for (int j = 0; j < tlist.size(); j++) {
			CompoundTag rs = tlist.getCompound(j);
			if (rs.contains(ASPECT_KEY_ACCESSOR.tagKey)) {
				addAll(Aspect.getAspect(ASPECT_KEY_ACCESSOR.readFromCompoundTag(rs)), ASPECT_AMOUNT_ACCESSOR.readFromCompoundTag(rs));
			}
		}
	}


	public void saveTo(CompoundTag tag) {
		saveTo(tag,ASPECT_ASPECTS_ACCESSOR);
	}
	public void saveTo(CompoundTag tag, CompoundTagHelper.ListTagAccessor accessor) {
		ListTag tlist = new ListTag();
		for (Aspect aspect : getAspectTypes()) {
			if (aspect != null) {
				CompoundTag f = new CompoundTag();
				ASPECT_KEY_ACCESSOR.writeToCompoundTag(f,aspect.getTag());
				ASPECT_AMOUNT_ACCESSOR.writeToCompoundTag(f,getAmount(aspect));
				tlist.add(f);
			}
		}
		accessor.writeToCompoundTag(tag,tlist);
	}
//	public void saveAdditional(CompoundTag CompoundTag, String label)
//	{
//		NBTTagList tlist = new NBTTagList();
//		CompoundTag.setTag(label, tlist);
//		for (Aspect aspect : getAspects())
//			if (aspect != null) {
//				CompoundTag f = new CompoundTag();
//				f.setString("key", aspect.getTag());
//				f.setInteger("amount", getAmount(aspect));
//				tlist.appendTag(f);
//			}
//	}

//	/**
//	 * Reads the list of aspects from nbt
//	 * @param CompoundTag
//	 * @return
//	 */
//	public void load(CompoundTag CompoundTag)
//    {
//        aspects.clear();
//        NBTTagList tlist = CompoundTag.getTagList("Aspects",(byte)10);
//		for (int j = 0; j < tlist.tagCount(); j++) {
//			CompoundTag rs = tlist.getCompoundTagAt(j);
//			if (rs.hasKey("key")) {
//				add(	Aspect.getAspect(rs.getString("key")),
//						rs.getInteger("amount"));
//			}
//		}
//    }
	
//	public void load(CompoundTag CompoundTag, String label)
//    {
//        aspects.clear();
//        NBTTagList tlist = CompoundTag.getTagList(label,(byte)10);
//		for (int j = 0; j < tlist.tagCount(); j++) {
//			CompoundTag rs = tlist.getCompoundTagAt(j);
//			if (rs.hasKey("key")) {
//				add(	Aspect.getAspect(rs.getString("key")),
//						rs.getInteger("amount"));
//			}
//		}
//    }
	
//	/**
//	 * Writes the list of aspects to nbt
//	 * @param CompoundTag
//	 * @return
//	 */
//	public void saveAdditional(CompoundTag CompoundTag)
//    {
//        NBTTagList tlist = new NBTTagList();
//		CompoundTag.setTag("Aspects", tlist);
//		for (Aspect aspect : getAspects())
//			if (aspect != null) {
//				CompoundTag f = new CompoundTag();
//				f.setString("key", aspect.getTag());
//				f.setInteger("amount", getAmount(aspect));
//				tlist.appendTag(f);
//			}
//    }
	
//	public void saveAdditional(CompoundTag CompoundTag, String label)
//    {
//        NBTTagList tlist = new NBTTagList();
//		CompoundTag.setTag(label, tlist);
//		for (Aspect aspect : getAspects())
//			if (aspect != null) {
//				CompoundTag f = new CompoundTag();
//				f.setString("key", aspect.getTag());
//				f.setInteger("amount", getAmount(aspect));
//				tlist.appendTag(f);
//			}
//    }

	public void putAllAspects(AspectList aspects) {
		this.aspects.putAll(aspects.getAspects());
	}


	public void replaceAll(BiFunction<Aspect,Integer,Integer> biFunction){
		aspects.replaceAll(biFunction);
	}

	public static AspectList of(Aspect... aspects){
		AspectList out = new AspectList();
		for (Aspect aspect : aspects){
			if (aspect != null){
				out.addAll(aspect,1);
			}
		}
		return out;
	}

	public Aspect randomAspect(RandomSource randomSource){
		if (this.size() == 0){
			return null;
		}
		return this.getAspects().keySet()
				.toArray(new Aspect[0])[randomSource.nextInt(this.size())];
	}

	public boolean isEmpty(){
		return this.aspects.isEmpty();
	}

	@Override
	public String toString() {
		return this.getClass() + "{" +
				"aspects=" + aspects +
				", aspectView=" + Arrays.toString(aspectView.entrySet()
                .stream()
                .map(entry -> entry.getKey() + ":" + entry.getValue())
                .toArray()) +
				'}';
	}
}
