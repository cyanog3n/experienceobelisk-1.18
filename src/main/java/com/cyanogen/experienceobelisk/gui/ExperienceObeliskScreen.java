package com.cyanogen.experienceobelisk.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.CraftingScreen;
import net.minecraft.client.gui.screens.inventory.FurnaceScreen;
import net.minecraft.commands.arguments.MessageArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.List;

public class ExperienceObeliskScreen extends Screen {

    public Level level;
    public Player player;
    private Button add1;
    private Button add10;
    private Button addAll;
    private Button drain1;
    private Button drain10;
    private Button drainAll;
    private Button close;

    public static final ResourceLocation EXPERIENCEOBELISK_BG = new ResourceLocation("experienceobelisk:gui/pee");

    public ExperienceObeliskScreen(Level level, Player player) {
        super(new TextComponent("Experience Obelisk"));
        this.level = level;
        this.player = player;
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
    protected void init() {
        setupWidgetElements();
    }

    private void setupWidgetElements() {
        clearWidgets();
        RenderSystem.setShaderTexture(0, this.EXPERIENCEOBELISK_BG);
        add1 = addRenderableWidget(new Button(10, 20, 50, 20, new TextComponent("+10"), (onPress) -> {

        }));
        add10 = addRenderableWidget(new Button(10, 20, 50, 20, new TextComponent("+10"), (onPress) -> {

        }));
        addAll = addRenderableWidget(new Button(10, 20, 50, 20, new TextComponent("+10"), (onPress) -> {

        }));
        drain1 = addRenderableWidget(new Button(10, 20, 50, 20, new TextComponent("+10"), (onPress) -> {

        }));
        drain10 = addRenderableWidget(new Button(10, 20, 50, 20, new TextComponent("+10"), (onPress) -> {

        }));
        drainAll = addRenderableWidget(new Button(10, 20, 50, 20, new TextComponent("+10"), (onPress) -> {

        }));
        close = addRenderableWidget(new Button(100, 20, 20, 20, new TextComponent("X"), (onPress) -> {
                minecraft.setScreen(null);
        }));


    }

}
