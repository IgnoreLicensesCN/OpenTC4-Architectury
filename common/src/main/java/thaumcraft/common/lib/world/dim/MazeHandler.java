package thaumcraft.common.lib.world.dim;

import fromhodgepodge.util.WorldDataSaver;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.LevelResource;

import java.io.File;
import java.nio.file.Files;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import static com.linearity.opentc4.Consts.MazeHandlerCompoundTagAccessors.*;

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

   private static void readNBT(CompoundTag nbt) {
      ListTag tagList = MAZE_HANDLER_CELLS_ACCESSOR.readFromCompoundTag(nbt);

      for(int a = 0; a < tagList.size(); ++a) {
         CompoundTag cell = tagList.getCompound(a);
         int x = MAZE_HANDLER_CELL_LOC_X_ACCESSOR.readFromCompoundTag(cell);
         int z = MAZE_HANDLER_CELL_LOC_Z_ACCESSOR.readFromCompoundTag(cell);
         short v = MAZE_HANDLER_CELL_INFO_ACCESSOR.readFromCompoundTag(cell);
         putToHashMapRaw(new CellLoc(x, z), v);
      }

   }

   private static CompoundTag writeAsNBT() {
      CompoundTag nbt = new CompoundTag();
      ListTag tagList = new ListTag();

      for (Map.Entry<CellLoc, Short> entry : MazeHandler.labyrinth.entrySet()) {
         if (entry.getValue() == null) {
            continue;
         }
         short v = entry.getValue();
         if (v<= 0) {
            continue;
         }
         CellLoc loc = entry.getKey();
         CompoundTag cell = new CompoundTag();
         MAZE_HANDLER_CELL_LOC_X_ACCESSOR.writeToCompoundTag(cell,loc.x);
         MAZE_HANDLER_CELL_LOC_Z_ACCESSOR.writeToCompoundTag(cell,loc.z);
         MAZE_HANDLER_CELL_INFO_ACCESSOR.writeToCompoundTag(cell,v);
         tagList.add(cell);
      }

      MAZE_HANDLER_CELLS_ACCESSOR.writeToCompoundTag(nbt,tagList);
      return nbt;
//      CompoundTag nbt = new CompoundTag();
//      NBTTagList tagList = new NBTTagList();
//
//      for(CellLoc loc : labyrinth.keySet()) {
//         short v = getFromHashMapRaw(loc);
//         if (v > 0) {
//            CompoundTag cell = new CompoundTag();
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

   public static void loadMaze(Level world) {
      if (!(world instanceof ServerLevel serverLevel)) {return;}
      clearHashMap();
      File file1 = new File(serverLevel.getServer().getWorldPath(MAZE_RESOURCE).toFile(), "labyrinth.dat");
      if (saveData(file1)) {
         return;
      }

      file1 = new File(serverLevel.getServer().getWorldPath(MAZE_RESOURCE).toFile(), "labyrinth.dat_old");
      if (saveData(file1)) {
         return;
      }

   }

   private static boolean saveData(File file1) {
      if (file1.exists()) {
         try {
            CompoundTag CompoundTag = NbtIo.readCompressed(Files.newInputStream(file1.toPath()));
            CompoundTag CompoundTag1 = CompoundTag.getCompound("Data");
            readNBT(CompoundTag1);
            return true;
         } catch (Exception exception1) {
            exception1.printStackTrace();
         }
      }
      return false;
   }

   public static final LevelResource MAZE_RESOURCE = new LevelResource("labyrinth.dat");

   public static void saveMaze(Level world) {
      if (!(world instanceof ServerLevel serverLevel)){return;}
      CompoundTag tag = writeAsNBT();
      CompoundTag parentTag = new CompoundTag();
      parentTag.put("data", tag);

//      String filename = tag.contains("version")
//              ? "labyrinth_v" + tag.getInt("version") + ".dat"
//              : "labyrinth.dat";

      //todo:is it correct?
      File file = new File(serverLevel.getServer().getWorldPath(MAZE_RESOURCE).toFile(), MAZE_RESOURCE.getId());
      WorldDataSaver.INSTANCE.saveData(file, parentTag, true, true);
//      CompoundTag tag = writeNBT();
//      CompoundTag parentTag = new CompoundTag();
//      parentTag.setTag("data", tag);
//      final String filename;
//
//      // Adds support for Salis Arcana updating the labyrinth file format
//      if (tag.hasKey("version")) {
//         filename = "labyrinth_v" + tag.getInteger("version") + ".dat";
//      } else {
//         filename = "labyrinth.dat";
//      }
//
//      final File file = new File(world.getSaveHandler().getWorldDirectory(), filename);
//
//      WorldDataSaver.INSTANCE.saveData(file, parentTag, true, true);
//      HodgepodgeCore.saveWorldDataBackup(file, parentTag);

//      CompoundTag CompoundTag = writeNBT();
//      CompoundTag CompoundTag1 = new CompoundTag();
//      CompoundTag1.setTag("Data", CompoundTag);
//
//      try {
//         File file1 = new File(world.getSaveHandler().getWorldDirectory(), "labyrinth.dat_new");
//         File file2 = new File(world.getSaveHandler().getWorldDirectory(), "labyrinth.dat_old");
//         File file3 = new File(world.getSaveHandler().getWorldDirectory(), "labyrinth.dat");
//         NbtIo.writeCompressed(CompoundTag1, Files.newOutputStream(file1.toPath()));
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

   public static void generateEldritch(Level world, Random random, int cx, int cz) {
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

   private static void generatePassage(Level world, Random random, int cx, int cz, int y, Cell cell) {
       random.nextInt(1);
       GenPassage.generateDefaultPassage(world, random, cx, cz, y, cell);
   }
}
