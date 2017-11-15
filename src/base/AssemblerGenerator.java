package base;

import java.util.Hashtable;
import java.util.List;

public class AssemblerGenerator {

    public static final String HEAD = ".386\r\n" +
            ".MODEL flat, stdcall\r\n" +
            ".STACK 200h\r\n" +
            "option casemap :none\r\n" +
            "include \\masm32\\include\\windows.inc" +"\r\n" +
            "include \\masm32\\include\\kernel32.inc\r\n" +
            "include \\masm32\\include\\user32.inc\r\n" +
            "includelib \\masm32\\lib\\kernel32.lib\r\n" +
            "includelib \\masm32\\lib\\user32.lib\r\n" +
            ".DATA";

    private Hashtable<Integer, Terceto> tercetos;
    private Data field1;
    private Data field2;
    private FileHandler assemblerCode;
    private FileHandler tercetosCode;
    private SymbolTable ST;
    private int contadorOUT = 1;
    public AssemblerGenerator(Hashtable<Integer, Terceto> tercetos) {
        this.tercetos = tercetos;
    }

    public AssemblerGenerator(SymbolTable ST, String path) {
        tercetos = null;
        this.ST = ST;
        assemblerCode = new FileHandler(1, path, "AssemblerCode.asm");
        tercetosCode = new FileHandler(1, path, "tercetosCode.txt");
    }

    public void setTercetos(Hashtable<Integer, Terceto> tercetos) {
        this.tercetos = tercetos;
    }

    public void generate() {
        System.out.println(HEAD);

        assemblerCode.write(HEAD);
        generateDeclarations();
        assemblerCode.write(".CODE\r\nSTART:");

        for (Integer index = 1; index <= tercetos.keySet().size(); index++) {

            Terceto terceto = tercetos.get(index);
            tercetosCode.write(terceto.toString());
            String codeAssembler = "";
            String operator = terceto.getOperator().getLexema();
            String operatorAux = "";

            setFields(terceto);

            // Verifico si es un terceto Label.
            if (terceto.getOperator().getType() != null && Integer.valueOf(terceto.getOperator().getType()).equals(Constants.BRANCH)) {
                operatorAux = operator;
                operator = "Label";
            }

            switch (operator) {
                case "*":
                    codeAssembler = generateMult(terceto);
                    break;
                case "+":
                    codeAssembler = generateAdd(terceto);
                    break;
                case "/":
                    codeAssembler = generateDiv(terceto);
                    break;
                case "-":
                    codeAssembler = generateSub(terceto);
                    break;
                case "=":
                    codeAssembler = generateAsig(terceto);
                    break;
                case "BF":
                    codeAssembler = generateBF(terceto);
                    break;
                case "BI":
                    codeAssembler = generateBI(terceto);
                    break;
                case "Label":
                    codeAssembler = operatorAux;
                    break;
                case "OUT": {
                    codeAssembler += "invoke MessageBox, NULL, addr Mensaje"+contadorOUT+", addr " + "Mensaje , MB_OK";
                    contadorOUT++;
                }
                    break;
                case "I_F":
                    codeAssembler = generateI_F(terceto);
                    break;
                default:
                    codeAssembler = generateCMP(terceto);
                    break;
            }
            assemblerCode.write(codeAssembler);
        }
        assemblerCode.write("END START");
        assemblerCode.closeWriter();
        tercetosCode.closeWriter();
    }

