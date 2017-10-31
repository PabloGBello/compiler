package AASS;

import base.SymbolTable;
import base.Values;

import java.util.List;

/**
 * Inicializa buffer en vacio y el primer caracter.
 */
public class AS6 extends SemanticAction {

    public AS6(SymbolTable st, Values v) {
        super(st, v);
    }

    @Override
    public List<Integer> execute(String c) {

        v.deleteBuffer();
        v.addCharToBuffer(c);

        return null;
    }
}
