
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.StringTokenizer;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author prueva
 */
public class TextFileManager {
    private BufferedReader br;
    private BufferedWriter bw;
    private String fileIn;
    private String fileOut;
    
    public TextFileManager(String fileIn, String fileOut){
        //super(fileIn, fileOut);
        this.fileIn="code/"+fileIn;
        this.fileOut="code/"+fileOut;
    }
    public void writeLine(String texto) throws IOException{
        try{
            bw=new BufferedWriter(new FileWriter(fileOut, false));
            bw.write(texto);
            bw.newLine();
            bw.close();
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
    }
    public String getLine(int row) throws FileNotFoundException, IOException{
        String line=null;
        try{
            br=new BufferedReader(new FileReader(fileIn));
            for(int i=1; i<=row; i++){
                line=br.readLine();
            }
            br.close();
        }catch(IOException e){
            System.out.println(e.getMessage());
        }finally{
            return line;
        }
    }
    public String buscarCadena(String cadena, int campo){
        int cont=0;
        String aux="";
        StringTokenizer token=new StringTokenizer(cadena, " ");
        while(token.hasMoreTokens()){
            aux=token.nextToken();
            cont++;
            if(cont==campo){
                break;
            }
        }
        return aux;
    }
}
