package com.cyanogen.experienceobelisk.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.CraftingScreen;
import net.minecraft.client.gui.screens.inventory.FurnaceScreen;
import net.minecraft.client.gui.screens.inventory.LoomScreen;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class GuiWrapper {

    public static void openGUI(Level level, Player player) {
        Minecraft.getInstance().setScreen(new ExperienceObeliskScreen(level, player));
    }
}
