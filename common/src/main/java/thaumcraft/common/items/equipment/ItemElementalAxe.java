package thaumcraft.common.items.equipment;

import com.google.common.collect.ImmutableSet;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.world.level.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.world.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.util.IIcon;
import com.linearity.opentc4.utils.vanilla1710.MathHelper;
import thaumcraft.api.IRepairEnchantable;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.entities.EntityFollowingItem;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.fx.PacketFXBlockBubbleS2C;
import thaumcraft.common.lib.utils.BlockUtils;
import thaumcraft.common.lib.utils.EntityUtils;
import thaumcraft.common.lib.utils.Utils;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ItemElementalAxe extends ItemAxe implements IRepairEnchantable {
    public IIcon icon;
    boolean alternateServer = false;
    boolean alternateClient = false;
    public static final List<List<Integer>> oreDictLogs = new ArrayList<>();

    public ItemElementalAxe(Item.ToolMaterial enumtoolmaterial) {
        super(enumtoolmaterial);
        this.setCreativeTab(Thaumcraft.tabTC);
    }

    @Override
    public Set<String> getToolClasses(ItemStack stack) {
        return ImmutableSet.of("axe");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister ir) {
        this.icon = ir.registerIcon("thaumcraft:elementalaxe");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int par1) {
        return this.icon;
    }

    @Override
    public EnumRarity getRarity(ItemStack itemstack) {
        return EnumRarity.rare;
    }

    @Override
    public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack) {
        return par2ItemStack.isItemEqual(new ItemStack(ThaumcraftItems.THAUMIUM_INGOT)) || super.getIsRepairable(par1ItemStack, par2ItemStack);
    }

    @Override
    public EnumAction getItemUseAction(ItemStack itemstack) {
        return EnumAction.bow;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack p_77626_1_) {
        return 72000;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack p_77659_1_, World p_77659_2_, Player p_77659_3_) {
        p_77659_3_.setItemInUse(p_77659_1_, this.getMaxItemUseDuration(p_77659_1_));
        return p_77659_1_;
    }

    @Override
    public void onUsingTick(ItemStack stack, Player player, int count) {
        ArrayList<Entity> stuff = EntityUtils.getEntitiesInRange(player.level(), player.posX, player.posY, player.posZ, player, EntityItem.class, 10.0F);
        if (stuff != null && !stuff.isEmpty()) {
            for (Entity e : stuff) {
                if ((!(e instanceof EntityFollowingItem) || ((EntityFollowingItem) e).target == null) && !e.isDead && e instanceof EntityItem) {
                    double d6 = e.posX - player.posX;
                    double d8 = e.posY - player.posY + (double) (player.height / 2.0F);
                    double d10 = e.posZ - player.posZ;
                    double d11 = MathHelper.sqrt_double(d6 * d6 + d8 * d8 + d10 * d10);
                    d6 /= d11;
                    d8 /= d11;
                    d10 /= d11;
                    double d13 = 0.3;
                    e.motionX -= d6 * d13;
                    e.motionY -= d8 * d13;
                    e.motionZ -= d10 * d13;
                    if (e.motionX > 0.35) {
                        e.motionX = 0.35;
                    }

                    if (e.motionX < -0.35) {
                        e.motionX = -0.35;
                    }

                    if (e.motionY > 0.35) {
                        e.motionY = 0.35;
                    }

                    if (e.motionY < -0.35) {
                        e.motionY = -0.35;
                    }

                    if (e.motionZ > 0.35) {
                        e.motionZ = 0.35;
                    }

                    if (e.motionZ < -0.35) {
                        e.motionZ = -0.35;
                    }

                    Thaumcraft.proxy.crucibleBubble(player.level(), (float) e.posX + (player.level().rand.nextFloat() - player.level().rand.nextFloat()) * 0.125F, (float) e.posY + (player.level().rand.nextFloat() - player.level().rand.nextFloat()) * 0.125F, (float) e.posZ + (player.level().rand.nextFloat() - player.level().rand.nextFloat()) * 0.125F, 0.33F, 0.33F, 1.0F);
                }
            }
        }

    }

    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, int x, int y, int z, Player player) {
        World world = player.level();
        Block bi = world.getBlock(x, y, z);
        if (!player.isSneaking() && Utils.isWoodLog(world, x, y, z)) {
            if (Platform.getEnvironment() != Env.CLIENT) {
                BlockUtils.breakFurthestBlock(world, x, y, z, bi, player, true, 10);
                PacketHandler.INSTANCE.sendToAllAround(new PacketFXBlockBubbleS2C(x, y, z, (new Color(0.33F, 0.33F, 1.0F)).getRGB()), new NetworkRegistry.TargetPoint(world.dimension(), x, y, z, 32.0F));
                world.playSoundEffect(x, y, z, "thaumcraft:bubble", 0.15F, 1.0F);
            }

            itemstack.damageItem(1, player);
            return true;
        } else {
            return super.onBlockStartBreak(itemstack, x, y, z, player);
        }
    }
}
