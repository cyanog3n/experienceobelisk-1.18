package com.cyanogen.experienceobelisk.block_entities;

import com.cyanogen.experienceobelisk.fluid.ModFluidsInit;
import com.cyanogen.experienceobelisk.network.PacketHandler;
import com.cyanogen.experienceobelisk.network.UpdateBlockFluidToServer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.WorldCapabilityData;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import org.lwjgl.system.CallbackI;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.List;
import java.util.function.Predicate;

import static com.cyanogen.experienceobelisk.network.UpdateBlockFluidToServer.Request.*;


public class XPObeliskEntity extends BlockEntity implements IAnimatable{

    //-----------ANIMATIONS-----------//

    //events that control what animation is being played
    private <E extends BlockEntity & IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        AnimationController controller = event.getController();
        controller.transitionLengthTicks = 0;
        controller.setAnimation(new AnimationBuilder().addAnimation("xpobelisk.idle", true));

        return PlayState.CONTINUE;
    }

    public XPObeliskEntity(BlockPos pPos, BlockState pState) {
        super(ModTileEntitiesInit.XPOBELISK_BE.get(), pPos, pState);
        this.pos = pPos;
        this.state = pState;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller", 0, this::predicate));
    }

    private final AnimationFactory manager = new AnimationFactory(this);
    @Override
    public AnimationFactory getFactory() {
        return manager;
    }


    //-----------FLUID HANDLER-----------//


    protected FluidTank tank = xpObeliskTank();
    private final LazyOptional<IFluidHandler> handler = LazyOptional.of(() -> tank);
    private static final Fluid rawExperience = ModFluidsInit.RAW_EXPERIENCE.get().getSource();
    public static BlockPos pos;
    public static BlockState state;
    public static double radius = 2.5;

    private FluidTank xpObeliskTank() {
        return new FluidTank(64000000){
            @Override
            protected void onContentsChanged()
            {
                super.onContentsChanged();
                setChanged();
            }
            @Override
            public boolean isFluidValid(FluidStack stack)
            {
                return stack.getFluid() == rawExperience;
            }
            @Override
            public boolean isFluidValid(int tank, @Nonnull FluidStack stack) {
                return stack.getFluid() == rawExperience;
            }
        };
    }

    public static <T> void tick(Level level, BlockPos pos, BlockState state, T blockEntity) {
        level.sendBlockUpdated(pos, state, state, 2);

        //passive xp absorption ability
        //todo: add configuration options either in game or via config files

        AABB area = new AABB(
                pos.getX() - radius,
                pos.getY() - radius,
                pos.getZ() - radius,
                pos.getX() + radius,
                pos.getY() + radius,
                pos.getZ() + radius);

        List<Entity> list = level.getEntities(null, area);
        BlockEntity entity = level.getBlockEntity(pos);

        if(entity instanceof XPObeliskEntity){
            for(Entity e : list){
                if(e instanceof ExperienceOrb){
                //  level.getNearestPlayer(e, 100).displayClientMessage(new TextComponent("ding!"), false);
                    int value = ((ExperienceOrb) e).getValue();
                    ((XPObeliskEntity) entity).fill(value);
                    e.discard();
                }
            }
        }
    }

    @Override
    public void load(CompoundTag tag)
    {
        super.load(tag);
        tank.readFromNBT(tag);
    }

    @Override
    protected void saveAdditional(CompoundTag tag)
    {
        super.saveAdditional(tag);
        tank.writeToNBT(tag);
    }

    //sends CompoundTag out with nbt data
    @Override
    public CompoundTag getUpdateTag() {
        return tank.writeToNBT(super.getUpdateTag());
    }

    //receives and loads nbt data from incoming tag
    @Override
    public void handleUpdateTag(CompoundTag tag){
        load(tag);
        tank.readFromNBT(tag);
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt)
    {
        CompoundTag compoundtag = pkt.getTag();
        if (compoundtag != null) {
            load(compoundtag);
        }
    }

    @Override
    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing)
    {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return handler.cast();
        return super.getCapability(capability, facing);
        //controls which sides can give or receive fluids
    }

    public void fill(int amount){
            tank.fill(new FluidStack(rawExperience, amount), IFluidHandler.FluidAction.EXECUTE);
    }

    public void drain(int amount){
            tank.drain(new FluidStack(rawExperience, amount), IFluidHandler.FluidAction.EXECUTE);
    }

    public void setFluid(int amount){
        tank.setFluid(new FluidStack(rawExperience, amount));
    }

    public int getFluidAmount(){
        return tank.getFluidAmount();
    }

    //for clientside access via packethandler

    public void fillFromClient(int amount){
        PacketHandler.INSTANCE.sendToServer(new UpdateBlockFluidToServer(pos, amount, FILL));
    }

    public void drainFromClient(int amount){
        PacketHandler.INSTANCE.sendToServer(new UpdateBlockFluidToServer(pos, amount, DRAIN));
    }

    public void setFromClient(int amount){
        PacketHandler.INSTANCE.sendToServer(new UpdateBlockFluidToServer(pos, amount, SET));
    }

    public void emptyFromClient(){
        PacketHandler.INSTANCE.sendToServer(new UpdateBlockFluidToServer(pos, 0, EMPTY));
    }


}

