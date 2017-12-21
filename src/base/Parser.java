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
    6,    4,    4,    4,    4,    4,    7,    7,   10,   11,
   11,   13,   14,   12,   17,   17,   17,   17,   17,   17,
    8,    8,   18,   19,   20,    9,    9,   15,   15,   21,
   21,   16,   16,   16,   22,   24,   23,   23,   23,   25,
   25,   25,   25,
};
final static short yylen[] = {                            2,
    1,    1,    2,    1,    1,    4,    3,    1,    1,    1,
    1,    1,    1,    1,    5,    2,    5,    3,    1,    5,
    3,    1,    1,    3,    1,    1,    1,    1,    1,    1,
    2,    3,    1,    6,    1,    4,    4,    3,    3,    1,
    2,    3,    3,    1,    1,    1,    3,    3,    1,    1,
    1,    2,    4,
};
final static short yydefred[] = {                         0,
    0,    0,   33,    0,    0,    0,    0,    2,    4,    5,
    0,   12,   13,   14,    0,    0,    0,    0,    0,   16,
    0,    0,    3,    0,    0,    0,    0,    0,   31,   18,
    0,   50,   51,    0,    0,   19,    0,    0,    0,    0,
   49,    0,    0,    0,   11,   10,    9,    0,    7,   32,
    0,   40,    0,    0,    0,   52,    0,   26,   27,   25,
   28,   29,   30,    0,    0,    0,    0,    0,    0,   37,
   36,    6,   39,   38,   41,    0,    0,    0,   17,    0,
    0,    0,   47,   48,   15,   35,    0,   53,    0,   22,
    0,   21,    0,   34,    0,   23,   20,
};
final static short yydgoto[] = {                          6,
    7,    8,    9,   10,   11,   48,   12,   13,   14,   35,
   79,   36,   89,   95,   28,   37,   64,   15,   29,   87,
   53,   38,   39,   40,   41,
};
final static short yysindex[] = {                      -225,
  -37,    2,    0, -256,  -51,    0, -225,    0,    0,    0,
    8,    0,    0,    0, -187, -214,  -29, -212,  -51,    0,
  -29,  -29,    0, -197, -194,   37, -220, -181,    0,    0,
   45,    0,    0, -189,   46,    0,  -45,   18,    0,  -22,
    0,   47,   43,   44,    0,    0,    0,   48,    0,    0,
 -170,    0, -206,   52,  -29,    0, -165,    0,    0,    0,
    0,    0,    0,  -29,  -29,  -29,  -29,  -29,   49,    0,
    0,    0,    0,    0,    0,  -29,   55, -163,    0,    0,
    0,    0,    0,    0,    0,    0,   57,    0, -235,    0,
   53,    0, -163,    0, -159,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    9,    0,  101,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   32,    0,  -41,    0,
    0,    0,   32,   32,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   32,    0,    0,   31,
  -34,  -12,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0,   95,    0,  -13,    0,    0,    0,    0,   99,    0,
    0,   28,    0,    0,  -31,    1,    0,    0,    0,    0,
    0,    0,   13,    0,   14,
};
final static int YYTABLESIZE=261;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         44,
   46,   44,   17,   44,   44,   46,   42,   46,   42,   22,
   42,   42,   46,   52,   62,   34,   63,   19,   44,   67,
   44,   43,   44,   92,   68,   42,   93,   42,   43,   46,
   43,    1,   43,   43,   46,   51,    1,    2,    3,   75,
    4,   18,    2,    3,   30,    4,   90,   43,    5,   43,
    1,   25,    8,   19,   74,   77,    2,    3,   45,    4,
   65,   96,   66,   42,   80,   24,    8,   19,   26,   46,
   47,   24,   27,   45,   45,   45,   45,   81,   82,   49,
   83,   84,   50,   54,   55,   56,   57,   69,   70,   71,
   73,   76,   78,   72,   85,   88,   27,   91,   94,   97,
    1,   23,   20,   86,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   21,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   16,    0,
    0,    0,    0,    0,   58,   59,   60,   61,   44,   44,
   44,   44,    0,    0,    0,   42,   42,   42,   42,   31,
    0,    0,    0,    0,   32,   33,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   43,   43,   43,
   43,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
   42,   43,   40,   45,   46,   47,   41,   42,   43,   61,
   45,   46,   47,   27,   60,   45,   62,  274,   60,   42,
   62,   21,   22,  259,   47,   60,  262,   62,   41,   42,
   43,  257,   45,   46,   47,  256,  257,  263,  264,   53,
  266,   40,  263,  264,  259,  266,   78,   60,  274,   62,
  257,   44,   44,  274,  261,   55,  263,  264,  256,  266,
   43,   93,   45,  276,   64,   58,   58,  274,  256,  267,
  268,   41,  260,   43,   43,   45,   45,   65,   66,  274,
   67,   68,   46,  265,   40,  275,   41,   41,   46,   46,
  261,   40,  258,   46,   46,   41,  260,   41,   46,  259,
    0,    7,    4,   76,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  256,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  256,   -1,
   -1,   -1,   -1,   -1,  270,  271,  272,  273,  270,  271,
  272,  273,   -1,   -1,   -1,  270,  271,  272,  273,  269,
   -1,   -1,   -1,   -1,  274,  275,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  270,  271,  272,
  273,
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
"tipo : error",
"ejecutable : seleccion",
"ejecutable : iteracion",
"ejecutable : asignacion",
"ejecutable : OUT '(' CADENA ')' '.'",
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
"expresion : expresion_a '+' termino",
"expresion : expresion_a '-' termino",
"expresion : termino",
"expresion_a : expresion",
"termino_a : termino",
"termino : termino_a '*' factor",
"termino : termino_a '/' factor",
"termino : factor",
"factor : ID",
"factor : CTE",
"factor : '-' CTE",
"factor : I_F '(' expresion ')'",
};

