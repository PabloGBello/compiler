package base;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class SymbolTable {
    private Hashtable<Integer,List<Data>> simbolos;
    private FileHandler compilationOutput;

    /**
     * Constructor carga la TS con los simbolos necesarios
     **/

    public SymbolTable(FileHandler handler){

        compilationOutput = handler;

        simbolos = new Hashtable();
        simbolos.put(Constants.CTE, new ArrayList()); //CTE
        simbolos.put(Constants.ID, new ArrayList()); //ID
        simbolos.put(Constants.CADENA, new ArrayList()); //STRING

        addSymbol(Constants.IF,"IF");
        addSymbol(Constants.THEN,"THEN");
        addSymbol(Constants.ELSE,"ELSE");
        addSymbol(Constants.END_IF,"END_IF");
        addSymbol(Constants.BEGIN,"BEGIN");
        addSymbol(Constants.END,"END");
        addSymbol(Constants.OUT,"OUT");
        addSymbol(Constants.DO,"DO");
        addSymbol(Constants.UNTIL,"UNTIL");
        addSymbol(Constants.LET,"LET");

        addSymbol(Constants.INT,"INT");
        addSymbol(Constants.FLOAT,"FLOAT");

        addSymbol(Constants.COMP_MAYOR_IGUAL,">=");
        addSymbol(Constants.COMP_MENOR_IGUAL,"<=");
        addSymbol(Constants.COMP_IGUAL_IGUAL,"==");
        addSymbol(Constants.COMP_DISTINTO,"<>");

        addSymbol(Constants.I_F,"I_F");
    }


    public void addSymbol(Integer key, String value){

        if (!simbolos.containsKey(key))
            simbolos.put(key, new ArrayList<Data>());

        Data data = new Data(value);
        data.setCode(key);
        simbolos.get(key).add(data);
    }

    public void addItem(Integer key, String value, String type){
        if (!simbolos.containsKey(key))
            simbolos.put(key, new ArrayList<Data>());
        Data data = new Data(value, String.valueOf(type));
        data.setCode(key);

        if (key.equals(Constants.CTE)) {
            // Se le asigna el valor al atributo value para cada constante.
            if (Integer.valueOf(type).equals(Constants.INT))
                data.setValue(Integer.valueOf(value));
            else if (Integer.valueOf(type).equals(Constants.FLOAT)) {

                float result;
                String aux = value.replace(',', '.');

                if (aux.contains("E")) {
                    float num = Float.valueOf(aux.substring(0, aux.indexOf('E')));
                    float exp = Float.valueOf(aux.substring(aux.indexOf('E') + 1));
                    String aux2 = String.valueOf(Math.pow(num, exp));
                    result = Float.valueOf(aux2);
                } else
                    result = Float.valueOf(aux);

                data.setValue(result);

            }
        }
        simbolos.get(key).add(data);
    }

    /*public void addSymbol(String value){

        Data data = new Data(value);

        if (isNumeric(value)) {
            data.setCode(Constants.CTE);
            simbolos.get(Constants.CTE).add(data);
        }
        else
        if (isString(value))
            simbolos.get(Constants.CADENA).add(data);
        else{
            data.setCode(Constants.ID);
            simbolos.get(Constants.ID).add(data);
        }
    }*/

    /**
     * Obtiene el identificador para un token dado
     * @param value token
     * @return -1 si el token no existe en la tabla de simbolos
     */
    public Integer getKey(String value){

        for(Integer i : simbolos.keySet()) {
            for (Data d : simbolos.get(i)) {
                if (d.getLexema().equals(value))
                    return i;
            }
        }
        return -1;
    }

    public String getSymbol(Integer key, int position){
        if(simbolos.containsKey(key)){
            if(simbolos.get(key).size()>=position) {
                return simbolos.get(key).get(position).getLexema();

            }
        }
        return null;
    }

    public void setType(String lex, String type){

        List<Data> aux = simbolos.get(Constants.ID);
        for (Data var : aux){
            if (lex.equals(var.getLexema())) {
                if(type.equals("INT"))
                    var.setType(String.valueOf(Constants.INT));
                if(type.equals("FLOAT"))
                    var.setType(String.valueOf(Constants.FLOAT));
                if(type.equals("STRING"))
                    var.setType(String.valueOf(Constants.CADENA));
            }
        }
    }

    public Integer getPosition (String value){

        List<Data> list = simbolos.get(getKey(value));
        for(int i = 0; i < list.size(); i++)
            if (value.equals(list.get(i).getLexema()))
                return i;
        return -1;
    }

    private boolean isNumeric(String s) {
        return s != null && s.matches("[-+]?\\d*\\.?\\d+");
    }

    private boolean isString(String s){
        if ((s.charAt(0) == '"') && (s.charAt(s.length()-1) == '"'))
            return true;
        return false;
    }

    public void showAll (){

        for(Integer i : simbolos.keySet())
            for (Data s : simbolos.get(i))
                System.out.println(i +  " : " + s.getLexema());
    }

    public Hashtable<Integer, List<Data>> getSimbolos() {
        return simbolos;
    }
    /**Para extraer la estructura completa Data.
     * CAPAZ QUE SE PARECE A OTRO METODO CON +- FUNCIONALIDAD PERO SE
     * REFINA A LO ULTIMO TODO ES NECESARIO*/

    public Data getData(int key, int pos){
        if(simbolos.containsKey(key)){
            if(simbolos.get(key).size()>=pos) {
                return simbolos.get(key).get(pos);

            }
        }
        return null;
    }

    public FileHandler getCompilationOutput(){
        return compilationOutput;
    }
}
