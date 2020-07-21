
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Isaias
 */
public class Sintactico {
    
    Lexico lexico;
    int[] tokens;
    int i;
    String error;
    boolean main;
    
    public Sintactico(String cadena){
        lexico=new Lexico();
        lexico.genararTokens(cadena);
        lexico.mostrar();
        main=false;
    }
    public boolean start(){
        if(lexico.generarTablaTokens()){
            tokens=lexico.getTokens();
            System.out.println(lexico.i);
            lexico.mostrarTabla();
            if(programa())
                return true;
            else
                return false;
        }else{
            System.out.println(lexico.i);
            lexico.mostrarTabla();
            error=lexico.getError();
            return false;
        }
    }
    public String getError(){
        return error;
    }
    public boolean programa(){
        if(nextToken()==1){
            if(nextToken()==2){
                while(i<tokens.length){
                    switch(nextToken()){
                        case 4:
                            if(!metodo())
                                return false;
                            break;
                        case 7:
                            if(!declaracion())
                                return false;
                            break;
                        case 19:
                            if(!mn())
                                return false;
                            break;
                        case 3:
                            if(i<tokens.length)
                            if(nextToken()==20 && main)
                                return true;
                            if(!main)
                                error="Falta main... ";
                            else
                                error="Falta 'Fin'";
                            return false;
                        default:
                            error="Linea "+Lexico.lineas.get(i-2)+": Comando no Reconocido";
                            return false;
                    }
                }
            }
        }
        error="Falta '}Fin'";
        return false;
    }
    public boolean declaracion(){
        if(nextToken()==18){
            if(nextToken()==12)
                return true;
            else
                error="Linea "+Lexico.lineas.get(i-2)+": Falta ';'";
        }else
            error="Linea "+Lexico.lineas.get(i-2)+": Id no Colocado;";
        return false;
    }
    public boolean instruccion(){
        while(i<tokens.length){
            switch(nextToken()){
                case 3:
                    return true;
                case 7:
                    if(!declaracion())
                        return false;
                    break;
                case 16:
                    if(!desicion())
                        return false;
                    break;
                case 17:
                    if(!repeticion())
                        return false;
                    break;
                case 18:
                    if(nextToken()==8){
                        if(!asignacion())
                            return false;
                    }else{
                        i--;
                        if(!llamada())
                            return false;
                    }   
                    break;
                case 6:
                    if(!impresion())
                        return false;
                    break;
                case 25:
                    if(!lectura())
                        return false;
                    break;
            }
        }
        error="Linea "+Lexico.lineas.get(i-2)+": Instruccion mal Definioda, Falta '}'";
        return false;
    }
    public boolean llamada(){
        if(nextToken()==12)
            return true;
        error="Linea "+Lexico.lineas.get(i-2)+": Falta ';'";
        return false;
    }
    public boolean metodo(){
        if(nextToken()==18){
            if(nextToken()==2){
                if(instruccion())
                    return true;
            }else
                error="Linea "+Lexico.lineas.get(i-2)+": funcion mal Definida, '{'";
        }else
            error="Linea "+Lexico.lineas.get(i-2)+": funcion mal Definida";
        return false;
    }
    public boolean desicion(){
        if(condicion()){
            if(nextToken()==2){
                if(instruccion())
                    return true;
            }else
                error="Linea "+Lexico.lineas.get(i-2)+": Si mal Definido";
        }
        return false;
    }
    public boolean condicion(){
        int a=nextToken();
        
        if(a==18 || a==13 || a==14 || cadena()){
            a=nextToken();
            if(a==9){
                a=nextToken();
                if(a==18 || a==13 || a==14 || cadena()){
                    a=tokens[i];
                    if(a==5){
                        i++;
                        if(condicion())
                            return true;
                        else{
                            error="Linea "+Lexico.lineas.get(i-2)+": Condicion mal Definida";
                            return false;
                        }
                    }else
                        return true;
                }
            }
        }
        error="Linea "+Lexico.lineas.get(i-2)+": Condicion mal Definida";
        return false;
    }
    public boolean repeticion(){
        if(condicion())
            if(nextToken()==2){
                if(instruccion())
                    if(tokens[i-1]==3)
                        return true;
            }else
                error="Linea "+Lexico.lineas.get(i-2)+": Repeticion mal Definida";
        return false;
    }
    public boolean asignacion(){
        int a=nextToken();
        
        if(a==18 || a==13 || a==14 || cadena() || a==25){
            if(a==25){//Si es lectura
                if(lectura())
                    return true;
            }else{//sino es asiganacion normal
                a=nextToken();
                if(a==10){
                    if(asignacion())
                        return true;
                }else{
                    if(a==12)
                        return true;
                    error="Linea "+Lexico.lineas.get(i-2)+": Falta ';'";
                    return false;
                }      
            }
        }
        return false;
    }
    public boolean operacion(){
        int a=tokens[i-1];
        if(a!=9){
            if(a==18 || a==13 || a==14 || cadena()){
                a=tokens[i];
                if(a==10){
                    i+=2;
                    if(operacion())
                        return true;
                }
                return true;
            }
            error="Linea "+Lexico.lineas.get(i-2)+": mal uso de los signos";
            return false;
        }
        return true;
    }
    public boolean cadena(){
        if(tokens[i-1]==11){
            if(tokens[i]==15){
                if(tokens[i+1]==11){
                    i+=2;
                    return true;
                }
                error="Linea "+Lexico.lineas.get(i-2)+": Cadena mal Definida, Falta '\"'";
                return false;
            }else{
                if(tokens[i]==11){
                    i++;
                    return true;
                }
                error="Linea "+Lexico.lineas.get(i-2)+": Cadena mal Definida, Falta '\"'";
                return false;
            }
        }
        return false;
    }
    public boolean impresion(){
        int a=nextToken();
        if(a==11){
            a=nextToken();
            if(a==15 ){
                if(nextToken()==11)
                    if(nextToken()==12)
                        return true;
                    else
                        error="Linea "+Lexico.lineas.get(i-2)+": Falta ';'";
                else
                    error="Linea "+Lexico.lineas.get(i-2)+": No esta limitada la Cadena";
            }else{
                if(a==11)
                    if(nextToken()==12)
                        return true;
                    else
                        error="Linea "+Lexico.lineas.get(i-2)+": Falta ';'";
                else
                    error="Linea "+Lexico.lineas.get(i-2)+": No esta limitada la Cadena";
            }
        }else
            if(a==18)
                if(nextToken()==12)
                    return true;
                else
                    error="Linea "+Lexico.lineas.get(i-2)+": Falta ';'";
            else            
                error="Linea "+Lexico.lineas.get(i-2)+": Falta cadena";
        return false;
    }
    public boolean lectura(){
        if(nextToken()==7)
            if(nextToken()==12)
                return true;
            else
                error="Linea "+Lexico.lineas.get(i-2)+": Falta ';'";
        else
            error="Linea "+Lexico.lineas.get(i-2)+": Falta tipo de Dato que se leera...";
        return false;
    }
    public boolean mn(){
        if(main){
            error="Linea "+Lexico.lineas.get(i-2)+": Solo debe de haber un main";
            return false;
        }else{
            if(nextToken()==2){
                if(instruccion())
                    main=true;
            }else
                error="Linea "+Lexico.lineas.get(i-2)+": main mal Definido, Falta '{'";
        }
        return main;
    }
    public int nextToken(){
        int tk=tokens[i];
        i++;
        return tk;
    }
}
