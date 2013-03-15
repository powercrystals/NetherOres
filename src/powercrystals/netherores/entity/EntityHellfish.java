package powercrystals.netherores.entity;

import powercrystals.netherores.NetherOresCore;
import net.minecraft.block.Block;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.util.Facing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityHellfish extends EntitySilverfish
{
	public EntityHellfish(World world)
	{
		super(world);
		this.texture = NetherOresCore.mobTexureFolder + "hellfish.png";
		this.moveSpeed = 0.9F;
	}
	
	@Override
	protected void updateEntityActionState()
	{
		super.updateEntityActionState();

		if(!this.worldObj.isRemote)
		{
			int positionX;
			int positionY;
			int positionZ;

			if(this.allySummonCooldown > 0)
			{
				--this.allySummonCooldown;

				if(this.allySummonCooldown == 0)
				{
					positionX = MathHelper.floor_double(this.posX);
					positionY = MathHelper.floor_double(this.posY);
					positionZ = MathHelper.floor_double(this.posZ);
					boolean stopSpawning = false;

					for(int x = 0; !stopSpawning && x <= 5 && x >= -5; x = x <= 0 ? 1 - x : 0 - x)
					{
						for(int y = 0; !stopSpawning && y <= 10 && y >= -10; y = y <= 0 ? 1 - y : 0 - y)
						{
							for(int z = 0; !stopSpawning && z <= 10 && z >= -10; z = z <= 0 ? 1 - z : 0 - z)
							{
								int blockId = this.worldObj.getBlockId(positionX + y, positionY + x, positionZ + z);

								if(blockId == NetherOresCore.blockHellfish.blockID)
								{
									this.worldObj.playAuxSFX(2001, positionX + y, positionY + x, positionZ + z, NetherOresCore.blockHellfish.blockID + (this.worldObj.getBlockMetadata(positionX + y, positionY + x, positionZ + z) << 12));
									this.worldObj.setBlockAndMetadataWithNotify(positionX + y, positionY + x, positionZ + z, 0, 0, 0);
									NetherOresCore.blockHellfish.onBlockDestroyedByPlayer(this.worldObj, positionX + y, positionY + x, positionZ + z, 0);

									if(this.rand.nextBoolean())
									{
										stopSpawning = true;
										break;
									}
								}
							}
						}
					}
				}
			}

			if(this.entityToAttack == null && !this.hasPath())
			{
				positionX = MathHelper.floor_double(this.posX);
				positionY = MathHelper.floor_double(this.posY + 0.5D);
				positionZ = MathHelper.floor_double(this.posZ);
				int direction = this.rand.nextInt(6);
				int blockId = this.worldObj.getBlockId(positionX + Facing.offsetsXForSide[direction], positionY + Facing.offsetsYForSide[direction], positionZ + Facing.offsetsZForSide[direction]);

				if(blockId == Block.netherrack.blockID && this.rand.nextInt(3) == 0)
				{
					this.worldObj.setBlockAndMetadataWithNotify(positionX + Facing.offsetsXForSide[direction], positionY + Facing.offsetsYForSide[direction], positionZ + Facing.offsetsZForSide[direction], NetherOresCore.blockHellfish.blockID, 0, 0);
					this.spawnExplosionParticle();
					this.setDead();
				}
				else
				{
					this.updateWanderPath();
				}
			}
			else if (this.entityToAttack != null && !this.hasPath())
			{
				this.entityToAttack = null;
			}
		}
	}
}
