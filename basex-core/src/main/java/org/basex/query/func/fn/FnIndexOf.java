package org.basex.query.func.fn;

import org.basex.query.*;
import org.basex.query.expr.*;
import org.basex.query.expr.CmpV.*;
import org.basex.query.func.*;
import org.basex.query.iter.*;
import org.basex.query.util.collation.*;
import org.basex.query.value.item.*;
import org.basex.query.value.type.*;
import org.basex.query.value.type.SeqType.*;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-17, BSD License
 * @author Christian Gruen
 */
public final class FnIndexOf extends StandardFunc {
  @Override
  public Iter iter(final QueryContext qc) throws QueryException {
    return new Iter() {
      final Iter iter = exprs[0].atomIter(qc, info);
      final Item srch = toAtomItem(exprs[1], qc);
      final Collation coll = toCollation(2, qc);
      int c;

      @Override
      public Int next() throws QueryException {
        while(true) {
          final Item it = iter.next();
          if(it == null) return null;
          ++c;
          if(it.comparable(srch) && OpV.EQ.eval(it, srch, coll, sc, info)) return Int.get(c);
          qc.checkStop();
        }
      }
    };
  }

  @Override
  protected Expr opt(final CompileContext cc) {
    final SeqType st = exprs[0].seqType();
    if(st.zero()) return exprs[0];
    if(st.zeroOrOne()) seqType = seqType.withOcc(Occ.ZERO_ONE);
    return this;
  }
}
