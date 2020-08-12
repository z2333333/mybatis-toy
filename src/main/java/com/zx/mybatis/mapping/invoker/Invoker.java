package com.zx.mybatis.mapping.invoker;

import java.lang.reflect.InvocationTargetException;



/**
 * 调用者
 *
 */
public interface Invoker {
  //调用
  Object invoke(Object target, Object[] args) throws IllegalAccessException, InvocationTargetException;

  //取得类型
  Class<?> getType();
}
