package yq.test.utils;

import java.util.Collection;
import java.util.Map;
import java.util.regex.Pattern;

public class Judge {

    /**
     * 使用正则表达式匹配字符串
     * @param str 欲匹配的字符串
     * @param regexText 正则字符串
     * @return 匹配 true
     */
    public static boolean isMatched(String str, String regexText) {
        // 编译正则
        Pattern pattern = Pattern.compile(regexText);
        // 创建matcher对象
        java.util.regex.Matcher m = pattern.matcher(str);
        return m.matches();
    }

    public static <T> boolean isNone( T  t ) {

        if(t instanceof String) return ((String) t).isEmpty();
        if(t instanceof Collection) return ((Collection) t).isEmpty();
        if(t instanceof Map)    return ((Map) t).isEmpty();

        return t == null;
    }

    public static <T> boolean notNone( T  t ){
        return ! isNone(t);
    }
}
