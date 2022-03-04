package com.cyanogen.experienceobelisk.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public class UpdateXPToServer {

    public static int experiencePoints;
    public static int experienceLevels;

    public UpdateXPToServer(int points, int levels) {
        experiencePoints = points;
        experienceLevels = levels;
    }

    public UpdateXPToServer(FriendlyByteBuf buffer) {
        experiencePoints = buffer.readInt();
        experienceLevels = buffer.readInt();

    }

    public void encode(FriendlyByteBuf buffer){
        buffer.writeInt(experiencePoints);
        buffer.writeInt(experienceLevels);
    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        final var success = new AtomicBoolean(false);
        ctx.get().enqueueWork(() -> {
            ServerPlayer sender = ctx.get().getSender();


            sender.giveExperiencePoints(experiencePoints);
            sender.giveExperienceLevels(experienceLevels);
            success.set(true);

        });
        ctx.get().setPacketHandled(true);
        return success.get();
    }
}
