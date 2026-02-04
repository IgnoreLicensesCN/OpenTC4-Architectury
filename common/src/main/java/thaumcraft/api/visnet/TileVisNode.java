package thaumcraft.api.visnet;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import tc4tweak.modules.visrelay.SavedLinkHandler;
import thaumcraft.api.tile.TileThaumcraft;
import thaumcraft.api.WorldCoordinates;
import thaumcraft.api.aspects.Aspect;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Azanor
 * 
 * The tile entity used by nodes in the vis energy network. A node is either a source (like an aura node),
 * a transport relay or vis receiver (like the infernal furnace).
 *
 */
public abstract class TileVisNode extends TileThaumcraft {
	
	WeakReference<TileVisNode> parent = null;
	ArrayList<WeakReference<TileVisNode>> children = new ArrayList<>();
	List<BlockPos> loadedLink = null;
	
	/**
	 * @return the WorldCoordinates location of where this node is located
	 */
	public WorldCoordinates getLocation() {
		return new WorldCoordinates(this);
	}
	
	/**
	 * @return the number of blocks away this node will check for parent nodes to connect to. 
	 */
	public abstract int getRange();
	
	/**
	 * @return true if this is the source or root node of the vis network. 
	 */
	public abstract boolean isSource();
		
	/**
	 * This method should never be called directly. Use {@link VisNetHandler#drainVis(Level, int, int, int, Aspect, int)} instead
	 * @param aspect what aspect to drain
	 * @param vis how much to drain
	 * @return how much was actually drained
	 */
	public int consumeVis(Aspect aspect, int vis) {
		if (VisNetHandler.isNodeValid(getParent())) {
			int out = getParent().get().consumeVis(aspect, vis);
			if (out>0) {
				triggerConsumeEffect(aspect);
			}
			return out;
		}
		return 0;
	}
	
	public void removeThisNode() {
		for (WeakReference<TileVisNode> n:getChildren()) {
			if (n!=null && n.get()!=null) {
				n.get().removeThisNode();
			}
		}	
		
		children = new ArrayList<>();
		if (VisNetHandler.isNodeValid(this.getParent())) {
			this.getParent().get().nodeRefresh=true;
		}
		this.setParent(null);
		this.parentChanged();
		Level worldObj = getLevel();
		if (worldObj == null) {
			throw new NullPointerException("worldObj is null");
		}
		if (this.isSource()) {
			ResourceKey<Level> key = worldObj.dimension();
			HashMap<WorldCoordinates, WeakReference<TileVisNode>> sourcelist = VisNetHandler.sources.get(key);
			if (sourcelist==null) {
				sourcelist = new HashMap<>();
			}
			sourcelist.remove(getLocation());
			VisNetHandler.sources.put( key, sourcelist );
		}
		BlockPos pos = this.worldPosition;
		BlockState state = worldObj.getBlockState(pos);
		worldObj.sendBlockUpdated(pos,state,state,3);
	}
	
	

	@Override
	public void invalidate() {
		removeThisNode();
		super.invalidate();
	}

	public void triggerConsumeEffect(Aspect aspect) {	}

	/**
	 * @return
	 */
	public WeakReference<TileVisNode> getParent() {
		return parent;
	}
	
	/**
	 * @return
	 */
	public WeakReference<TileVisNode> getRootSource() {
		return VisNetHandler.isNodeValid(getParent()) ? 
				getParent().get().getRootSource() : this.isSource() ?
						new WeakReference(this) : null;
	}
	
	/**
	 * @param parent
	 */
	public void setParent(WeakReference<TileVisNode> parent) {
		this.parent = parent;
	}
	
	/**
	 * @return
	 */
	public ArrayList<WeakReference<TileVisNode>> getChildren() {
		return children;
	}
	
	@Override
	public boolean canUpdate() {
        return super.canUpdate();
    }
	
	protected int nodeCounter = 0;
	private boolean nodeRegged = false;
	public boolean nodeRefresh = false;

	@Override
	public void updateEntity() {
		if (SavedLinkHandler.processSavedLink(this)){return;}
		if (level == null) {return;}
		if (Platform.getEnvironment() != Env.CLIENT && ((nodeCounter++) % 40==0 || nodeRefresh)) {
			//check for changes
			if (!nodeRefresh
					&& !children.isEmpty()) {
				for (WeakReference<TileVisNode> n:children) {
					if (n==null || n.get()==null || !VisNetHandler.canNodeBeSeen(this, n.get())) {
						nodeRefresh=true;
						break;
					}
				}			
			}
			
			//refresh linked nodes
			if (nodeRefresh) {
				for (WeakReference<TileVisNode> n:children) {
					if (n.get()!=null) {
						n.get().nodeRefresh=true;
					}
				}
				children.clear();
				parent=null;
			}
			
			//redo stuff
			if (isSource() && !nodeRegged) {
				VisNetHandler.addSource(getLevel(), this);
				nodeRegged = true;
			} else 
			if (!isSource() && !VisNetHandler.isNodeValid(getParent())) {
				setParent(VisNetHandler.addNode(getLevel(), this));				
				nodeRefresh=true;
			}
			
			if (nodeRefresh) {
				worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
				parentChanged();
			}
			nodeRefresh=false;
		}
		
	}
	
	public void parentChanged() { }
	
	/**
	 * @return the type of shard this is attuned to:
	 * none -1, air 0, fire 1, water 2, earth 3, order 4, entropy 5
	 * Should return -1 for most implementations
	 */
	public byte getAttunement() {
		return -1;
	}

	@Override
	public void load(CompoundTag CompoundTag) {
		super.load(CompoundTag);
        this.loadedLink = SavedLinkHandler.load(this, CompoundTag);
	}

	@Override
	public void saveAdditional(CompoundTag CompoundTag) {
		super.saveAdditional(CompoundTag);
		SavedLinkHandler.saveAdditional(this, CompoundTag);
	}

	public List<BlockPos> getSavedLink() {
		return loadedLink;
	}

	public void clearSavedLink() {
		this.loadedLink = null;
	}
}
