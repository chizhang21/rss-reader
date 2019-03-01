package com.cashzhang.nozdormu;

/**
 * Created by cz21 on 2018/2/8.
 */

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class FeedItem implements Serializable {
    private static final long serialVersionUID = 3L;

    public String m_webtitle = "";
    public String m_title = "";
    public String m_imageLink = "";
    public String m_imageName = "";
    public String m_urlTrimmed = "";
    public String m_url = "";
    public String m_content = "";
    public String m_tcontent = "";
    public String[] m_desLines = {"", "", ""};
    public Long m_time = 0L;

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeUTF(m_webtitle);
        out.writeUTF(m_title);
        out.writeUTF(m_imageLink);
        out.writeUTF(m_imageName);
        out.writeUTF(m_urlTrimmed);
        out.writeUTF(m_url);
        out.writeUTF(m_content);
        out.writeUTF(m_tcontent);
        out.writeObject(m_desLines);
        out.writeLong(m_time);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        m_webtitle = in.readUTF();
        m_title = in.readUTF();
        m_imageLink = in.readUTF();
        m_imageName = in.readUTF();
        m_urlTrimmed = in.readUTF();
        m_url = in.readUTF();
        m_content = in.readUTF();
        m_tcontent = in.readUTF();
        m_desLines = (String[]) in.readObject();
        m_time = in.readLong();
    }

}