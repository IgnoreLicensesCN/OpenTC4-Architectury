package thaumcraft.common.lib.network.fx;

import dev.architectury.networking.NetworkManager;
import thaumcraft.common.lib.ThaumcraftBaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import thaumcraft.client.fx.migrated.particles.FXBoreParticles;
import thaumcraft.common.ClientFXUtils;
import thaumcraft.common.Thaumcraft;

public class PacketFXBlockDigS2C extends ThaumcraftBaseS2CMessage {
    public static final String ID = Thaumcraft.MOD_ID + ":block_dig";
    public static MessageType messageType;

    private int x;
    private int y;
    private int z;
    private byte xd;
    private byte yd;
    private byte zd;
    private ResourceLocation item;


    public PacketFXBlockDigS2C() {
    }

    public PacketFXBlockDigS2C(int x, int y, int z, byte xd, byte yd, byte zd, Item item) {
        this(x, y, z, xd, yd, zd, item.arch$registryName());
    }

    public PacketFXBlockDigS2C(int x, int y, int z, byte xd, byte yd, byte zd, ResourceLocation item) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.xd = xd;
        this.yd = yd;
        this.zd = zd;
        this.item = item;
    }

    @Override
    public MessageType getType() {
        return messageType;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
        buf.writeByte(xd);
        buf.writeByte(yd);
        buf.writeByte(zd);
        buf.writeResourceLocation(item);
    }

    public static PacketFXBlockDigS2C decode(FriendlyByteBuf buf) {
        return new PacketFXBlockDigS2C(
                buf.readInt(),
                buf.readInt(),
                buf.readInt(),
                buf.readByte(),
                buf.readByte(),
                buf.readByte(),
                buf.readResourceLocation()
        );
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        Item item = BuiltInRegistries.ITEM.get(this.item);
        var level = Minecraft.getInstance().level;
        if (level == null) {
            return;
        }
        if (item instanceof BlockItem blockItem) {
            Block block = blockItem.getBlock();
            var soundType = block.getSoundType(block.defaultBlockState());
            for (int a = 0; a < ClientFXUtils.particleCount(20); ++a) {
                FXBoreParticles fb = (new FXBoreParticles(
                        level, (float) xd + level.random.nextFloat(),
                        (float) yd + level.random.nextFloat(),
                        (float) zd + level.random.nextFloat(),
                        (double) x + (double) 0.5F, (double) y + (double) 0.5F,
                        (double) z + (double) 0.5F, block, Direction.values()[level.random.nextInt(6)]
                )).applyColourMultiplier(x, y, z);
                Minecraft.getInstance().particleEngine.add(fb);
            }

            level.playSound(
                    null, (float) xd + 0.5F, (float) yd + 0.5F, (float) zd + 0.5F,
                    soundType.getBreakSound(), SoundSource.BLOCKS, (soundType.getVolume() + 1.0F) / 2.0F,
                    soundType.getPitch() * 0.8F
            );
        } else {
            var soundType = Blocks.STONE.getSoundType(Blocks.STONE.defaultBlockState());
            for (int a = 0; a < ClientFXUtils.particleCount(20); ++a) {
                FXBoreParticles fb = (new FXBoreParticles(
                        level, (float) xd + level.random.nextFloat(), (float) yd + level.random.nextFloat(),
                        (float) zd + level.random.nextFloat(), (double) x + (double) 0.5F, (double) y + (double) 0.5F,
                        (double) z + (double) 0.5F, item, Direction.values()[level.random.nextInt(6)]
                )).applyColourMultiplier(x, y, z);
                Minecraft.getInstance().particleEngine.add(fb);
            }

            level.playSound(
                    null, (float) xd + 0.5F, (float) yd + 0.5F, (float) zd + 0.5F,
                    soundType.getBreakSound(), SoundSource.BLOCKS, (soundType.getVolume() + 1.0F) / 2.0F,
                    soundType.getPitch() * 0.8F
            );
        }
    }
}
