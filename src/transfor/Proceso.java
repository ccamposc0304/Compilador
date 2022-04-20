package transfor;

import java.io.IOException;
import java.util.regex.Pattern;
import static java.util.regex.Pattern.CASE_INSENSITIVE;

public class Proceso extends IOException {

    public static FlujoArchivos archivos;                                       //instancia de clase archivos
    public Cmd cmd;                                                             //instancia de clase Cmd
    private FlujoArchivos FA;                                                   //instancia FA de tipo FlujoArchivos
    private FlujoArchivos FA1;                                                  //instancia FA1 de tipo FlujoArchivos
    private FlujoArchivos FA2;                                                  //instancia FA2 de tipo FlujoArchivos
    private String linea;                                                       //se va a usar para leer la linea del archivo a revisar
    private String ruta;                                                        //se va a usar para leer la ruta del archivo a revisar
    private EtiquetasAmpersand etiqAmper;                                       //instancia de clase EtiquetasAmpersand
    private LexSem lexSem;                                                      //instancia de clase LexSem

    /* para el manejo de delimitadores*/
    private String sec1;                                                        
    private String sec3ant;
    private String sec2;
    private String sec3;
    private String sec4;
    private String sec5;
    
    /*manejo para control de lectura del archivo*/
    boolean skipLinea;
    boolean hayErrores;
    private int coutAmperLine;                                                  //control de cantidad de multilinea

    public Proceso() {
        FA = new FlujoArchivos();
        FA1 = new FlujoArchivos();
        FA2 = new FlujoArchivos();
        etiqAmper = new EtiquetasAmpersand();                                   //inicializa clase EtiquetasAmpersand
        archivos = new FlujoArchivos();                                         //inicializa clase FlujoArchivos
        lexSem = new LexSem();                                                  //inicializa clase LexSem
        this.linea = " ";                                                       //inicializa string linea
        hayErrores = false;
        cmd = new Cmd();
    }

    public void InstanciaArchivos(String ruta) throws IOException {
        //Instancia e inicializacion de variables para el flujo de archivos de la clase FlujoArchivos
        this.ruta = ruta;
        
        
        Pattern validaNombre = Pattern.compile("^([A-Za-z])([\\w]{0,28})([A-Za-z0-9])\\.\\w+$", CASE_INSENSITIVE);
        if (Pattern.matches(validaNombre.pattern(), ruta)) {                    //valida el formato para el nombre del archivo
            FA.abrirArchivo(ruta);
        }
    }

    public void iniciaLeerEscribirLinea() throws IOException, InterruptedException {
        /*INICIO seccion para iniciar escritura de archivos*/
        String cadena1;
        String cadena2;
        int cuentaLin = 0;
        cadena1 = ruta.replaceAll("(?i)\\.TRANSFOR", "-errores.txt");
        cadena2 = ruta.replaceAll("(?i)\\.TRANSFOR", ".f90");
        FA1.abreEscribir(cadena1);
        FA2.abreEscribir(cadena2);
        /*FIN seccion para iniciar escritura de archivos*/

        while (linea != null)                                                   // mientras la linea a leer no sea nula
        {
            linea = FA.leeLinea();                                              //lee la linea

            if (linea != null)                                                  //si la linea fue NULL no la escribe en los archivos: errores y .f90
            {
                FA1.escribeLinea(String.format("%05d %s\n", cuentaLin, linea));
                FA2.escribeLinea(String.format("%s\n", linea));
                cuentaLin++;
            }
            this.delimitaLinea(linea);                                          // pasa la linea a la funcion delimitaLiena para dividirla en secciones de codigo
        }
        if(linea == null && !lexSem.hayEndprogram)                              // si el programa termina sin ENDPROGRMA
        {
            FA1.escribeLinea(ErrLog.erroresLog.E9.mostrarErr() );
        }
        if(linea == null && lexSem.hayDo)                                       // si el programa termina sin ENDDO y existe un DO antes
        {
            FA1.escribeLinea(ErrLog.erroresLog.E18.mostrarErr() );
        }
        if(linea == null && lexSem.hayElse)                                     // si el programa o ENDIF termina sin ELSE y existe un THEN antes
        {
            FA1.escribeLinea(ErrLog.erroresLog.E21.mostrarErr() );
        }
        if(linea == null && lexSem.hayIf)                                       // si el programa termina sin ENDIF y existe un IF antes
        {
            FA1.escribeLinea(ErrLog.erroresLog.E19.mostrarErr() );
        }
        
        /* cierra la escritura de archivoa e inicia la ejecución de cmd.ejecuta*/
        FA1.cierraEscribir();
        FA2.cierraEscribir();
        if (!hayErrores) 
        {
            cmd.ejecuta(ruta);
        }
        
    }

