package com.cashzhang.nozdormu

import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable

/**
 * Created by cz21 on 2018/2/8.
 */
class FeedItem : Serializable {
    private var webtitle = ""
    private var title = ""
    private var imageLink = ""
    private var imageName = ""
    private var urlTrimmed = ""
    private var url = ""
    private var content = ""
    private var tcontent = ""
    private var desLines = arrayOf("", "", "")
    private var time = 0L

    @Throws(IOException::class)
    private fun writeObject(out: ObjectOutputStream) {
        out.writeUTF(webtitle)
        out.writeUTF(title)
        out.writeUTF(imageLink)
        out.writeUTF(imageName)
        out.writeUTF(urlTrimmed)
        out.writeUTF(url)
        out.writeUTF(content)
        out.writeUTF(tcontent)
        out.writeObject(desLines)
        out.writeLong(time)
    }

    @Throws(IOException::class, ClassNotFoundException::class)
    private fun readObject(`in`: ObjectInputStream) {
        webtitle = `in`.readUTF()
        title = `in`.readUTF()
        imageLink = `in`.readUTF()
        imageName = `in`.readUTF()
        urlTrimmed = `in`.readUTF()
        url = `in`.readUTF()
        content = `in`.readUTF()
        tcontent = `in`.readUTF()
        desLines = `in`.readObject() as Array<String>
        time = `in`.readLong()
    }

    companion object {
        private const val serialVersionUID = 3L
    }
}