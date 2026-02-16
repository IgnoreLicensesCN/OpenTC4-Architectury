package thaumcraft.common.items.wands;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.*;
import thaumcraft.api.IArchitect;
import thaumcraft.api.wands.*;
import thaumcraft.common.config.Config;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.tiles.TileOwned;

import java.util.ArrayList;

@Deprecated(forRemoval = true)
public class ItemWandCasting /*extends Item implements IArchitect*/ {
//    private IIcon icon;
//    DecimalFormat myFormatter = new DecimalFormat("#######.##");
//    public ItemFocusBasic.WandFocusAnimation animation = null;

//    public ItemWandCasting() {
//        this.maxStackSize = 1;
//        this.setMaxDamage(0);
//        this.setHasSubtypes(true);
//        this.setCreativeTab(Thaumcraft.tabTC);
//    }

//    public boolean isDamageable() {
//        return false;
//    }
//
//    @SideOnly(Side.CLIENT)
//    public void registerIcons(IIconRegister par1IconRegister) {
//        this.icon = par1IconRegister.registerIcon("thaumcraft:blank");
//    }

//    @SideOnly(Side.CLIENT)
//    public IIcon getIcon(ItemStack stack, int pass) {
//        return this.icon;
//    }
//
//    @SideOnly(Side.CLIENT)
//    public boolean isFull3D() {
//        return true;
//    }

//    public int getMaxVis(ItemStack stack) {
//        return this.getRod(stack).getCapacity() * (this.isSceptre(stack) ? 150 : 100);
//    }
//
//    public EnumRarity getRarity(ItemStack itemstack) {
//        return EnumRarity.uncommon;
//    }

//    @SideOnly(Side.CLIENT)
//    public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
//        ItemStack w1 = new ItemStack(this, 1, 0);
//        ItemStack w2 = new ItemStack(this, 1, 9);
//        ItemStack w3 = new ItemStack(this, 1, 54);
//        ((WandCastingItem) w2.getItem()).setCap(w2, ConfigItems.WAND_CAP_GOLD);
//        ((WandCastingItem) w3.getItem()).setCap(w3, ConfigItems.WAND_CAP_THAUMIUM);
//        ((WandCastingItem) w2.getItem()).setRod(w2, ConfigItems.WAND_ROD_GREATWOOD);
//        ((WandCastingItem) w3.getItem()).setRod(w3, ConfigItems.WAND_ROD_SILVERWOOD);
//        ItemStack sceptre = new ItemStack(ConfigItems.WandCastingItem, 1, 128);
//        ((WandCastingItem) sceptre.getItem()).setCap(sceptre, ConfigItems.WAND_CAP_THAUMIUM);
//        ((WandCastingItem) sceptre.getItem()).setRod(sceptre, ConfigItems.WAND_ROD_SILVERWOOD);
//        sceptre.setTagInfo("sceptre", new NBTTagByte((byte) 1));
//
//        for (Aspect aspect : Aspect.getPrimalAspects()) {
//            ((WandCastingItem) w1.getItem()).addVis(w1, aspect, ((WandCastingItem) w1.getItem()).getMaxVis(w1), true);
//            ((WandCastingItem) w2.getItem()).addVis(w2, aspect, ((WandCastingItem) w2.getItem()).getMaxVis(w2), true);
//            ((WandCastingItem) w3.getItem()).addVis(w3, aspect, ((WandCastingItem) w3.getItem()).getMaxVis(w3), true);
//            ((WandCastingItem) sceptre.getItem()).addVis(sceptre, aspect, ((WandCastingItem) sceptre.getItem()).getMaxVis(sceptre), true);
//        }
//
//        par3List.add(w1);
//        par3List.add(w2);
//        par3List.add(w3);
//        par3List.add(sceptre);
//    }

//    public String getItemStackDisplayName(ItemStack is) {
//        String name = StatCollector.translateToLocal("item.Wand.name");
//        name = name.replace("%CAP", StatCollector.translateToLocal("item.Wand." + this.getCap(is).getTag() + ".cap"));
//        String rod = this.getRod(is).getTag();
//        if (rod.contains("_staff")) {
//            rod = rod.substring(0, this.getRod(is).getTag().indexOf("_staff"));
//        }
//
//        name = name.replace("%ROD", StatCollector.translateToLocal("item.Wand." + rod + ".rod"));
//        name = name.replace("%OBJ", this.isStaff(is) ? StatCollector.translateToLocal("item.Wand.staff.obj") : (this.isSceptre(is) ? StatCollector.translateToLocal("item.Wand.sceptre.obj") : StatCollector.translateToLocal("item.Wand.wand.obj")));
//        return name;
//    }

//    public void addInformation(ItemStack stack, Player player, List list, boolean par4) {
//        int pos = list.size();
//        String tt2 = "";
//        if (stack.hasTagCompound()) {
//            StringBuilder tt = new StringBuilder();
//            int tot = 0;
//            int num = 0;
//
//            for (Aspect aspect : Aspect.getPrimalAspects()) {
//                if (stack.stackTagCompound.hasKey(aspect.getTag())) {
//                    String amount = this.myFormatter.format((float) stack.stackTagCompound.getInteger(aspect.getTag()) / 100.0F);
//                    float mod = this.getConsumptionModifier(stack, player, aspect, false);
//                    String consumption = this.myFormatter.format(mod * 100.0F);
//                    ++num;
//                    tot = (int) ((float) tot + mod * 100.0F);
//                    String text = "";
//                    ItemStack focus = this.getFocusItem(stack);
//                    if (focus != null) {
//                        int amt = ((ItemFocusBasic) focus.getItem()).getCentiVisCost(focus).getAmount(aspect);
//                        if (amt > 0) {
//                            text = "§r, " + this.myFormatter.format((float) amt * mod / 100.0F) + " " + StatCollector.translateToLocal(((ItemFocusBasic) focus.getItem()).isVisCostPerTick(focus) ? "item.Focus.cost2" : "item.Focus.cost1");
//                        }
//                    }
//
//                    if (Thaumcraft.proxy.isShiftKeyDown()) {
//                        list.add(" §" + aspect.getChatcolor() + aspect.getName() + "§r x " + amount + ", §o(" + consumption + "% " + StatCollector.translateToLocal("tc.vis.cost") + ")" + text);
//                    } else {
//                        if (tt.length() > 0) {
//                            tt.append(" | ");
//                        }
//
//                        tt.append("§").append(aspect.getChatcolor()).append(amount).append("§r");
//                    }
//                }
//            }
//
//            if (!Thaumcraft.proxy.isShiftKeyDown() && num > 0) {
//                list.add(tt.toString());
//                tot /= num;
//                tt2 = " (" + tot + "% " + StatCollector.translateToLocal("tc.vis.costavg") + ")";
//            }
//        }
//
//        list.add(pos, EnumChatFormatting.GOLD + StatCollector.translateToLocal("item.capacity.text") + " " + this.getMaxVis(stack) / 100 + "§r" + tt2);
//        if (this.getFocus(stack) != null) {
//            list.add(EnumChatFormatting.BOLD + "" + EnumChatFormatting.ITALIC + EnumChatFormatting.GREEN + this.getFocus(stack).getItemStackDisplayName(this.getFocusItem(stack)));
//            if (Thaumcraft.proxy.isShiftKeyDown()) {
//                this.getFocus(stack).addFocusInformation(this.getFocusItem(stack), player, list, par4);
//            }
//        }
//
//    }

//    public AspectList<Aspect>getAllVis(ItemStack is) {
//        AspectList<Aspect>out = new AspectList<>();
//
//        for (Aspect aspect : Aspect.getPrimalAspects()) {
//            if (is.hasTagCompound() && is.stackTagCompound.hasKey(aspect.getTag())) {
//                out.merge(aspect, is.stackTagCompound.getInteger(aspect.getTag()));
//            } else {
//                out.merge(aspect, 0);
//            }
//        }
//
//        return out;
//    }

//    public AspectList<Aspect>getAspectsWithRoomRemaining(ItemStack wandstack) {
//        AspectList<Aspect>out = new AspectList<>();
//        AspectList<Aspect>cur = this.getAllVis(wandstack);
//
//        for (Aspect aspect : cur.getAspects()) {
//            if (cur.getAmount(aspect) < this.getMaxVis(wandstack)) {
//                out.add(aspect, 1);
//            }
//        }
//
//        return out;
//    }

//    public void storeAllVis(ItemStack is, AspectList<Aspect>in) {
//        for (Aspect aspect : in.getAspects()) {
//            is.setTagInfo(aspect.getTag(), new NBTTagInt(in.getAmount(aspect)));
//        }
//
//    }

//    public int getVis(ItemStack is, Aspect aspect) {
//        int out = 0;
//        if (is != null && aspect != null && is.hasTagCompound() && is.stackTagCompound.hasKey(aspect.getTag())) {
//            out = is.stackTagCompound.getInteger(aspect.getTag());
//        }
//
//        return out;
//    }
//
//    public void storeVis(ItemStack is, Aspect aspect, int amount) {
//        is.setTagInfo(aspect.getTag(), new NBTTagInt(amount));
//    }

//    public float getConsumptionModifier(ItemStack is, Player player, Aspect aspect, boolean crafting) {
//        return ConsumptionModifierCalculator.getConsumptionModifier(this, is, player, aspect, crafting);
////        float consumptionModifier = 1.0F;
////        if (this.getCap(is).getSpecialCostModifierAspects() != null
////                && this.getCap(is).getSpecialCostModifierAspects().contains(aspect)
////        ) {
////            consumptionModifier = this.getCap(is).getSpecialCostModifier();
////        } else {
////            consumptionModifier = this.getCap(is).getBaseCostModifier();
////        }
////
////        if (player != null) {
////            consumptionModifier -= WandManager.getTotalVisDiscount(player, aspect);
////        }
////
////        if (this.getFocus(is) != null && !crafting) {
////            consumptionModifier -= (float) this.getFocusFrugal(is) / 10.0F;
////        }
////
////        if (this.isSceptre(is)) {
////            consumptionModifier -= 0.1F;
////        }
////
////        return Math.max(consumptionModifier, 0.1F);
//    }

//    public int getFocusPotency(ItemStack itemstack) {
//        return this.getFocus(itemstack) == null ? 0 : this.getFocus(itemstack).getUpgradeLevel(this.getFocusItem(itemstack), FocusUpgradeType.potency) + (this.hasRunes(itemstack) ? 1 : 0);
//    }
//
//    public int getFocusTreasure(ItemStack itemstack) {
//        return this.getFocus(itemstack) == null ? 0 : this.getFocus(itemstack).getUpgradeLevel(this.getFocusItem(itemstack), FocusUpgradeType.treasure);
//    }
//
//    public int getFocusFrugal(ItemStack itemstack) {
//        return this.getFocus(itemstack) == null ? 0 : this.getFocus(itemstack).getUpgradeLevel(this.getFocusItem(itemstack), FocusUpgradeType.frugal);
//    }
//
//    public int getFocusEnlarge(ItemStack itemstack) {
//        return this.getFocus(itemstack) == null ? 0 : this.getFocus(itemstack).getUpgradeLevel(this.getFocusItem(itemstack), FocusUpgradeType.enlarge);
//    }
//
//    public int getFocusExtend(ItemStack itemstack) {
//        return this.getFocus(itemstack) == null ? 0 : this.getFocus(itemstack).getUpgradeLevel(this.getFocusItem(itemstack), FocusUpgradeType.extend);
//    }

//    public boolean consumeCentiVisNoThrow(ItemStack is, Player player, Aspect aspect, int amount, boolean crafting) {
//        amount = (int) ((float) amount * this.getConsumptionModifier(is, player, aspect, crafting));
//        if (this.getVis(is, aspect) >= amount) {
//            this.storeVis(is, aspect, this.getVis(is, aspect) - amount);
//            return true;
//        } else {
//            return false;
//        }
//    }

//    public boolean consumeAllCentiVisCrafting(ItemStack is, Player player, AspectList<Aspect>aspects, boolean doit) {
//        if (aspects != null && aspects.size() != 0) {
//            AspectList<Aspect>nl = new AspectList<>();
//
//            for (Aspect aspect : aspects.getAspects()) {
//                int cost = aspects.getAmount(aspect) * 100;
//                nl.add(aspect, cost);
//            }
//
//            return this.consumeAllCentiVis(is, player, nl, doit, true);
//        } else {
//            return false;
//        }
//    }
//
//    public boolean consumeAllCentiVis(ItemStack is, Player player, AspectList<Aspect>aspects, boolean doit, boolean crafting) {
//        if (aspects != null && aspects.size() != 0) {
//            AspectList<Aspect>nl = new AspectList<>();
//
//            for (Aspect aspect : aspects.getAspects()) {
//                int cost = aspects.getAmount(aspect);
//                cost = (int) ((float) cost * this.getConsumptionModifier(is, player, aspect, crafting));
//                nl.add(aspect, cost);
//            }
//
//            for (Aspect aspect : nl.getAspects()) {
//                if (this.getVis(is, aspect) < nl.getAmount(aspect)) {
//                    return false;
//                }
//            }
//
//            if (doit && Platform.getEnvironment() != Env.CLIENT) {
//                for (Aspect aspect : nl.getAspects()) {
//                    this.storeVis(is, aspect, this.getVis(is, aspect) - nl.getAmount(aspect));
//                }
//            }
//
//            return true;
//        } else {
//            return false;
//        }
//    }

//    public int addVis(ItemStack is, Aspect aspect, int amount, boolean doit) {
//        if (!aspect.isPrimal()) {
//            return 0;
//        } else {
//            int storeAmount = this.getVis(is, aspect) + amount * 100;
//            int leftover = Math.max(storeAmount - this.getMaxVis(is), 0);
//            if (doit) {
//                this.storeVis(is, aspect, Math.min(storeAmount, this.getMaxVis(is)));
//            }
//
//            return leftover / 100;
//        }
//    }

//    public int addRealVis(ItemStack is, Aspect aspect, int amount, boolean doit) {
//        if (!aspect.isPrimal()) {
//            return 0;
//        } else {
//            int storeAmount = this.getVis(is, aspect) + amount;
//            int leftover = Math.max(storeAmount - this.getMaxVis(is), 0);
//            if (doit) {
//                this.storeVis(is, aspect, Math.min(storeAmount, this.getMaxVis(is)));
//            }
//
//            return leftover;
//        }
//    }

//    public void onUpdate(ItemStack is, World w, Entity e, int slot, boolean currentItem) {
//        if (Platform.getEnvironment() != Env.CLIENT) {
//            Player player = (Player) e;
//            if (this.getRod(is).getOnUpdate() != null) {
//                this.getRod(is).getOnUpdate().onUpdate(is, player);
//            }
//        }
//
//    }

