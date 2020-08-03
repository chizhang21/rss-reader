package com.cashzhang.nozdormu

import android.content.Context
import android.util.Log
import com.cashzhang.nozdormu.ObjectIO
import java.io.*

/**
 * Created by cz21 on 2018/2/7.
 */
class ObjectIO {
    private val m_context: Context
    private var m_fileName: String? = null
    private var m_object: Any? = null
    private var m_type: Int

    constructor(context: Context, fileName: String?, type: Int) {
        m_context = context
        m_fileName = fileName
        m_type = type
    }

    constructor(context: Context, type: Int) {
        m_context = context
        m_type = type
    }

    fun setNewFileName(fileName: String?) {
        m_fileName = fileName
        m_object = null
    }

    fun write(`object`: Any?): Boolean {
        try {
            val stringBuilder = StringBuilder()
            when (m_type) {
                TYPE_COLLECTION -> stringBuilder.append(m_context.getExternalFilesDir("collection"))
                TYPE_CONTENT -> stringBuilder.append(m_context.getExternalFilesDir("content"))
                else -> {
                }
            }
            val file = File("$stringBuilder/$m_fileName")
            Log.d(TAG, "write: filePath=" + file.absolutePath)
            if (!file.exists()) file.createNewFile()
            val out = ObjectOutputStream(FileOutputStream(file))
            return try {
                out.writeObject(`object`)
                true
            } finally {
                out.close()
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return false
    }

    val elementCount: Int
        get() {
            var count = 0
            try {
                if (null != m_object) {
                    count = (m_object as Collection<*>).size
                } else {
                    val `object` = read() as Collection<*>?
                    if (null != `object`) {
                        count = `object`.size
                    }
                }
            } catch (ignored: ClassCastException) {
            }
            return count
        }

    fun read(): Any? {
        try {
            val stringBuilder = StringBuilder()
            when (m_type) {
                1 -> stringBuilder.append(m_context.getExternalFilesDir("collection"))
                2 -> stringBuilder.append(m_context.getExternalFilesDir("content"))
                else -> {
                }
            }
            val file = File("$stringBuilder/$m_fileName")
            Log.d(TAG, "write: filePath=" + file.absolutePath)
            val `in`: ObjectInput = ObjectInputStream(FileInputStream(file))
            return try {
                m_object = `in`.readObject()
                m_object
            } finally {
                `in`.close()
            }
        } catch (e: ClassNotFoundException) {
        } catch (e: StreamCorruptedException) {
        } catch (e: FileNotFoundException) {
        } catch (e: IOException) {
        }
        return null
    }

    companion object {
        private val TAG = ObjectIO::class.java.simpleName
        private const val TYPE_FILE = 0
        private const val TYPE_COLLECTION = 1
        private const val TYPE_CONTENT = 2
    }
}