    public void delimitaLinea(String linea) throws IOException {
        int sizeLinea = 0;                                                      //para almacenar el tamaño la linea a leida
        if (linea != null) {                                                    //si el tamaño de la linea no es 0
            sizeLinea = linea.length();                                         // copia el tamaño en sizeLinea            
        } else {                                                                // o no hace nada
        }

        /*sec3ant almacena la sec3 de la linea anterior solo si existe un &*/
        sec3ant = "";                                                           

        if (etiqAmper.getHayAmpersand() && this.coutAmperLine < 4) {
            sec3ant += sec3;
        }
        if (this.coutAmperLine > 3) {
            FA1.escribeLinea(ErrLog.erroresLog.E7.mostrarErr());
        }
        
        /*reset de todos los valores para las seciones de la nueva linea*/
        sec1 = "";
        sec2 = "";
        sec3 = "";
        sec4 = "";
        sec5 = "";
        
        boolean fueraRangoLin = false;                                          // reset de fuera de rango de la linea anterior
         skipLinea = false;                                                     // reset de fuera de comentarios de la linea anterior

        /*recorre la linea a nivel de characters*/
        for (int charPos = 0; charPos < sizeLinea; charPos++) {

            if (linea.charAt(charPos) == '!') {                                 // si hay un '!' deja de procesar los char hacia adelante del recorrido
                skipLinea = true;
            }
            if (!skipLinea) {                                                   // si no, continua el proceso del char
                
                
                if (charPos < 5) {                                              /*si es menor a 5 lo concatena en la sec1*/
                    sec1 += Character.toString(linea.charAt(charPos));
                } else if (charPos == 5) {                                      /*si es 5 lo concatena en la sec2*/
                    sec2 += Character.toString(linea.charAt(charPos));
                } else if (charPos > 5 && charPos < 72) {                       /*si es mayor a 5 y menor a 72 lo concatena en la sec3*/
                    sec3 += Character.toString(linea.charAt(charPos));
                } else if (charPos == 72) {                                     /*si es 72 lo concatena en la sec3*/
                    sec4 += Character.toString(linea.charAt(charPos));
                } else if (charPos > 72 && charPos < 80) {                      /* entre 73 y 80 lo concatena en la sec4*/
                    sec5 += Character.toString(linea.charAt(charPos));

                } else if (charPos > 81) {                                      // si es mayor a 81
                    if (linea.charAt(charPos) == ' ') {                         // si es blanco lo omite, si no alerta fuera de rango

                    } else {
                        fueraRangoLin = true;
                    }
                }
            }
        }
        

        if (fueraRangoLin) {                                                    //si se alerta fuera de rango muestra el respectivo error             
            FA1.escribeLinea(ErrLog.erroresLog.E1.mostrarErr());
            System.out.print(ErrLog.erroresLog.E1.mostrarErr());
        }
        if (etiqAmper.getHayAmpersand()) {                                      // si hay & almacena sec3 en sec3ant
            sec3 = sec3ant + sec3;
        }
        
        //quita los espacios de las secciones
        sec3 = sec3.replaceAll("^[\\s]*", "");                                  
        sec3 = sec3.replaceAll("[\\s]*$", "");
        sec3 = sec3.replaceAll("[\\s]{1,}", " ");

        System.out.printf("1>[%s] 2>[%s] 3>[%s] 4>[%s]\n", sec1, sec2, sec3, sec4);
        this.procesaLinea(sec1, sec2, sec3, sec4); // procesa las seciones
        skipLinea = false;
    }

    public void procesaLinea(String sec1, String sec2, String sec3, String sec4) throws IOException {
        if (sec1 != null | !"".equals(sec1)) {                                  //Validación de las etiquetas
            if(etiqAmper.validaFormatoEtiqueta(sec1))
                lexSem.etiquetas.add(sec1);
            if (etiqAmper.canalErrores() != "") {
                this.hayErrores = true;
                FA1.escribeLinea(etiqAmper.canalErrores());
                System.out.print(etiqAmper.canalErrores());
                etiqAmper.resetErr();
            }
        }
        if (sec2 != null | !"".equals(sec2)) {                                  //Validación de los Ampersand
            etiqAmper.validaFormatoAmpersand(sec2, "");
            if (etiqAmper.canalErrores() != "") {
                this.hayErrores = true;
                FA1.escribeLinea(etiqAmper.canalErrores());
                System.out.print(etiqAmper.canalErrores());
                etiqAmper.resetErr();

            }
        }
        if (sec4 != null | !"".equals(sec4)) {                                  //Validación de los Ampersand
            etiqAmper.validaFormatoAmpersand("", sec4);
            if (etiqAmper.canalErrores() != "") {
                this.hayErrores = true;
                FA1.escribeLinea(etiqAmper.canalErrores());
                System.out.print(etiqAmper.canalErrores());
                etiqAmper.resetErr();

            }
        }
        if (sec3 != null | !"".equals(sec3)) {                                  //validadción del codigo
            if (!etiqAmper.getHayAmpersand()) { 
                
                if (!skipLinea || !sec3.isEmpty()) {
                    if (!"".equals(sec3))
                    {
                        
                        lexSem.setLinea(sec3);
                        lexSem.vaciaLista();
                    }
                    
                }
                

                if (lexSem.canalErrores() != "")                                // si hubieron errores lo alerta exepto por Print y Read sin '*'
                {
                    if (lexSem.canalErrores().matches("(?i)(([\\s|\\w])*)(Advertencia001:)(([\\s|\\w])*)")) 
                    {
                        
                    }else{
                        this.hayErrores = true;
                    }
                    
                    FA1.escribeLinea(lexSem.canalErrores());
                    System.out.print(lexSem.canalErrores());
                    lexSem.resetErr();

                }
            }
        }
        this.cuentaAmperLineas();
    }

    public void cuentaAmperLineas() {                                           //cuenta cuantas lineas se levan con ampersand
        if (etiqAmper.getHayAmpersand()) {
            coutAmperLine++;
        } else {
            coutAmperLine = 0;
        }
    }
}
