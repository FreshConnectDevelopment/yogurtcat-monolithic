package org.yogurtcat.server.modules.payment.controller;

import java.io.IOException;
import java.util.Optional;

import org.aspectj.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.yogurtcat.server.common.payment.domain.Payment;
import org.yogurtcat.server.common.payment.domain.PaymentVO;
import org.yogurtcat.server.common.payment.service.PaymentService;
import org.yogurtcat.server.commons.constant.ResultCode;
import org.yogurtcat.server.commons.exception.BusinessException;
import org.yogurtcat.server.commons.response.ResponseResult;

/**
 * 支付方式配置接口
 * @author s
 *
 */
@RestController
@RequestMapping("payment")
public class PaymentController {

	@Autowired
	private PaymentService paymentService;
	
	/**
	 * 查询全部支付方式
	 * @param condition
	 * @return
	 * @throws BusinessException
	 */
	@GetMapping(path = "list")
	public ResponseResult list(PaymentVO condition, @RequestParam(defaultValue="20") Integer limit, 
			@RequestParam(defaultValue="1") Integer page) throws BusinessException {
		Page<Payment> list = paymentService.list(Optional.ofNullable(condition), PageRequest.of(page - 1, limit));
		if(Optional.ofNullable(list).isPresent()) {
			return ResponseResult.builder().data(list).build();
		}else {
			throw BusinessException.builder().code(ResultCode.SYSTEM_ERROR.getCode()).message(ResultCode.SYSTEM_ERROR.getMessage()).build();
		}
	}
	
	@PostMapping(path = "update")
	public ResponseResult update(MultipartFile file, PaymentVO data) throws BusinessException, IOException {
		Payment payment = paymentService.findByType(data.getType());
		if (null == payment) {
			payment = new Payment();
			payment.setType(data.getType());
		}
		payment.setAppId(data.getAppId());
		payment.setMchId(data.getMchId());
		payment.setApiSecret(data.getApiSecret());
		if (null != file) {
			payment.setCertName(file.getOriginalFilename());
			payment.setCertStream(FileUtil.readAsByteArray(file.getInputStream()));
		}else if(ObjectUtils.isEmpty(data.getCertName())){
			payment.setCertName(null);
			payment.setCertStream(null);
		}
		
		Payment project = paymentService.save(payment);
		return ResponseResult.builder().data(project).build();
	}
	
	@PostMapping(path = "delete", consumes = "application/json")
	public ResponseResult delete(@RequestBody PaymentVO data) throws BusinessException {
		paymentService.delete(data);
		return ResponseResult.builder().build();
	}

}
