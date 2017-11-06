package AASS;

import base.Printer;
import base.SymbolTable;
import base.Values;
import base.Constants;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;

import java.util.ArrayList;
import java.util.List;

/**
 * Agrega a la tabla de simbolos las ctes de tipo int y verifica su rango
 * Setea la variable de posicion del arreglo de caracteres una posicion mas atras para poder leer el simbolo distinto a un digito
 */

public class AS10 extends SemanticAction {

    public AS10(SymbolTable s, Values v) {
        super(s, v);
    }

    @Override
    public List<Integer> execute(String c) {

        List<Integer> aux = new ArrayList();
        String lex = v.getBuffer();
        v.subI();

        Integer intVal = Integer.parseInt(lex);

        if(intVal < -32768 || intVal > 32767){
            String s = Printer.getMessage(0,0, v.getCurrentLine(), "Constante entera fuera del rango permitido " + lex);
            st.getCompilationOutput().write(s);
        }

        if (st.getKey(lex) == -1){
            st.addItem(Constants.CTE, lex, String.valueOf(Constants.INT));
        }

        aux.add(st.getKey(lex));
        aux.add(st.getPosition(lex));

        return aux;
    }

}
