package powercrystals.netherores.world;

import java.util.Random;

import powercrystals.netherores.NetherOresCore;
import powercrystals.netherores.ores.Ores;

import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import cpw.mods.fml.common.IWorldGenerator;

public class NetherOresWorldGenHandler implements IWorldGenerator
{
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider)
	{
		if(world.provider.dimensionId == -1)
		{
			generateNether(world, random, chunkX * 16, chunkZ * 16);
		}
	}

	private void generateNether(World world, Random random, int chunkX, int chunkZ)
	{
		for(Ores o : Ores.values())
		{
			if((o.isRegisteredSmelting() || o.isRegisteredMacerator() || NetherOresCore.forceOreSpawn.getBoolean(false)) && !o.getDisabled())
			{
				for(int i = 0; i < o.getGroupsPerChunk(); i++)
				{
					int x = chunkX + random.nextInt(16); 
					int y = o.getMinY() + random.nextInt(o.getMaxY() - o.getMinY());
					int z = chunkZ + random.nextInt(16);
					new WorldGenNetherOres(NetherOresCore.getOreBlock(o.getBlockIndex()).blockID, o.getMetadata(), o.getBlocksPerGroup()).generate(world, random, x, y, z);
				}
			}
		}
		
		if(NetherOresCore.enableHellfish.getBoolean(true))
		{
			for(int i = 0; i < 9; i++)
			{
				int x = chunkX + random.nextInt(16); 
				int y = random.nextInt(128);
				int z = chunkZ + random.nextInt(16);
				new WorldGenNetherOres(NetherOresCore.blockHellfish.blockID, 0, 12).generate(world, random, x, y, z);
			}
		}
	}
}
