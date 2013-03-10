package powercrystals.netherores.net;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

import powercrystals.core.net.PacketWrapper;
import powercrystals.netherores.entity.EntityArmedOre;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public class ClientPacketHandler implements IPacketHandler
{
	@SuppressWarnings("rawtypes")
	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player)
	{
		DataInputStream data = new DataInputStream(new ByteArrayInputStream(packet.data));
		int packetType = PacketWrapper.readPacketID(data);
		
		if (packetType == 0) // server -> client; spawn armed entity: x y z meta
		{
			Class[] decodeAs = { Integer.class, Integer.class, Integer.class, Integer.class };
			Object[] packetReadout = PacketWrapper.readPacketData(data, decodeAs);
			
			EntityArmedOre e = new EntityArmedOre(((EntityPlayer)player).worldObj, (Integer)packetReadout[0] + 0.5, (Integer)packetReadout[1] + 0.5, (Integer)packetReadout[2] + 0.5);
			e.setMeta((Integer)packetReadout[3]);
			((EntityPlayer)player).worldObj.spawnEntityInWorld(e);
		}
	}
}
