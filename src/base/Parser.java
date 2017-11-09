//### This file created by BYACC 1.8(/Java extension  1.15)
//### Java capabilities added 7 Jan 97, Bob Jamison
//### Updated : 27 Nov 97  -- Bob Jamison, Joe Nieten
//###           01 Jan 98  -- Bob Jamison -- fixed generic semantic constructor
//###           01 Jun 99  -- Bob Jamison -- added Runnable support
//###           06 Aug 00  -- Bob Jamison -- made state variables class-global
//###           03 Jan 01  -- Bob Jamison -- improved flags, tracing
//###           16 May 01  -- Bob Jamison -- added custom stack sizing
//###           04 Mar 02  -- Yuval Oren  -- improved java performance, added options
//###           14 Mar 02  -- Tomas Hurka -- -d support, static initializer workaround
//### Please send bug reports to tom@hukatronic.cz
//### static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";






//#line 2 "gramatica.y"

package base;

import base.SymbolTable;

import base.LexicalAnalizer;

import java.util.*;

//#line 27 "Parser.java"




public class Parser
{

boolean yydebug;        //do I want debug output?
int yynerrs;            //number of errors so far
int yyerrflag;          //was there an error?
int yychar;             //the current working character

//########## MESSAGES ##########
//###############################################################
// method: debug
//###############################################################
void debug(String msg)
{
  if (yydebug)
    System.out.println(msg);
}

//########## STATE STACK ##########
final static int YYSTACKSIZE = 500;  //maximum stack size
int statestk[] = new int[YYSTACKSIZE]; //state stack
int stateptr;
int stateptrmax;                     //highest index of stackptr
int statemax;                        //state when highest index reached
//###############################################################
// methods: state stack push,pop,drop,peek
//###############################################################
final void state_push(int state)
{
  try {
		stateptr++;
		statestk[stateptr]=state;
	 }
	 catch (ArrayIndexOutOfBoundsException e) {
     int oldsize = statestk.length;
     int newsize = oldsize * 2;
     int[] newstack = new int[newsize];
     System.arraycopy(statestk,0,newstack,0,oldsize);
     statestk = newstack;
     statestk[stateptr]=state;
  }
}
final int state_pop()
{
  return statestk[stateptr--];
}
final void state_drop(int cnt)
{
  stateptr -= cnt; 
}
final int state_peek(int relative)
{
  return statestk[stateptr-relative];
}
//###############################################################
// method: init_stacks : allocate and prepare stacks
//###############################################################
final boolean init_stacks()
{
  stateptr = -1;
  val_init();
  return true;
}
//###############################################################
// method: dump_stacks : show n levels of the stacks
//###############################################################
void dump_stacks(int count)
{
int i;
  System.out.println("=index==state====value=     s:"+stateptr+"  v:"+valptr);
  for (i=0;i<count;i++)
    System.out.println(" "+i+"    "+statestk[i]+"      "+valstk[i]);
  System.out.println("======================");
}


//########## SEMANTIC VALUES ##########
//public class ParserVal is defined in ParserVal.java


String   yytext;//user variable to return contextual strings
ParserVal yyval; //used to return semantic vals from action routines
ParserVal yylval;//the 'lval' (result) I got from yylex()
ParserVal valstk[];
int valptr;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
void val_init()
{
  valstk=new ParserVal[YYSTACKSIZE];
  yyval=new ParserVal();
  yylval=new ParserVal();
  valptr=-1;
}
void val_push(ParserVal val)
{
  if (valptr>=YYSTACKSIZE)
    return;
  valstk[++valptr]=val;
}
ParserVal val_pop()
{
  if (valptr<0)
    return new ParserVal();
  return valstk[valptr--];
}
void val_drop(int cnt)
{
int ptr;
  ptr=valptr-cnt;
  if (ptr<0)
    return;
  valptr = ptr;
}
ParserVal val_peek(int relative)
{
int ptr;
  ptr=valptr-relative;
  if (ptr<0)
    return new ParserVal();
  return valstk[ptr];
}
final ParserVal dup_yyval(ParserVal val)
{
  ParserVal dup = new ParserVal();
  dup.ival = val.ival;
  dup.dval = val.dval;
  dup.sval = val.sval;
  dup.obj = val.obj;
  return dup;
}
//#### end semantic value section ####
public final static short IF=257;
public final static short THEN=258;
public final static short END_IF=259;
public final static short BEGIN=260;
public final static short END=261;
public final static short ELSE=262;
public final static short OUT=263;
public final static short DO=264;
public final static short UNTIL=265;
public final static short LET=266;
public final static short FLOAT=267;
public final static short INT=268;
public final static short I_F=269;
public final static short COMP_IGUAL_IGUAL=270;
public final static short COMP_MAYOR_IGUAL=271;
public final static short COMP_DISTINTO=272;
public final static short COMP_MENOR_IGUAL=273;
public final static short ID=274;
public final static short CTE=275;
public final static short CADENA=276;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    1,    1,    2,    2,    3,    5,    5,    6,    6,
    4,    4,    4,    4,    4,    7,    7,   10,   11,   11,
   13,   14,   12,   17,   17,   17,   17,   17,   17,    8,
    8,   18,   19,   20,    9,    9,   15,   15,   21,   21,
   16,   16,   22,   22,   22,   23,   23,   23,   24,   24,
   24,
};
final static short yylen[] = {                            2,
    1,    1,    2,    1,    1,    4,    3,    1,    1,    1,
    1,    1,    1,    4,    2,    5,    3,    1,    5,    3,
    1,    1,    3,    1,    1,    1,    1,    1,    1,    2,
    3,    1,    6,    1,    4,    4,    3,    3,    1,    2,
    2,    1,    3,    3,    1,    3,    3,    1,    1,    1,
    2,
};
final static short yydefred[] = {                         0,
    0,    0,   32,    0,    0,    0,    0,    2,    4,    5,
    0,   11,   12,   13,    0,    0,    0,    0,    0,   15,
    0,    0,    3,    0,    0,    0,    0,    0,   30,   17,
    0,   49,   50,    0,    0,   18,    0,    0,    0,   48,
    0,    0,    0,   10,    9,    0,    7,   31,    0,   39,
    0,    0,    0,   51,    0,   25,   26,   24,   27,   28,
   29,    0,    0,    0,    0,    0,   14,   36,   35,    6,
   38,   37,   40,    0,    0,   16,   23,    0,    0,   46,
   47,   34,    0,    0,   21,    0,   20,    0,   33,    0,
   22,   19,
};
final static short yydgoto[] = {                          6,
    7,    8,    9,   10,   11,   46,   12,   13,   14,   35,
   76,   36,   84,   90,   28,   37,   62,   15,   29,   83,
   51,   38,   39,   40,
};
final static short yysindex[] = {                      -192,
  -39,  -33,    0, -248,  -58,    0, -192,    0,    0,    0,
  -16,    0,    0,    0, -205, -221,  -11, -224,  -58,    0,
  -11,  -11,    0, -190, -218,   17, -216, -196,    0,    0,
  -15,    0,    0, -208,   32,    0,  -47,   23,    7,    0,
   34,   35,   39,    0,    0,   40,    0,    0, -174,    0,
 -204,   48,   23,    0, -169,    0,    0,    0,    0,    0,
    0,  -11,  -15,  -15,  -15,  -15,    0,    0,    0,    0,
    0,    0,    0,  -11, -170,    0,    0,    7,    7,    0,
    0,    0,   50, -198,    0,   46,    0, -170,    0, -166,
    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,  -12,    0,   94,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  -23,  -41,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  -17,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  -35,  -29,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,
};
final static short yygindex[] = {                         0,
    0,   88,    0,   -7,    0,    0,    0,    0,   92,    0,
    0,   24,    0,    0,  -66,   14,    0,    0,    0,    0,
    0,   66,   16,   18,
};
final static int YYTABLESIZE=264;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         45,
   17,   45,   22,   45,   45,   43,   18,   43,   85,   43,
   43,   44,   60,   44,   61,   44,   44,   42,   45,   50,
   45,   91,   42,   41,   43,   19,   43,   25,   41,   34,
   44,    8,   44,   34,   42,   43,   42,   30,   42,   49,
    1,   24,   41,   73,   41,    8,    2,    3,   65,    4,
   26,   41,    1,   66,   27,   47,   72,   19,    2,    3,
   87,    4,   48,   88,    1,   63,   54,   64,   52,   19,
    2,    3,   55,    4,   67,   77,   44,   45,   78,   79,
   68,    5,   80,   81,   69,   70,   71,   74,   75,   27,
   86,   89,   92,    1,   23,   20,   53,   82,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   21,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   16,    0,    0,    0,
    0,    0,   56,   57,   58,   59,    0,    0,   45,   45,
   45,   45,    0,    0,   43,   43,   43,   43,    0,    0,
   44,   44,   44,   44,    0,    0,   42,   42,   42,   42,
    0,    0,   41,   41,   41,   41,    0,   31,   32,   33,
    0,    0,   32,   33,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
   40,   43,   61,   45,   46,   41,   40,   43,   75,   45,
   46,   41,   60,   43,   62,   45,   46,   41,   60,   27,
   62,   88,   46,   41,   60,  274,   62,   44,   46,   45,
   60,   44,   62,   45,   21,   22,   60,  259,   62,  256,
  257,   58,   60,   51,   62,   58,  263,  264,   42,  266,
  256,  276,  257,   47,  260,  274,  261,  274,  263,  264,
  259,  266,   46,  262,  257,   43,  275,   45,  265,  274,
  263,  264,   41,  266,   41,   62,  267,  268,   63,   64,
   46,  274,   65,   66,   46,   46,  261,   40,  258,  260,
   41,   46,  259,    0,    7,    4,   31,   74,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  256,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  256,   -1,   -1,   -1,
   -1,   -1,  270,  271,  272,  273,   -1,   -1,  270,  271,
  272,  273,   -1,   -1,  270,  271,  272,  273,   -1,   -1,
  270,  271,  272,  273,   -1,   -1,  270,  271,  272,  273,
   -1,   -1,  270,  271,  272,  273,   -1,  269,  274,  275,
   -1,   -1,  274,  275,
};
}
final static short YYFINAL=6;
final static short YYMAXTOKEN=276;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,"'('","')'","'*'","'+'","','",
"'-'","'.'","'/'",null,null,null,null,null,null,null,null,null,null,"':'",null,
"'<'","'='","'>'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,"IF","THEN","END_IF","BEGIN","END","ELSE","OUT",
"DO","UNTIL","LET","FLOAT","INT","I_F","COMP_IGUAL_IGUAL","COMP_MAYOR_IGUAL",
"COMP_DISTINTO","COMP_MENOR_IGUAL","ID","CTE","CADENA",
};
final static String yyrule[] = {
"$accept : prog",
"prog : programa",
"programa : sentencia",
"programa : programa sentencia",
"sentencia : declarativa",
"sentencia : ejecutable",
"declarativa : decl ':' tipo '.'",
"decl : decl ',' ID",
"decl : ID",
"tipo : INT",
"tipo : FLOAT",
"ejecutable : seleccion",
"ejecutable : iteracion",
"ejecutable : asignacion",
"ejecutable : OUT '(' CADENA ')'",
"ejecutable : LET asignacion",
"seleccion : IF '(' cond_if ')' cpo_if",
"seleccion : IF error END_IF",
"cond_if : condicion",
"cpo_if : THEN cpo_then ELSE cpo_else END_IF",
"cpo_if : THEN cpo_then END_IF",
"cpo_then : bloque",
"cpo_else : bloque",
"condicion : expresion op expresion",
"op : COMP_DISTINTO",
"op : COMP_IGUAL_IGUAL",
"op : COMP_MAYOR_IGUAL",
"op : COMP_MENOR_IGUAL",
"op : '<'",
"op : '>'",
"iteracion : DO_IND cpo_until",
"iteracion : DO_IND error '.'",
"DO_IND : DO",
"cpo_until : bloque UNTIL '(' cond_until ')' '.'",
"cond_until : condicion",
"asignacion : ID '=' expresion '.'",
"asignacion : ID error expresion '.'",
"bloque : BEGIN grupo_de_sentencias END",
"bloque : BEGIN error END",
"grupo_de_sentencias : ejecutable",
"grupo_de_sentencias : grupo_de_sentencias ejecutable",
"expresion : I_F expresion_s",
"expresion : expresion_s",
"expresion_s : expresion_s '+' termino",
"expresion_s : expresion_s '-' termino",
"expresion_s : termino",
"termino : termino '*' factor",
"termino : termino '/' factor",
"termino : factor",
"factor : ID",
"factor : CTE",
"factor : '-' CTE",
};

