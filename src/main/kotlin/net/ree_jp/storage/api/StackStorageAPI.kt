/*
 * RRRRRR                         jjj
 * RR   RR   eee    eee               pp pp
 * RRRRRR  ee   e ee   e _____    jjj ppp  pp
 * RR  RR  eeeee  eeeee           jjj pppppp
 * RR   RR  eeeee  eeeee          jjj pp
 *                              jjjj  pp
 *
 * Copyright (c) 2020. Ree-jp.  All Rights Reserved.
 */

package net.ree_jp.storage.api

import cn.nukkit.Player
import cn.nukkit.Server
import cn.nukkit.blockentity.BlockEntity
import cn.nukkit.blockentity.BlockEntityChest
import cn.nukkit.item.Item
import cn.nukkit.level.Position
import net.ree_jp.storage.StackStoragePlugin
import net.ree_jp.storage.inventory.StackStorage
import net.ree_jp.storage.sqlite.SqliteHelper

class StackStorageAPI {

    companion object{

        fun check(p: Player, permission: String): Boolean {
            return if (!p.hasPermission(permission)) {
                p.sendMessage("request permission: $permission")
                false
            }else true
        }
    }

    private val storage = mutableMapOf<String, StackStorage>()

    fun isOpen(p: Player): Boolean {
        val xuid = p.loginChainData.xuid
        return storage[xuid] != null
    }

    fun sendGui(p: Player) {
        if (!check(p,"stackstorage.action.open")) return
        val xuid = p.loginChainData.xuid
        val level = p.level
        if (storage[xuid] != null) return
        val window = createGui(p.position.add(0.0, 2.0, 0.0))
        storage[xuid] = window
        level.sendBlocks(arrayOf(p), arrayOf(window.rightSide.holder.levelBlock, window.leftSide.holder.levelBlock))
        Server.getInstance().scheduler.scheduleDelayedTask(
            { p.addWindow(window) },
            3
        )
    }

    fun closeGui(p: Player) {
        val xuid = p.loginChainData.xuid
        val window = storage.remove(xuid) ?: return
        val level = p.level
        window.holder.inventory.clearAll()
        window.close(p)
        level.getBlock(window.rightSide.holder.levelBlock)
        level.getBlock(window.leftSide.holder.levelBlock)
    }

    fun addItem(p: Player, item: Item) {
        val xuid = p.loginChainData.xuid
        item.setCount(item.count + getHelper().getItem(xuid, item).count)
        getHelper().setItem(xuid, item)
    }

    fun removeItem(p: Player, item: Item) {
        val xuid = p.loginChainData.xuid
        val storageItem = getHelper().getItem(xuid, item)
        if (storageItem.count >= item.count) {
            storageItem.setCount(storageItem.count - item.count)
            getHelper().setItem(xuid, storageItem)
        }
    }

    private fun createGui(pos: Position): StackStorage {
        val level = pos.level
        val chunk = level.getChunk(pos.chunkX, pos.chunkZ)
        val chest1 = BlockEntity.createBlockEntity(
            BlockEntity.CHEST,
            chunk,
            BlockEntityChest.getDefaultCompound(pos, BlockEntity.CHEST)
        )
        val chest2 = BlockEntity.createBlockEntity(
            BlockEntity.CHEST,
            chunk,
            BlockEntityChest.getDefaultCompound(pos.west(), BlockEntity.CHEST)
        )
        if (chest1 is BlockEntityChest && chest2 is BlockEntityChest) {
            chest1.name = "StackStorage"
            chest2.name = "StackStorage"
            chest1.pairWith(chest2)
            return StackStorage(chest1, chest2)
        } else throw Exception()
    }

    private fun getHelper(): SqliteHelper {
        return StackStoragePlugin.getInstance().getHelper()
    }
}