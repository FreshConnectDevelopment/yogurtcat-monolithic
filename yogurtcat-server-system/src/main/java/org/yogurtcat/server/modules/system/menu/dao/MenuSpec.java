package org.yogurtcat.server.modules.system.menu.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.yogurtcat.server.modules.system.menu.domain.Menu;
import org.yogurtcat.server.modules.system.menu.domain.MenuVo;

import io.netty.util.internal.StringUtil;
import lombok.Builder;
import lombok.Data;

/**
 * 菜单查询条件类
 * @author s
 *
 */
@Builder
@Data
public class MenuSpec implements Specification<Menu> {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 条件
	 */
	@Builder.Default
	private Optional<MenuVo> condition = Optional.empty();

	@Override
	public Predicate toPredicate(Root<Menu> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
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
