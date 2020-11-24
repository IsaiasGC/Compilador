
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import sun.awt.CustomCursor;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Isaias
 */
public class Entrada extends javax.swing.JFrame {
    
    TextFileManager manager;
    Mensaje mensaje;
    boolean cambios;
    String cadena="";
    String nuevo="\n\nfuntion example{\n\t\n}\nmain{\n\t\n}\n";
    String sketch="";
    int p;
    DefaultStyledDocument doc;
    StyleContext sc;
    
//    boolean fi=true, enter=false;
//    int npalabra=0;
//    String upalabra="";
    ArrayList<Token> tokens;
    String[][] palabras={{"funtion", "2"}, {"int", "2"}, {"float", "2"}, {"string", "2"}, 
        {"boolean", "2"}, {"AND", "4"}, {"OR", "4"}, {"if", "3"}, {"while", "3"}, {"out", "1"}, {"in", "1"}, {"main", "1"}};
    Color colores[]={null, Color.ORANGE, Color.BLUE, Color.GREEN, Color.YELLOW};
//    int caract=0;
//    int ncaract;
//    String text;
//    
//    /**
//     * Creates new form Entrada
//     */
    public Entrada() {
        sc=new StyleContext();
        doc=new DefaultStyledDocument(sc);
        initComponents();
//        setSize(1000, 600);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        inicializar();
        nuevo();
        setSize(screenSize);
        setResizable(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        mensaje=new Mensaje(this, false);
        this.setTitle("Compilador");
    }
    private void nuevo(){
        try {
            doc.insertString(0, nuevo, null);
        } catch (BadLocationException ex) {
            Logger.getLogger(Entrada.class.getName()).log(Level.SEVERE, null, ex);
        }
        buscar();
        texto.setSelectionStart(18);
        //sketch=texto.getText();
        //System.out.println(sketch);
    }
    public void pintarAzul(int inicio, int fin){
        Style azul=sc.addStyle("ConstantWidth", null);
        StyleConstants.setForeground(azul, new Color(16, 156, 199));
        StyleConstants.setBold(azul, true);
        doc.setCharacterAttributes(inicio, fin, azul, true);
    }
    public void pintarAnaranjado(int inicio, int fin){
        Style anaranjado=sc.addStyle("ConstantWidth", null);
        StyleConstants.setBold(anaranjado, true);
        StyleConstants.setForeground(anaranjado, new Color(48, 176, 39));
        doc.setCharacterAttributes(inicio, fin, anaranjado, true);
    }
    public void pintarVerde(int inicio, int fin){
        Style verde=sc.addStyle("ConstantWidth", null);
        StyleConstants.setBold(verde, true);
        StyleConstants.setForeground(verde, new Color(199, 16, 89));
        doc.setCharacterAttributes(inicio, fin, verde, true);
    }
    public void pintarAmarillo(int inicio, int fin){
        Style amarillo=sc.addStyle("ConstantWidth", null);
        StyleConstants.setForeground(amarillo, new Color(16, 199, 68));
        doc.setCharacterAttributes(inicio, fin, amarillo, true);
    }
    public void pintarNormal(int inicio, int fin){
        Style gris=sc.addStyle("ConstantWidth", null);
        StyleConstants.setBold(gris, false);
        StyleConstants.setForeground(gris, new Color(192, 192, 192));
        doc.setCharacterAttributes(inicio, fin, gris, true);
    }
    public void pintarNormal(){
        Style gris=sc.addStyle("ConstantWidth", null);
        StyleConstants.setBold(gris, false);
        StyleConstants.setForeground(gris, new Color(192, 192, 192));
    }
    public void inicializar(){
//        SimpleAttributeSet attrs = new SimpleAttributeSet();
//        StyleConstants.setForeground(attrs, Color.GRAY);
        texto.addKeyListener(new KeyListener(){
            @Override
            public void keyPressed(KeyEvent e){
                    p=texto.getSelectionStart();
                    /*sketch=sketch.substring(0, p)+e.getKeyChar()+sketch.substring(p+1, sketch.length()-1);
                    System.out.println(sketch);
                    texto.getDocument().remove(0, texto.getText().length());
                    Thread.sleep(3000);
                    texto.setText(sketch);*/
                    buscar();
                    revisar();
            }

            @Override
            public void keyTyped(KeyEvent e) {
            }
            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
    }
    public void buscar(){
//        System.out.println("Puntero: "+p);
        try {
            genararTokens(doc.getText(0, doc.getLength()));
        } catch (BadLocationException ex) {
            Logger.getLogger(Entrada.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (int i=0; i<tokens.size(); i++) {
            reconocer(tokens.get(i));
            //System.out.print(tokens.get(i)+", ");
        }
        texto.setSelectionStart(p);
    }
    public void genararTokens(String cadena){
        tokens=new ArrayList<Token>();
        String token="";
        Token tk;
        int s=0;
        char c;
        boolean f=true;
        for (int i=0; i<cadena.length(); i++) {
            c=cadena.charAt(i);
            if(c!='\n' && c!='\r' && c!='\t' && c!=' '){
                if(c!='(' && c!=')' && c!=';' && c!='{' && c!='}' && c!='+' && c!='-' && c!='*' && c!='/' && c!='%' && c!='=' && c!='!' && c!='<' && c!='>'&& c!='"'){
                    token=token+c;
                    if(f){
                        s=i;
//                        System.out.print(s+"-");
                        f=false;
                    }
                }else{
                    if(!token.equals("")){
                        tk=new Token();
                        tk.token=token;
                        tk.start=s;
                        tk.end=i-1;
                        tokens.add(tk);
                        token="";
//                        System.out.print(i+", ");
                    }
                    f=true;
//                    tk=new Token();
//                    tk.token=c+"";
//                    tk.start=s;
//                    tk.end=s;
//                    tokens.add(tk);
//                    s=i+1;
                }
            }else{ 
                if(!token.equals("")){
                    tk=new Token();
                    tk.token=token;
                    tk.start=s;
                    tk.end=i-1;
                    tokens.add(tk);
                    token="";
//                    System.out.print(i+", ");
                }
                f=true;
//                tk=new Token();
//                tk.token=c+"";
//                tk.start=s;
//                tk.end=s;
//                tokens.add(tk);
//                s=i+1;
            }
        }
        if(!token.equals("")){
            tk=new Token();
            tk.token=token;
            tk.start=s;
            tk.end=cadena.length();
            tokens.add(tk);
            token="";
//            System.out.print(cadena.length()+"\n");
        }
    }
    public void reconocer(Token reservada){
        String color="";
        boolean val=false;
        for (int i = 0; i <palabras.length ; i++) {
//            System.out.println(reservada.token+"["+reservada.start+"-"+reservada.end+"]");
            if(reservada.token.equals(palabras[i][0]))
            {
                val=true;
                color=palabras[i][1];
                break;
            }
        }
        if(val){
//            SimpleAttributeSet attrs = new SimpleAttributeSet();
//            StyleConstants.setForeground(attrs, colores[Integer.parseInt(color)]);
//            try {
                switch(color){
                    case "1":
                        pintarAnaranjado(reservada.start, reservada.end);
                        break;
                    case "2":
                        pintarAzul(reservada.start, reservada.end);
                        break;
                    case "3":
                        pintarVerde(reservada.start, reservada.end);
                        break;
                    case "4":
                        pintarAmarillo(reservada.start, reservada.end);
                        break;
                }
                pintarNormal();
//                texto.getStyledDocument().insertString(texto.getStyledDocument().getLength(), reservada+" ", attrs);
//                StyleConstants.setForeground(attrs, Color.DARK_GRAY);
//                texto.getStyledDocument().insertString(texto.getStyledDocument().getLength()," ", attrs);
//            }catch(BadLocationException ex){}
        }else{
            pintarNormal(reservada.start, reservada.end);
//            SimpleAttributeSet attrs = new SimpleAttributeSet();
//            StyleConstants.setForeground(attrs, Color.DARK_GRAY);
//            try {
//                texto.getStyledDocument().insertString(texto.getStyledDocument().getLength(), reservada+" ", attrs);
//            }catch(BadLocationException ex){}
        }
        texto.setForeground(new Color(192,192,192));
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        texto = new javax.swing.JTextPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        consola = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        menuArchivo = new javax.swing.JMenu();
        mNuevo = new javax.swing.JMenuItem();
        mGuardar = new javax.swing.JMenuItem();
        menuCorrer = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Compilador");
        setBackground(new java.awt.Color(102, 102, 102));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        panel.setBackground(new java.awt.Color(102, 102, 102));

        texto.setBackground(new java.awt.Color(51, 51, 51));
        texto.setDocument(doc);
        texto.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        texto.setForeground(new java.awt.Color(192, 192, 192));
        texto.setCaretColor(new java.awt.Color(192, 192, 192));
        texto.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        texto.setDisabledTextColor(new java.awt.Color(255, 255, 255));
        texto.setMinimumSize(new java.awt.Dimension(6, 150000));
        jScrollPane1.setViewportView(texto);

        consola.setEditable(false);
        consola.setBackground(new java.awt.Color(153, 153, 153));
        consola.setColumns(20);
        consola.setFont(new java.awt.Font("Consolas", 0, 13)); // NOI18N
        consola.setRows(5);
        consola.setMaximumSize(new java.awt.Dimension(2147483647, 22));
        jScrollPane2.setViewportView(consola);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel1.setText("Consola:");

        javax.swing.GroupLayout panelLayout = new javax.swing.GroupLayout(panel);
        panel.setLayout(panelLayout);
        panelLayout.setHorizontalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1237, Short.MAX_VALUE)
                    .addComponent(jScrollPane2)
                    .addGroup(panelLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panelLayout.setVerticalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 550, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16))
        );

        jMenuBar1.setBackground(new java.awt.Color(255, 255, 255));

        menuArchivo.setText("Archivo");

        mNuevo.setText("Nuevo");
        mNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mNuevoActionPerformed(evt);
            }
        });
        menuArchivo.add(mNuevo);

