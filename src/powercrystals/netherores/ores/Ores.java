package powercrystals.netherores.ores;

import cpw.mods.fml.common.Loader;
import ic2.api.Ic2Recipes;
import powercrystals.netherores.NetherOresCore;
import thermalexpansion.api.core.ItemRegistry;
import thermalexpansion.api.crafting.CraftingManagers;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;

public enum Ores
{
	coal(0, 0, "Coal", 8, 16, 2, 4),
	diamond(0, 1, "Diamond", 4, 3, 2, 4),
	gold(0, 2, "Gold", 8, 6, 2, 4),
	iron(0, 3, "Iron", 8, 8, 2, 4),
	lapis(0, 4, "Lapis", 6, 6, 2, 24),
	redstone(0, 5, "Redstone", 6, 8, 2, 24),
	copper(0, 6, "Copper", 8, 8, 2, 4),
	tin(0, 7, "Tin", 8, 8, 2, 4),
	emerald(0, 8, "Emerald", 3, 2, 2, 4),
	silver(0, 9, "Silver", 6, 4, 2, 4),
	lead(0, 10, "Lead", 6, 6, 2, 4),
	uranium(0, 11, "Uranium", 3, 2, 2, 4),
	nikolite(0, 12, "Nikolite", 8, 4, 2, 24),
	ruby(0, 13, "Ruby", 6, 3, 2, 4),
	rpemerald(0, 14, "GreenSapphire", 6, 3, 2, 4),
	sapphire(0, 15, "Sapphire", 6, 3, 2, 4),
	
	platinum(1, 0, "Platinum", 3, 3, 2, 4),
	ferrous(1, 1, "Ferrous", 4, 6, 2, 4),
	pigiron(1, 2, "Steel", 3, 4, 2, 4),
	iridium(1, 3, "Iridium", 1, 2, 2, 4),
	osmium(1, 0, "Osmium", 8, 7, 2, 4);
	
	private int _blockIndex;
	private int _metadata;
	private String _oreName;
	private String _netherOreName;
	private String _dustName;
	private boolean _registeredSmelting;
	private boolean _registeredMacerator;
	private int _oreGenMinY = 1;
	private int _oreGenMaxY = 126;
	private int _oreGenGroupsPerChunk = 6;
	private int _oreGenBlocksPerGroup = 14;
	private boolean _oreGenDisable = false;
	private int _smeltCount;
	private int _maceCount;
	
	private Ores(int blockIndex, int meta, String oreSuffix, int groupsPerChunk, int blocksPerGroup, int smeltCount, int maceCount)
	{
		_blockIndex = blockIndex;
		_metadata = meta;
		_oreName = "ore" + oreSuffix;
		_dustName = "dust" + oreSuffix;
		_netherOreName = "oreNether" + oreSuffix;
		_oreGenGroupsPerChunk = groupsPerChunk;
		_oreGenBlocksPerGroup = blocksPerGroup;
		_smeltCount = smeltCount;
		_maceCount = maceCount; 
	}
	
	public int getBlockIndex()
	{
		return _blockIndex;
	}
	
	public int getMetadata()
	{
		return _metadata;
	}
	
	public String getOreName()
	{
		return _oreName;
	}
	
	public String getDustName()
	{
		return _dustName;
	}
	
	public boolean isRegisteredSmelting()
	{
		return _registeredSmelting;
	}
	
	public boolean isRegisteredMacerator()
	{
		return _registeredMacerator;
	}
	
	public int getMaxY()
	{
		return _oreGenMaxY;
	}
	
	public int getMinY()
	{
		return _oreGenMinY;
	}
	
	public int getGroupsPerChunk()
	{
		return _oreGenGroupsPerChunk;
	}
	
	public int getBlocksPerGroup()
	{
		return _oreGenBlocksPerGroup;
	}
	
	public boolean getDisabled()
	{
		return _oreGenDisable;
	}
	
	public void load()
	{
		MinecraftForge.setBlockHarvestLevel(NetherOresCore.getOreBlock(_blockIndex), _metadata, "pickaxe", 2);
		ItemStack oreStack = new ItemStack(NetherOresCore.getOreBlock(_blockIndex), 1, _metadata);
		OreDictionary.registerOre(_netherOreName, oreStack);
	}
	
	public void registerSmelting(ItemStack smeltStack)
	{
		_registeredSmelting = true;
		if(NetherOresCore.enableStandardFurnaceRecipes.getBoolean(true))
		{
			ItemStack smeltTo = smeltStack.copy();
			smeltTo.stackSize = _smeltCount;
			FurnaceRecipes.smelting().addSmelting(NetherOresCore.getOreBlock(_blockIndex).blockID, _metadata, smeltTo, 1F);
		}
		
		if(NetherOresCore.enableInductionSmelterRecipes.getBoolean(true) && Loader.isModLoaded("ThermalExpansion|Factory"))
		{
			ItemStack smeltToReg = smeltStack.copy();
			ItemStack smeltToRich = smeltStack.copy();

			smeltToReg.stackSize += 1;
			smeltToRich.stackSize += 2;
		   
			CraftingManagers.smelterManager.addRecipe(320, new ItemStack(NetherOresCore.getOreBlock(_blockIndex), 1, _metadata), new ItemStack(Block.sand), smeltToReg, ItemRegistry.getItem("slagRich", 1), 10, false);
			CraftingManagers.smelterManager.addRecipe(400, new ItemStack(NetherOresCore.getOreBlock(_blockIndex), 1, _metadata), ItemRegistry.getItem("slagRich", 1), smeltToRich, ItemRegistry.getItem("slag", 1), 80, false);
		}
	}
	
	public void registerMacerator(ItemStack maceStack)
	{
		if(NetherOresCore.enableMaceratorRecipes.getBoolean(true) && Loader.isModLoaded("IC2"))
		{
			ItemStack maceTo = maceStack.copy();
			maceTo.stackSize = _maceCount;

			Ic2Recipes.addMaceratorRecipe(new ItemStack(NetherOresCore.getOreBlock(_blockIndex), 1, _metadata), maceTo.copy());
		}
		
		if(NetherOresCore.enablePulverizerRecipes.getBoolean(true) && Loader.isModLoaded("ThermalExpansion|Factory"))
		{
			ItemStack pulvPriTo = maceStack.copy();
			ItemStack pulvSecTo = new ItemStack(Block.netherrack);
		   
			pulvPriTo.stackSize = _maceCount;
			pulvSecTo.stackSize = 1;
		   
			CraftingManagers.pulverizerManager.addRecipe(400, new ItemStack(NetherOresCore.getOreBlock(_blockIndex), 1, _metadata), pulvPriTo, pulvSecTo, 15, false);
		}
	}
	
	public void loadConfig(Configuration c)
	{
		_oreGenMaxY = c.get("WorldGen", _oreName + "MaxY", _oreGenMaxY).getInt();
		_oreGenMinY = c.get("WorldGen", _oreName + "MinY", _oreGenMinY).getInt();
		_oreGenGroupsPerChunk = c.get("WorldGen", _oreName + "GroupsPerChunk", _oreGenGroupsPerChunk).getInt();
		_oreGenBlocksPerGroup = c.get("WorldGen", _oreName + "BlocksPerGroup", _oreGenBlocksPerGroup).getInt();
		_oreGenDisable = c.get("WorldGen", _oreName + "Disable", false).getBoolean(false);
		
		if(_oreGenMinY >= _oreGenMaxY)
		{
			_oreGenMinY = _oreGenMaxY - 1;
		}
	}
}
