package com.zx.mybatis.mapper;


/**
 * @author zx
 * @date 2020/3/5 11:26
 */
public interface RoleMapper {
    public Role getRole(Long id);
    public Role findRole(String roleName);
    public int deleteRole(Long id);
    public int insertRole(Role role);
}
