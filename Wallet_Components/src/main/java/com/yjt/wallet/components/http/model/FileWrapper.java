package com.yjt.wallet.components.http.model;

import android.text.TextUtils;

import com.yjt.wallet.components.constant.Regex;

import java.io.File;

import okhttp3.MediaType;

public class FileWrapper {

    private File file;
    private String fileName;
    private MediaType mediaType;
    private long fileSize;

    public FileWrapper(File file) {
        this.file = file;
        this.fileName = file.getName();
        this.fileSize = file.length();
    }

    public FileWrapper(File file, MediaType mediaType) {
        this(file);
        this.mediaType = mediaType;
    }

    public void setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    public String getFileName() {
        if (!TextUtils.isEmpty(fileName)) {
            return fileName;

        } else {
            return Regex.NONE.getRegext();
        }
    }

    public File getFile() {
        return file;
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public long getFileSize() {
        return fileSize;
    }
}
