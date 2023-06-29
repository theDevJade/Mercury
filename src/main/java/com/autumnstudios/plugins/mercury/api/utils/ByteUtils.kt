package com.autumnstudios.plugins.mercury.api.utils

import java.io.BufferedReader
import java.io.ByteArrayInputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.Charset


object ByteUtils {

  fun jsonToBytes(json: String, con: HttpURLConnection) {
    con.outputStream.use { os ->
      val input: ByteArrayInputStream = json.byteInputStream(Charset.defaultCharset())
      val inputBytes: ByteArray = input.readBytes()
      os.write(inputBytes, 0, inputBytes.size)
    }

  }

  fun readResponse(con: HttpURLConnection) : String {
    BufferedReader(
      InputStreamReader(con.inputStream, "utf-8")
    ).use { br ->
      val response = StringBuilder()
      var responseLine: String? = null
      while (br.readLine().also { responseLine = it } != null) {
        response.append(responseLine!!.trim { it <= ' ' })
      }

      return response.toString()

    }
  }

  fun generateConnection(urlString: String, method: String) : HttpURLConnection {
    val url: URL = URL(urlString)

    val connection: HttpURLConnection = url.openConnection() as HttpURLConnection

    connection.requestMethod = method
    connection.setRequestProperty("Content-Type", "application/json")
    connection.setRequestProperty("Accept", "application/json")
    connection.doOutput = true

    return connection
  }
}
