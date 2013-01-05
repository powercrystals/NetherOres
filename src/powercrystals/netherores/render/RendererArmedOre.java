package powercrystals.netherores.render;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;

import powercrystals.netherores.ores.EntityArmedOre;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RendererArmedOre extends Render
{
	public RendererArmedOre()
	{
		shadowSize = 0.0F;
	}

	public void renderArmedOre(EntityArmedOre ore, double f1, double f2, double f3, float par8, float par9)
	{
	}

	public void doRender(Entity entity, double x, double y, double z, float par8, float par9)
	{
		renderArmedOre((EntityArmedOre)entity, x, y, z, par8, par9);
	}
}
