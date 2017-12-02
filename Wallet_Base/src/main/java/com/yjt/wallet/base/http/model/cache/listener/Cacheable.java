package com.yjt.wallet.base.http.model.cache.listener;

import java.io.File;

public interface Cacheable {

    void write(Object object, File file);

    void write(Object object, String filePath);
    
    void delete(File file);
    
    void delete(String filePath);
}
