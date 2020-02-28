package org.yogurtcat.server.modules.system.menu.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.Repository;
import org.yogurtcat.server.modules.system.menu.domain.Menu;

/**
 * 菜单dao
 * @author s
 *
 */
public interface MenuRepository extends Repository<Menu, Long>, JpaSpecificationExecutor<Menu> {

	/**
	 * 新增或修改
	 * @param value
	 */
	void save(Menu value);

	/**
	 * 根据父id查询子菜单
	 * @param pid
	 * @return
	 */
	List<Menu> findAllByPid(long pid);

	/**
	 * 删除
	 * @param id
	 */
	void deleteById(Long id);
}
