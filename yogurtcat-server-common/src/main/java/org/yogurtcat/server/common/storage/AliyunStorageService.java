package org.yogurtcat.server.common.storage;

import java.io.InputStream;

import org.springframework.stereotype.Service;
import org.yogurtcat.server.common.storage.domain.Storage;

import com.aliyun.oss.OSSClient;

import lombok.extern.slf4j.Slf4j;

/**
 * 阿里云oss存储服务类
 * @author s
 *
 */
@Slf4j
@Service
public class AliyunStorageService {
	
    /**
     * 获取oss路径
     * @param storage 
     * @return
     */
    private String getBaseUrl(Storage storage) {
    	return "https://" + storage.getBucketname() + "." + storage.getEndpoint() + "/";
    }
	
	/**
     * 获取阿里云OSS客户端对象
	 * @param storage 
     *
     * @return ossClient
     */
    private OSSClient getOssClient(Storage storage) {
        return new OSSClient(storage.getEndpoint(), storage.getAccessKeyId(), storage.getAccessKeySecret());
    }
	
    public String store(InputStream inputStream, String keyName, Storage storage) {
		String url = null;
        try {
            getOssClient(storage).putObject(storage.getBucketname(), keyName, inputStream);
            
            url = getBaseUrl(storage) + keyName;
        } catch (Exception e) {
            log.error("Aliyun oss upload fail.", e);
            throw new RuntimeException();
        }
        return url;
    }
	
    public void delete(String keyName, Storage storage) {
        try {
            getOssClient(storage).deleteObject(storage.getBucketname(), keyName);
        } catch (Exception e) {
        	log.error("Aliyun oss delete fail.", e);
        }

    }
}
