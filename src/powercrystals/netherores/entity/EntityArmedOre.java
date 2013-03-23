package powercrystals.netherores.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import powercrystals.netherores.NetherOresCore;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityArmedOre extends Entity
{
	private int _fuse;
	
	public EntityArmedOre(World world)
	{
		super(world);
		_fuse = 0;
		preventEntitySpawning = true;
		setSize(0.0F, 0.0F);
		yOffset = height / 2.0F;
	}
	
	public EntityArmedOre(World world, double x, double y, double z)
	{
		this(world);
		setPosition(x, y, z);
		motionX = 0.0F;
		motionY = 0.0F;
		motionZ = 0.0F;
		_fuse = 80;
		prevPosX = x;
		prevPosY = y;
		prevPosZ = z;
	}

	@Override
	protected void entityInit() {}

	@Override
	protected boolean canTriggerWalking()
	{
		return false;
	}

	@Override
	public boolean canBeCollidedWith()
	{
		return !this.isDead;
	}
	
	@Override
	public void onUpdate()
	{
		if(_fuse-- <= 0)
		{
			setDead();

			if(!worldObj.isRemote)
			{
				explode();
			}
		}
		else
		{
			worldObj.spawnParticle("smoke", this.posX, this.posY + 0.5D, this.posZ, 0.0D, 0.0D, 0.0D);
		}
	}

	private void explode()
	{
		int blockId = worldObj.getBlockId(MathHelper.floor_double(posX), MathHelper.floor_double(posY), MathHelper.floor_double(posZ));
		if(blockId == NetherOresCore.blockNetherOres0.blockID || blockId == NetherOresCore.blockNetherOres1.blockID)
		{
			worldObj.newExplosion(null, this.posX, this.posY, this.posZ, NetherOresCore.explosionPower.getInt(), true, true);
		}
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
	{
		par1NBTTagCompound.setByte("Fuse", (byte)_fuse);
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		_fuse = par1NBTTagCompound.getByte("Fuse");
	}

	@SideOnly(Side.CLIENT)
	@Override
	public float getShadowSize()
	{
		return 0.0F;
	}
}
