package thaumcraft.common.tiles;

@Deprecated(forRemoval = true)
public class TileOwned /*extends TileThaumcraft*/ {
//   public String owner = "";
//   public ArrayList<String> accessList = new ArrayList<>();
//   public boolean safeToRemove = false;
//
//   public boolean canUpdate() {
//      return false;
//   }
//
//   public void readFromNBT(NBTTagCompound nbttagcompound) {
//      super.readFromNBT(nbttagcompound);
//      this.owner = nbttagcompound.getString("owner");
//      NBTTagList var2 = nbttagcompound.getTagList("access", 10);
//      this.accessList = new ArrayList<>();
//
//      for(int var3 = 0; var3 < var2.tagCount(); ++var3) {
//         NBTTagCompound var4 = var2.getCompoundTagAt(var3);
//         this.accessList.add(var4.getString("name"));
//      }
//
//   }
//
//   public void writeToNBT(NBTTagCompound nbttagcompound) {
//      super.writeToNBT(nbttagcompound);
//      NBTTagList var2 = new NBTTagList();
//
//       for (String s : this.accessList) {
//           NBTTagCompound var4 = new NBTTagCompound();
//           var4.setString("name", s);
//           var2.appendTag(var4);
//       }
//
//      nbttagcompound.setTag("access", var2);
//   }
//
//   public void readCustomNBT(NBTTagCompound nbttagcompound) {
//      this.owner = nbttagcompound.getString("owner");
//   }
//
//   public void writeCustomNBT(NBTTagCompound nbttagcompound) {
//      nbttagcompound.setString("owner", this.owner);
//   }
}