    public boolean onItemUseFirst(ItemStack itemstack, Player player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        Block bi = world.getBlock(x, y, z);
        int md = world.getBlockMetadata(x, y, z);
        boolean result = false;
        Direction direction = Direction.getOrientation(side);
        if (bi instanceof IWandable) {
            int ret = ((IWandable) bi).onWandRightClick(world, itemstack, player, x, y, z, side, md);
            if (ret >= 0) {
                return ret == 1;
            }
        }

        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof IWandable) {
            int ret = ((IWandable) tile).onWandRightClick(world, itemstack, player, x, y, z, side, md);
            if (ret >= 0) {
                return ret == 1;
            }
        }

        if (WandTriggerRegistry.hasTrigger(bi)) {
            return WandTriggerRegistry.performTrigger(world, itemstack, player, x, y, z, side, bi, md);
        } else {
            //TODO:Migrate this part to block
            if ((bi == ConfigBlocks.blockWoodenDevice && md == 2 
                    || bi == ConfigBlocks.blockCosmeticOpaque && md == 2) 
                    && (!Config.wardedStone || tile instanceof TileOwned && player.getGameProfile().getName().equals(((TileOwned) tile).owner))
            ) {
                if (Platform.getEnvironment() != Env.CLIENT) {
                    ((TileOwned) tile).safeToRemove = true;
                    world.spawnEntityInWorld(new EntityItem(world, (double) x + (double) 0.5F, (double) y + (double) 0.5F, (double) z + (double) 0.5F, new ItemStack(bi, 1, md)));
                    world.playAuxSFX(2001, x, y, z, Block.getIdFromBlock(bi) + (md << 12));
                    world.setBlockToAir(x, y, z);
                } else {
                    player.swingItem();
                }
            }

            if (bi == ConfigBlocks.blockArcaneDoor 
                    && (!Config.wardedStone || tile instanceof TileOwned && player.getCommandSenderName().equals(((TileOwned) tile).owner))) {
                if (Platform.getEnvironment() != Env.CLIENT) {
                    ((TileOwned) tile).safeToRemove = true;
                    if ((md & 8) == 0) {
                        tile = world.getTileEntity(x, y + 1, z);
                    } else {
                        tile = world.getTileEntity(x, y - 1, z);
                    }

                    if (tile instanceof TileOwned) {
                        ((TileOwned) tile).safeToRemove = true;
                    }

                    if (Config.wardedStone || !Config.wardedStone && (md & 8) == 0) {
                        world.spawnEntityInWorld(new EntityItem(world, (double) x + (double) 0.5F, (double) y + (double) 0.5F, (double) z + (double) 0.5F, new ItemStack(ConfigItems.itemArcaneDoor)));
                    }

                    world.playAuxSFX(2001, x, y, z, Block.getIdFromBlock(bi) + (md << 12));
                    world.setBlockToAir(x, y, z);
                } else {
                    player.swingItem();
                }
            }

            return result;
        }
    }

