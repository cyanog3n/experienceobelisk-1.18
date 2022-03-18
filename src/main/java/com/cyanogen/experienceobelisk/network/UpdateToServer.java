package com.cyanogen.experienceobelisk.network;

import com.cyanogen.experienceobelisk.block_entities.XPObeliskEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;


public class UpdateToServer {

    public static BlockPos pos;
    public static int XP;
    public static Request request;
    private long playerXP;
    private long finalXP;

    public UpdateToServer(BlockPos pos, int XP, Request request) {
        this.pos = pos;
        this.XP = XP;
        this.request = request;
    }

    public enum Request{
        FILL,
        DRAIN,
        FILL_ALL,
        DRAIN_ALL
    }

    public UpdateToServer(FriendlyByteBuf buffer) {

        pos = buffer.readBlockPos();
        XP = buffer.readInt();
        request = buffer.readEnum(Request.class);

    }

    public void encode(FriendlyByteBuf buffer){

        buffer.writeBlockPos(pos);
        buffer.writeInt(XP);
        buffer.writeEnum(request);
    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        final var success = new AtomicBoolean(false);
        ctx.get().enqueueWork(() -> {
            ServerPlayer sender = ctx.get().getSender();
            assert sender != null;
            BlockEntity serverEntity = sender.level.getBlockEntity(pos);

            //total experience points player has
            playerXP = levelsToXP(sender.experienceLevel) + Math.round(sender.experienceProgress * sender.getXpNeededForNextLevel());

            if(serverEntity instanceof XPObeliskEntity xpobelisk){

                //-----FILLING-----//

                if(request == Request.FILL){


                    //final amount of experience points the player will have after storing n levels
                    finalXP = levelsToXP(sender.experienceLevel - XP) + Math.round(sender.experienceProgress *
                            (levelsToXP(sender.experienceLevel - XP + 1) - levelsToXP(sender.experienceLevel - XP)));


                    if (xpobelisk.getSpace() == 0){ }

                    //if amount to add exceeds remaining capacity
                    else if(playerXP - finalXP >= xpobelisk.getSpace()){
                        sender.giveExperiencePoints(-xpobelisk.fill(16000000));
                    }

                    //normal operation
                    else if(sender.experienceLevel >= XP){

                        xpobelisk.fill((int) (playerXP - finalXP));
                        sender.giveExperienceLevels(-XP);

                    }
                    else if (playerXP >= 1){

                        sender.giveExperiencePoints(-xpobelisk.fill((int) playerXP));

                    }
                }

                //-----DRAINING-----//

                else if(request == Request.DRAIN){

                    int amount = xpobelisk.getFluidAmount();

                    finalXP = levelsToXP(sender.experienceLevel + XP) + Math.round(sender.experienceProgress *
                            (levelsToXP(sender.experienceLevel + XP + 1) - levelsToXP(sender.experienceLevel + XP)));

                    if(amount >= finalXP - playerXP){

                        xpobelisk.drain((int) (finalXP - playerXP));
                        sender.giveExperienceLevels(XP);

                    }
                    else if(amount >= 1){

                        sender.giveExperiencePoints(amount);
                        xpobelisk.setFluid(0);
                    }
                }

                //-----FILL OR DRAIN ALL-----//

                else if(request == Request.FILL_ALL){

                    if(playerXP <= xpobelisk.getSpace()){
                        xpobelisk.fill((int) playerXP);
                        sender.setExperiencePoints(0);
                        sender.setExperienceLevels(0);
                    }
                    else{
                        sender.giveExperiencePoints(-xpobelisk.getSpace());
                        xpobelisk.setFluid(16000000);
                    }


                }
                else if(request == Request.DRAIN_ALL){

                    sender.giveExperiencePoints(xpobelisk.getFluidAmount());
                    xpobelisk.setFluid(0);
                }

                success.set(true);
            }

        });
        ctx.get().setPacketHandled(true);
        return success.get();
    }

    public static int levelsToXP(int levels){
        if (levels <= 16) {
            return (int) (Math.pow(levels, 2) + 6 * levels);
        } else if (levels >= 17 && levels <= 31) {
            return (int) (2.5 * Math.pow(levels, 2) - 40.5 * levels + 360);
        } else if (levels >= 32) {
            return (int) (4.5 * Math.pow(levels, 2) - 162.5 * levels + 2220);
        }
        return 0;
    }
}
