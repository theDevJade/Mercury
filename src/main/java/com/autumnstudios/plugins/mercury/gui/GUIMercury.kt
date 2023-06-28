package com.autumnstudios.plugins.mercury.gui

import com.autumnstudios.plugins.mercury.chat.ColorUtil
import com.autumnstudios.plugins.mercury.item.ItemUtils
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

class GUIMercury(rows: Int, name: String) {

  private val rows: Int

  private val iutils: ItemUtils;

  private val name: String;

  private val rowDefinations: MutableMap<Int, String> = HashMap<Int, String>()

  val charDefinations: MutableMap<Char, ItemStack> = HashMap<Char, ItemStack>()


  init {
    this.rows = rows;
    this.iutils = ItemUtils()
    this.name = name;

    initializeFeatures()

  }

  private fun initializeFeatures() {
    for (i in 1..rows) {
      rowDefinations[i] = "_________"
    }

    charDefinations.put(' ', ItemStack(Material.AIR))
    charDefinations.put('_', iutils.quickItem(Material.GRAY_STAINED_GLASS_PANE, "&e"))
  }

  fun defineRow(row: Int, data: String) {
    var dataRestoreRow: String? = null
    if (rowDefinations[row] != null) {
      dataRestoreRow = rowDefinations[row]
    }

    rowDefinations[row] = data

    syntaxCheck(row, dataRestoreRow)


  }

  fun defineChar(chr: Char, stack: ItemStack) {
    charDefinations[chr] = stack
  }

  fun buildInventory() : Inventory {
    val inv: Inventory = Bukkit.createInventory(null, (rows * 9), ColorUtil().colorize(name))
    val mutableDefinationMap: MutableMap<Int, List<Char>> = HashMap()
    for (i in 1..rows) {
      val mutableList: MutableList<Char>? = rowDefinations[i]?.toMutableList()
      mutableDefinationMap.put(i, mutableList?.toList()!!)

      for (chr: Char in mutableList) {
        val index = (mutableList.indexOf(chr) + 1) * i
        inv.setItem(index - 1, charDefinations[chr]!!)
      }
    }

    return inv;
  }

  private fun syntaxCheck(row: Int, dataRestore: String?) : Boolean {
    if (!rowDefinations.contains(row)) {
      return false
    }

    if (dataRestore != null) {
      if (rowDefinations.get(row)?.length != 9) {
        rowDefinations[row] = dataRestore
        return false
      }
    }

    return rowDefinations.get(row)?.length == 9
  }
}
