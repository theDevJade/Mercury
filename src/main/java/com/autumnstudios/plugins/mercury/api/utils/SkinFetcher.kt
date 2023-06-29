package com.autumnstudios.plugins.mercury.api.utils


import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import java.net.HttpURLConnection
import java.util.*

object SkinFetcher {

  const val URL_USERNAME_TO_UUID: String = "https://api.mojang.com/profiles/minecraft"
  const val URL_UUID_TO_SKIN: String = "https://api.mineskin.org/generate/user"

  fun fetch(uuid: String) : Skin? {
    val url: String = URL_UUID_TO_SKIN

    val connection: HttpURLConnection = ByteUtils.generateConnection(url, "POST")

    ByteUtils.jsonToBytes("{\"uuid\": \"${uuid}\"}", connection)

    val out: String = ByteUtils.readResponse(connection)

    val gson: JsonParser = JsonParser()
    val obj: JsonObject = gson.parse(out).asJsonObject.get("data").asJsonObject.get("texture").asJsonObject
    return if (obj.get("value") != null) {
      Skin(obj.get("value").asString, obj.get("signature").asString)
    } else {
      null
    }

  }

  fun usernameToUUID(username: String): String {
    val payload: String = "[\"${username}\"]"

    val connection: HttpURLConnection = ByteUtils.generateConnection(URL_USERNAME_TO_UUID, "POST")

    ByteUtils.jsonToBytes(payload, connection)

    val out: String = ByteUtils.readResponse(connection)

    val gson: JsonParser = JsonParser()
    val obj: JsonArray = gson.parse(out).asJsonArray

    val string = obj.get(0).asJsonObject.get("id").asString

    return fromTrimmed(string).toString()


  }

  fun fromTrimmed(trimmedUUID: String?): UUID? {
    requireNotNull(trimmedUUID)
    val builder = StringBuilder(trimmedUUID.trim { it <= ' ' })
    /* Backwards adding to avoid index adjustments */try {
      builder.insert(20, "-")
      builder.insert(16, "-")
      builder.insert(12, "-")
      builder.insert(8, "-")
    } catch (e: StringIndexOutOfBoundsException) {
      throw IllegalArgumentException()
    }
    return UUID.fromString(builder.toString())
  }
}
