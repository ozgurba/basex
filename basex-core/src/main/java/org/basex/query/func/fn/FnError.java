package org.basex.query.func.fn;

import static org.basex.query.QueryError.*;
import static org.basex.query.func.Function.*;

import org.basex.query.*;
import org.basex.query.func.*;
import org.basex.query.iter.*;
import org.basex.query.value.*;
import org.basex.query.value.item.*;
import org.basex.query.value.type.*;
import org.basex.util.*;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-17, BSD License
 * @author Christian Gruen
 */
public final class FnError extends StandardFunc {
  @Override
  public Iter iter(final QueryContext qc) throws QueryException {
    final int al = exprs.length;
    if(al == 0) throw FUNERR1.get(info);

    QNm name = toQNm(exprs[0], qc, true);
    if(name == null) name = FUNERR1.qname();

    String msg = al > 1 ? Token.string(toToken(exprs[1], qc)) : FUNERR1.desc;
    final Value val = al > 2 ? qc.value(exprs[2]) : null;
    throw new QueryException(info, name, msg).value(val);
  }

  @Override
  public boolean isVacuous() {
    return true;
  }

  /**
   * Creates an error function instance.
   * @param ex query exception
   * @param st type of the expression
   * @param sc static context
   * @return function
   */
  public static StandardFunc get(final QueryException ex, final SeqType st,
      final StaticContext sc) {
    final StandardFunc sf = ERROR.get(sc, ex.info(), ex.qname(), Str.get(ex.getLocalizedMessage()));
    sf.seqType = st;
    return sf;
  }
}
