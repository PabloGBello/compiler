package base;

import AASS.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LexicalAnalizer {

    private Matriz SA = new Matriz();
    private Matriz FSM = new Matriz();
    private ArrayList<Character> code;
    private List<Integer> token = new ArrayList();

    public static FileHandler detectedTokens;

    public Data val;

    public static FileHandler compilationOutput, STOutput;

    public static Values values = new Values();
    public SymbolTable ST;

    public LexicalAnalizer(String dir, String fileName){

        //Obtenemos el contenido del archivo fuente
        FileHandler codeReader = new FileHandler(0, dir, fileName);
        code = codeReader.dumpSourceCode();

        //Instanciamos el manejador de archivo para la salida de la compilacion
        compilationOutput = new FileHandler(1, dir, "OUTPUT.txt");

        //Instanciamos el manejador de archivo para volcado de la TS
        STOutput = new FileHandler(1, dir, "ST_DUMP.txt");

        detectedTokens = new FileHandler(1, dir, "TOKENS_DUMP.txt");

        // CREAMOS LA TABLA DE SIMBOLOS
        ST = new SymbolTable(compilationOutput);

        // CARGAMOS TRANSICIONES Y A.S.
        this.loadStates();
        this.loadActions();
    }

    public List<Integer> getToken(){

        token.clear();

        String currItem;
        Integer currState;

        while (( values.getI() < code.size()) && (values.getState() != 15 )){

            currItem = Tools.translate(code.get(values.getI()));
            currState = values.getState();

            values.setState((Integer)FSM.get(currState, currItem));
            if(SA.get(currState, currItem) != null){
                token = ((SemanticAction)SA.get(currState, currItem)).execute(code.get(values.getI()).toString());
            }
            values.addI();
        }
        values.setState(0);
        return token;
    }

    public int yylex(){

        token = this.getToken();

        if(token != null && !token.isEmpty())
            val = valGenerate(token);
        return ( (token == null) || (token.isEmpty()) ) ? 0 : token.get(0);
    }

    private Data valGenerate(List<Integer> token){
        Data aux = new Data();
        if(token.get(1) == null) {
            char ch = (char) token.get(0).intValue();
            aux.setLexema(String.valueOf(ch));
            aux.setCode(token.get(0));
            return aux;
        }
        else{
            return ST.getData(token.get(0), token.get(1));
        }

    }

    public SymbolTable getSymbolTable(){
        return ST;
    }

    public void outputST(){
        STOutput.dumpST(ST);
    }

    //<editor-fold desc="Load States" default-state=collapsed>


    private void loadStates(){

        FSM.add(0, "BL", 0);
        FSM.add(0, "NL", 0);
        FSM.add(0, "TAB", 0);
        FSM.add(0, 'c', 0);

        FSM.add(0, '(', 15);
        FSM.add(0, ')', 15);
        FSM.add(0, ':', 15);
        FSM.add(0, '.', 15);
        FSM.add(0, '+', 15);
        FSM.add(0, '-', 15);
        FSM.add(0, '/', 15);

        FSM.add(0, 'l', 1);
        FSM.add(0, 'E', 1);


        FSM.add(0, '*', 2);
        FSM.add(0, 'd', 4);
        FSM.add(0, ',', 8);
        FSM.add(0, '<', 5);
        FSM.add(0, '=', 6);
        FSM.add(0, '>', 6);
        FSM.add(0, '"', 10);

        FSM.add(1, 'l', 1);
        FSM.add(1, 'E', 1);
        FSM.add(1, 'd', 1);
        FSM.add(1, '_', 1);
        FSM.add(1, "BL", 15);
        FSM.add(1, "NL", 15);
        FSM.add(1, "TAB", 15);
        FSM.add(1, 'c', 15);
        FSM.add(1, '=', 15);
        FSM.add(1, '>', 15);
        FSM.add(1, '<', 15);
        FSM.add(1, ',', 15);
        FSM.add(1, '(', 15);
        FSM.add(1, ')', 15);
        FSM.add(1, ':', 15);
        FSM.add(1, '.', 15);
        FSM.add(1, '"', 15);
        FSM.add(1, '+', 15);
        FSM.add(1, '-', 15);
        FSM.add(1, '/', 15);
        FSM.add(1, '*', 15);

        FSM.add(2,'*', 3);

        FSM.add(2,'l', 15);
        FSM.add(2,'d', 15);
        FSM.add(2,'_', 15);
        FSM.add(2,"BL", 15);
        FSM.add(2,"NL", 15);
        FSM.add(2,"TAB", 15);
        FSM.add(2,'c', 15);
        FSM.add(2,'=', 15);
        FSM.add(2,'>', 15);
        FSM.add(2,'<', 15);
        FSM.add(2,',', 15);
        FSM.add(2,'(', 15);
        FSM.add(2,')', 15);
        FSM.add(2,':', 15);
        FSM.add(2,'.', 15);
        FSM.add(2,'\'', 15);
        FSM.add(2,'+', 15);
        FSM.add(2,'-', 15);
        FSM.add(2,'/', 15);
        FSM.add(2,'_', 15);
        FSM.add(2,'E', 15);

        FSM.add(3, "NL", 0);

        FSM.add(3, 'l', 3);
        FSM.add(3, 'd', 3);
        FSM.add(3, '_', 3);
        FSM.add(3, "BL", 3);
        FSM.add(3, "TAB", 3);
        FSM.add(3, 'c', 3);
        FSM.add(3, '=', 3);
        FSM.add(3, '>', 3);
        FSM.add(3, '<', 3);
        FSM.add(3, ',', 3);
        FSM.add(3, '(', 3);
        FSM.add(3, ')', 3);
        FSM.add(3, ':', 3);
        FSM.add(3, '.', 3);
        FSM.add(3, '"', 3);
        FSM.add(3, '+', 3);
        FSM.add(3, '-', 3);
        FSM.add(3, '/', 3);
        FSM.add(3, '*', 3);
        FSM.add(3, '_', 3);
        FSM.add(3, 'E', 3);

        FSM.add(4, 'd', 4);
        FSM.add(4, ',', 8);
        FSM.add(4, 'l', 15);
        FSM.add(4, '_', 15);
        FSM.add(4, "BL", 15);
        FSM.add(4, "NL", 15);
        FSM.add(4, "TAB", 15);
        FSM.add(4, 'c', 15);
        FSM.add(4, '=', 15);
        FSM.add(4, '>', 15);
        FSM.add(4, '<', 15);
        FSM.add(4, '(', 15);
        FSM.add(4, ')', 15);
        FSM.add(4, ':', 15);
        FSM.add(4, '.', 15);
        FSM.add(4, '"', 15);
        FSM.add(4, '+', 15);
        FSM.add(4, '-', 15);
        FSM.add(4, '/', 15);
        FSM.add(4, '*', 15);
        FSM.add(4, '_', 15);

        FSM.add(5, '>', 15);
        FSM.add(5, '=', 15);

        FSM.add(5, 'l', 15);
        FSM.add(5, 'd', 15);
        FSM.add(5, '_', 15);
        FSM.add(5, "BL", 15);
        FSM.add(5, "NL", 15);
        FSM.add(5, "TAB", 15);
        FSM.add(5, 'c', 15);
        FSM.add(5, '<', 15);
        FSM.add(5, ',', 15);
        FSM.add(5, '(', 15);
        FSM.add(5, ')', 15);
        FSM.add(5, ':', 15);
        FSM.add(5, '.', 15);
        FSM.add(5, '"', 15);
        FSM.add(5, '+', 15);
        FSM.add(5, '-', 15);
        FSM.add(5, '/', 15);
        FSM.add(5, '*', 15);
        FSM.add(5, '_', 15);

        FSM.add(6, '=', 15);

        FSM.add(6, 'l', 15);
        FSM.add(6, 'd', 15);
        FSM.add(6, '_', 15);
        FSM.add(6, "BL", 15);
        FSM.add(6, "NL", 15);
        FSM.add(6, "TAB", 15);
        FSM.add(6, 'c', 15);
        FSM.add(6, '>', 15);
        FSM.add(6, '<', 15);
        FSM.add(6, ',', 15);
        FSM.add(6, '(', 15);
        FSM.add(6, ')', 15);
        FSM.add(6, ':', 15);
        FSM.add(6, '.', 15);
        FSM.add(6, '"', 15);
        FSM.add(6, '+', 15);
        FSM.add(6, '-', 15);
        FSM.add(6, '/', 15);
        FSM.add(6, '*', 15);
        FSM.add(6, '_', 15);

        FSM.add(7, 'd', 9);
        FSM.add(7, '+', 9);
        FSM.add(7, '-', 9);

        FSM.add(8, 'd', 8);
        FSM.add(8, 'E', 7);
        FSM.add(8, 'l', 15);
        FSM.add(8, '_', 15);
        FSM.add(8, "BL", 15);
        FSM.add(8, "NL", 15);
        FSM.add(8, "TAB", 15);
        FSM.add(8, 'c', 15);
        FSM.add(8, '=', 15);
        FSM.add(8, '>', 15);
        FSM.add(8, '<', 15);
        FSM.add(8, ',', 15);
        FSM.add(8, '(', 15);
        FSM.add(8, ')', 15);
        FSM.add(8, ':', 15);
        FSM.add(8, '.', 15);
        FSM.add(8, '"', 15);
        FSM.add(8, '+', 15);
        FSM.add(8, '-', 15);
        FSM.add(8, '/', 15);
        FSM.add(8, '*', 15);
        FSM.add(8, '_', 15);


        FSM.add(9, 'd', 9);
        FSM.add(9, 'l', 15);
        FSM.add(9, '_', 15);
        FSM.add(9, "BL", 15);
        FSM.add(9, "NL", 15);
        FSM.add(9, "TAB", 15);
        FSM.add(9, 'c', 15);
        FSM.add(9, '=', 15);
        FSM.add(9, '>', 15);
        FSM.add(9, '<', 15);
        FSM.add(9, ',', 15);
        FSM.add(9, '(', 15);
        FSM.add(9, ')', 15);
        FSM.add(9, ':', 15);
        FSM.add(9, '.', 15);
        FSM.add(9, '"', 15);
        FSM.add(9, '+', 15);
        FSM.add(9, '-', 15);
        FSM.add(9, '/', 15);
        FSM.add(9, '*', 15);
        FSM.add(9, '_', 15);

        FSM.add(10, '"', 11);
        FSM.add(10, '.', 12);
        FSM.add(10, 'l', 10);
        FSM.add(10, 'd', 10);
        FSM.add(10, '_', 10);
        FSM.add(10, "BL", 10);
        FSM.add(10, "NL", 10);
        FSM.add(10, "TAB", 10);
        FSM.add(10, 'c', 10);
        FSM.add(10, '=', 10);
        FSM.add(10, '>', 10);
        FSM.add(10, '<', 10);
        FSM.add(10, ',', 10);
        FSM.add(10, '(', 10);
        FSM.add(10, ')', 10);
        FSM.add(10, ':', 10);
        FSM.add(10, '+', 10);
        FSM.add(10, '-', 10);
        FSM.add(10, '/', 10);
        FSM.add(10, '*', 10);
        FSM.add(10, '_', 10);
        FSM.add(10, 'E', 10);

        FSM.add(11, '"', 10);
        FSM.add(11, 'l', 15);
        FSM.add(11, 'd', 15);
        FSM.add(11, '_', 15);
        FSM.add(11, "BL", 15);
        FSM.add(11, "NL", 15);
        FSM.add(11, "TAB", 15);
        FSM.add(11, 'c', 15);
        FSM.add(11, '=', 15);
        FSM.add(11, '>', 15);
        FSM.add(11, '<', 15);
        FSM.add(11, ',', 15);
        FSM.add(11, '(', 15);
        FSM.add(11, ')', 15);
        FSM.add(11, ':', 15);
        FSM.add(11, '.', 15);
        FSM.add(11, '+', 15);
        FSM.add(11, '-', 15);
        FSM.add(11, '/', 15);
        FSM.add(11, '*', 15);
        FSM.add(11, '_', 15);

        FSM.add(12, '.', 13);
        FSM.add(12, 'l', 10);
        FSM.add(12, 'd', 10);
        FSM.add(12, '_', 10);
        FSM.add(12, "BL", 10);
        FSM.add(12, "NL", 10);
        FSM.add(12, "TAB", 10);
        FSM.add(12, 'c', 10);
        FSM.add(12, '=', 10);
        FSM.add(12, '>', 10);
        FSM.add(12, '<', 10);
        FSM.add(12, ',', 10);
        FSM.add(12, '(', 10);
        FSM.add(12, ')', 10);
        FSM.add(12, ':', 10);
        FSM.add(12, '"', 10);
        FSM.add(12, '+', 10);
        FSM.add(12, '-', 10);
        FSM.add(12, '/', 10);
        FSM.add(12, '*', 10);
        FSM.add(12, '_', 10);
        FSM.add(12, 'E', 10);
        FSM.add(12, '"', 11);

        FSM.add(13, '.', 14);
        FSM.add(13, 'l', 10);
        FSM.add(13, 'd', 10);
        FSM.add(13, '_', 10);
        FSM.add(13, "BL", 10);
        FSM.add(13, "NL", 10);
        FSM.add(13, "TAB", 10);
        FSM.add(13, 'c', 10);
        FSM.add(13, '=', 10);
        FSM.add(13, '>', 10);
        FSM.add(13, '<', 10);
        FSM.add(13, ',', 10);
        FSM.add(13, '(', 10);
        FSM.add(13, ')', 10);
        FSM.add(13, ':', 10);
        FSM.add(13, '"', 10);
        FSM.add(13, '+', 10);
        FSM.add(13, '-', 10);
        FSM.add(13, '/', 10);
        FSM.add(13, '*', 10);
        FSM.add(13, '_', 10);
        FSM.add(13, 'E', 10);
        FSM.add(13, '"', 11);

        FSM.add(14, '.', 14);
        FSM.add(14, "NL", 10);
        FSM.add(14, 'l', 10);
        FSM.add(14, 'd', 10);
        FSM.add(14, '_', 10);
        FSM.add(14, "BL", 10);
        FSM.add(14, "TAB", 10);
        FSM.add(14, 'c', 14);
        FSM.add(14, '=', 10);
        FSM.add(14, '>', 10);
        FSM.add(14, '<', 10);
        FSM.add(14, ',', 10);
        FSM.add(14, '(', 10);
        FSM.add(14, ')', 10);
        FSM.add(14, ':', 10);
        FSM.add(14, '"', 10);
        FSM.add(14, '+', 10);
        FSM.add(14, '-', 10);
        FSM.add(14, '/', 10);
        FSM.add(14, '*', 10);
        FSM.add(14, '_', 10);
        FSM.add(14, 'E', 10);
        FSM.add(14, '"', 11);

    }

    //</editor-fold>

    //<editor-fold desc="Load SA">

    private void loadActions(){

        SemanticAction as1 = new AS1(ST, values);
        SemanticAction as2 = new AS2(ST, values);
        SemanticAction as3 = new AS3(ST, values);
        SemanticAction as4 = new AS4(ST, values);
        SemanticAction as5 = new AS5(ST, values);
        SemanticAction as6 = new AS6(ST, values);
        SemanticAction as7 = new AS7(ST, values);
        SemanticAction as8 = new AS8(ST, values);
        SemanticAction as9 = new AS9(ST, values);
        SemanticAction as10 = new AS10(ST, values);
        SemanticAction as11 = new AS11(ST, values);
        SemanticAction as12 = new AS12(ST, values);

        SA.add(0, 'l', as6);
        SA.add(0, 'E', as6);


        /*SA.add(0, "BL", null);
        SA.add(0, "NL", null);
        SA.add(0, "TAB", null);
        SA.add(0, 'c', null);*/

        SA.add(0, "NL", as3);
        /*SA.add(1, "NL", as3);
        SA.add(2,"NL", as3);
        SA.add(3, "NL", as3);
        SA.add(4, "NL", as3);
        SA.add(5, "NL", as3);
        SA.add(6, "NL", as3);
        SA.add(7, "NL", as3);
        SA.add(8, "NL", as3);
        SA.add(9, "NL", as3);
        SA.add(10, "NL", as3);
        SA.add(11, "NL", as3);
        SA.add(12, "NL", as3);
        SA.add(13, "NL", as3);
        SA.add(14, "NL", as3);*/

        SA.add(0, '(', as2);
        SA.add(0, ')', as2);
        SA.add(0, ':', as2);
        SA.add(0, '.', as2);
        SA.add(0, '+', as2);
        SA.add(0, '-', as2);
        SA.add(0, '/', as2);

        SA.add(0, '*', as6);
        SA.add(0, 'd', as6);
        SA.add(0, ',', as6);
        SA.add(0, '<', as6);
        SA.add(0, '=', as6);
        SA.add(0, '>', as6);

        SA.add(0, '"', as6);

        SA.add(1, 'l', as5);
        SA.add(1, 'E', as5);
        SA.add(1, 'd', as5);
        SA.add(1, '_', as5);

        SA.add(1, "BL", as1);
        SA.add(1, "NL", as1);
        SA.add(1, "TAB",as1);
        SA.add(1, 'c', as1);
        SA.add(1, '=', as1);
        SA.add(1, '>', as1);
        SA.add(1, '<', as1);
        SA.add(1, ',', as1);
        SA.add(1, '(', as1);
        SA.add(1, ')', as1);
        SA.add(1, ':', as1);
        SA.add(1, '.', as1);
        SA.add(1, '"', as1);
        SA.add(1, '+', as1);
        SA.add(1, '-', as1);
        SA.add(1, '/', as1);
        SA.add(1, '*', as1);

        SA.add(2,'l', as8);
        SA.add(2,'d', as8);
        SA.add(2,'_', as8);
        SA.add(2,"BL", as8);
        SA.add(2,"NL", as8);
        SA.add(2,"TAB", as8);
        SA.add(2,'c', as8);
        SA.add(2,'=', as8);
        SA.add(2,'>', as8);
        SA.add(2,'<', as8);
        SA.add(2,',', as8);
        SA.add(2,'(', as8);
        SA.add(2,')', as8);
        SA.add(2,':', as8);
        SA.add(2,'.', as8);
        SA.add(2,'\'', as8);
        SA.add(2,'+', as8);
        SA.add(2,'-', as8);
        SA.add(2,'/', as8);
        SA.add(2,'_', as8);
        SA.add(2,'E', as8);

        /*SA.add(2,'*', 3);*/

        //SA.add(3, "NL", as3);

        /*SA.add(3, 'l', 3);
        SA.add(3, 'd', 3);
        SA.add(3, '_', 3);
        SA.add(3, "BL", 3);
        SA.add(3, "TAB", 3);
        SA.add(3, 'c', 3);
        SA.add(3, '=', 3);
        SA.add(3, '>', 3);
        SA.add(3, '<', 3);
        SA.add(3, ',', 3);
        SA.add(3, '(', 3);
        SA.add(3, ')', 3);
        SA.add(3, ':', 3);
        SA.add(3, '.', 3);
        SA.add(3, '"', 3);
        SA.add(3, '+', 3);
        SA.add(3, '-', 3);
        SA.add(3, '/', 3);
        SA.add(3, '*', 3);
        SA.add(3, '_', 3);*/

        SA.add(3, "NL", as3);

        SA.add(4, 'd', as5);
        SA.add(4, ',', as5);

        SA.add(4, 'l', as10);
        SA.add(4, '_', as10);
        SA.add(4, "BL", as10);
        SA.add(4, "NL", as10);
        SA.add(4, "TAB", as10);
        SA.add(4, 'c', as10);
        SA.add(4, '=', as10);
        SA.add(4, '>', as10);
        SA.add(4, '<', as10);
        SA.add(4, '(', as10);
        SA.add(4, ')', as10);
        SA.add(4, ':', as10);
        SA.add(4, '.', as10);
        SA.add(4, '"', as10);
        SA.add(4, '+', as10);
        SA.add(4, '-', as10);
        SA.add(4, '/', as10);
        SA.add(4, '*', as10);
        SA.add(4, '_', as10);

        SA.add(5, '>', as7);
        SA.add(5, '=', as7);

        SA.add(5, 'l', as8);
        SA.add(5, 'd', as8);
        SA.add(5, '_', as8);
        SA.add(5, "BL", as8);
        SA.add(5, "NL", as8);
        SA.add(5, "TAB", as8);
        SA.add(5, 'c', as8);
        SA.add(5, '<', as8);
        SA.add(5, ',', as8);
        SA.add(5, '(', as8);
        SA.add(5, ')', as8);
        SA.add(5, ':', as8);
        SA.add(5, '.', as8);
        SA.add(5, '"', as8);
        SA.add(5, '+', as8);
        SA.add(5, '-', as8);
        SA.add(5, '/', as8);
        SA.add(5, '*', as8);
        SA.add(5, '_', as8);

        SA.add(6, '=', as7);

        SA.add(6, 'l', as8);
        SA.add(6, 'd', as8);
        SA.add(6, '_', as8);
        SA.add(6, "BL", as8);
        SA.add(6, "NL", as8);
        SA.add(6, "TAB", as8);
        SA.add(6, 'c', as8);
        SA.add(6, '>', as8);
        SA.add(6, '<', as8);
        SA.add(6, ',', as8);
        SA.add(6, '(', as8);
        SA.add(6, ')', as8);
        SA.add(6, ':', as8);
        SA.add(6, '.', as8);
        SA.add(6, '"', as8);
        SA.add(6, '+', as8);
        SA.add(6, '-', as8);
        SA.add(6, '/', as8);
        SA.add(6, '*', as8);
        SA.add(6, '_', as8);

        SA.add(7, 'd', as5);
        SA.add(7, '+', as5);
        SA.add(7, '-', as5);

        SA.add(8, 'd', as5);
        SA.add(8, 'E', as5);

        SA.add(8, 'l', as11);
        SA.add(8, '_', as11);
        SA.add(8, "BL", as11);
        SA.add(8, "NL", as11);
        SA.add(8, "TAB", as11);
        SA.add(8, 'c', as11);
        SA.add(8, '=', as11);
        SA.add(8, '>', as11);
        SA.add(8, '<', as11);
        SA.add(8, ',', as11);
        SA.add(8, '(', as11);
        SA.add(8, ')', as11);
        SA.add(8, ':', as11);
        SA.add(8, '.', as11);
        SA.add(8, '"', as11);
        SA.add(8, '+', as11);
        SA.add(8, '-', as11);
        SA.add(8, '/', as11);
        SA.add(8, '*', as11);
        SA.add(8, '_', as11);


        SA.add(9, 'd', as5);

        SA.add(9, 'l', as12);
        SA.add(9, '_', as12);
        SA.add(9, "BL", as12);
        SA.add(9, "NL", as12);
        SA.add(9, "TAB", as12);
        SA.add(9, 'c', as12);
        SA.add(9, '=', as12);
        SA.add(9, '>', as12);
        SA.add(9, '<', as12);
        SA.add(9, ',', as12);
        SA.add(9, '(', as12);
        SA.add(9, ')', as12);
        SA.add(9, ':', as12);
        SA.add(9, '.', as12);
        SA.add(9, '"', as12);
        SA.add(9, '+', as12);
        SA.add(9, '-', as12);
        SA.add(9, '/', as12);
        SA.add(9, '*', as12);
        SA.add(9, '_', as12);


        SA.add(10, '"', as5);
        SA.add(10, '.', as5);

        SA.add(10, 'l', as5);
        SA.add(10, 'E', as5);
        SA.add(10, 'd', as5);
        SA.add(10, '_', as5);
        SA.add(10, "BL", as5);
        SA.add(10, "NL", as5);
        SA.add(10, "TAB", as5);
        SA.add(10, 'c', as5);
        SA.add(10, '=', as5);
        SA.add(10, '>', as5);
        SA.add(10, '<', as5);
        SA.add(10, ',', as5);
        SA.add(10, '(', as5);
        SA.add(10, ')', as5);
        SA.add(10, ':', as5);
        SA.add(10, '+', as5);
        SA.add(10, '-', as5);
        SA.add(10, '/', as5);
        SA.add(10, '*', as5);
        SA.add(10, '_', as5);

        SA.add(11, '"', as5);

        SA.add(11, 'l', as9);
        SA.add(11, 'd', as9);
        SA.add(11, '_', as9);
        SA.add(11, "BL", as9);
        SA.add(11, "NL", as9);
        SA.add(11, "TAB", as9);
        SA.add(11, 'c', as9);
        SA.add(11, '=', as9);
        SA.add(11, '>', as9);
        SA.add(11, '<', as9);
        SA.add(11, ',', as9);
        SA.add(11, '(', as9);
        SA.add(11, ')', as9);
        SA.add(11, ':', as9);
        SA.add(11, '.', as9);
        SA.add(11, '+', as9);
        SA.add(11, '-', as9);
        SA.add(11, '/', as9);
        SA.add(11, '*', as9);
        SA.add(11, '_', as9);


        SA.add(12, '.', as5);
        SA.add(12, 'l', as5);
        SA.add(12, 'd', as5);
        SA.add(12, '_', as5);
        SA.add(12, "BL", as5);
        SA.add(12, "NL", as5);
        SA.add(12, "TAB", as5);
        SA.add(12, 'c', as5);
        SA.add(12, '=', as5);
        SA.add(12, '>', as5);
        SA.add(12, '<', as5);
        SA.add(12, ',', as5);
        SA.add(12, '(', as5);
        SA.add(12, ')', as5);
        SA.add(12, ':', as5);
        SA.add(12, '"', as5);
        SA.add(12, '+', as5);
        SA.add(12, '-', as5);
        SA.add(12, '/', as5);
        SA.add(12, '*', as5);
        SA.add(12, '_', as5);
        SA.add(12, '"', as5);

        SA.add(13, '.', as5);
        SA.add(13, 'l', as5);
        SA.add(13, 'd', as5);
        SA.add(13, '_', as5);
        SA.add(13, "BL", as5);
        SA.add(13, "NL", as5);
        SA.add(13, "TAB", as5);
        SA.add(13, 'c', as5);
        SA.add(13, '=', as5);
        SA.add(13, '>', as5);
        SA.add(13, '<', as5);
        SA.add(13, ',', as5);
        SA.add(13, '(', as5);
        SA.add(13, ')', as5);
        SA.add(13, ':', as5);
        SA.add(13, '"', as5);
        SA.add(13, '+', as5);
        SA.add(13, '-', as5);
        SA.add(13, '/', as5);
        SA.add(13, '*', as5);
        SA.add(13, '_', as5);
        SA.add(13, '"', as5);

        SA.add(14, "NL", as4);

        SA.add(14, '.', as5);
        SA.add(14, 'l', as5);
        SA.add(14, 'd', as5);
        SA.add(14, '_', as5);
        SA.add(14, "BL", as5);
        SA.add(14, "TAB", as5);
        //SA.add(14, 'c', as5);
        SA.add(14, '=', as5);
        SA.add(14, '>', as5);
        SA.add(14, '<', as5);
        SA.add(14, ',', as5);
        SA.add(14, '(', as5);
        SA.add(14, ')', as5);
        SA.add(14, ':', as5);
        SA.add(14, '"', as5);
        SA.add(14, '+', as5);
        SA.add(14, '-', as5);
        SA.add(14, '/', as5);
        SA.add(14, '*', as5);
        SA.add(14, '_', as5);
        SA.add(14, '"', as5);
    }

    //</editor-fold>
}