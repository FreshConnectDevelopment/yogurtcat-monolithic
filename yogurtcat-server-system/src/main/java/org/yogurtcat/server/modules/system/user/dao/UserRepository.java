package org.yogurtcat.server.modules.system.user.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.Repository;
import org.yogurtcat.server.modules.system.user.domain.User;

/**
 * 管理员dao
 * @author s
 *
 */
public interface UserRepository extends Repository<User, Long>, JpaSpecificationExecutor<User> {
	
	/**
	 * 新增或修改
	 * @param user
	 * @return
	 */
	User save(User user);

	/**
	 * 删除
	 * @param user
	 */
	void delete(User user);

	/**
	 * 根据username查找
	 * @param username
	 * @return
	 */
	User findByUsername(String username);
}
