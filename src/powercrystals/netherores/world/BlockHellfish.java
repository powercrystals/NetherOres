package powercrystals.netherores.world;

import java.util.Random;

import powercrystals.netherores.entity.EntityHellfish;
import net.minecraft.block.BlockNetherrack;
import net.minecraft.world.World;

public class BlockHellfish extends BlockNetherrack
{
	public BlockHellfish(int blockId)
	{
		super(blockId, 103);
		setHardness(0.4F);
		setStepSound(soundStoneFootstep);
		setBlockName("hellfish");
	}
	
	public int quantityDropped(Random rand)
	{
		return 0;
	}
	
	public void onBlockDestroyedByPlayer(World world, int x, int y, int z, int fortune)
	{
		if (!world.isRemote)
		{
			EntityHellfish hellfish = new EntityHellfish(world);
			hellfish.setLocationAndAngles((double)x + 0.5D, (double)y, (double)z + 0.5D, 0.0F, 0.0F);
			world.spawnEntityInWorld(hellfish);
			hellfish.spawnExplosionParticle();
		}

		super.onBlockDestroyedByPlayer(world, x, y, z, fortune);
	}
}
