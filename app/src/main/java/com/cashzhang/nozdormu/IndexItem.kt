package com.cashzhang.nozdormu

import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable

/**
 * Created by cz21 on 2018/2/7.
 */
class IndexItem(var m_uid: Long, var m_url: String, vararg tags: String) : Serializable {
    var m_tags: Array<String>

    @Throws(IOException::class)
    private fun writeObject(out: ObjectOutputStream) {
        out.writeLong(m_uid)
        out.writeUTF(m_url)
        out.writeObject(m_tags)
    }

    @Throws(IOException::class, ClassNotFoundException::class)
    private fun readObject(`in`: ObjectInputStream) {
        m_uid = `in`.readLong()
        m_url = `in`.readUTF()
        m_tags = `in`.readObject() as Array<String>
    }

    companion object {
        private const val serialVersionUID = 200L
    }

    init {
        m_tags = tags
    }
}