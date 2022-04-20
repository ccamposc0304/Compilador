package transfor;

import java.io.IOException;
import java.util.regex.Pattern;
import static java.util.regex.Pattern.CASE_INSENSITIVE;


public class Transfor 
{
    public static Proceso procesa;                                              //instancia de clase Proceso
   
    public static void main(String[] args) throws IOException, InterruptedException 
    {
        procesa = new Proceso();                                                //inicializa clase Proceso
        
        procesa.InstanciaArchivos(args[0]);                                     //corre la funcion InstanciaArchivos y le envia como parametro el argumento[0] del mai
        procesa.iniciaLeerEscribirLinea();                                      // inicia el proceso de lectura de lineas
    }
    
}
