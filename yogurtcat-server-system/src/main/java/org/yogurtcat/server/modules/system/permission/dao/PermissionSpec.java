package org.yogurtcat.server.modules.system.permission.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.yogurtcat.server.modules.system.permission.domain.Permission;
import org.yogurtcat.server.modules.system.permission.domain.PermissionVo;

import io.netty.util.internal.StringUtil;
import lombok.Builder;
import lombok.Data;

/**
 * 权限查询条件类
 * @author s
 *
 */
@Builder
@Data
public class PermissionSpec implements Specification<Permission> {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 条件
	 */
	@Builder.Default
	private Optional<PermissionVo> condition = Optional.empty();

	@Override
	public Predicate toPredicate(Root<Permission> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
		List<Predicate> predicates = new ArrayList<Predicate>();
		if(condition.isPresent()) {
			if(!StringUtil.isNullOrEmpty(condition.get().getName())) {
				predicates.add(criteriaBuilder.like(root.get("name"), "%" + condition.get().getName() + "%"));
			}
			return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
		}else {
			return null;
		}
	}

}
