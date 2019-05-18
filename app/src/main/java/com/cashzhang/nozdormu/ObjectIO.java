package com.cashzhang.nozdormu;

import android.content.Context;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
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
 * Created by cz21 on 2018/2/7.
 */


public class ObjectIO {
    private static final String TAG = ObjectIO.class.getSimpleName();
    private final Context m_context;
    private String m_fileName;
    private Object m_object;
    private int m_type;

    private static final int TYPE_FILE = 0;
    private static final int TYPE_COLLECTION = 1;
    private static final int TYPE_CONTENT = 2;

    public ObjectIO(Context context, String fileName, int type) {
        m_context = context;
        m_fileName = fileName;
        m_type = type;

    }
    public ObjectIO(Context context, int type) {
        m_context = context;
        m_type = type;
    }

    public void setNewFileName(String fileName) {
        m_fileName = fileName;
        m_object = null;
    }

    public boolean write(Object object) {
        try {
            StringBuilder stringBuilder = new StringBuilder();

            switch (m_type) {
                case TYPE_COLLECTION:
                    stringBuilder.append(m_context.getExternalFilesDir("collection"));
                    break;
                case TYPE_CONTENT:
                    stringBuilder.append(m_context.getExternalFilesDir("content"));
                    break;
                default:
                    break;
            }
            File file = new File(stringBuilder.toString()+"/"+m_fileName);
            Log.d(TAG, "write: filePath="+file.getAbsolutePath());
            if (!file.exists())
                file.createNewFile();
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));

            try {
                out.writeObject(object);
                return true;
            } finally {
                out.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
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
            StringBuilder stringBuilder = new StringBuilder();
            switch (m_type) {
                case 1:
                    stringBuilder.append(m_context.getExternalFilesDir("collection"));
                    break;
                case 2:
                    stringBuilder.append(m_context.getExternalFilesDir("content"));
                    break;
                default:
                    break;
            }
            File file = new File(stringBuilder.toString()+"/"+m_fileName);
            Log.d(TAG, "write: filePath="+file.getAbsolutePath());

            ObjectInput in = new ObjectInputStream(new FileInputStream(file));
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