//#line 291 "gramatica.y"



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
    LexicalAnalizer.detectedTokens.closeWriter();

    // Generacion de codigo assembler
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

//#line 390 "Parser.java"
//###############################################################
// method: yylexdebug : check lexer state
//###############################################################
void yylexdebug(int state,int ch)
{
String s=null;
  if (ch < 0) ch=0;
  if (ch <= YYMAXTOKEN) //check index bounds
     s = yyname[ch];    //now get it
  if (s==null)
    s = "illegal-symbol";
  debug("state "+state+", reading "+ch+" ("+s+")");
}





//The following are now global, to aid in error reporting
int yyn;       //next next thing to do
int yym;       //
int yystate;   //current parsing state from state table
String yys;    //current token string


//###############################################################
// method: yyparse : parse input and execute indicated items
//###############################################################
int yyparse()
{
boolean doaction;
  init_stacks();
  yynerrs = 0;
  yyerrflag = 0;
  yychar = -1;          //impossible char forces a read
  yystate=0;            //initial state
  state_push(yystate);  //save it
  val_push(yylval);     //save empty value
  while (true) //until parsing is done, either correctly, or w/error
    {
    doaction=true;
    if (yydebug) debug("loop"); 
    //#### NEXT ACTION (from reduction table)
    for (yyn=yydefred[yystate];yyn==0;yyn=yydefred[yystate])
      {
      if (yydebug) debug("yyn:"+yyn+"  state:"+yystate+"  yychar:"+yychar);
      if (yychar < 0)      //we want a char?
        {
        yychar = yylex();  //get next token
        if (yydebug) debug(" next yychar:"+yychar);
        //#### ERROR CHECK ####
        if (yychar < 0)    //it it didn't work/error
          {
          yychar = 0;      //change it to default string (no -1!)
          if (yydebug)
            yylexdebug(yystate,yychar);
          }
        }//yychar<0
      yyn = yysindex[yystate];  //get amount to shift by (shift index)
      if ((yyn != 0) && (yyn += yychar) >= 0 &&
          yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
        {
        if (yydebug)
          debug("state "+yystate+", shifting to state "+yytable[yyn]);
        //#### NEXT STATE ####
        yystate = yytable[yyn];//we are in a new state
        state_push(yystate);   //save it
        val_push(yylval);      //push our lval as the input for next rule
        yychar = -1;           //since we have 'eaten' a token, say we need another
        if (yyerrflag > 0)     //have we recovered an error?
           --yyerrflag;        //give ourselves credit
        doaction=false;        //but don't process yet
        break;   //quit the yyn=0 loop
        }

    yyn = yyrindex[yystate];  //reduce
    if ((yyn !=0 ) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
      {   //we reduced!
      if (yydebug) debug("reduce");
      yyn = yytable[yyn];
      doaction=true; //get ready to execute
      break;         //drop down to actions
      }
    else //ERROR RECOVERY
      {
      if (yyerrflag==0)
        {
        yyerror("syntax error");
        yynerrs++;
        }
      if (yyerrflag < 3) //low error count?
        {
        yyerrflag = 3;
        while (true)   //do until break
          {
          if (stateptr<0)   //check for under & overflow here
            {
            yyerror("stack underflow. aborting...");  //note lower case 's'
            return 1;
            }
          yyn = yysindex[state_peek(0)];
          if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE)
            {
            if (yydebug)
              debug("state "+state_peek(0)+", error recovery shifting to state "+yytable[yyn]+" ");
            yystate = yytable[yyn];
            state_push(yystate);
            val_push(yylval);
            doaction=false;
            break;
            }
          else
            {
            if (yydebug)
              debug("error recovery discarding state "+state_peek(0)+" ");
            if (stateptr<0)   //check for under & overflow here
              {
              yyerror("Stack underflow. aborting...");  //capital 'S'
              return 1;
              }
            state_pop();
            val_pop();
            }
          }
        }
      else            //discard this token
        {
        if (yychar == 0)
          return 1; //yyabort
        if (yydebug)
          {
          yys = null;
          if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
          if (yys == null) yys = "illegal-symbol";
          debug("state "+yystate+", error recovery discards token "+yychar+" ("+yys+")");
          }
        yychar = -1;  //read another
        }
      }//end error recovery
    }//yyn=0 loop
    if (!doaction)   //any reason not to proceed?
      continue;      //skip action
    yym = yylen[yyn];          //get count of terminals on rhs
    if (yydebug)
      debug("state "+yystate+", reducing "+yym+" by rule "+yyn+" ("+yyrule[yyn]+")");
    if (yym>0)                 //if count of rhs not 'nil'
      yyval = val_peek(yym-1); //get current semantic value
    yyval = dup_yyval(yyval); //duplicate yyval if ParserVal is used as semantic value
    switch(yyn)
      {
//########## USER-SUPPLIED ACTIONS ##########
case 1:
//#line 65 "gramatica.y"
{tg.showTercetos();}
break;
case 6:
//#line 87 "gramatica.y"
{declarar(((Data)val_peek(1).obj).getLexema());}
break;
case 7:
//#line 93 "gramatica.y"
{aDeclarar.add(((Data)val_peek(0).obj).getLexema());}
break;
case 8:
//#line 95 "gramatica.y"
{aDeclarar.add(((Data)val_peek(0).obj).getLexema());}
break;
case 16:
//#line 127 "gramatica.y"
{tg.tercetoDesapilar(0);
                                                                        tg.tercetoLabel();}
break;
case 17:
//#line 130 "gramatica.y"
{yyerror("Estructura IF incorrecta");}
break;
case 18:
//#line 136 "gramatica.y"
{tg.tercetoIncompleto("BF");}
break;
case 19:
//#line 142 "gramatica.y"
{yyerror("Estructura IF correcta.");}
break;
case 20:
//#line 144 "gramatica.y"
{yyerror("Estructura IF correcta.");}
break;
case 21:
//#line 150 "gramatica.y"
{tg.tercetoDesapilar(1);

                                                                        tg.tercetoIncompleto("BI");
                                                                        tg.tercetoLabel();}
break;
case 23:
//#line 165 "gramatica.y"
{tg.createTerceto(((Data)val_peek(1).obj).getLexema(), (Data)val_peek(2).obj, (Data)val_peek(0).obj);}
break;
case 31:
//#line 193 "gramatica.y"
{yyerror("Estructura UNTIL incorrecta.");}
break;
case 32:
//#line 199 "gramatica.y"
{tg.setIndexDO();
                                                                          tg.tercetoLabel();}
break;
case 33:
//#line 206 "gramatica.y"
{tg.tercetoIteration("BF");

                                                                          yyerror("Estructura UNTIL correcta.");}
break;
case 35:
//#line 224 "gramatica.y"
{tg.setAptr(tg.createTerceto("=", (Data)val_peek(3).obj, tg.getEptr()));}
break;
case 36:
//#line 228 "gramatica.y"
{yyerror("Asignacion incorrecta.");}
break;
case 38:
//#line 236 "gramatica.y"
{yyerror("Error en bloque.");}
break;
case 43:
//#line 258 "gramatica.y"
{tg.setEptr(tg.createTerceto("+", tg.getEptr(), tg.getTptr()));}
break;
case 44:
//#line 260 "gramatica.y"
{tg.setEptr(tg.createTerceto("-", tg.getEptr(), tg.getTptr()));}
break;
case 45:
//#line 262 "gramatica.y"
{tg.setEptr(tg.getTptr());}
break;
case 46:
//#line 268 "gramatica.y"
{tg.setTptr(tg.createTerceto("*", tg.getTptr(), tg.getFptr()));}
break;
case 47:
//#line 270 "gramatica.y"
{tg.setTptr(tg.createTerceto("/", tg.getTptr(), tg.getFptr()));}
break;
case 48:
//#line 272 "gramatica.y"
{tg.setTptr(tg.getFptr());}
break;
case 49:
//#line 278 "gramatica.y"
{tg.setFptr(tg.lastDeclaration((Data)val_peek(0).obj));}
break;
case 50:
//#line 280 "gramatica.y"
{tg.setFptr((Data)val_peek(0).obj);}
break;
case 51:
//#line 282 "gramatica.y"
{addSymbol((Data)val_peek(0).obj);

                                                                          tg.setFptr((Data)val_peek(0).obj);}
break;
//#line 652 "Parser.java"
//########## END OF USER-SUPPLIED ACTIONS ##########
    }//switch
    //#### Now let's reduce... ####
    if (yydebug) debug("reduce");
    state_drop(yym);             //we just reduced yylen states
    yystate = state_peek(0);     //get new state
    val_drop(yym);               //corresponding value drop
    yym = yylhs[yyn];            //select next TERMINAL(on lhs)
    if (yystate == 0 && yym == 0)//done? 'rest' state and at first TERMINAL
      {
      if (yydebug) debug("After reduction, shifting from state 0 to state "+YYFINAL+"");
      yystate = YYFINAL;         //explicitly say we're done
      state_push(YYFINAL);       //and save it
      val_push(yyval);           //also save the semantic value of parsing
      if (yychar < 0)            //we want another character?
        {
        yychar = yylex();        //get next character
        if (yychar<0) yychar=0;  //clean, if necessary
        if (yydebug)
          yylexdebug(yystate,yychar);
        }
      if (yychar == 0)          //Good exit (if lex returns 0 ;-)
         break;                 //quit the loop--all DONE
      }//if yystate
    else                        //else not done yet
      {                         //get next state and push, for next yydefred[]
      yyn = yygindex[yym];      //find out where to go
      if ((yyn != 0) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
        yystate = yytable[yyn]; //get new state
      else
        yystate = yydgoto[yym]; //else go to new defred
      if (yydebug) debug("after reduction, shifting from state "+state_peek(0)+" to state "+yystate+"");
      state_push(yystate);     //going again, so push state & val...
      val_push(yyval);         //for next action
      }
    }//main loop
  return 0;//yyaccept!!
}
//## end of method parse() ######################################



//## run() --- for Thread #######################################
/**
 * A default run method, used for operating this parser
 * object in the background.  It is intended for extending Thread
 * or implementing Runnable.  Turn off with -Jnorun .
 */
public void run()
{
  yyparse();
}
//## end of method run() ########################################



//## Constructors ###############################################
/**
 * Default constructor.  Turn off with -Jnoconstruct .

 */
public Parser()
{
  //nothing to do
}


/**
 * Create a parser, setting the debug to true or false.
 * @param debugMe true for debugging, false for no debug.
 */
public Parser(boolean debugMe)
{
  yydebug=debugMe;
}
//###############################################################



}
//################### END OF CLASS ##############################
