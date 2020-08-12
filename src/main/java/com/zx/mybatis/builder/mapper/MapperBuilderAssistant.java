package com.zx.mybatis.builder.mapper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zx
 * @date 2020/7/28 15:42
 */
public class MapperBuilderAssistant {
    private final String openToken = "#{";
    private final String closeToken = "}";

    private final List<String> orgSqlParameter = new ArrayList<>();

    public String parseSql(String text) {
        StringBuilder builder = new StringBuilder();
        if (text != null && text.length() > 0) {
            char[] src = text.toCharArray();
            int offset = 0;
            int start = text.indexOf(openToken, offset);
            //#{favouriteSection,jdbcType=VARCHAR}
            //这里是循环解析参数，参考GenericTokenParserTest,比如可以解析${first_name} ${initial} ${last_name} reporting.这样的字符串,里面有3个 ${}
            while (start > -1) {
                //判断一下 ${ 前面是否是反斜杠，这个逻辑在老版的mybatis中（如3.1.0）是没有的
                if (start > 0 && src[start - 1] == '\\') {
                    // the variable is escaped. remove the backslash.
                    //新版已经没有调用substring了，改为调用如下的offset方式，提高了效率
                    //issue #760
                    builder.append(src, offset, start - offset - 1).append(openToken);
                    offset = start + openToken.length();
                } else {
                    int end = text.indexOf(closeToken, start);
                    if (end == -1) {
                        builder.append(src, offset, src.length - offset);
                        offset = src.length;
                    } else {
                        builder.append(src, offset, start - offset);
                        offset = start + openToken.length();
                        String content = new String(src, offset, end - offset);
                        //得到一对大括号里的字符串后，调用handler.handleToken,比如替换变量这种功能
                        //builder.append(handler.handleToken(content));
                        orgSqlParameter.add(content);
                        builder.append("?");
                        offset = end + closeToken.length();
                    }
                }
                start = text.indexOf(openToken, offset);
            }
            if (offset < src.length) {
                builder.append(src, offset, src.length - offset);
            }
        }
        return builder.toString();
    }

    public List<String> getOrgSqlParameter() {
        return orgSqlParameter;
    }

    public void resetOrgSqlParameter() {
        orgSqlParameter.clear();
    }
}
