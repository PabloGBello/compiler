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

prog : programa                                                   {tg.showTercetos();}

;



programa : sentencia

         | programa sentencia

;



sentencia : declarativa

          | ejecutable

;



declarativa : decl ':' tipo'.'                                  {declarar(((Data)$3.obj).getLexema());}

;



decl : decl ',' ID                                              {aDeclarar.add(((Data)$3.obj).getLexema());}

     | ID                                                       {aDeclarar.add(((Data)$1.obj).getLexema());}

;



tipo : INT

     | FLOAT

;



ejecutable : seleccion

           | iteracion

           | asignacion

           | OUT '(' CADENA ')'

           | LET asignacion

;



/*--IF--*/



seleccion : IF '(' cond_if ')' cpo_if                                   {tg.tercetoDesapilar(0);
                                                                        tg.tercetoLabel();}

          | IF error END_IF                                             {yyerror("Estructura IF incorrecta");}

;



cond_if : condicion 	                                                  {tg.tercetoIncompleto("BF");}

;



cpo_if : THEN cpo_then ELSE cpo_else END_IF                             {yyerror("Estructura IF correcta.");}

       | THEN cpo_then END_IF                                           {yyerror("Estructura IF correcta.");}

;



cpo_then : bloque                                                       {tg.tercetoDesapilar(1);

                                                                        tg.tercetoIncompleto("BI");
                                                                        tg.tercetoLabel();}

;



cpo_else : bloque

;



condicion : expresion op expresion                                       {tg.createTerceto(((Data)$2.obj).getLexema(), (Data)$1.obj, (Data)$3.obj);}

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

          | DO_IND error '.'                                                 {yyerror("Estructura UNTIL incorrecta.");}

;



DO_IND    : DO                                                            {tg.setIndexDO();
                                                                          tg.tercetoLabel();}

;



cpo_until : bloque UNTIL '(' cond_until ')''.'                           {tg.tercetoIteration("BF");

                                                                          yyerror("Estructura UNTIL correcta.");}

;



cond_until : condicion

;



/*------------*/



asignacion : ID '=' expresion'.'                                          {tg.setAptr(tg.createTerceto("=", (Data)$1.obj, tg.getEptr()));}



           | ID error expresion'.'                                        {yyerror("Asignacion incorrecta.");}

;



bloque : BEGIN grupo_de_sentencias END

       | BEGIN error END                                                  {yyerror("Error en bloque.");}

;



grupo_de_sentencias : ejecutable                                          

                    | grupo_de_sentencias ejecutable

;



expresion : I_F expresion_s

          | expresion_s

;                                                 



expresion_s : expresion_s '+' termino                                     {tg.setEptr(tg.createTerceto("+", tg.getEptr(), tg.getTptr()));}

            | expresion_s '-' termino                                     {tg.setEptr(tg.createTerceto("-", tg.getEptr(), tg.getTptr()));}

            | termino                                                     {tg.setEptr(tg.getTptr());}

;



termino : termino '*' factor                                              {tg.setTptr(tg.createTerceto("*", tg.getTptr(), tg.getFptr()));}

        | termino '/' factor                                              {tg.setTptr(tg.createTerceto("/", tg.getTptr(), tg.getFptr()));}

        | factor                                                          {tg.setTptr(tg.getFptr());}

;



factor : ID                                                               {tg.setFptr(tg.lastDeclaration((Data)$1.obj));}

       | CTE                                                              {tg.setFptr((Data)$1.obj);}

       | '-'CTE                                                           {addSymbol((Data)$2.obj);

                                                                          tg.setFptr((Data)$2.obj);}

;



%%



LexicalAnalizer la;
TercetoGenerator tg;
ArrayList<String> aDeclarar = new ArrayList();

public Parser(String dir) {

  la = new LexicalAnalizer(dir);
  tg = new TercetoGenerator(la.getSymbolTable());
}

public TercetoGenerator getTg(){
    return tg;
}

public static void main(String[] args) {
    Parser parser = new Parser(args[0]);
    int parsedValue = parser.yyparse();
    System.out.println(parsedValue);

    parser.getLa().outputST();
    parser.getLa().getCompilationOutput().closeWriter();

    AssemblerGenerator ag = new AssemblerGenerator(parser.getLa().getSymbolTable(),args[0]);
    ag.setTercetos(parser.getTg().getTercetos());
    ag.generate();
}

private int yylex(){
	int token = la.yylex();
    yylval = new ParserVal(la.val);
    return token;
}

private void yyerror(String mensaje){
    if(!mensaje.contains("syntax error")){
        System.out.println(mensaje);
        String s = Printer.getMessage(1,1, la.getValues().getCurrentLine(), mensaje); //v.currentLine
        la.getCompilationOutput().write(s);
    }
}

public void addSymbol(Data field){ /*Agrega un numero negativo a la tabla*/
        SymbolTable tab = la.getSymbolTable();
        int value = Integer.parseInt(field.getLexema()) * (-1);
        field.setLexema(String.valueOf(value));
        tab.addSymbol(Integer.valueOf(CTE), String.valueOf(value));
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

