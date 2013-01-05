package powercrystals.netherores.net;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.MinecraftForgeClient;
import powercrystals.netherores.NetherOresCore;
import powercrystals.netherores.ores.EntityArmedOre;
import powercrystals.netherores.render.RendererArmedOre;

public class ClientProxy implements INetherOresProxy
{
	@Override
	public void load()
	{
        MinecraftForgeClient.preloadTexture(NetherOresCore.terrainTexture);

        LanguageRegistry.addName(new ItemStack(NetherOresCore.blockNetherOres, 1, 0), "Nether Coal");
        LanguageRegistry.addName(new ItemStack(NetherOresCore.blockNetherOres, 1, 1), "Nether Diamond");
        LanguageRegistry.addName(new ItemStack(NetherOresCore.blockNetherOres, 1, 2), "Nether Gold Ore");
        LanguageRegistry.addName(new ItemStack(NetherOresCore.blockNetherOres, 1, 3), "Nether Iron Ore");
        LanguageRegistry.addName(new ItemStack(NetherOresCore.blockNetherOres, 1, 4), "Nether Lapis Lazuli");
        LanguageRegistry.addName(new ItemStack(NetherOresCore.blockNetherOres, 1, 5), "Nether Redstone Ore");
        LanguageRegistry.addName(new ItemStack(NetherOresCore.blockNetherOres, 1, 6), "Nether Copper Ore");
        LanguageRegistry.addName(new ItemStack(NetherOresCore.blockNetherOres, 1, 7), "Nether Tin Ore");
        LanguageRegistry.addName(new ItemStack(NetherOresCore.blockNetherOres, 1, 8), "Nether Emerald Ore");
        LanguageRegistry.addName(new ItemStack(NetherOresCore.blockNetherOres, 1, 9), "Nether Silver Ore");
        LanguageRegistry.addName(new ItemStack(NetherOresCore.blockNetherOres, 1, 10), "Nether Lead Ore");
        LanguageRegistry.addName(new ItemStack(NetherOresCore.blockNetherOres, 1, 11), "Nether Uranium Ore");
        LanguageRegistry.addName(new ItemStack(NetherOresCore.blockNetherOres, 1, 12), "Nether Nikolite Ore");
        
        RenderingRegistry.registerEntityRenderingHandler(EntityArmedOre.class, new RendererArmedOre());
	}
}
