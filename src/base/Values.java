package base;

/**
 * Variables utiles para usar en el proceso de obtencion de tokens.
 * i : iterador para recorrer arreglo de caracteres
 * state : estado actual maquina estados
 * buffer : String donde se van guardando los caracteres leidos
 */

public class Values {

    private Integer i;
    private Integer state;
    private String buffer;

    private Integer currentLine = 1;

    public Values() {
        i = 0;
        state = 0;
        buffer = "";
    }

    public Integer getI() {
        return i;
    }

    public Integer getCurrentLine() {
        return currentLine;
    }

    public void addLine() {
        currentLine++;
    }

    public Integer getState() {
        return state;
    }

    public String getBuffer() {
        return buffer;
    }

    public void addI() {
        i++;
    }

    public void subI() {
        i--;
    }

    public void setBuffer(String buffer) {
        this.buffer = buffer;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    /**
     * Concatena un caracter en el buffer
     *
     * @param c
     */

    public void addCharToBuffer(String c) {
        buffer += c;
    }


    /**
     * Elimina ultimo caracter de buffer
     */
    public void deleteLastCharToBuffer() {
        buffer = buffer.substring(0, buffer.length() - 1);
    }

    public void deleteBuffer() {
        buffer = "";
    }
}