//    public ItemFocusBasic getFocus(ItemStack stack) {
//        if (stack == null) {
//            return null;
//        }
//        if (stack.hasTagCompound() && stack.stackTagCompound.hasKey("focus")) {
//            NBTTagCompound nbt = stack.stackTagCompound.getCompoundTag("focus");
//            return (ItemFocusBasic) ItemStack.loadItemStackFromNBT(nbt).getItem();
//        } else {
//            return null;
//        }
//    }
//
//    public ItemStack getFocusItem(ItemStack stack) {
//        if (stack == null) {
//            return null;
//        }
//        if (stack.hasTagCompound() && stack.stackTagCompound.hasKey("focus")) {
//            NBTTagCompound nbt = stack.stackTagCompound.getCompoundTag("focus");
//            return ItemStack.loadItemStackFromNBT(nbt);
//        } else {
//            return null;
//        }
//    }
//
//    public void setFocus(ItemStack stack, ItemStack focus) {
//        if (focus == null) {
//            stack.stackTagCompound.removeTag("focus");
//        } else {
//            stack.setTagInfo("focus", focus.writeToNBT(new NBTTagCompound()));
//        }
//
//    }
//
//    public WandRod getRod(ItemStack stack) {
//        return stack.hasTagCompound() && stack.stackTagCompound.hasKey("rod") ? WandRod.rods.get(stack.stackTagCompound.getString("rod")) : ConfigItems.WAND_ROD_WOOD;
//    }
//
//    public boolean isStaff(ItemStack stack) {
//        WandRod rod = this.getRod(stack);
//        return rod instanceof StaffRod;
//    }
//
//    public boolean isSceptre(ItemStack stack) {
//        return stack.hasTagCompound() && stack.stackTagCompound.hasKey("sceptre");
//    }

