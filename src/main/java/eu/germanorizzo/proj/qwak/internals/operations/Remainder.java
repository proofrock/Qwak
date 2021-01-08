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
package eu.germanorizzo.proj.qwak.internals.operations;

import eu.germanorizzo.proj.qwak.internals.EvalException;
import eu.germanorizzo.proj.qwak.internals.Operand;
import eu.germanorizzo.proj.qwak.internals.Operations;

import java.math.BigDecimal;

public class Remainder implements Operations.Performable {
    @Override
    public Operand perform(Operand... operands) throws EvalException {
        if (operands.length != 2)
            throw new EvalException("Operands for REMAINDER must be 2, are " + operands.length);
        Operand o1 = operands[0];
        Operand o2 = operands[1];

        switch (o1.getType()) {
            case STRING:
                throw new EvalException("Cannot REMAINDER on strings");
            case NUM:
                switch (o2.getType()) {
                    case STRING:
                        throw new EvalException("Cannot REMAINDER on strings");
                    case NUM: {
                        return Operand.numItem(((BigDecimal) o1.getValue()).remainder((BigDecimal) o2.getValue()));
                    }
                    case BOOL:
                        throw new EvalException("Cannot REMAINDER on booleans");
                    case NULL:
                        throw new EvalException("Cannot REMAINDER on nulls");
                }
            case BOOL:
                throw new EvalException("Cannot REMAINDER on booleans");
            case NULL:
                throw new EvalException("Cannot REMAINDER on nulls");
        }

        throw new EvalException("Invalid parameters combination for REMAINDER");
    }
}
