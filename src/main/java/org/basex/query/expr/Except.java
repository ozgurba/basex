package org.basex.query.expr;

import static org.basex.query.QueryText.*;

import org.basex.query.*;
import org.basex.query.iter.*;
import org.basex.query.value.item.*;
import org.basex.query.value.node.*;
import org.basex.util.*;

/**
 * Except expression.
 *
 * @author BaseX Team 2005-12, BSD License
 * @author Christian Gruen
 */
public final class Except extends Set {
  /**
   * Constructor.
   * @param ii input info
   * @param e expression list
   */
  public Except(final InputInfo ii, final Expr[] e) {
    super(ii, e);
  }

  @Override
  public Expr compile(final QueryContext ctx) throws QueryException {
    super.compile(ctx);
    if(expr[0].isEmpty()) return optPre(null, ctx);

    for(int e = 1; e < expr.length; ++e) {
      if(expr[e].isEmpty()) {
        ctx.compInfo(OPTREMOVE, description(), expr[e]);
        expr = Array.delete(expr, e--);
      }
    }
    // results must always be sorted
    return expr.length == 1 && iterable ? expr[0] : this;
  }

  @Override
  protected NodeSeqBuilder eval(final Iter[] iter) throws QueryException {
    final NodeSeqBuilder nc = new NodeSeqBuilder().check();

    for(Item it; (it = iter[0].next()) != null;) nc.add(checkNode(it));
    final boolean db = nc.dbnodes();

    for(int e = 1; e != expr.length && nc.size() != 0; ++e) {
      final Iter ir = iter[e];
      for(Item it; (it = ir.next()) != null;) {
        final int i = nc.indexOf(checkNode(it), db);
        if(i != -1) nc.delete(i);
      }
    }
    return nc;
  }

  @Override
  protected NodeIter iter(final Iter[] iter) {
    return new SetIter(iter) {
      @Override
      public ANode next() throws QueryException {
        if(item == null) {
          item = new ANode[iter.length];
          for(int i = 0; i != iter.length; ++i) next(i);
        }

        for(int i = 1; i != item.length; ++i) {
          if(item[0] == null) return null;
          if(item[i] == null) continue;
          final int d = item[0].diff(item[i]);

          if(d < 0 && i + 1 == item.length) break;
          if(d == 0) {
            next(0);
            i = 0;
          }
          if(d > 0) next(i--);
        }
        final ANode temp = item[0];
        next(0);
        return temp;
      }
    };
  }
}
