package com.cyanogen.experienceobelisk.network;

import com.cyanogen.experienceobelisk.block_entities.XPObeliskEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public class UpdateBlockFluidToServer {

    public static BlockPos pos;
    public static int amount;
    public static Request request;

    public UpdateBlockFluidToServer(BlockPos pos, int amount, Request request) {
        this.pos = pos;
        this.amount = amount;
        this.request = request;
    }

    public enum Request{
        FILL,
        DRAIN,
        SET,
        EMPTY
    }


    public UpdateBlockFluidToServer(FriendlyByteBuf buffer) {

        pos = buffer.readBlockPos();
        amount = buffer.readInt();
        request = buffer.readEnum(Request.class);

    }

    public void encode(FriendlyByteBuf buffer){

        buffer.writeBlockPos(pos);
        buffer.writeInt(amount);
        buffer.writeEnum(request);
    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        final var success = new AtomicBoolean(false);
        ctx.get().enqueueWork(() -> {
            ServerPlayer sender = ctx.get().getSender();
            assert sender != null;
            BlockEntity serverEntity = sender.level.getBlockEntity(pos);

            //change fluid amount accordingly
            if(serverEntity instanceof XPObeliskEntity){
                if(request == Request.SET){
                    ((XPObeliskEntity) serverEntity).setFluid(amount);
                }
                else if(request == Request.FILL){
                    ((XPObeliskEntity) serverEntity).fill(amount);
                }
                else if(request == Request.DRAIN){
                    ((XPObeliskEntity) serverEntity).drain(amount);
                }
                else if(request == Request.EMPTY){
                    ((XPObeliskEntity) serverEntity).setFluid(0);
                }
                success.set(true);
            }

        });
        ctx.get().setPacketHandled(true);
        return success.get();
    }
}
