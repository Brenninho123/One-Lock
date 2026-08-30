package com.arngmods93.onelock.data

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.io.IOException

class DataFile(private val context: Context) {

    private val json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    private fun fileFor(name: String): File {
        return File(context.filesDir, name)
    }

    suspend inline fun <reified T> read(name: String): T? = withContext(Dispatchers.IO) {
        val file = fileFor(name)
        if (!file.exists()) return@withContext null
        return@withContext try {
            json.decodeFromString<T>(file.readText())
        } catch (e: IOException) {
            null
        } catch (e: kotlinx.serialization.SerializationException) {
            null
        }
    }

    suspend inline fun <reified T> write(name: String, data: T): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            fileFor(name).writeText(json.encodeToString(data))
            true
        } catch (e: IOException) {
            false
        }
    }

    suspend fun delete(name: String): Boolean = withContext(Dispatchers.IO) {
        return@withContext fileFor(name).let { if (it.exists()) it.delete() else true }
    }

    suspend fun exists(name: String): Boolean = withContext(Dispatchers.IO) {
        fileFor(name).exists()
    }
}
