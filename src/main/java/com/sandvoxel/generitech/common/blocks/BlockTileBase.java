/*
 * LIMITED USE SOFTWARE LICENSE AGREEMENT
 * This Limited Use Software License Agreement (the "Agreement") is a legal agreement between you, the end-user, and the AlgorithmicsAnonymous Team ("AlgorithmicsAnonymous"). By downloading or purchasing the software materials, which includes source code (the "Source Code"), artwork data, music and software tools (collectively, the "Software"), you are agreeing to be bound by the terms of this Agreement. If you do not agree to the terms of this Agreement, promptly destroy the Software you may have downloaded or copied.
 * AlgorithmicsAnonymous SOFTWARE LICENSE
 * 1. Grant of License. AlgorithmicsAnonymous grants to you the right to use the Software. You have no ownership or proprietary rights in or to the Software, or the Trademark. For purposes of this section, "use" means loading the Software into RAM, as well as installation on a hard disk or other storage device. The Software, together with any archive copy thereof, shall be destroyed when no longer used in accordance with this Agreement, or when the right to use the Software is terminated. You agree that the Software will not be shipped, transferred or exported into any country in violation of the U.S. Export Administration Act (or any other law governing such matters) and that you will not utilize, in any other manner, the Software in violation of any applicable law.
 * 2. Permitted Uses. For educational purposes only, you, the end-user, may use portions of the Source Code, such as particular routines, to develop your own software, but may not duplicate the Source Code, except as noted in paragraph 4. The limited right referenced in the preceding sentence is hereinafter referred to as "Educational Use." By so exercising the Educational Use right you shall not obtain any ownership, copyright, proprietary or other interest in or to the Source Code, or any portion of the Source Code. You may dispose of your own software in your sole discretion. With the exception of the Educational Use right, you may not otherwise use the Software, or an portion of the Software, which includes the Source Code, for commercial gain.
 * 3. Prohibited Uses: Under no circumstances shall you, the end-user, be permitted, allowed or authorized to commercially exploit the Software. Neither you nor anyone at your direction shall do any of the following acts with regard to the Software, or any portion thereof:
 * Rent;
 * Sell;
 * Lease;
 * Offer on a pay-per-play basis;
 * Distribute for money or any other consideration; or
 * In any other manner and through any medium whatsoever commercially exploit or use for any commercial purpose.
 * Notwithstanding the foregoing prohibitions, you may commercially exploit the software you develop by exercising the Educational Use right, referenced in paragraph 2. hereinabove.
 * 4. Copyright. The Software and all copyrights related thereto (including all characters and other images generated by the Software or depicted in the Software) are owned by AlgorithmicsAnonymous and is protected by United States copyright laws and international treaty provisions. AlgorithmicsAnonymous shall retain exclusive ownership and copyright in and to the Software and all portions of the Software and you shall have no ownership or other proprietary interest in such materials. You must treat the Software like any other copyrighted materials. You may not otherwise reproduce, copy or disclose to others, in whole or in any part, the Software. You may not copy the written materials accompanying the Software. You agree to use your best efforts to see that any user of the Software licensed hereunder complies with this Agreement.
 * 5. NO WARRANTIES. AlgorithmicsAnonymous DISCLAIMS ALL WARRANTIES, BOTH EXPRESS IMPLIED, INCLUDING BUT NOT LIMITED TO, IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE WITH RESPECT TO THE SOFTWARE. THIS LIMITED WARRANTY GIVES YOU SPECIFIC LEGAL RIGHTS. YOU MAY HAVE OTHER RIGHTS WHICH VARY FROM JURISDICTION TO JURISDICTION. AlgorithmicsAnonymous DOES NOT WARRANT THAT THE OPERATION OF THE SOFTWARE WILL BE UNINTERRUPTED, ERROR FREE OR MEET YOUR SPECIFIC REQUIREMENTS. THE WARRANTY SET FORTH ABOVE IS IN LIEU OF ALL OTHER EXPRESS WARRANTIES WHETHER ORAL OR WRITTEN. THE AGENTS, EMPLOYEES, DISTRIBUTORS, AND DEALERS OF AlgorithmicsAnonymous ARE NOT AUTHORIZED TO MAKE MODIFICATIONS TO THIS WARRANTY, OR ADDITIONAL WARRANTIES ON BEHALF OF AlgorithmicsAnonymous.
 * Exclusive Remedies. The Software is being offered to you free of any charge. You agree that you have no remedy against AlgorithmicsAnonymous, its affiliates, contractors, suppliers, and agents for loss or damage caused by any defect or failure in the Software regardless of the form of action, whether in contract, tort, includinegligence, strict liability or otherwise, with regard to the Software. Copyright and other proprietary matters will be governed by United States laws and international treaties. IN ANY CASE, AlgorithmicsAnonymous SHALL NOT BE LIABLE FOR LOSS OF DATA, LOSS OF PROFITS, LOST SAVINGS, SPECIAL, INCIDENTAL, CONSEQUENTIAL, INDIRECT OR OTHER SIMILAR DAMAGES ARISING FROM BREACH OF WARRANTY, BREACH OF CONTRACT, NEGLIGENCE, OR OTHER LEGAL THEORY EVEN IF AlgorithmicsAnonymous OR ITS AGENT HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES, OR FOR ANY CLAIM BY ANY OTHER PARTY. Some jurisdictions do not allow the exclusion or limitation of incidental or consequential damages, so the above limitation or exclusion may not apply to you.
 */

