package com.cyanogen.experienceobelisk;

import com.cyanogen.experienceobelisk.block.ModBlocksInit;
import com.cyanogen.experienceobelisk.block_entities.ModTileEntitiesInit;
import com.cyanogen.experienceobelisk.block_entities.XPObeliskTileRenderer;
import com.cyanogen.experienceobelisk.fluid.ModFluidsInit;
import com.cyanogen.experienceobelisk.item.ModItemsInit;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.bernie.geckolib3.GeckoLib;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(ExperienceObelisk.MOD_ID)

public class ExperienceObelisk
{
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "experienceobelisk";

    public ExperienceObelisk() {
        // Register the setup method for modloading
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        eventBus.addListener(this::setup);
        ModItemsInit.register(eventBus);
        ModBlocksInit.register(eventBus);
        ModTileEntitiesInit.register(eventBus);
        GeckoLib.initialize();

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModTileEntitiesInit.XPOBELISK_BE.get(), XPObeliskTileRenderer::new);
        LOGGER.info("Entity Renderer registered");
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        // some preinit code
        LOGGER.info("HELLO FROM PREINIT");

        //setting render types for custom models
        //note: set geckolib model types in tile renderer
        //ItemBlockRenderTypes.setRenderLayer(ModBlocks.experience_obelisk, RenderType.translucent());
    }
}