    public void generateDeclarations() {

        List<Data> declarations = ST.getSimbolos().get(Constants.ID);
        for (Data d : declarations)
            if (Integer.valueOf(d.getType()).equals(Constants.INT)) {
                if (d.getLexema().contains("@"))
                    assemblerCode.write(d.getLexema() + " DW ?");
                else
                    assemblerCode.write("_" + d.getLexema() + " DW ?");
            } else {
                if (d.getLexema().contains("@"))
                    assemblerCode.write(d.getLexema() + " DQ ?");
                else
                    assemblerCode.write("_" + d.getLexema() + " DQ ?");
            }
        declarations.clear();
        declarations = ST.getSimbolos().get(Constants.CADENA);
        int cont = 1;
        assemblerCode.write("Mensaje db "+ "\"Mensaje\"" +", 0");
        assemblerCode.write( "ERROR_DIV db \"Division by zero\", 0");
        assemblerCode.write( "ERROR_OVERF db \"Overflow in result\", 0");
        //assemblerCode.write( "ERROR_DIV db \"Division by zero\", 0");
        //assemblerCode.write( "ERROR_DIV db \"Division by zero\", 0");

        for (Data d : declarations) {
            assemblerCode.write( "Mensaje"+cont+" db "+ d.getLexema() + ", 0");
            cont++;
        }
        declarations.clear();
        declarations = ST.getSimbolos().get(Constants.CTE);
        int contFloat =1;
        for(Data d : declarations){
            if(Integer.valueOf(d.getType()) == Constants.FLOAT){
                String auxF = "@float"+contFloat;
                assemblerCode.write(auxF + " DQ "+d.getLexema());
                d.setLexema(auxF);

                if(contFloat == 1){
                    assemblerCode.write("@f_max " + "DQ 3.40e38");
                    assemblerCode.write("@f_mim " + "DQ 1.18e-38");
                    assemblerCode.write("@f_a1"  + " DQ ?");
                    assemblerCode.write("@f_a2"  + " DQ ?");
                }

                contFloat++;
            }
        }
    }

    public String generateI_F(Terceto terceto) {

        String result = "";

        result = "MOV EAX," + field1.getLexema() + "\r\n" +
                "MOV " + terceto.getVarAux().getLexema() + ",EAX";

        //float res = ((Float) field1.getValue()) - ((Float) field2.getValue());
        //terceto.getVarAux().setValue(res);
        return result;
    }

    public String generateBI(Terceto terceto) {

        String result = "";
        String aux = terceto.getField1().getLexema();
        aux = aux.substring(1, aux.length() - 1);
        int pos = Integer.valueOf(aux);
        result = "JMP Label" + pos;
        return result;
    }

    public String generateBF(Terceto terceto) {

        String result = "";
        String oper = "";
        Terceto tercetoCMP = tercetos.get(terceto.getIndex() - 1);

        switch (tercetoCMP.getOperator().getLexema()) {
            case ">":
                oper = "JG";
                break;
            case "<>":
                oper = "JNE";
                break;
            case ">=":
                oper = "JGE";
                break;
            case "==":
                oper = "JE";
                break;
            case "<":
                oper = "JL";
                break;
            case "<=":
                oper = "JLE";
                break;
        }

        String aux = terceto.getField2().getLexema();
        aux = aux.substring(1, aux.length() - 1);
        int pos = Integer.valueOf(aux);
        result = oper + " Label" + pos;
        return result;
    }

    public String generateCMP(Terceto terceto) {
        String result = "";
        result = "CMP " + field1.getLexema() + "," + field2.getLexema();
        return result;
    }

    public String generateMult(Terceto terceto) {
        System.out.println("Crea multiplicacion con: "+terceto);
        String result = "";

        String error = "invoke MessageBox, NULL, addr ERROR_OVERF, addr Mensaje, MB_OK\r\ninvoke ExitProcess, 0";

        if (Integer.valueOf(terceto.getType()).equals(Constants.INT)) {

            result = "MOV AX," + field1.getLexema() + "\r\n" +
                    "IMUL AX," + field2.getLexema() + "\r\n" +
                    "JNO Label" + terceto.getIndex() + "\r\n" +
                    error + "\r\n" +
                    "Label" + terceto.getIndex() + ": \r\n" +
                    "MOV " + terceto.getVarAux().getLexema() + ",AX";

        } else if (Integer.valueOf(terceto.getType()).equals(Constants.FLOAT)) {

            result = "FLD " +field1.getLexema() + "\r\n" +
                    "FSTP @f_a1" + "\r\n" +
                    "FLD " +field2.getLexema() + "\r\n" +
                    "FSTP @f_a2" + "\r\n" +
                    "FLD @f_a1" + "\r\n" +
                    "FLD @f_a2" + "\r\n" +
                    "FMUL " + "\r\n" +
                    "FSTP " + terceto.getVarAux().getLexema() +"\r\n" +
                    "FLD @f_max" + "\r\n" +         /****A*/
                    "FLD " + terceto.getVarAux().getLexema() +"\r\n" + /****B*/
                    "FCOMPP"  + "\r\n" +
                    "FSTSW AX" + "\r\n" +
                    "FFREE ST(0)"  + "\r\n" +
                    "FFREE ST(1)"  + "\r\n" +
                    "FWAIT"  + "\r\n" +
                    "SAHF"  + "\r\n" +
                    "JBE Label" + terceto.getIndex() + "\r\n" + /****B <= A*/
                    error + "\r\n" +
                    "Label" + terceto.getIndex() + ":";
        }
        return result;
    }

