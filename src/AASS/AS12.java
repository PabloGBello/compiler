package AASS;

import base.Printer;
import base.SymbolTable;
import base.Values;
import base.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Agrega a la tabla de simbolos las ctes de tipo float con exponente y verifica su rango
 * Setea la variable de posicion del arreglo de caracteres una posicion anterior para poder leer el simbolo distinto a un digito
 */

public class AS12 extends SemanticAction {

    public AS12(SymbolTable s, Values v) {
        super(s, v);
    }

    @Override
    public List<Integer> execute(String c) {

        List<Integer> aux = new ArrayList();
        String lex = v.getBuffer();
        v.subI();

        Float inf = Float.valueOf("1.17549435E-38");
        Float sup = Float.valueOf("3.40282347E38");

        Float fVal = Float.valueOf(lex.replace(',','.'));

        if(fVal < inf || fVal > sup){
            String s = Printer.getMessage(0,0, v.getCurrentLine(), "Constante flotante fuera del rango permitido " + lex);
            st.getCompilationOutput().write(s);
        }

        if (st.getKey(lex) == -1){
            st.addConstant(Constants.CTE, lex, String.valueOf(Constants.FLOAT));
        }

        aux.add(st.getKey(lex));
        aux.add(st.getPosition(lex));
        return aux;
    }
}
