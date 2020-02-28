package org.yogurtcat.server.modules.storage.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.yogurtcat.server.common.constant.BusinessType;
import org.yogurtcat.server.common.constant.Module;
import org.yogurtcat.server.common.constant.StorageType;
import org.yogurtcat.server.common.storage.domain.Storage;
import org.yogurtcat.server.common.utils.ExcelUtil;
import org.yogurtcat.server.common.utils.lang.ExcelData;
import org.yogurtcat.server.commons.constant.ResultCode;
import org.yogurtcat.server.commons.exception.BusinessException;
import org.yogurtcat.server.commons.response.ResponseResult;
import org.yogurtcat.server.modules.monitor.operationlog.annotation.OperationLog;
import org.yogurtcat.server.modules.storage.domain.StorageVO;
import org.yogurtcat.server.modules.storage.service.StorageService;

/**
 * 存储配置接口
 * @author s
 *
 */
@Controller
@RequestMapping("storage")
public class StorageController {
	
	@Autowired
	private StorageService storageService;
	
	/**
	 * 查询存储方式列表
	 * @param condition
	 * @return
	 * @throws BusinessException
	 */
	@ResponseBody
	@GetMapping(path = "list")
	public ResponseResult list(StorageVO condition, @RequestParam(defaultValue="20") Integer limit, 
			@RequestParam(defaultValue="1") Integer page) throws BusinessException {
		Page<Storage> list = storageService.list(Optional.ofNullable(condition), PageRequest.of(page - 1, limit));
		if(Optional.ofNullable(list).isPresent()) {
			return ResponseResult.builder().data(list).build();
		}else {
			throw BusinessException.builder().code(ResultCode.SYSTEM_ERROR.getCode()).message(ResultCode.SYSTEM_ERROR.getMessage()).build();
		}
	}
	
	@ResponseBody
	@OperationLog(logCode = "saveStorage", module = Module.新增文件存储配置)
	@PostMapping(path = "create", consumes = "application/json")
	public ResponseResult create(@RequestBody StorageVO data) throws BusinessException {
		Storage storage = storageService.save(data);
		return ResponseResult.builder().data(storage).build();
	}
	
	@ResponseBody
	@OperationLog(logCode = "storage", module = Module.修改文件存储配置)
	@PostMapping(path = "update", consumes = "application/json")
	public ResponseResult update(@RequestBody StorageVO data) throws BusinessException {
		Storage storage = storageService.save(data);
		return ResponseResult.builder().data(storage).build();
	}
	
	@ResponseBody
	@PostMapping(path = "delete", consumes = "application/json")
	public ResponseResult delete(@RequestBody StorageVO data) throws BusinessException {
		storageService.delete(data);
		return ResponseResult.builder().build();
	}
	
	
	/**
	 * 导出存储方式
	 * @param resp
	 * @throws Exception
	 */
	@PostMapping(path = "exportExcel")
	public void export(HttpServletResponse resp) throws Exception {
		ExcelData exceldata = new ExcelData();
		exceldata.setFileName("temp.xls");
		
		String[] head = {"存储名", "存储服务类型", "关联业务类型", "服务器IP", "bucketname", "endpoint", "accessKeyId", "accessKeySecret"};
		exceldata.setHead(head);
		
		List<Storage> list = storageService.findAll();
		List<String[]> data = new ArrayList<>();
		list.stream().forEach(storage -> {
			String[] s = new String[8];
			s[0] = storage.getName();
			s[1] = storage.getType().name();
			s[2] = storage.getBusinessType();
			s[3] = storage.getIp();
			s[4] = storage.getBucketname();
			s[5] = storage.getEndpoint();
			s[6] = storage.getAccessKeyId();
			s[7] = storage.getAccessKeySecret();
			data.add(s);
		});
		exceldata.setData(data);
		
		ExcelUtil.exportExcel(resp, exceldata);
	}
	
	/**
	 * 导出存储模板
	 * @param resp
	 * @throws Exception
	 */
	@PostMapping(path = "exportTemp")
	public void exportTemp(HttpServletResponse resp) throws Exception {
		ExcelData exceldata = new ExcelData();
		exceldata.setFileName("example.xls");
		
		String[] head = {"存储名", "存储服务类型", "关联业务类型", "服务器IP", "bucketname", "endpoint", "accessKeyId", "accessKeySecret"};
		exceldata.setHead(head);
		
		List<String[]> data = createTempData();
		exceldata.setData(data);
		
		ExcelUtil.exportExcel(resp, exceldata);
	}
	
	/**
	 * 模板数据
	 * @return
	 */
	private List<String[]> createTempData() {
		List<String[]> data = new ArrayList<>();

		String[] s = new String[8];
		s[0] = "阿里云OSS";
		s[1] = StorageType.阿里云OSS.name();
		s[2] = BusinessType.通用.name();
		s[4] = "xxxxxx";
		s[5] = "oss-cn-shenzhen.aliyuncs.com";
		s[6] = "xxxxxxxxxxxxx";
		s[7] = "xxxxxxxxxxxxxxxxxxxxx";
		data.add(s);
		
		s = new String[8];
		String str = Arrays.toString(BusinessType.values());
		s[0] = "本地存储";
		s[1] = StorageType.本地存储.name();
		s[2] = str.substring(1, str.length() - 1);
		s[3] = "10.10.10.10";
		data.add(s);
		
		return data;
	}

	/**
	 * 导入存储方式
	 * @param file
	 * @return
	 * @throws IOException
	 * @throws BusinessException 
	 */
	@ResponseBody
	@PostMapping(path = "importExcel")
	public ResponseResult importExcel(MultipartFile file) throws IOException, BusinessException {
		
		List<String[]> dataList = ExcelUtil.load(file.getInputStream(), file.getOriginalFilename(), 1, 2);
		for (int i = 0; i < dataList.size(); i++) {
			String[] data = dataList.get(i);
			StorageVO s = new StorageVO();
			s.setName(validData(data, i, 0, 2));
			try {
				s.setType(StorageType.valueOf(validData(data, i, 1, 2)));
			} catch (IllegalArgumentException e) {
				throw BusinessException.builder().code(ResultCode.INPORT_ERROR.getCode())
					.message(String.format(ResultCode.INPORT_ERROR.getMessage(), i + 2, 2)).build();
			}
			s.setBusinessType(validData(data, i, 2, 2));
			if (StorageType.本地存储 == s.getType()) {
				s.setIp(validData(data, i, 3, 2));
			} else if (StorageType.阿里云OSS == s.getType()) {
				s.setBucketname(validData(data, i, 4, 2));
				s.setEndpoint(validData(data, i, 5, 2));
				s.setAccessKeyId(validData(data, i, 6, 2));
				s.setAccessKeySecret(validData(data, i, 7, 2));
			} else {
				throw BusinessException.builder().code(ResultCode.INPORT_ERROR.getCode())
					.message(String.format(ResultCode.INPORT_ERROR.getMessage(), i + 2, 2)).build();
			}
			storageService.save(s);
		}
		return ResponseResult.builder().build();
	}
	
	/**
	 * 校验数据
	 * @param data
	 * @param i 行
	 * @param j 列
	 * @param startRow 初始行
	 * @return
	 * @throws BusinessException
	 */
	private String validData(String[] data, int i, int j, int startRow) throws BusinessException {
		if (j < data.length && !StringUtils.isEmpty(data[j])) {
			return data[j];
		}
		throw BusinessException.builder().code(ResultCode.INPORT_ERROR.getCode())
				.message(String.format(ResultCode.INPORT_ERROR.getMessage(), i + startRow, j + 1)).build();
		
	}
	
}
