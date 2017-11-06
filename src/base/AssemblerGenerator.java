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

        for (Integer index = 1; index <= tercetos.keySet().size(); index++){

            Terceto terceto = tercetos.get(index);
            String codeAssembler = "";

            switch(terceto.getOperator().getLexema()){
                case "*":
                    codeAssembler = generateMult(terceto);
                    break;
                case "+":
                    //codeAssembler = generateAdd(terceto);
                    break;
                case "/":
                    //codeAssembler = generateDiv(terceto);
                    break;
                case "-":
                    //codeAssembler = generateSub(terceto);
                    break;
                case "=":
                    //codeAssembler = generateAsig(terceto);
                    break;
                default:
                    //codeAssembler = "";
                    break;
            }
            System.out.println(codeAssembler); // Aca iria el write al buffer del archivo.
        }
    }

    public String generateMult(Terceto terceto){
        String result = "";

        if (getCase(terceto) == 4) {
            result = "MOV EAX,_" + terceto.getField1().getLexema() + "\r\n";
            result += "MUL EAX,_" + terceto.getField2().getLexema() + "\r\n";
            result += "MOV @" + terceto.getVarAux().getLexema() + ",EAX";
        }

        // faltan los demas casos
        return result;
    }

    // Informa que tipo de campos tiene un terceto.
    public int getCase(Terceto terceto){

        // caso 1 : field1 como puntero a terceto
        if ((terceto.getField1().getType() != null) && (terceto.getField1().getType().equals(((Integer) Constants.PUN_TERCETO).toString())))
            return 1;
        else
            // caso 2 : field2 como puntero a terceto
            if ((terceto.getField2().getType() != null) && (terceto.getField2().getType().equals(((Integer) Constants.PUN_TERCETO).toString())))
                return 2;
            else
                //caso 3 : field1 y field2 punteros a tercetos
                if ((terceto.getField2().getType() != null) && (terceto.getField1().getType() != null) && (terceto.getField2().getType().equals(((Integer) Constants.PUN_TERCETO).toString())) && (terceto.getField1().getType().equals(((Integer) Constants.PUN_TERCETO).toString())))
                    return 3;
                else
                    // caso 4 : field1 o field2 con variables o constantes
                    return 4;
    }
}
