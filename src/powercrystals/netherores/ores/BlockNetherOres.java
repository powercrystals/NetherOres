package powercrystals.netherores.ores;

import java.util.List;
import java.util.Random;

import cpw.mods.fml.common.network.PacketDispatcher;

import powercrystals.core.net.PacketWrapper;
import powercrystals.netherores.NetherOresCore;
import powercrystals.netherores.entity.EntityArmedOre;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class BlockNetherOres extends Block
{	
	private static int _aggroRange = 32;
	private Icon[] _netherOresIcons = new Icon[16];
	
	public BlockNetherOres(int blockId, int blockIndex)
	{
		super(blockId, Block.netherrack.blockMaterial);
		setHardness(5.0F);
		setResistance(1.0F);
		setUnlocalizedName("blockNetherOres" + blockIndex);
		setStepSound(soundStoneFootstep);
	}
	
	@Override
	public void func_94332_a(IconRegister ir)
	{
		for(int i = 0; i < 16; i++)
		{
			_netherOresIcons[i] = ir.func_94245_a("powercrystals/netherores/" + getUnlocalizedName() + "_" + i);
		}
	}
	
	@Override
	public Icon getBlockTextureFromSideAndMetadata(int side, int meta)
	{
		return _netherOresIcons[meta];
	}
	
	@Override
	public int damageDropped(int meta)
	{
		return meta;
	}
	
	@Override
	public int quantityDropped(Random random)
	{
		return 1;
	}
	
	@Override
	public void harvestBlock(World world, EntityPlayer entityplayer, int x, int y, int z, int fortune)
	{
		super.harvestBlock(world, entityplayer, x, y, z, fortune);
		if(NetherOresCore.enableAngryPigmen.getBoolean(true))
		{
			angerPigmen(entityplayer, world, x, y, z);
		}
	}
	
	@Override
	public boolean removeBlockByPlayer(World world, EntityPlayer player, int x, int y, int z)
	{
		checkExplosionChances(world, x, y, z);
		return super.removeBlockByPlayer(world, player, x, y, z);
	}
	
	@Override
	public void onBlockDestroyedByExplosion(World world, int x, int y, int z, Explosion explosion)
	{
		if(NetherOresCore.enableExplosionChainReactions.getBoolean(true))
		{
			checkExplosionChances(world, x, y, z);
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
	
	private void angerPigmen(EntityPlayer player, World world, int x, int y, int z)
	{
		List<?> list = world.getEntitiesWithinAABB(EntityPigZombie.class,
				AxisAlignedBB.getBoundingBox(x - _aggroRange, y - _aggroRange, z - _aggroRange, x + _aggroRange + 1, y + _aggroRange + 1, z + _aggroRange + 1));
		for(int j = 0; j < list.size(); j++)
		{
			Entity entity1 = (Entity)list.get(j);
			if(entity1 instanceof EntityPigZombie)
			{
				EntityPigZombie entitypigzombie = (EntityPigZombie)entity1;
				entitypigzombie.becomeAngryAt(player);
			}
		}
	}
}
