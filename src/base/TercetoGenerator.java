package base;

import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;

import java.util.*;

public class TercetoGenerator {

    int indexTerceto = 1;
    Hashtable<Integer,Terceto> tercetos;
    int conTermino = 3;
    boolean terminoComplete = false;
    boolean crear = true;
    boolean expresionClosed = false;
    int flagOpAnt  = -1;
    List<Integer>  pila =  new ArrayList();
    Conversions conversions = new Conversions();

    private Data Fptr = null;
    public static Data Eptr = null;
    private Data E_sptr = null;
    private Data Tptr = null;
    private Data Aptr = null;
    public static Data AUXptr = null;
    private int indexDO = -1;
    public List<Data> pilaGramatica = new ArrayList<>();
    private SymbolTable ST;

    /* Variables necesarias para identificar el primer terceto de un bloque - usado para DO UNTIL*/
    private int indexPrimerSentBloque = 0;
    private boolean primerSentBloque = false;

    public Hashtable<Integer,Terceto> getTercetos(){
        return tercetos;
    }

    public Data getFptr() {
        return Fptr;
    }

    public void setFptr(Data fptr) {
        Fptr = fptr;
    }

    public Data getEptr() {
        return Eptr;
    }

    public void setEptr(Data eptr) {
        Eptr = eptr;
    }

    public Data getE_sptr() {
        return E_sptr;
    }

    public void setE_sptr(Data e_sptr) {
        E_sptr = e_sptr;
    }

    public Data getTptr() {
        return Tptr;
    }

    public void setTptr(Data tptr) {
        Tptr = tptr;
    }

    public Data getAptr() {
        return Aptr;
    }

    public void setAptr(Data aptr) {
        Aptr = aptr;
    }

    public TercetoGenerator(SymbolTable ST){
        tercetos = new Hashtable<>();
        this.ST = ST;
    }

    public List<Integer> getPila() {
        return pila;
    }

    public int getIndexTerceto() {
        return indexTerceto;
    }

    public void setIndexDO(){ indexDO = indexTerceto;}

    public void tercetoIncompleto(String s){

        Terceto aux = new Terceto();
        aux.setIndex(indexTerceto);
        aux.setOperator(new Data(s));
        aux.setField1(new Data("["+((Integer)(indexTerceto-1)).toString()+"]", ((Integer)Constants.PUN_TERCETO).toString()));
        aux.setType(String.valueOf(Constants.BRANCH));
        pila.add(indexTerceto);
        tercetos.put(indexTerceto, aux);
        indexTerceto++;
    }
    public void tercetoLabel(){
        Terceto aux = new Terceto();
        aux.setIndex(indexTerceto);
        aux.setOperator(new Data("Label"+indexTerceto,String.valueOf(Constants.BRANCH)));
        aux.setField1(new Data("-"));
        aux.setField2(new Data("-"));
        aux.setType(String.valueOf(Constants.BRANCH));
        tercetos.put(indexTerceto, aux);
        indexTerceto++;
    }

    public void tercetoIteration(String s){

        Terceto aux = new Terceto();
        aux.setIndex(indexTerceto);
        aux.setOperator(new Data(s));
        aux.setField1(new Data("["+((Integer)(indexTerceto-1)).toString() + "]"));
        aux.setField2(new Data("[" + indexDO + "]", ((Integer) Constants.PUN_TERCETO).toString()));
        aux.setType(String.valueOf(Constants.BRANCH));
        tercetos.put(indexTerceto, aux);
        indexTerceto++;
    }
    public void  tercetoOUT(Data field){
        Terceto aux = new Terceto();
        aux.setIndex(indexTerceto);
        aux.setOperator(new Data("OUT",String.valueOf(Constants.CADENA)));
        aux.setField1(field);
        aux.setField2(new Data("-",String.valueOf(Constants.CADENA)));
        aux.setType(String.valueOf(Constants.CADENA));
        tercetos.put(indexTerceto, aux);
        indexTerceto++;
    }

    public void tercetoDesapilar(int b){
        if(b == 1)
            tercetos.get(pila.remove(0)).setField2(new Data("["+((Integer)(indexTerceto+1)).toString()+"]", ((Integer)Constants.PUN_TERCETO).toString()));
        else {
            tercetos.get(pila.get(0)).setField1(new Data("["+((Integer) (indexTerceto)).toString()+"]", ((Integer) Constants.PUN_TERCETO).toString()));
            tercetos.get(pila.remove(0)).setField2(new Data("-", ((Integer) Constants.OTHER).toString()));
        }
    }
    /**
     (I_F, a, -)
     **/
    public Data tercetoI_F(Data field){
        //if(Integer.valueOf(field.getType()) == Constants.INT) {
            Terceto aux = new Terceto();
            aux.setIndex(indexTerceto);
            Data op = new Data("I_F", String.valueOf(Constants.I_F));
            op.setCode(Constants.I_F);
            aux.setOperator(op);
            Data field1 = new Data(lastDeclaration(field));
            //field1.setType(String.valueOf(Constants.FLOAT));
            aux.setField1(field1);
            aux.setField2(new Data("-", String.valueOf(Constants.FLOAT)));
            aux.setType(String.valueOf(Constants.FLOAT));
            Data info = new Data("@aux" + aux.getIndex(),aux.getType());
            ST.addItem(Constants.ID,"@aux" + aux.getIndex(),aux.getType());
            tercetos.put(indexTerceto, aux);
            indexTerceto++;
            Data aux2 = new Data("["+String.valueOf(indexTerceto-1)+"]", String.valueOf(Constants.PUN_TERCETO));
            aux2.setCode(Constants.PUN_TERCETO);
            return aux2;
        //}
    }