        mGuardar.setText("Guardar");
        mGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mGuardarActionPerformed(evt);
            }
        });
        menuArchivo.add(mGuardar);

        jMenuBar1.add(menuArchivo);

        menuCorrer.setText("Correr");

        jMenuItem1.setText("Limpiar");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        menuCorrer.add(jMenuItem1);

        jMenuItem2.setText("Correr");
        menuCorrer.add(jMenuItem2);

        jMenuBar1.add(menuCorrer);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void mGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mGuardarActionPerformed
        String nom=JOptionPane.showInputDialog("Nombre del Archivo");
        manager=new TextFileManager(nom+".cod", nom+".cod");
        try {
            manager.writeLine(texto.getText());
            cambios=false;
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }//GEN-LAST:event_mGuardarActionPerformed

    private void mNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mNuevoActionPerformed
        if(cambios){
            int op=JOptionPane.showConfirmDialog(null, "Desea Guardar los Cambios Realizados");
            JOptionPane.showMessageDialog(null, op+"");
            if(op==0){
                guardar();
                texto.setText(nuevo);
            }else if(op==1)
                texto.setText(nuevo);
        }else
            texto.setText(nuevo);
        manager=null;
    }//GEN-LAST:event_mNuevoActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        guardar();
        revisar();
    }//GEN-LAST:event_jMenuItem1ActionPerformed
    public void revisar(){
        if(!cadena.equals(texto.getText())){
            cadena=texto.getText();
            if(Reconocedor.analizar(cadena)){
                mostrar("Analisis Completo\n\tEl codigo esta Correctamente escrito...", true);
            }else{
                mostrar(Reconocedor.error+"    ...", false);
            }
        }
    }
    public void guardar(){
        if(manager==null){
            String nom=JOptionPane.showInputDialog("Nombre del Archivo");
            manager=new TextFileManager(nom+".cod", nom+".cod");
        }
            try {
                manager.writeLine(texto.getText());
                cambios=false;
            } catch (IOException ex) {
                System.err.println(ex.getMessage());
            }
    }
    public void mostrar(String mensage, boolean bien){
        if(bien)
            consola.setForeground(Color.BLUE);
        else
            consola.setForeground(Color.RED);
        consola.setText(mensage);
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea consola;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JMenuItem mGuardar;
    private javax.swing.JMenuItem mNuevo;
    private javax.swing.JMenu menuArchivo;
    private javax.swing.JMenu menuCorrer;
    private javax.swing.JPanel panel;
    private javax.swing.JTextPane texto;
    // End of variables declaration//GEN-END:variables
}
