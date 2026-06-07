package thaumcraft.common.items.junkbox;

@Deprecated(forRemoval = true)
public class ItemResearchNotes/* extends Item */{
//   @SideOnly(Side.CLIENT)
//   public IIcon iconNote;
//   @SideOnly(Side.CLIENT)
//   public IIcon iconNoteOver;
//   @SideOnly(Side.CLIENT)
//   public IIcon iconDiscovery;
//   @SideOnly(Side.CLIENT)
//   public IIcon iconDiscoveryOver;
//
//   public ItemResearchNotes() {
//      this.setHasSubtypes(true);
//      this.setMaxDamage(0);
//      this.setMaxStackSize(1);
//   }
//
//   @SideOnly(Side.CLIENT)
//   public void registerIcons(IIconRegister ir) {
//      this.iconNote = ir.registerIcon("thaumcraft:researchnotes");
//      this.iconNoteOver = ir.registerIcon("thaumcraft:researchnotesoverlay");
//      this.iconDiscovery = ir.registerIcon("thaumcraft:discovery");
//      this.iconDiscoveryOver = ir.registerIcon("thaumcraft:discoveryoverlay");
//   }
//
//   @SideOnly(Side.CLIENT)
//   public IIcon getIconFromDamage(int par1) {
//      return par1 / 64 == 0 ? this.iconNote : this.iconDiscovery;
//   }
//
//   @SideOnly(Side.CLIENT)
//   public IIcon getIconFromDamageForRenderPass(int par1, int renderPass) {
//      return renderPass == 0 ? (par1 / 64 == 0 ? this.iconNote : this.iconDiscovery) : (par1 / 64 == 0 ? this.iconNoteOver : this.iconDiscoveryOver);
//   }
//
//   public ItemStack onItemRightClick(ItemStack stack, World world, Player player) {
//      if (Platform.getEnvironment() != Env.CLIENT) {
//         if (ResearchManager.getData(stack) != null
//                 && ResearchManager.getData(stack).isCompleted()
//                 && !ResearchManager.isResearchComplete(player.getCommandSenderName(), ResearchManager.getData(stack).key)
//         ) {
//            if (ResearchItem.doesPlayerHaveRequisites(player.getCommandSenderName(), ResearchManager.getData(stack).key)) {
//               PacketHandler.INSTANCE.sendTo(new PacketResearchComplete(ResearchManager.getData(stack).key), (ServerPlayer)player);
//               Thaumcraft.proxy.getResearchManager().completeResearch(player, ResearchManager.getData(stack).key);
//               if (ResearchItem.getResearch(ResearchManager.getData(stack).key).siblings != null) {
//                  for(String sibling : ResearchItem.getResearch(ResearchManager.getData(stack).key).siblings) {
//                     if (!ResearchManager.isResearchComplete(player.getCommandSenderName(), sibling) && ResearchItem.doesPlayerHaveRequisites(player.getCommandSenderName(), sibling)) {
//                        PacketHandler.INSTANCE.sendTo(new PacketResearchComplete(sibling), (ServerPlayer)player);
//                        Thaumcraft.proxy.getResearchManager().completeResearch(player, sibling);
//                     }
//                  }
//               }
//
//               --stack.stackSize;
//               world.playSoundAtEntity(player, "thaumcraft:learn", 0.75F, 1.0F);
//            } else {
//               player.addChatMessage(new ChatComponentTranslation(Component.translatable("tc.researcherror")));
//            }
//         } else if (stack.getItemDamage() == 42 || stack.getItemDamage() == 24) {//removed
//            String key = ResearchManager.findHiddenResearch(player);
//            if (key.equals("FAIL")) {
//               --stack.stackSize;
//               EntityItem entityItem = new EntityItem(
//                       world,
//                       player.posX,
//                       player.posY + (double)(player.getEyeHeight() / 2.0F),
//                       player.posZ, new ItemStack(ConfigItems.itemResource, 7 + world.getRandom().nextInt(3), 9));
//               world.spawnEntityInWorld(entityItem);
//               world.playSoundAtEntity(player, "thaumcraft:erase", 0.75F, 1.0F);
//            } else {
//               stack.setItemDamage(0);
//               stack.stackTagCompound = ResearchManager.createNote(stack, key, player.level()).stackTagCompound;
//               world.playSoundAtEntity(player, "thaumcraft:write", 0.75F, 1.0F);
//            }
//         }
//      }
//
//      return stack;
//   }
//
//   @SideOnly(Side.CLIENT)
//   public int getColorFromItemStack(ItemStack stack, int par2) {
//      if (par2 == 1) {
//         int c = 10066329;
//         ResearchNoteData rd = ResearchManager.getData(stack);
//         if (rd != null) {
//            c = rd.color;
//         }
//
//         return c;
//      } else {
//         return super.getColorFromItemStack(stack, par2);
//      }
//   }
//
//   @SideOnly(Side.CLIENT)
//   public boolean requiresMultipleRenderPasses() {
//      return true;
//   }
//
//   public boolean getShareTag() {
//       return super.getShareTag();
//   }
//
//   public String getItemStackDisplayName(ItemStack itemstack) {
//      String name = itemstack.getItemDamage() < 64 ? Component.translatable("item.researchnotes.name") : Component.translatable("item.discovery.name");
//      return name;
//   }
//
//   public void addInformation(ItemStack stack, Player par2Player, List list, boolean par4) {
//      if (stack.getItemDamage() == 24 || stack.getItemDamage() == 42) {
//         list.add(EnumChatFormatting.GOLD + Component.translatable("item.researchnotes.unknown.1"));
//         list.add(EnumChatFormatting.BLUE + Component.translatable("item.researchnotes.unknown.2"));
//      }
//
//      ResearchNoteData rd = ResearchManager.getData(stack);
//      if (rd != null && rd.key != null && ResearchItem.getResearch(rd.key) != null) {
//         list.add("§6" + ResearchItem.getResearch(rd.key).getName());
//         list.add("§o" + ResearchItem.getResearch(rd.key).getText());
//         int warp = ThaumcraftApi.getWarp(rd.key);
//         if (warp > 0) {
//            if (warp > 5) {
//               warp = 5;
//            }
//
//            String ws = Component.translatable("tc.forbidden");
//            String wr = Component.translatable("tc.forbidden.level." + warp);
//            String wte = ws.replaceAll("%n", wr);
//            list.add(EnumChatFormatting.DARK_PURPLE + wte);
//         }
//      }
//
//   }
//
//   public EnumRarity getRarity(ItemStack itemstack) {
//      return itemstack.getItemDamage() < 64 ? EnumRarity.rare : EnumRarity.epic;
//   }
}
