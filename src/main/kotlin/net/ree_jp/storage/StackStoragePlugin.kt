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

package net.ree_jp.storage

import cn.nukkit.plugin.PluginBase
import net.ree_jp.storage.api.StackStorageAPI
import net.ree_jp.storage.sqlite.SqliteHelper

class StackStoragePlugin: PluginBase() {

    companion object{

        private lateinit var instance: StackStoragePlugin

        fun getInstance(): StackStoragePlugin{
            return instance
        }
    }

    private lateinit var helper: SqliteHelper

    private lateinit var api: StackStorageAPI

    override fun onLoad() {
        instance = this
        dataFolder.mkdir()
        super.onLoad()
    }

    override fun onEnable() {
        super.onEnable()
    }

    override fun onDisable() {
        super.onDisable()
    }

    fun getHelper(): SqliteHelper{
        if (!::helper.isInitialized) {
            helper = SqliteHelper(getInstance().dataFolder.path)
        }
        return helper
    }

    fun getApi(): StackStorageAPI{
        if (!::api.isInitialized) {
            api = StackStorageAPI()
        }
        return api
    }
}