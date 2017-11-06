package base;

import java.util.Hashtable;

public class AssemblerGenerator {

    private Hashtable<Integer,Terceto> tercetos;

    public AssemblerGenerator(Hashtable<Integer,Terceto> tercetos){
        this.tercetos = tercetos;
    }

    public AssemblerGenerator(){

        tercetos = null;
    }

    public void setTercetos(Hashtable<Integer,Terceto> tercetos){
        
        this.tercetos = tercetos;
    }

    public void generate(){

        for (Integer index = 1; index <= tercetos.keySet().size(); index++) {

            Terceto terceto = tercetos.get(index);
            System.out.println("operador : " + terceto.getOperator().getLexema());
            System.out.println("Field 1 : " + terceto.getField1().getLexema());
            System.out.println("Field 2 : " + terceto.getField2().getLexema());
            System.out.println("var aux : " + terceto.getVarAux().getLexema());

            /*if ((terceto.getField2().getType() != null) && (terceto.getField2().getType().equals(((Integer) Constants.PUN_TERCETO).toString()))){

                System.out.println("MOV" + " R1" + ",_" + terceto.getField1().getLexema());
                String aux = terceto.getField2().getLexema();
                aux = aux.substring(1,aux.length()-1);
                int pos = Integer.valueOf(aux);
                System.out.println(getOperation(terceto.getOperator().getLexema()) + " R1" + "," + variablesAuxs.get(pos-1));
                System.out.println("MOV " + variablesAuxs.get(numberVarAux-1)+ ",R1");
            }
            else {

                System.out.println("MOV" + " R1" + ",_" + terceto.getField1().getLexema());
                System.out.println(getOperation(terceto.getOperator().getLexema()) + " R1" + ",_" + terceto.getField2().getLexema());
                System.out.println("MOV " + variablesAuxs.get(numberVarAux-1)+ ",R1");
            }*/
        }
    }

    public String generateMult(Terceto terceto){

        return null;
    }

    public String getOperation(String op){

        String result = null;
        switch(op){

            case "*":
                result = "MUL";
                break;
            case "+":
                result = "ADD";
                break;
            case "/":
                result = "DIV";
                break;
            case "-":
                result = "SUB";
                break;
            case "=":
                result = "MOV";
                break;
            default:
                result = "";
                break;
        }

        return result;
    }

}
