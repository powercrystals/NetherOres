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
	
	@Override
	public int getIconFromDamage(int i)
	{
		return Math.min(i, 12);
	}
	
	@Override
	public String getItemNameIS(ItemStack itemstack)
	{
		int md = itemstack.getItemDamage();
		if(md == Ores.coal.getMetadata()) return "itemNetherCoal";
		if(md == Ores.diamond.getMetadata()) return "itemNetherDiamond";
		if(md == Ores.gold.getMetadata()) return "itemNetherGold";
		if(md == Ores.iron.getMetadata()) return "itemNetherIron";
		if(md == Ores.lapis.getMetadata()) return "itemNetherLapis";
		if(md == Ores.redstone.getMetadata()) return "itemNetherRedstone";
		if(md == Ores.copper.getMetadata()) return "itemNetherCopper";
		if(md == Ores.tin.getMetadata()) return "itemNetherTin";
		if(md == Ores.emerald.getMetadata()) return "itemNetherEmerald";
		if(md == Ores.silver.getMetadata()) return "itemNetherSilver";
		if(md == Ores.lead.getMetadata()) return "itemNetherLead";
		if(md == Ores.uranium.getMetadata()) return "itemNetherUranium";
		if(md == Ores.nikolite.getMetadata()) return "itemNetherNikolite";
		if(md == Ores.ruby.getMetadata()) return "itemNetherRuby";
		if(md == Ores.rpemerald.getMetadata()) return "itemNetherRPEmerald";
		if(md == Ores.sapphire.getMetadata()) return "itemNetherSapphire";
		
		return "itemNetherUnknown";
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
