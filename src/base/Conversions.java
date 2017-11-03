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
        Matriz asignacion = new Matriz();
        Matriz comparacion = new Matriz();

        suma.add(Constants.INT, Constants.INT, Constants.INT);
        suma.add(Constants.INT, Constants.FLOAT, -1);
        suma.add(Constants.INT, Constants.CADENA, -1);
        suma.add(Constants.FLOAT, Constants.INT, -1);
        suma.add(Constants.FLOAT, Constants.FLOAT, Constants.FLOAT);
        suma.add(Constants.FLOAT, Constants.CADENA, -1);
        suma.add(Constants.CADENA, Constants.INT, -1);
        suma.add(Constants.CADENA, Constants.FLOAT, -1);
        suma.add(Constants.CADENA, Constants.CADENA, Constants.CADENA);

        resta.add(Constants.INT, Constants.INT, Constants.INT);
        resta.add(Constants.INT, Constants.FLOAT, -1);
        resta.add(Constants.INT, Constants.CADENA, -1);
        resta.add(Constants.FLOAT, Constants.INT, -1);
        resta.add(Constants.FLOAT, Constants.FLOAT, Constants.FLOAT);
        resta.add(Constants.FLOAT, Constants.CADENA, -1);
        resta.add(Constants.CADENA, Constants.INT, -1);
        resta.add(Constants.CADENA, Constants.FLOAT, -1);
        resta.add(Constants.CADENA, Constants.CADENA, Constants.CADENA);

        asignacion.add(Constants.INT, Constants.INT, Constants.INT);
        asignacion.add(Constants.INT, Constants.FLOAT, -1);
        asignacion.add(Constants.INT, Constants.CADENA, -1);
        asignacion.add(Constants.FLOAT, Constants.INT, -1);
        asignacion.add(Constants.FLOAT, Constants.FLOAT, Constants.FLOAT);
        asignacion.add(Constants.FLOAT, Constants.CADENA, -1);
        asignacion.add(Constants.CADENA, Constants.INT, -1);
        asignacion.add(Constants.CADENA, Constants.FLOAT, -1);
        asignacion.add(Constants.CADENA, Constants.CADENA, Constants.CADENA);

        comparacion.add(Constants.INT, Constants.INT, Constants.INT);
        comparacion.add(Constants.INT, Constants.FLOAT, -1);
        comparacion.add(Constants.INT, Constants.CADENA, -1);
        comparacion.add(Constants.FLOAT, Constants.INT, -1);
        comparacion.add(Constants.FLOAT, Constants.FLOAT, Constants.FLOAT);
        comparacion.add(Constants.FLOAT, Constants.CADENA, -1);
        comparacion.add(Constants.CADENA, Constants.INT, -1);
        comparacion.add(Constants.CADENA, Constants.FLOAT, -1);
        comparacion.add(Constants.CADENA, Constants.CADENA, Constants.CADENA);

        matrices.put("+", suma);
        matrices.put("=", asignacion);
        matrices.put("==", comparacion);


    }


    private Matriz getMatriz(String op){
        return matrices.get(op);
    }
    /**
     * 1: INT
     * 2: FLOAT
     * 3: STRING
     * */
    public int getConversion(String op, int t1, int t2){
        if((t1 == -1) || (t2 == -1))
            return -1;
        if(this.getMatriz(op).get(t1, t2) == null)
            return -1;
        return ((Integer)this.getMatriz(op).get(t1, t2)).intValue();
    }
}
