%{

package base;

import base.SymbolTable;

import base.LexicalAnalizer;

import java.util.*;

%}



%token IF

%token THEN

%token END_IF

%token BEGIN

%token END

%token ELSE

%token OUT

%token DO

%token UNTIL

%token LET

%token FLOAT

%token INT

%token I_F

%token COMP_IGUAL_IGUAL

%token COMP_MAYOR_IGUAL

%token COMP_DISTINTO

%token COMP_MENOR_IGUAL

%token ID

%token CTE

%token CADENA

%token error



%start prog



%%

prog : programa                                                             {tg.showTercetos();}

;



programa : sentencia

         | programa sentencia

;



sentencia : declarativa

          | ejecutable

;



declarativa : decl ':' tipo'.'                                              {declarar(((Data)$3.obj).getLexema());}

;



decl : decl ',' ID                                                          {aDeclarar.add(((Data)$3.obj).getLexema());}

     | ID                                                                   {aDeclarar.add(((Data)$1.obj).getLexema());}

;



tipo : INT

     | FLOAT

;



ejecutable : seleccion

           | iteracion

           | asignacion

           | OUT '(' CADENA ')''.'                                          {tg.tercetoOUT((Data)$3.obj);}

           | LET asignacion                                                 {tg.tercetoLET();}

;



/*--IF--*/



seleccion : IF '(' cond_if ')' cpo_if                                       {tg.tercetoDesapilar(0);
                                                                            tg.tercetoLabel();}

          | IF error END_IF                                                 {yynotify(1, "Estructura IF incorrecta");}

;



cond_if : condicion 	                                                    {tg.tercetoIncompleto("BF");}

;



cpo_if : THEN cpo_then ELSE cpo_else END_IF                                 {yynotify(2, "Estructura IF correcta.");}

       | THEN cpo_then END_IF                                               {yynotify(2, "Estructura IF correcta.");}

;



cpo_then : bloque                                                           {tg.tercetoDesapilar(1);

                                                                            tg.tercetoIncompleto("BI");
                                                                            tg.tercetoLabel();}

;



cpo_else : bloque

;



condicion : expresion op expresion                                          {tg.createTerceto(((Data)$2.obj).getLexema(), (Data)$1.obj, (Data)$3.obj);}

;



op : COMP_DISTINTO

   | COMP_IGUAL_IGUAL

   | COMP_MAYOR_IGUAL

   | COMP_MENOR_IGUAL

   | '<'

   | '>'

;


/*--DO-UNTIL--*/



iteracion : DO_IND cpo_until

          | DO_IND error '.'                                                {yynotify(1, "Estructura UNTIL incorrecta.");}

;



DO_IND    : DO                                                              {tg.setIndexDO();
                                                                            tg.tercetoLabel();}

;



cpo_until : bloque UNTIL '(' cond_until ')''.'                              {tg.tercetoIteration("BF");

                                                                            yynotify(2, "Estructura UNTIL correcta.");}

;



cond_until : condicion

;



/*------------*/



asignacion : ID '=' expresion'.'                                            {if(TercetoGenerator.AUXptr != null){
                                                                                tg.setAptr(tg.createTerceto("=", TercetoGenerator.AUXptr, tg.getEptr()));
                                                                                TercetoGenerator.AUXptr = null;
                                                                            }
                                                                            else
                                                                                tg.setAptr(tg.createTerceto("=", la.ST.getData(((Data)$1.obj).getCode(), ((Data)$1.obj).getLexema()), tg.getEptr()));}



           | ID error expresion'.'                                          {yynotify(1, "Asignacion incorrecta.");}

;



bloque : BEGIN grupo_de_sentencias END

       | BEGIN error END                                                    {yynotify(1, "Error en bloque.");}

;



grupo_de_sentencias : ejecutable                                          

                    | grupo_de_sentencias ejecutable

;

expresion : expresion_a '+' termino                                         {tg.setEptr(tg.createTerceto("+", tg.pilaGramatica.remove(tg.pilaGramatica.size()-1), tg.getTptr()));}

            | expresion_a '-' termino                                       {tg.setEptr(tg.createTerceto("-", tg.pilaGramatica.remove(tg.pilaGramatica.size()-1), tg.getTptr()));}

            | termino                                                       {tg.setEptr(tg.getTptr());}

;

expresion_a : expresion                                                     {tg.pilaGramatica.add(tg.getEptr());}
;
termino_a : termino                                                         {tg.pilaGramatica.add(tg.getTptr());}
;

termino : termino_a '*' factor                                              {tg.setTptr(tg.createTerceto("*", tg.pilaGramatica.remove(tg.pilaGramatica.size()-1), tg.getFptr()));}

        | termino_a '/' factor                                              {tg.setTptr(tg.createTerceto("/", tg.pilaGramatica.remove(tg.pilaGramatica.size()-1), tg.getFptr()));}

        | factor                                                            {tg.setTptr(tg.getFptr());}

;

factor : ID                                                                 {tg.setFptr((Data)$1.obj);}

       | CTE                                                                {tg.setFptr((Data)$1.obj);}

       | '-'CTE                                                             {tg.setFptr(addSymbol((Data)$2.obj));}

       | I_F'('expresion')'                                                 {tg.setFptr(tg.tercetoI_F((Data)$3.obj));}

;

%%

LexicalAnalizer la;
TercetoGenerator tg;
ArrayList<String> aDeclarar = new ArrayList();

public Parser(String dir, String fileName) {

  la = new LexicalAnalizer(dir, fileName);
  tg = new TercetoGenerator(la.getSymbolTable());
}

public TercetoGenerator getTg(){
    return tg;
}

public static void main(String[] args) {
    Parser parser = new Parser(args[0], args[1]);
    int parsedValue = parser.yyparse();
    System.out.println(parsedValue);
    TercetoGenerator.finalCheck();
    parser.getLa().outputST();
    parser.getLa().compilationOutput.closeWriter();

    AssemblerGenerator ag = new AssemblerGenerator(parser.getLa().getSymbolTable(),args[0]);
    ag.setTercetos(parser.getTg().getTercetos());
    ag.generate();
}

private int yylex(){
	int token = la.yylex();
    yylval = new ParserVal(la.val);
    return token;
}

private void yyerror(String msj){
}

private void yynotify(int type, String mensaje){
        System.out.println(mensaje);
        String s = Printer.getMessage(1,type, la.values.getCurrentLine(), mensaje); //v.currentLine
        la.compilationOutput.write(s);

}

public Data addSymbol(Data field){ /*Agrega un numero negativo a la tabla*/
        SymbolTable tab = la.getSymbolTable();
        int value = Integer.parseInt(field.getLexema()) * (-1);
        Data aux = tab.getData(field.getCode(), field.getLexema());
        if(aux != null && aux.getLexema().equals(field.getLexema())) {
            aux = tab.getData(field.getCode(), String.valueOf(value));
            if (aux == null) {
                aux = new Data();
                aux.setLexema(String.valueOf(value));
                aux.setType(field.getType());
                aux.setCode(field.getCode());
                tab.addData(aux);
            }
        }
        return aux;
}

public void declarar(String type){
    SymbolTable tab = la.getSymbolTable();
    for (String s : aDeclarar){
        tab.setType(s,type);
    }
    aDeclarar.clear();
}



public LexicalAnalizer getLa() {
    return la;
}
