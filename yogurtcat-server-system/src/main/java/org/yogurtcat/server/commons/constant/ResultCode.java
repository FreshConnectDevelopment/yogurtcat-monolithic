package org.yogurtcat.server.commons.constant;

import lombok.Getter;
import lombok.Setter;

/**
 * 相应结果编码
 * @author heaven
 *
 */
public enum ResultCode {
	
	NORMAL(20000L, "正常响应"), 
	AUTH_FAIL(20001L, "账号或密码错误"), 
	SYSTEM_ERROR(20002L, "系统异常"), 
	ACCESS_DENIED_ERROR(20003L, "无访问权限"),
	INPORT_ERROR(20004L, "文件%d行%d列数据不合法"),
	STORAGE_ERROR(20005L, "请配置存储方式"),
	NAME_EXITS_ERROR(20006L, "已存在此账户名，请更换账户名"),
	METHOD_ARGUMENT_NOT_VALID_ERROR(20007L, "方法校验失败"),
	ROLE_EXITS_ERROR(20006L, "已存在此角色名，请更换角色名"),
	ROLE_ERROR(20011L, "该角色已被用户关联,请先在用户管理取消关联,再删除~"),
	PERMISSION_ERROR(20011L, "该权限已被角色关联,请先在角色管理取消关联,再删除~"),
	MENU_ERROR(20011L, "该菜单已被角色关联,请先在角色管理取消关联,再删除~");
	
	@Setter
	@Getter
	private Long code;
	
	@Setter
	@Getter
	private String message;
	
	ResultCode(Long code, String message) {
		this.setCode(code);
		this.setMessage(message);
	}
	
}
