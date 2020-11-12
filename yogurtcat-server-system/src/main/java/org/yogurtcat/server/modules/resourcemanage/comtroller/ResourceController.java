package org.yogurtcat.server.modules.resourcemanage.comtroller;

import java.io.File;
import java.util.Optional;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.yogurtcat.server.common.constant.BusinessType;
import org.yogurtcat.server.common.constant.StorageType;
import org.yogurtcat.server.common.seaweedfs.SeaweedfsService;
import org.yogurtcat.server.common.seaweedfs.domain.DeleteFilesOrDirectoryRequest;
import org.yogurtcat.server.common.seaweedfs.domain.UploadRequest;
import org.yogurtcat.server.common.storage.AliyunStorageService;
import org.yogurtcat.server.common.storage.domain.Storage;
import org.yogurtcat.server.common.utils.IdUtil;
import org.yogurtcat.server.commons.constant.ResultCode;
import org.yogurtcat.server.commons.exception.BusinessException;
import org.yogurtcat.server.commons.response.ResponseResult;
import org.yogurtcat.server.modules.resourcemanage.domain.Resource;
import org.yogurtcat.server.modules.resourcemanage.service.ResourceService;
import org.yogurtcat.server.modules.storage.service.StorageService;

import io.netty.util.internal.StringUtil;

/**
 * 资源管理接口类，包含统一上传接口
 * 代码需优化，还没想好怎么写
 * @author s
 *
 */
@RestController
@RequestMapping("resource")
public class ResourceController {
	
	private static final String POINT = ".";
	private static final String VIRGULE = "/";
	
	@Autowired
	private AliyunStorageService aliyunStorageService;
	
	@Autowired
	private SeaweedfsService seaweedfsService;

	@Autowired
	private ResourceService resourceService;
	
	@Autowired
	private StorageService storageService;
	
	/**
	 * 统一上传接口，并存储资源信息
	 * @param file
	 * @param businessType 上传资源的业务类型
	 * @param path 存放路径
	 * @return 上传结果
	 * @throws Exception
	 * @throws BusinessException 
	 */
	@PostMapping(value="upload/{businessType}/{path}")
	@ResponseBody
	public ResponseResult upload(@RequestParam("file")MultipartFile file, 
			@PathVariable BusinessType businessType, @PathVariable String path) 
			throws Exception, BusinessException{
		
		String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf(POINT));
		
		String filename = path + VIRGULE + IdUtil.simpleUuid() + suffix;
		
		//获取该业务类型上传配置，未找到默认使用本地存储方式
		String a = businessType.name();
		Storage storage = storageService.findByBusinessType(a);
		if (null == storage) {
			throw BusinessException.builder().code(ResultCode.STORAGE_ERROR.getCode())
				.message(ResultCode.STORAGE_ERROR.getMessage()).build();
		}
		
		//根据存储方式调用相应的上传接口
		String url = StringUtil.EMPTY_STRING;
		if (StorageType.本地存储 == storage.getType()) {
			File nf = new File(filename);
			FileUtils.copyInputStreamToFile(file.getInputStream(), nf);
			UploadRequest request = UploadRequest.builder().serverIp(storage.getIp()).file(nf).path(path + VIRGULE).build();
			url = seaweedfsService.upload(request).getUrl();
			FileUtils.deleteQuietly(nf);
		} else if (StorageType.阿里云OSS == storage.getType()) {
			url = aliyunStorageService.store(file.getInputStream(), filename, storage);
		}
		
		//保存资源记录
        Resource resource = new Resource();
        resource.setName(originalFilename);
        resource.setKeyName(filename);
        resource.setBusinessType(businessType);
        resource.setSize(file.getSize());
        resource.setType(file.getContentType());
        resource.setStorage(storage);
        resource.setUrl(url);
        resourceService.save(resource);
        
        return ResponseResult.builder().data(url).build();
	}
	
	
	/**
	 * 查询资源列表
	 * @param condition
	 * @return 资源列表
	 * @throws BusinessException
	 */
	@GetMapping(path = "list")
	public ResponseResult list(Resource condition, @RequestParam(defaultValue="20") Integer limit, 
			@RequestParam(defaultValue="1") Integer page) throws BusinessException {
		Page<Resource> list = resourceService.list(Optional.ofNullable(condition), PageRequest.of(page - 1, limit));
		if(Optional.ofNullable(list).isPresent()) {
			return ResponseResult.builder().data(list).build();
		}else {
			throw BusinessException.builder().code(ResultCode.SYSTEM_ERROR.getCode()).message(ResultCode.SYSTEM_ERROR.getMessage()).build();
		}
	}
	
	/**
	 * 删除资源数据，并在存储服务器删除文件
	 * @param data
	 * @return 删除资源结果
	 * @throws BusinessException
	 */
	@PostMapping(path = "delete", consumes = "application/json")
	public ResponseResult delete(@RequestBody Resource data) throws BusinessException {
		StorageType type = data.getStorage().getType();
		if (StorageType.本地存储 == type) {
			DeleteFilesOrDirectoryRequest request = DeleteFilesOrDirectoryRequest.builder()
					.isDirectory(false).serverIp(data.getStorage().getIp()).path(data.getKeyName()).build();
			seaweedfsService.deleteFilesOrDirectory(request);
		} else if (StorageType.阿里云OSS == type) {
			aliyunStorageService.delete(data.getKeyName(), data.getStorage());
		}
		
		resourceService.delete(data);
		return ResponseResult.builder().build();
	}

}
