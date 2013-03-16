package powercrystals.netherores.ores;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockNetherOre extends ItemBlock
{
	public ItemBlockNetherOre(int i)
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
	
	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return Block.blocksList[getBlockID()].getUnlocalizedName() + "_" + stack.getItemDamage();
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
