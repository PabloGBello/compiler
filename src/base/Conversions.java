package base;

import java.util.Hashtable;

public class Conversions {

    private Hashtable<String, Matriz> matrices;

    public Conversions(){

        matrices = new Hashtable();

        Matriz suma = new Matriz();
        Matriz resta = new Matriz();
        Matriz producto = new Matriz();
        Matriz division = new Matriz();

        suma.add(1, 1, 1);
        suma.add(1, 2, -1);
        suma.add(1, 3, -1);
        suma.add(2, 1, -1);
        suma.add(2, 2, 2);
        suma.add(2, 3, -1);
        suma.add(3, 1, -1);
        suma.add(3, 2, -1);
        suma.add(3, 3, 3);
        matrices.put("+", suma);



    }


    private Matriz getMatriz(String op){
        return matrices.get(op);
    }

    public int getConversion(String op, int t1, int t2){
        return (int)this.getMatriz(op).get(t1, t2);
    }
}
