package AASS;

import base.SymbolTable;
import base.Values;

import java.util.List;

/**
 * Accion semantica 4:
 * Por lo que veo elimina los 3 ultimos y agrega un espacio en blanco
 * Debe ser la AS para string multilinea (Alta supocision,Y si fueras ciego? jajaja)
 *
 **/

public class AS4 extends SemanticAction {

    public AS4(SymbolTable st, Values v) {
        super(st, v);
    }


    @Override
    public List<Integer> execute(String c) {
        String lex = v.getBuffer();
        v.setBuffer(lex.substring(0,lex.length() - 3));
        v.addCharToBuffer( " ");

        return null;
    }
}
