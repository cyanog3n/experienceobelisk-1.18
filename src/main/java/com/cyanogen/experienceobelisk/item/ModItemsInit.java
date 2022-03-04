package com.cyanogen.experienceobelisk.item;

import com.cyanogen.experienceobelisk.ExperienceObelisk;
import com.cyanogen.experienceobelisk.ModCreativeModeTab;
import com.cyanogen.experienceobelisk.fluid.ModFluidsInit;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static net.minecraft.world.item.Items.BUCKET;

public class ModItemsInit {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, ExperienceObelisk.MOD_ID);

    //test item, probably will be unused
    public static final RegistryObject<Item> STEEL = ITEMS.register("steel",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MOD_TAB)));

    //bucket of experience
    public static BucketItem rawExperienceBucket = new BucketItem(ModFluidsInit.RAW_EXPERIENCE,
            new Item.Properties().tab(ModCreativeModeTab.MOD_TAB).craftRemainder(BUCKET).stacksTo(1).rarity(Rarity.UNCOMMON));

    public static final RegistryObject<Item> RAW_EXPERIENCE_BUCKET = ITEMS.register("raw_experience_bucket",
            () -> rawExperienceBucket);

    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
