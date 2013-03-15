package powercrystals.netherores.ores;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemNetherOre extends ItemBlock
{
	public ItemNetherOre(int i)
	{
		super(i);
		setHasSubtypes(true);
		setMaxDamage(0);
	}
	
	@Override
	public int getMetadata(int i)
	{
		return i;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
	    for (int i = 0; i <= 15; i++)
	    {
	        par3List.add(new ItemStack(par1, 1, i));
	    }
    }
}
