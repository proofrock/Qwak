import eu.germanorizzo.proj.qwak.Evaluator;
import eu.germanorizzo.proj.qwak.internals.EvalException;
import eu.germanorizzo.proj.qwak.internals.Operand;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestEvaluator {
    private final Map<String, String> variables = new HashMap<>();

    @BeforeAll
    public void setUp() {
        variables.put("a", "0");
        variables.put("b", "1");
        variables.put("c", "2");
        variables.put("d", "d");
        variables.put("f", null);
        variables.put("g", "0.5");
        variables.put("h", "h");
        variables.put("i", "stringa");
        variables.put("j", "str");
        variables.put("k", "rin");
        variables.put("l", "nga");
        variables.put("m", "-1");
    }

    private Operand eval(String expression) throws ParseException, EvalException {
        Evaluator eval = Evaluator.compile(expression);
        return eval.evaluate(variables);
    }

    @Test
    public void testTrivial() throws ParseException, EvalException {
        assertEquals(eval("1"), Operand.numItem(BigDecimal.ONE));
        testTruth("1 == 1");
        testTruth("(%a == %b) || ((%a + %b) == 1)");
        testFalseness("(%a == %b) && ((%a + %b) == 1)");
    }

    @Test
    public void testNull() throws ParseException, EvalException {
        testTruth("NULL == NULL");
        testTruth("$e == NULL");
        testTruth("$f == NULL");
        testTruth("$e == $f");
    }

    @Test
    public void testNumeric() throws ParseException, EvalException {
        testTruth("%a == 0");
        testTruth("%a+%b == 1");
        testTruth("%b+%b == %c");
        testTruth("%b/%c == %g");
        testTruth("%g+%g == %b");
        testTruth("%g*4 ==  %c");
        testTruth("(%a == %b) || ((%a + %b) == 1)");
        testFalseness("(%a == %b) && ((%a + %b) == 1)");
        testTruth("%m < 0");
        testTruth("%m -1 == -2");
        testTruth("%m - 1 == -2");
        testTruth("1-%m == 2");
        testTruth("1- %m == 2");
        testTruth("1 -%m == 2");
        testTruth("1 - %m == 2");

        testTruth("pow(2,3)==8");
    }

    @Test
    public void testStrings() throws ParseException, EvalException {
        testTruth("$a == \"0\"");
        testTruth("($a+$d) == \"0d\"");
        testTruth("($h+$d) == \"hd\"");
        testTruth("startsWith(\"hd\", $h)");
        testTruth("endsWith(\"hd\", $d)");
        testTruth("startsWith($i, $j)");
        testTruth("endsWith($i, $l)");
        testTruth("contains($i, $j)");
        testTruth("contains($i, $k)");
        testTruth("contains($i, $l)");
        testFalseness("endsWith($i, $j)");
        testFalseness("startsWith($i, $l)");
        testTruth("trim($i+\" \") == $i");
        testTruth("left($i, 3) == $j");
        testTruth("right($i, 3) == $l");
        testTruth("right(left($i, 5), 3) == $k");
        testTruth("$j+\"i\"+$l == $i");

        testTruth("toString(%g*2) == $b");
        testTruth("toString(%g+%g)+\"\" == $b+\"\"");
        testTruth("trim(\" \"+toString(%g+%g)) == $b+\"\"");
    }

    @Test
    public void testStringComplex() throws ParseException, EvalException {
        // see bug 161
        testTruth("'a' + 1 == 'a' + '1'");

        // notice that 0.5 + 0.5 = 1, not 1.0
        testTruth("'a' + (0.5 + 0.5) == 'a'+'1'");

        testTruth("%g+%g + 'a' == '1a'"); //($g+$g)+"a"
        testTruth("\"a\" + $g + %g == \"a0.50.5\""); //("a"+$g)+$g
        testTruth("\"\"+(%g+%g) == toString(1)"); //(""+$g)+$g = $g+$g = 0.5+0.5 = 1.0

        testTruth(
                "left($i, 3) + right(left($i, 4), 1) + right($i, 3) == $i");

        testTruth("$d + (%g + %g) == $d + $b");
    }

    @Test
    public void testAssociativity() throws ParseException, EvalException {
        // see bug XXX
        testTruth("(NULL == NULL) || startsWith(\"xyz\", \"xy\")");
        testTruth("NULL == NULL || startsWith(\"xyz\", \"xy\")");

        testTruth("2 + 3 == 10 - 5");
        assertEquals(eval("toNum(2 + toNum(3 == 3) + 2 == 5) - 2 * 0.5"), eval("0"));
    }

    @Test
    public void testBoolean() throws ParseException, EvalException {
        testTruth("~false");
        testFalseness("false");

        testTruth("true");
        testFalseness("~true");

        testFalseness("false && true");
        testTruth("true && true");

        testFalseness("false || false");
        testTruth("true || false");

        testTruth("~false && true");
        testFalseness("~true && true");

        testTruth("~false || false");
        testFalseness("~true || false");

        testTruth("~?a || ?a");
        testFalseness("~?b || ?a");
    }

    @Test
    public void testWrongNumOfOperands() {
        testFailCompilation("(5*2)8");
    }

    private void testTruth(String expression) throws ParseException, EvalException {
        Evaluator eval = Evaluator.compile(expression);
        assertEquals(eval.evaluate(variables), Operand.TRUE);
    }

    private void testFalseness(String expression) throws ParseException, EvalException {
        Evaluator eval = Evaluator.compile(expression);
        assertEquals(eval.evaluate(variables), Operand.FALSE);
    }

    private void testFailCompilation(String expression) {
        try {
            Evaluator.compile(expression);
            fail();
        } catch (Exception e) {
        }
    }
}
