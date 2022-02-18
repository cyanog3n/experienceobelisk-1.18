package com.cyanogen.experienceobelisk.block_entities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;


public class XPObeliskEntity extends BlockEntity implements IAnimatable {

    //events that control what animation is being played
    private <E extends BlockEntity & IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        AnimationController controller = event.getController();
        controller.transitionLengthTicks = 0;
        controller.setAnimation(new AnimationBuilder().addAnimation("xpobelisk.idle", true));

        return PlayState.CONTINUE;
    }


    public XPObeliskEntity(BlockPos pPos, BlockState pState) {
        super(ModTileEntitiesInit.XPOBELISK_BE.get(), pPos, pState);
    }
    private final AnimationFactory factory = new AnimationFactory(this);

    //registering controller
    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller", 0, this::predicate));
    }

    //create new AnimationFactory for this block entity
    private final AnimationFactory manager = new AnimationFactory(this);
    @Override
    public AnimationFactory getFactory() {
        return manager;
    }
}

