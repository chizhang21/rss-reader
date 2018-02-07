package com.cashzhang.ashley;

/**
 * Created by zhangchi on 2018/2/7.
 */

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public
class IndexItem implements Serializable
{
    private static final long serialVersionUID = 200L;

    public long m_uid;
    public String m_url;
    public String[] m_tags;

    public
    IndexItem(long id, String url, String... tags)
    {
        m_uid = id;
        m_url = url;
        m_tags = tags;
    }

    private
    void writeObject(ObjectOutputStream out) throws IOException
    {
        out.writeLong(m_uid);
        out.writeUTF(m_url);
        out.writeObject(m_tags);
    }

    private
    void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
    {
        m_uid = in.readLong();
        m_url = in.readUTF();
        m_tags = (String[]) in.readObject();
    }
}