package org.yogurtcat.server.common.payment.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.yogurtcat.server.common.constant.PaymentType;
import org.yogurtcat.server.common.payment.domain.Payment;
import org.yogurtcat.server.common.payment.domain.PaymentVO;

/**
 * 支付配置service
 * @author s
 *
 */
public interface PaymentService {

	/**
	 * 条件查询
	 * @param condition
	 * @param pageable
	 * @return
	 */
	Page<Payment> list(Optional<PaymentVO> condition, Pageable pageable);

	/**
	 * 新增或修改
	 * @param data
	 * @return
	 */
	Payment save(Payment data);

	/**
	 * 删除
	 * @param data
	 */
	void delete(PaymentVO data);
	
	/**
	 * 根据支付类型查找
	 * @param type
	 * @return
	 */
	Payment findByType(PaymentType type);
}
