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

public class ToBool implements Operations.Performable {
    @Override
    public Operand perform(Operand... operands) throws EvalException {
        if (operands.length != 1)
            throw new EvalException("Operands for TO_BOOL must be 1, are " + operands.length);
        Operand o1 = operands[0];

        switch (o1.getType()) {
            case STRING:
                String s1 = (String) o1.getValue();
                return Operand.boolItem("true".equalsIgnoreCase(s1) || "1".equalsIgnoreCase(s1));
            case NUM:
                BigDecimal bd1 = (BigDecimal) o1.getValue();
                return Operand.boolItem(!BigDecimal.ZERO.equals(bd1));
            case BOOL:
                return o1;
            case NULL:
                return Operand.FALSE;
        }

        throw new EvalException("Invalid parameters combination for TO_BOOL");
    }
}
