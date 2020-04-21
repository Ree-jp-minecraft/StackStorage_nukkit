package net.ree_jp.storage.sqlite

import cn.nukkit.item.Item

interface ISqliteHelper {

    fun isExists(xuid: String): Boolean

    fun getItem(xuid: String, item: Item): Item

    fun setItem(xuid: String, item: Item)

    fun getStorage(xuid: String): List<Item>

    fun setStorage(xuid: String, storage: List<Item>)
}