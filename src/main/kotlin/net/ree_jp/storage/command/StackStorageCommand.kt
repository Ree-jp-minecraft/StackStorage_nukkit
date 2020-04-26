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

package net.ree_jp.storage.command

import cn.nukkit.Player
import cn.nukkit.command.Command
import cn.nukkit.command.CommandSender
import net.ree_jp.storage.StackStoragePlugin

class StackStorageCommand(name: String, description: String): Command(name, description) {

    init {
        permission = "stackstorage.command.$name"
        aliases = arrayOf("st")
    }

    override fun execute(sender: CommandSender?, commandLabel: String?, args: Array<out String>?): Boolean {
        if (testPermission(sender) && sender is Player) {
            StackStoragePlugin.getInstance().getApi().sendGui(sender)
        }
        return true
    }
}