package base;

import java.util.ArrayList;
import java.util.Hashtable;


/**
 * Matriz basica: La implementan la matriz de transiciones y la matriz de A.S.
 * Internamente contiene un HastTable, donde la clave es #fila + ":" + #columna
**/

public class Matriz {

    private Hashtable<String, Object> mat = new Hashtable();


    // DEUDA: Quizá agregar control de errores, aunque quizá sea al pedo...
    public void add(Comparable row, Comparable col, Object obj){
        mat.put(row + ":,:" + col, obj);
    }

    public Object get(Comparable row, Comparable col){
        if( mat.containsKey(row + ":,:" + col) ) {
            return  mat.get(row + ":,:" + col);
        }
        return null;
    }

    public void remove(Comparable row, Comparable col) {
        if ( mat.containsKey(row + ":,:" + col) ) {
            mat.remove(row + ":,:" + col);
        }
        return;
    }

    public ArrayList<Comparable> getRows(){
        ArrayList<Comparable> aux = new ArrayList();
        for(String c: mat.keySet()) {
            aux.add(c.split(":,:")[0]);
        }
        return aux;
    }

    public ArrayList<Comparable> getColumns(){
        ArrayList<Comparable> aux = new ArrayList();
        for(String c: mat.keySet()) {
            aux.add(c.split(":,:")[1]);
        }
        return aux;
    }

    public Matriz getCopy(){
        Matriz aux = new Matriz();
        for(Comparable f: this.getRows()){
            for (Comparable c : this.getColumns()){
                aux.add(f, c, this.get(f, c));
            }
        }
        return aux;
    }

    // Retorna el hastTable interno (para debug y otras cosas)
    public Hashtable<String, Object> getInnerTable(){
        return mat;
    }

    // UNUSED
    /*private String getKey(Object o1, Object o2){
        return o1 + ":,:" + o2;
    }*/
}