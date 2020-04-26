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

package net.ree_jp.storage.sqlite

import cn.nukkit.item.Item

interface ISqliteHelper {

    fun isExists(xuid: String): Boolean

    fun getItem(xuid: String, item: Item): Item

    fun setItem(xuid: String, item: Item)

    fun getStorage(xuid: String): List<Item>

    fun setStorage(xuid: String, storage: List<Item>)
}