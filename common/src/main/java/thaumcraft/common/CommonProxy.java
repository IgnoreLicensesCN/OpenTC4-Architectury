package thaumcraft.common;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.network.IGuiHandler;

import java.util.ArrayList;
import java.util.Map;

import cpw.mods.fml.relauncher.Side;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.BehaviorProjectileDispense;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import tc4tweak.ConfigurationHandler;
import tc4tweak.modules.FlushableCache;
import tc4tweak.network.MessageSendConfiguration;
import tc4tweak.network.MessageSendConfigurationV2;
import tc4tweak.network.NetworkedConfiguration;
import tc4tweak.network.TileHoleSyncPacket;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.container.ContainerAlchemyFurnace;
import thaumcraft.common.container.ContainerArcaneBore;
import thaumcraft.common.container.ContainerArcaneWorkbench;
import thaumcraft.common.container.ContainerDeconstructionTable;
import thaumcraft.common.container.ContainerFocalManipulator;
import thaumcraft.common.container.ContainerFocusPouch;
import thaumcraft.common.container.ContainerHandMirror;
import thaumcraft.common.container.ContainerHoverHarness;
import thaumcraft.common.container.ContainerMagicBox;
import thaumcraft.common.container.ContainerResearchTable;
import thaumcraft.common.container.ContainerSpa;
import thaumcraft.common.container.ContainerThaumatorium;
import thaumcraft.common.entities.ContainerPech;
import thaumcraft.common.entities.golems.ContainerGolem;
import thaumcraft.common.entities.golems.ContainerTravelingTrunk;
import thaumcraft.common.entities.golems.EntityGolemBase;
import thaumcraft.common.entities.golems.EntityTravelingTrunk;
import thaumcraft.common.entities.monster.EntityPech;
import thaumcraft.common.entities.projectile.EntityPrimalArrow;
import thaumcraft.common.items.wands.WandManager;
import thaumcraft.common.lib.research.PlayerKnowledge;
import thaumcraft.common.lib.research.ResearchManager;
import thaumcraft.common.tiles.TileAlchemyFurnace;
import thaumcraft.common.tiles.TileArcaneBore;
import thaumcraft.common.tiles.TileArcaneWorkbench;
import thaumcraft.common.tiles.TileCrucible;
import thaumcraft.common.tiles.TileDeconstructionTable;
import thaumcraft.common.tiles.TileFocalManipulator;
import thaumcraft.common.tiles.TileMagicBox;
import thaumcraft.common.tiles.TileResearchTable;
import thaumcraft.common.tiles.TileSpa;
import thaumcraft.common.tiles.TileThaumatorium;

public class CommonProxy implements IGuiHandler {
   public PlayerKnowledge playerKnowledge;
   public ResearchManager researchManager;
   public WandManager wandManager = new WandManager();

   public PlayerKnowledge getPlayerKnowledge() {
      return this.playerKnowledge;
   }

   public ResearchManager getResearchManager() {
      return this.researchManager;
   }

   public Map<String, ArrayList<String>> getCompletedResearch() {
      return this.playerKnowledge.researchCompleted;
   }

   public Map<String,ArrayList<String>> getScannedObjects() {
      return this.playerKnowledge.objectsScanned;
   }

   public Map<String,ArrayList<String>> getScannedEntities() {
      return this.playerKnowledge.entitiesScanned;
   }

   public Map<String,ArrayList<String>> getScannedPhenomena() {
      return this.playerKnowledge.phenomenaScanned;
   }

   public Map<String, AspectList> getKnownAspects() {
      return this.playerKnowledge.aspectsDiscovered;
   }

   public void registerDisplayInformation() {
   }

   public void registerHandlers() {
   }

