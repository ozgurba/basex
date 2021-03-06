package org.basex.query.expr;

import static org.basex.query.QueryText.*;

import org.basex.query.*;
import org.basex.query.value.item.*;
import org.basex.query.value.node.*;
import org.basex.query.value.type.*;
import org.basex.query.var.*;
import org.basex.util.*;
import org.basex.util.hash.*;

/**
 * Instance test.
 *
 * @author BaseX Team 2005-17, BSD License
 * @author Christian Gruen
 */
public final class Instance extends Single {
  /** Sequence type to check for. */
  private final SeqType type;

  /**
   * Constructor.
   * @param info input info
   * @param expr expression
   * @param type sequence type to check for
   */
  public Instance(final InputInfo info, final Expr expr, final SeqType type) {
    super(info, expr);
    this.type = type;
    seqType = SeqType.BLN;
  }

  @Override
  public Expr compile(final CompileContext cc) throws QueryException {
    return super.compile(cc).optimize(cc);
  }

  @Override
  public Bln item(final QueryContext qc, final InputInfo ii) throws QueryException {
    return Bln.get(type.instance(qc.value(expr)));
  }

  @Override
  public Expr copy(final CompileContext cc, final IntObjMap<Var> vm) {
    return new Instance(info, expr.copy(cc, vm), type);
  }

  @Override
  public boolean equals(final Object obj) {
    return this == obj || obj instanceof Instance && type.eq(((Instance) obj).type) &&
        super.equals(obj);
  }

  @Override
  public void plan(final FElem plan) {
    addPlan(plan, planElem(OF, type), expr);
  }

  @Override
  public String toString() {
    return Util.info("% instance of %", expr, type);
  }
}
