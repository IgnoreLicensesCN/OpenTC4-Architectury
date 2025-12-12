package thaumcraft.client;


import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import cpw.mods.fml.common.registry.VillagerRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.model.ModelChicken;
import net.minecraft.client.model.ModelCow;
import net.minecraft.client.model.ModelPig;
import net.minecraft.client.model.ModelSlime;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.particle.EntityDiggingFX;
import net.minecraft.client.particle.EntityLavaFX;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.init.Items;
import net.minecraft.world.item.Item;
import net.minecraft.util.EnumChatFormatting;
import com.linearity.opentc4.utils.vanilla1710.MathHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.world.WorldEvent;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.lwjgl.input.Mouse;
import tc4tweak.CommonUtils;
import tc4tweak.ConfigurationHandler;
import tc4tweak.modules.hudNotif.HUDNotification;
import tc4tweak.modules.particleEngine.ParticleEngineFix;
import tc4tweak.modules.researchBrowser.BrowserPaging;
import tc4tweak.modules.researchBrowser.DrawResearchCompletionCounter;
import tc4tweak.modules.researchBrowser.ThaumonomiconIndexSearcher;
import tc4tweak.network.NetworkedConfiguration;
import net.minecraft.client.Minecraft;
import thaumcraft.client.fx.beams.*;
import thaumcraft.client.fx.bolt.FXLightningBolt;
import thaumcraft.client.fx.other.FXBlockWard;
import thaumcraft.client.fx.particles.*;
import thaumcraft.client.gui.*;
import thaumcraft.client.lib.ClientTickEventsFML;
import thaumcraft.client.renderers.block.*;
import thaumcraft.client.renderers.entity.*;
import thaumcraft.client.renderers.item.*;
import thaumcraft.client.renderers.models.entities.*;
import thaumcraft.client.renderers.tile.*;
import thaumcraft.common.CommonProxy;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.config.ConfigEntities;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.entities.*;
import thaumcraft.common.entities.golems.EntityGolemBase;
import thaumcraft.common.entities.golems.EntityGolemBobber;
import thaumcraft.common.entities.golems.EntityTravelingTrunk;
import thaumcraft.common.entities.monster.*;
import thaumcraft.common.entities.monster.boss.*;
import thaumcraft.common.entities.projectile.*;
import thaumcraft.common.lib.events.KeyHandler;
import thaumcraft.common.tiles.*;

import java.awt.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

import static tc4tweak.ClientUtils.postponed;

public class ClientProxy extends CommonProxy {

//   protected PlayerKnowledge playerResearch = new PlayerKnowledge();
//   protected ResearchManager researchManager = new ResearchManager();
//   public WandManager wandManager = new WandManager();
//   private HashMap customIcons = new HashMap<>();

   public void registerHandlers() {
      FMLCommonHandler.instance().bus().register(new ClientTickEventsFML());
      MinecraftForge.EVENT_BUS.register(Thaumcraft.instance.renderEventHandler);
      MinecraftForge.EVENT_BUS.register(ConfigBlocks.blockTube);
      MinecraftForge.EVENT_BUS.register(ParticleEngine.instance);
      FMLCommonHandler.instance().bus().register(ParticleEngine.instance);
   }

   public void registerKeyBindings() {
      FMLCommonHandler.instance().bus().register(new KeyHandler());
   }

   public Object getClientGuiElement(int ID, Player player, World world, int x, int y, int z) {
      if (world instanceof WorldClient) {
         switch (ID) {
            case 0:
               return new GuiGolem(player, (EntityGolemBase) world.getEntityByID(x));
            case 1:
               return new GuiPech(player.inventory, world, (EntityPech) world.getEntityByID(x));
            case 2:
               return new GuiTravelingTrunk(player, (EntityTravelingTrunk) world.getEntityByID(x));
            case 3:
               return new GuiThaumatorium(player.inventory, (TileThaumatorium)world.getTileEntity(x, y, z));
            case 5:
               return new GuiFocusPouch(player.inventory, world, x, y, z);
            case 8:
               return new GuiDeconstructionTable(player.inventory, (TileDeconstructionTable)world.getTileEntity(x, y, z));
            case 9:
               return new GuiAlchemyFurnace(player.inventory, (TileAlchemyFurnace)world.getTileEntity(x, y, z));
            case 10:
               return new GuiResearchTable(player, (TileResearchTable)world.getTileEntity(x, y, z));
            case 12:
               return new GuiResearchBrowser();
            case 13:
               return new GuiArcaneWorkbench(player.inventory, (TileArcaneWorkbench)world.getTileEntity(x, y, z));
            case 15:
               return new GuiArcaneBore(player.inventory, (TileArcaneBore)world.getTileEntity(x, y, z));
            case 16:
               return new GuiHandMirror(player.inventory, world, x, y, z);
            case 17:
               return new GuiHoverHarness(player.inventory, world, x, y, z);
            case 18:
               return new GuiMagicBox(player.inventory, (TileMagicBox)world.getTileEntity(x, y, z));
            case 19:
               return new GuiSpa(player.inventory, (TileSpa)world.getTileEntity(x, y, z));
            case 20:
               return new GuiFocalManipulator(player.inventory, (TileFocalManipulator)world.getTileEntity(x, y, z));
            default:
               break;
         }
      }

      return null;
   }

   public void registerDisplayInformation() {
      Thaumcraft.instance.aspectShift = FMLClientHandler.instance().hasOptifine();
      if (Loader.isModLoaded("NotEnoughItems")) {
         Thaumcraft.instance.aspectShift = true;
      }

      this.setupItemRenderers();
      this.setupEntityRenderers();
      this.setupBlockRenderers();
      this.setupTileRenderers();
   }

   private void setupItemRenderers() {
      MinecraftForgeClient.registerItemRenderer(ConfigItems.itemJarFilled, new ItemJarFilledRenderer());
      MinecraftForgeClient.registerItemRenderer(ConfigItems.itemJarNode, new ItemJarNodeRenderer());
      MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ConfigBlocks.blockAiry), new ItemNodeRenderer());
