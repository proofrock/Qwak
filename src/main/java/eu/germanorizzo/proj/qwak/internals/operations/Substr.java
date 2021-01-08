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
import eu.germanorizzo.proj.qwak.internals.ValuedItem;

public class Substr implements Operations.Performable {
    @Override
    public Operand perform(Operand... operands) throws EvalException {
        if (operands.length != 3)
            throw new EvalException("Operands for SUBSTR must be 3, are " + operands.length);
        Operand o1 = operands[0];
        Operand o2 = operands[1];
        Operand o3 = operands[2];

        if (!(o1.getType() == ValuedItem.Type.STRING || o1.getType() == ValuedItem.Type.NULL) || o2.getType() != ValuedItem.Type.NUM || o3.getType() != ValuedItem.Type.NUM)
            throw new EvalException("Operands for SUBSTR must be [String|Null], Num and Num");

        String v1 = (String) o1.getValue();
        int v2 = o2.coalesceToInt("Second argument for SUBSTR must be an integer");
        int v3 = o3.coalesceToInt("Third argument for SUBSTR must be an integer");

        if (o1.getType() == ValuedItem.Type.NULL)
            return o1;

        v2 = Math.max(0, v2);
        v3 = Math.max(Math.min(v3, v1.length()), v2);
        return Operand.strItem(v1.substring(v2, v3));
    }
}
