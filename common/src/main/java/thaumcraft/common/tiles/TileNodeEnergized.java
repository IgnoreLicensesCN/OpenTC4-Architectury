package thaumcraft.common.tiles;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.MathHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IAspectContainer;
import thaumcraft.api.nodes.NodeModifier;
import thaumcraft.api.nodes.NodeType;
import thaumcraft.api.visnet.TileVisNode;
import thaumcraft.common.lib.research.ResearchManager;

public class TileNodeEnergized extends TileVisNode implements IAspectContainer {
   private AspectList auraBase;
   AspectList visBase;
   AspectList vis;
   private NodeType nodeType;
   private NodeModifier nodeModifier;
   String id;

   public TileNodeEnergized() {
      this.auraBase = (new AspectList()).add(Aspect.AIR, 20).add(Aspect.FIRE, 20).add(Aspect.EARTH, 20).add(Aspect.WATER, 20).add(Aspect.ORDER, 20).add(Aspect.ENTROPY, 20);
      this.visBase = new AspectList();
      this.vis = new AspectList();
      this.nodeType = NodeType.NORMAL;
      this.nodeModifier = null;
      this.id = "blank";
   }

   public void updateEntity() {
      super.updateEntity();
      if (!this.worldObj.isRemote) {
         if (this.getNodeType() == NodeType.UNSTABLE && this.worldObj.rand.nextInt(500) == 1) {
            this.visBase = new AspectList();
         }

         if (this.visBase.size() == 0 && this.getAuraBase().size() > 0) {
            this.setupNode();
         }

         this.vis = this.visBase.copy();
      }

   }

   public boolean canUpdate() {
       return super.canUpdate();
   }

   public void setupNode() {
      this.visBase = new AspectList();
      AspectList temp = ResearchManager.reduceToPrimals(this.getAuraBase(), true);

      for(Aspect aspect : temp.getAspects()) {
         int amt = temp.getAmount(aspect);
         if (this.getNodeModifier() == NodeModifier.BRIGHT) {
            amt = (int)((float)amt * 1.2F);
         }

         if (this.getNodeModifier() == NodeModifier.PALE) {
            amt = (int)((float)amt * 0.8F);
         }

         if (this.getNodeModifier() == NodeModifier.FADING) {
            amt = (int)((float)amt * 0.5F);
         }

         amt = MathHelper.floor_double(MathHelper.sqrt_double(amt));
         if (this.getNodeType() == NodeType.UNSTABLE) {
            amt += this.worldObj.rand.nextInt(5) - 2;
         }

         if (amt >= 1) {
            this.visBase.merge(aspect, amt);
         }
      }

      this.markDirty();
      this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
   }

   public void readFromNBT(NBTTagCompound nbttagcompound) {
      super.readFromNBT(nbttagcompound);
   }

   public void writeToNBT(NBTTagCompound nbttagcompound) {
      super.writeToNBT(nbttagcompound);
   }

   public void readCustomNBT(NBTTagCompound nbttagcompound) {
      this.id = nbttagcompound.getString("nodeId");
      this.setNodeType(NodeType.values()[nbttagcompound.getByte("type")]);
      byte mod = nbttagcompound.getByte("modifier");
      if (mod >= 0) {
         this.setNodeModifier(NodeModifier.values()[mod]);
      } else {
         this.setNodeModifier(null);
      }

      this.visBase.aspects.clear();
      NBTTagList tlist = nbttagcompound.getTagList("AEB", 10);

      for(int j = 0; j < tlist.tagCount(); ++j) {
         NBTTagCompound rs = tlist.getCompoundTagAt(j);
         if (rs.hasKey("key")) {
            this.visBase.add(Aspect.getAspect(rs.getString("key")), rs.getInteger("amount"));
         }
      }

      this.getAuraBase().readFromNBT(nbttagcompound);
   }

   public void writeCustomNBT(NBTTagCompound nbttagcompound) {
      nbttagcompound.setString("nodeId", this.id);
      nbttagcompound.setByte("type", (byte)this.getNodeType().ordinal());
      nbttagcompound.setByte("modifier", this.getNodeModifier() == null ? -1 : (byte)this.getNodeModifier().ordinal());
      NBTTagList tlist = new NBTTagList();
      nbttagcompound.setTag("AEB", tlist);

      for(Aspect aspect : this.visBase.getAspects()) {
         if (aspect != null) {
            NBTTagCompound f = new NBTTagCompound();
            f.setString("key", aspect.getTag());
            f.setInteger("amount", this.visBase.getAmount(aspect));
            tlist.appendTag(f);
         }
      }

      this.getAuraBase().writeToNBT(nbttagcompound);
   }

   public boolean receiveClientEvent(int i, int j) {
      return super.receiveClientEvent(i, j);
   }

   public NodeType getNodeType() {
      return this.nodeType;
   }

   public void setNodeType(NodeType nodeType) {
      this.nodeType = nodeType;
   }

   public void setNodeModifier(NodeModifier nodeModifier) {
      this.nodeModifier = nodeModifier;
   }

   public NodeModifier getNodeModifier() {
      return this.nodeModifier;
   }

   public int getRange() {
      return 8;
   }

   public boolean isSource() {
      return true;
   }

   public int consumeVis(Aspect aspect, int amount) {
      int drain = Math.min(this.vis.getAmount(aspect), amount);
      if (drain > 0) {
         this.vis.reduce(aspect, drain);
      }

      return drain;
   }

   public AspectList getAuraBase() {
      return this.auraBase;
   }

   public AspectList getAspects() {
      return this.visBase;
   }

   public void setAspects(AspectList aspects) {
      this.auraBase = aspects;
   }

   public boolean doesContainerAccept(Aspect tag) {
      return false;
   }

   public int addToContainer(Aspect tag, int amount) {
      return 0;
   }

   public boolean takeFromContainer(Aspect tag, int amount) {
      return false;
   }

   public boolean takeFromContainer(AspectList ot) {
      return false;
   }

   public boolean doesContainerContainAmount(Aspect tag, int amount) {
      return false;
   }

   public boolean doesContainerContain(AspectList ot) {
      return false;
   }

   public int containerContains(Aspect tag) {
      return 0;
   }

   public byte getAttunement() {
       return super.getAttunement();
   }
}
