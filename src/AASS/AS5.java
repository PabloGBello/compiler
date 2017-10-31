package AASS;

import base.SymbolTable;
import base.Values;

import java.util.List;

/**
 * Agrega un caracter al buffer.
 */

public class AS5 extends SemanticAction {

    public AS5(SymbolTable st, Values v) {
        super(st, v);
    }

    @Override
    public List<Integer> execute(String c) {
        v.addCharToBuffer(c);
        return null;
    }
}