//      MinecraftForgeClient.registerItemRenderer(ThaumcraftItems.THAUMOMETER, new ItemThaumometerRenderer());
      MinecraftForgeClient.registerItemRenderer(ConfigItems.WandCastingItem, new ItemWandRenderer());
      MinecraftForgeClient.registerItemRenderer(ConfigItems.itemTrunkSpawner, new ItemTrunkSpawnerRenderer());
      MinecraftForgeClient.registerItemRenderer(ConfigItems.itemBowBone, new ItemBowBoneRenderer());
      MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ConfigBlocks.blockWoodenDevice), new ItemBannerRenderer());
   }

   private void setupEntityRenderers() {
      RenderingRegistry.registerEntityRenderingHandler(EntityItemGrate.class, new RenderItem());
      RenderingRegistry.registerEntityRenderingHandler(EntitySpecialItem.class, new RenderSpecialItem());
      RenderingRegistry.registerEntityRenderingHandler(EntityFollowingItem.class, new RenderFollowingItem());
      RenderingRegistry.registerEntityRenderingHandler(EntityPermanentItem.class, new RenderSpecialItem());
      RenderingRegistry.registerEntityRenderingHandler(EntityAspectOrb.class, new RenderAspectOrb());
      RenderingRegistry.registerEntityRenderingHandler(EntityGolemBobber.class, new RenderGolemBobber());
      RenderingRegistry.registerEntityRenderingHandler(EntityGolemBase.class, new RenderGolemBase(new ModelGolem(false)));
      RenderingRegistry.registerEntityRenderingHandler(EntityWisp.class, new RenderWisp());
      RenderingRegistry.registerEntityRenderingHandler(EntityAlumentum.class, new RenderAlumentum());
      RenderingRegistry.registerEntityRenderingHandler(EntityPrimalOrb.class, new RenderPrimalOrb());
      RenderingRegistry.registerEntityRenderingHandler(EntityEldritchOrb.class, new RenderEldritchOrb());
      RenderingRegistry.registerEntityRenderingHandler(EntityGolemOrb.class, new RenderElectricOrb());
      RenderingRegistry.registerEntityRenderingHandler(EntityEmber.class, new RenderEmber());
      RenderingRegistry.registerEntityRenderingHandler(EntityShockOrb.class, new RenderElectricOrb());
      RenderingRegistry.registerEntityRenderingHandler(EntityExplosiveOrb.class, new RenderExplosiveOrb());
      RenderingRegistry.registerEntityRenderingHandler(EntityPechBlast.class, new RenderPechBlast());
      RenderingRegistry.registerEntityRenderingHandler(EntityBrainyZombie.class, new RenderBrainyZombie());
      RenderingRegistry.registerEntityRenderingHandler(EntityInhabitedZombie.class, new RenderInhabitedZombie());
      RenderingRegistry.registerEntityRenderingHandler(EntityGiantBrainyZombie.class, new RenderBrainyZombie());
      RenderingRegistry.registerEntityRenderingHandler(EntityPech.class, new RenderPech(new ModelPech(), 0.25F));
      RenderingRegistry.registerEntityRenderingHandler(EntityFireBat.class, new RenderFireBat());
      RenderingRegistry.registerEntityRenderingHandler(EntityFrostShard.class, new RenderFrostShard());
      RenderingRegistry.registerEntityRenderingHandler(EntityDart.class, new RenderDart());
      RenderingRegistry.registerEntityRenderingHandler(EntityPrimalArrow.class, new RenderPrimalArrow());
      RenderingRegistry.registerEntityRenderingHandler(EntityFallingTaint.class, new RenderFallingTaint());
      RenderingRegistry.registerEntityRenderingHandler(EntityThaumicSlime.class, new RenderThaumicSlime(new ModelSlime(16), new ModelSlime(0), 0.25F));
      RenderingRegistry.registerEntityRenderingHandler(EntityTaintSpider.class, new RenderTaintSpider());
      RenderingRegistry.registerEntityRenderingHandler(EntityTaintacle.class, new RenderTaintacle(0.6F, 10));
      RenderingRegistry.registerEntityRenderingHandler(EntityTaintacleSmall.class, new RenderTaintacle(0.2F, 6));
      RenderingRegistry.registerEntityRenderingHandler(EntityTaintacleGiant.class, new RenderTaintacle(1.0F, 14));
      RenderingRegistry.registerEntityRenderingHandler(EntityTaintSpore.class, new RenderTaintSpore());
      RenderingRegistry.registerEntityRenderingHandler(EntityTaintSporeSwarmer.class, new RenderTaintSporeSwarmer());
      RenderingRegistry.registerEntityRenderingHandler(EntityTaintSwarm.class, new RenderTaintSwarm());
      RenderingRegistry.registerEntityRenderingHandler(EntityTaintChicken.class, new RenderTaintChicken(new ModelChicken(), 0.3F));
      RenderingRegistry.registerEntityRenderingHandler(EntityTaintCow.class, new RenderTaintCow(new ModelCow(), 0.7F));
      RenderingRegistry.registerEntityRenderingHandler(EntityTaintCreeper.class, new RenderTaintCreeper());
      RenderingRegistry.registerEntityRenderingHandler(EntityTaintPig.class, new RenderTaintPig(new ModelPig(), 0.7F));
      RenderingRegistry.registerEntityRenderingHandler(EntityTaintSheep.class, new RenderTaintSheep(new ModelTaintSheep2(), new ModelTaintSheep1(), 0.7F));
      RenderingRegistry.registerEntityRenderingHandler(EntityTaintVillager.class, new RenderTaintVillager());
      RenderingRegistry.registerEntityRenderingHandler(EntityTravelingTrunk.class, new RenderTravelingTrunk(new ModelTrunk(), 0.5F));
      RenderingRegistry.registerEntityRenderingHandler(EntityMindSpider.class, new RenderMindSpider());
      RenderingRegistry.registerEntityRenderingHandler(EntityEldritchGuardian.class, new RenderEldritchGuardian(new ModelEldritchGuardian(), 0.5F));
      RenderingRegistry.registerEntityRenderingHandler(EntityEldritchWarden.class, new RenderEldritchGuardian(new ModelEldritchGuardian(), 0.6F));
      RenderingRegistry.registerEntityRenderingHandler(EntityCultistPortal.class, new RenderCultistPortal());
      RenderingRegistry.registerEntityRenderingHandler(EntityCultistKnight.class, new RenderCultist());
      RenderingRegistry.registerEntityRenderingHandler(EntityCultistLeader.class, new RenderCultist());
      RenderingRegistry.registerEntityRenderingHandler(EntityCultistCleric.class, new RenderCultist());
      RenderingRegistry.registerEntityRenderingHandler(EntityEldritchGolem.class, new RenderEldritchGolem(new ModelEldritchGolem(), 0.5F));
      RenderingRegistry.registerEntityRenderingHandler(EntityBottleTaint.class, new RenderSnowball(ConfigItems.itemBottleTaint, 0));
      RenderingRegistry.registerEntityRenderingHandler(EntityEldritchCrab.class, new RenderEldritchCrab());
      VillagerRegistry.instance().registerVillagerSkin(ConfigEntities.entWizardId, new ResourceLocation("thaumcraft", "textures/models/wizard.png"));
      VillagerRegistry.instance().registerVillagerSkin(ConfigEntities.entBankerId, new ResourceLocation("thaumcraft", "textures/models/moneychanger.png"));
   }

   void setupTileRenderers() {
      this.registerTileEntitySpecialRenderer(TileAlembic.class, new TileAlembicRenderer());
      this.registerTileEntitySpecialRenderer(TileArcaneBore.class, new TileArcaneBoreRenderer());
      this.registerTileEntitySpecialRenderer(TileArcaneBoreBase.class, new TileArcaneBoreBaseRenderer());
      this.registerTileEntitySpecialRenderer(TileArcaneLamp.class, new TileArcaneLampRenderer());
      this.registerTileEntitySpecialRenderer(TileArcaneLampGrowth.class, new TileArcaneLampRenderer());
      this.registerTileEntitySpecialRenderer(TileArcaneLampFertility.class, new TileArcaneLampRenderer());
      this.registerTileEntitySpecialRenderer(TileArcaneWorkbench.class, new TileArcaneWorkbenchRenderer());
      this.registerTileEntitySpecialRenderer(TileBanner.class, new TileBannerRenderer());
      this.registerTileEntitySpecialRenderer(TileBellows.class, new TileBellowsRenderer());
      this.registerTileEntitySpecialRenderer(TileCentrifuge.class, new TileCentrifugeRenderer());
      this.registerTileEntitySpecialRenderer(TileChestHungry.class, new TileChestHungryRenderer());
      this.registerTileEntitySpecialRenderer(TileCrucible.class, new TileCrucibleRenderer());
      this.registerTileEntitySpecialRenderer(TileCrystal.class, new TileCrystalRenderer());
      this.registerTileEntitySpecialRenderer(TileEldritchCrystal.class, new TileEldritchCrystalRenderer());
      this.registerTileEntitySpecialRenderer(TileDeconstructionTable.class, new TileDeconstructionTableRenderer());
      this.registerTileEntitySpecialRenderer(TileEldritchAltar.class, new TileEldritchCapRenderer("textures/models/obelisk_cap_altar.png"));
      this.registerTileEntitySpecialRenderer(TileEldritchCap.class, new TileEldritchCapRenderer("textures/models/obelisk_cap.png"));
      this.registerTileEntitySpecialRenderer(TileEldritchCrabSpawner.class, new TileEldritchCrabSpawnerRenderer());
      this.registerTileEntitySpecialRenderer(TileEldritchNothing.class, new TileEldritchNothingRenderer());
      this.registerTileEntitySpecialRenderer(TileEldritchObelisk.class, new TileEldritchObeliskRenderer());
      this.registerTileEntitySpecialRenderer(TileEldritchPortal.class, new TileEldritchPortalRenderer());
      this.registerTileEntitySpecialRenderer(TileEldritchLock.class, new TileEldritchLockRenderer());
      this.registerTileEntitySpecialRenderer(TileEssentiaCrystalizer.class, new TileEssentiaCrystalizerRenderer());
      this.registerTileEntitySpecialRenderer(TileEssentiaReservoir.class, new TileEssentiaReservoirRenderer());
      this.registerTileEntitySpecialRenderer(TileEtherealBloom.class, new TileEtherealBloomRenderer());
      this.registerTileEntitySpecialRenderer(TileHole.class, new TileHoleRenderer());
      this.registerTileEntitySpecialRenderer(TileInfusionMatrix.class, new TileRunicMatrixRenderer(0));
      this.registerTileEntitySpecialRenderer(TileInfusionPillar.class, new TileInfusionPillarRenderer());
      this.registerTileEntitySpecialRenderer(TileJar.class, new TileJarRenderer());
      this.registerTileEntitySpecialRenderer(TileMagicWorkbenchCharger.class, new TileMagicWorkbenchChargerRenderer());
      this.registerTileEntitySpecialRenderer(TileManaPod.class, new TileManaPodRenderer());
      TileMirrorRenderer tmr = new TileMirrorRenderer();
      this.registerTileEntitySpecialRenderer(TileMirror.class, tmr);
      this.registerTileEntitySpecialRenderer(TileMirrorEssentia.class, tmr);
      this.registerTileEntitySpecialRenderer(TileNode.class, new TileNodeRenderer());
      this.registerTileEntitySpecialRenderer(TileNodeEnergized.class, new TileNodeEnergizedRenderer());
      this.registerTileEntitySpecialRenderer(TileNodeConverter.class, new TileNodeConverterRenderer());
      this.registerTileEntitySpecialRenderer(TileNodeStabilizer.class, new TileNodeStabilizerRenderer());
      this.registerTileEntitySpecialRenderer(TilePedestal.class, new TilePedestalRenderer());
      this.registerTileEntitySpecialRenderer(TileResearchTable.class, new TileResearchTableRenderer());
      this.registerTileEntitySpecialRenderer(TileTable.class, new TileTableRenderer());
      this.registerTileEntitySpecialRenderer(TileThaumatorium.class, new TileThaumatoriumRenderer());
      this.registerTileEntitySpecialRenderer(TileTubeBuffer.class, new TileTubeBufferRenderer());
      this.registerTileEntitySpecialRenderer(TileTubeOneway.class, new TileTubeOnewayRenderer());
      this.registerTileEntitySpecialRenderer(TileTubeValve.class, new TileTubeValveRenderer());
      this.registerTileEntitySpecialRenderer(TileVisRelay.class, new TileVisRelayRenderer());
      this.registerTileEntitySpecialRenderer(TileWandPedestal.class, new TileWandPedestalRenderer());
      this.registerTileEntitySpecialRenderer(TileWarded.class, new TileWardedRenderer());
      this.registerTileEntitySpecialRenderer(TileFocalManipulator.class, new TileFocalManipulatorRenderer());
      this.registerTileEntitySpecialRenderer(TileAlchemyFurnaceAdvanced.class, new TileAlchemyFurnaceAdvancedRenderer());
      this.registerTileEntitySpecialRenderer(TileFluxScrubber.class, new TileFluxScrubberRenderer());
   }

   void setupBlockRenderers() {
      ConfigBlocks.blockFluxGasRI = RenderingRegistry.getNextAvailableRenderId();
      this.registerBlockRenderer(new BlockGasRenderer());
      ConfigBlocks.blockArcaneFurnaceRI = RenderingRegistry.getNextAvailableRenderId();
      this.registerBlockRenderer(new BlockArcaneFurnaceRenderer());
      ConfigBlocks.blockMetalDeviceRI = RenderingRegistry.getNextAvailableRenderId();
      this.registerBlockRenderer(new BlockMetalDeviceRenderer());
      ConfigBlocks.blockStoneDeviceRI = RenderingRegistry.getNextAvailableRenderId();
      this.registerBlockRenderer(new BlockStoneDeviceRenderer());
      ConfigBlocks.blockTaintRI = RenderingRegistry.getNextAvailableRenderId();
      this.registerBlockRenderer(new BlockTaintRenderer());
      ConfigBlocks.blockCosmeticOpaqueRI = RenderingRegistry.getNextAvailableRenderId();
      this.registerBlockRenderer(new BlockCosmeticOpaqueRenderer());
      ConfigBlocks.blockTubeRI = RenderingRegistry.getNextAvailableRenderId();
      this.registerBlockRenderer(new BlockTubeRenderer());
      ConfigBlocks.blockTaintFibreRI = RenderingRegistry.getNextAvailableRenderId();
      this.registerBlockRenderer(new BlockTaintFibreRenderer());
      ConfigBlocks.blockJarRI = RenderingRegistry.getNextAvailableRenderId();
      this.registerBlockRenderer(new BlockJarRenderer());
      ConfigBlocks.blockCustomOreRI = RenderingRegistry.getNextAvailableRenderId();
      this.registerBlockRenderer(new BlockCustomOreRenderer());
      ConfigBlocks.blockChestHungryRI = RenderingRegistry.getNextAvailableRenderId();
      this.registerBlockRenderer(new BlockChestHungryRenderer());
      ConfigBlocks.blockTableRI = RenderingRegistry.getNextAvailableRenderId();
      this.registerBlockRenderer(new BlockTableRenderer());
      ConfigBlocks.blockCandleRI = RenderingRegistry.getNextAvailableRenderId();
      this.registerBlockRenderer(new BlockCandleRenderer());
      ConfigBlocks.blockWoodenDeviceRI = RenderingRegistry.getNextAvailableRenderId();
      this.registerBlockRenderer(new BlockWoodenDeviceRenderer());
      ConfigBlocks.blockLifterRI = RenderingRegistry.getNextAvailableRenderId();
      this.registerBlockRenderer(new BlockLifterRenderer());
      ConfigBlocks.blockCrystalRI = RenderingRegistry.getNextAvailableRenderId();
      this.registerBlockRenderer(new BlockCrystalRenderer());
      ConfigBlocks.blockWardedRI = RenderingRegistry.getNextAvailableRenderId();
      this.registerBlockRenderer(new BlockWardedRenderer());
      ConfigBlocks.blockEldritchRI = RenderingRegistry.getNextAvailableRenderId();
      this.registerBlockRenderer(new BlockEldritchRenderer());
      ConfigBlocks.blockEssentiaReservoirRI = RenderingRegistry.getNextAvailableRenderId();
      this.registerBlockRenderer(new BlockEssentiaReservoirRenderer());
      ConfigBlocks.blockLootUrnRI = RenderingRegistry.getNextAvailableRenderId();
      this.registerBlockRenderer(new BlockLootUrnRenderer());
      ConfigBlocks.blockLootCrateRI = RenderingRegistry.getNextAvailableRenderId();
      this.registerBlockRenderer(new BlockLootCrateRenderer());
   }

   public void registerTileEntitySpecialRenderer(Class tile, TileEntitySpecialRenderer renderer) {
      ClientRegistry.bindTileEntitySpecialRenderer(tile, renderer);
   }

   public void registerBlockRenderer(ISimpleBlockRenderingHandler renderer) {
      RenderingRegistry.registerBlockHandler(renderer);
   }

   public World getClientWorld() {
      return FMLClientHandler.instance().getClient().theWorld;
   }

   public void blockSparkle(World world, int x, int y, int z, int c, int count) {
      Color color = new Color(c);
      float r = (float)color.getRed() / 255.0F;
      float g = (float)color.getGreen() / 255.0F;
      float b = (float)color.getBlue() / 255.0F;

      for(int a = 0; a < Thaumcraft.proxy.particleCount(count); ++a) {
         if (c == -9999) {
            r = 0.33F + world.getRandom().nextFloat() * 0.67F;
            g = 0.33F + world.getRandom().nextFloat() * 0.67F;
            b = 0.33F + world.getRandom().nextFloat() * 0.67F;
         }

         Thaumcraft.proxy.drawGenericParticles(world, (float)x - 0.1F + world.getRandom().nextFloat() * 1.2F, (float)y - 0.1F + world.getRandom().nextFloat() * 1.2F, (float)z - 0.1F + world.getRandom().nextFloat() * 1.2F, 0.0F, (double)world.getRandom().nextFloat() * 0.02, 0.0F, r - 0.2F + world.getRandom().nextFloat() * 0.4F, g - 0.2F + world.getRandom().nextFloat() * 0.4F, b - 0.2F + world.getRandom().nextFloat() * 0.4F, 0.9F, false, 112, 9, 1, 5 + world.getRandom().nextInt(8), world.getRandom().nextInt(10), 0.7F + world.getRandom().nextFloat() * 0.4F);
      }

   }

   public void sparkle(float x, float y, float z, float size, int color, float gravity) {
      if (this.getClientWorld() != null && this.getClientWorld().rand.nextInt(6) < this.particleCount(2)) {
         FXSparkle fx = new FXSparkle(this.getClientWorld(), x, y, z, size, color, 6);
         fx.noClip = true;
         fx.setGravity(gravity);
         Minecraft.getInstance().particleEngine.add(fx);

      }

   }

   public void sparkle(float x, float y, float z, int color) {
      if (this.getClientWorld() != null && this.getClientWorld().rand.nextInt(6) < this.particleCount(2)) {
         FXSparkle fx = new FXSparkle(this.getClientWorld(), x, y, z, 1.5F, color, 6);
         fx.noClip = true;
         Minecraft.getInstance().particleEngine.add(fx);

      }

   }

   public void spark(float x, float y, float z, float size, float r, float g, float b, float a) {
      if (this.getClientWorld() != null) {
         FXSpark fx = new FXSpark(this.getClientWorld(), x, y, z, size);
         fx.setRBGColorF(r, g, b);
         fx.setAlphaF(a);
         Minecraft.getInstance().particleEngine.add(fx);

      }

   }

   public void smokeSpiral(World world, double x, double y, double z, float rad, int start, int miny, int color) {
      FXSmokeSpiral fx = new FXSmokeSpiral(this.getClientWorld(), x, y, z, rad, start, miny);
      Color c = new Color(color);
      fx.setRBGColorF((float)c.getRed() / 255.0F, (float)c.getGreen() / 255.0F, (float)c.getBlue() / 255.0F);
      Minecraft.getInstance().particleEngine.add(fx);

   }

   public void crucibleBoilSound(World world, int xCoord, int yCoord, int zCoord) {
      world.playSound((float)xCoord + 0.5F, (float)yCoord + 0.5F, (float)zCoord + 0.5F, "thaumcraft:spill", 0.2F, 1.0F, false);
   }

   public void crucibleBoil(World world, int xCoord, int yCoord, int zCoord, TileCrucible tile, int j) {
      for(int a = 0; a < this.particleCount(1); ++a) {
         FXBubble fb = new FXBubble(world, (float)xCoord + 0.2F + world.getRandom().nextFloat() * 0.6F, (float)yCoord + 0.1F + tile.getFluidHeight(), (float)zCoord + 0.2F + world.getRandom().nextFloat() * 0.6F, 0.0F, 0.0F, 0.0F, 3);
         if (tile.aspects.size() == 0) {
            fb.setRBGColorF(1.0F, 1.0F, 1.0F);
         } else {
            Color color = new Color(tile.aspects.getAspects()[world.getRandom().nextInt(tile.aspects.getAspects().length)].getColor());
            fb.setRBGColorF((float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F);
         }

         fb.bubblespeed = 0.003 * (double)j;
         Minecraft.getInstance().particleEngine.add(fb);

      }

   }

   public void crucibleBubble(World world, float x, float y, float z, float cr, float cg, float cb) {
      FXBubble fb = new FXBubble(world, x, y, z, 0.0F, 0.0F, 0.0F, 1);
      fb.setRBGColorF(cr, cg, cb);
      Minecraft.getInstance().particleEngine.add(fb);

   }

   public void crucibleFroth(World world, float x, float y, float z) {
      FXBubble fb = new FXBubble(world, x, y, z, 0.0F, 0.0F, 0.0F, -4);
      fb.setRBGColorF(0.5F, 0.5F, 0.7F);
      fb.setFroth();
      Minecraft.getInstance().particleEngine.add(fb);

   }

   public void crucibleFrothDown(World world, float x, float y, float z) {
      FXBubble fb = new FXBubble(world, x, y, z, 0.0F, 0.0F, 0.0F, -4);
      fb.setRBGColorF(0.5F, 0.5F, 0.7F);
      fb.setFroth2();
      Minecraft.getInstance().particleEngine.add(fb);

   }

   public void wispFX(World worldObj, double posX, double posY, double posZ, float f, float g, float h, float i) {
      FXWisp ef = new FXWisp(worldObj, posX, posY, posZ, f, g, h, i);
      ef.setGravity(0.02F);
      Minecraft.getInstance().particleEngine.add(ef);

   }

   public void wispFX2(World worldObj, double posX, double posY, double posZ, float size, int type, boolean shrink, boolean clip, float gravity) {
      FXWisp ef = new FXWisp(worldObj, posX, posY, posZ, size, type);
      ef.setGravity(gravity);
      ef.shrink = shrink;
      ef.noClip = clip;
      Minecraft.getInstance().particleEngine.add(ef);

   }

   public void wispFXEG(World worldObj, double posX, double posY, double posZ, Entity target) {
      for(int a = 0; a < this.particleCount(1); ++a) {
         FXWispEG ef = new FXWispEG(worldObj, posX, posY, posZ, target);
         Minecraft.getInstance().particleEngine.add(ef);

      }

   }

   public void wispFX3(World worldObj, double posX, double posY, double posZ, double posX2, double posY2, double posZ2, float size, int type, boolean shrink, float gravity) {
      FXWisp ef = new FXWisp(worldObj, posX, posY, posZ, posX2, posY2, posZ2, size, type);
      ef.setGravity(gravity);
      ef.shrink = shrink;
      Minecraft.getInstance().particleEngine.add(ef);

   }

   public void wispFX4(World worldObj, double posX, double posY, double posZ, Entity target, int type, boolean shrink, float gravity) {
      FXWisp ef = new FXWisp(worldObj, posX, posY, posZ, target, type);
      ef.setGravity(gravity);
      ef.shrink = shrink;
      Minecraft.getInstance().particleEngine.add(ef);

   }

   public void burst(World worldObj, double sx, double sy, double sz, float size) {
      FXBurst ef = new FXBurst(worldObj, sx, sy, sz, size);
      Minecraft.getInstance().particleEngine.add(ef);
   }

   public void sourceStreamFX(World worldObj, double sx, double sy, double sz, float tx, float ty, float tz, int tagColor) {
      Color c = new Color(tagColor);
      FXWispArcing ef = new FXWispArcing(worldObj, tx, ty, tz, sx, sy, sz, 0.1F, (float)c.getRed() / 255.0F, (float)c.getGreen() / 255.0F, (float)c.getBlue() / 255.0F);
      ef.setGravity(0.0F);
      Minecraft.getInstance().particleEngine.add(ef);

   }

   public void bolt(World worldObj, Entity sourceEntity, Entity targetedEntity) {
      FXLightningBolt bolt = new FXLightningBolt(worldObj, sourceEntity, targetedEntity, worldObj.rand.nextLong(), 4);
      bolt.defaultFractal();
      bolt.setType(0);
      bolt.finalizeBolt();
   }

   public void nodeBolt(World worldObj, float x, float y, float z, Entity targetedEntity) {
      FXLightningBolt bolt = new FXLightningBolt(worldObj, x, y, z, targetedEntity.posX, targetedEntity.posY, targetedEntity.posZ, worldObj.rand.nextLong(), 10, 4.0F, 5);
      bolt.defaultFractal();
      bolt.setType(3);
      bolt.finalizeBolt();
   }

   public void nodeBolt(World worldObj, float x, float y, float z, float x2, float y2, float z2) {
      FXLightningBolt bolt = new FXLightningBolt(worldObj, x, y, z, x2, y2, z2, worldObj.rand.nextLong(), 10, 4.0F, 5);
      bolt.defaultFractal();
      bolt.setType(0);
      bolt.finalizeBolt();
   }

   public void excavateFX(int x, int y, int z, Player p, int bi, int md, int progress) {
      RenderGlobal rg = Minecraft.getMinecraft().renderGlobal;
      rg.destroyBlockPartially(p.getEntityId(), x, y, z, progress);
   }

   public void beam(World worldObj, double sx, double sy, double sz, double tx, double ty, double tz, int type, int color, boolean reverse, float endmod, int age) {
      FXBeam beamcon = null;
      Color c = new Color(color);
      beamcon = new FXBeam(worldObj, sx, sy, sz, tx, ty, tz, (float)c.getRed() / 255.0F, (float)c.getGreen() / 255.0F, (float)c.getBlue() / 255.0F, age);
      beamcon.setType(type);
      beamcon.setEndMod(endmod);
      beamcon.setReverse(reverse);
      beamcon.setPulse(false);
      Minecraft.getInstance().particleEngine.add(beamcon);
   }

   public FXBeamWand beamCont(World worldObj, Player p, double tx, double ty, double tz, int type, int color, boolean reverse, float endmod, Object input, int impact) {
      FXBeamWand beamcon = null;
      Color c = new Color(color);
      if (input instanceof FXBeamWand) {
         beamcon = (FXBeamWand)input;
      }

      if (beamcon != null && !beamcon.isDead) {
         beamcon.updateBeam(tx, ty, tz);
         beamcon.setEndMod(endmod);
         beamcon.impact = impact;
      } else {
         beamcon = new FXBeamWand(worldObj, p, tx, ty, tz, (float)c.getRed() / 255.0F, (float)c.getGreen() / 255.0F, (float)c.getBlue() / 255.0F, 8);
         beamcon.setType(type);
         beamcon.setEndMod(endmod);
         beamcon.setReverse(reverse);
         Minecraft.getInstance().particleEngine.add(beamcon);
      }

      return beamcon;
   }

   public Object beamBore(World worldObj, double px, double py, double pz, double tx, double ty, double tz, int type, int color, boolean reverse, float endmod, Object input, int impact) {
      FXBeamBore beamcon = null;
      Color c = new Color(color);
      if (input instanceof FXBeamBore) {
         beamcon = (FXBeamBore)input;
      }

      if (beamcon != null && !beamcon.isDead) {
         beamcon.updateBeam(tx, ty, tz);
         beamcon.setEndMod(endmod);
         beamcon.impact = impact;
      } else {
         beamcon = new FXBeamBore(worldObj, px, py, pz, tx, ty, tz, (float)c.getRed() / 255.0F, (float)c.getGreen() / 255.0F, (float)c.getBlue() / 255.0F, 8);
         beamcon.setType(type);
         beamcon.setEndMod(endmod);
         beamcon.setReverse(reverse);
         Minecraft.getInstance().particleEngine.add(beamcon);
      }

      return beamcon;
   }

   public void boreDigFx(World worldObj, int x, int y, int z, int x2, int y2, int z2, Block bi, int md) {
      if (worldObj.rand.nextInt(10) == 0) {
         FXBoreSparkle fb = new FXBoreSparkle(worldObj, (float)x + worldObj.rand.nextFloat(), (float)y + worldObj.rand.nextFloat(), (float)z + worldObj.rand.nextFloat(), (double)x2 + (double)0.5F, (double)y2 + (double)0.5F, (double)z2 + (double)0.5F);
         Minecraft.getInstance().particleEngine.add(fb);

      } else {
         FXBoreParticles fb = (new FXBoreParticles(worldObj, (float)x + worldObj.rand.nextFloat(), (float)y + worldObj.rand.nextFloat(), (float)z + worldObj.rand.nextFloat(), (double)x2 + (double)0.5F, (double)y2 + (double)0.5F, (double)z2 + (double)0.5F, bi, worldObj.rand.nextInt(6), md)).applyColourMultiplier(x, y, z);
         Minecraft.getInstance().particleEngine.add(fb);
      }

   }

   public void essentiaTrailFx(World worldObj, int x, int y, int z, int x2, int y2, int z2, int count, int color, float scale) {
      FXEssentiaTrail fb = new FXEssentiaTrail(worldObj, (double)x + (double)0.5F, (double)y + (double)0.5F, (double)z + (double)0.5F, (double)x2 + (double)0.5F, (double)y2 + (double)0.5F, (double)z2 + (double)0.5F, count, color, scale);
      Minecraft.getInstance().particleEngine.add(fb);

   }

   public void soulTrail(World world, Entity source, Entity target, float r, float g, float b) {
      for(int a = 0; a < this.particleCount(2); ++a) {
         if (world.getRandom().nextInt(10) == 0) {
            FXSparkleTrail st = new FXSparkleTrail(world, source.posX - (double)(source.width / 2.0F) + (double)(world.getRandom().nextFloat() * source.width), source.posY + (double)(world.getRandom().nextFloat() * source.height), source.posZ - (double)(source.width / 2.0F) + (double)(world.getRandom().nextFloat() * source.width), target, r, g, b);
            st.noClip = true;
            Minecraft.getInstance().particleEngine.add(st);

         } else {
            FXSmokeTrail st = new FXSmokeTrail(world, source.posX - (double)(source.width / 2.0F) + (double)(world.getRandom().nextFloat() * source.width), source.posY + (double)(world.getRandom().nextFloat() * source.height), source.posZ - (double)(source.width / 2.0F) + (double)(world.getRandom().nextFloat() * source.width), target, r, g, b);
            st.noClip = true;
            Minecraft.getInstance().particleEngine.add(st);

         }
      }

   }

   public int particleCount(int base) {
      if (FMLClientHandler.instance().getClient().gameSettings.particleSetting == 2) {
         return 0;
      } else {
         return FMLClientHandler.instance().getClient().gameSettings.particleSetting == 1 ? base : base * 2;
      }
   }

   public void furnaceLavaFx(World worldObj, int x, int y, int z, int facingX, int facingZ) {
      EntityLavaFX fb = new EntityLavaFX(worldObj, (float)x + 0.5F + (worldObj.rand.nextFloat() - worldObj.rand.nextFloat()) * 0.3F + (float) facingX, (float)y + 0.3F, (float)z + 0.5F + (worldObj.rand.nextFloat() - worldObj.rand.nextFloat()) * 0.3F + (float) facingZ);
      fb.motionY = 0.2F * worldObj.rand.nextFloat();
      float qx = facingX == 0 ? (worldObj.rand.nextFloat() - worldObj.rand.nextFloat()) * 0.5F : (float)facingX * worldObj.rand.nextFloat();
      float qz = facingZ == 0 ? (worldObj.rand.nextFloat() - worldObj.rand.nextFloat()) * 0.5F : (float)facingZ * worldObj.rand.nextFloat();
      fb.motionX = 0.15F * qx;
      fb.motionZ = 0.15F * qz;
      Minecraft.getInstance().particleEngine.add(fb);
   }

   public void blockRunes(World world, double x, double y, double z, float r, float g, float b, int dur, float grav) {
      FXBlockRunes fb = new FXBlockRunes(world, x + (double)0.5F, y + (double)0.5F, z + (double)0.5F, r, g, b, dur);
      fb.setGravity(grav);
      Minecraft.getInstance().particleEngine.add(fb);

   }

   public void blockWard(World world, double x, double y, double z, ForgeDirection side, float f, float f1, float f2) {
      FXBlockWard fb = new FXBlockWard(world, x + (double)0.5F, y + (double)0.5F, z + (double)0.5F, side, f, f1, f2);
      Minecraft.getInstance().particleEngine.add(fb);
   }

   public Object swarmParticleFX(World worldObj, Entity targetedEntity, float f1, float f2, float pg) {
      FXSwarm fx = new FXSwarm(worldObj, targetedEntity.posX + (double)((worldObj.rand.nextFloat() - worldObj.rand.nextFloat()) * 2.0F), targetedEntity.posY + (double)((worldObj.rand.nextFloat() - worldObj.rand.nextFloat()) * 2.0F), targetedEntity.posZ + (double)((worldObj.rand.nextFloat() - worldObj.rand.nextFloat()) * 2.0F), targetedEntity, 0.8F + worldObj.rand.nextFloat() * 0.2F, worldObj.rand.nextFloat() * 0.4F, 1.0F - worldObj.rand.nextFloat() * 0.2F, f1, f2, pg);
      Minecraft.getInstance().particleEngine.add(fx);

      return fx;
   }

   public void splooshFX(Entity e) {
      float f = e.level().rand.nextFloat() * (float)Math.PI * 2.0F;
      float f1 = e.level().rand.nextFloat() * 0.5F + 0.5F;
      float f2 = MathHelper.sin(f) * 2.0F * 0.5F * f1;
      float f3 = MathHelper.cos(f) * 2.0F * 0.5F * f1;
      FXBreaking fx = new FXBreaking(e.level(), e.posX + (double)f2, e.posY + (double)(e.level().rand.nextFloat() * e.height), e.posZ + (double)f3, Items.slime_ball);
      if (e.level().rand.nextBoolean()) {
         fx.setRBGColorF(0.6F, 0.0F, 0.3F);
         fx.setAlphaF(0.4F);
      } else {
         fx.setRBGColorF(0.3F, 0.0F, 0.3F);
         fx.setAlphaF(0.6F);
      }

      fx.setParticleMaxAge((int)(66.0F / (e.level().rand.nextFloat() * 0.9F + 0.1F)));
      Minecraft.getInstance().particleEngine.add(fx);
   }

   public void splooshFX(World worldObj, int x, int y, int z) {
      float f = worldObj.rand.nextFloat() * (float)Math.PI * 2.0F;
      float f1 = worldObj.rand.nextFloat() * 0.5F + 0.5F;
      float f2 = MathHelper.sin(f) * 2.0F * 0.5F * f1;
      float f3 = MathHelper.cos(f) * 2.0F * 0.5F * f1;
      FXBreaking fx = new FXBreaking(worldObj, (double)x + (double)f2 + (double)0.5F, (float)y + worldObj.rand.nextFloat(), (double)z + (double)f3 + (double)0.5F, Items.slime_ball);
      if (worldObj.rand.nextBoolean()) {
         fx.setRBGColorF(0.6F, 0.0F, 0.3F);
         fx.setAlphaF(0.4F);
      } else {
         fx.setRBGColorF(0.3F, 0.0F, 0.3F);
         fx.setAlphaF(0.6F);
      }

      fx.setParticleMaxAge((int)(66.0F / (worldObj.rand.nextFloat() * 0.9F + 0.1F)));
      Minecraft.getInstance().particleEngine.add(fx);
   }

   public void taintsplosionFX(Entity e) {
      FXBreaking fx = new FXBreaking(e.level(), e.posX, e.posY + (double)(e.level().rand.nextFloat() * e.height), e.posZ, Items.slime_ball);
      if (e.level().rand.nextBoolean()) {
         fx.setRBGColorF(0.6F, 0.0F, 0.3F);
         fx.setAlphaF(0.4F);
      } else {
         fx.setRBGColorF(0.3F, 0.0F, 0.3F);
         fx.setAlphaF(0.6F);
      }

      fx.motionX = (float)(Math.random() * (double)2.0F - (double)1.0F);
      fx.motionY = (float)(Math.random() * (double)2.0F - (double)1.0F);
      fx.motionZ = (float)(Math.random() * (double)2.0F - (double)1.0F);
      float f = (float)(Math.random() + Math.random() + (double)1.0F) * 0.15F;
      float f1 = MathHelper.sqrt_double(fx.motionX * fx.motionX + fx.motionY * fx.motionY + fx.motionZ * fx.motionZ);
      fx.motionX = fx.motionX / (double)f1 * (double)f * 0.9640000000596046;
      fx.motionY = fx.motionY / (double)f1 * (double)f * 0.9640000000596046 + (double)0.1F;
      fx.motionZ = fx.motionZ / (double)f1 * (double)f * 0.9640000000596046;
      fx.setParticleMaxAge((int)(66.0F / (e.level().rand.nextFloat() * 0.9F + 0.1F)));
      Minecraft.getInstance().particleEngine.add(fx);
   }

   public void tentacleAriseFX(Entity e) {
      int xx = MathHelper.floor_double(e.posX);
      int yy = MathHelper.floor_double(e.posY) - 1;
      int zz = MathHelper.floor_double(e.posZ);

      for(int j = 0; (float)j < 2.0F * e.height; ++j) {
         float f = e.level().rand.nextFloat() * (float)Math.PI * e.height;
         float f1 = e.level().rand.nextFloat() * 0.5F + 0.5F;
         float f2 = MathHelper.sin(f) * e.height * 0.25F * f1;
         float f3 = MathHelper.cos(f) * e.height * 0.25F * f1;
         FXBreaking fx = new FXBreaking(e.level(), e.posX + (double)f2, e.posY, e.posZ + (double)f3, Items.slime_ball);
         fx.setRBGColorF(0.4F, 0.0F, 0.4F);
         fx.setAlphaF(0.5F);
         fx.setParticleMaxAge((int)(66.0F / (e.level().rand.nextFloat() * 0.9F + 0.1F)));
         Minecraft.getInstance().particleEngine.add(fx);
         if (!e.level().isAirBlock(xx, yy, zz)) {
            f = e.level().rand.nextFloat() * (float)Math.PI * e.height;
            f1 = e.level().rand.nextFloat() * 0.5F + 0.5F;
            f2 = MathHelper.sin(f) * e.height * 0.25F * f1;
            f3 = MathHelper.cos(f) * e.height * 0.25F * f1;
            EntityDiggingFX fx2 = (new EntityDiggingFX(e.level(), e.posX + (double)f2, e.posY, e.posZ + (double)f3, 0.0F, 0.0F, 0.0F, e.level().getBlock(xx, yy, zz), e.level().getBlockMetadata(xx, yy, zz), 1)).applyColourMultiplier(xx, yy, zz);
            Minecraft.getInstance().particleEngine.add(fx2);
         }
      }

   }

   public void slimeJumpFX(Entity e, int i) {
      float f = e.level().rand.nextFloat() * (float)Math.PI * 2.0F;
      float f1 = e.level().rand.nextFloat() * 0.5F + 0.5F;
      float f2 = MathHelper.sin(f) * (float)i * 0.5F * f1;
      float f3 = MathHelper.cos(f) * (float)i * 0.5F * f1;
      FXBreaking fx = new FXBreaking(e.level(), e.posX + (double)f2, (e.boundingBox.minY + e.boundingBox.maxY) / (double)2.0F, e.posZ + (double)f3, Items.slime_ball);
      fx.setRBGColorF(0.7F, 0.0F, 1.0F);
      fx.setAlphaF(0.4F);
      fx.setParticleMaxAge((int)(66.0F / (e.level().rand.nextFloat() * 0.9F + 0.1F)));
      Minecraft.getInstance().particleEngine.add(fx);
   }

   public void dropletFX(World world, float i, float j, float k, float r, float g, float b) {
      FXDrop obj = new FXDrop(world, i, j, k, r, g, b);
      Minecraft.getInstance().particleEngine.add(obj);
   }

   public void taintLandFX(Entity e) {
      float f = e.level().rand.nextFloat() * (float)Math.PI * 2.0F;
      float f1 = e.level().rand.nextFloat() * 0.5F + 0.5F;
      float f2 = MathHelper.sin(f) * 2.0F * 0.5F * f1;
      float f3 = MathHelper.cos(f) * 2.0F * 0.5F * f1;
      if (e.level().isRemote) {
         FXBreaking fx = new FXBreaking(e.level(), e.posX + (double)f2, (e.boundingBox.minY + e.boundingBox.maxY) / (double)2.0F, e.posZ + (double)f3, Items.slime_ball);
         fx.setRBGColorF(0.1F, 0.0F, 0.1F);
         fx.setAlphaF(0.4F);
         fx.setParticleMaxAge((int)(66.0F / (e.level().rand.nextFloat() * 0.9F + 0.1F)));
         Minecraft.getInstance().particleEngine.add(fx);
      }

   }

   public void hungryNodeFX(World worldObj, int sourceX, int sourceY, int sourceZ, int targetX, int targetY, int targetZ, Block block, int md) {
      FXBoreParticles fb = (new FXBoreParticles(worldObj, (float)sourceX + worldObj.rand.nextFloat(), (float)sourceY + worldObj.rand.nextFloat(), (float)sourceZ + worldObj.rand.nextFloat(), (double)targetX + (double)0.5F, (double)targetY + (double)0.5F, (double)targetZ + (double)0.5F, block, worldObj.rand.nextInt(6), md)).applyColourMultiplier(sourceX, sourceY, sourceZ);
      Minecraft.getInstance().particleEngine.add(fb);
   }

   public void drawInfusionParticles1(World worldObj, double x, double y, double z, int x2, int y2, int z2, Item id, int md) {
      FXBoreParticles fb = (new FXBoreParticles(worldObj, x, y, z, (double)x2 + (double)0.5F, (double)y2 - (double)0.5F, (double)z2 + (double)0.5F, id, worldObj.rand.nextInt(6), md)).applyColourMultiplier(x2, y2, z2);
      fb.setAlphaF(0.3F);
      fb.motionX = (float)worldObj.rand.nextGaussian() * 0.03F;
      fb.motionY = (float)worldObj.rand.nextGaussian() * 0.03F;
      fb.motionZ = (float)worldObj.rand.nextGaussian() * 0.03F;
      Minecraft.getInstance().particleEngine.add(fb);
   }

   public void drawInfusionParticles2(World worldObj, double x, double y, double z, int x2, int y2, int z2, Block id, int md) {
      FXBoreParticles fb = (new FXBoreParticles(worldObj, x, y, z, (double)x2 + (double)0.5F, (double)y2 - (double)0.5F, (double)z2 + (double)0.5F, id, worldObj.rand.nextInt(6), md)).applyColourMultiplier(x2, y2, z2);
      fb.setAlphaF(0.3F);
      Minecraft.getInstance().particleEngine.add(fb);
   }

   public void drawInfusionParticles3(World worldObj, double x, double y, double z, int x2, int y2, int z2) {
      FXBoreSparkle fb = new FXBoreSparkle(worldObj, x, y, z, (double)x2 + (double)0.5F, (double)y2 - (double)0.5F, (double)z2 + (double)0.5F);
      fb.setRBGColorF(0.4F + worldObj.rand.nextFloat() * 0.2F, 0.2F, 0.6F + worldObj.rand.nextFloat() * 0.3F);
      Minecraft.getInstance().particleEngine.add(fb);

   }

   public void drawInfusionParticles4(World worldObj, double x, double y, double z, int x2, int y2, int z2) {
      FXBoreSparkle fb = new FXBoreSparkle(worldObj, x, y, z, (double)x2 + (double)0.5F, (double)y2 - (double)0.5F, (double)z2 + (double)0.5F);
      fb.setRBGColorF(0.2F, 0.6F + worldObj.rand.nextFloat() * 0.3F, 0.3F);
      Minecraft.getInstance().particleEngine.add(fb);

   }

   public void drawVentParticles(World worldObj, double x, double y, double z, double x2, double y2, double z2, int color) {
      FXVent fb = new FXVent(worldObj, x, y, z, x2, y2, z2, color);
      fb.setAlphaF(0.4F);
      Minecraft.getInstance().particleEngine.add(fb);

   }

   public void drawGenericParticles(World worldObj, double x, double y, double z, double x2, double y2, double z2, float r, float g, float b, float alpha, boolean loop, int start, int num, int inc, int age, int delay, float scale) {
      FXGeneric fb = new FXGeneric(worldObj, x, y, z, x2, y2, z2);
      fb.setMaxAge(age, delay);
      fb.setRBGColorF(r, g, b);
      fb.setAlphaF(alpha);
      fb.setLoop(loop);
      fb.setParticles(start, num, inc);
      fb.setScale(scale);
      Minecraft.getInstance().particleEngine.add(fb);

   }

   public void drawVentParticles(World worldObj, double x, double y, double z, double x2, double y2, double z2, int color, float scale) {
      FXVent fb = new FXVent(worldObj, x, y, z, x2, y2, z2, color);
      fb.setAlphaF(0.4F);
      fb.setScale(scale);
      Minecraft.getInstance().particleEngine.add(fb);

   }

   public Object beamPower(World worldObj, double px, double py, double pz, double tx, double ty, double tz, float r, float g, float b, boolean pulse, Object input) {
      FXBeamPower beamcon = null;
      if (input instanceof FXBeamPower) {
         beamcon = (FXBeamPower)input;
      }

      if (beamcon != null && !beamcon.isDead) {
         beamcon.updateBeam(px, py, pz, tx, ty, tz);
         beamcon.setPulse(pulse, r, g, b);
      } else {
         beamcon = new FXBeamPower(worldObj, px, py, pz, tx, ty, tz, r, g, b, 8);
         Minecraft.getInstance().particleEngine.add(beamcon);
      }

      return beamcon;
   }

   public boolean isShiftKeyDown() {
      return GuiScreen.isShiftKeyDown();
   }

   public void bottleTaintBreak(World world, double x, double y, double z) {
      String s = "iconcrack_" + Item.getIdFromItem(ConfigItems.itemBottleTaint) + "_" + 0;

      for(int k1 = 0; k1 < 8; ++k1) {
         Minecraft.getMinecraft().renderGlobal.spawnParticle(s, x, y, z, world.getRandom().nextGaussian() * 0.15, world.getRandom().nextDouble() * 0.2, world.getRandom().nextGaussian() * 0.15);
      }

      world.playSound(x, y, z, "game.potion.smash", 1.0F, world.getRandom().nextFloat() * 0.1F + 0.9F, false);
   }

   public void arcLightning(World world, double x, double y, double z, double tx, double ty, double tz, float r, float g, float b, float h) {
      FXSparkle ef2 = new FXSparkle(world, tx, ty, tz, tx, ty, tz, 3.0F, 6, 2);
      ef2.setGravity(0.0F);
      ef2.noClip = true;
      ef2.setRBGColorF(r, g, b);
      Minecraft.getInstance().particleEngine.add(ef2);

      FXArc efa = new FXArc(world, x, y, z, tx, ty, tz, r, g, b, h);
      Minecraft.getInstance().particleEngine.add(efa);
   }


   static long lastScroll = 0;
   static Field fieldPage = null;
   static Field fieldLastPage = null;
   static Method methodPlayScroll = null;
   static Method GuiResearchRecipeMouseClicked = null;
   private static final int mPrevX = 261, mPrevY = 189, mNextX = -17, mNextY = 189;
   private static final int paneWidth = 256, paneHeight = 181;

   private long updateCounter = 0;

   public static void handleMouseInput(GuiResearchTable screen) {
      if (fieldLastPage == null || fieldPage == null || methodPlayScroll == null) return;
      final int dwheel = Mouse.getEventDWheel();
      if (dwheel == 0)
         return;
      final long currentTimeMillis = System.currentTimeMillis();
      if (currentTimeMillis - lastScroll > 50) {
         lastScroll = currentTimeMillis;
         try {
            int page = fieldPage.getInt(screen);
            if ((dwheel < 0) != ConfigurationHandler.INSTANCE.isInverted()) {
               int lastPage = fieldLastPage.getInt(screen);
               if (page < lastPage) {
                  fieldPage.setInt(screen, page + 1);
                  methodPlayScroll.invoke(screen);
               }
            } else {
               if (page > 0) {
                  fieldPage.setInt(screen, page - 1);
                  methodPlayScroll.invoke(screen);
               }
            }
         } catch (ReflectiveOperationException err) {
            System.err.println("Error scrolling through aspect list!");
            err.printStackTrace();
         }
      }
   }

   public static void handleMouseInput(GuiResearchRecipe screen) {
      if (GuiResearchRecipeMouseClicked == null) return;
      final int dwheel = Mouse.getEventDWheel();
      if (dwheel == 0)
         return;
      final long currentTimeMillis = System.currentTimeMillis();
      if (currentTimeMillis - lastScroll > 50) {
         lastScroll = currentTimeMillis;
         // emulate a click into respective buttons
         int mX, mY;
         if ((dwheel < 0) != ConfigurationHandler.INSTANCE.isInverted()) {
            mX = mPrevX;
            mY= mPrevY;
         } else {
            mX = mNextX;
            mY = mNextY;
         }
         mX += (screen.width - paneWidth) / 2;
         mY += (screen.height - paneHeight) / 2;
         try {
            GuiResearchRecipeMouseClicked.invoke(screen, mX, mY, 0);
         } catch (ReflectiveOperationException err) {
            System.err.println("Error scrolling through research page!");
            err.printStackTrace();
         }
      }
   }

   public ClientProxy() {
      super();
      MinecraftForge.EVENT_BUS.register(this);
   }

   public static boolean dev = false;//!(boolean) data.get("runtimeDeobfuscationEnabled");

   @Override
   public void preInit(FMLPreInitializationEvent e) {
      super.preInit(e);
      ConfigurationHandler.INSTANCE.setGUISettings();
      try {
         Class<GuiResearchTable> guiResearchTableClass = GuiResearchTable.class;
         fieldPage = guiResearchTableClass.getDeclaredField("page");
         fieldPage.setAccessible(true);
         fieldLastPage = guiResearchTableClass.getDeclaredField("lastPage");
         fieldLastPage.setAccessible(true);
         methodPlayScroll = guiResearchTableClass.getDeclaredMethod("playButtonScroll");
         methodPlayScroll.setAccessible(true);
      } catch (Exception err) {
         System.err.println("Cannot find thaumcraft fields. Aspect list scrolling will not properly function!");
         err.printStackTrace();
      }
      String mouseClicked = dev ? "mouseClicked" : "func_73864_a";
      try {
         GuiResearchRecipeMouseClicked = GuiResearchRecipe.class.getDeclaredMethod(mouseClicked, int.class, int.class, int.class);
         GuiResearchRecipeMouseClicked.setAccessible(true);
      } catch (Exception err) {
         System.err.println("Cannot find thaumcraft fields. Research page scrolling will not properly function!");
         err.printStackTrace();
      }
//      final IResourceManager resourceManager = Minecraft.getMinecraft().getResourceManager();
//      if (resourceManager instanceof IReloadableResourceManager) {
//         //noinspection Convert2Lambda
//         ((IReloadableResourceManager) resourceManager).registerReloadListener(new IResourceManagerReloadListener() {
//            @Override
//            public void onResourceManagerReload(IResourceManager ignored) {
//               reflectiveReloadModel(FXSonic.class, "MODEL");
//               reflectiveReloadModel(TileAlchemyFurnaceAdvancedRenderer.class, "FURNACE");
//            }
//         });
//      }
      ParticleEngineFix.init();
   }

   @Override
   public void init(FMLInitializationEvent e) {
      super.init(e);
      ThaumonomiconIndexSearcher.init();
      DrawResearchCompletionCounter.init();
      HUDNotification.init();
      BrowserPaging.init();
   }

   @Override
   public void postInit(FMLPostInitializationEvent e) {
      super.postInit(e);
      try {
         Object wgSearcher = Class.forName("witchinggadgets.client.ThaumonomiconIndexSearcher").getField("instance").get(null);
         MinecraftForge.EVENT_BUS.unregister(wgSearcher);
         FMLCommonHandler.instance().bus().unregister(wgSearcher);
      } catch (ReflectiveOperationException ignored) {
         // WG is probably not installed, ignoring
      }
   }

   @SubscribeEvent
   public void onWorldLoad(WorldEvent.Load e) {
      if (e.(Platform.getEnvironment() == Env.CLIENT))
         CommonUtils.sortResearchCategories(false);
   }

   @SubscribeEvent(priority = EventPriority.LOWEST)
   public void onTooltip(ItemTooltipEvent e) {
      if (ConfigurationHandler.INSTANCE.isAddTooltip() && e.itemStack != null) {
         if (e.itemStack.getItem() == ConfigItems.itemResearchNotes)
            e.toolTip.add(EnumChatFormatting.GOLD + StatCollector.translateToLocal("tc4tweaks.enabled_scrolling"));
         else if (e.itemStack.getItem() == ConfigItems.WandCastingItem) {
            if (!ConfigurationHandler.INSTANCE.isCheckWorkbenchRecipes() || !NetworkedConfiguration.isCheckWorkbenchRecipes()) {
               e.toolTip.add(EnumChatFormatting.RED + StatCollector.translateToLocal("tc4tweaks.disable_vanilla"));
            }
         }
      }
   }

   @SubscribeEvent
   public void onTickEnd(TickEvent.ClientTickEvent e) {
      if (e.phase == TickEvent.Phase.END) {
         if (++updateCounter > ConfigurationHandler.INSTANCE.getUpdateInterval()) {
            updateCounter = 0;
            synchronized (postponed) {
               for (Map.Entry<TileMagicWorkbench, Void> workbench : postponed.entrySet()) {
                  TileMagicWorkbench tile = workbench.getKey();
                  if (tile != null && tile.eventHandler != null && !tile.isInvalid() && tile.hasWorldObj()) {
                     // best effort guess on whether tile is valid
                     tile.eventHandler.onCraftMatrixChanged(tile);
                  }
               }
               postponed.clear();
            }
         }
      }
   }

   @SubscribeEvent
   public void onServerConnected(FMLNetworkEvent.ClientConnectedToServerEvent e) {
      if (!e.isLocal) {
         NetworkedConfiguration.resetClient();
      }
   }

   private static void reflectiveReloadModel(Class<?> cls, String resLocationField) {
      try {
         FieldUtils.writeDeclaredStaticField(cls,
                 "model",
                 AdvancedModelLoader.loadModel(
                         (ResourceLocation)
                                 FieldUtils.readDeclaredStaticField(
                                         cls,
                                         resLocationField,
                                         true)
                 )
                 , true);
      } catch (ReflectiveOperationException e) {
         // ignore
      }
   }
}