//    public boolean hasRunes(ItemStack stack) {
//        WandRod rod = this.getRod(stack);
//        return rod instanceof StaffRod && ((StaffRod) rod).hasRunes();
//    }

//    public void setRod(ItemStack stack, WandRod rod) {
//        stack.setTagInfo("rod", new NBTTagString(rod.getTag()));
//        if (rod instanceof StaffRod) {
//            NBTTagList tags = new NBTTagList();
//            NBTTagCompound tag = new NBTTagCompound();
//            tag.setString("AttributeName", SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName());
//            AttributeModifier am = new AttributeModifier(field_111210_e, "Weapon modifier", 6.0F, 0);
//            tag.setString("Name", am.getName());
//            tag.setDouble("Amount", am.getAmount());
//            tag.setInteger("Operation", am.getOperation());
//            tag.setLong("UUIDMost", am.getID().getMostSignificantBits());
//            tag.setLong("UUIDLeast", am.getID().getLeastSignificantBits());
//            tags.appendTag(tag);
//            stack.stackTagCompound.setTag("AttributeModifiers", tags);
//        }
//
//    }

//    public WandCap getCap(ItemStack stack) {
//        return stack.hasTagCompound() && stack.stackTagCompound.hasKey("cap") ? WandCap.caps.get(stack.stackTagCompound.getString("cap")) : ConfigItems.WAND_CAP_IRON;
//    }
//
//    public void setCap(ItemStack stack, WandCap cap) {
//        stack.setTagInfo("cap", new NBTTagString(cap.getTag()));
//    }

