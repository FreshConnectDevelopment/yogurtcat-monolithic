package org.yogurtcat.server.modules.system.role.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.SetJoin;

import org.springframework.data.jpa.domain.Specification;
import org.yogurtcat.server.modules.system.role.domain.Role;
import org.yogurtcat.server.modules.system.user.domain.User;

import io.netty.util.internal.StringUtil;
import lombok.Builder;
import lombok.Data;

/**
 * 角色查询条件类
 * @author s
 *
 */
@Builder
@Data
public class RoleSpec implements Specification<Role> {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 条件
	 */
	@Builder.Default
	private Optional<Role> condition = Optional.empty();
	
	/**
	 * 条件
	 */
	@Builder.Default
	private Optional<User> userCondition = Optional.empty();

	@Override
	public Predicate toPredicate(Root<Role> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
		List<Predicate> predicates = new ArrayList<Predicate>();
		if(condition.isPresent()) {
			if(!StringUtil.isNullOrEmpty(condition.get().getName())) {
				predicates.add(criteriaBuilder.equal(root.get("name"), condition.get().getName()));
			}
			return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
		}
		
		if(userCondition.isPresent()) {
			if(userCondition.get().getId() != null) {
				SetJoin<Role, User> users = root.join(root.getModel().getSet("users", User.class), JoinType.LEFT);
				predicates.add(users.in(userCondition.get().getId()));
			}
		}
		
		if(predicates.isEmpty()) {
			return null;
		}else {
			return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
		}
	}

}
