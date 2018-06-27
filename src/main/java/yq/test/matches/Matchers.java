package yq.test.matches;

import org.hamcrest.Matcher;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static org.hamcrest.Matchers.anyOf;

public class Matchers {

    /**
     * 使用anyOf一次为所有调用传参
     * @param ts
     * @param f
     * @param <T>
     * @param <S>
     * @return
     */
    public static<T,S> Matcher<S> anyOfWith(List<T> ts, Function<T,Matcher<S>> f) {
        List<Matcher<S>> col = new ArrayList<>();
        for (T t : ts) {
            col.add(f.apply(t));
        }
        return anyOf((Iterable)col);
    }
}
