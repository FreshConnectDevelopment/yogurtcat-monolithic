package org.yogurtcat.server.commons.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.yogurtcat.server.modules.system.user.domain.User;
import org.yogurtcat.server.modules.system.user.domain.UserVo;

/**
 * 用户实体类映射
 * @author s
 *
 */
@Mapper
public interface UserMapper {

	UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

	/**
	 * User to UserVo
	 * @param user
	 * @return
	 */
	UserVo toUserVo(User user);
}
