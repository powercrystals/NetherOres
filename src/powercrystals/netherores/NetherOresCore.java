package powercrystals.netherores;

import java.io.File;

import powercrystals.core.updater.IUpdateableMod;
import powercrystals.core.updater.UpdateManager;
import powercrystals.netherores.net.ClientPacketHandler;
import powercrystals.netherores.net.ConnectionHandler;
import powercrystals.netherores.net.INetherOresProxy;
import powercrystals.netherores.net.ServerPacketHandler;
import powercrystals.netherores.ores.BlockNetherOres;
import powercrystals.netherores.ores.EntityArmedOre;
import powercrystals.netherores.ores.ItemNetherOre;
import powercrystals.netherores.ores.Ores;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.Property;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreDictionary.OreRegisterEvent;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkMod.SidedPacketHandler;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

@Mod(modid = NetherOresCore.modId, name = NetherOresCore.modName, version = NetherOresCore.version, dependencies = "required-after:PowerCrystalsCore;after:IC2;after:ThermalExpansion|Factory")
@NetworkMod(clientSideRequired = true, serverSideRequired = false,
clientPacketHandlerSpec = @SidedPacketHandler(channels = { NetherOresCore.modId }, packetHandler = ClientPacketHandler.class),
serverPacketHandlerSpec = @SidedPacketHandler(channels = { NetherOresCore.modId }, packetHandler = ServerPacketHandler.class),
connectionHandler = ConnectionHandler.class)
public class NetherOresCore implements IUpdateableMod
{
	public static final String modId = "NetherOres";
	public static final String version = "1.4.6R2.0.5B1";
	public static final String modName = "Nether Ores";
	
	public static final String terrainTexture = "/powercrystals/netherores/textures/terrain_0.png";

	public static Block blockNetherOres;
	private static Property netherOreBlockId;

	public static Property explosionPower;
	public static Property explosionProbability;
	public static Property enableExplosions;
	public static Property enableExplosionChainReactions;
	public static Property enableAngryPigmen;
	public static Property enableStandardFurnaceRecipes;
	public static Property enableMaceratorRecipes;
	public static Property forceOreSpawn;
	
	@SidedProxy(clientSide = "powercrystals.netherores.net.ClientProxy", serverSide="powercrystals.netherores.net.ServerProxy")
	public static INetherOresProxy proxy;

	@PreInit
	public void preInit(FMLPreInitializationEvent evt)
	{
		loadConfig(evt.getSuggestedConfigurationFile());
		blockNetherOres = new BlockNetherOres(Integer.parseInt(netherOreBlockId.value), 0);
	}

	@Init
	public void load(FMLInitializationEvent evt)
	{
		GameRegistry.registerBlock(blockNetherOres, ItemNetherOre.class, "netherOresBlockOres");
		GameRegistry.registerWorldGenerator(new NetherOresWorldGenHandler());
		
		for(Ores o : Ores.values())
		{
			o.load();
		}
		
		EntityRegistry.registerModEntity(EntityArmedOre.class, "netherOresArmedOre", 0, this, 160, 5, true);
		
		proxy.load();

		TickRegistry.registerScheduledTickHandler(new UpdateManager(this), Side.CLIENT);
	}
	
