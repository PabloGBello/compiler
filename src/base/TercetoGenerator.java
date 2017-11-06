package base;


import java.util.*;

public class TercetoGenerator {

    int indexTerceto = 1;
    Hashtable<Integer,Terceto> tercetos;
    int conTermino = 3;
    boolean terminoComplete = false;
    boolean crear = true;
    boolean expresionClosed = false;
    int flagOpAnt  = -1;
    List<Integer>  pila =  new ArrayList<>();
    Conversions conversions = new Conversions();

    private Data Fptr = null;
    private Data Eptr = null;
    private Data E_sptr = null;
    private Data Tptr = null;
    private Data Aptr = null;
    private Data IDptr = null;
    private int indexDO = -1;

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

    public Data getIDptr() {
        return IDptr;
    }

    public void setIDptr(Data idptr) {
        IDptr = idptr;
    }

    public TercetoGenerator(){
        tercetos = new Hashtable<>();
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

    public void tercetoIteration(String s){

        Terceto aux = new Terceto();
        aux.setIndex(indexTerceto);
        aux.setOperator(new Data(s));
        aux.setField1(new Data("["+((Integer)(indexTerceto-1)).toString() + "]"));
        aux.setField2(new Data("[" + indexDO + "]", ((Integer) Constants.PUN_TERCETO).toString()));

        tercetos.put(indexTerceto, aux);
        indexTerceto++;
    }

    public void tercetoDesapilar(int b){
        if(b == 1)
            tercetos.get(pila.remove(0)).setField2(new Data("["+((Integer)(indexTerceto+1)).toString()+"]", ((Integer)Constants.PUN_TERCETO).toString()));
        else {
            tercetos.get(pila.get(0)).setField1(new Data("["+((Integer) (indexTerceto)).toString()+"]", ((Integer) Constants.PUN_TERCETO).toString()));
            tercetos.get(pila.remove(0)).setField2(new Data("-", ((Integer) Constants.PUN_TERCETO).toString()));
        }
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
        if(operator.equals("="))
            aux1 = field1;
        else
            aux1 = this.lastDeclaration(field1);
        if(field2.getCode() == Constants.ID)
            aux2 = this.lastDeclaration(field2);
        else
            aux2 = field2;

        int typeTerceto1 = whatType(aux1);
        int typeTerceto2 = whatType(aux2);

        int type = conversions.getConversion(operator, typeTerceto1, typeTerceto2);
        if( type == -1)
            System.out.println("Terceto: "+indexTerceto+" Incompatibilidad de tipos: "+typeTerceto1+ " y "+typeTerceto2);
        else {
            aux.setType(String.valueOf(type));
        }
        aux.setField1(aux1);
        aux.setField2(aux2);
        Data data = new Data(String.valueOf("["+indexTerceto+"]"), String.valueOf(Constants.PUN_TERCETO));

        aux.setVarAux(new Data("aux"+aux.getIndex(),aux.getType()));
        tercetos.put(indexTerceto, aux);
        indexTerceto++;
        return data;
    }

    /**Si es ID --> devuelve el terceto en el cual sufrio la ultima actualizacion, si fue actualizada.
     * Si es una CTE devuelve la misma
     * De lo contrario solo devueelve una referencia al terceto anterior*/
    public Data lastDeclaration(Data field){
        if(field.getCode() == Constants.ID) {
            for (int i = tercetos.size(); i > 0; i--) {
                if (tercetos.get(i).getField1().getLexema().equals(field.getLexema())
                        && tercetos.get(i).getOperator().getLexema().equals("=")){
                    return new Data("[" + String.valueOf(i) + "]", String.valueOf(Constants.PUN_TERCETO));
                }
            }
        }
        if(field.getCode() == Constants.CTE || field.getCode() == Constants.ID)
            return field;
        return new Data("["+String.valueOf(indexTerceto-1)+"]", String.valueOf(Constants.PUN_TERCETO));
    }
    private int whatType(Data field){
        if(!field.getLexema().equals("BF") && !field.getLexema().equals("BI")) {
            int type = 0;
            int index;
            Data aux;
            if (Integer.valueOf(field.getType()) == Constants.PUN_TERCETO) {
                index = Integer.valueOf(field.getLexema().substring(1, field.getLexema().length() - 1));
                if (tercetos.get(index).getType() == null)
                    return -1;
                else {
                    type = Integer.valueOf(tercetos.get(index).getType());
                    return type;
                }
            }
        }
        return Integer.valueOf(field.getType());
    }

    public void showTercetos(){
        System.out.println("*******************");
        System.out.println("Tercetos generados:");
        System.out.println("*******************");
        for(int i=1; i<=tercetos.size(); i++)
            System.out.println(tercetos.get(i).toString());
        System.out.println("*******************");
    }
}
