
package transfor;

import java.io.IOException;


public class Cmd 
{
    public void ejecuta(String ruta) throws InterruptedException
    {
        try {
            
                String nameApp = ruta.replaceAll("(.[\\w]+)$", "");
                String ruta1 = ruta.replaceAll(".transfor", ".f90");


                new ProcessBuilder("cmd", "/c", "gfortran \"" + ruta1 + "\" -o \" " + nameApp).inheritIO().start().waitFor();
                new ProcessBuilder("cmd", "/c", "\" " + nameApp).inheritIO().start().waitFor();
                
            } catch (IOException e) {
            }
    }
}
