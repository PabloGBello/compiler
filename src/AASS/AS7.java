package AASS;

import base.SymbolTable;
import base.Values;

import java.util.ArrayList;
import java.util.List;

/**
 * Retorna el identificador de un simbolo compuesto por dos comparadores que se encuentran cargados en la tabla de simbolos.
 */

public class AS7 extends SemanticAction {

    public AS7(SymbolTable st, Values v) {
        super(st, v);
    }

    @Override
    public List<Integer> execute(String c) {

        List<Integer> aux = new ArrayList();
        v.addCharToBuffer(c);

        String lex = v.getBuffer();

        aux.add(st.getKey(lex));
        aux.add(st.getPosition(lex));
        return aux;

    }
}
