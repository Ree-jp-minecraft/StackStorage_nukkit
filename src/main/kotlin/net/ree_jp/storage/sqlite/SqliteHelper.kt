package net.ree_jp.storage.sqlite

import cn.nukkit.item.Item
import cn.nukkit.item.enchantment.Enchantment
import com.google.gson.Gson
import org.sqlite.SQLiteException
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement

class SqliteHelper(path: String) : ISqliteHelper {

    private lateinit var connection: Connection

    init {
        try {
            Class.forName("org.sqlite.JDBC")
            connection = DriverManager.getConnection("jdbc:sqlite:$path/StackStorage.db")
        } catch (ex: SQLiteException) {
            throw ex
        }
    }

    override fun isExists(xuid: String): Boolean {
        val stmt = connection.prepareStatement("SELECT COUNT(*) FROM sqlite_master WHERE type = 'table' AND xuid = ?")
        stmt.setString(1, xuid)
        return stmt.executeQuery().next()
    }

    override fun getItem(xuid: String, item: Item): Item {
        if (!isExists(xuid)) throw Exception("storage not found")

        val stmt = connection.prepareStatement("SELECT count FROM '$xuid' WHERE id = ?, meta =?, enchant = ?")
        stmt.setInt(1, item.id)
        stmt.setInt(2, item.damage)
        stmt.setString(3, decodeEnchant(item))
        val result = stmt.executeQuery()
        if (result.next()) {
            item.setCount(result.getInt("count"))
        }else{
            item.setCount(0)
        }
        return item
    }

    override fun setItem(xuid: String, item: Item) {
        if (!isExists(xuid)) setTable(xuid)

        val id = item.count
        val meta = item.damage
        val enchant = decodeEnchant(item)
        val count = item.count

        val stmt: PreparedStatement
        if (count > 0) {
            stmt = connection.prepareStatement("REPLACE INTO '$xuid' VALUES (?, ?, ?, ?)")
            stmt.setInt(4, count)
        }else{
            stmt = connection.prepareStatement("DELETE FROM '$xuid' WHERE id = ?, meta = ?, enchant = ?")
        }
        stmt.setInt(1, id)
        stmt.setInt(2, meta)
        stmt.setString(3, enchant)
        stmt.execute()
    }

    override fun getStorage(xuid: String): List<Item> {
        if (!isExists(xuid)) throw Exception("storage not found")

        val result = connection.createStatement().executeQuery("SELECT * FROM '$xuid'")
        val list = mutableListOf<Item>()
        while (result.next()) {
            val item = Item.get(result.getInt("id"), result.getInt("meta"), result.getInt("count"))
            list.add(encodeEnchant(item, result.getString("enchant")))
        }
        return list
    }

    override fun setStorage(xuid: String, storage: List<Item>) {
        if (isExists(xuid)) connection.createStatement().execute("DELETE FROM '$xuid'")

        for (item in storage) {
            setItem(xuid, item)
        }
    }

    private fun encodeEnchant(item: Item, json: String): Item {
        val enchants = Gson().fromJson<Map<Int, Int>>(json, Map::class.java)
        for (key in enchants.keys) {
            val level = enchants[key] ?: throw  Exception("enchant not found(id:$key)")
            item.addEnchantment(Enchantment.get(key).setLevel(level))
        }
        return item
    }

    private fun decodeEnchant(item: Item): String {
        val enchants = mutableMapOf<Int, Int>()
        for (enchant in item.enchantments) {
            enchants[enchant.id] = enchant.level
        }
        return Gson().toJson(enchants)
    }

    private fun setTable(xuid: String) {
        connection.createStatement()
            .execute("CREATE TABLE IF NOT EXISTS '$xuid' (id INTEGER NOT NULL ,meta INTEGER NOT NULL ,enchant TEXT NOT NULL ,count INTEGER NOT NULL ,PRIMARY KEY (id,meta,enchant))")
    }
}