package base;

/**Estructura para tercetos.
 *
 *         (op, field1, field2) -> (+,x,8)
 *  operator: +, -, /, *, =, conver. de tipo
 *  field1 y field2: Identificadores y constantes
 *  tipo: tipo de dato del terceto (todos los fields deben tener el mismo tipo).
 **/
public class Terceto {
    int index;
    String type;
    Data field1, field2, operator;
    public Terceto() {
        index = -1;
        operator = null;
        this.field1 = null;
        this.field2 = null;
        this.type = null;
    }

    public Terceto(int indexTerceto, Data op, Data field1, Data field2, String type) {
        index = indexTerceto;
        operator = op;
        this.field1 = field1;
        this.field2 = field2;
        this.type = type;
    }
    public int getIndex(){
        return index;
    }
    public void setIndex(int indexTerceto){
        index = indexTerceto;
    }
    public Data getOperator() {
        return operator;
    }

    public void setOperator(Data operator) {
        this.operator = operator;
    }

    public Data getField1() {
        return field1;
    }

    public void setField1(Data field1) {
        this.field1 = field1;
    }

    public Data getField2() {
        return field2;
    }

    public void setField2(Data field2) {
        this.field2 = field2;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    public String toString(){
        String aux = "" + index+"-(";
        if(operator != null)
            aux = aux + operator.getLexema() + ",";
        else
            aux += "  ,";
        if(field1 != null)
            aux = aux + field1.getLexema() + ",";
        else
            aux += "  ,";
        if(field2 != null)
            aux = aux + field2.getLexema() + ")";
        else
            aux += "  )";
        return aux + "- type: "+type;
    }

}
