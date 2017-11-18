package base;

import java.io.*;
import java.util.ArrayList;
import java.util.Set;

public class FileHandler {

    private File file;
    private FileReader reader;
    private BufferedWriter writer;

    /**
     *
     *
     * @param mode 0: lectura de archivos
     *             1: escritura de archivos
     * @param path
     * @param fileName
     */

    public FileHandler(int mode, String path, String fileName) {

        if (mode == 0){
            try {
                reader = new FileReader(path + fileName);
            }
            catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        else if (mode == 1){
            try {
                file = new File(path + fileName);
                writer = new BufferedWriter(new FileWriter(file));
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
    * Escribe en el archivo
    **/
    public void write(String msg){
        try{
            writer.write(msg);
            writer.newLine();
            writer.flush();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Retorna una lista con los caracteres contenidos en el archivo pasado como parametro
     */
    public ArrayList<Character> dumpSourceCode() {

        ArrayList<Character> chars = new ArrayList();

        try
        {
            int car = reader.read();
            while(car != -1){
                chars.add(new Character((char)car));
                car = reader.read();
            }

            chars.add(new Character((char)car));
            reader.close();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

        return chars;
    }

    /**
     * Vuelca contenido de la TS en un archivo
     **/
    public void dumpST(SymbolTable ST){

        Set<Integer> keys = ST.getSimbolos().keySet();

        try {
            this.writeKeys(keys, ST);
            writer.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeKeys(Set<Integer> s, SymbolTable ST){
        String title;

        try {
            writer.write("\n------------------------------------------------------------\n");
            writer.write("-- TABLA DE SÍMBOLOS: CONTENIDO --\n\n");
            writer.write("-- FORMATO IDENTIFICADORES: [ <lexema>, <numero>, <tipo>, <valor> ]\n");
            writer.write("-- FORMATO CONSTANTES: [ <lexema>, <tipo> ]\n");
            writer.write("-- PR: PALABRA RESERVADA\n");
            writer.write("------------------------------------------------------------\n\n");

            for(Integer i : s) {
                switch(i){
                    case 276:
                        title = "CADENAS";
                        break;
                    case 275:
                        title = "CONSTANTES";
                        break;
                    case 274:
                        title = "IDENTIFICADORES";
                        break;
                    case 273 | 272 | 271 | 270:
                        title = "COMPARADORES";
                        break;
                    default:
                        title = "PR";
                        break;
                }
                writer.write(title + ": ");
                this.writeData(i, ST);
                writer.write("\r\n");
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void writeData(Integer i, SymbolTable ST){
        String txt;
        try {
            for (Data d : ST.getSimbolos().get(i)) {
                if (i == 274) {
                    System.out.println("mierda:"+ d);
                    txt = "[ " + d.getLexema()
                            + ", " + d.getNumero()
                            + ", " + ST.getTypeReverse(Integer.valueOf(d.getType()))
                            + ", " + d.getValue()
                            +  " ] ";
                }
                else if (i == 275) {
                    txt = "[ " + d.getLexema()
                            + ", " + ST.getTypeReverse(Integer.valueOf(d.getType()))
                            +  " ] ";
                }
                else if (i == 276) {
                    txt = "[ " + d.getLexema()
                            +  " ] ";
                }
                else{
                    txt = d.getLexema() + " ";
                }
                writer.write(txt);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void closeWriter(){
        try {
            writer.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

}
