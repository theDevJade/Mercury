package com.autumnstudios.mercury.utils

import java.util.Objects

class Horizon {

    companion object {
        fun containsFromList(t: String, items: List<String>) : Boolean {
            for (s: String in items) {
                if (t == s) {
                    return true
                }
            }

            return false
        }

        fun <Any> quickList(vararg objects: Any) : List<Any> {
            val list: List<Any> = objects.asList()
            return list;
        }

      fun setDeclaredFieldValue(instance: Any, fieldName: String, value: Any) {
        try {
          val declaredField = instance.javaClass.getDeclaredField(fieldName)
          declaredField.isAccessible = true
          declaredField.set(instance, value)
        } catch (_: Exception) {

        }
      }
    }
}
