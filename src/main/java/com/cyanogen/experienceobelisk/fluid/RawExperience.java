package com.cyanogen.experienceobelisk.fluid;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;


public class RawExperience extends Fluid {

    private net.minecraftforge.fluids.FluidAttributes forgeFluidAttributes;

    @Override
    protected net.minecraftforge.fluids.FluidAttributes createAttributes()
    {
        return forgeFluidAttributes;
    }


    @Override
    public Item getBucket() {
        return null;
    }

    @Override
    protected boolean canBeReplacedWith(FluidState pFluidState, BlockGetter pBlockReader, BlockPos pPos, Fluid pFluid, Direction pDirection) {
        return false;
    }

    @Override
    protected Vec3 getFlow(BlockGetter pBlockReader, BlockPos pPos, FluidState pFluidState) {
        return null;
    }

    @Override
    public int getTickDelay(LevelReader p_76120_) {
        return 0;
    }

    @Override
    protected float getExplosionResistance() {
        return 0;
    }

    @Override
    public float getHeight(FluidState p_76124_, BlockGetter p_76125_, BlockPos p_76126_) {
        return 0;
    }

    @Override
    public float getOwnHeight(FluidState p_76123_) {
        return 0;
    }

    @Override
    protected BlockState createLegacyBlock(FluidState pState) {
        return null;
    }

    @Override
    public boolean isSource(FluidState pState) {
        return false;
    }

    @Override
    public int getAmount(FluidState pState) {
        return 0;
    }

    @Override
    public VoxelShape getShape(FluidState p_76137_, BlockGetter p_76138_, BlockPos p_76139_) {
        return null;
    }
}