	@PostInit
	public void postInit(FMLPostInitializationEvent e)
	{
		if(enableStandardFurnaceRecipes.getBoolean(true))
		{
			Ores.coal.registerSmelting(new ItemStack(Block.oreCoal));
			Ores.diamond.registerSmelting(new ItemStack(Block.oreDiamond));
			Ores.gold.registerSmelting(new ItemStack(Block.oreGold));
			Ores.iron.registerSmelting(new ItemStack(Block.oreIron));
			Ores.lapis.registerSmelting(new ItemStack(Block.oreLapis));
			Ores.redstone.registerSmelting(new ItemStack(Block.oreRedstone));
			Ores.emerald.registerSmelting(new ItemStack(Block.oreEmerald));
		}
		if(enableMaceratorRecipes.getBoolean(true))
		{
			Ores.diamond.registerMacerator(new ItemStack(Item.diamond));
			Ores.coal.registerMacerator(new ItemStack(Item.coal));
			Ores.redstone.registerMacerator(new ItemStack(Item.redstone));
			Ores.lapis.registerMacerator(new ItemStack(Item.dyePowder, 1, 4));
		}
		
		for(String oreName : OreDictionary.getOreNames())
		{
			if(OreDictionary.getOres(oreName).size() > 0)
			{
				registerOreDictionaryEntry(oreName, OreDictionary.getOres(oreName).get(0));
			}
		}
		
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	private void registerOreDictionaryEntry(String oreName, ItemStack stack)
	{
		for(Ores ore : Ores.values())
		{
			if(!ore.isRegisteredSmelting() && ore.getOreName().trim().toLowerCase().equals(oreName.trim().toLowerCase()))
			{
				ore.registerSmelting(stack);
			}
			if(!ore.isRegisteredMacerator() && ore.getDustName().trim().toLowerCase().equals(oreName.trim().toLowerCase()))
			{
				ore.registerMacerator(stack);
			}
		}
	}

	private void loadConfig(File f)
	{
		Configuration c = new Configuration(f);
		c.load();

		netherOreBlockId = c.getBlock(Configuration.CATEGORY_BLOCK, "ID.NetherOreBlock", 1440);
		explosionPower = c.get(Configuration.CATEGORY_GENERAL, "ExplosionPower", 2);
		explosionPower.comment = "How powerful an explosion will be. Creepers are 3, TNT is 4, electrified creepers are 6. This affects both the ability of the explosion to punch through blocks as well as the blast radius.";
		explosionProbability = c.get(Configuration.CATEGORY_GENERAL, "ExplosionProbability", 75);
		explosionProbability.comment = "The likelyhood an adjacent netherore will turn into an armed ore when one is mined. Percent chance out of 1000 (lower is less likely).";
		enableExplosions = c.get(Configuration.CATEGORY_GENERAL, "ExplosionEnable", true);
		enableExplosions.comment = "NetherOres have a chance to explode when mined if true.";
		enableExplosionChainReactions = c.get(Configuration.CATEGORY_GENERAL, "ExplosionChainReactEnable", true);
		enableExplosionChainReactions.comment = "NetherOre explosions can trigger more explosions if true. Does nothing if ExplosionEnable is false.";
		enableAngryPigmen = c.get(Configuration.CATEGORY_GENERAL, "AngryPigmenEnable", true);
		enableAngryPigmen.comment = "If true, when NetherOres are mined, nearby pigmen become angry to the player.";
		enableStandardFurnaceRecipes = c.get(Configuration.CATEGORY_GENERAL, "EnableStandardFurnaceRecipes", true);
		enableStandardFurnaceRecipes.comment = "Set this to false to remove the standard furnace recipes (ie, nether iron ore -> normal iron ore). Provided for compatibility with Metallurgy. If you set this to false and no other mod connects to this mod's ores, they will be useless.";
		enableMaceratorRecipes = c.get(Configuration.CATEGORY_GENERAL, "EnableMaceratorRecipes", true);
		enableMaceratorRecipes.comment = "Set this to false to remove the direct macerator recipes (ie, nether iron ore -> 4x iron dust). Provided for compatibility with Metallurgy. If you set this to false and no other mod connects to this mod's ores, they will be useless.";
		forceOreSpawn = c.get(Configuration.CATEGORY_GENERAL, "ForceOreSpawn", false);
		forceOreSpawn.comment = "If true, will spawn nether ores regardless of if a furnace or macerator recipe was found. If false, at least one of those two must be found to spawn the ore.";

		for(Ores o : Ores.values())
		{
			o.loadConfig(c);
		}
		
		c.save();
	}

	@ForgeSubscribe
	public void registerOreEvent(OreRegisterEvent event)
	{
		registerOreDictionaryEntry(event.Name, event.Ore);
	}

	@Override
	public String getModId()
	{
		return modId;
	}

	@Override
	public String getModName()
	{
		return modName;
	}

	@Override
	public String getModFolder()
	{
		return modId;
	}

	@Override
	public String getModVersion()
	{
		return version;
	}
}