    public String generateSub(Terceto terceto) {

        String result = "";
        if (Integer.valueOf(terceto.getType()).equals(Constants.INT)) {

            result = "MOV AX," + field1.getLexema() + "\r\n" +
                    "SUB AX," + field2.getLexema() + "\r\n" +
                    "MOV " + terceto.getVarAux().getLexema() + ",AX";
        } else if (Integer.valueOf(terceto.getType()).equals(Constants.FLOAT)) {
            result = "MOV EAX," + field1.getLexema() + "\r\n" +
                    "SUB EAX," + field2.getLexema() + "\r\n" +
                    "MOV " + terceto.getVarAux().getLexema() + ",EAX";
        }
        return result;
    }

    public String generateDiv(Terceto terceto) {

        String result = "";
        String error = "invoke MessageBox, NULL, addr ERROR_DIV, addr Mensaje, MB_OK\r\ninvoke ExitProcess, 0";

        if (Integer.valueOf(terceto.getType()).equals(Constants.INT)) {

            result = "MOV AX,0"+ " \r\n" +
                    "CMP AX," + field2.getLexema() + " \r\n" +
                    "JNE Label" + terceto.getIndex() + "\r\n" +
                    error + "\r\n" +
                    "Label" + terceto.getIndex() + ":\r\n" +
                    "MOV AX," + field1.getLexema() + "\r\n" +
                    "MOV DX,0" + "\r\n" +
                    "MOV BX," + field2.getLexema() + "\r\n" +
                    "IDIV BX" + "\r\n" +
                    "MOV " + terceto.getVarAux().getLexema() + ",BX";
        } else if (Integer.valueOf(terceto.getType()).equals(Constants.FLOAT)) {

            result = "MOV AX,0"+ " \r\n" +
                    "CMP AX," + field2.getLexema() + " \r\n" +
                    "JNE Label" + terceto.getIndex() + "\r\n" +
                    error + "\r\n" +
                    "Label" + terceto.getIndex() + ": \r\n" +
                    "MOV EAX," + field1.getLexema() + "\r\n" +
                    "FDIV EAX," + field2.getLexema() + "\r\n" +
                    "MOV " + terceto.getVarAux().getLexema() + ",EAX";
        }

        return result;
    }

    public String generateAdd(Terceto terceto) {

        String result = "";

        String error = "invoke MessageBox, NULL, addr ERROR_OVERF, addr Mensaje, MB_OK\r\ninvoke ExitProcess, 0";

        if (Integer.valueOf(terceto.getType()).equals(Constants.INT)) {

            result = "MOV AX," + field1.getLexema() + "\r\n" +
                    "ADD AX," + field2.getLexema() + "\r\n" +
                    "JNO Label" + terceto.getIndex() + "\r\n" +
                    error + "\r\n" +
                    "Label" + terceto.getIndex() + ": \r\n" +
                    "MOV " + terceto.getVarAux().getLexema() + ",AX";
        } else if (Integer.valueOf(terceto.getType()).equals(Constants.FLOAT)) {

            result = "FLD " +field1.getLexema() + "\r\n" +
                    "FSTP @f_a1" + "\r\n" +
                    "FLD " +field2.getLexema() + "\r\n" +
                    "FSTP @f_a2" + "\r\n" +
                    "FLD @f_a1" + "\r\n" +
                    "FLD @f_a2" + "\r\n" +
                    "FADD " + "\r\n" +
                    "FSTP " + terceto.getVarAux().getLexema() +"\r\n" +
                    "FLD @f_max" + "\r\n" +         /****A*/
                    "FLD " + terceto.getVarAux().getLexema() +"\r\n" + /****B*/
                    "FCOMPP"  + "\r\n" +
                    "FSTSW AX" + "\r\n" +
                    "FFREE ST(0)"  + "\r\n" +
                    "FFREE ST(1)"  + "\r\n" +
                    "FWAIT"  + "\r\n" +
                    "SAHF"  + "\r\n" +
                    "JBE Label" + terceto.getIndex() + "\r\n" + /****B <= A*/
                    error + "\r\n" +
                    "Label" + terceto.getIndex() + ":";
        }
        return result;
    }

