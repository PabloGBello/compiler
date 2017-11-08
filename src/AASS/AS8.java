package AASS;

import base.SymbolTable;
import base.Values;
import java.util.ArrayList;
import java.util.List;

/**
 * Retorna el numero ASCII asociado a el simbolo que se encuentra dentro del buffer.
 */

public class AS8 extends SemanticAction {

    public AS8(SymbolTable st, Values v) {
        super(st, v);
    }

    @Override
    public List<Integer> execute(String c) {

        List<Integer> aux = new ArrayList();
        v.subI();
        super.addToken(v.getBuffer());
        aux.add((int)v.getBuffer().charAt(0));
        aux.add(null);

        return aux;
    }
}
