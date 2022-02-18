package com.cyanogen.experienceobelisk.fluid;

import com.cyanogen.experienceobelisk.ExperienceObelisk;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModFluidsInit {
    public static final DeferredRegister<Fluid> FLUIDS =
            DeferredRegister.create(ForgeRegistries.FLUIDS, ExperienceObelisk.MOD_ID);

    //registering fluid
    public static final RegistryObject<Fluid> RAW_EXPERIENCE = FLUIDS.register("raw_experience",
            () -> new RawExperience());


public static void register(IEventBus eventBus){
        FLUIDS.register(eventBus);
    }
}
