package powercrystals.netherores.gui;

import powercrystals.netherores.NetherOresCore;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class NOCreativeTab extends CreativeTabs
{
	public static final NOCreativeTab tab = new NOCreativeTab("Nether Ores");

	public NOCreativeTab(String label)
	{
		super(label);
	}

	@Override
	public ItemStack getIconItemStack()
	{
		return new ItemStack(NetherOresCore.blockNetherOres0.blockID, 1, 1);
	}

	@Override
	public String getTranslatedTabLabel()
	{
		return this.getTabLabel();
	}
}
