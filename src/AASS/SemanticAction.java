package AASS;

import base.SymbolTable;
import base.Values;

import java.util.List;

public abstract class SemanticAction {

    protected SymbolTable st;
    protected Values v;

    public SemanticAction(SymbolTable st, Values v){
        this.st = st;
        this.v = v;
    }

    public abstract List<Integer> execute(String c);
}