//    public ItemStack onItemRightClick(ItemStack itemstack, World world, Player player) {
//        HitResult HitResult = this.getHitResultFromPlayer(world, player, true);
//        if (HitResult != null && HitResult.typeOfHit == MovingObjectType.BLOCK) {
//            int i = HitResult.blockX;
//            int j = HitResult.blockY;
//            int k = HitResult.blockZ;
//            Block bi = world.getBlock(i, j, k);
//            world.getBlockMetadata(i, j, k);
//            if (bi instanceof IWandable) {
//                ItemStack is = ((IWandable) bi).onWandRightClick(world, itemstack, player);
//                if (is != null) {
//                    return is;
//                }
//            }
//
//            TileEntity tile = world.getTileEntity(i, j, k);
//            if (tile instanceof IWandable) {
//                ItemStack is = ((IWandable) tile).onWandRightClick(world, itemstack, player);
//                if (is != null) {
//                    return is;
//                }
//            }
//        }
//
//        ItemFocusBasic focus = this.getFocus(itemstack);
//        if (focus != null && !WandManager.isOnCooldown(player)) {
//            WandManager.setCooldown(player, focus.getActivationCooldown(this.getFocusItem(itemstack)));
//            ItemStack ret = focus.onFocusRightClick(itemstack, world, player, HitResult);
//            if (ret != null) {
//                return ret;
//            }
//        }
//
//        return super.onItemRightClick(itemstack, world, player);
//    }

