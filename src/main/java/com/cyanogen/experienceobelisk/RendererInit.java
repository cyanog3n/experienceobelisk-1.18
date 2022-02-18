package com.cyanogen.experienceobelisk;

import com.cyanogen.experienceobelisk.block_entities.ModTileEntitiesInit;
import com.cyanogen.experienceobelisk.block_entities.XPObeliskTileRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ExperienceObelisk.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class RendererInit {

    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    //registering geckolib custom renderer
    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModTileEntitiesInit.XPOBELISK_BE.get(), XPObeliskTileRenderer::new);
        LOGGER.info("Entity Renderer registered");
    }
}
