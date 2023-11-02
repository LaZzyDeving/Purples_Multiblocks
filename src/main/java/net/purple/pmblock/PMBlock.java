package net.purple.pmblock;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import net.purple.pmblock.client.ClientSetup;
import net.purple.pmblock.common.registry.ItemRegistry;
import net.purple.pmblock.common.registry.Registry;
import org.slf4j.Logger;

import static net.purple.pmblock.PMBlock.MOD_ID;

@Mod(MOD_ID)
public class PMBlock
{
    public static final String MOD_ID = "pmblock";
    public static final Logger LOGGER = LogUtils.getLogger();

    public PMBlock()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Client Setups
        modEventBus.addListener(ClientSetup::registerRenderers);
        modEventBus.addListener(ClientSetup::registerShaders);

        // Register Registries for Blocks, Items, Creativetaps and so on
        Registry.registerDeferredRegisters(modEventBus);

        // Register the commonSetup method for modloading
        // modEventBus.addListener(this::commonSetup);


        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        modEventBus.addListener(Registry::addCreative);

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        // TODO - Add config back in
        //ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }




}
