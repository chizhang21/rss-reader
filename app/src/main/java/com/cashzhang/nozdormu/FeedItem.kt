package com.cashzhang.nozdormu

import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable

/**
 * Created by cz21 on 2018/2/8.
 */
class FeedItem : Serializable {
    var m_webtitle = ""
    var m_title = ""
    var m_imageLink = ""
    var m_imageName = ""
    var m_urlTrimmed = ""
    var m_url = ""
    var m_content = ""
    var m_tcontent = ""
    var m_desLines = arrayOf("", "", "")
    var m_time = 0L

    @Throws(IOException::class)
    private fun writeObject(out: ObjectOutputStream) {
        out.writeUTF(m_webtitle)
        out.writeUTF(m_title)
        out.writeUTF(m_imageLink)
        out.writeUTF(m_imageName)
        out.writeUTF(m_urlTrimmed)
        out.writeUTF(m_url)
        out.writeUTF(m_content)
        out.writeUTF(m_tcontent)
        out.writeObject(m_desLines)
        out.writeLong(m_time)
    }

    @Throws(IOException::class, ClassNotFoundException::class)
    private fun readObject(`in`: ObjectInputStream) {
        m_webtitle = `in`.readUTF()
        m_title = `in`.readUTF()
        m_imageLink = `in`.readUTF()
        m_imageName = `in`.readUTF()
        m_urlTrimmed = `in`.readUTF()
        m_url = `in`.readUTF()
        m_content = `in`.readUTF()
        m_tcontent = `in`.readUTF()
        m_desLines = `in`.readObject() as Array<String>
        m_time = `in`.readLong()
    }

    companion object {
        private const val serialVersionUID = 3L
    }
}