package org.yogurtcat.server.common.payment.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.yogurtcat.server.common.payment.domain.Payment;
import org.yogurtcat.server.common.payment.domain.PaymentVO;

import lombok.Builder;
import lombok.Data;

/**
 * 支付配置查询条件类
 * @author s
 *
 */
@Builder
@Data
public class PaymentSpec implements Specification<Payment> {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 条件
	 */
	@Builder.Default
	private Optional<PaymentVO> condition = Optional.empty();
	
	@Override
	public Predicate toPredicate(Root<Payment> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
		List<Predicate> predicates = new ArrayList<Predicate>();
		if(condition.isPresent()) {
			return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
		}else {
			return null;
		}
	}
}