//    public void setObjectInUse(ItemStack stack, int x, int y, int z) {
//        if (stack.stackTagCompound == null) {
//            stack.stackTagCompound = new NBTTagCompound();
//        }
//
//        stack.stackTagCompound.setInteger("IIUX", x);
//        stack.stackTagCompound.setInteger("IIUY", y);
//        stack.stackTagCompound.setInteger("IIUZ", z);
//    }
//
//    public void clearObjectInUse(ItemStack stack) {
//        if (stack.stackTagCompound == null) {
//            stack.stackTagCompound = new NBTTagCompound();
//        }
//
//        stack.stackTagCompound.removeTag("IIUX");
//        stack.stackTagCompound.removeTag("IIUY");
//        stack.stackTagCompound.removeTag("IIUZ");
//    }
//
//    public IWandable getObjectInUse(ItemStack stack, World world) {
//        if (stack.hasTagCompound() && stack.stackTagCompound.hasKey("IIUX")) {
//            TileEntity te = world.getTileEntity(stack.stackTagCompound.getInteger("IIUX"), stack.stackTagCompound.getInteger("IIUY"), stack.stackTagCompound.getInteger("IIUZ"));
//            if (te instanceof IWandable) {
//                return (IWandable) te;
//            }
//        }
//
//        return null;
//    }

