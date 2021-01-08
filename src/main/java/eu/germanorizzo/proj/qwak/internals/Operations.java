/*
 * CDDL HEADER START
 *
 * The contents of this file are subject to the terms of the
 * Common Development and Distribution License, Version 1.0 only
 * (the "License").  You may not use this file except in compliance
 * with the License.
 *
 * You can obtain a copy of the license at usr/src/OPENSOLARIS.LICENSE
 * or http://www.opensolaris.org/os/licensing.
 * See the License for the specific language governing permissions
 * and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL HEADER in each
 * file and include the License file.
 * If applicable, add the following below this CDDL HEADER, with the
 * fields enclosed by brackets "[]" replaced with your own identifying
 * information: Portions Copyright [yyyy] [name of copyright owner]
 *
 * CDDL HEADER END
 *
 * Copyright 2008 Aton S.p.A. (http://www.aton.eu).
 * Use is subject to license terms.
 */
package eu.germanorizzo.proj.qwak.internals;

import eu.germanorizzo.proj.qwak.internals.operations.*;

//see as reference http://en.cppreference.com/w/cpp/language/operator_precedence
public enum Operations implements Item {
    TO_BOOL("toBool", Type.UNARY_PREFIX, 6, Associativity.LEFT, new ToBool()),
    TO_NUM("toNum", Type.UNARY_PREFIX, 6, Associativity.LEFT, new ToNum()),
    TO_STR("toString", Type.UNARY_PREFIX, 6, Associativity.LEFT, new ToString()),
    ADD("+", Type.BINARY, 6, Associativity.LEFT, new Add()),
    SUBTRACT("-", Type.BINARY, 6, Associativity.LEFT, new Subtract()),
    MULTIPLY("*", Type.BINARY, 5, Associativity.LEFT, new Multiply()),
    DIVIDE("/", Type.BINARY, 5, Associativity.LEFT, new Divide()),
    REMAINDER("rem", Type.BINARY, 3, Associativity.LEFT, new Remainder()),
    POWER("pow", Type.BINARY, 3, null, new Power()),
    OPEN_BRACKET("(", Type.UNARY_PREFIX, 100, null, NullOp.INSTANCE),
    CLOSE_BRACKET(")", null, 200, null, NullOp.INSTANCE),
    COMMA(",", null, 200, null, NullOp.INSTANCE),
    ABS("abs", Type.UNARY_PREFIX, 3, null, new Abs()),
    MIN("min", Type.BINARY, 3, Associativity.LEFT, new Min()),
    MAX("max", Type.BINARY, 3, Associativity.LEFT, new Max()),
    SIGNUM("sig", Type.UNARY_PREFIX, 3, null, new Signum()),
    LENGTH("len", Type.UNARY_PREFIX, 3, null, new Length()),
    SUBSTR("substr", Type.TERNARY, 3, null, new Substr()),
    LEFT("left", Type.BINARY, 3, null, new Left()),
    RIGHT("right", Type.BINARY, 3, null, new Right()),
    TRIM("trim", Type.UNARY_PREFIX, 3, null, new Trim()),
    STARTSWITH("startsWith", Type.BINARY, 3, null, new StartsWith()),
    ENDSWITH("endsWith", Type.BINARY, 3, null, new EndsWith()),
    CONTAINS("contains", Type.BINARY, 3, null, new Contains()),
    EQUAL("==", Type.BINARY, 9, Associativity.LEFT, new Equals()),
    NOT_EQUAL("!=", Type.BINARY, 9, Associativity.LEFT, new NotEquals()),
    GREATER_EQ(">=", Type.BINARY, 8, Associativity.LEFT, new GreaterThanOrEqual()),
    LESSER_EQ("<=", Type.BINARY, 8, Associativity.LEFT, new LesserThanOrEqual()),
    /* The following 3 must be AFTER >=, <= and != */
    GREATER(">", Type.BINARY, 8, Associativity.LEFT, new GreaterThan()),
    LESSER("<", Type.BINARY, 8, Associativity.LEFT, new LesserThan()),
    AND("&&", Type.BINARY, 13, Associativity.LEFT, new And()),
    OR("||", Type.BINARY, 14, Associativity.LEFT, new Or()),
    NOT("~", Type.UNARY_PREFIX, 12, null, new Not());

    public enum Type {
        UNARY_POSTFIX(1), UNARY_PREFIX(1), BINARY(2), TERNARY(3);
        private int opNum;

        private Type(int opNum) {
            this.opNum = opNum;
        }

        public int getOpNum() {
            return opNum;
        }
    }

    public enum Associativity {
        LEFT, RIGHT;
    }

    public interface Performable {
        public Operand perform(Operand... operands) throws EvalException;
    }

    public final String literal;
    public final Type type;
    public final int priority;
    public final Associativity associativity;
    public final Performable action;

    private Operations(String literal, Type type, int priority,
                       Associativity associativity, Performable action) {
        this.literal = literal;
        this.type = type;
        this.priority = priority;
        this.associativity = associativity;
        this.action = action;
    }

    public static Operations getOperation(String token) {
        token = token.trim();
        for (Operations op : Operations.values())
            if (op.literal.equals(token))
                return op;
        return null;
    }
}
