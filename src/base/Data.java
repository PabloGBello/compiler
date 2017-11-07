package base;

/**
 * Contiene los atributos para los identificadores y constantes que se
 * utilizan en la tabla de simbolos.
 */

public class Data {

    private int codigo, numero;
    private String lexema, type;

    public Data(){
    }

    public Data(String lexema){
        this.lexema = lexema;
        numero = 0;
    }

    public Data(String lexema, String type){
        this.lexema = lexema;
        this.type = type;
        numero = 0;
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

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
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
