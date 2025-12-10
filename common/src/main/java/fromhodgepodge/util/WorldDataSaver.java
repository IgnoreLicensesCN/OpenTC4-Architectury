package fromhodgepodge.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WorldDataSaver {

    public static final WorldDataSaver INSTANCE = new WorldDataSaver();
    public static final Logger LOGGER = LogManager.getLogger("HodgepodgeWorldDataSaver");

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Map<Path, WrappedCompoundTag> pendingData = new ConcurrentHashMap<>();

    private WorldDataSaver() {}

    public record WrappedCompoundTag(CompoundTag tag, boolean compressed, boolean backup) {}

    public void saveData(File file, CompoundTag parentTag, boolean compressed, boolean backup) {
        Path path = file.toPath();
        pendingData.put(path, new WrappedCompoundTag(parentTag, compressed, backup));
        executor.submit(() -> writeNext(path));
    }

    private void writeNext(Path path) {
        WrappedCompoundTag wrapped = pendingData.remove(path);
        if (wrapped == null) return;
        try {
            if (wrapped.backup && Files.exists(path)) {
                Path backupPath = path.resolveSibling(path.getFileName() + "_old");
                Files.deleteIfExists(backupPath);
                Files.move(path, backupPath);
            }

            if (wrapped.compressed) {
                NbtIo.writeCompressed(wrapped.tag, path.toFile());
            } else {
                NbtIo.write(wrapped.tag, path.toFile());
            }

        } catch (Exception e) {
            LOGGER.error("Failed to write data to file {}", path, e);
        }
    }

    public void shutdown() {
        executor.shutdown();
    }
}

//import net.minecraft.nbt.NbtIo;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.world.storage.IThreadedFileIO;
//import net.minecraft.world.storage.ThreadedFileIOBase;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.util.Collections;
//import java.util.Iterator;
//import java.util.LinkedHashMap;
//import java.util.Map;
//
//public class WorldDataSaver implements IThreadedFileIO {
//
//    public static final WorldDataSaver INSTANCE = new WorldDataSaver();
//
//    static class WrappedCompoundTag {
//
//        public final CompoundTag tag;
//        public final boolean compressed;
//        public final boolean backup;
//
//        public WrappedCompoundTag(CompoundTag tag, boolean compressed, boolean backup) {
//            this.tag = tag;
//            this.compressed = compressed;
//            this.backup = backup;
//        }
//    }
//
//    public static final Logger LOGGER = LogManager.getLogger("HodgepodgeWorldDataSaver");
//
//    protected WorldDataSaver() {}
//
//    private final Map<File, WrappedCompoundTag> pendingData = Collections.synchronizedMap(new LinkedHashMap<>());
//
//    @Override
//    public boolean writeNextIO() {
//        final File file;
//        final WrappedCompoundTag wrapped;
//        final CompoundTag data;
//        final boolean compressed;
//        final boolean backup;
//        synchronized (pendingData) {
//            Iterator<Map.Entry<File, WrappedCompoundTag>> it = pendingData.entrySet().iterator();
//            if (!it.hasNext()) {
//                return false;
//            }
//            Map.Entry<File, WrappedCompoundTag> entry = it.next();
//            file = entry.getKey();
//            wrapped = entry.getValue();
//            data = wrapped.tag;
//            backup = wrapped.backup;
//            compressed = wrapped.compressed;
//            it.remove();
//
//        }
//        if (backup) {
//            final File backupFile = new File(file.getParentFile(), file.getName() + "_old");
//            if (backupFile.exists()) {
//                backupFile.delete();
//            }
//            file.renameTo(backupFile);
//        }
//
//        try {
//            if (compressed) {
//                try (FileOutputStream fileoutputstream = new FileOutputStream(file)) {
//                    NbtIo.writeCompressed(data, fileoutputstream);
//                }
//            } else NbtIo.write(data, file);
//
//        } catch (Exception e) {
//            LOGGER.error("Failed to write data to file {}", file, e);
//            e.printStackTrace();
//        }
//        return true;
//    }
//
//    public void saveData(File file, CompoundTag parentTag, boolean compressed, boolean backup) {
//        WrappedCompoundTag wrapped = new WrappedCompoundTag(parentTag, compressed, backup);
//        if (pendingData.containsKey(file)) {
//            pendingData.replace(file, wrapped);
//        } else {
//            pendingData.put(file, wrapped);
//        }
//        ThreadedFileIOBase.threadedIOInstance.queueIO(this);
//    }
//}
