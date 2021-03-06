package org.basex.query.expr;

import org.basex.query.*;
import org.basex.query.util.collation.*;
import org.basex.query.value.item.*;
import org.basex.query.var.*;
import org.basex.util.*;
import org.basex.util.hash.*;

/**
 * General comparison.
 *
 * @author BaseX Team 2005-17, BSD License
 * @author Christian Gruen
 */
public final class CmpAtomicG extends CmpG {
  /**
   * Constructor.
   * @param expr1 first expression
   * @param expr2 second expression
   * @param op operator
   * @param coll collation (can be {@code null})
   * @param sc static context
   * @param info input info
   */
  public CmpAtomicG(final Expr expr1, final Expr expr2, final OpG op, final Collation coll,
      final StaticContext sc, final InputInfo info) {
    super(expr1, expr2, op, coll, sc, info);
  }

  @Override
  public Expr optimize(final CompileContext cc) throws QueryException {
    // pre-evaluate values
    return allAreValues() ? cc.preEval(this) : this;
  }

  @Override
  public Bln item(final QueryContext qc, final InputInfo ii) throws QueryException {
    final Item it1 = exprs[0].item(qc, info);
    if(it1 == null) return Bln.FALSE;
    final Item it2 = exprs[1].item(qc, info);
    return Bln.get(it2 != null && eval(it1, it2));
  }

  @Override
  public CmpG copy(final CompileContext cc, final IntObjMap<Var> vm) {
    return new CmpAtomicG(exprs[0].copy(cc, vm), exprs[1].copy(cc, vm), op, coll, sc, info);
  }

  @Override
  public String description() {
    return "atomic '" + op + "' operator";
  }
}
