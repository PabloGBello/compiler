package base;

import java.io.*;
import java.util.ArrayList;

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
        try {
            writer.write("TOKEN: LEXEMA");
            writer.write(" \r\n");
            for(Integer i : ST.getSimbolos().keySet()) {
                writer.write(i + ": ");
                for (Data d : ST.getSimbolos().get(i)) {
                    writer.write(d.getLexema()+" ");
                }
                writer.write(" \r\n");
            }
            writer.close();
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
