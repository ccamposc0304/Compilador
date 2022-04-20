package transfor;

public class ErrLog 
{
    
    enum erroresLog /*Enum con un repositrio de todos errores que exiten en este sistema*/
    {
        A1("Advertencia001: Este comando no es soportado por Transfor"),
        E1("Error001: La linea no puede exceder 80 columnas"),
        E2("Error002: En columnas de 0 a 4 solo se permiten etiquetas con\n"
                + "        numeracion de 00000 a 99999 en el formato #####,\n"
                + "        ejemplo(00001 , 00024)"),
        E3("Error003: Esta columna solo permite '&' o ''(espacio en blaco)"),
        E4("Error004: La etiqueta registrada en la linea anterior esta repetida,\n"
                + "        debe utilizar una etiqueta no registrada anteriormente"),
        E5("Error005: Se esperaba un '&' en la columna 74 de la linea anterior a esta"),
        E6("Error006: Se esperaba un '&' en la columna 5 de esta linea"),
        E7("Error007: El limite multilinea es de 3 lienas"),
        E8("Error008: La declaracion tiene un formato invalido\n"
                + "        o no soportado por Transfor, verifique."),
         E9("Error009: Se esperaba ENDPROGRAM en este punto del programa"),
         E10("Error010: Se espera PROGRAM antes de cualquier otra ejecucion"),
         E11("Error011: PROGRAM ya fue definido antes, comando duplicado"),
         E12("Error012: ENDPROGRAM ya fue definido antes, comando duplicado"),
         E13("Error013: Formato incorrecto para Program, se espera [PROGRAM nombre_programa]"),
         E14("Error014: El programa ya fue finalizado por ENDPROGRAM, instrucci[on no valida]"),
         E15("Error015: Una de las variables que trata de mostrar no fue declarada antes."),
         E16("Error016: Esta declarando una variable que ya se habia declarado."),
         E17("Error017: Se requiere una variable declarada tipo INTEGER para "
                 + "        constante_mínima y variable_máxima."),
         E18("Error018: Se esperaba el ENDDO para el ciclo DO abierto."),
         E19("Error019: Se esperaba el ENDIF para el ciclo IF abierto."),
         E20("Error020: La declaraci[on de IF es incorrecta, revise su sintaxis."),
         E21("Error021: Se esperaba un ELSE en el ciclo IF"),
         E22("Error022: GOTO esta apuntando a una etiqueta no valida"),
         E23("Error023 El comando STOP debe contener comillas('') en el mesaje, si lo contiene"),
         E99("Error099: EL COMANDO QUE ESTA INGRESANDO NO SE RECONOCE.");
        
        
        public final String errMsj;
        
        
        erroresLog(String errMsj)
        {
            this.errMsj = errMsj;
        }
        
        public String mostrarErr()
        {
            String err = String.format( "        %s\n",errMsj).toUpperCase();
            
            return err;
        }
    }
    
    
}
