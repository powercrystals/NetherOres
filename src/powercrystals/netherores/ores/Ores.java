package powercrystals.netherores.ores;

import powercrystals.netherores.NetherOresCore;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;

public enum Ores
{
	coal(0, "oreCoal", "oreNetherCoal", 8, 16),
	diamond(1, "oreDiamond", "oreNetherDiamond", 4, 3),
	gold(2, "oreGold", "oreNetherGold", 8, 6),
	iron(3, "oreIron", "oreNetherIron", 8, 8),
	lapis(4, "oreLapis", "oreNetherLapis", 6, 6),
	redstone(5, "oreRedstone", "oreNetherRedstone", 6, 8),
	copper(6, "oreCopper", "oreNetherCopper", 8, 8),
	tin(7, "oreTin", "oreNetherTin", 8, 8),
	emerald(8, "oreEmerald", "oreNetherEmerald", 3, 2),
	silver(9, "oreSilver", "oreNetherSilver", 6, 4),
	lead(10, "oreLead", "oreNetherLead", 6, 6),
	uranium(11, "oreUranium", "oreNetherUranium", 3, 2),
	nikolite(12, "oreNikolite", "oreNetherNikolite", 8, 4);
	
	private int _metadata;
	private String _oreName;
	private String _netherOreName;
	private ItemStack _smeltTo;
	private boolean _registered;
	private int _oreGenMaxY = 128;
	private int _oreGenGroupsPerChunk = 6;
	private int _oreGenBlocksPerGroup = 14;
	
	private Ores(int meta, String oreName, String netherOreName, int groupsPerChunk, int blocksPerGroup)
	{
		_metadata = meta;
		_oreName = oreName;
		_netherOreName = netherOreName;
		_oreGenGroupsPerChunk = groupsPerChunk;
		_oreGenBlocksPerGroup = blocksPerGroup;
	}
	
	public int getMetadata()
	{
		return _metadata;
	}
	
	public String getOreName()
	{
		return _oreName;
	}
	
	public boolean isRegistered()
	{
		return _registered;
	}
	
	public int getMaxY()
	{
		return _oreGenMaxY;
	}
	
	public int getGroupsPerChunk()
	{
		return _oreGenGroupsPerChunk;
	}
	
	public int getBlocksPerGroup()
	{
		return _oreGenBlocksPerGroup;
	}
	
	public void register(ItemStack smeltStack)
	{
		_registered = true;
		_smeltTo = smeltStack.copy();
		_smeltTo.stackSize = 1;
		MinecraftForge.setBlockHarvestLevel(NetherOresCore.blockNetherOres, _metadata, "pickaxe", 2);
		if(NetherOresCore.enableStandardFurnaceRecipes.getBoolean(true))
		{
			FurnaceRecipes.smelting().addSmelting(NetherOresCore.blockNetherOres.blockID, _metadata, _smeltTo, 1F);
		}

		ItemStack oreStack = new ItemStack(NetherOresCore.blockNetherOres, 1, _metadata);
		OreDictionary.registerOre(_netherOreName, oreStack);
	}
	
	public void loadConfig(Configuration c)
	{
		_oreGenMaxY = c.get("WorldGen", _oreName + "MaxY", _oreGenMaxY).getInt();
		_oreGenGroupsPerChunk = c.get("WorldGen", _oreName + "GroupsPerChunk", _oreGenGroupsPerChunk).getInt();
		_oreGenBlocksPerGroup = c.get("WorldGen", _oreName + "BlocksPerGroup", _oreGenBlocksPerGroup).getInt();
	}
}
