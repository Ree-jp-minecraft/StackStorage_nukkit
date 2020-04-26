/*
 * Some source code is used by @nukkitx.
 * https://github.com/NukkitX/FakeInventories
 */

package net.ree_jp.storage.api

import cn.nukkit.Player
import cn.nukkit.block.BlockID
import cn.nukkit.blockentity.BlockEntity
import cn.nukkit.level.GlobalBlockPalette
import cn.nukkit.math.BlockVector3
import cn.nukkit.nbt.NBTIO
import cn.nukkit.nbt.tag.CompoundTag
import cn.nukkit.network.protocol.BlockEntityDataPacket
import cn.nukkit.network.protocol.UpdateBlockPacket
import java.nio.ByteOrder


class FakeAPI {

    companion object {
        private lateinit var instance: FakeAPI

        fun getInstance(): FakeAPI {
            if (!::instance.isInitialized) {
                instance = FakeAPI()
            }
            return instance
        }
    }

    private val chestPos = mutableMapOf<String, BlockVector3>()

    fun placeChest(who: Player, pos: BlockVector3) {
        place(who, pos, pos.add(1))
        place(who, pos.add(1), pos)
        chestPos[who.loginChainData.xuid] = pos
    }

    fun removeChest(who: Player) {
        val pos = chestPos[who.loginChainData.xuid] ?: return
        update(who, pos)
        update(who, pos.add(1))
    }

    private fun place(who: Player, pos: BlockVector3, pair: BlockVector3) {
        val updateBlock = UpdateBlockPacket()
        updateBlock.blockRuntimeId = GlobalBlockPalette.getOrCreateRuntimeId(BlockID.CHEST, 0)
        updateBlock.flags = UpdateBlockPacket.FLAG_ALL_PRIORITY
        updateBlock.x = pos.x
        updateBlock.y = pos.y
        updateBlock.z = pos.z
        who.dataPacket(updateBlock)
        val blockEntityData = BlockEntityDataPacket()
        blockEntityData.x = pos.x
        blockEntityData.y = pos.y
        blockEntityData.z = pos.z
        blockEntityData.namedTag = getDoubleNbt(pos, pair)
        who.dataPacket(blockEntityData)

    }

    private fun update(who: Player, pos: BlockVector3) {
        val updateBlock = UpdateBlockPacket()
        updateBlock.blockRuntimeId =
            GlobalBlockPalette.getOrCreateRuntimeId(who.getLevel().getBlock(pos.asVector3()).fullId)
        updateBlock.flags = UpdateBlockPacket.FLAG_ALL_PRIORITY
        updateBlock.x = pos.x
        updateBlock.y = pos.y
        updateBlock.z = pos.z

        who.dataPacket(updateBlock)
    }

    private fun getDoubleNbt(pos: BlockVector3, pos2: BlockVector3): ByteArray? {
        val tag = CompoundTag()
            .putString("id", BlockEntity.CHEST)
            .putInt("x", pos.x)
            .putInt("y", pos.y)
            .putInt("z", pos.z)
            .putInt("pairx", pos2.x)
            .putInt("pairz", pos2.z)
            .putString("CustomName", "StackStorage")
        return NBTIO.write(tag, ByteOrder.LITTLE_ENDIAN, true)
    }
}