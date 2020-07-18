package com.zx.mybatis.builder.mapper;

import com.zx.mybatis.util.ResolverUtil;

import java.util.Set;

/**
 * @author zx
 * @date 2020/6/9 15:40
 */
public class MapperRegistry {
    public static void main(String[] args) {
        //addMappers("com.zx.mybatis.mapper",Object.class);
        try {
            Class<?> loadClass = Thread.currentThread().getContextClassLoader().loadClass("com.zx.mybatis.mapper.RoleMapper");
            addMapper(loadClass);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void addMappers(String packageName, Class<?> superType) {
        //查找包下所有是superType的类
        ResolverUtil<Class<?>> resolverUtil = new ResolverUtil<Class<?>>();
        resolverUtil.find(new ResolverUtil.IsA(superType), packageName);
        Set<Class<? extends Class<?>>> mapperSet = resolverUtil.getClasses();
        for (Class<?> mapperClass : mapperSet) {
            addMapper(mapperClass);
        }
    }

    protected Set<Class<? extends Class<?>>> findClassByPackageName(String packageName) {
        ResolverUtil<Class<?>> resolverUtil = new ResolverUtil<Class<?>>();
        resolverUtil.find(new ResolverUtil.IsA(Object.class), packageName);
        Set<Class<? extends Class<?>>> mapperSet = resolverUtil.getClasses();
        return mapperSet;
    }

    public static  <T> void addMapper(Class<T> type) {
        //mapper必须是接口！才会添加
//        if (type.isInterface()) {
//            MapperProxyFactory mapperProxyFactory = new MapperProxyFactory(type);
//            Object instance = mapperProxyFactory.newInstance();
//            System.out.println(instance instanceof RoleMapper);
//            System.out.print("\n"+"subject中的方法有：");
//
//            Method[] method=instance.getClass().getDeclaredMethods();
//
//            for(Method m:method){
//                System.out.print(m.getName()+", ");
//            }
//
//            RoleMapper roleMapper = (RoleMapper) instance;
//            roleMapper.findRole("1");
//        }
    }
}
