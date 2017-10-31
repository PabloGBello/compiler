package AASS;

import base.SymbolTable;
import base.Values;

import java.util.List;

/**
 * Accion semantica 3:
 * Elimina los elementos actuales del buffer
 *
 **/
public class AS3 extends SemanticAction {

    public AS3(SymbolTable st, Values v) {
        super(st, v);
    }

    @Override
    public List<Integer> execute(String c) {
        v.addLine();
        return null;
    }
}