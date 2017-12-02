package com.yjt.wallet.base.http.model.cache.listener.implement;

import com.yjt.wallet.base.http.model.cache.listener.Cacheable;
import com.yjt.wallet.components.utils.LogUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public abstract class CacheableImplement implements Serializable, Cacheable {

    @Override
    public void write(Object object, File file) {
        if (object != null && file != null) {
            LogUtil.getInstance().print("执行写文件操作：" + file.getAbsolutePath());
            ByteArrayOutputStream byteArrayOutputStream = null;
            ObjectOutputStream objectOutputStream = null;
            FileOutputStream fileOutputStream = null;
            try {
                byteArrayOutputStream = new ByteArrayOutputStream();
                objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                objectOutputStream.writeObject(object);
                objectOutputStream.flush();
                fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(byteArrayOutputStream.toByteArray());
                fileOutputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (byteArrayOutputStream != null) {
                        byteArrayOutputStream.close();
                    }
                    if (objectOutputStream != null) {
                        objectOutputStream.close();
                    }
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void write(Object object, String filePath) {
        write(object, new File(filePath));
    }

    @Override
    public void delete(File file) {
        if (file != null && file.exists()) {
            if (file.isFile()) {
                LogUtil.getInstance().print("删除单个文件" + file.getAbsolutePath());
                file.delete();
                return;
            }
            if (file.isDirectory()) {
                LogUtil.getInstance().print("删除一个文件夹开始" + file.getAbsolutePath());
                File[] childFile = file.listFiles();
                if (childFile == null || childFile.length == 0) {
                    LogUtil.getInstance().print("删除空文件夹" + file.getAbsolutePath());
                    file.delete();
                    return;
                }
                for (File f : childFile) {
                    LogUtil.getInstance().print("删除子文件或文件夹" + file.getAbsolutePath());
                    delete(f);
                }
                file.delete();
                LogUtil.getInstance().print("删除一个文件夹结束" + file.getAbsolutePath());
            }
        } else {
            LogUtil.getInstance().print("无需执行删除操作");
        }
    }

    @Override
    public void delete(String directoryPath) {
        delete(new File(directoryPath));
    }
}
