package transfor;

import java.util.ArrayList;
import java.util.List;

public class LexSem {
    
    /*Instancia de clases usadas*/
    private List<String> tokenList;
    public List<String> etiquetas;
    private Estructura estruc;
    
    /*Para el manejo de cademas*/
    private String linea;
    private String linOrg;
    private String errMsjs;
    
    /*booleanos y contandores para manejo de estructuras con apertura y cierre en diferentes lineas*/
    boolean hayProgram;
    boolean hayEndprogram;
    int hayProgramC;
    int hayEndprogramC;
    boolean hayDo;
    boolean hayIf;
    boolean hayElse;

    public LexSem() {                                                           //constructor de esta clase
        etiquetas = new ArrayList<>();
        tokenList = new ArrayList<>();
        hayProgram = false;
        hayEndprogram = false;
        estruc = new Estructura();
        errMsjs = "";
        hayProgramC = 0;
        hayEndprogramC = 0;
        hayDo = false;
        hayIf = false;
        hayElse = false;
    }

    /* seters y geters de la clase*/
    public String getLinea() {
        return linea;
    }

    public boolean getHayDo() {
        return this.hayDo;
    }

    public boolean getHayIf() {
        return this.hayIf;
    }

    public boolean getHayProgram() {
        return this.hayProgram;
    }

    public boolean getHayEndprogram() {
        return this.hayEndprogram;
    }

    public void setLinea(String linea) {
        this.linea = linea;
        this.tokenizer(linea);
    }
    /* FIN - seters y geters de la clase*/

    public void vaciaLista() {                                                  // para vaciar la lista de tokens almacenados cuando se debe leer una nueva linea
        tokenList.clear();
    }

    public void tokenizer(String linea) {
        
        /*Se remplazantodos los blancos duplicados entre palabraspor uno solo,
        los que estan al inicio o final de una cadena se eliminan por completo*/
        linea = linea.replaceAll("^[\\s]*", "");
        linea = linea.replaceAll("[\\s]*$", "");
        linea = linea.replaceAll("[\\s]{1,}", " ");
        
        this.linOrg = linea;                                                    // para conservar el enunciado original
        String[] tokens = linea.split("([\\s]+)|(,[\\s]*)|(^[\\s]+)|(=[\\s]*)");// separa la linea en tokens segun conveniencia
        
        for (int i = 0; i < tokens.length; i++) {                               //almacena cada token en una lista
            if (!"".equals(tokens[i])) {
                System.out.printf("[%s] ", tokens[i]);
                tokenList.add(tokens[i]);
            }
        }
        System.out.println("");

        this.analizaLexSem(linOrg);

    }

    public void analizaLexSem(String linea) {

        String result = "";
        
        //primero se compara la linea contra el enum con los formatos correctos.
        for (Estructura.EstructuraTransfor comparaDatos : Estructura.EstructuraTransfor.values()) {
            String pat = comparaDatos.formato;
            if (linea.matches(pat)) {
                System.out.printf("%s \n", comparaDatos.name());
                result = comparaDatos.name();
                this.procesaSem(result);

            }
        }
        if (result == "") {//se compara la linea contra el enum con formatos erroneos pero parecidos a alguna de las estructuras en especifco.
            for (Estructura.FortranSimilar comparaDatos : Estructura.FortranSimilar.values()) {
                String pat = comparaDatos.formato;
                if (linea.matches(pat)) {
                    System.out.printf("%s \n", comparaDatos.name());
                    result = comparaDatos.name();
                    this.errLex(result);

                }
            }
        }
        if (result == "") { // si ante las comparaciones anteriores nmo hay nada, envia un error general
            this.addErr(ErrLog.erroresLog.E99.mostrarErr());
        }
    }

