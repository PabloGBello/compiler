package base;

import com.sun.org.apache.bcel.internal.generic.INEG;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class SymbolTable {
    private Hashtable<Integer, List<Data>> simbolos;
    private FileHandler compilationOutput;

    /**
     * Constructor carga la TS con los simbolos necesarios
     **/

    public SymbolTable(FileHandler handler) {

        compilationOutput = handler;

        simbolos = new Hashtable();
        simbolos.put(Constants.CTE, new ArrayList()); //CTE
        simbolos.put(Constants.ID, new ArrayList()); //ID
        simbolos.put(Constants.CADENA, new ArrayList()); //STRING

        addSymbol(Constants.IF, "IF");
        addSymbol(Constants.THEN, "THEN");
        addSymbol(Constants.ELSE, "ELSE");
        addSymbol(Constants.END_IF, "END_IF");
        addSymbol(Constants.BEGIN, "BEGIN");
        addSymbol(Constants.END, "END");
        addSymbol(Constants.OUT, "OUT");
        addSymbol(Constants.DO, "DO");
        addSymbol(Constants.UNTIL, "UNTIL");
        addSymbol(Constants.LET, "LET");

        addSymbol(Constants.INT, "INT");
        addSymbol(Constants.FLOAT, "FLOAT");

        addSymbol(Constants.COMP_MAYOR_IGUAL, ">=");
        addSymbol(Constants.COMP_MENOR_IGUAL, "<=");
        addSymbol(Constants.COMP_IGUAL_IGUAL, "==");
        addSymbol(Constants.COMP_DISTINTO, "<>");

        addSymbol(Constants.I_F, "I_F");
    }


    public void addSymbol(Integer key, String value) {

        if (!simbolos.containsKey(key))
            simbolos.put(key, new ArrayList());

        Data data = new Data(value);
        data.setCode(key);
        if(key == Constants.CADENA)
            data.setType(String.valueOf(key));
        simbolos.get(key).add(data);
    }

    public void addItem(Integer key, String value, String type) {
        if (!simbolos.containsKey(key))
            simbolos.put(key, new ArrayList());
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

    public void addSymbol(Integer key, String value, String type, int numero) {
        if (!simbolos.containsKey(key))
            simbolos.put(key, new ArrayList());

        System.out.println("Entra por aca");
        Data data = new Data(value, String.valueOf(type));
        data.setCode(key);
        simbolos.get(key).add(data);
        data.setNumero(numero);
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
     *
     * @param value token
     * @return -1 si el token no existe en la tabla de simbolos
     */
    public Integer getKey(String value) {

        for (Integer i : simbolos.keySet()) {
            for (Data d : simbolos.get(i)) {
                if (d.getLexema().equals(value))
                    return i;
            }
        }
        return -1;
    }

    public String getSymbol(Integer key, int position) {
        if (simbolos.containsKey(key)) {
            if (simbolos.get(key).size() >= position) {
                return simbolos.get(key).get(position).getLexema();

            }
        }
        return null;
    }

    public void setType(String lex, String type) {

        List<Data> lista = simbolos.get(Constants.ID);

        int i = lista.size() - 1;
        boolean stop = false;

        //Recorro la lista de IDs de atras hacia adelante
        while (i >= 0 && !stop) {

            Data item = lista.get(i);
            String lexema = item.getLexema();

            // Coincide lexema: es la misma variable
            if (lexema.equals(lex)) {
                stop = true;
                if (item.getType() == null) {
                    //Variable no fue declarada
                    item.setType(this.getType(type));
                    item.setNumero(0);
                }
                else {
                    //Variable existe
                    if (item.getType().equals(this.getType(type))) {
                        //Variable en T.S. == tipo que la variable proxima a declarar: ERROR!
                        String msg = "Declaracon de dos variables iguales y de mismo tipo de forma adyacente.";
                        String s = Printer.getMessage(2, 1, LexicalAnalizer.values.getCurrentLine(), msg); //v.currentLine
                        LexicalAnalizer.compilationOutput.write(s);
                        System.exit(1);
                    }
                    else {
                        //Variable en T.S. <> tipo que la variable proxima a declarar: SHADOWING
                        this.addSymbol(Constants.ID, lex, this.getType(type), item.getNumero() + 1);
                        TercetoGenerator.AUXptr = getData(Constants.ID, lex, this.getType(type));
                    }

                }
            }
            i--;
        }
    }

    public Integer getPosition(String value) {

        List<Data> list = simbolos.get(getKey(value));
        for (int i = 0; i < list.size(); i++)
            if (value.equals(list.get(i).getLexema()))
                return i;
        return -1;
    }

    private boolean isNumeric(String s) {
        return s != null && s.matches("[-+]?\\d*\\.?\\d+");
    }

    private boolean isString(String s) {
        if ((s.charAt(0) == '"') && (s.charAt(s.length() - 1) == '"'))
            return true;
        return false;
    }


    public Hashtable<Integer, List<Data>> getSimbolos() {
        return simbolos;
    }

    /**
     * Para extraer la estructura completa Data.
     * CAPAZ QUE SE PARECE A OTRO METODO CON +- FUNCIONALIDAD PERO SE
     * REFINA A LO ULTIMO TODO ES NECESARIO
     */

    public Data getData(int key, String lexema) {
        if (simbolos.containsKey(key)) {
            for(Data d: simbolos.get(key)){
                if (d.getLexema().equals(lexema))
                    return d;
            }
        }
        return null;
    }

    public Data getData(int key, String lexema, String type) {
        if (simbolos.containsKey(key)) {
            for(Data d: simbolos.get(key)){
                if ((d.getLexema().equals(lexema)) && d.getType().equals(type)) {
                    return d;
                }
            }
        }
        return null;
    }


    public void addData(Data data){
        simbolos.get(data.getCode()).add(data);
    }

    public Data getData(int key, int pos) {
        if (simbolos.containsKey(key)) {
            if (simbolos.get(key).size() >= pos) {
                return simbolos.get(key).get(pos);

            }
        }
        return null;
    }

    public FileHandler getCompilationOutput() {
        return compilationOutput;
    }

    // Para no andar reiterando arriba en el setType
    public String getType(String type) {
        if (type.equals("INT"))
            return String.valueOf(Constants.INT);
        if (type.equals("FLOAT"))
            return String.valueOf(Constants.FLOAT);
        if (type.equals("STRING"))
            return String.valueOf(Constants.CADENA);
        return null;
    }

    public static String getTypeReverse(Integer type) {
        switch(type){
            case 267:
                return "FLOAT";
            case 268:
                return "INT";
            case 276:
                return "CADENA";
        }
        return "";
    }
}
