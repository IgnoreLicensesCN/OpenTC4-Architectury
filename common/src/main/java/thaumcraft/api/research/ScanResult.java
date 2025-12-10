package thaumcraft.api.research;

import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import java.util.Objects;

public class ScanResult {
	public byte type = 0;   //1=blocks,2=entities,3=phenomena
	public String item;
	public Entity entity;
	public String phenomena;

	public ScanResult(byte type, Item item, Entity entity,
			String phenomena) {
		this(type,item.toString(),entity,phenomena);
	}
	public ScanResult(byte type,String item, Entity entity, String phenomena) {
		super();
		this.type = type;
		this.item = item;
		this.entity = entity;
		this.phenomena = phenomena;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ScanResult) {
			ScanResult sr = (ScanResult) obj;
			if (type != sr.type)
				return false;
			if (type == 1
					&& (item != sr.item))
				return false;
			if (type == 2) {
				// 旧版用 entity.getEntityId()
				// 新版改为用 ResourceKey
				Level level = sr.entity.level();
				ResourceKey<EntityType<?>> key1 = level.registryAccess()
						.registryOrThrow(Registries.ENTITY_TYPE)
						.getResourceKey(sr.entity.getType())
						.orElseThrow();
				ResourceKey<EntityType<?>> key2 = level.registryAccess()
						.registryOrThrow(Registries.ENTITY_TYPE)
						.getResourceKey(this.entity.getType())
						.orElseThrow();
				if (!Objects.equals(key1,key2)) return false;
			}
            return type != 3 || phenomena.equals(sr.phenomena);
		}
		return true;
	}

	@Override
	public int hashCode() {
		return Objects.hash(type, item, entity, phenomena);
	}
}