package com.sandvoxel.generitech.common.blocks;

import com.sandvoxel.generitech.Reference;
import com.sandvoxel.generitech.common.tileentities.TileEntityBase;
import com.sandvoxel.generitech.common.util.IBlockRenderer;
import com.sandvoxel.generitech.common.util.IOrientable;
import com.sandvoxel.generitech.common.util.TileHelper;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public abstract class BlockTileBase extends BlockBase implements ITileEntityProvider, IBlockRenderer {
    protected static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

    @Nonnull
    private Class<? extends TileEntity> tileEntityClass;

    public BlockTileBase(Material material, String resourcePath) {
        super(material, resourcePath);
    }

    protected void setTileEntity(final Class<? extends TileEntity> clazz) {
        this.tileEntityClass = clazz;
        this.setTileProvider(true);
        this.isInventory = IInventory.class.isAssignableFrom(clazz);

        String tileName = "tileentity." + Reference.MOD_ID + "." + clazz.getSimpleName();
        GameRegistry.registerTileEntity(this.tileEntityClass, tileName);
    }

    private void setTileProvider(final boolean b) {
        ReflectionHelper.setPrivateValue(Block.class, this, b, "isTileProvider");
    }

    public Class<? extends TileEntity> getTileEntityClass() {
        return this.tileEntityClass;
    }

    @Override
    public final TileEntity createNewTileEntity(World worldIn, int meta) {
        try {
            return this.tileEntityClass.newInstance();
        } catch (final InstantiationException ex) {
            throw new IllegalStateException("Failed to create a new instance of an illegal class " + this.tileEntityClass, ex);
        } catch (final IllegalAccessException ex) {
            throw new IllegalStateException("Failed to create a new instance of " + this.tileEntityClass + " because of a lack of permissions", ex);
        }
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        final TileEntityBase tileEntity = TileHelper.getTileEntity(worldIn, pos, TileEntityBase.class);
        if (tileEntity != null) {
            tileEntity.dropItems();
        }
    }

    @Override
    public EnumFacing[] getValidRotations(World world, BlockPos pos) {
        final TileEntityBase tileEntity = TileHelper.getTileEntity(world, pos, TileEntityBase.class);
        if (tileEntity != null && tileEntity.canBeRotated())
            return EnumFacing.HORIZONTALS;

        return super.getValidRotations(world, pos);
    }

    @Override
    public IOrientable getOrientable(IBlockAccess world, BlockPos pos) {
        return TileHelper.getTileEntity(world, pos, TileEntityBase.class);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        TileEntityBase tileEntity = TileHelper.getTileEntity(worldIn, pos, TileEntityBase.class);
        if (tileEntity != null && tileEntity.canBeRotated()) {
            return state.withProperty(FACING, tileEntity.getForward());
        }
        return state.withProperty(FACING, EnumFacing.NORTH);
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos blockPos, IBlockState state, EntityLivingBase placer, ItemStack itemStack) {
        TileEntityBase tileEntity = TileHelper.getTileEntity(world, blockPos, TileEntityBase.class);

        if (tileEntity == null)
            return;

        if (itemStack.hasTagCompound() && itemStack.getTagCompound().hasKey("MachineItemData")) {
            tileEntity.setMachineItemData(itemStack.getTagCompound().getCompoundTag("MachineItemData"));
            tileEntity.initMachineData();
        }

        if (itemStack.hasDisplayName()) {
            tileEntity.setCustomName(itemStack.getDisplayName());
        }

        if (tileEntity.canBeRotated()) {
            //todo: make client side configuration for this option...
            if (placer.isSneaking()) {
                tileEntity.setOrientation(placer.getHorizontalFacing());
            } else {
                tileEntity.setOrientation(placer.getHorizontalFacing().getOpposite());
            }
        }
    }

    @Override
    public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
        return willHarvest || super.removedByPlayer(state, world, pos, player, false);
    }

    @Override
    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te, ItemStack stack) {
        super.harvestBlock(worldIn, player, pos, state, te, stack);
        worldIn.setBlockToAir(pos);
    }

    /**
     * Add machineItemData to item that drops
     *
     * @param world
     * @param pos
     * @param state
     * @param fortune
     * @return
     */
    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        TileEntityBase tileEntityBase = TileHelper.getTileEntity(world, pos, TileEntityBase.class);
        if (tileEntityBase != null) {
            final ItemStack itemStack = new ItemStack(this, 1, tileEntityBase.getBlockMetadata());

            NBTTagCompound machineItemData = tileEntityBase.getMachineItemData();
            if (machineItemData != null) {
                NBTTagCompound itemTag = new NBTTagCompound();
                itemTag.setTag("MachineItemData", machineItemData);
                itemStack.setTagCompound(itemTag);
            }

            if (tileEntityBase.hasCustomName()) {
                itemStack.setStackDisplayName(tileEntityBase.getCustomName());
            }

            ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
            drops.add(itemStack);
            return drops;
        }
        return super.getDrops(world, pos, state, fortune);
    }

    @Override
    public void registerBlockRenderer() {
        super.registerBlockRenderer();
    }

    @Override
    public void registerBlockItemRenderer() {
        super.registerBlockItemRenderer();
    }
}
