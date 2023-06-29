package me.flaming.utils

import org.bukkit.Material

@JvmRecord
data class Button(val x: Int, val y: Int, val material: Material, val name: String) {
    fun execute() {}
}
