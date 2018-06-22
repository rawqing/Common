package yq.test.matches;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import java.util.regex.Pattern;

public class TextMatcher {

    /**
     * 使用正则表达式匹配Text
     * @param regexText
     * @return
     */
    public static Matcher<String> withRegexText(final String regexText) {
        return new BaseMatcher<String>() {
            @Override
            public boolean matches(Object item) {
                String text = item.toString();
                // 编译正则
                Pattern pattern = Pattern.compile(regexText);
                // 创建matcher对象
                java.util.regex.Matcher m = pattern.matcher(text);
                return m.matches();
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("with regex text: " + regexText);
            }
        };
    }

    /**
     * 使用通配符匹配Text
     * 目前限定通配符为 * , ? 两种
     * @param wildcardText
     * @return
     */
    public static Matcher<String> withWildcardText(final String wildcardText){
        String regexText = wildcardText.replace("*",".*").replace("?",".?");
        return withRegexText(regexText);
    }

    /**
     * 匹配包含字符串
     * @param containText
     * @return
     */
    public static Matcher<String> withContainText(final String containText) {
        return new BaseMatcher<String>() {
            @Override
            public boolean matches(Object item) {
                String text = item.toString();
                return text.contains(containText);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("with contain text: " + containText);
            }
        };
    }
}
