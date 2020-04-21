package net.ree_jp.storage

import cn.nukkit.plugin.PluginBase
import net.ree_jp.storage.sqlite.SqliteHelper

class StackStoragePlugin: PluginBase() {

    companion object{

        private lateinit var instance: StackStoragePlugin

        private lateinit var helper: SqliteHelper

        fun getInstance(): StackStoragePlugin{
            return instance
        }

        fun getHelper(): SqliteHelper{
            if (!::helper.isInitialized) {
                helper = SqliteHelper(getInstance().dataFolder.path)
            }
            return helper
        }
    }

    override fun onLoad() {
        instance = this
        dataFolder.mkdir()
        super.onLoad()
    }

    override fun onEnable() {
        getHelper()
        super.onEnable()
    }

    override fun onDisable() {
        super.onDisable()
    }
}