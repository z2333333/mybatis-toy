package com.zx.mybatis.builder;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Offline entity resolver for the MyBatis DTDs
 * 目的是未联网的情况下也能做DTD验证，实现原理就是将DTD搞到本地，然后用org.xml.sax.EntityResolver，最后调用DocumentBuilder.setEntityResolver来达到脱机验证
 * EntityResolver
 * public InputSource resolveEntity (String publicId, String systemId)
 * 应用程序可以使用此接口将系统标识符重定向到本地 URI
 * 但是用DTD是比较过时的做法，新的都改用xsd了
 * 这个类的名字并不准确，因为它被两个类都用到了（XMLConfigBuilder,XMLMapperBuilder）
 *
 * @author Clinton Begin
 */
public class XMLMapperEntityResolver implements EntityResolver {

  private static final Map<String, String> doctypeMap = new HashMap<String, String>();

	// <?xml version="1.0" encoding="UTF-8" ?>
	// <!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
	// 常量定义
  private static final String IBATIS_CONFIG_PUBLIC = "-//ibatis.apache.org//DTD Config 3.0//EN".toUpperCase(Locale.ENGLISH);
  private static final String IBATIS_CONFIG_SYSTEM = "http://ibatis.apache.org/dtd/ibatis-3-config.dtd".toUpperCase(Locale.ENGLISH);

  private static final String IBATIS_MAPPER_PUBLIC = "-//ibatis.apache.org//DTD Mapper 3.0//EN".toUpperCase(Locale.ENGLISH);
  private static final String IBATIS_MAPPER_SYSTEM = "http://ibatis.apache.org/dtd/ibatis-3-mapper.dtd".toUpperCase(Locale.ENGLISH);

  private static final String MYBATIS_CONFIG_PUBLIC = "-//mybatis.org//DTD Config 3.0//EN".toUpperCase(Locale.ENGLISH);
  private static final String MYBATIS_CONFIG_SYSTEM = "http://mybatis.org/dtd/mybatis-3-config.dtd".toUpperCase(Locale.ENGLISH);

  private static final String MYBATIS_MAPPER_PUBLIC = "-//mybatis.org//DTD Mapper 3.0//EN".toUpperCase(Locale.ENGLISH);
  private static final String MYBATIS_MAPPER_SYSTEM = "http://mybatis.org/dtd/mybatis-3-mapper.dtd".toUpperCase(Locale.ENGLISH);

  private static final String MYBATIS_CONFIG_DTD = "dtd/mybatis-3-config.dtd";
  private static final String MYBATIS_MAPPER_DTD = "dtd/mybatis-3-mapper.dtd";

  static {
		// 将DOCTYPE和URL都映射到本地类路径下的DTD
    doctypeMap.put(IBATIS_CONFIG_SYSTEM, MYBATIS_CONFIG_DTD);
    doctypeMap.put(IBATIS_CONFIG_PUBLIC, MYBATIS_CONFIG_DTD);

    doctypeMap.put(IBATIS_MAPPER_SYSTEM, MYBATIS_MAPPER_DTD);
    doctypeMap.put(IBATIS_MAPPER_PUBLIC, MYBATIS_MAPPER_DTD);

    doctypeMap.put(MYBATIS_CONFIG_SYSTEM, MYBATIS_CONFIG_DTD);
    doctypeMap.put(MYBATIS_CONFIG_PUBLIC, MYBATIS_CONFIG_DTD);

    doctypeMap.put(MYBATIS_MAPPER_SYSTEM, MYBATIS_MAPPER_DTD);
    doctypeMap.put(MYBATIS_MAPPER_PUBLIC, MYBATIS_MAPPER_DTD);
  }

  /*
   * Converts a public DTD into a local one
   *
   * @param publicId The public id that is what comes after "PUBLIC"
   * @param systemId The system id that is what comes after the public id.
   * @return The InputSource for the DTD
   *
   * @throws org.xml.sax.SAXException If anything goes wrong
   */
  @Override
  //核心就是覆盖这个方法，达到转public DTD到本地DTD的目的
  public InputSource resolveEntity(String publicId, String systemId) throws SAXException {

    if (publicId != null) {
      publicId = publicId.toUpperCase(Locale.ENGLISH);
    }
    if (systemId != null) {
      systemId = systemId.toUpperCase(Locale.ENGLISH);
    }

    InputSource source = null;
    try {
		//先找publicId，找不到再找systemId，貌似不可能找不到的说
      String path = doctypeMap.get(publicId);
      source = getInputSource(path, source);
      if (source == null) {
        path = doctypeMap.get(systemId);
        source = getInputSource(path, source);
      }
    } catch (Exception e) {
      throw new SAXException(e.toString());
    }
    return source;
  }

  private InputSource getInputSource(String path, InputSource source) {
    if (path != null) {
      InputStream in;
      try {
        in = Resources.getResourceAsStream(path);
        source = new InputSource(in);
      } catch (IOException e) {
        // ignore, null is ok
      }
    }
    return source;
  }

}
