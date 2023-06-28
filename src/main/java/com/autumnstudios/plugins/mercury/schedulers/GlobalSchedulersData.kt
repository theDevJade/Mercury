package com.autumnstudios.plugins.mercury.schedulers

import org.bukkit.Bukkit

class GlobalSchedulersData {

  private val globalSchedulersData: MutableMap<String, Int> = HashMap()

  fun addGlobalScheduler(id: String, taskId: Int) {
    globalSchedulersData[id] = taskId
  }

  fun putDownScheduler(taskId: Int) : Boolean {
    if (globalSchedulersData.containsValue(taskId)) {
      Bukkit.getScheduler().cancelTask(taskId)
      return true
    }

    return false
  }

  fun putDownScheduler(id: String) : Boolean {
    if (globalSchedulersData.contains(id)) {
      Bukkit.getScheduler().cancelTask(globalSchedulersData[id]!!)
      return true
    }

    return false
  }
}
