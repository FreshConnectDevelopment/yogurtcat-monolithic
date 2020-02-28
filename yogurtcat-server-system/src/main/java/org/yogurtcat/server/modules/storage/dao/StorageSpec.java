package org.yogurtcat.server.modules.storage.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.yogurtcat.server.common.storage.domain.Storage;
import org.yogurtcat.server.modules.storage.domain.StorageVO;

import lombok.Builder;
import lombok.Data;

/**
 * 存储查询条件
 * @author s
 *
 */
@Builder
@Data
public class StorageSpec implements Specification<Storage> {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 条件
	 */
	@Builder.Default
	private Optional<StorageVO> condition = Optional.empty();

	@Override
	public Predicate toPredicate(Root<Storage> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
		List<Predicate> predicates = new ArrayList<Predicate>();
		if(condition.isPresent()) {
			if(!StringUtils.isEmpty(condition.get().getName())) {
				predicates.add(criteriaBuilder.equal(root.get("name"), condition.get().getName()));
			}
			return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
		}else {
			return null;
		}
	}

}
