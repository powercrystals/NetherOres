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
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.Property;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreDictionary.OreRegisterEvent;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkMod.SidedPacketHandler;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

@Mod(modid = NetherOresCore.modId, name = NetherOresCore.modName, version = NetherOresCore.version, dependencies = "after:IC2")
@NetworkMod(clientSideRequired = true, serverSideRequired = false,
clientPacketHandlerSpec = @SidedPacketHandler(channels = { NetherOresCore.modId }, packetHandler = ClientPacketHandler.class),
serverPacketHandlerSpec = @SidedPacketHandler(channels = { NetherOresCore.modId }, packetHandler = ServerPacketHandler.class),
connectionHandler = ConnectionHandler.class)
public class NetherOresCore implements IUpdateableMod
{
	public static final String modId = "NetherOres";
	public static final String version = "1.4.6R2.0.0";
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

		Ores.coal.register(new ItemStack(Block.oreCoal));
		Ores.diamond.register(new ItemStack(Block.oreDiamond));
		Ores.gold.register(new ItemStack(Block.oreGold));
		Ores.iron.register(new ItemStack(Block.oreIron));
		Ores.lapis.register(new ItemStack(Block.oreLapis));
		Ores.redstone.register(new ItemStack(Block.oreRedstone));
		Ores.emerald.register(new ItemStack(Block.oreEmerald));
		
		EntityRegistry.registerModEntity(EntityArmedOre.class, "netherOresArmedOre", 0, this, 160, 5, true);
		
		for(String oreName : OreDictionary.getOreNames())
		{
			for(ItemStack stack : OreDictionary.getOres(oreName))
			{
				for(Ores ore : Ores.values())
				{
					if(!ore.isRegistered() && ore.getOreName() == oreName)
					{
						ore.register(stack);
					}
				}
			}
		}
		
		proxy.load();

		TickRegistry.registerScheduledTickHandler(new UpdateManager(this), Side.CLIENT);
		
		MinecraftForge.EVENT_BUS.register(this);
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

		for(Ores o : Ores.values())
		{
			o.loadConfig(c);
		}
		
		c.save();
	}

	@ForgeSubscribe
	public void registerOreEvent(OreRegisterEvent event)
	{
		for(Ores o : Ores.values())
		{
			if(event.Name.equals(o.getOreName()) && !o.isRegistered())
			{
				o.register(event.Ore);
			}
		}
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
