package powercrystals.netherores.nei;

import powercrystals.netherores.NetherOresCore;
import codechicken.nei.MultiItemRange;
import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;

public class NEINetherOresConfig implements IConfigureNEI
{

	@Override
	public void loadConfig()
	{
		MultiItemRange subTypes = new MultiItemRange();

		subTypes.add(NetherOresCore.blockNetherOres0, 0, 15);
		subTypes.add(NetherOresCore.blockNetherOres1, 0, 3);
		
		API.addSetRange("NetherOres", subTypes);
	}

	@Override
	public String getName()
	{
		return NetherOresCore.modName;
	}

	@Override
	public String getVersion()
	{
		return NetherOresCore.version;
	}

}