    public String generateAsig(Terceto terceto) {

        String result = "";
        System.out.println("GenerateAsig terceto: " +terceto+"  "+field1+"  "+field2);
        field1.setValue(field2.getValue());

        if (Integer.valueOf(terceto.getType()).equals(Constants.INT)) {
            result = "MOV AX," + field2.getLexema() + "\r\n" +
                    "MOV _" + field1.getLexema() + ",AX" + "\r\n" +
                    "MOV " + terceto.getVarAux().getLexema() + ",AX";
        } else if (Integer.valueOf(terceto.getType()).equals(Constants.FLOAT)) {
            result = "FLD " + field2.getLexema() + "\r\n" +
                    "FSTP _" + field1.getLexema() + "\r\n" +
                    "FSTP " + terceto.getVarAux().getLexema();
        }
        terceto.getVarAux().setValue(field2.getValue());
        return result;
    }

    public void setFields(Terceto terceto) {

        String aux = "";
        int pos = 0;
        int caso = this.getCase(terceto);

        if (caso == 1) {
            aux = terceto.getField1().getLexema();
            aux = aux.substring(1, aux.length() - 1);
            pos = Integer.valueOf(aux);

            field1 = tercetos.get(pos).getVarAux();
            field2 = terceto.getField2();
        } else if (caso == 2) {

            field1 = terceto.getField1();
            aux = terceto.getField2().getLexema();
            aux = aux.substring(1, aux.length() - 1);
            pos = Integer.valueOf(aux);
            field2 = tercetos.get(pos).getVarAux();
        } else if (caso == 3) {

            aux = terceto.getField1().getLexema();
            aux = aux.substring(1, aux.length() - 1);
            pos = Integer.valueOf(aux);

            field1 = tercetos.get(pos).getVarAux();

            aux = terceto.getField2().getLexema();
            aux = aux.substring(1, aux.length() - 1);
            pos = Integer.valueOf(aux);

            field2 = tercetos.get(pos).getVarAux();
        } else if (caso == 4) {

            field1 = terceto.getField1();
            field2 = terceto.getField2();
            System.out.println("Entra al caso 4 con:" + terceto);
        }
    }

    // Informa que tipo de campos tiene un terceto.
    public int getCase(Terceto terceto) {

        int v1 = 0;
        int v2 = 0;
        int v3 = 0;

        if (terceto.getField1().getType() != null && terceto.getField2().getType() != null) {

            v1 = Integer.valueOf(terceto.getField1().getType());
            v2 = Integer.valueOf(terceto.getField2().getType());
        }

        v3 = (terceto.getOperator().getType() != null) ? Integer.valueOf(terceto.getOperator().getType()) : 0;

        // Caso de los tercetos solo con Labels
        if (v3 == Constants.BRANCH)
            return 5;
        else
            // caso 1 : field1 como puntero a terceto
            if (v1 == Constants.PUN_TERCETO && v2 != Constants.PUN_TERCETO)
                return 1;
            else
                // caso 2 : field2 como puntero a terceto
                if (v1 != Constants.PUN_TERCETO && v2 == Constants.PUN_TERCETO)
                    return 2;
                else
                    //caso 3 : field1 y field2 punteros a tercetos
                    if (v1 == Constants.PUN_TERCETO && v2 == Constants.PUN_TERCETO)
                        return 3;
                    else
                        return 4;
    }
}

