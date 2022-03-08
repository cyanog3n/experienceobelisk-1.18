package com.cyanogen.experienceobelisk.gui;

import com.cyanogen.experienceobelisk.block_entities.XPObeliskEntity;
import com.cyanogen.experienceobelisk.network.PacketHandler;
import com.cyanogen.experienceobelisk.network.UpdateXPToServer;
import net.minecraft.world.entity.player.Player;

public class XPManager {

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

    public static int xpToLevels(int xp){
        if (xp < 394) {
            return (int) (Math.sqrt(xp + 9) - 3);
        } else if (xp >= 394 && xp < 1628) {
            return (int) ((Math.sqrt(40 * xp - 7839) + 81) * 0.1);
        } else if (xp >= 1628) {
            return (int) ((Math.sqrt(72 * xp - 54215) + 325) / 18);
        }
        return 0;
    }

    public static int playerXP;
    public static int finalXP;
    public static int tickCount = 0;

    public static void storeXP(int levels, Player player, XPObeliskEntity xpobelisk){

        if(player.tickCount != tickCount){

            //total amount of experience points the player currently has
            playerXP = levelsToXP(player.experienceLevel) + Math.round(player.experienceProgress * player.getXpNeededForNextLevel());

            if(player.experienceLevel >= levels){

                finalXP = levelsToXP(player.experienceLevel - levels) + Math.round(player.experienceProgress *
                        (levelsToXP(player.experienceLevel - levels + 1) - levelsToXP(player.experienceLevel - levels)));

                player.giveExperienceLevels(-levels);
                PacketHandler.INSTANCE.sendToServer(new UpdateXPToServer(0,-levels));

                xpobelisk.fillFromClient(playerXP - finalXP);
            }
            else if (playerXP >= 1){
                xpobelisk.fillFromClient(playerXP);
                PacketHandler.INSTANCE.sendToServer(new UpdateXPToServer(-2147483647,-2147483647));
            }
        }

        tickCount = player.tickCount;
    }

    public static void retrieveXP(int levels, Player player, XPObeliskEntity xpobelisk){

        if(player.tickCount != tickCount){

            playerXP = levelsToXP(player.experienceLevel) + Math.round(player.experienceProgress * player.getXpNeededForNextLevel());

            finalXP = levelsToXP(player.experienceLevel + levels) + Math.round(player.experienceProgress *
                    (levelsToXP(player.experienceLevel + levels + 1) - levelsToXP(player.experienceLevel + levels)));

            if(xpobelisk.getFluidAmount() >= finalXP - playerXP){
                xpobelisk.drain(finalXP - playerXP);
                xpobelisk.drainFromClient(finalXP - playerXP);

                PacketHandler.INSTANCE.sendToServer(new UpdateXPToServer(0,levels));
            }
            else if(xpobelisk.getFluidAmount() >= 1){
                PacketHandler.INSTANCE.sendToServer(new UpdateXPToServer(xpobelisk.getFluidAmount(),0));

                xpobelisk.setFluid(0);
                xpobelisk.emptyFromClient();
            }
        }

        tickCount = player.tickCount;
    }
}
