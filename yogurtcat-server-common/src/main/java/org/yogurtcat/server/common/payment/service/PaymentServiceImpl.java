package org.yogurtcat.server.common.payment.service;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.yogurtcat.server.common.constant.PaymentType;
import org.yogurtcat.server.common.payment.dao.PaymentRepository;
import org.yogurtcat.server.common.payment.dao.PaymentSpec;
import org.yogurtcat.server.common.payment.domain.Payment;
import org.yogurtcat.server.common.payment.domain.PaymentVO;

/**
 * 支付配置serviceImpl
 * @author s
 *
 */
@Service
public class PaymentServiceImpl implements PaymentService {
	
	@Autowired
	private PaymentRepository paymentRepository;

	@Override
	public Page<Payment> list(Optional<PaymentVO> condition, Pageable pageable) {
		Page<Payment> page = paymentRepository.findAll(PaymentSpec.builder().condition(condition).build(), pageable);
		return page;
	}

	@Override
	public Payment save(Payment data) {
		data = paymentRepository.save(data);
		return data;
	}

	@Override
	public void delete(PaymentVO data) {
		Payment target = new Payment();
		BeanUtils.copyProperties(data, target);
		paymentRepository.delete(target);
	}

	@Override
	public Payment findByType(PaymentType type) {
		return paymentRepository.findByType(type);
	}

	
}
