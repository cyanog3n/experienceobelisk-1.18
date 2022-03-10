package com.cyanogen.experienceobelisk.network;

import com.cyanogen.experienceobelisk.ExperienceObelisk;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public final class PacketHandler {

    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(ExperienceObelisk.MOD_ID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void init(){
        int index = 0;

        INSTANCE.registerMessage(index++,
                UpdateToServer.class,
                UpdateToServer::encode,
                UpdateToServer::new,
                UpdateToServer::handle);

    }

}
