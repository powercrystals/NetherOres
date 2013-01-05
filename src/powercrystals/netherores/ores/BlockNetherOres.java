package powercrystals.netherores.ores;

import java.util.List;
import java.util.Random;

import cpw.mods.fml.common.network.PacketDispatcher;

import powercrystals.core.net.PacketWrapper;
import powercrystals.netherores.NetherOresCore;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class BlockNetherOres extends Block
{	
	private static int aggroRange = 32;
	
	public BlockNetherOres(int i, int j)
	{
		super(i, j, Block.netherrack.blockMaterial);
		setHardness(5.0F);
		setResistance(1.0F);
		setBlockName("blockNetherOres");
		setStepSound(soundStoneFootstep);
		setRequiresSelfNotify();
	}
	
	public int getBlockTextureFromSideAndMetadata(int i, int j)
	{
		return j;
	}
	
	@Override
	public int damageDropped(int i)
	{
		return i;
	}
	
	@Override
	public int quantityDropped(Random random)
	{
		return 1;
	}
	
	@Override
	public void harvestBlock(World world, EntityPlayer entityplayer, int i, int j, int k, int l)
	{
		super.harvestBlock(world, entityplayer, i, j, k, l);
		if(NetherOresCore.enableAngryPigmen.getBoolean(true))
		{
			angerPigmen(entityplayer, world, i, j, k);
		}
	}
	
	@Override
	public boolean removeBlockByPlayer(World world, EntityPlayer player, int i, int j, int k)
	{
		checkExplosionChances(world, i, j, k);
		return super.removeBlockByPlayer(world, player, i, j, k);
	}
	
	@Override
	public void onBlockDestroyedByExplosion(World world, int i, int j, int k)
	{
		if(NetherOresCore.enableExplosionChainReactions.getBoolean(true))
		{
			checkExplosionChances(world, i, j, k);
		}
	}
	
	private void checkExplosionChances(World world, int x, int y, int z)
	{
		if(!world.isRemote && NetherOresCore.enableExplosions.getBoolean(true))
		{
			for(int xOffset = -1; xOffset <= 1; xOffset++)
			{
				for(int yOffset = -1; yOffset <= 1; yOffset++)
				{
					for(int zOffset = -1; zOffset <= 1; zOffset++)
					{
						int tx = x + xOffset;
						int ty = y + yOffset;
						int tz = z + zOffset;
						
						if(tx == x && ty == y && tz == z)
						{
							continue;
						}
						
						if(world.getBlockId(tx, ty, tz) == blockID && world.rand.nextInt(1000) < NetherOresCore.explosionProbability.getInt())
						{
							int meta = world.getBlockMetadata(tx, ty, tz);
							
							EntityArmedOre eao = new EntityArmedOre(world, tx + 0.5, ty + 0.5, tz + 0.5);
							eao.setMeta(meta);
							world.spawnEntityInWorld(eao);
							
							PacketDispatcher.sendPacketToAllAround(tx, ty, tz, 50, world.getWorldInfo().getDimension(), PacketWrapper.createPacket(NetherOresCore.modId, 0, new Object[] { tx, ty, tz, meta }));
							
							world.playSoundEffect(x + 0.5D, y + 0.5D, z + 0.5D, "random.fuse", 1.0F, 1.0F);
						}
					}
				}
			}
		}
	}
		
	

	@Override
	public String getTextureFile()
	{
		return NetherOresCore.terrainTexture;
	}
	
	private void angerPigmen(EntityPlayer player, World world, int x, int y, int z)
	{
		List<?> list = world.getEntitiesWithinAABB(EntityPigZombie.class,
				AxisAlignedBB.getBoundingBox(x - aggroRange, y - aggroRange, z - aggroRange, x + aggroRange + 1, y + aggroRange + 1, z + aggroRange + 1));
		for(int j = 0; j < list.size(); j++)
		{
			Entity entity1 = (Entity)list.get(j);
			if(entity1 instanceof EntityPigZombie)
			{
				EntityPigZombie entitypigzombie = (EntityPigZombie)entity1;
				entitypigzombie.attackEntityFrom(DamageSource.causePlayerDamage(player), 0);
			}
		}
	}
}
