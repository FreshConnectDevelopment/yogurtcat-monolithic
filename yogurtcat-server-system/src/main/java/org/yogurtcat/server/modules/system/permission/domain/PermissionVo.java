package org.yogurtcat.server.modules.system.permission.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 角色信息
 * @author heaven
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class PermissionVo extends Permission {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 子节点列表
	 */
	private List<PermissionVo> children;
}
