package thaumcraft.client.fx;


@Deprecated(forRemoval = true,since = "use mc's own")
public class ParticleEngine {
//   public static ParticleEngine instance = new ParticleEngine();
//   public static final ResourceLocation particleTexture = new ResourceLocation("thaumcraft", "textures/misc/particles.png");
//   public static final ResourceLocation particleTexture2 = new ResourceLocation("thaumcraft", "textures/misc/particles2.png");
//   protected World worldObj;
//   private HashMap<Integer,ArrayList<EntityFX>>[] particles = new HashMap[]{new HashMap<>(), new HashMap<>(), new HashMap<>(), new HashMap<>()};
//   private Random rand = new Random();
//
//   @SideOnly(Side.CLIENT)
//   @SubscribeEvent
//   public void onRenderWorldLast(RenderWorldLastEvent event) {
//      float frame = event.partialTicks;
//      Entity entity = Minecraft.getMinecraft().thePlayer;
//      TextureManager renderer = Minecraft.getMinecraft().renderEngine;
//      int dim = Minecraft.getMinecraft().theWorld.dimension();
//      renderer.bindTexture(particleTexture);
//      GL11.glPushMatrix();
//      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//      GL11.glDepthMask(false);
//      GL11.glEnable(GL11.GL_BLEND);
//      GL11.glAlphaFunc(516, 0.003921569F);
//      boolean rebound = false;
//
//      for(int layer = 0; layer < 4; ++layer) {
//         if (this.particles[layer].containsKey(dim)) {
//            ArrayList<EntityFX> parts = this.particles[layer].get(dim);
//            if (!parts.isEmpty()) {
//               if (!rebound && layer >= 2) {
//                  renderer.bindTexture(particleTexture2);
//                  rebound = true;
//               }
//
//               GL11.glPushMatrix();
//               switch (layer) {
//                  case 0:
//                  case 2:
//                     GL11.glBlendFunc(770, 1);
//                     break;
//                  case 1:
//                  case 3:
//                     GL11.glBlendFunc(770, 771);
//               }
//
//               float f1 = ActiveRenderInfo.rotationX;
//               float f2 = ActiveRenderInfo.rotationZ;
//               float f3 = ActiveRenderInfo.rotationYZ;
//               float f4 = ActiveRenderInfo.rotationXY;
//               float f5 = ActiveRenderInfo.rotationXZ;
//               EntityFX.interpPosX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)frame;
//               EntityFX.interpPosY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)frame;
//               EntityFX.interpPosZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)frame;
//               Tessellator tessellator = Tessellator.instance;
//               tessellator.startDrawingQuads();
//
//                for (EntityFX part : parts) {
//                    final EntityFX entityfx = part;
//                    if (entityfx != null) {
//                        tessellator.setBrightness(entityfx.getBrightnessForRender(frame));
//
//                        try {
//                            entityfx.renderParticle(tessellator, frame, f1, f5, f2, f3, f4);
//                        } catch (Throwable throwable) {
//                            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Rendering Particle");
//                            CrashReportCategory crashreportcategory = crashreport.makeCategory("Particle being rendered");
//                            crashreportcategory.addCrashSectionCallable("Particle", entityfx::toString);
//                            crashreportcategory.addCrashSectionCallable("Particle Type", () -> "ENTITY_PARTICLE_TEXTURE");
//                            throw new ReportedException(crashreport);
//                        }
//                    }
//                }
//
//               tessellator.draw();
//               GL11.glPopMatrix();
//            }
//         }
//      }
//
//      GL11.glDisable(GL11.GL_BLEND);
//      GL11.glDepthMask(true);
//      GL11.glAlphaFunc(516, 0.1F);
//      GL11.glPopMatrix();
//   }
//
//   public void addEffect(World world, EntityFX fx) {
//      if (!this.particles[fx.getFXLayer()].containsKey(world.dimension())) {
//         this.particles[fx.getFXLayer()].put(world.dimension(), new ArrayList());
//      }
//
//      ArrayList<EntityFX> parts = this.particles[fx.getFXLayer()].get(world.dimension());
//      if (parts.size() >= 2000) {
//         parts.remove(0);
//      }
//
//      parts.add(fx);
//      this.particles[fx.getFXLayer()].put(world.dimension(), parts);
//   }
//
//   @SideOnly(Side.CLIENT)
//   @SubscribeEvent
//   public void updateParticles(TickEvent.ClientTickEvent event) {
//      if (event.side != Side.SERVER) {
//         Minecraft mc = FMLClientHandler.instance().getClient();
//         World world = mc.theWorld;
//         if (mc.theWorld != null) {
//            int dim = world.dimension();
//            if (event.phase == Phase.START) {
//               for(int layer = 0; layer < 4; ++layer) {
//                  if (this.particles[layer].containsKey(dim)) {
//                     ArrayList<EntityFX> parts = this.particles[layer].get(dim);
//
//                     for(int j = 0; j < parts.size(); ++j) {
//                        final EntityFX entityfx = parts.get(j);
//
//                        try {
//                           if (entityfx != null) {
//                              entityfx.onUpdate();
//                           }
//                        } catch (Throwable throwable) {
//                           CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Ticking Particle");
//                           CrashReportCategory crashreportcategory = crashreport.makeCategory("Particle being ticked");
//                           crashreportcategory.addCrashSectionCallable("Particle", entityfx::toString);
//                           crashreportcategory.addCrashSectionCallable("Particle Type", () -> "ENTITY_PARTICLE_TEXTURE");
//                           throw new ReportedException(crashreport);
//                        }
//
//                        if (entityfx == null || entityfx.isDead) {
//                           parts.remove(j--);
//                           this.particles[layer].put(dim, parts);
//                        }
//                     }
//                  }
//               }
//            }
//
//         }
//      }
//   }
}
