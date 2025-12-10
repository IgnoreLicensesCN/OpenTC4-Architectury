package thaumcraft.common.tiles;

import cpw.mods.fml.common.network.NetworkRegistry;
import net.minecraft.block.Block;
import net.minecraft.world.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.phys.HitResult;
import net.minecraft.util.HitResult.MovingObjectType;
import net.minecraft.util.Vec3;
import net.minecraft.world.level.Level;
import net.minecraft.world.biome.BiomeGenBase;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.TileThaumcraft;
import thaumcraft.api.WorldCoordinates;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.nodes.INode;
import thaumcraft.api.nodes.NodeModifier;
import thaumcraft.api.nodes.NodeType;
import thaumcraft.api.research.ScanResult;
import thaumcraft.api.wands.IWandable;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.blocks.BlockTaintFibres;
import thaumcraft.common.config.Config;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.entities.EntityAspectOrb;
import thaumcraft.common.entities.monster.EntityGiantBrainyZombie;
import thaumcraft.common.items.misc.ItemCompassStone;
import thaumcraft.common.items.wands.WandCastingItem;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.fx.PacketFXBlockZap;
import thaumcraft.common.lib.research.ResearchManager;
import thaumcraft.common.lib.research.ScanManager;
import thaumcraft.common.lib.utils.EntityUtils;
import thaumcraft.common.lib.utils.Utils;
import thaumcraft.common.lib.world.ThaumcraftWorldGenerator;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TileNode extends TileThaumcraft implements INode, IWandable {
   long lastActive = 0L;
   AspectList aspects = new AspectList();
   AspectList aspectsBase = new AspectList();
   public static HashMap locations = new HashMap<>();
   private NodeType nodeType;
   private NodeModifier nodeModifier;
   int count;
   int regeneration;
   int wait;
   String id;
   byte nodeLock;
   boolean catchUp;
   public Entity drainEntity;
   public HitResult drainCollision;
   public int drainColor;
   public Color targetColor;
   public Color color;

   public TileNode() {
      this.nodeType = NodeType.NORMAL;
      this.nodeModifier = null;
      this.count = 0;
      this.regeneration = -1;
      this.wait = 0;
      this.id = null;
      this.nodeLock = 0;
      this.catchUp = false;
      this.drainEntity = null;
      this.drainCollision = null;
      this.drainColor = 16777215;
      this.targetColor = new Color(16777215);
      this.color = new Color(16777215);
   }

   public String getId() {
      if (this.id == null) {
         this.id = this.generateId();
      }

      return this.id;
   }

   public String generateId() {
      this.id = this.level().dimension() + ":" + this.xCoord + ":" + this.yCoord + ":" + this.zCoord;
      if (this.level() != null && locations != null) {
         ArrayList<Integer> t = new ArrayList<>();
         t.add(this.level().dimension());
         t.add(this.xCoord);
         t.add(this.yCoord);
         t.add(this.zCoord);
         locations.put(this.id, t);
      }

      return this.id;
   }

   public void onChunkUnload() {
      if (locations != null) {
         locations.remove(this.id);
      }

      super.onChunkUnload();
   }

   public void validate() {
      super.validate();
   }

   public void updateEntity() {
      super.updateEntity();
      if (this.id == null) {
         this.generateId();
      }

      boolean change = false;
      change = this.handleHungryNodeFirst(change);
      ++this.count;
      this.checkLock();
      if (Platform.getEnvironment() != Env.CLIENT) {
         change = this.handleDischarge(change);
         change = this.handleRecharge(change);
         change = this.handleTaintNode(change);
         change = this.handleNodeStability(change);
         change = this.handleDarkNode(change);
         change = this.handlePureNode(change);
         change = this.handleHungryNodeSecond(change);
         if (change) {
            this.markDirty();
            this.level().markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
         }
      } else if (this.getNodeType() == NodeType.DARK && this.count % 50 == 0) {
         ItemCompassStone.sinisterNodes.put(new WorldCoordinates(this), System.currentTimeMillis());
      }

   }

   public void nodeChange() {
      this.regeneration = -1;
      this.markDirty();
      this.level().markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
   }

   public boolean canUpdate() {
       return super.canUpdate();
   }

   public double getDistanceTo(double par1, double par3, double par5) {
      double var7 = (double)this.xCoord + (double)0.5F - par1;
      double var9 = (double)this.yCoord + (double)0.5F - par3;
      double var11 = (double)this.zCoord + (double)0.5F - par5;
      return var7 * var7 + var9 * var9 + var11 * var11;
   }

   public int onWandRightClick(Level world, ItemStack wandstack, Player player, int x, int y, int z, int side, int md) {
      return -1;
   }

   public ItemStack onWandRightClick(World world, ItemStack wandstack, Player player) {
      player.setItemInUse(wandstack, Integer.MAX_VALUE);
      WandCastingItem wand = (WandCastingItem)wandstack.getItem();
      wand.setObjectInUse(wandstack, this.xCoord, this.yCoord, this.zCoord);
      return wandstack;
   }

   public AspectList getAspects() {
      return this.aspects;
   }

   public AspectList getAspectsBase() {
      return this.aspectsBase;
   }

   public void setAspects(AspectList aspects) {
      this.aspects = aspects;
      this.aspectsBase = aspects.copy();
   }

   public int addToContainer(Aspect aspect, int amount) {
      int left = amount + this.aspects.getAmount(aspect) - this.aspectsBase.getAmount(aspect);
      left = Math.max(left, 0);
      this.aspects.add(aspect, amount - left);
      return left;
   }

   public boolean takeFromContainer(Aspect aspect, int amount) {
      return this.aspects.reduce(aspect, amount);
   }

   public Aspect takeRandomPrimalFromSource() {
      Aspect[] primals = this.aspects.getPrimalAspects();
      Aspect asp = primals[this.level().rand.nextInt(primals.length)];
      return asp != null && this.aspects.reduce(asp, 1) ? asp : null;
   }

   public Aspect chooseRandomFilteredFromSource(AspectList filter, boolean preserve) {
      int min = preserve ? 1 : 0;
      ArrayList<Aspect> validaspects = new ArrayList<>();

      for(Aspect prim : this.aspects.getAspects()) {
         if (filter.getAmount(prim) > 0 && this.aspects.getAmount(prim) > min) {
            validaspects.add(prim);
         }
      }

      if (validaspects.isEmpty()) {
         return null;
      } else {
         Aspect asp = validaspects.get(this.level().rand.nextInt(validaspects.size()));
         if (asp != null && this.aspects.getAmount(asp) > min) {
            return asp;
         } else {
            return null;
         }
      }
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

   public int getNodeVisBase(Aspect aspect) {
      return this.aspectsBase.getAmount(aspect);
   }

   public void setNodeVisBase(Aspect aspect, short nodeVisBase) {
      if (this.aspectsBase.getAmount(aspect) < nodeVisBase) {
         this.aspectsBase.merge(aspect, nodeVisBase);
      } else {
         this.aspectsBase.reduce(aspect, this.aspectsBase.getAmount(aspect) - nodeVisBase);
      }

   }

   public void readFromNBT(NBTTagCompound nbttagcompound) {
      super.readFromNBT(nbttagcompound);
      this.lastActive = nbttagcompound.getLong("lastActive");
      AspectList al = new AspectList();
      NBTTagList tlist = nbttagcompound.getTagList("AspectsBase", 10);

      for(int j = 0; j < tlist.tagCount(); ++j) {
         NBTTagCompound rs = tlist.getCompoundTagAt(j);
         if (rs.hasKey("key")) {
            al.add(Aspect.getAspect(rs.getString("key")), rs.getInteger("amount"));
         }
      }

      short oldBase = nbttagcompound.getShort("nodeVisBase");
      this.aspectsBase = new AspectList();
      if (oldBase > 0 && al.size() == 0) {
         for(Aspect a : this.aspects.getAspects()) {
            this.aspectsBase.merge(a, oldBase);
         }
      } else {
         this.aspectsBase = al.copy();
      }

      int regen = 600;
      if (this.getNodeModifier() != null) {
         switch (this.getNodeModifier()) {
            case BRIGHT:
               regen = 400;
               break;
            case PALE:
               regen = 900;
               break;
            case FADING:
               regen = 0;
         }
      }

      long ct = System.currentTimeMillis();
      int inc = regen * 75;
      if (regen > 0 && this.lastActive > 0L && ct > this.lastActive + (long)inc) {
         this.catchUp = true;
      }

   }

   public void writeToNBT(NBTTagCompound nbttagcompound) {
      super.writeToNBT(nbttagcompound);
      nbttagcompound.setLong("lastActive", this.lastActive);
      NBTTagList tlist = new NBTTagList();
      nbttagcompound.setTag("AspectsBase", tlist);

      for(Aspect aspect : this.aspectsBase.getAspects()) {
         if (aspect != null) {
            NBTTagCompound f = new NBTTagCompound();
            f.setString("key", aspect.getTag());
            f.setInteger("amount", this.aspectsBase.getAmount(aspect));
            tlist.appendTag(f);
         }
      }

   }

   public void readCustomNBT(NBTTagCompound nbttagcompound) {
      this.id = nbttagcompound.getString("nodeId");
      if (this.level() != null && locations != null) {
         ArrayList<Integer> t = new ArrayList<>();
         t.add(this.level().dimension());
         t.add(this.xCoord);
         t.add(this.yCoord);
         t.add(this.zCoord);
         locations.put(this.id, t);
      }

      this.setNodeType(NodeType.values()[nbttagcompound.getByte("type")]);
      byte mod = nbttagcompound.getByte("modifier");
      if (mod >= 0) {
         this.setNodeModifier(NodeModifier.values()[mod]);
      } else {
         this.setNodeModifier(null);
      }

      this.aspects.readFromNBT(nbttagcompound);
      String de = nbttagcompound.getString("drainer");
      if (de != null && !de.isEmpty() && this.getLevel() != null) {
         this.drainEntity = this.getLevel().getPlayerEntityByName(de);
         if (this.drainEntity != null) {
            this.drainCollision = new HitResult(this.xCoord, this.yCoord, this.zCoord, 0, Vec3.createVectorHelper(this.drainEntity.posX, this.drainEntity.posY, this.drainEntity.posZ));
         }
      }

      this.drainColor = nbttagcompound.getInteger("draincolor");
   }

   public void writeCustomNBT(NBTTagCompound nbttagcompound) {
      if (this.id == null) {
         this.id = this.generateId();
      }

      if (this.level() != null && locations != null) {
         ArrayList<Integer> t = new ArrayList<>();
         t.add(this.level().dimension());
         t.add(this.xCoord);
         t.add(this.yCoord);
         t.add(this.zCoord);
         locations.put(this.id, t);
      }

      nbttagcompound.setString("nodeId", this.id);
      nbttagcompound.setByte("type", (byte)this.getNodeType().ordinal());
      nbttagcompound.setByte("modifier", this.getNodeModifier() == null ? -1 : (byte)this.getNodeModifier().ordinal());
      this.aspects.writeToNBT(nbttagcompound);
      if (this.drainEntity != null && this.drainEntity instanceof Player) {
         nbttagcompound.setString("drainer", this.drainEntity.getCommandSenderName());
      }

      nbttagcompound.setInteger("draincolor", this.drainColor);
   }

   public void onUsingWandTick(ItemStack wandstack, Player player, int count) {
      boolean mfu = false;
      WandCastingItem wand = (WandCastingItem)wandstack.getItem();
      HitResult HitResult = EntityUtils.getHitResultFromPlayer(this.level(), player, true);
      if (HitResult != null && HitResult.typeOfHit == MovingObjectType.BLOCK) {
         int i = HitResult.blockX;
         int j = HitResult.blockY;
         int k = HitResult.blockZ;
         if (i != this.xCoord || j != this.yCoord || k != this.zCoord) {
            player.stopUsingItem();
         }
      } else {
         player.stopUsingItem();
      }

      if (count % 5 == 0) {
         int tap = 1;
         if (ResearchManager.isResearchComplete(player.getCommandSenderName(), "NODETAPPER1")) {
            ++tap;
         }

         if (ResearchManager.isResearchComplete(player.getCommandSenderName(), "NODETAPPER2")) {
            ++tap;
         }

         boolean preserve = !player.isSneaking() && ResearchManager.isResearchComplete(player.getCommandSenderName(), "NODEPRESERVE") && !wand.getRod(wandstack).getTag().equals("wood") && !wand.getCap(wandstack).getTag().equals("iron");
         boolean success = false;
         Aspect aspect = null;
         if ((aspect = this.chooseRandomFilteredFromSource(wand.getAspectsWithRoom(wandstack), preserve)) != null) {
            int amt = this.getAspects().getAmount(aspect);
            if (tap > amt) {
               tap = amt;
            }

            if (preserve && tap == amt) {
               --tap;
            }

            if (tap > 0) {
               int rem = wand.addVis(wandstack, aspect, tap, Platform.getEnvironment() != Env.CLIENT);
               if (rem < tap) {
                  this.drainColor = aspect.getColor();
                  if (Platform.getEnvironment() != Env.CLIENT) {
                     this.takeFromContainer(aspect, tap - rem);
                     mfu = true;
                  }

                  success = true;
               }
            }
         }

         if (success) {
            this.drainEntity = player;
            this.drainCollision = HitResult;
            this.targetColor = new Color(this.drainColor);
         } else {
            this.drainEntity = null;
            this.drainCollision = null;
         }

         if (mfu) {
            this.level().markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
            this.markDirty();
         }
      }

      if ((Platform.getEnvironment() == Env.CLIENT)) {
         int r = this.targetColor.getRed();
         int g = this.targetColor.getGreen();
         int b = this.targetColor.getBlue();
         int r2 = this.color.getRed() * 4;
         int g2 = this.color.getGreen() * 4;
         int b2 = this.color.getBlue() * 4;
         this.color = new Color((r + r2) / 5, (g + g2) / 5, (b + b2) / 5);
      }

   }

   public void onWandStoppedUsing(ItemStack wandstack, World world, Player player, int count) {
      this.drainEntity = null;
      this.drainCollision = null;
   }

   public boolean receiveClientEvent(int i, int j) {
      return super.receiveClientEvent(i, j);
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

   public boolean doesContainerAccept(Aspect tag) {
      return true;
   }

   private boolean handleHungryNodeFirst(boolean change) {
      if (this.getNodeType() == NodeType.HUNGRY) {
         if ((Platform.getEnvironment() == Env.CLIENT)) {
            for(int a = 0; a < Thaumcraft.proxy.particleCount(1); ++a) {
               int tx = this.xCoord + this.level().rand.nextInt(16) - this.level().rand.nextInt(16);
               int ty = this.yCoord + this.level().rand.nextInt(16) - this.level().rand.nextInt(16);
               int tz = this.zCoord + this.level().rand.nextInt(16) - this.level().rand.nextInt(16);
               if (ty > this.level().getHeightValue(tx, tz)) {
                  ty = this.level().getHeightValue(tx, tz);
               }

               Vec3 v1 = Vec3.createVectorHelper((double)this.xCoord + (double)0.5F, (double)this.yCoord + (double)0.5F, (double)this.zCoord + (double)0.5F);
               Vec3 v2 = Vec3.createVectorHelper((double)tx + (double)0.5F, (double)ty + (double)0.5F, (double)tz + (double)0.5F);
               HitResult mop = ThaumcraftApiHelper.rayTraceIgnoringSource(this.level(), v1, v2, true, false, false);
               if (mop != null && this.getDistanceFrom(mop.blockX, mop.blockY, mop.blockZ) < (double)256.0F) {
                  tx = mop.blockX;
                  ty = mop.blockY;
                  tz = mop.blockZ;
                  Block bi = this.level().getBlock(tx, ty, tz);
                  int md = this.level().getBlockMetadata(tx, ty, tz);
                  if (!bi.isAir(this.level(), tx, ty, tz)) {
                     Thaumcraft.proxy.hungryNodeFX(this.level(), tx, ty, tz, this.xCoord, this.yCoord, this.zCoord, bi, md);
                  }
               }
            }
         }

         if (Config.hardNode) {
            List ents = this.level().getEntitiesWithinAABB(Entity.class, AxisAlignedBB.getBoundingBox(this.xCoord, this.yCoord, this.zCoord, this.xCoord + 1, this.yCoord + 1, this.zCoord + 1).expand(15.0F, 15.0F, 15.0F));
            if (ents != null && !ents.isEmpty()) {
               for(Object ent : ents) {
                  Entity eo = (Entity)ent;
                  if (!(eo instanceof Player) || !((Player)eo).capabilities.disableDamage) {
                     if (eo.isEntityAlive() && !eo.isEntityInvulnerable()) {
                        double d = this.getDistanceTo(eo.posX, eo.posY, eo.posZ);
                        if (d < (double)2.0F) {
                           eo.attackEntityFrom(DamageSource.outOfWorld, 1.0F);
                           if (!eo.isEntityAlive() && Platform.getEnvironment() != Env.CLIENT) {
                              ScanResult scan = new ScanResult((byte)2, 0, 0, eo, "");
                              AspectList al = ScanManager.getScanAspects(scan, this.level());
                              if (al != null && al.size() > 0) {
                                 al = ResearchManager.reduceToPrimals(al.copy());
                                 if (al != null && al.size() > 0) {
                                    Aspect a = al.getAspects()[this.level().rand.nextInt(al.size())];
                                    if (this.getAspects().getAmount(a) < this.getNodeVisBase(a)) {
                                       this.addToContainer(a, 1);
                                       change = true;
                                    } else if (this.level().rand.nextInt(1 + this.getNodeVisBase(a) * 2) < al.getAmount(a)) {
                                       this.aspectsBase.add(a, 1);
                                       change = true;
                                    }
                                 }
                              }
                           }
                        }
                     }

                     double var3 = ((double)this.xCoord + (double)0.5F - eo.posX) / (double)15.0F;
                     double var5 = ((double)this.yCoord + (double)0.5F - eo.posY) / (double)15.0F;
                     double var7 = ((double)this.zCoord + (double)0.5F - eo.posZ) / (double)15.0F;
                     double var9 = Math.sqrt(var3 * var3 + var5 * var5 + var7 * var7);
                     double var11 = (double)1.0F - var9;
                     if (var11 > (double)0.0F) {
                        var11 *= var11;
                        eo.motionX += var3 / var9 * var11 * 0.15;
                        eo.motionY += var5 / var9 * var11 * (double)0.25F;
                        eo.motionZ += var7 / var9 * var11 * 0.15;
                     }
                  }
               }
            }
         }
      }

      return change;
   }

   private boolean handleDischarge(boolean change) {
      if (this.level().getBlock(this.xCoord, this.yCoord, this.zCoord) == ConfigBlocks.blockAiry && this.getLock() != 1) {
         if (this.getNodeModifier() == NodeModifier.FADING) {
            return change;
         } else {
            boolean shiny = this.getNodeType() == NodeType.HUNGRY || this.getNodeModifier() == NodeModifier.BRIGHT;
            int inc = this.getNodeModifier() == null ? 2 : (shiny ? 1 : (this.getNodeModifier() == NodeModifier.PALE ? 3 : 2));
            if (this.count % inc != 0) {
               return change;
            } else {
               int x = this.level().rand.nextInt(5) - this.level().rand.nextInt(5);
               int y = this.level().rand.nextInt(5) - this.level().rand.nextInt(5);
               int z = this.level().rand.nextInt(5) - this.level().rand.nextInt(5);
               if (this.getNodeModifier() == NodeModifier.PALE && this.level().rand.nextBoolean()) {
                  return change;
               } else {
                  if (x != 0 || y != 0 || z != 0) {
                     TileEntity te = this.level().getTileEntity(this.xCoord + x, this.yCoord + y, this.zCoord + z);
                     if (te instanceof INode && this.level().getBlock(this.xCoord + x, this.yCoord + y, this.zCoord + z) == ConfigBlocks.blockAiry) {
                        if (te instanceof TileNode && ((TileNode)te).getLock() > 0) {
                           return change;
                        }

                        INode nd = (INode)te;
                        int ndavg = (nd.getAspects().visSize() + nd.getAspectsBase().visSize()) / 2;
                        int thisavg = (this.getAspects().visSize() + this.getAspectsBase().visSize()) / 2;
                        if (ndavg < thisavg && nd.getAspects().size() > 0) {
                           Aspect a = nd.getAspects().getAspects()[this.level().rand.nextInt(nd.getAspects().size())];
                           boolean u = false;
                           if (this.getAspects().getAmount(a) < this.getNodeVisBase(a) && nd.takeFromContainer(a, 1)) {
                              this.addToContainer(a, 1);
                              u = true;
                           } else if (nd.takeFromContainer(a, 1)) {
                              if (this.level().rand.nextInt(1 + (int)((double)this.getNodeVisBase(a) / (shiny ? (double)1.5F : (double)1.0F))) == 0) {
                                 this.aspectsBase.add(a, 1);
                                 if (this.getNodeModifier() == NodeModifier.PALE && this.level().rand.nextInt(100) == 0) {
                                    this.setNodeModifier(null);
                                    this.regeneration = -1;
                                 }

                                 if (this.level().rand.nextInt(3) == 0) {
                                    nd.setNodeVisBase(a, (short)(nd.getNodeVisBase(a) - 1));
                                 }
                              }

                              u = true;
                           }

                           if (u) {
                              ((TileNode)te).wait = ((TileNode)te).regeneration / 2;
                              this.level().markBlockForUpdate(this.xCoord + x, this.yCoord + y, this.zCoord + z);
                              te.markDirty();
                              change = true;
                              PacketHandler.INSTANCE.sendToAllAround(new PacketFXBlockZap((float)(this.xCoord + x) + 0.5F, (float)(this.yCoord + y) + 0.5F, (float)(this.zCoord + z) + 0.5F, (float)this.xCoord + 0.5F, (float)this.yCoord + 0.5F, (float)this.zCoord + 0.5F), new NetworkRegistry.TargetPoint(this.level().dimension(), this.xCoord, this.yCoord, this.zCoord, 32.0F));
                           }
                        }
                     }
                  }

                  return change;
               }
            }
         }
      } else {
         return change;
      }
   }

   private boolean handleRecharge(boolean change) {
      if (this.regeneration < 0) {
         this.regeneration = 600;
         if (this.getNodeModifier() != null) {
            switch (this.getNodeModifier()) {
               case BRIGHT:
                  this.regeneration = 400;
                  break;
               case PALE:
                  this.regeneration = 900;
                  break;
               case FADING:
                  this.regeneration = 0;
            }
         }

         if (this.getLock() == 1) {
            this.regeneration *= 2;
         }

         if (this.getLock() == 2) {
            this.regeneration *= 20;
         }
      }

      if (this.catchUp) {
         this.catchUp = false;
         long ct = System.currentTimeMillis();
         int inc = this.regeneration * 75;
         int amt = inc > 0 ? (int)((ct - this.lastActive) / (long)inc) : 0;
         if (amt > 0) {
            for(int a = 0; a < Math.min(amt, this.aspectsBase.visSize()); ++a) {
               AspectList al = new AspectList();

               for(Aspect aspect : this.getAspects().getAspects()) {
                  if (this.getAspects().getAmount(aspect) < this.getNodeVisBase(aspect)) {
                     al.add(aspect, 1);
                  }
               }

               if (al.size() > 0) {
                  this.addToContainer(al.getAspects()[this.level().rand.nextInt(al.size())], 1);
               }
            }
         }
      }

      if (this.count % 1200 == 0) {
         for(Aspect aspect : this.getAspects().getAspects()) {
            if (this.getAspects().getAmount(aspect) <= 0) {
               this.setNodeVisBase(aspect, (short)(this.getNodeVisBase(aspect) - 1));
               if (this.level().rand.nextInt(20) == 0 || this.getNodeVisBase(aspect) <= 0) {
                  this.getAspects().remove(aspect);
                  if (this.level().rand.nextInt(5) == 0) {
                     if (this.getNodeModifier() == NodeModifier.BRIGHT) {
                        this.setNodeModifier(null);
                     } else if (this.getNodeModifier() == null) {
                        this.setNodeModifier(NodeModifier.PALE);
                     }

                     if (this.getNodeModifier() == NodeModifier.PALE && this.level().rand.nextInt(5) == 0) {
                        this.setNodeModifier(NodeModifier.FADING);
                     }
                  }

                  this.nodeChange();
                  break;
               }

               this.nodeChange();
            }
         }

         if (this.getAspects().size() <= 0) {
            this.invalidate();
            if (this.getBlockType() == ConfigBlocks.blockAiry) {
               this.level().setBlockToAir(this.xCoord, this.yCoord, this.zCoord);
            } else if (this.getBlockType() == ConfigBlocks.blockMagicalLog) {
               this.level().setBlockMetadataWithNotify(this.xCoord, this.yCoord, this.zCoord, this.level().getBlockMetadata(this.xCoord, this.yCoord, this.zCoord) - 1, 3);
            }
         }
      }

      if (this.wait > 0) {
         --this.wait;
      }

      if (this.regeneration > 0 && this.wait == 0 && this.count % this.regeneration == 0) {
         this.lastActive = System.currentTimeMillis();
         AspectList al = new AspectList();

         for(Aspect aspect : this.getAspects().getAspects()) {
            if (this.getAspects().getAmount(aspect) < this.getNodeVisBase(aspect)) {
               al.add(aspect, 1);
            }
         }

         if (al.size() > 0) {
            this.addToContainer(al.getAspects()[this.level().rand.nextInt(al.size())], 1);
            change = true;
         }
      }

      return change;
   }

   private boolean handleTaintNode(boolean change) {
      if (this.getNodeType() == NodeType.TAINTED && this.count % 50 == 0) {
         int x = 0;
         int z = 0;
         int y = 0;
         x = this.xCoord + this.level().rand.nextInt(8) - this.level().rand.nextInt(8);
         z = this.zCoord + this.level().rand.nextInt(8) - this.level().rand.nextInt(8);
         BiomeGenBase bg = this.level().getBiomeGenForCoords(x, z);
         if (bg.biomeID != ThaumcraftWorldGenerator.biomeTaint.biomeID) {
            Utils.setBiomeAt(this.level(), x, z, ThaumcraftWorldGenerator.biomeTaint);
         }

         if (Config.hardNode && this.level().rand.nextBoolean()) {
            x = this.xCoord + this.level().rand.nextInt(5) - this.level().rand.nextInt(5);
            z = this.zCoord + this.level().rand.nextInt(5) - this.level().rand.nextInt(5);
            y = this.yCoord + this.level().rand.nextInt(5) - this.level().rand.nextInt(5);
            if (BlockTaintFibres.spreadFibres(this.level(), x, y, z)) {
            }
         }
      } else if (this.getNodeType() != NodeType.PURE && this.getNodeType() != NodeType.TAINTED && this.count % 100 == 0) {
         BiomeGenBase bg = this.level().getBiomeGenForCoords(this.xCoord, this.zCoord);
         if (bg.biomeID == ThaumcraftWorldGenerator.biomeTaint.biomeID && this.level().rand.nextInt(500) == 0) {
            this.setNodeType(NodeType.TAINTED);
            this.nodeChange();
         }
      }

      return change;
   }

   private boolean handleNodeStability(boolean change) {
      if (this.count % 100 == 0) {
         if (this.getNodeType() == NodeType.UNSTABLE && this.level().rand.nextBoolean()) {
            if (this.getLock() == 0) {
               Aspect aspect = null;
               if ((aspect = this.takeRandomPrimalFromSource()) != null) {
                  EntityAspectOrb orb = new EntityAspectOrb(this.level(), (double)this.xCoord + (double)0.5F, (double)this.yCoord + (double)0.5F, (double)this.zCoord + (double)0.5F, aspect, 1);
                  this.level().spawnEntityInWorld(orb);
                  change = true;
               }
            } else if (this.level().rand.nextInt(10000 / this.getLock()) == 42) {
               this.setNodeType(NodeType.NORMAL);
               change = true;
            }
         }

         if (this.getNodeModifier() == NodeModifier.FADING && this.getLock() > 0 && this.level().rand.nextInt(12500 / this.getLock()) == 69) {
            this.setNodeModifier(NodeModifier.PALE);
            change = true;
         }
      }

      return change;
   }

   private boolean handlePureNode(boolean change) {
      int dimbl = ThaumcraftWorldGenerator.getDimBlacklist(this.level().dimension());
      if (this.level().dimension() != -1 && this.level().dimension() != 1 && dimbl != 0 && dimbl != 2 && this.getNodeType() == NodeType.PURE && this.count % 50 == 0) {
         int x = this.xCoord + this.level().rand.nextInt(8) - this.level().rand.nextInt(8);
         int z = this.zCoord + this.level().rand.nextInt(8) - this.level().rand.nextInt(8);
         BiomeGenBase bg = this.level().getBiomeGenForCoords(x, z);
         int biobl = ThaumcraftWorldGenerator.getBiomeBlacklist(bg.biomeID);
         if (biobl != 0 && biobl != 2 && bg.biomeID != ThaumcraftWorldGenerator.biomeMagicalForest.biomeID) {
            if (bg.biomeID == ThaumcraftWorldGenerator.biomeTaint.biomeID) {
               Utils.setBiomeAt(this.level(), x, z, ThaumcraftWorldGenerator.biomeMagicalForest);
            } else if (this.level().getBlock(this.xCoord, this.yCoord, this.zCoord) == ConfigBlocks.blockMagicalLog) {
               Utils.setBiomeAt(this.level(), x, z, ThaumcraftWorldGenerator.biomeMagicalForest);
            }
         }
      }

      return change;
   }

   private boolean handleDarkNode(boolean change) {
      int dimbl = ThaumcraftWorldGenerator.getDimBlacklist(this.level().dimension());
      int biobl = ThaumcraftWorldGenerator.getBiomeBlacklist(this.level().getBiomeGenForCoords(this.xCoord, this.zCoord).biomeID);
      if (biobl != 0 && biobl != 2 && this.level().dimension() != -1 && this.level().dimension() != 1 && dimbl != 0 && dimbl != 2 && this.getNodeType() == NodeType.DARK && this.count % 50 == 0) {
         int x = this.xCoord + this.level().rand.nextInt(12) - this.level().rand.nextInt(12);
         int z = this.zCoord + this.level().rand.nextInt(12) - this.level().rand.nextInt(12);
         BiomeGenBase bg = this.level().getBiomeGenForCoords(x, z);
         if (bg.biomeID != ThaumcraftWorldGenerator.biomeEerie.biomeID) {
            Utils.setBiomeAt(this.level(), x, z, ThaumcraftWorldGenerator.biomeEerie);
         }

         if (Config.hardNode && this.level().rand.nextBoolean() && this.level().getClosestPlayer((double)this.xCoord + (double)0.5F, (double)this.yCoord + (double)0.5F, (double)this.zCoord + (double)0.5F, 24.0F) != null) {
            EntityGiantBrainyZombie entity = new EntityGiantBrainyZombie(this.level());
            if (entity != null) {
               int j = this.level().getEntitiesWithinAABB(entity.getClass(), AxisAlignedBB.getBoundingBox(this.xCoord, this.yCoord, this.zCoord, this.xCoord + 1, this.yCoord + 1, this.zCoord + 1).expand(10.0F, 6.0F, 10.0F)).size();
               if (j <= 3) {
                  double d0 = (double)this.xCoord + (this.level().rand.nextDouble() - this.level().rand.nextDouble()) * (double)5.0F;
                  double d3 = this.yCoord + this.level().rand.nextInt(3) - 1;
                  double d4 = (double)this.zCoord + (this.level().rand.nextDouble() - this.level().rand.nextDouble()) * (double)5.0F;
                  EntityLiving entityliving = entity instanceof EntityLiving ? entity : null;
                  entity.setLocationAndAngles(d0, d3, d4, this.level().rand.nextFloat() * 360.0F, 0.0F);
                  if (entityliving == null || entityliving.getCanSpawnHere()) {
                     this.level().spawnEntityInWorld(entityliving);
                     this.level().playAuxSFX(2004, this.xCoord, this.yCoord, this.zCoord, 0);
                     if (entityliving != null) {
                        entityliving.spawnExplosionParticle();
                     }
                  }
               }
            }
         }
      }

      return change;
   }

   private boolean handleHungryNodeSecond(boolean change) {
      if (this.getNodeType() == NodeType.HUNGRY && this.count % 50 == 0) {
         int tx = this.xCoord + this.level().rand.nextInt(16) - this.level().rand.nextInt(16);
         int ty = this.yCoord + this.level().rand.nextInt(16) - this.level().rand.nextInt(16);
         int tz = this.zCoord + this.level().rand.nextInt(16) - this.level().rand.nextInt(16);
         if (ty > this.level().getHeightValue(tx, tz)) {
            ty = this.level().getHeightValue(tx, tz);
         }

         Vec3 v1 = Vec3.createVectorHelper((double)this.xCoord + (double)0.5F, (double)this.yCoord + (double)0.5F, (double)this.zCoord + (double)0.5F);
         Vec3 v2 = Vec3.createVectorHelper((double)tx + (double)0.5F, (double)ty + (double)0.5F, (double)tz + (double)0.5F);
         HitResult mop = ThaumcraftApiHelper.rayTraceIgnoringSource(this.level(), v1, v2, true, false, false);
         if (mop != null && this.getDistanceFrom(mop.blockX, mop.blockY, mop.blockZ) < (double)256.0F) {
            tx = mop.blockX;
            ty = mop.blockY;
            tz = mop.blockZ;
            Block bi = this.level().getBlock(tx, ty, tz);
            this.level().getBlockMetadata(tx, ty, tz);
            if (!bi.isAir(this.level(), tx, ty, tz)) {
               float h = bi.getBlockHardness(this.level(), tx, ty, tz);
               if (h >= 0.0F && h < 5.0F) {
                  this.level().func_147480_a(tx, ty, tz, true);
               }
            }
         }
      }

      return change;
   }

   public byte getLock() {
      return this.nodeLock;
   }

   public void checkLock() {
      if ((this.count <= 1 || this.count % 50 == 0) && this.yCoord > 0 && this.getBlockType() == ConfigBlocks.blockAiry) {
         byte oldLock = this.nodeLock;
         this.nodeLock = 0;
         if (!this.level().isBlockIndirectlyGettingPowered(this.xCoord, this.yCoord - 1, this.zCoord) && this.level().getBlock(this.xCoord, this.yCoord - 1, this.zCoord) == ConfigBlocks.blockStoneDevice) {
            if (this.level().getBlockMetadata(this.xCoord, this.yCoord - 1, this.zCoord) == 9) {
               this.nodeLock = 1;
            } else if (this.level().getBlockMetadata(this.xCoord, this.yCoord - 1, this.zCoord) == 10) {
               this.nodeLock = 2;
            }
         }

         if (oldLock != this.nodeLock) {
            this.regeneration = -1;
         }
      }

   }
}
