package com.cyanogen.experienceobelisk.item;

import com.cyanogen.experienceobelisk.ExperienceObelisk;
import com.cyanogen.experienceobelisk.ModCreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItemsInit {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, ExperienceObelisk.MOD_ID);

    //test item, probably will be unused
    public static final RegistryObject<Item> STEEL = ITEMS.register("steel",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MOD_TAB)));

    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
