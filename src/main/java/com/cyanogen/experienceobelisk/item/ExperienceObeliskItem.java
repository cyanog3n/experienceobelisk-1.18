package com.cyanogen.experienceobelisk.item;

import com.cyanogen.experienceobelisk.gui.ExperienceObeliskScreen;
import com.cyanogen.experienceobelisk.network.UpdateToServer;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.List;


public class ExperienceObeliskItem extends BlockItem {

    public ExperienceObeliskItem(Block pBlock, Properties pProperties) {
        super(pBlock, pProperties);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltip, TooltipFlag pFlag) {

        //pStack.getTag().get("amount").getAsString();
       // String info = pStack.getOrCreateTag().getAsString();
        String info = pStack.getOrCreateTag().getCompound("BlockEntityTag").getAsString();
        int amount = pStack.getOrCreateTag().getCompound("BlockEntityTag").getInt("Amount");
        int levels = ExperienceObeliskScreen.xpToLevels(amount);

        //pTooltip.add(new TextComponent("Fluid Amount: " + String.valueOf(amount)));

        pTooltip.add(new TranslatableComponent("tooltip.experienceobelisk.experience_obelisk.item_fluid_amount",
                new TextComponent(String.valueOf(amount) + " mB").withStyle(ChatFormatting.YELLOW)));

        pTooltip.add(new TranslatableComponent("tooltip.experienceobelisk.experience_obelisk.item_levels",
                new TextComponent(String.valueOf(levels)).withStyle(ChatFormatting.GREEN)));

        super.appendHoverText(pStack, pLevel, pTooltip, pFlag);

        //data get entity @s SelectedItem
    }
}
