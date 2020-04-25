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

package net.ree_jp.storage.inventory

import cn.nukkit.Server
import cn.nukkit.blockentity.BlockEntityChest
import cn.nukkit.item.Item
import net.ree_jp.storage.StackStoragePlugin
import net.ree_jp.storage.sqlite.SqliteHelper

class StackStorage(left: BlockEntityChest, right: BlockEntityChest) : VirtualDoubleInventory(left, right) {

    private var page = 1

    fun nextPage(xuid: String) {
        page ++
        refresh(xuid)
    }

    fun backPage(xuid: String) {
        page --
        refresh(xuid)
    }

    fun refresh(xuid: String) {
        Server.getInstance().scheduler.scheduleDelayedTask(
            { callRefresh(xuid) },
            1
        )
    }

    private fun callRefresh(xuid: String) {
        val items = helper().getStorage(xuid).withIndex().groupBy{ it.index / 45 }.map{ entry -> entry.value.map{ it.value } }
        clearAll()
        if (items.isNotEmpty()) {
            val pageItem = items[page - 1]
            for ((index, item) in pageItem.withIndex()) {
                val count = item.count
                val max = item.maxStackSize
                if (count > max) {
                    item.setLore("count: $count")
                    item.setCount(max)
                }
                setItem(index, item)
            }
        }
        setItem(49, Item.get(Item.BOOK).setCustomName("Close"))
        if (page > 1) {
            setItem(45, Item.get(Item.ARROW).setCustomName("BackPage"))
        }
        if (items.size > page) {
            setItem(53, Item.get(Item.ARROW).setCustomName("NextPage"))
        }
    }

    private fun helper(): SqliteHelper {
        return StackStoragePlugin.getInstance().getHelper()
    }
}