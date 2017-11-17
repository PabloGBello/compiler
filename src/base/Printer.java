package base;

public class Printer {

    /**
     * @param type tipo: WARNING, ERROR, MENSAJE
     * @param line LINEA DEL FUENTE.TXT
     * @param msg EL MSJ MISMO
     * @return
     */
    public static String getMessage(int level, int type, int line, String msg){

        String l = null;
        String t = null;

        switch(level){
            case 0:
                l = "[LEXICO] ";
                break;
            case 1:
                l = "[SINTAXIS] ";
                break;
            case 2:
                l = "[SEMANTICA] ";
                break;
        }
        switch(type){
            case 0:
                t = "[ADVERTENCIA] ";
            break;
            case 1:
                t = "[ERROR] ";
            break;
            case 2:
                t = "[MENSAJE] ";
            break;
        }
        return (t + l + ": " + msg + " en linea " + line);
    }

}
