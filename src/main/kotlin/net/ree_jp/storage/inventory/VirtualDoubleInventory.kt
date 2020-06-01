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

import cn.nukkit.blockentity.BlockEntityChest
import cn.nukkit.inventory.DoubleChestInventory

open class VirtualDoubleInventory(left: BlockEntityChest, right: BlockEntityChest) : DoubleChestInventory(left, right)