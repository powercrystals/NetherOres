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
	coal(0, "Coal", 8, 16, 1, 4),
	diamond(1, "Diamond", 4, 3, 1, 4),
	gold(2, "Gold", 8, 6, 1, 4),
	iron(3, "Iron", 8, 8, 1, 4),
	lapis(4, "Lapis", 6, 6, 1, 24),
	redstone(5, "Redstone", 6, 8, 1, 24),
	copper(6, "Copper", 8, 8, 1, 4),
	tin(7, "Tin", 8, 8, 1, 4),
	emerald(8, "Emerald", 3, 2, 1, 4),
	silver(9, "Silver", 6, 4, 1, 4),
	lead(10, "Lead", 6, 6, 1, 4),
	uranium(11, "Uranium", 3, 2, 1, 4),
	nikolite(12, "Nikolite", 8, 4, 1, 24),
	ruby(13, "Ruby", 6, 3, 1, 4),
	rpemerald(14, "GreenSapphire", 6, 3, 1, 4),
	sapphire(15, "Sapphire", 6, 3, 1, 4);
	
	private int _metadata;
	private String _oreName;
	private String _netherOreName;
	private String _dustName;
	private boolean _registeredSmelting;
	private boolean _registeredMacerator;
	private int _oreGenMaxY = 128;
	private int _oreGenGroupsPerChunk = 6;
	private int _oreGenBlocksPerGroup = 14;
	private int _smeltCount;
	private int _maceCount;
	
	private Ores(int meta, String oreSuffix, int groupsPerChunk, int blocksPerGroup, int smeltCount, int maceCount)
	{
		_metadata = meta;
		_oreName = "ore" + oreSuffix;
		_dustName = "dust" + oreSuffix;
		_netherOreName = "oreNether" + oreSuffix;
		_oreGenGroupsPerChunk = groupsPerChunk;
		_oreGenBlocksPerGroup = blocksPerGroup;
		_smeltCount = smeltCount;
		_maceCount = maceCount; 
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
	
	public int getGroupsPerChunk()
	{
		return _oreGenGroupsPerChunk;
	}
	
	public int getBlocksPerGroup()
	{
		return _oreGenBlocksPerGroup;
	}
	
	public void load()
	{
		MinecraftForge.setBlockHarvestLevel(NetherOresCore.blockNetherOres, _metadata, "pickaxe", 2);
		ItemStack oreStack = new ItemStack(NetherOresCore.blockNetherOres, 1, _metadata);
		OreDictionary.registerOre(_netherOreName, oreStack);
	}
	
	public void registerSmelting(ItemStack smeltStack)
	{
		_registeredSmelting = true;
		if(NetherOresCore.enableStandardFurnaceRecipes.getBoolean(true))
		{
			ItemStack smeltTo = smeltStack.copy();
			smeltTo.stackSize = _smeltCount;
			FurnaceRecipes.smelting().addSmelting(NetherOresCore.blockNetherOres.blockID, _metadata, smeltTo, 1F);
			if(Loader.isModLoaded("ThermalExpansion|Factory"))
			{
				ItemStack smeltToReg = smeltStack.copy();
				ItemStack smeltToRich = smeltStack.copy();

				smeltToReg.stackSize += 1;
				smeltToRich.stackSize += 2;
			   
				CraftingManagers.smelterManager.addRecipe(320, new ItemStack(NetherOresCore.blockNetherOres, 1, _metadata), new ItemStack(Block.sand), smeltToReg, ItemRegistry.getItem("slagRich", 1), 10, false);
				CraftingManagers.smelterManager.addRecipe(400, new ItemStack(NetherOresCore.blockNetherOres, 1, _metadata), ItemRegistry.getItem("slagRich", 1), smeltToRich, ItemRegistry.getItem("slag", 1), 80, false);
			}
		}
	}
	
	public void registerMacerator(ItemStack maceStack)
	{
		if(NetherOresCore.enableMaceratorRecipes.getBoolean(true))
		{
			ItemStack maceTo = maceStack.copy();
			maceTo.stackSize = _maceCount;
			if(Loader.isModLoaded("IC2"))
			{
				Ic2Recipes.addMaceratorRecipe(new ItemStack(NetherOresCore.blockNetherOres, 1, _metadata), maceTo.copy());
			}
			if(Loader.isModLoaded("ThermalExpansion|Factory"))
			{
				ItemStack pulvPriTo = maceStack.copy();
				ItemStack pulvSecTo = new ItemStack(Block.netherrack);
			   
				pulvPriTo.stackSize = _maceCount;
				pulvSecTo.stackSize = 1;
			   
				CraftingManagers.pulverizerManager.addRecipe(400, new ItemStack(NetherOresCore.blockNetherOres, 1, _metadata), pulvPriTo, pulvSecTo, 15, false);
			}
		}
	}
	
	public void loadConfig(Configuration c)
	{
		_oreGenMaxY = c.get("WorldGen", _oreName + "MaxY", _oreGenMaxY).getInt();
		_oreGenGroupsPerChunk = c.get("WorldGen", _oreName + "GroupsPerChunk", _oreGenGroupsPerChunk).getInt();
		_oreGenBlocksPerGroup = c.get("WorldGen", _oreName + "BlocksPerGroup", _oreGenBlocksPerGroup).getInt();
	}
}
