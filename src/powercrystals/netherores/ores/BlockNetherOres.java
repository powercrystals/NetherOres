package powercrystals.netherores.ores;

import java.util.List;
import java.util.Random;

import powercrystals.netherores.NetherOresCore;
import powercrystals.netherores.entity.EntityArmedOre;
import powercrystals.netherores.gui.NOCreativeTab;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.enchantment.EnchantmentHelper;
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
	private int _blockIndex = 0;
	private Icon[] _netherOresIcons = new Icon[16];
	
	public BlockNetherOres(int blockId, int blockIndex)
	{
		super(blockId, Block.netherrack.blockMaterial);
		setHardness(5.0F);
		setResistance(1.0F);
		setUnlocalizedName("netherores.ore." + blockIndex);
		setStepSound(soundStoneFootstep);
		setCreativeTab(NOCreativeTab.tab);
		_blockIndex = blockIndex;
	}
	
	public int getBlockIndex()
	{
		return _blockIndex;
	}
	
	@Override
	public void registerIcons(IconRegister ir)
	{
		Ores[] ores = Ores.values();
		for(int i = 0, e = Math.min(_blockIndex * 16 + 15, ores.length - 1) % 16; i <= e; i++)
		{
			_netherOresIcons[i] = ir.registerIcon("powercrystals/netherores/" + getUnlocalizedName() + "_" + i);
		}
	}
	
	@Override
	public Icon getIcon(int side, int meta)
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
		if(player == null || !EnchantmentHelper.getSilkTouchModifier(player))
		{
			checkExplosionChances(world, x, y, z);				
		}
		
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
							EntityArmedOre eao = new EntityArmedOre(world, tx + 0.5, ty + 0.5, tz + 0.5);
							world.spawnEntityInWorld(eao);
							
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
