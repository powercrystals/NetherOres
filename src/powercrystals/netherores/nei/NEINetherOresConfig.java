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
        int blockID = NetherOresCore.blockNetherOres.blockID;
        for (int i = 0; i <= 15; i++)
        {
            subTypes.add(blockID, i, i);
        }
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
