package org.yogurtcat.server.common.payment.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.Repository;
import org.yogurtcat.server.common.constant.PaymentType;
import org.yogurtcat.server.common.payment.domain.Payment;

/**
 * 支付配置dao
 * @author s
 *
 */
public interface PaymentRepository extends Repository<Payment, Long>, JpaSpecificationExecutor<Payment> {

	/**
	 * 新增或修改
	 * @param payment
	 * @return
	 */
	Payment save(Payment payment);

	/**
	 * 删除
	 * @param payment
	 */
	void delete(Payment payment);

	/**
	 * 根据类型查询
	 * @param type
	 * @return
	 */
	Payment findByType(PaymentType type);
}
