package com.cyanogen.experienceobelisk.gui;

import com.cyanogen.experienceobelisk.block_entities.XPObeliskEntity;
import com.cyanogen.experienceobelisk.network.PacketHandler;
import com.cyanogen.experienceobelisk.network.UpdateXPToServer;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;


public class ExperienceObeliskScreen extends Screen{

    public Level level;
    public Player player;
    public BlockPos pos;
    public XPObeliskEntity xpobelisk;
    int finalXP;
    int playerXP;
    private Button add1;
    private Button add10;
    private Button addAll;
    private Button drain1;
    private Button drain10;
    private Button drainAll;


    private ResourceLocation texture = new ResourceLocation("experienceobelisk:textures/gui/container/furnace.png");

    public ExperienceObeliskScreen(Level level, Player player, BlockPos pos) {
        super(new TextComponent("Experience Obelisk"));
        this.level = level;
        this.player = player;
        this.pos = pos;
        this.xpobelisk = (XPObeliskEntity) level.getBlockEntity(pos);
    }

    @Override
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        if (pKeyCode == 256 && this.shouldCloseOnEsc()) {
            this.onClose();
            return true;
        } else {
            return false;
        }
    }


    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public Minecraft getMinecraft() {
        return this.minecraft;
    }

    @Override
    protected void init() {}

    @Override
    public void tick() {}

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        renderBackground(pPoseStack);

        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, texture);

        int textureWidth = 176;
        int textureHeight = 166;
        int x = this.width / 2 - textureWidth / 2;
        int y = this.height / 2 - textureHeight / 2;


        //render gui texture
        blit(pPoseStack, x, y, (float)0, (float)0, 176, 166, textureWidth, textureHeight);

        drawCenteredString(new PoseStack(), this.font, String.valueOf(xpobelisk.getFluidAmount()) + " mB",
                this.width / 2,this.height / 2 + 30, 0xFFFFFF); //ARGB
        drawCenteredString(new PoseStack(), this.font, String.valueOf(XpToLevels(xpobelisk.getFluidAmount())) + " Levels",
                this.width / 2,this.height / 2 + 45, 0xFFFFFF);

        setupWidgetElements();

        //render widgets
       for(Widget widget : this.renderables) {
           widget.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
       }

    }

    public static int LevelsToXP(int levels){
        if (levels <= 16) {
            return (int) (Math.pow(levels, 2) + 6 * levels);
        } else if (levels >= 17 && levels <= 31) {
            return (int) (2.5 * Math.pow(levels, 2) - 40.5 * levels + 360);
        } else if (levels >= 32) {
            return (int) (4.5 * Math.pow(levels, 2) - 162.5 * levels + 2220);
        }
        return 0;
    }

    public static int XpToLevels(int xp){
        if (xp < 394) {
            return (int) (Math.sqrt(xp + 9) - 3);
        } else if (xp >= 394 && xp < 1628) {
            return (int) ((Math.sqrt(40 * xp - 7839) + 81) * 0.1);
        } else if (xp >= 1628) {
            return (int) ((Math.sqrt(72 * xp - 54215) + 325) / 18);
        }
        return 0;
    }

    //buttons and whatnot go here
    private void setupWidgetElements() {

        clearWidgets();
        playerXP = LevelsToXP(player.experienceLevel) + Math.round(player.experienceProgress * player.getXpNeededForNextLevel());

        //deposit
        add1 = addRenderableWidget(new Button(this.width / 2 - 75, this.height / 2 - 40, 40, 20, new TextComponent("+1"), (onPress) -> {

            if(player.experienceLevel >= 1){

                finalXP = LevelsToXP(player.experienceLevel - 1) + Math.round(player.experienceProgress *
                        (LevelsToXP(player.experienceLevel) - LevelsToXP(player.experienceLevel - 1)));

                PacketHandler.INSTANCE.sendToServer(new UpdateXPToServer(0,-1));

                xpobelisk.fillFromClient(playerXP - finalXP);
            }
            else if (playerXP > 0){
                xpobelisk.fillFromClient(playerXP);
                PacketHandler.INSTANCE.sendToServer(new UpdateXPToServer(0,-2147483647));
            }

            //player.displayClientMessage(new TextComponent(String.valueOf(playerXP)),false);

        }));
        add10 = addRenderableWidget(new Button(this.width / 2 - 20, this.height / 2 - 40, 40, 20, new TextComponent("+10"), (onPress) -> {

            if(player.experienceLevel >= 10){

                finalXP = LevelsToXP(player.experienceLevel - 10) + Math.round(player.experienceProgress *
                        (LevelsToXP(player.experienceLevel - 9) - LevelsToXP(player.experienceLevel - 10)));

                PacketHandler.INSTANCE.sendToServer(new UpdateXPToServer(0,-10));

                xpobelisk.fillFromClient(playerXP - finalXP);
            }
            else if (playerXP > 0){
                xpobelisk.fillFromClient(playerXP);
                PacketHandler.INSTANCE.sendToServer(new UpdateXPToServer(0,-2147483647));
            }


        }));
        addAll = addRenderableWidget(new Button(this.width / 2 + 35, this.height / 2 - 40, 40, 20, new TextComponent("+All"), (onPress) -> {

            xpobelisk.fillFromClient(playerXP);
            PacketHandler.INSTANCE.sendToServer(new UpdateXPToServer(0,-2147483647));

        }));

        //withdraw
        drain1 = addRenderableWidget(new Button(this.width / 2 - 75, this.height / 2 - 10, 40, 20, new TextComponent("-1"), (onPress) -> {

            finalXP = LevelsToXP(player.experienceLevel + 1) + Math.round(player.experienceProgress *
                    (LevelsToXP(player.experienceLevel + 2) - LevelsToXP(player.experienceLevel + 1)));

            if(xpobelisk.getFluidAmount() >= finalXP - playerXP){
                xpobelisk.drainFromClient(finalXP - playerXP);
                PacketHandler.INSTANCE.sendToServer(new UpdateXPToServer(0,1));
            }
            else if(xpobelisk.getFluidAmount() > 0){
                PacketHandler.INSTANCE.sendToServer(new UpdateXPToServer(xpobelisk.getFluidAmount(),0));
                xpobelisk.emptyFromClient();
            }

        }));
        drain10 = addRenderableWidget(new Button(this.width / 2 - 20, this.height / 2 - 10, 40, 20, new TextComponent("-10"), (onPress) -> {

            finalXP = LevelsToXP(player.experienceLevel + 10) + Math.round(player.experienceProgress *
                    (LevelsToXP(player.experienceLevel + 11) - LevelsToXP(player.experienceLevel + 10)));

            if(xpobelisk.getFluidAmount() >= finalXP - playerXP){
                xpobelisk.drainFromClient(finalXP - playerXP);
                PacketHandler.INSTANCE.sendToServer(new UpdateXPToServer(0,10));
            }
            else if(xpobelisk.getFluidAmount() > 0){
                PacketHandler.INSTANCE.sendToServer(new UpdateXPToServer(xpobelisk.getFluidAmount(),0));
                xpobelisk.emptyFromClient();
            }

        }));
        drainAll = addRenderableWidget(new Button(this.width / 2 + 35, this.height / 2 - 10, 40, 20, new TextComponent("-All"), (onPress) -> {

            PacketHandler.INSTANCE.sendToServer(new UpdateXPToServer(xpobelisk.getFluidAmount(),0));
            xpobelisk.emptyFromClient();

        }));

    }

}
