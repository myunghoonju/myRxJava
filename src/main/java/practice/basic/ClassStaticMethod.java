package practice.basic;

import io.reactivex.rxjava3.functions.Function;

public class ClassStaticMethod {

    public static void main(String[] args) throws Throwable {
        Function<String, Integer> stringIntegerFunction = (String s) -> Integer.parseInt(s);
        Integer result1 = stringIntegerFunction.apply("3");

        Function<String, Integer> functionTwo = s -> Integer.parseInt(s);
        Integer result2 = functionTwo.apply("4");

        Function<String, Integer> function3 = Integer::parseInt;
        Integer result3 = function3.apply("5");

        System.out.println(result1);
        System.out.println(result2);
        System.out.println(result3);
    }
}
