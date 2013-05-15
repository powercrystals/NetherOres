package powercrystals.netherores.ores;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockNetherOre extends ItemBlock
{
	private String[][] _oreNames =
		{
			{ "coal", "diamond", "gold", "iron", "lapis", "redstone", "copper", "tin", "emerald", "silver", "lead", "uranium", "nikolite", "ruby", "greensapphire", "sapphire" },
			{ "platinum", "ferrous", "pigiron", "iridium", "osmium" }
		};
	
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
		int md = Math.min(stack.getItemDamage(), _oreNames[index].length);
		return "tile.netherores.ore." + _oreNames[index][md];
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void getSubItems(int itemId, CreativeTabs creativeTab, List subTypes)
	{
		int index = ((BlockNetherOres)Block.blocksList[getBlockID()]).getBlockIndex();
		for (int i = 0; i < _oreNames[index].length; i++)
		{
			subTypes.add(new ItemStack(itemId, 1, i));
		}
	}
}
