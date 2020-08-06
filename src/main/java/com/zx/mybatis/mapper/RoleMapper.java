package com.zx.mybatis.mapper;


/**
 * @author zx
 * @date 2020/3/5 11:26
 */
public interface RoleMapper {
    Role getRole(Long id);
    Role findRole(String roleName);
    int deleteRole(Long id);
    int insertRole(Role role);
}