//#line 290 "gramatica.y"

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

    if (!Constants.err){
        AssemblerGenerator ag = new AssemblerGenerator(parser.getLa().getSymbolTable(),args[0]);
        ag.setTercetos(parser.getTg().getTercetos());
        ag.generate();
     }
     else
        System.exit(1);
}

private int yylex(){
	int token = la.yylex();
    yylval = new ParserVal(la.val);
    return token;
}

private void yyerror(String msj){
}

private void yynotify(int type, String mensaje){
        if (type == 1)
            Constants.err = true;
        System.out.println(mensaje);
        String s = Printer.getMessage(1,type, la.values.getCurrentLine(), mensaje); //v.currentLine
        la.compilationOutput.write(s);

}

public Data addSymbol(Data field){ /*Agrega un numero negativo a la tabla*/
    SymbolTable tab = la.getSymbolTable();
    int ai;
    float af;
    String value;
    if(Integer.valueOf(field.getType()) == Constants.INT) {
        ai = Integer.parseInt(field.getLexema()) * (-1);
        value = String.valueOf(ai);
    } else {
        af = Float.parseFloat(field.getLexema()) * (-1.0f);
        value = String.valueOf(af);
    }
    Data aux = tab.getData(field.getCode(), value);
    if(aux != null && aux.getLexema().equals(value)) {
        return aux;
    }
    if (aux == null) {
        aux = new Data();
        aux.setLexema(String.valueOf(value));
        aux.setType(field.getType());
        aux.setCode(field.getCode());
        tab.checkType(aux);
        tab.getSimbolos().get(Constants.CTE).add(aux);
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
//#line 417 "Parser.java"
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
case 11:
//#line 105 "gramatica.y"
{   String msg = "El tipo " + ((Data)val_peek(0).obj).getLexema().toUpperCase()+ " no es valido. No se puede continuar. \\r\\n";
                                                                                String s = Printer.getMessage(2, 1, LexicalAnalizer.values.getCurrentLine(), msg);
                                                                                LexicalAnalizer.compilationOutput.write(s);
                                                                                System.exit(1);}
break;
case 15:
//#line 120 "gramatica.y"
{tg.tercetoOUT((Data)val_peek(2).obj);}
break;
case 16:
//#line 122 "gramatica.y"
{tg.tercetoLET();}
break;
case 17:
//#line 132 "gramatica.y"
{tg.tercetoDesapilar(0);
                                                                            tg.tercetoLabel();}
break;
case 18:
//#line 135 "gramatica.y"
{yynotify(1, "Estructura IF incorrecta");}
break;
case 19:
//#line 141 "gramatica.y"
{tg.tercetoIncompleto("BF");}
break;
case 20:
//#line 147 "gramatica.y"
{yynotify(2, "Estructura IF correcta.");}
break;
case 21:
//#line 149 "gramatica.y"
{yynotify(2, "Estructura IF correcta.");}
break;
case 22:
//#line 155 "gramatica.y"
{tg.tercetoDesapilar(1);

                                                                            tg.tercetoIncompleto("BI");
                                                                            tg.tercetoLabel();}
break;
case 24:
//#line 170 "gramatica.y"
{tg.createTerceto(((Data)val_peek(1).obj).getLexema(), (Data)val_peek(2).obj, (Data)val_peek(0).obj);}
break;
case 32:
//#line 197 "gramatica.y"
{yynotify(1, "Estructura UNTIL incorrecta.");}
break;
case 33:
//#line 203 "gramatica.y"
{tg.setIndexDO();
                                                                            tg.tercetoLabel();}
break;
case 34:
//#line 210 "gramatica.y"
{tg.tercetoIteration("BF");

                                                                            yynotify(2, "Estructura UNTIL correcta.");}
break;
case 36:
//#line 228 "gramatica.y"
{if(TercetoGenerator.AUXptr != null){
                                                                                tg.setAptr(tg.createTerceto("=", TercetoGenerator.AUXptr, tg.getEptr()));
                                                                                TercetoGenerator.AUXptr = null;
                                                                            }
                                                                            else
                                                                                tg.setAptr(tg.createTerceto("=", la.ST.getData(((Data)val_peek(3).obj).getCode(), ((Data)val_peek(3).obj).getLexema()), tg.getEptr()));}
break;
case 37:
//#line 237 "gramatica.y"
{yynotify(1, "Asignacion incorrecta.");}
break;
case 39:
//#line 245 "gramatica.y"
{yynotify(1, "Error en bloque.");}
break;
case 42:
//#line 257 "gramatica.y"
{tg.setEptr(tg.createTerceto("+", tg.pilaGramatica.remove(tg.pilaGramatica.size()-1), tg.getTptr()));}
break;
case 43:
//#line 259 "gramatica.y"
{tg.setEptr(tg.createTerceto("-", tg.pilaGramatica.remove(tg.pilaGramatica.size()-1), tg.getTptr()));}
break;
case 44:
//#line 261 "gramatica.y"
{tg.setEptr(tg.getTptr());}
break;
case 45:
//#line 265 "gramatica.y"
{tg.pilaGramatica.add(tg.getEptr());}
break;
case 46:
//#line 267 "gramatica.y"
{tg.pilaGramatica.add(tg.getTptr());}
break;
case 47:
//#line 270 "gramatica.y"
{tg.setTptr(tg.createTerceto("*", tg.pilaGramatica.remove(tg.pilaGramatica.size()-1), tg.getFptr()));}
break;
case 48:
//#line 272 "gramatica.y"
{tg.setTptr(tg.createTerceto("/", tg.pilaGramatica.remove(tg.pilaGramatica.size()-1), tg.getFptr()));}
break;
case 49:
//#line 274 "gramatica.y"
{tg.setTptr(tg.getFptr());}
break;
case 50:
//#line 278 "gramatica.y"
{tg.setFptr((Data)val_peek(0).obj);}
break;
case 51:
//#line 280 "gramatica.y"
{la.getSymbolTable().checkType((Data)val_peek(0).obj);
                                                                            tg.setFptr((Data)val_peek(0).obj);}
break;
case 52:
//#line 283 "gramatica.y"
{tg.setFptr(addSymbol((Data)val_peek(0).obj));}
break;
case 53:
//#line 285 "gramatica.y"
{tg.setFptr(tg.tercetoI_F((Data)val_peek(1).obj));}
break;
//#line 710 "Parser.java"
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
