# Qwak 0.1.0

Qwak is a small Java library that implements a micro-language to evaluate expressions.

Given an expression string and a Map with a set of variables, it allows to evaluate the expression.

The expression can be pre-compiled to speed up the evaluation.

It's very fast (*TODO: benchmarks*), doesn't have external dependencies and allows to simply embed logic into your application, without requiring a full scripting language infrastructure.

Example of usage:

```java
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
```

Of course, it can be used from any JVM-based language.

This is derived from a work I made for my company, Aton S.p.A., in 2008; copyright is theirs under the CDDL.