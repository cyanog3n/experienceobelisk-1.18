package com.cyanogen.experienceobelisk.network;

import com.cyanogen.experienceobelisk.ExperienceObelisk;
import com.google.common.graph.Network;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
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
                UpdateXPToServer.class,
                UpdateXPToServer::encode,
                UpdateXPToServer::new,
                UpdateXPToServer::handle);


        INSTANCE.registerMessage(index++,
                UpdateBlockFluidToServer.class,
                UpdateBlockFluidToServer::encode,
                UpdateBlockFluidToServer::new,
                UpdateBlockFluidToServer::handle);

    }

}
