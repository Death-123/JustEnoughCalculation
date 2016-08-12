package pers.towdium.just_enough_calculation;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pers.towdium.just_enough_calculation.item.ItemCalculator;
import pers.towdium.just_enough_calculation.item.ItemFluidContainer;
import pers.towdium.just_enough_calculation.network.IProxy;
import pers.towdium.just_enough_calculation.network.packets.PacketRecordModify;
import pers.towdium.just_enough_calculation.network.packets.PacketRecordSync;

/**
 * @author Towdium
 */
@Mod(modid = JustEnoughCalculation.Reference.MODID, name = JustEnoughCalculation.Reference.MODNAME, version = JustEnoughCalculation.Reference.VERSION,
        dependencies = "required-after:JEI")
public class JustEnoughCalculation {
    @Mod.Instance(JustEnoughCalculation.Reference.MODID)
    public static JustEnoughCalculation instance;
    @SidedProxy(clientSide = "pers.towdium.just_enough_calculation.network.ProxyClient", serverSide = "pers.towdium.just_enough_calculation.network.ProxyServer")
    public static IProxy proxy;
    public static SimpleNetworkWrapper networkWrapper;
    public static Logger log = LogManager.getLogger(Reference.MODID);

    public static Item itemCalculator = new ItemCalculator().setUnlocalizedName("itemCalculator").setRegistryName("itemCalculator");
    public static Item itemFluidContainer = new ItemFluidContainer().setUnlocalizedName("itemFluidContainer").setRegistryName("itemFluidContainer");

    @Mod.EventHandler
    public static void preInit(FMLPreInitializationEvent event) {
        JECConfig.preInit(event);
        GameRegistry.register(itemCalculator);
        GameRegistry.register(itemFluidContainer);
        networkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MODID);
        networkWrapper.registerMessage(PacketRecordModify.class, PacketRecordModify.class, 0, Side.SERVER);
        networkWrapper.registerMessage(PacketRecordSync.class, PacketRecordSync.class, 1, Side.CLIENT);
        proxy.preInit();
    }

    @Mod.EventHandler
    public static void init(FMLInitializationEvent event) {
        proxy.init();
    }

    @Mod.EventHandler
    public void onServerStart(FMLServerStartingEvent event) {
        event.registerServerCommand(new JECCommand());
    }

    public static class Reference {
        public static final String MODID = "je_calculation";
        public static final String MODNAME = "Just Enough Calculation";
        public static final String VERSION = "1.9.4-0.0.1";
    }
}
