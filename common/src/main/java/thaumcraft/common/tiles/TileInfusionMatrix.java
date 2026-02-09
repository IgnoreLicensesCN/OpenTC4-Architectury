package thaumcraft.common.tiles;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.world.level.block.Block;
import net.minecraft.core.BlockPos;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemBlock;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.core.Direction;
import thaumcraft.api.tile.TileThaumcraft;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IAspectContainer;
import thaumcraft.api.crafting.interfaces.IInfusionStabiliser;
import thaumcraft.api.crafting.ThaumcraftInfusionEnchantmentRecipe;
import thaumcraft.api.crafting.InfusionRecipe;
import thaumcraft.api.wands.IWandable;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.container.InventoryFake;
import thaumcraft.common.lib.crafting.InfusionRunicAugmentRecipe;
import thaumcraft.common.lib.crafting.ThaumcraftCraftingManager;
import thaumcraft.common.lib.effects.ThaumcraftEffects;
import thaumcraft.common.lib.events.EssentiaHandler;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.fx.PacketFXBlockZapS2C;
import thaumcraft.common.lib.network.fx.PacketFXInfusionSourceS2C;
import thaumcraft.common.lib.utils.InventoryUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TileInfusionMatrix extends TileThaumcraft implements IWandable, IAspectContainer {
    private ArrayList<ChunkCoordinates> pedestals = new ArrayList<>();
    private int dangerCount = 0;
    public boolean active = false;
    public boolean crafting = false;
    public boolean checkSurroundings = true;
    public int symmetry = 0;
    public int instability = 0;
    private AspectList<Aspect>recipeEssentia = new AspectList<>();
    private ArrayList<ItemStack> recipeIngredients = null;
    private Object recipeOutput = null;
    private String recipePlayer = null;
    private String recipeOutputLabel = null;
    private ItemStack recipeInput = null;
    private int recipeInstability = 0;
    private int recipeXP = 0;
    private int recipeType = 0;
    public HashMap sourceFX = new HashMap<>();
    public int count = 0;
    public int craftCount = 0;
    public float startUp;
    private int countDelay = 10;
    ArrayList ingredients = new ArrayList<>();
    int itemCount = 0;

    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        return AxisAlignedBB.getBoundingBox(this.xCoord - 1, this.yCoord - 1, this.zCoord - 1, this.xCoord + 1, this.yCoord + 1, this.zCoord + 1);
    }

    public void readCustomNBT(NBTTagCompound nbtCompound) {
        this.active = nbtCompound.getBoolean("active");
        this.crafting = nbtCompound.getBoolean("crafting");
        this.instability = nbtCompound.getShort("instability");
        this.recipeEssentia.readFromNBT(nbtCompound);
    }

    public void writeCustomNBT(NBTTagCompound nbtCompound) {
        nbtCompound.setBoolean("active", this.active);
        nbtCompound.setBoolean("crafting", this.crafting);
        nbtCompound.setShort("instability", (short) this.instability);
        this.recipeEssentia.writeToNBT(nbtCompound);
    }

    public void readFromNBT(NBTTagCompound nbtCompound) {
        super.readFromNBT(nbtCompound);
        NBTTagList nbttaglist = nbtCompound.getTagList("recipein", 10);
        this.recipeIngredients = new ArrayList<>();

        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
            byte b0 = nbttagcompound1.getByte("item");
            this.recipeIngredients.add(ItemStack.loadItemStackFromNBT(nbttagcompound1));
        }

        String rot = nbtCompound.getString("rotype");
        if (rot != null && rot.equals("@")) {
            this.recipeOutput = ItemStack.loadItemStackFromNBT(nbtCompound.getCompoundTag("recipeout"));
        } else if (rot != null) {
            this.recipeOutputLabel = rot;
            this.recipeOutput = nbtCompound.getTag("recipeout");
        }

        this.recipeInput = ItemStack.loadItemStackFromNBT(nbtCompound.getCompoundTag("recipeinput"));
        this.recipeInstability = nbtCompound.getInteger("recipeinst");
        this.recipeType = nbtCompound.getInteger("recipetype");
        this.recipeXP = nbtCompound.getInteger("recipexp");
        this.recipePlayer = nbtCompound.getString("recipeplayer");
        if (this.recipePlayer.isEmpty()) {
            this.recipePlayer = null;
        }

    }

    public void writeToNBT(NBTTagCompound nbtCompound) {
        super.writeToNBT(nbtCompound);
        if (this.recipeIngredients != null && !this.recipeIngredients.isEmpty()) {
            NBTTagList nbttaglist = new NBTTagList();
            int count = 0;

            for (ItemStack stack : this.recipeIngredients) {
                if (stack != null) {
                    NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                    nbttagcompound1.setByte("item", (byte) count);
                    stack.writeToNBT(nbttagcompound1);
                    nbttaglist.appendTag(nbttagcompound1);
                    ++count;
                }
            }

            nbtCompound.setTag("recipein", nbttaglist);
        }

        if (this.recipeOutput != null && this.recipeOutput instanceof ItemStack) {
            nbtCompound.setString("rotype", "@");
        }

        if (this.recipeOutput != null && this.recipeOutput instanceof NBTBase) {
            nbtCompound.setString("rotype", this.recipeOutputLabel);
        }

        if (this.recipeOutput != null && this.recipeOutput instanceof ItemStack) {
            nbtCompound.setTag("recipeout", ((ItemStack) this.recipeOutput).writeToNBT(new NBTTagCompound()));
        }

        if (this.recipeOutput != null && this.recipeOutput instanceof NBTBase) {
            nbtCompound.setTag("recipeout", (NBTBase) this.recipeOutput);
        }

        if (this.recipeInput != null) {
            nbtCompound.setTag("recipeinput", this.recipeInput.writeToNBT(new NBTTagCompound()));
        }

        nbtCompound.setInteger("recipeinst", this.recipeInstability);
        nbtCompound.setInteger("recipetype", this.recipeType);
        nbtCompound.setInteger("recipexp", this.recipeXP);
        if (this.recipePlayer == null) {
            nbtCompound.setString("recipeplayer", "");
        } else {
            nbtCompound.setString("recipeplayer", this.recipePlayer);
        }

    }

    public boolean canUpdate() {
        return super.canUpdate();
    }

    public void updateEntity() {
        super.updateEntity();
        ++this.count;
        if (this.checkSurroundings) {
            this.checkSurroundings = false;
            this.getSurroundings();
        }

        if ((Platform.getEnvironment() == Env.CLIENT)) {
            this.doEffects();
        } else {
            if (this.count % (this.crafting ? 20 : 100) == 0 && !this.validLocation()) {
                this.active = false;
                this.markDirty();
                this.level().markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
                return;
            }

            if (this.active && this.crafting && this.count % this.countDelay == 0) {
                this.craftCycle();
                this.markDirty();
            }
        }

    }

    public boolean validLocation() {
        TileEntity te = null;
        te = this.level().getTileEntity(this.xCoord, this.yCoord - 2, this.zCoord);
        if (te instanceof TilePedestal) {
            te = this.level().getTileEntity(this.xCoord + 1, this.yCoord - 2, this.zCoord + 1);
            if (te instanceof TileInfusionPillar) {
                te = this.level().getTileEntity(this.xCoord + 1, this.yCoord - 2, this.zCoord - 1);
                if (te instanceof TileInfusionPillar) {
                    te = this.level().getTileEntity(this.xCoord - 1, this.yCoord - 2, this.zCoord - 1);
                    if (te instanceof TileInfusionPillar) {
                        te = this.level().getTileEntity(this.xCoord - 1, this.yCoord - 2, this.zCoord + 1);
                        return te instanceof TileInfusionPillar;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public void craftingStart(Player player) {
        if (!this.validLocation()) {
            this.active = false;
            this.markDirty();
            this.level().markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
        } else {
            this.getSurroundings();
            TileEntity te = null;
            this.recipeInput = null;
            te = this.level().getTileEntity(this.xCoord, this.yCoord - 2, this.zCoord);
            if (te instanceof TilePedestal) {
                TilePedestal ped = (TilePedestal) te;
                if (ped.getStackInSlot(0) != null) {
                    this.recipeInput = ped.getStackInSlot(0).copy();
                }
            }

            if (this.recipeInput != null) {
                ArrayList<ItemStack> components = new ArrayList<>();

                for (ChunkCoordinates cc : this.pedestals) {
                    te = this.level().getTileEntity(cc.posX, cc.posY, cc.posZ);
                    if (te instanceof TilePedestal) {
                        TilePedestal ped = (TilePedestal) te;
                        if (ped.getStackInSlot(0) != null) {
                            components.add(ped.getStackInSlot(0).copy());
                        }
                    }
                }

                if (!components.isEmpty()) {
                    InfusionRecipe recipe = ThaumcraftCraftingManager.findMatchingInfusionRecipe(components, this.recipeInput, player);
                    if (recipe != null) {
                        this.recipeType = 0;
                        this.recipeIngredients = new ArrayList<>();
                        if (recipe instanceof InfusionRunicAugmentRecipe) {
                            for (ItemStack ing : ((InfusionRunicAugmentRecipe) recipe).getComponents(this.recipeInput)) {
                                this.recipeIngredients.add(ing.copy());
                            }
                        } else {
                            for (ItemStack ing : recipe.getComponents()) {
                                this.recipeIngredients.add(ing.copy());
                            }
                        }

                        if (recipe.getRecipeOutput(this.recipeInput) instanceof Object[]) {
                            Object[] obj = (Object[]) recipe.getRecipeOutput(this.recipeInput);
                            this.recipeOutputLabel = (String) obj[0];
                            this.recipeOutput = obj[1];
                        } else {
                            this.recipeOutput = recipe.getRecipeOutput(this.recipeInput);
                        }

                        this.recipeInstability = recipe.getInstability(this.recipeInput);
                        this.recipeEssentia = recipe.getAspects(this.recipeInput).copy();
                        this.recipePlayer = player.getCommandSenderName();
                        this.instability = this.symmetry + this.recipeInstability;
                        this.crafting = true;
                        this.level().playSoundEffect(this.xCoord, this.yCoord, this.zCoord, "thaumcraft:craftstart", 0.5F, 1.0F);
                        this.level().markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
                        this.markDirty();
                    } else {
                        ThaumcraftInfusionEnchantmentRecipe recipe2 = ThaumcraftCraftingManager.findMatchingInfusionEnchantmentRecipe(components, this.recipeInput, player);
                        if (recipe2 != null) {
                            this.recipeType = 1;
                            this.recipeIngredients = new ArrayList<>();

                            for (ItemStack ing : recipe2.components) {
                                this.recipeIngredients.add(ing.copy());
                            }

                            this.recipeOutput = recipe2.getEnchantment();
                            this.recipeInstability = recipe2.calcInstability(this.recipeInput);
                            AspectList<Aspect>esscost = recipe2.basicCostAspects.copy();
                            float essmod = recipe2.getAspectsModified(this.recipeInput);

                            for (Aspect as : esscost.getAspects()) {
                                esscost.addAll(as, (int) ((float) esscost.getAmount(as) * essmod));
                            }

                            this.recipeEssentia = esscost;
                            this.recipeXP = recipe2.calcXP(this.recipeInput);
                            this.instability = this.symmetry + this.recipeInstability;
                            this.crafting = true;
                            this.level().playSoundEffect(this.xCoord, this.yCoord, this.zCoord, "thaumcraft:craftstart", 0.5F, 1.0F);
                            this.level().markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
                            this.markDirty();
                        }
                    }
                }
            }
        }
    }

    public void craftCycle() {
        boolean validCraftingFlag = false;
        TileEntity te = this.level().getTileEntity(this.xCoord, this.yCoord - 2, this.zCoord);
        if (te instanceof TilePedestal) {
            TilePedestal ped = (TilePedestal) te;
            if (ped.getStackInSlot(0) != null) {
                ItemStack i2 = ped.getStackInSlot(0).copy();
                if (this.recipeInput.getItemDamage() == 32767) {
                    i2.setItemDamage(32767);
                }

                if (InventoryUtils.areItemStacksEqualForCrafting(i2, this.recipeInput, true, true, false)) {
                    validCraftingFlag = true;
                }
            }
        }

        if (!validCraftingFlag || this.instability > 0 && Math.max(1, this.level().rand.nextInt(500)) <= this.instability) {
            //events for instability
            switch (this.level().rand.nextInt(21)) {
                case 0:
                case 2:
                case 10:
                case 13:
                    this.inEvEjectItem(0);
                    break;
                case 1:
                case 11:
                    this.inEvEjectItem(2);
                    break;
                case 3:
                case 8:
                case 14:
                    this.inEvZap(false);
                    break;
                case 4:
                case 15:
                    this.inEvEjectItem(5);
                    break;
                case 5:
                case 16:
                    this.inEvHarm(false);
                    break;
                case 6:
                case 17:
                    this.inEvEjectItem(1);
                    break;
                case 7:
                    this.inEvEjectItem(4);
                    break;
                case 9:
                    this.level().createExplosion(null, (float) this.xCoord + 0.5F, (float) this.yCoord + 0.5F, (float) this.zCoord + 0.5F, 1.5F + this.level().rand.nextFloat(), false);
                    break;
                case 12:
                    this.inEvZap(true);
                    break;
                case 18:
                    this.inEvHarm(true);
                    break;
                case 19:
                    this.inEvEjectItem(3);
                    break;
                case 20:
                    this.inEvWarp();
            }

            if (validCraftingFlag) {
                return;
            }
        }

        if (!validCraftingFlag) {
            //cancel infusion
            this.instability = 0;
            this.crafting = false;
            this.recipeEssentia = new AspectList<>();
            this.level().markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
            this.level().playSoundEffect(this.xCoord, this.yCoord, this.zCoord, "thaumcraft:craftfail", 1.0F, 0.6F);
            this.markDirty();
        } else if (this.recipeType == 1 && this.recipeXP > 0) {
            List<Player> targets =
                    this.level().getEntitiesWithinAABB(Player.class,
                            AxisAlignedBB.getBoundingBox(
                                            this.xCoord, this.yCoord, this.zCoord,
                                            this.xCoord + 1, this.yCoord + 1, this.zCoord + 1
                                    )
                                    .expand(10.0F, 10.0F, 10.0F)
                    );
            if (targets != null && !targets.isEmpty()) {
                for (Player playerBeingTokenXP : targets) {
                    //taking XP from players(yes multiple players!)
                    if (playerBeingTokenXP.experienceLevel > 0) {
                        playerBeingTokenXP.addExperienceLevel(-1);
                        --this.recipeXP;
                        playerBeingTokenXP.attackEntityFrom(DamageSource.magic, (float) this.level().rand.nextInt(2));
                        PacketFXInfusionSourceS2C var22 = new PacketFXInfusionSourceS2C(
                                this.xCoord, this.yCoord, this.zCoord,
                                (byte) 0, (byte) 0, (byte) 0,
                                playerBeingTokenXP.getEntityId());
                        PacketHandler.INSTANCE.sendToAllAround(var22, new NetworkRegistry.TargetPoint(
                                this.getLevel().dimension(),
                                this.xCoord, this.yCoord, this.zCoord,
                                32.0F));
                        this.level().playSoundAtEntity(playerBeingTokenXP,
                                "random.fizz",
                                1.0F,
                                2.0F + this.level().rand.nextFloat() * 0.4F);
                        this.countDelay = 20;
                        return;
                    }
                }

                Aspect[] recipeRequiredAspects = this.recipeEssentia.getAspects();
                if (recipeRequiredAspects != null
                        && recipeRequiredAspects.length > 0
                        && this.level().rand.nextInt(3) == 0) {
                    addInstabilityAndRequiredAspect(recipeRequiredAspects);
                }
            }

        } else {
            if (this.recipeType == 1 && this.recipeXP == 0) {
                this.countDelay = 10;
            }

            if (this.recipeEssentia.visSize() > 0) {
                //draining essentia,may add instability if failed
                for (Aspect aspect : this.recipeEssentia.getAspects()) {
                    if (this.recipeEssentia.getAmount(aspect) > 0) {
                        if (EssentiaHandler.drainEssentia(this, aspect, Direction.UNKNOWN, 12)) {
                            this.recipeEssentia.reduce(aspect, 1);
                            this.level().markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
                            this.markDirty();
                            return;
                        }

                        if (this.level().rand.nextInt(100 - this.recipeInstability * 3) == 0) {
                            ++this.instability;
                        }
                        checkInstability();
                    }
                }

                this.checkSurroundings = true;
            } else if (this.recipeIngredients.isEmpty()) {
                //finish infusion
                this.instability = 0;
                this.crafting = false;
                this.craftingFinish(this.recipeOutput, this.recipeOutputLabel);
                this.recipeOutput = null;
                this.level().markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
                this.markDirty();
            } else {
                for (int a = 0; a < this.recipeIngredients.size(); ++a) {
                    for (ChunkCoordinates cc : this.pedestals) {
                        te = this.level().getTileEntity(cc.posX, cc.posY, cc.posZ);
                        if (te instanceof TilePedestal && ((TilePedestal) te).getStackInSlot(0) != null && InfusionRecipe.areItemStacksEqual(((TilePedestal) te).getStackInSlot(0), this.recipeIngredients.get(a), true)) {
                            if (this.itemCount == 0) {
                                this.itemCount = 5;
                                SimpleNetworkWrapper var10000 = PacketHandler.INSTANCE;
                                PacketFXInfusionSourceS2C var10001 = new PacketFXInfusionSourceS2C(this.xCoord, this.yCoord, this.zCoord, (byte) (this.xCoord - cc.posX), (byte) (this.yCoord - cc.posY), (byte) (this.zCoord - cc.posZ), 0);
                                double var10005 = this.xCoord;
                                double var10006 = this.yCoord;
                                double var10007 = this.zCoord;
                                var10000.sendToAllAround(var10001, new NetworkRegistry.TargetPoint(this.getLevel().dimension(), var10005, var10006, var10007, 32.0F));
                            } else if (this.itemCount-- <= 1) {
                                ItemStack is = ((TilePedestal) te).getStackInSlot(0).getItem().getContainerItem(((TilePedestal) te).getStackInSlot(0));
                                ((TilePedestal) te).setInventorySlotContents(0, is == null ? null : is.copy());
                                this.recipeIngredients.remove(a);
                            }
                            return;
                        }
                    }

                    Aspect[] recipeRequiredAspects = this.recipeEssentia.getAspects();
                    if (recipeRequiredAspects != null
                            && recipeRequiredAspects.length > 0
                            && this.level().rand.nextInt(1 + a) == 0) {
                        addInstabilityAndRequiredAspect(recipeRequiredAspects);
                    }
                }

            }
        }
    }

    private void addInstabilityAndRequiredAspect(Aspect[] recipeRequiredAspects) {
        Aspect asToAdd = recipeRequiredAspects[this.level().rand.nextInt(recipeRequiredAspects.length)];
        this.recipeEssentia.addAll(asToAdd, 1);
        if (this.level().rand.nextInt(50 - this.recipeInstability * 2) == 0) {
            ++this.instability;
        }

        checkInstability();
    }

    private void checkInstability() {
        if (this.instability > 25) {
            this.instability = 25;
        }

        this.level().markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
        this.markDirty();
    }

    private void inEvZap(boolean all) {
        List<Entity> targets = this.level().getEntitiesWithinAABB(EntityLivingBase.class, AxisAlignedBB.getBoundingBox(this.xCoord, this.yCoord, this.zCoord, this.xCoord + 1, this.yCoord + 1, this.zCoord + 1).expand(10.0F, 10.0F, 10.0F));
        if (targets != null && !targets.isEmpty()) {
            for (Entity target : targets) {
                PacketHandler.INSTANCE.sendToAllAround(new PacketFXBlockZapS2C((float) this.xCoord + 0.5F, (float) this.yCoord + 0.5F, (float) this.zCoord + 0.5F, (float) target.posX, (float) target.posY + target.height / 2.0F, (float) target.posZ), new NetworkRegistry.TargetPoint(this.level().dimension(), this.xCoord, this.yCoord, this.zCoord, 32.0F));
                target.attackEntityFrom(DamageSource.magic, (float) (4 + this.level().rand.nextInt(4)));
                if (!all) {
                    break;
                }
            }
        }

    }

    private void inEvHarm(boolean all) {
        List<EntityLivingBase> targets = this.level().getEntitiesWithinAABB(EntityLivingBase.class, AxisAlignedBB.getBoundingBox(this.xCoord, this.yCoord, this.zCoord, this.xCoord + 1, this.yCoord + 1, this.zCoord + 1).expand(10.0F, 10.0F, 10.0F));
        if (targets != null && !targets.isEmpty()) {
            for (EntityLivingBase target : targets) {
                if (this.level().rand.nextBoolean()) {
                    target.addPotionEffect(new PotionEffect(ThaumcraftEffects.FLUX_TAINT, 120, 0, false));
                } else {
                    PotionEffect pe = new PotionEffect(ThaumcraftEffects.VIS_EXHAUST, 2400, 0, true);
                    target.addPotionEffect(pe);
                }

                if (!all) {
                    break;
                }
            }
        }

    }

    private void inEvWarp() {
        List<Player> targets = this.level().getEntitiesWithinAABB(Player.class, AxisAlignedBB.getBoundingBox(this.xCoord, this.yCoord, this.zCoord, this.xCoord + 1, this.yCoord + 1, this.zCoord + 1).expand(10.0F, 10.0F, 10.0F));
        if (targets != null && !targets.isEmpty()) {
            Player target = targets.get(this.level().rand.nextInt(targets.size()));
            if (this.level().rand.nextFloat() < 0.25F) {
                Thaumcraft.addStickyWarpToPlayer(target, 1);
            } else {
                Thaumcraft.addWarpToPlayer(target, 1 + this.level().rand.nextInt(5), true);
            }
        }

    }

    private void inEvEjectItem(int type) {
        for (int q = 0; q < 50 && !this.pedestals.isEmpty(); ++q) {
            ChunkCoordinates cc = this.pedestals.get(this.level().rand.nextInt(this.pedestals.size()));
            TileEntity te = this.level().getTileEntity(cc.posX, cc.posY, cc.posZ);
            if (te instanceof TilePedestal && ((TilePedestal) te).getStackInSlot(0) != null) {
                if (type >= 3 && type != 5) {
                    ((TilePedestal) te).setInventorySlotContents(0, null);
                } else {
                    InventoryUtils.dropItems(this.level(), cc.posX, cc.posY, cc.posZ);
                }

                if (type != 1 && type != 3) {
                    if (type != 2 && type != 4) {
                        if (type == 5) {
                            this.level().createExplosion(null, (float) cc.posX + 0.5F, (float) cc.posY + 0.5F, (float) cc.posZ + 0.5F, 1.0F, false);
                        }
                    } else {
                        this.level().setBlock(cc.posX, cc.posY + 1, cc.posZ, ConfigBlocks.blockFluxGas, 7, 3);
                        this.level().playSoundEffect(cc.posX, cc.posY, cc.posZ, "random.fizz", 0.3F, 1.0F);
                    }
                } else {
                    this.level().setBlock(cc.posX, cc.posY + 1, cc.posZ, ConfigBlocks.blockFluxGoo, 7, 3);
                    this.level().playSoundEffect(cc.posX, cc.posY, cc.posZ, "game.neutral.swim", 0.3F, 1.0F);
                }

                this.level().addBlockEvent(cc.posX, cc.posY, cc.posZ, ConfigBlocks.blockStoneDevice, 11, 0);
                PacketHandler.INSTANCE.sendToAllAround(new PacketFXBlockZapS2C((float) this.xCoord + 0.5F, (float) this.yCoord + 0.5F, (float) this.zCoord + 0.5F, (float) cc.posX + 0.5F, (float) cc.posY + 1.5F, (float) cc.posZ + 0.5F), new NetworkRegistry.TargetPoint(this.level().dimension(), this.xCoord, this.yCoord, this.zCoord, 32.0F));
                return;
            }
        }

    }

    public void craftingFinish(Object out, String label) {
        TileEntity te = this.level().getTileEntity(this.xCoord, this.yCoord - 2, this.zCoord);
        if (te instanceof TilePedestal) {
            if (out instanceof ItemStack) {
                ((TilePedestal) te).setInventorySlotContentsFromInfusion(0, ((ItemStack) out).copy());
            } else if (out instanceof NBTBase) {
                ItemStack temp = ((TilePedestal) te).getStackInSlot(0);
                NBTBase tag = (NBTBase) out;
                temp.setTagInfo(label, tag);
                this.level().markBlockForUpdate(this.xCoord, this.yCoord - 2, this.zCoord);
                te.markDirty();
            } else if (out instanceof Enchantment) {
                ItemStack temp = ((TilePedestal) te).getStackInSlot(0);
                Map enchantments = EnchantmentHelper.getEnchantments(temp);
                enchantments.put(((Enchantment) out).effectId, EnchantmentHelper.getEnchantmentLevel(((Enchantment) out).effectId, temp) + 1);
                EnchantmentHelper.setEnchantments(enchantments, temp);
                this.level().markBlockForUpdate(this.xCoord, this.yCoord - 2, this.zCoord);
                te.markDirty();
            }

            if (this.recipePlayer != null) {
                Player p = this.level().getPlayerEntityByName(this.recipePlayer);
                if (p != null) {
                    FMLCommonHandler.instance().firePlayerCraftingEvent(p, ((TilePedestal) te).getStackInSlot(0), new InventoryFake(this.recipeIngredients));
                }
            }

            this.recipeEssentia = new AspectList<>();
            this.level().markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
            this.markDirty();
            this.level().addBlockEvent(this.xCoord, this.yCoord - 2, this.zCoord, ConfigBlocks.blockStoneDevice, 12, 0);
        }

    }

    private void getSurroundings() {
        ArrayList<ChunkCoordinates> stuff = new ArrayList<>();
        this.pedestals.clear();

        try {
            for (int xx = -12; xx <= 12; ++xx) {
                for (int zz = -12; zz <= 12; ++zz) {
                    boolean skip = false;

                    for (int yy = -5; yy <= 10; ++yy) {
                        if (xx != 0 || zz != 0) {
                            int x = this.xCoord + xx;
                            int y = this.yCoord - yy;
                            int z = this.zCoord + zz;
                            TileEntity te = this.level().getTileEntity(x, y, z);
                            if (!skip && yy > 0 && Math.abs(xx) <= 8 && Math.abs(zz) <= 8 && te instanceof TilePedestal) {
                                this.pedestals.add(new ChunkCoordinates(x, y, z));
                                skip = true;
                            } else {
                                Block bi = this.level().getBlock(x, y, z);
                                if (bi == Blocks.skull || bi instanceof IInfusionStabiliser && ((IInfusionStabiliser) bi).canStabaliseInfusion(this.getLevel(), x, y, z)) {
                                    stuff.add(new ChunkCoordinates(x, y, z));
                                }
                            }
                        }
                    }
                }
            }

            this.symmetry = 0;

            for (ChunkCoordinates cc : this.pedestals) {
                boolean items = false;
                int x = this.xCoord - cc.posX;
                int z = this.zCoord - cc.posZ;
                TileEntity te = this.level().getTileEntity(cc.posX, cc.posY, cc.posZ);
                if (te instanceof TilePedestal) {
                    this.symmetry += 2;
                    if (((TilePedestal) te).getStackInSlot(0) != null) {
                        ++this.symmetry;
                        items = true;
                    }
                }

                int xx = this.xCoord + x;
                int zz = this.zCoord + z;
                te = this.level().getTileEntity(xx, cc.posY, zz);
                if (te instanceof TilePedestal) {
                    this.symmetry -= 2;
                    if (((TilePedestal) te).getStackInSlot(0) != null && items) {
                        --this.symmetry;
                    }
                }
            }

            float sym = 0.0F;

            for (ChunkCoordinates cc : stuff) {
                boolean items = false;
                int x = this.xCoord - cc.posX;
                int z = this.zCoord - cc.posZ;
                Block bi = this.level().getBlock(cc.posX, cc.posY, cc.posZ);
                if (bi == Blocks.skull || bi instanceof IInfusionStabiliser && ((IInfusionStabiliser) bi).canStabaliseInfusion(this.getLevel(), cc.posX, cc.posY, cc.posZ)) {
                    sym += 0.1F;
                }

                int xx = this.xCoord + x;
                int zz = this.zCoord + z;
                bi = this.level().getBlock(xx, cc.posY, zz);
                if (bi == Blocks.skull || bi instanceof IInfusionStabiliser && ((IInfusionStabiliser) bi).canStabaliseInfusion(this.getLevel(), cc.posX, cc.posY, cc.posZ)) {
                    sym -= 0.2F;
                }
            }

            this.symmetry = (int) ((float) this.symmetry + sym);
        } catch (Exception ignored) {
        }

    }

    public int onWandRightClick(World world, ItemStack wandstack, Player player, int x, int y, int z, int side, int md) {
        if (Platform.getEnvironment() != Env.CLIENT && this.active && !this.crafting) {
            this.craftingStart(player);
            return 0;
        } else if (Platform.getEnvironment() != Env.CLIENT && !this.active && this.validLocation()) {
            this.active = true;
            this.level().markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
            this.markDirty();
            return 0;
        } else {
            return -1;
        }
    }

    public ItemStack onWandRightClick(World world, ItemStack wandstack, Player player) {
        return wandstack;
    }

    public void onUsingWandTick(ItemStack wandstack, Player player, int count) {
    }

    public void onWandStoppedUsing(ItemStack wandstack, World world, Player player, int count) {
    }

    private void doEffects() {
        if (this.crafting) {
            if (this.craftCount == 0) {
                this.level().playSound(this.xCoord, this.yCoord, this.zCoord, "thaumcraft:infuserstart", 0.5F, 1.0F, false);
            } else if (this.craftCount % 65 == 0) {
                this.level().playSound(this.xCoord, this.yCoord, this.zCoord, "thaumcraft:infuser", 0.5F, 1.0F, false);
            }

            ++this.craftCount;
            ClientFXUtils.blockRunes(this.level(), this.xCoord, this.yCoord - 2, this.zCoord, 0.5F + this.level().rand.nextFloat() * 0.2F, 0.1F, 0.7F + this.level().rand.nextFloat() * 0.3F, 25, -0.03F);
        } else if (this.craftCount > 0) {
            this.craftCount -= 2;
            if (this.craftCount < 0) {
                this.craftCount = 0;
            }

            if (this.craftCount > 50) {
                this.craftCount = 50;
            }
        }

        if (this.active && this.startUp != 1.0F) {
            if (this.startUp < 1.0F) {
                this.startUp += Math.max(this.startUp / 10.0F, 0.001F);
            }

            if ((double) this.startUp > 0.999) {
                this.startUp = 1.0F;
            }
        }

        if (!this.active && this.startUp > 0.0F) {

            this.startUp -= this.startUp / 10.0F;


            if ((double) this.startUp < 0.001) {
                this.startUp = 0.0F;
            }
        }

        for (String fxk : (String[]) this.sourceFX.keySet().toArray(new String[0])) {
            SourceFX fx = (SourceFX) this.sourceFX.get(fxk);
            if (fx.ticks <= 0) {
                this.sourceFX.remove(fxk);
            } else {
                if (fx.loc.posX == this.xCoord && fx.loc.posY == this.yCoord && fx.loc.posZ == this.zCoord) {
                    Entity player = this.level().getEntityByID(fx.color);
                    if (player != null) {
                        for (int a = 0; a < Thaumcraft.proxy.particleCount(2); ++a) {
                            Thaumcraft.proxy.drawInfusionParticles4(this.level(), player.posX + (double) ((this.level().rand.nextFloat() - this.level().rand.nextFloat()) * player.width), player.boundingBox.minY + (double) (this.level().rand.nextFloat() * player.height), player.posZ + (double) ((this.level().rand.nextFloat() - this.level().rand.nextFloat()) * player.width), this.xCoord, this.yCoord, this.zCoord);
                        }
                    }
                } else {
                    TileEntity tile = this.level().getTileEntity(fx.loc.posX, fx.loc.posY, fx.loc.posZ);
                    if (tile instanceof TilePedestal) {
                        ItemStack is = ((TilePedestal) tile).getStackInSlot(0);
                        if (is != null) {
                            if (this.level().rand.nextInt(3) == 0) {
                                Thaumcraft.proxy.drawInfusionParticles3(this.level(), (float) fx.loc.posX + this.level().rand.nextFloat(), (float) fx.loc.posY + this.level().rand.nextFloat() + 1.0F, (float) fx.loc.posZ + this.level().rand.nextFloat(), this.xCoord, this.yCoord, this.zCoord);
                            } else {
                                Item bi = is.getItem();
                                int md = is.getItemDamage();
                                if (is.getItemSpriteNumber() == 0 && bi instanceof ItemBlock) {
                                    for (int a = 0; a < Thaumcraft.proxy.particleCount(2); ++a) {
                                        Thaumcraft.proxy.drawInfusionParticles2(this.level(), (float) fx.loc.posX + this.level().rand.nextFloat(), (float) fx.loc.posY + this.level().rand.nextFloat() + 1.0F, (float) fx.loc.posZ + this.level().rand.nextFloat(), this.xCoord, this.yCoord, this.zCoord, Block.getBlockFromItem(bi), md);
                                    }
                                } else {
                                    for (int a = 0; a < Thaumcraft.proxy.particleCount(2); ++a) {
                                        Thaumcraft.proxy.drawInfusionParticles1(this.level(), (float) fx.loc.posX + 0.4F + this.level().rand.nextFloat() * 0.2F, (float) fx.loc.posY + 1.23F + this.level().rand.nextFloat() * 0.2F, (float) fx.loc.posZ + 0.4F + this.level().rand.nextFloat() * 0.2F, this.xCoord, this.yCoord, this.zCoord, bi, md);
                                    }
                                }
                            }
                        }
                    } else {
                        fx.ticks = 0;
                    }
                }

                --fx.ticks;
                this.sourceFX.put(fxk, fx);
            }
        }

        if (this.crafting && this.instability > 0 && this.level().rand.nextInt(200) <= this.instability) {
            Thaumcraft.proxy.nodeBolt(this.level(), (float) this.xCoord + 0.5F, (float) this.yCoord + 0.5F, (float) this.zCoord + 0.5F, (float) this.xCoord + 0.5F + (this.level().rand.nextFloat() - this.level().rand.nextFloat()) * 2.0F, (float) this.yCoord + 0.5F + (this.level().rand.nextFloat() - this.level().rand.nextFloat()) * 2.0F, (float) this.zCoord + 0.5F + (this.level().rand.nextFloat() - this.level().rand.nextFloat()) * 2.0F);
        }

    }

    public AspectList<Aspect>getAspects() {
        return this.recipeEssentia;
    }

    public void setAspects(AspectList<Aspect>aspects) {
    }

    public int addToContainer(Aspect tag, int amount) {
        return 0;
    }

    public boolean takeFromContainer(Aspect tag, int amount) {
        return false;
    }

    public boolean takeFromContainer(AspectList<Aspect>ot) {
        return false;
    }

    public boolean doesContainerContainAmount(Aspect tag, int amount) {
        return false;
    }

    public boolean doesContainerContain(AspectList<Aspect>ot) {
        return false;
    }

    public int containerContains(Aspect tag) {
        return 0;
    }

    public boolean doesContainerAccept(Aspect tag) {
        return true;
    }

    public static class SourceFX {
        public BlockPos loc;
        public int ticks;
        public int color;
        public int entity;

        public SourceFX(BlockPos loc, int ticks, int color) {
            this.loc = loc;
            this.ticks = ticks;
            this.color = color;
        }
    }
}
