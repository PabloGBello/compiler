package AASS;

import base.SymbolTable;
import base.Values;

import java.util.ArrayList;
import java.util.List;

/**
 * Retorna el numero ASCII asociado a un simbolo que viene por parametro (c)
 **/

public class AS2 extends SemanticAction {

    public AS2(SymbolTable st, Values v) {
        super(st, v);
    }

    @Override
    public List<Integer> execute(String c) {

        List<Integer> aux = new ArrayList();
        super.addToken(c);
        aux.add((int)c.charAt(0));
        aux.add(null);

        return aux;
    }

}
