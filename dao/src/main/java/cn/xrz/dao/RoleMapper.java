package cn.xrz.dao;

import cn.xrz.pojo.Role;

import java.util.List;

/**
 * @author XRZ
 * @date 2019\2\27 0027
 * @Description :
 */
public interface RoleMapper {

    /**
     * 获取所有角色列表
     * @return
     */
    public List<Role> getRoleList();
}