//    public void onUsingTick(ItemStack stack, Player player, int count) {
//        IWandable tv = this.getObjectInUse(stack, player.level());
//        if (tv != null) {
//            this.animation = ItemFocusBasic.WandFocusAnimation.WAVE;
//            tv.onUsingWandTick(stack, player, count);
//        } else {
//            ItemFocusBasic focus = this.getFocus(stack);
//            if (focus != null && !WandManager.isOnCooldown(player)) {
//                WandManager.setCooldown(player, focus.getActivationCooldown(this.getFocusItem(stack)));
//                focus.onUsingFocusTick(stack, player, count);
//            }
//        }
//
//    }
//
//    public void onPlayerStoppedUsing(ItemStack stack, World world, Player player, int count) {
//        IWandable tv = this.getObjectInUse(stack, player.level());
//        if (tv != null) {
//            tv.onWandStoppedUsing(stack, world, player, count);
//            this.animation = null;
//        } else {
//            ItemFocusBasic focus = this.getFocus(stack);
//            if (focus != null) {
//                focus.onPlayerStoppedUsingFocus(stack, world, player, count);
//            }
//        }
//
//        this.clearObjectInUse(stack);
//    }
//
//    public EnumAction getItemUseAction(ItemStack par1ItemStack) {
//        return EnumAction.bow;
//    }
//
//    public int getMaxItemUseDuration(ItemStack itemstack) {
//        return Integer.MAX_VALUE;
//    }
//
//    public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack stack) {
//        ItemStack focus = this.getFocusItem(stack);
//        if (focus != null && !WandManager.isOnCooldown(entityLiving)) {
//            WandManager.setCooldown(entityLiving, this.getFocus(stack).getActivationCooldown(focus));
//            return focus.getItem().onEntitySwing(entityLiving, stack);
//        } else {
//            return super.onEntitySwing(entityLiving, stack);
//        }
//    }
//
//    public boolean onBlockStartBreak(ItemStack itemstack, int x, int y, int z, Player player) {
//        ItemFocusBasic focus = this.getFocus(itemstack);
//        if (focus != null && !WandManager.isOnCooldown(player)) {
//            WandManager.setCooldown(player, focus.getActivationCooldown(this.getFocusItem(itemstack)));
//            return focus.onFocusBlockStartBreak(itemstack, x, y, z, player);
//        } else {
//            return false;
//        }
//    }
//
//    public boolean canHarvestBlock(Block par1Block, ItemStack itemstack) {
//        ItemFocusBasic focus = this.getFocus(itemstack);
//        return focus != null && this.getFocusItem(itemstack).getItem().canHarvestBlock(par1Block, itemstack);
//    }
//
//    public float func_150893_a(ItemStack itemstack, Block block) {
//        ItemFocusBasic focus = this.getFocus(itemstack);
//        return focus != null ? this.getFocusItem(itemstack).getItem().func_150893_a(itemstack, null) : super.func_150893_a(itemstack, block);
//    }

//    public ArrayList getArchitectBlocks(ItemStack stack, World world, int x, int y, int z, int side, Player player) {
//        ItemFocusBasic focus = this.getFocus(stack);
//        return focus instanceof IArchitect && focus.isUpgradedWith(this.getFocusItem(stack), FocusUpgradeType.architect) ? ((IArchitect) focus).getArchitectBlocks(stack, world, x, y, z, side, player) : null;
//    }
//
//    public boolean showAxis(ItemStack stack, World world, Player player, int side, EnumAxis axis) {
//        ItemFocusBasic focus = this.getFocus(stack);
//        return focus instanceof IArchitect && focus.isUpgradedWith(this.getFocusItem(stack), FocusUpgradeType.architect) && ((IArchitect) focus).showAxis(stack, world, player, side, axis);
//    }
}
