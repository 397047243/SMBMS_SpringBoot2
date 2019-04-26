package cn.xrz.service.impl;


import cn.xrz.dao.RoleMapper;
import cn.xrz.pojo.Role;
import cn.xrz.service.RoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("roleService")
public class RoleServiceImpl implements RoleService {

	@Resource
	private RoleMapper roleMapper;

	@Override
	public List<Role> getRoleList() {
		return this.roleMapper.getRoleList();
	}
}
