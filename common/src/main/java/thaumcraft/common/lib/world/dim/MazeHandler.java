package thaumcraft.common.lib.world.dim;

import fromhodgepodge.util.WorldDataSaver;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;

import java.io.File;
import java.nio.file.Files;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class MazeHandler {
   public static ConcurrentHashMap<CellLoc,Short> labyrinth = new ConcurrentHashMap<>();

   public static synchronized void putToHashMap(CellLoc key, Cell cell) {
      labyrinth.put(key, cell.pack());
   }

   public static synchronized void putToHashMapRaw(CellLoc key, short cell) {
      labyrinth.put(key, cell);
   }

   public static synchronized Cell getFromHashMap(CellLoc key) {
      return labyrinth.containsKey(key) ? new Cell(labyrinth.get(key)) : null;
   }

   public static synchronized void removeFromHashMap(CellLoc key) {
      labyrinth.remove(key);
   }

   public static synchronized short getFromHashMapRaw(CellLoc key) {
      return labyrinth.containsKey(key) ? labyrinth.get(key) : 0;
   }

   public static synchronized void clearHashMap() {
      labyrinth.clear();
   }

   private static void readNBT(NBTTagCompound nbt) {
      NBTTagList tagList = nbt.getTagList("cells", 10);

      for(int a = 0; a < tagList.tagCount(); ++a) {
         NBTTagCompound cell = tagList.getCompoundTagAt(a);
         int x = cell.getInteger("x");
         int z = cell.getInteger("z");
         short v = cell.getShort("cell");
         putToHashMapRaw(new CellLoc(x, z), v);
      }

   }

   private static NBTTagCompound writeNBT() {
      NBTTagCompound nbt = new NBTTagCompound();
      NBTTagList tagList = new NBTTagList();

      for (Map.Entry<CellLoc, Short> entry : MazeHandler.labyrinth.entrySet()) {
         if (entry.getValue() == null) {
            continue;
         }
         short v = entry.getValue();
         if (v<= 0) {
            continue;
         }
         CellLoc loc = entry.getKey();
         NBTTagCompound cell = new NBTTagCompound();
         cell.setInteger("x", loc.x);
         cell.setInteger("z", loc.z);
         cell.setShort("cell", v);
         tagList.appendTag(cell);
      }

      nbt.setTag("cells", tagList);
      return nbt;
//      NBTTagCompound nbt = new NBTTagCompound();
//      NBTTagList tagList = new NBTTagList();
//
//      for(CellLoc loc : labyrinth.keySet()) {
//         short v = getFromHashMapRaw(loc);
//         if (v > 0) {
//            NBTTagCompound cell = new NBTTagCompound();
//            cell.setInteger("x", loc.x);
//            cell.setInteger("z", loc.z);
//            cell.setShort("cell", v);
//            tagList.appendTag(cell);
//         }
//      }
//
//      nbt.setTag("cells", tagList);
//      return nbt;
   }

   public static void loadMaze(World world) {
      clearHashMap();
      File file1 = new File(world.getSaveHandler().getWorldDirectory(), "labyrinth.dat");
      if (saveData(file1)) {
         return;
      }

      file1 = new File(world.getSaveHandler().getWorldDirectory(), "labyrinth.dat_old");
      if (saveData(file1)) {
         return;
      }

   }

   private static boolean saveData(File file1) {
      if (file1.exists()) {
         try {
            NBTTagCompound nbttagcompound = CompressedStreamTools.readCompressed(Files.newInputStream(file1.toPath()));
            NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("Data");
            readNBT(nbttagcompound1);
            return true;
         } catch (Exception exception1) {
            exception1.printStackTrace();
         }
      }
      return false;
   }

   public static void saveMaze(World world) {
      NBTTagCompound tag = writeNBT();
      NBTTagCompound parentTag = new NBTTagCompound();
      parentTag.setTag("data", tag);
      final String filename;

      // Adds support for Salis Arcana updating the labyrinth file format
      if (tag.hasKey("version")) {
         filename = "labyrinth_v" + tag.getInteger("version") + ".dat";
      } else {
         filename = "labyrinth.dat";
      }

      final File file = new File(world.getSaveHandler().getWorldDirectory(), filename);

      WorldDataSaver.INSTANCE.saveData(file, parentTag, true, true);
//      HodgepodgeCore.saveWorldDataBackup(file, parentTag);

//      NBTTagCompound nbttagcompound = writeNBT();
//      NBTTagCompound nbttagcompound1 = new NBTTagCompound();
//      nbttagcompound1.setTag("Data", nbttagcompound);
//
//      try {
//         File file1 = new File(world.getSaveHandler().getWorldDirectory(), "labyrinth.dat_new");
//         File file2 = new File(world.getSaveHandler().getWorldDirectory(), "labyrinth.dat_old");
//         File file3 = new File(world.getSaveHandler().getWorldDirectory(), "labyrinth.dat");
//         CompressedStreamTools.writeCompressed(nbttagcompound1, Files.newOutputStream(file1.toPath()));
//         if (file2.exists()) {
//            file2.delete();
//         }
//
//         file3.renameTo(file2);
//         if (file3.exists()) {
//            file3.delete();
//         }
//
//         file1.renameTo(file3);
//         if (file1.exists()) {
//            file1.delete();
//         }
//      } catch (Exception exception) {
//         exception.printStackTrace();
//      }

   }

   public static boolean mazesInRange(int chunkX, int chunkZ, int w, int h) {
      for(int x = -w; x <= w; ++x) {
         for(int z = -h; z <= h; ++z) {
            if (getFromHashMap(new CellLoc(chunkX + x, chunkZ + z)) != null) {
               return true;
            }
         }
      }

      return false;
   }

   public static void generateEldritch(World world, Random random, int cx, int cz) {
      CellLoc loc = new CellLoc(cx, cz);
      Cell cell = getFromHashMap(loc);
      if (cell != null) {
         switch (cell.feature) {
            case 1:
               GenPortal.generatePortal(world, random, cx, cz, 50, cell);
               break;
            case 2:
            case 3:
            case 4:
            case 5:
               GenBossRoom.generateRoom(world, random, cx, cz, 50, cell);
               break;
            case 6:
               GenKeyRoom.generateRoom(world, random, cx, cz, 50, cell);
               break;
            case 7:
               GenNestRoom.generateRoom(world, random, cx, cz, 50, cell);
               break;
            case 8:
               GenLibraryRoom.generateRoom(world, random, cx, cz, 50, cell);
               break;
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            default:
               generatePassage(world, random, cx, cz, 50, cell);
         }

         GenCommon.processDecorations(world);
      }

   }

   private static void generatePassage(World world, Random random, int cx, int cz, int y, Cell cell) {
       random.nextInt(1);
       GenPassage.generateDefaultPassage(world, random, cx, cz, y, cell);
   }
}
