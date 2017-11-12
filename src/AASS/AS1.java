package AASS;

import base.SymbolTable;
import base.Values;
import base.Constants;
import base.Printer;

import java.util.ArrayList;
import java.util.List;

/**
 * Agrega a la tabla de simbolos los identificadores verificando anteriormente si no son palabras reservadas, y
 * convirtiendo el lexema en minuscula.
 * Trunca el lexema si pasa el limite de caracteres.
**/

public class AS1 extends SemanticAction {

    public AS1(SymbolTable s, Values v) {
        super(s, v);
    }

    @Override
    public List<Integer> execute(String c) {

        List<Integer> aux = new ArrayList();

        String lex = v.getBuffer();

        super.addToken(lex);
        v.subI();

        if (st.getKey(lex) == -1){
            lex = lex.toLowerCase();
            if (st.getKey(lex) == -1) {
                v.setBuffer(lex.toLowerCase());
                if (lex.length() > 15) {
                    lex = lex.substring(0, 15);
                    String s = Printer.getMessage(0,0, v.getCurrentLine(), "El identificador supera el maximo de caracteres permitido. Se trunca a los primeros 15. " + lex);
                    st.getCompilationOutput().write(s);
                }
                st.addSymbol(Constants.ID, lex);
            }
        }
        aux.add(st.getKey(lex));
        aux.add(st.getPosition(lex));
        return aux;
    }

}
