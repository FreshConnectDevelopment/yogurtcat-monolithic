package org.yogurtcat.server.modules.monitor.operationlog.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.yogurtcat.server.modules.monitor.operationlog.domain.OperationLog;
import org.yogurtcat.server.modules.monitor.operationlog.domain.OperationLogVo;

import io.netty.util.internal.StringUtil;
import lombok.Builder;
import lombok.Data;

/**
 * 管理员操作日志dao
 * @author s
 *
 */
@Builder
@Data
public class OperationLogSpec implements Specification<OperationLog> {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 条件
	 */
	@Builder.Default
	private Optional<OperationLogVo> condition = Optional.empty();

	@Override
	public Predicate toPredicate(Root<OperationLog> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
		List<Predicate> predicates = new ArrayList<Predicate>();
		if(condition.isPresent()) {
			if(!StringUtil.isNullOrEmpty(condition.get().getUsername())) {
				predicates.add(criteriaBuilder.equal(root.get("username"), condition.get().getUsername()));
			}
			query.orderBy(criteriaBuilder.desc(root.get("id")));
			return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
		}else {
			return null;
		}
	}

}
