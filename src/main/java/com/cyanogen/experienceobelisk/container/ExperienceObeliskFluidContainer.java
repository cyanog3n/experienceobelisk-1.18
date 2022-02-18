package com.cyanogen.experienceobelisk.container;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.ItemFluidContainer;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import java.util.function.Predicate;

public class ExperienceObeliskFluidContainer extends FluidTank {


    public ExperienceObeliskFluidContainer(int capacity, Predicate<FluidStack> validator) {
        super(capacity, validator);
    }
}
