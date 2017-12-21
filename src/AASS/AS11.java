package AASS;

import base.SymbolTable;
import base.Values;
import base.Constants;
import base.Printer;

import java.util.ArrayList;
import java.util.List;

/**
 * Agrega a la tabla de simbolos las ctes de tipo float con coma y verifica su rango
 * Setea la variable de posicion del arreglo de caracteres una posicion mas atras para poder leer el simbolo distinto a un digito
 */

public class AS11 extends SemanticAction {

    public AS11(SymbolTable st, Values v) {
        super(st, v);
    }

    @Override
    public List<Integer> execute(String c) {

        List<Integer> aux = new ArrayList();
        String lex = v.getBuffer();
        super.addToken(lex);
        v.subI();

        if (lex.trim().equals(",")){
            aux.add((int)lex.charAt(0));
            aux.add(null);
        }
        else{
            Float fVal = Float.valueOf(lex.replace(',','.'));
            lex = fVal.toString();

            //Chequeo de rango de constantes se movio a la etapa semantica debido a los negativos
            /*
            if(fVal < -32768 || fVal > 32767){
                String s = Printer.getMessage(0,0, v.getCurrentLine(), "Constante flotante fuera del rango permitido " + lex);
                st.getCompilationOutput().write(s);
            }*/

            if (st.getKey(lex) == -1){

                st.addItem(Constants.CTE, lex, String.valueOf(Constants.FLOAT));
            }

            aux.add(st.getKey(lex));
            aux.add(st.getPosition(lex));
        }


        return aux;

    }
}
