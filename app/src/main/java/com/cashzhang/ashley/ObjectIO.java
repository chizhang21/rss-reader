package com.cashzhang.ashley;

import android.content.Context;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.Collection;

/**
 * Created by zhangchi on 2018/2/7.
 */


public class ObjectIO {
    private final Context m_context;
    private String m_fileName;
    private Object m_object;

    public ObjectIO(Context context, String fileName) {
        m_context = context;
        m_fileName = fileName;
    }

    public void setNewFileName(String fileName) {
        m_fileName = fileName;
        m_object = null;
    }

    public void write(Object object) {
        try {
            FileOutputStream fos = m_context.openFileOutput(m_fileName, Context.MODE_PRIVATE);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            ObjectOutput out = new ObjectOutputStream(bos);
            try {
                out.writeObject(object);
            } finally {
                out.close();
            }
        } catch (FileNotFoundException e) {
            String message = e.getMessage();
        } catch (IOException e) {
            String message = e.getMessage();
        }
    }

    int getElementCount() {
        int count = 0;
        try {
            if (null != m_object) {
                count = ((Collection) m_object).size();
            } else {
                Collection object = (Collection) read();
                if (null != object) {
                    count = object.size();
                }
            }
        } catch (ClassCastException ignored) {
        }
        return count;
    }

    public Object read() {
        try {
            ObjectInput in = new ObjectInputStream(new BufferedInputStream(m_context.openFileInput(m_fileName)));
            try {
                m_object = in.readObject();
                return m_object;
            } finally {
                in.close();
            }
        } catch (ClassNotFoundException e) {
        } catch (StreamCorruptedException e) {
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }
        return null;
    }
}
