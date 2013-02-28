package powercrystals.netherores;

import java.util.Random;

import powercrystals.netherores.ores.Ores;
import powercrystals.netherores.ores.WorldGenNetherOres;

import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkProvider;
import cpw.mods.fml.common.IWorldGenerator;

public class NetherOresWorldGenHandler implements IWorldGenerator
{
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider)
	{
		if(world.getBiomeGenForCoords(chunkX, chunkZ) == BiomeGenBase.hell)
		{
			generateNether(world, random, chunkX*16, chunkZ*16);
		}
	}

	private void generateNether(World world, Random random, int chunkX, int chunkZ)
	{
		for(Ores o : Ores.values())
		{
			if(o.isRegisteredSmelting() || o.isRegisteredMacerator() || NetherOresCore.forceOreSpawn.getBoolean(false))
			{
				for(int i = 0; i < o.getGroupsPerChunk(); i++)
				{
					int x = chunkX + random.nextInt(16); 
					int y = o.getMinY() + random.nextInt(o.getMaxY() - o.getMinY());
					int z = chunkZ + random.nextInt(16);
					new WorldGenNetherOres(NetherOresCore.blockNetherOres.blockID, o.getMetadata(), o.getBlocksPerGroup()).generate(world, random, x, y, z);
				}
			}
		}
	}
}
