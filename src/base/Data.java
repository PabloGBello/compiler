package base;

/**
 * Contiene los atributos para los identificadores y constantes que se
 * utilizan en la tabla de simbolos.
 */

public class Data {
    int codigo;
    private String lexema, type;

    public Data(){

    }
    public Data(String lexema, String type){

        this.lexema = lexema;
        this.type = type;
    }

    public Data(String lexema){

        this.lexema = lexema;
    }

    public String getLexema() {
        return lexema;
    }

    public String getType() {
        return type;
    }

    public void setLexema(String lexema) {
        this.lexema = lexema;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCode() {
        return codigo;
    }

    public void setCode(int code) {
        this.codigo = code;
    }
    public String toString(){
        return "["+lexema+","+type+"]";
    }
}
