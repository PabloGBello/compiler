package base;

public class Tools {

    /**
     *  traduce un caracter en un simbolo correspondiente
     *  (0,1,2,3,4,5,6,7,8,9)--> d(digitos)
     *  (a,A,b,B,c,C,...)--> l(letras)
     *  La letra (E) o (e) se trata aparte porque se utiliza como exponente en flotantes
     *  ...etc.
    */
    public static String translate(Character s){

        if(Character.isDigit(s))
            return "d";
        if(s == 'e' || s == 'E')
            return "E";
        else
        if((s >= 'a' && s <= 'z' ) || (s >= 'A' && s <= 'Z' ))
            return "l";
        if(s == ' ' )
            return "BL";
        if(s == '\n' ) {
            return "NL";
        }
        if(s == '\t' )
            return "TAB";
        if(s == '=' )
            return "=";
        if(s == '>' )
            return ">";
        if(s == '<' )
            return "<";
        if(s == ',' )
            return ",";
        if(s == '(' )
            return "(";
        if(s == ')' )
            return ")";
        if(s == ':' )
            return ":";
        if(s == '.' )
            return ".";
        if(s == '_' )
            return "_";
        if(s == '"' )
            return Character.toString(s);
        if(s == '+' )
            return "+";
        if(s == '-' )
            return "-";
        if(s == '/' )
            return "/";
        if(s == '*' )
            return "*";
        else
            return "c";
    }


}