   public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
      return null;
   }

   public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
      switch (ID) {
         case 0:
            return new ContainerGolem(player.inventory, ((EntityGolemBase) world.getEntityByID(x)).inventory);
         case 1:
            return new ContainerPech(player.inventory, world, (EntityPech) world.getEntityByID(x));
         case 2:
            return new ContainerTravelingTrunk(player.inventory, world, (EntityTravelingTrunk) world.getEntityByID(x));
         case 3:
            return new ContainerThaumatorium(player.inventory, (TileThaumatorium)world.getTileEntity(x, y, z));
         case 5:
            return new ContainerFocusPouch(player.inventory, world, x, y, z);
         case 8:
            return new ContainerDeconstructionTable(player.inventory, (TileDeconstructionTable)world.getTileEntity(x, y, z));
         case 9:
            return new ContainerAlchemyFurnace(player.inventory, (TileAlchemyFurnace)world.getTileEntity(x, y, z));
         case 10:
            return new ContainerResearchTable(player.inventory, (TileResearchTable)world.getTileEntity(x, y, z));
         case 13:
            return new ContainerArcaneWorkbench(player.inventory, (TileArcaneWorkbench)world.getTileEntity(x, y, z));
         case 15:
            return new ContainerArcaneBore(player.inventory, (TileArcaneBore)world.getTileEntity(x, y, z));
         case 16:
            return new ContainerHandMirror(player.inventory, world, x, y, z);
         case 17:
            return new ContainerHoverHarness(player.inventory, world, x, y, z);
         case 18:
            return new ContainerMagicBox(player.inventory, (TileMagicBox)world.getTileEntity(x, y, z));
         case 19:
            return new ContainerSpa(player.inventory, (TileSpa)world.getTileEntity(x, y, z));
         case 20:
            return new ContainerFocalManipulator(player.inventory, (TileFocalManipulator)world.getTileEntity(x, y, z));
         default:
            return null;
      }
   }

   public World getClientWorld() {
      return null;
   }

   public void blockSparkle(World world, int x, int y, int z, int i, int count) {
   }

   public void sparkle(float x, float y, float z, float size, int color, float gravity) {
   }

   public void sparkle(float x, float y, float z, int color) {
   }

   public void crucibleBoil(World world, int xCoord, int yCoord, int zCoord, TileCrucible tile, int j) {
   }

   public void crucibleBoilSound(World world, int xCoord, int yCoord, int zCoord) {
   }

   public void crucibleBubble(World world, float x, float y, float z, float cr, float cg, float cb) {
   }

   public int particleCount(int base) {
      return 0;
   }

   public void wispFX(World worldObj, double posX, double posY, double posZ, float f, float g, float h, float i) {
   }

   public void sourceStreamFX(World worldObj, double sx, double sy, double sz, float tx, float ty, float tz, int tag) {
   }

   public void bolt(World worldObj, Entity sourceEntity, Entity targetedEntity) {
   }

   public void furnaceLavaFx(World worldObj, int x, int y, int z, int facingX, int facingZ) {
   }

   public void wispFX2(World worldObj, double posX, double posY, double posZ, float size, int type, boolean shrink, boolean clip, float gravity) {
   }

   public void crucibleFroth(World world, float x, float y, float z) {
   }

   public void crucibleFrothDown(World world, float x, float y, float z) {
   }

   public Object beamCont(World worldObj, EntityPlayer pm, double tx, double ty, double tz, int type, int color, boolean reverse, float endmod, Object input, int impact) {
      return null;
   }

   public void excavateFX(int x, int y, int z, EntityPlayer p, int bi, int md, int progress) {
   }

   public void burst(World worldObj, double sx, double sy, double sz, float size) {
   }

   public void wispFX3(World worldObj, double posX, double posY, double posZ, double posX2, double posY2, double posZ2, float size, int type, boolean shrink, float gravity) {
   }

   public void smokeSpiral(World m, double x, double y, double z, float rad, int start, int miny, int color) {
   }

   public void beam(World worldObj, double sx, double sy, double sz, double tx, double ty, double tz, int type, int color, boolean reverse, float endmod, int impact) {
   }

   public void blockRunes(World world, double x, double y, double z, float r, float g, float b, int dur, float grav) {
   }

   public Object beamBore(World worldObj, double px, double py, double pz, double tx, double ty, double tz, int type, int color, boolean reverse, float endmod, Object input, int impact) {
      return null;
   }

   public void boreDigFx(World worldObj, int x, int y, int z, int x2, int y2, int z2, Block bi, int md) {
   }

   public void soulTrail(World world, Entity source, Entity target, float r, float g, float b) {
   }

   public void nodeBolt(World worldObj, float x, float y, float z, Entity targetedEntity) {
   }

   public void splooshFX(Entity e) {
   }

   public void tentacleAriseFX(Entity e) {
   }

   public void slimeJumpFX(Entity e, int i) {
   }

   public void dropletFX(World world, float i, float j, float k, float r, float g, float b) {
   }

   public void taintLandFX(Entity e) {
   }

   public Object swarmParticleFX(World worldObj, Entity targetedEntity, float f1, float f2, float pg) {
      return null;
   }

   public void taintsplosionFX(Entity e) {
   }

   public IIcon getIcon(String string) {
      return null;
   }

   public void registerCustomIcons() {
   }

   public void hungryNodeFX(World worldObj, int tx, int ty, int tz, int xCoord, int yCoord, int zCoord, Block block, int md) {
   }

   public void essentiaTrailFx(World worldObj, int x, int y, int z, int x2, int y2, int z2, int count, int color, float scale) {
   }

   public void splooshFX(World worldObj, int x, int y, int z) {
   }

   public void nodeBolt(World worldObj, float x, float y, float z, float x2, float y2, float z2) {
   }

   public void drawInfusionParticles1(World worldObj, double x, double y, double z, int x2, int y2, int z2, Item bi, int md) {
   }

   public void drawInfusionParticles2(World worldObj, double x, double y, double z, int x2, int y2, int z2, Block bi, int md) {
   }

   public void drawInfusionParticles3(World worldObj, double x, double y, double z, int x2, int y2, int z2) {
   }

   public void drawInfusionParticles4(World worldObj, double x, double y, double z, int x2, int y2, int z2) {
   }

   public void drawVentParticles(World worldObj, double x, double y, double z, double x2, double y2, double z2, int color) {
   }

   public void blockWard(World world, double x, double y, double z, ForgeDirection side, float f, float f1, float f2) {
   }

   public void wispFX4(World worldObj, double posX, double posY, double posZ, Entity target, int type, boolean shrink, float gravity) {
   }

   public void registerKeyBindings() {
   }

   public Object beamPower(World worldObj, double px, double py, double pz, double tx, double ty, double tz, float r, float g, float b, boolean pulse, Object input) {
      return null;
   }

   public boolean isShiftKeyDown() {
      return false;
   }

   public void wispFXEG(World worldObj, double posX, double posY, double posZ, Entity target) {
   }

   public void reservoirBubble(World worldObj, int xCoord, int yCoord, int zCoord, int color) {
   }

   public void spark(float x, float y, float z, float size, float r, float g, float b, float a) {
   }

   public void drawVentParticles(World worldObj, double x, double y, double z, double x2, double y2, double z2, int color, float scale) {
   }

   public void bottleTaintBreak(World world, double x, double y, double z) {
   }

   public void drawGenericParticles(World worldObj, double x, double y, double z, double x2, double y2, double z2, float r, float g, float b, float alpha, boolean loop, int start, int num, int inc, int age, int delay, float scale) {
   }

   public void arcLightning(World world, double x, double y, double z, double tx, double ty, double tz, float r, float g, float b, float h) {
   }


   public CommonProxy() {
      FMLCommonHandler.instance().bus().register(this);
   }

   public void preInit(FMLPreInitializationEvent e) {
      ConfigurationHandler.INSTANCE.init(e.getSuggestedConfigurationFile());

//        if (Loader.isModLoaded("MineTweaker3")) {
//            MTCompat.preInit();
//        }

      Thaumcraft.instance.CHANNEL.registerMessage(MessageSendConfiguration.class, MessageSendConfiguration.class, 0, Side.CLIENT);
      Thaumcraft.instance.CHANNEL.registerMessage(MessageSendConfigurationV2.class, MessageSendConfigurationV2.class, 1, Side.CLIENT);
      Thaumcraft.instance.CHANNEL.registerMessage(TileHoleSyncPacket.class, TileHoleSyncPacket.class, 2, Side.CLIENT);
      int debugadd = Integer.getInteger("glease.debug.addtc4tabs.pre", 0);
      addDummyCategories(debugadd, "DUMMYPRE");
   }

   public void serverStarted(FMLServerStartedEvent e) {
      FlushableCache.enableAll(true);
      NetworkedConfiguration.resetServer();
   }

   public void init(FMLInitializationEvent e) {
      BlockDispenser.dispenseBehaviorRegistry.putObject(ConfigItems.itemPrimalArrow, new BehaviorProjectileDispense() {
         @Override
         public ItemStack dispenseStack(IBlockSource dispenser, ItemStack stack) {
            EnumFacing facing = BlockDispenser.func_149937_b(dispenser.getBlockMetadata());
            IPosition pos = BlockDispenser.func_149939_a(dispenser);
            ItemStack toDrop = stack.splitStack(1);
            if (ConfigurationHandler.INSTANCE.isDispenserShootPrimalArrow()) {
               World w = dispenser.getWorld();
               EntityPrimalArrow e = (EntityPrimalArrow) getProjectileEntity(w, pos);
               e.type = toDrop.getItemDamage();
               if (e.type == 3)
                  // inherent power of earth arrow
                  // this is unfortunately not done on hit, but at bow draw time, so we must emulate this as well
                  e.setKnockbackStrength(1);
               e.setThrowableHeading(facing.getFrontOffsetX(), facing.getFrontOffsetY() + 0.1F, facing.getFrontOffsetZ(), this.func_82500_b(), this.func_82498_a());
               w.spawnEntityInWorld(e);
            } else {
               doDispense(dispenser.getWorld(), toDrop, 6, facing, pos);
            }
            return stack;
         }

         @Override
         protected IProjectile getProjectileEntity(World w, IPosition iposition) {
            return new EntityPrimalArrow(w, iposition.getX(), iposition.getY(), iposition.getZ());
         }
      });
   }

   public void postInit(FMLPostInitializationEvent e) {
      int debugadd = Integer.getInteger("glease.debug.addtc4tabs.post", 0);
      for (int i = 0; i < debugadd; i++) {
         addDummyCategories(debugadd, "DUMMYPOST");
      }
   }

   private void addDummyCategories(int amount, String categoryPrefix) {
      for (int i = 0; i < amount; i++) {
         ResearchCategories.registerCategory(categoryPrefix + i, new ResourceLocation("thaumcraft", "textures/items/thaumonomiconcheat.png"), new ResourceLocation("thaumcraft", "textures/gui/gui_researchback.png"));
      }
   }

   @SubscribeEvent
   public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent e) {
      if (e.player instanceof EntityPlayerMP && !((EntityPlayerMP) e.player).playerNetServerHandler.netManager.isLocalChannel()) {
         // no point sending config over to a local client
         Thaumcraft.instance.CHANNEL.sendTo(new MessageSendConfiguration(), (EntityPlayerMP) e.player);
         Thaumcraft.instance.CHANNEL.sendTo(new MessageSendConfigurationV2(), (EntityPlayerMP) e.player);
      }
   }
}
