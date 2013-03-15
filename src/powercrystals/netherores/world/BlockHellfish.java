package powercrystals.netherores.world;

import java.util.Random;

import powercrystals.netherores.entity.EntityHellfish;
import net.minecraft.block.BlockNetherrack;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

public class BlockHellfish extends BlockNetherrack
{
	private Icon _icon;
	
	public BlockHellfish(int blockId)
	{
		super(blockId);
		setHardness(0.4F);
		setStepSound(soundStoneFootstep);
		setUnlocalizedName("blockNetherOresHellfish");
	}
	
	@Override
	public void func_94332_a(IconRegister ir)
	{
		_icon = ir.func_94245_a("powercrystals/netherores/" + getUnlocalizedName());
	}
	
	@Override
	public Icon getBlockTextureFromSideAndMetadata(int side, int meta)
	{
		return _icon;
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
