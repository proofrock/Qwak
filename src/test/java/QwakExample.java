import eu.germanorizzo.proj.qwak.Evaluator;
import eu.germanorizzo.proj.qwak.internals.EvalException;
import eu.germanorizzo.proj.qwak.internals.Operand;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

public class QwakExample {
    public static void main(String[] args) throws ParseException, EvalException {
        final Map<String, String> vars = new HashMap<>();
        vars.put("myString", "Hello, World 1!");

        // extracts the 13th char from the variable, converts it to a number
        // and adds 1
        final String expressionString = "toNum(substr($myString, 13, 14)) + 1";

        // this can be put in a constant, it's also thread safe
        final Evaluator expr = Evaluator.compile(expressionString);

        // calculate the result
        final Operand result = expr.evaluate(vars);

        System.out.println(result.getType()); // NUM
        System.out.println(result.getValue()); // 2
    }
}
