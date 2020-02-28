package org.yogurtcat.server.modules.system.user.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.yogurtcat.server.modules.system.user.domain.User;
import org.yogurtcat.server.modules.system.user.domain.UserVo;

import io.netty.util.internal.StringUtil;
import lombok.Builder;
import lombok.Data;

/**
 * 管理员查询条件类
 * @author s
 *
 */
@Builder
@Data
public class UserSpec implements Specification<User> {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 条件
	 */
	@Builder.Default
	private Optional<UserVo> condition = Optional.empty();

	@Override
	public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
		List<Predicate> predicates = new ArrayList<Predicate>();
		if(condition.isPresent()) {
			if(!StringUtil.isNullOrEmpty(condition.get().getUsername())) {
				predicates.add(criteriaBuilder.equal(root.get("username"), condition.get().getUsername()));
			}
			if(!StringUtil.isNullOrEmpty(condition.get().getPassword())) {
				predicates.add(criteriaBuilder.equal(root.get("password"), condition.get().getPassword()));
			}
			if(!StringUtil.isNullOrEmpty(condition.get().getToken())) {
				predicates.add(criteriaBuilder.equal(root.get("token"), condition.get().getToken()));
			}
			return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
		}else {
			return null;
		}
	}

}
