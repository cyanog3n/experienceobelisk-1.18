package com.cyanogen.experienceobelisk.block;

import com.cyanogen.experienceobelisk.block_entities.ModTileEntitiesInit;
import com.cyanogen.experienceobelisk.block_entities.XPObeliskEntity;
import com.cyanogen.experienceobelisk.gui.GuiWrapper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.*;
import org.jetbrains.annotations.Nullable;

import java.util.function.ToIntFunction;

public class ExperienceObeliskBlock extends Block implements EntityBlock {

    public ExperienceObeliskBlock() {
        super(Properties.of(Material.METAL)
                .strength(9f)
                .destroyTime(1.2f)
                .requiresCorrectToolForDrops()
                .explosionResistance(8f)
                .lightLevel(new ToIntFunction<BlockState>() {
                    @Override
                    public int applyAsInt(BlockState value) {
                        return 14;
                    }
                })
                .noOcclusion()
                .emissiveRendering(new StatePredicate() {
                    @Override
                    public boolean test(BlockState state, BlockGetter getter, BlockPos pos) {
                        return true;
                    }
                })
        );
    }

    //crystal
    VoxelShape shape1 = Shapes.create(new AABB(6 / 16D,0 / 16D,6 / 16D,10 / 16D,16 / 16D,10 / 16D));
    //base
    VoxelShape shape2 = Shapes.create(new AABB(0 / 16D,0 / 16D,0 / 16D,16 / 16D,2 / 16D,16 / 16D));
    VoxelShape shape = Shapes.join(shape1, shape2, BooleanOp.OR);

    @Override
    public VoxelShape getCollisionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return shape;
    }


    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if(pLevel.isClientSide()){
            GuiWrapper.openGUI(pState, pLevel, pPos, pPlayer);
        }
        return InteractionResult.CONSUME;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return pBlockEntityType == ModTileEntitiesInit.XPOBELISK_BE.get() ? XPObeliskEntity::tick : null;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    //block entity
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return ModTileEntitiesInit.XPOBELISK_BE.get().create(pPos, pState);
    }

}
