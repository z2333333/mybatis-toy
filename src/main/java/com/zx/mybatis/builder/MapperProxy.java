package com.zx.mybatis.builder;


import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 映射器代理，代理模式
 *
 */
public class MapperProxy<T> implements InvocationHandler, Serializable {

  private static final long serialVersionUID = -6424540398559729838L;
  private final Class<T> mapperInterface;

  public MapperProxy(Class<T> mapperInterface) {
    this.mapperInterface = mapperInterface;
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    //代理以后，所有Mapper的方法调用时，都会调用这个invoke方法
    //并不是任何一个方法都需要执行调用代理对象进行执行，如果这个方法是Object中通用的方法（toString、hashCode等）无需执行
    if (Object.class.equals(method.getDeclaringClass())) {
      try {
        return method.invoke(this, args);
      } catch (Throwable t) {

      }
    }
    //这里优化了，去缓存中找MapperMethod
    //final MapperMethod mapperMethod = cachedMapperMethod(method);
    //执行
    //return mapperMethod.execute(sqlSession, args);
      return method.invoke(this, args);
  }

  //去缓存中找MapperMethod
//  private MapperMethod cachedMapperMethod(Method method) {
//    MapperMethod mapperMethod = methodCache.get(method);
//    if (mapperMethod == null) {
//      //找不到才去new
//      mapperMethod = new MapperMethod(mapperInterface, method, sqlSession.getConfiguration());
//      methodCache.put(method, mapperMethod);
//    }
//    return mapperMethod;
//  }

}