    public Data createTerceto(String operator, Data field1, Data field2){
        if (!primerSentBloque){
            primerSentBloque = true;
            indexPrimerSentBloque = indexTerceto;
        }
        Terceto aux = new Terceto();
        aux.setIndex(indexTerceto);
        aux.setOperator(new Data(operator));
        Data aux1, aux2;

        if(operator.equals("=")) {
            aux1 = field1;
        }
        else {
            aux1 = this.lastDeclaration(field1);

        }

        if(field2.getCode() == Constants.ID) {
            aux2 = this.lastDeclaration(field2);
        }
        else {
            aux2 = field2;
        }

        int typeTerceto2 = whatType(aux2);

        if(operator.equals("=") && field1.getType() == null) {
            field1.setType(String.valueOf(Constants.OTHER));
        }

        int typeTerceto1 = whatType(aux1);

        int type = conversions.getConversion(operator, typeTerceto1, typeTerceto2);

        if( type == -1) {
            System.out.println("Terceto: " + indexTerceto + " Incompatibilidad de tipos: " + aux1 + " y " + aux2);
            String msg = "Incompatibilidad de tipos entre tercetos. Terceto "
                    + indexTerceto + ", tipos " + aux1 + " y " + aux2;
            String s = Printer.getMessage(2, 1, LexicalAnalizer.values.getCurrentLine(), msg); //v.currentLine
            LexicalAnalizer.compilationOutput.write(s);
            System.exit(1);
        }
        else {
            aux.setType(String.valueOf(type));
        }

        aux.setField1(aux1);
        aux.setField2(aux2);

        Data data = new Data(String.valueOf("["+indexTerceto+"]"), String.valueOf(Constants.PUN_TERCETO));
        data.setCode(Constants.PUN_TERCETO);

        // Variable auxiliar asociada a cada terceto.
        Data info = new Data("@aux" + aux.getIndex(),aux.getType());
        info.setCode(data.getCode());
        aux.setVarAux(info);

        // Se añade la variable auxiliar a la tabla de simbolos.
        ST.addItem(Constants.ID,"@aux" + aux.getIndex(),aux.getType());
        tercetos.put(indexTerceto, aux);
        indexTerceto++;
        return data;
    }

    public void  tercetoLET(){
        String lexA = this.getAptr().getLexema();
        int i = Integer.valueOf(lexA.substring(1, lexA.length() - 1));
        String tipoE = this.getEptr().getType();
        this.tercetos.get(i).field1.setType(tipoE);
    }


    /**Si es ID --> devuelve el terceto en el cual sufrio la ultima actualizacion, si fue actualizada.
     * Si es una CTE devuelve la misma
     * De lo contrario solo devueelve una referencia al terceto anterior*/
    public Data lastDeclaration(Data field){
        if(field.getCode() == Constants.ID) {
            for (int i = tercetos.size(); i > 0; i--) {
                if (tercetos.get(i).getField1().getLexema().equals(field.getLexema())
                        && tercetos.get(i).getOperator().getLexema().equals("=")){
                    Data aux = new Data("[" + String.valueOf(i) + "]", String.valueOf(Constants.PUN_TERCETO));
                    aux.setCode(Constants.PUN_TERCETO);
                    return aux;
                }
            }
        }
        if(field.getCode() == Constants.CTE || field.getCode() == Constants.PUN_TERCETO || field.getCode() == Constants.ID)
            return field;
        return new Data("["+String.valueOf(indexTerceto-1)+"]", String.valueOf(Constants.PUN_TERCETO));
    }
    private int whatType(Data field){
        if(!field.getLexema().equals("BF") && !field.getLexema().equals("BI") && Integer.valueOf(field.getType()) != Constants.OTHER) {
            int index;
            if (Integer.valueOf(field.getType()) == Constants.PUN_TERCETO) {
                index = Integer.valueOf(field.getLexema().substring(1, field.getLexema().length() - 1));
                if (tercetos.get(index).getType() == null)
                    return -1;
                else {
                    return Integer.valueOf(tercetos.get(index).getType());
                }
            }
        }

        return Integer.valueOf(field.getType());
    }

    public void showTercetos(){
        System.out.println("------------------");
        System.out.println("Tercetos generados:");
        System.out.println("------------------");
        for(int i=1; i<=tercetos.size(); i++)
            System.out.println(tercetos.get(i).toString());
        System.out.println("------------------");
    }
}
