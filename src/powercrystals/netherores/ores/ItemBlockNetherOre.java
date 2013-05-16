package powercrystals.netherores.ores;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockNetherOre extends ItemBlock
{
	public ItemBlockNetherOre(int id)
	{
		super(id);
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
		int index = ((BlockNetherOres)Block.blocksList[getBlockID()]).getBlockIndex();
		Ores[] ores = Ores.values();
		int md = Math.min(index * 16 + stack.getItemDamage(), ores.length - 1);
		return "tile.netherores.ore." + ores[md].name();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void getSubItems(int itemId, CreativeTabs creativeTab, List subTypes)
	{
		int index = ((BlockNetherOres)Block.blocksList[getBlockID()]).getBlockIndex();
		Ores[] ores = Ores.values();
		for (int i = 0, e = Math.min(index * 16 + 15, ores.length - 1) % 16; i <= e; ++i)
		{
			subTypes.add(new ItemStack(itemId, 1, i));
		}
	}
}