    public void procesaSem(String result) {                                     //validaciones logicas de la esructura capturada
        int countErrShow = 0;
        switch (result) {
            case "PROGRAM":
                this.validaHayEndprogram();
                this.hayProgram = true;
                hayProgramC++;
                if (hayProgramC > 1) {
                    this.addErr(ErrLog.erroresLog.E11.mostrarErr());
                }
                break;
            case "ENDPROGRAM":
                this.validaHayProgram();
                this.validaHayEndprogram();
                this.hayEndprogram = true;
                hayEndprogramC++;
                if (hayEndprogramC > 1) {
                    this.addErr(ErrLog.erroresLog.E12.mostrarErr());
                }
                break;
            case "DECLARACION":
                this.validaHayProgram();
                this.validaHayEndprogram();
                for (int i = 1; i < tokenList.size(); i++) {

                    if (estruc.validaVar(tokenList.get(i))) {
                        this.addErr(ErrLog.erroresLog.E16.mostrarErr());
                    } else {

                        if (tokenList.get(0).matches("(?i)(REAL)")) {
                            estruc.setREAL(tokenList.get(i));
                        }
                        if (tokenList.get(0).matches("(?i)(INTEGER)")) {
                            estruc.setINTEGER(tokenList.get(i));
                        }
                        if (tokenList.get(0).matches("(?i)(CHARACTER)")) {
                            estruc.setCHARACTER(tokenList.get(i));
                        }
                        if (tokenList.get(0).matches("(?i)DOUBLE")) {
                            if (countErrShow == 0) {
                                this.addErr(ErrLog.erroresLog.A1.mostrarErr());
                            }
                            countErrShow++;
                        }
                    }

                }
                break;

            case "PRINT":

                break;

            case "ASIGNACION":
                boolean encuentraPar = false;
                for (int i = 0; i < tokenList.size(); i++) {
                    if (tokenList.get(i).matches("((\\W|\\d|D)*)")) {

                    } else if (!estruc.validaVar(tokenList.get(i))) {
                        this.addErr(ErrLog.erroresLog.E15.mostrarErr());
                    }
                }

                break;

            case "DO":
                this.hayDo = true;
                for (int i = 1; i < tokenList.size(); i++) {
                    if (tokenList.get(i).matches("((\\W|\\d|D)*)")) {

                    } else if (!estruc.validaInteger(tokenList.get(i))) {
                        this.addErr(ErrLog.erroresLog.E15.mostrarErr());
                    }
                }

                break;
            case "ENDDO":
                this.hayDo = false;
                break;

            case "IF":
                this.hayIf = true;
                for (int i = 1; i < tokenList.size(); i++) {

                    if (tokenList.get(i).matches("((\\W|\\d|D)*|(.LE.)|(THEN))")) {
                        if (tokenList.get(i).matches("(THEN)")) {
                            this.hayElse = true;
                        }
                    } else if (!estruc.validaVar(tokenList.get(i))) {
                        this.addErr(ErrLog.erroresLog.E15.mostrarErr());
                    }
                }
                break;
                
            case "ELSE":
                this.hayElse = false;
                
            case "ENDIF":
                 if (this.hayElse) 
                 {
                    this.addErr(ErrLog.erroresLog.E21.mostrarErr());
                    this.hayElse = false;
                }
                 this.hayIf = false;
                 break;
                 
            case "GOTO":
                boolean etiqValida = false;
                for (int i = 0; i < etiquetas.size(); i++) 
                {
                    
                    if (etiquetas.get(i).matches(tokenList.get(1))) 
                    {
                        etiqValida = true;
                    }
                }
                if (!etiqValida) {
                     this.addErr(ErrLog.erroresLog.E22.mostrarErr());
                }
               
                break;
                    
                
        }
    }

    public void errLex(String result) {                                         //Asoscia la estructura mala a un codigo error
        switch (result) {
            case "PROGRAM":
                this.addErr(ErrLog.erroresLog.E13.mostrarErr());
                break;

            case "DECLARACION":
                this.addErr(ErrLog.erroresLog.E8.mostrarErr());
                break;

            case "PRINT":
                this.addErr(ErrLog.erroresLog.A1.mostrarErr());
                if (tokenList.size() < 2) {

                } else if (tokenList.get(1).matches("'[\\w|\\W]*")) {

                } else {
                    boolean encuentraPar = false;
                    for (int i = 1; i < tokenList.size(); i++) {

                        if (!estruc.validaVar(tokenList.get(i))) {
                            this.addErr(ErrLog.erroresLog.E15.mostrarErr());
                        }
                    }

                }
                break;

            case "IF":
                this.addErr(ErrLog.erroresLog.E20.mostrarErr());
                break;
                
            case "STOP":
                this.addErr(ErrLog.erroresLog.E23.mostrarErr());
                break;

        }
    }

    /*Validación de aperturas y cierres de estructuras*/
    public void validaHayProgram() {
        if (!hayProgram) {
            this.addErr(ErrLog.erroresLog.E10.mostrarErr());
        }
    }

    public void validaHayEndprogram() {
        if (hayEndprogram) {
            this.addErr(ErrLog.erroresLog.E14.mostrarErr());
        }
    }

    public void validaHayDo() {
        if (this.hayDo) {
            this.addErr(ErrLog.erroresLog.E18.mostrarErr());
        }
    }

    
    /*manejo del canal de errores*/
    public void resetErr() {
        this.errMsjs = "";
    }

    public void addErr(String newErr) {
        this.errMsjs = this.errMsjs + newErr;
    }

    public String canalErrores() {
        return this.errMsjs;
    }

}
