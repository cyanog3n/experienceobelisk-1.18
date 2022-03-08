package com.cyanogen.experienceobelisk.gui;

import com.cyanogen.experienceobelisk.block_entities.XPObeliskEntity;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import static com.cyanogen.experienceobelisk.gui.XPManager.levelsToXP;
import static com.cyanogen.experienceobelisk.gui.XPManager.xpToLevels;


public class ExperienceObeliskScreen extends Screen{

    public Level level;
    public Player player;
    public BlockPos pos;
    public XPObeliskEntity xpobelisk;
    private Button add1;
    private Button add10;
    private Button addAll;
    private Button drain1;
    private Button drain10;
    private Button drainAll;

    private final ResourceLocation texture = new ResourceLocation("experienceobelisk:textures/gui/container/dark_bg2.png");

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
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        renderBackground(pPoseStack);

        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, texture);

        int textureWidth = 256;
        int textureHeight = 256;
        int x = this.width / 2 - 176 / 2;
        int y = this.height / 2 - 166 / 2;

        int n = xpobelisk.getFluidAmount() - levelsToXP(xpToLevels(xpobelisk.getFluidAmount())); //remaining xp
        int m = levelsToXP(xpToLevels(xpobelisk.getFluidAmount()) + 1) - levelsToXP(xpToLevels(xpobelisk.getFluidAmount())); //xp for next level
        int p = Math.round(n * 138 / m);

        //render gui texture
        blit(pPoseStack, x, y, 0, 0, 176, 166, textureWidth, textureHeight);

        //render xp bar
        blit(pPoseStack, this.width / 2 - 138 / 2, this.height / 2 + 50, 0, 169, 138, 5, textureWidth, textureHeight);
        blit(pPoseStack, this.width / 2 - 138 / 2, this.height / 2 + 50, 0, 173, p, 5, textureWidth, textureHeight);

        //descriptors & info
        drawCenteredString(new PoseStack(), this.font, "Experience Obelisk",
                this.width / 2,this.height / 2 - 76, 0xFFFFFF);
        drawString(new PoseStack(), this.font, "Store",
                this.width / 2 - 77,this.height / 2 - 56, 0xFFFFFF);
        drawString(new PoseStack(), this.font, "Retrieve",
                this.width / 2 - 77,this.height / 2 - 10, 0xFFFFFF);
        drawCenteredString(new PoseStack(), this.font, String.valueOf(xpobelisk.getFluidAmount()) + " mB",
                this.width / 2,this.height / 2 + 35, 0xFFFFFF);
        drawCenteredString(new PoseStack(), this.font, String.valueOf(xpToLevels(xpobelisk.getFluidAmount())),
                this.width / 2,this.height / 2 + 60, 0x4DFF12);

        //widgets
        setupWidgetElements();

        //render widgets
        for(Widget widget : this.renderables) {
            widget.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        }

    }

    //buttons and whatnot go here
    private void setupWidgetElements() {

        clearWidgets();

        Style style = Style.EMPTY;
        Style green = style.withColor(0x45FF5B);
        Style red = style.withColor(0xFF454B);
        int w = 50; //width (divisible by 2)
        int h = 20; //height
        int s = 2; //spacing
        int y1 = 43;
        int y2 = -3;

        //deposit

        add1 = addRenderableWidget(new Button((int) (this.width / 2 - 1.5*w - s), this.height / 2 - y1, w, h, new TextComponent("+1")
                .setStyle(green), (onPress) -> {

            XPManager.storeXP(1, player, xpobelisk);
            int tick = player.tickCount;
            player.displayClientMessage(new TextComponent(String.valueOf(tick)), false);

        },
                new Button.OnTooltip() {
                    @Override
                    public void onTooltip(Button pButton, PoseStack pPoseStack, int pMouseX, int pMouseY) {
                        renderTooltip(pPoseStack, new TranslatableComponent("tooltip.experienceobelisk.experience_obelisk.add1"), pMouseX, pMouseY);
                    }
                }
        ));


        add10 = addRenderableWidget(new Button(this.width / 2 - w/2, this.height / 2 - y1, w, h, new TextComponent("+10")
                .setStyle(green), (onPress) -> {

            XPManager.storeXP(10, player, xpobelisk);

        },
                new Button.OnTooltip() {
                    @Override
                    public void onTooltip(Button pButton, PoseStack pPoseStack, int pMouseX, int pMouseY) {
                        renderTooltip(pPoseStack, new TranslatableComponent("tooltip.experienceobelisk.experience_obelisk.add10"), pMouseX, pMouseY);
                    }
                }
        ));

        addAll = addRenderableWidget(new Button((int) (this.width / 2 + 0.5*w + s), this.height / 2 - y1, w, h, new TextComponent("+All")
                .setStyle(green), (onPress) -> {

            XPManager.storeXP(player.experienceLevel + 1, player, xpobelisk);

        },
                new Button.OnTooltip() {
                    @Override
                    public void onTooltip(Button pButton, PoseStack pPoseStack, int pMouseX, int pMouseY) {
                        renderTooltip(pPoseStack, new TranslatableComponent("tooltip.experienceobelisk.experience_obelisk.addAll"), pMouseX, pMouseY);
                    }
                }
        ));


        //withdraw
        drain1 = addRenderableWidget(new Button((int) (this.width / 2 - 1.5*w - s), this.height / 2 - y2, w, h, new TextComponent("-1")
                .setStyle(red), (onPress) -> {

            XPManager.retrieveXP(1, player, xpobelisk);

        },
                new Button.OnTooltip() {
                    @Override
                    public void onTooltip(Button pButton, PoseStack pPoseStack, int pMouseX, int pMouseY) {
                        renderTooltip(pPoseStack, new TranslatableComponent("tooltip.experienceobelisk.experience_obelisk.drain1"), pMouseX, pMouseY);
                    }
                }
        ));

        drain10 = addRenderableWidget(new Button(this.width / 2 - w/2, this.height / 2 - y2, w, h, new TextComponent("-10")
                .setStyle(red), (onPress) -> {

            XPManager.retrieveXP(10, player, xpobelisk);

        },
                new Button.OnTooltip() {
                    @Override
                    public void onTooltip(Button pButton, PoseStack pPoseStack, int pMouseX, int pMouseY) {
                        renderTooltip(pPoseStack, new TranslatableComponent("tooltip.experienceobelisk.experience_obelisk.drain10"), pMouseX, pMouseY);
                    }
                }
        ));

        drainAll = addRenderableWidget(new Button((int) (this.width / 2 + 0.5*w + s), this.height / 2 - y2, w, h, new TextComponent("-All")
                .setStyle(red), (onPress) -> {

            XPManager.retrieveXP(64000000, player, xpobelisk);

        },
                new Button.OnTooltip() {
                    @Override
                    public void onTooltip(Button pButton, PoseStack pPoseStack, int pMouseX, int pMouseY) {
                        renderTooltip(pPoseStack, new TranslatableComponent("tooltip.experienceobelisk.experience_obelisk.drainAll"), pMouseX, pMouseY);
                    }
                }
        ));


    }

}
