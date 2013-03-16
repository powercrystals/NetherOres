package powercrystals.netherores.world;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenNetherOres extends WorldGenerator
{
	private int _minableBlockId;
	private int _minableBlockMeta;
	private int _numberOfBlocks;
	
	public WorldGenNetherOres(int blockId, int blockMeta, int numBlocks)
	{
		_minableBlockId = blockId;
		_minableBlockMeta = blockMeta;
		_numberOfBlocks = numBlocks;
	}

	@Override
	public boolean generate(World world, Random random, int chunkX, int y, int chunkZ)
	{
		float f = random.nextFloat() * (float)Math.PI;
		double d = (float)(chunkX + 8) + (MathHelper.sin(f) * (float)_numberOfBlocks) / 8F;
		double d1 = (float)(chunkX + 8) - (MathHelper.sin(f) * (float)_numberOfBlocks) / 8F;
		double d2 = (float)(chunkZ + 8) + (MathHelper.cos(f) * (float)_numberOfBlocks) / 8F;
		double d3 = (float)(chunkZ + 8) - (MathHelper.cos(f) * (float)_numberOfBlocks) / 8F;
		double d4 = (y + random.nextInt(3)) - 2;
		double d5 = (y + random.nextInt(3)) - 2;
		for(int blockNum = 0; blockNum <= _numberOfBlocks; blockNum++)
		{
			double d6 = d + ((d1 - d) * (double)blockNum) / (double)_numberOfBlocks;
			double d7 = d4 + ((d5 - d4) * (double)blockNum) / (double)_numberOfBlocks;
			double d8 = d2 + ((d3 - d2) * (double)blockNum) / (double)_numberOfBlocks;
			double d9 = (random.nextDouble() * (double)_numberOfBlocks) / 16D;
			double d10 = (double)(MathHelper.sin(((float)blockNum * 3.141593F) / (float)_numberOfBlocks) + 1.0F) * d9 + 1.0D;
			double d11 = (double)(MathHelper.sin(((float)blockNum * 3.141593F) / (float)_numberOfBlocks) + 1.0F) * d9 + 1.0D;
			int xStart = MathHelper.floor_double(d6 - d10 / 2D);
			int yStart = MathHelper.floor_double(d7 - d11 / 2D);
			int zStart = MathHelper.floor_double(d8 - d10 / 2D);
			int xStop = MathHelper.floor_double(d6 + d10 / 2D);
			int yStop = MathHelper.floor_double(d7 + d11 / 2D);
			int zStop = MathHelper.floor_double(d8 + d10 / 2D);
			for(int blockX = xStart; blockX <= xStop; blockX++)
			{
				double d12 = (((double)blockX + 0.5D) - d6) / (d10 / 2D);
				if(d12 * d12 >= 1.0D)
				{
					continue;
				}
				for(int blockY = yStart; blockY <= yStop; blockY++)
				{
					double d13 = (((double)blockY + 0.5D) - d7) / (d11 / 2D);
					if(d12 * d12 + d13 * d13 >= 1.0D)
					{
						continue;
					}
					for(int blockZ = zStart; blockZ <= zStop; blockZ++)
					{
						double d14 = (((double)blockZ + 0.5D) - d8) / (d10 / 2D);
						if(d12 * d12 + d13 * d13 + d14 * d14 < 1.0D && world.getBlockId(blockX, blockY, blockZ) == Block.netherrack.blockID)
						{
							world.setBlockAndMetadataWithNotify(blockX, blockY, blockZ, _minableBlockId, _minableBlockMeta, 2);
						}
					}
				}
			}
		}

		return true;
	}
}