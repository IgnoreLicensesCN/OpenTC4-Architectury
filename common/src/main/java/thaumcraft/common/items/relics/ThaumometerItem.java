package thaumcraft.common.items.relics;

import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.nodes.INodeBlockEntity;
import thaumcraft.api.research.IScanEventHandler;
import thaumcraft.api.research.ScanResult;
import thaumcraft.common.lib.network.playerdata.PacketScannedToServerC2S;
import thaumcraft.common.lib.research.ScanManager;
import thaumcraft.common.lib.utils.EntityUtils;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import static thaumcraft.common.ThaumcraftSounds.CAMERA_TICKS;

public class ThaumometerItem extends Item {
   ScanResult startScan = null;

   public ThaumometerItem() {
      super(new Properties().stacksTo(1));
   }

//   public EnumRarity getRarity(ItemStack itemstack) {
//      return EnumRarity.uncommon;
//   }
//
//   public IIcon icon;
//   @SideOnly(Side.CLIENT)
//   public void registerIcons(IIconRegister ir) {
//      this.icon = ir.registerIcon("thaumcraft:blank");
//   }

//   @SideOnly(Side.CLIENT)
//   public IIcon getIconFromDamage(int par1) {
//      return this.icon;
//   }


//   public EnumAction getItemUseAction(ItemStack itemstack) {
//      return EnumAction.none;
//   }

   private ScanResult doScan(ItemStack stack, Level world, Player p, int count) {
      Entity pointedEntity = EntityUtils.getPointedEntity(p, 0.5F, 10.0F, 0.0F, true);
      if (pointedEntity != null) {
         ScanResult sr = new ScanResult((byte)2, (Item) null, pointedEntity, "");
         if (ScanManager.isValidScanTarget(p, sr, "@")) {
            ClientFXUtils.blockRunes(world,
                    pointedEntity.getX() - (double)0.5F,
                    pointedEntity.getY() + (double)(pointedEntity.getEyeHeight() / 2.0F),
                    pointedEntity.getZ() - (double)0.5F, 0.3F + world.getRandom().nextFloat() * 0.7F,
                    0.0F,
                    0.3F + world.getRandom().nextFloat() * 0.7F,
                    (int)(pointedEntity.getBoundingBox().maxY - pointedEntity.getBoundingBox().minY * 15.0F),
                    0.03F);
            return sr;
         } else {
            return null;
         }
      } else {
         HitResult mop = EntityUtils.getHitResultFromPlayer(p.level(), p, true);
         if (mop instanceof BlockHitResult blockHitResult && mop.getType() != HitResult.Type.MISS) {
            var pos = blockHitResult.getBlockPos();
            BlockEntity tile = world.getBlockEntity(pos);
            if (tile instanceof INodeBlockEntity node) {
               ScanResult sr = new ScanResult((byte)3, "", null, "NODE" + node.getId());
               if (ScanManager.isValidScanTarget(p, sr, "@")) {

                  ClientFXUtils.blockRunes(
                          world,
                          pos.getX(), pos.getY() + 0.25, pos.getZ(),
                          0.3F + world.getRandom().nextFloat() * 0.7F,
                          0.0F,
                          0.3F + world.getRandom().nextFloat() * 0.7F,
                          15,
                          0.03F);
                  return sr;
               }

               return null;
            }

            BlockState bi = world.getBlockState(pos);
            if (!bi.isAir()) {
               Item item = bi.getBlock().asItem();
               ItemStack is = bi.getBlock().asItem().getDefaultInstance();//bi.getPickBlock(mop, p.level(), mop.blockX, mop.blockY, mop.blockZ);
               ScanResult sr = null;
               sr = new ScanResult((byte)1, item, null, "");
               if (ScanManager.isValidScanTarget(p, sr, "@")) {
                  ClientFXUtils.blockRunes(world,
                          pos.getX(),
                          pos.getY() + 0.25,
                          pos.getZ(),
                          0.3F + world.getRandom().nextFloat() * 0.7F,
                          0.0F,
                          0.3F + world.getRandom().nextFloat() * 0.7F,
                          15, 0.03F);
                  return sr;
               }

               return null;
            }
         }

         for(IScanEventHandler seh : ThaumcraftApi.scanEventhandlers) {
            ScanResult scan = seh.scanPhenomena(stack, world, p);
            if (scan != null) {
               return scan;
            }
         }

         return null;
      }
   }


   @Override
   public @NotNull InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
      ItemStack stack = player.getItemInHand(hand);
      onItemRightClick(stack, world, player);
      player.startUsingItem(hand); // 开始持续使用
      return InteractionResultHolder.consume(stack);
   }
   public ItemStack onItemRightClick(ItemStack stack, Level world, Player p) {//TODO:migrate
      if (world.isClientSide()) {
         ScanResult scan = this.doScan(stack, world, p, 0);
         if (scan != null) {
            this.startScan = scan;
         }
      }

//      p.setItemInUse(stack, 25);
      return stack;
   }

   @Override
   public void onUseTick(Level level, LivingEntity livingEntity, ItemStack itemStack, int useRemainingCount) {
      if (livingEntity instanceof Player player) {
         onUsingTick(itemStack,player,useRemainingCount);
      }
   }
   private final AtomicInteger counter = new AtomicInteger(0);
   public void onUsingTick(ItemStack stack, Player p, int useRemainingCount) {//TODO:migrate
      var count = counter.decrementAndGet();
      var localPlayer = Minecraft.getInstance().player;
      if (p.level().isClientSide() && localPlayer != null && Objects.equals(localPlayer.getName().getString(),p.getName().getString())) {
         ScanResult scan = this.doScan(stack, p.level(), p, count);
         if (scan != null && scan.equals(this.startScan)) {
            if (count <= 5) {
               this.startScan = null;

               counter.set(25);
//               p.stopUsingItem();
               if (ScanManager.completeScan(p, scan, "@")) {
                  new PacketScannedToServerC2S(scan, p, "@").sendToServer();
               }
            }

            if (count % 2 == 0) {
               p.level().playSound(p,p.getOnPos(), CAMERA_TICKS, SoundSource.PLAYERS, 0.2F, 0.45F + p.level().getRandom().nextFloat() * 0.1F);
            }
         } else {
            this.startScan = null;
         }
      }

   }

   @Override
   public void releaseUsing(ItemStack itemStack, Level level, LivingEntity livingEntity, int i) {
      super.releaseUsing(itemStack, level, livingEntity, i);
      this.startScan = null;
   }

   @Override
   public int getUseDuration(ItemStack stack) {
      return Integer.MAX_VALUE;
   }
}
