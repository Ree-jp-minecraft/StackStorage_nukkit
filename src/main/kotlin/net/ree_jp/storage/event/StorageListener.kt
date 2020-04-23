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

package net.ree_jp.storage.event

import cn.nukkit.event.EventHandler
import cn.nukkit.event.Listener
import cn.nukkit.event.inventory.InventoryCloseEvent
import cn.nukkit.event.inventory.InventoryTransactionEvent
import cn.nukkit.inventory.Inventory
import cn.nukkit.inventory.transaction.action.SlotChangeAction
import cn.nukkit.item.Item
import net.ree_jp.storage.StackStoragePlugin
import net.ree_jp.storage.inventory.StackStorage

class StorageListener: Listener {

    @EventHandler
    fun transferForStorageInOut(ev: InventoryTransactionEvent) {
        val tr = ev.transaction
        val p = tr.source
        val xuid = p.loginChainData.xuid
        val api = StackStoragePlugin.getInstance().getApi()

        for (action in tr.actions) {
            if (ev.isCancelled) return
            if (action is SlotChangeAction) {
                val inventory = action.inventory
                if (inventory is StackStorage) {
                    val target = action.targetItem
                    val source = action.sourceItem
                    if (source.id != Item.AIR) {
                        when(action.slot) {
                            49 -> {
                                ev.setCancelled()
                                api.closeGui(p)
                            }
                            45 -> {
                                ev.setCancelled()
                                inventory.backPage(xuid)
                            }
                            53 -> {
                                ev.setCancelled()
                                inventory.nextPage(xuid)
                            }
                        }
                    }
                    if (target.id != Item.AIR) {
                        api.addItem(p, target)
                        inventory.refresh(xuid)
                    }
                    if (source.id != Item.AIR) {
                        api.removeItem(p, source)
                        inventory.refresh(xuid)
                    }
                }
            }
        }
    }

    @EventHandler
    fun closeForStorageClear(ev: InventoryCloseEvent) {
        val p = ev.player
        val inventory = ev.inventory
        if (inventory is StackStorage) {
            StackStoragePlugin.getInstance().getApi().closeGui(p)
            removeLore(inventory)
        }
    }

    private fun removeLore(inventory: Inventory) {
        val size = inventory.size - 1
        for (index in 0..size) inventory.setItem(index, inventory.getItem(index).setLore())
    }
}