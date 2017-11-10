package AASS;

import base.Constants;
import base.SymbolTable;
import base.Values;

import java.util.ArrayList;
import java.util.List;

/**
 * Agrega a la tabla de simbolos una cadena de caracteres.
 */

public class AS9 extends SemanticAction {

    public AS9(SymbolTable st, Values v) {
        super(st, v);
    }

    @Override
    public List<Integer> execute(String c) {

        List<Integer> aux = new ArrayList();

        v.subI();

        String lex = v.getBuffer();

        super.addToken(lex);

        if (st.getKey(lex) == -1){
            System.out.println("encuentra " + lex);
            st.addSymbol(Constants.CADENA, lex);
        }

        aux.add(st.getKey(lex));
        aux.add(st.getPosition(lex));

        return aux;
    }
}
