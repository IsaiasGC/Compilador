/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Isaias
 */
public class Reconocedor {

    static String error;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Entrada entrada=new Entrada();
        entrada.setVisible(true);
    }
    
    public static boolean analizar(String cadena){
        Sintactico sin=new Sintactico(cadena);
        if(sin.start())
            return true;
        else{
            error=sin.getError();
            return false;
        }
    }
}
