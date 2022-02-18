package com.cyanogen.experienceobelisk.block;

import com.cyanogen.experienceobelisk.block_entities.ModTileEntitiesInit;
import com.cyanogen.experienceobelisk.gui.GuiWrapper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.function.ToIntFunction;

public class ExperienceObeliskBlock extends Block implements EntityBlock {

    public ExperienceObeliskBlock() {
        super(Properties.of(Material.METAL)
                .strength(9f)
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

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if(pLevel.isClientSide()){
            GuiWrapper.openGUI(pLevel, pPlayer);
        }
        return InteractionResult.PASS;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return ModTileEntitiesInit.XPOBELISK_BE.get().create(pPos, pState);
    }
}
