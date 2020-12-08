
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Isaias
 */
public class Lexico {
    
    int i;
    ArrayList<String> tokens;
    static ArrayList<Integer> lineas;
    int[] tabla;
    static ArrayList<String[]> tablaSimbolos;
    boolean cad=false;
    String error;
    
    public void genararTokens(String cadena){
        tablaSimbolos=null;
        int linea=1;
        lineas=new ArrayList<Integer>();
        tokens=new ArrayList<String>();
        String token="";
        char c;
        for (int i=0; i<cadena.length(); i++) {
            c=cadena.charAt(i);
            if(c!='\n' && c!='\r' && c!='\t' && c!=' '){
                if(c!='(' && c!=')' && c!=';' && c!='{' && c!='}' && c!='+' && c!='-' && c!='*' && c!='/' && c!='%' && c!='=' && c!='!' && c!='<' && c!='>'&& c!='"'){
                    token=token+c;
                }else{
                    if(!token.equals("")){
                        lineas.add(linea);
                        tokens.add(token);
                        token="";
                    }
                    lineas.add(linea);
                    tokens.add(c+"");
                }
            }else {
                if(!token.equals("")){
                    lineas.add(linea);
                    tokens.add(token);
                    token="";
                }
                if(c=='\n')
                    linea++;
            }
        }
        if(!token.equals("")){
            lineas.add(linea);
            tokens.add(token);
            token="";
        }
    }
    public boolean generarTablaTokens(){
        i=0;
        tabla=new int[tokens.size()];
        String t;
        for (int j=0; i<tokens.size() && j<tabla.length; j++) {
            t=getNextToken();
            if(cad && !t.equals("\"")){//Cadena
                if(tabla[j-1]==15)
                    j--;
                    tabla[j]=15;
            }
            else//No es cadena
            switch(t){
                case "(":
                    tabla[j]=1;
                    break;
                case ")":
                    tabla[j]=20;
                    break;
                case "{":
                    tabla[j]=2;
                    break;
                case "}":
                    tabla[j]=3;
                    break;
                case "funtion":
                    tabla[j]=4;
                    break;
                case "AND":
                    tabla[j]=5;
                    break;
                case "OR":
                    tabla[j]=5;
                    break;
                case "int":
                    tabla[j]=7;
                    break;
                case "float":
                    tabla[j]=7;
                    break;
                case "string":
                    tabla[j]=7;
                    break;
                case "boolean":
                    tabla[j]=7;
                    break;
                case "true":
                case "false":
                    tabla[j]=21;
                    break;
                case "=":
                    if(tabla[j-1]==8){//==
                        tabla[j-1]=9;
                        j--;
                    }
                    else
                        tabla[j]=8;
                    break;
                case "<":
                    tabla[j]=9;
                    if(!getNextToken().equals("="))
                        i--;
                    break;
                case ">":
                    tabla[j]=9;
                    if(!getNextToken().equals("="))
                        i--;
                    break;
                case "!":
                    tabla[j]=9;
                    if(!getNextToken().equals("=")){
                        tabla[j]=-1;
                        i--;
                        error="Simbolo '!' mal Empleado";
                        return false;
                    }
                    break;
                case "+":
                    tabla[j]=10;
                    break;
                case "-":
                    tabla[j]=10;
                    break;
                case "/":
                    tabla[j]=10;
                    break;
                case "*":
                    tabla[j]=10;
                    break;
                case "%":
                    tabla[j]=10;
                    break;
                case "\"":
                    tabla[j]=11;
                    cad=!cad;
                    break;
                case ";":
                    tabla[j]=12;
                    break;
                case "if":
                    tabla[j]=16;
                    break;
                case "while":
                    tabla[j]=17;
                    break;
                case "out":
                    tabla[j]=6;
                    break;
                case "main":
                    tabla[j]=19;
                    break;
                case "in":
                    tabla[j]=25;
                    break;
                default:
                    try{
                        int a=Integer.parseInt(t);//Entero
                        tabla[j]=13;
                    }catch(Exception e){
                        try{
                            float b=Float.parseFloat(t);//real
                            tabla[j]=14;
                        }catch(Exception f){
                            int c=t.charAt(0);
                            if((c>64 && c<91) || (c>96 && c<123) || (c=='$' || c=='_')){
                                if(tabla[j-1]==7 || tabla[j-1]==4){
                                    tabla[j]=18;
                                    if(!agregarSimbolo(t, tokens.get(i-2))){
                                        tabla[j]=-1;
                                        error="Linea "+lineas.get(i)+": id ya definido";
                                        return false;
                                    }  
                                }else{
                                    if(existeSimbolo(t))
                                        tabla[j]=18;
                                    else{
                                        tabla[j]=-1;
                                        error="Linea "+lineas.get(i)+": Variable o identificador no Definido";
                                        return false;
                                    }
                                }
                            }else{
                                tabla[j]=-1;
                                error="Linea "+lineas.get(i)+":\nIdentificador mal Escrito...\n\tDebe de Empesar con:\n\n\ta-Z o A-Z o $ o _";
                                return false;
                            }
                        }
                    }
            }
        }
        System.out.println("\nLexico Correcto");
        System.out.println("Lineas:"+lineas.size()+" // Tokens:"+tokens.size());
        return true;
    }
    public boolean agregarSimbolo(String id, String type){
        String simb[]=new String[2];
        if(tablaSimbolos==null)
            tablaSimbolos=new ArrayList<String[]>();
        if(existeSimbolo(id)){
            return false;
        }
        simb[0]=id;
        simb[1]=type;
        tablaSimbolos.add(simb);
        return true;
    }
    public boolean existeSimbolo(String id){
        if(tablaSimbolos!=null)
        for (int i=0; i< tablaSimbolos.size(); i++) {
            if(id.equals(tablaSimbolos.get(i)[0]))
                return true;
        }
        error="Linea "+lineas.get(i)+": ID no Definido";
        return false;
    }
    public int[] getTokens(){
        if(tabla!=null)
            return tabla;
        return null;
    }
    public String getNextToken(){
        String tok=tokens.get(i);
        i++;
        return tok;
    }
    public void mostrar(){
        for (int i=0; i<tokens.size(); i++) {
            System.out.println(tokens.get(i));
        }
    }
    public void mostrarTabla(){
        System.out.println("\n");
        for (int i=0; i<tabla.length; i++) {
            System.out.print(tabla[i]+",");
        }
        System.out.println("\n");
    }
    public String getError(){
        return error;
    }
}
