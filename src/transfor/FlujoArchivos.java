
package transfor;

import java.io.*;

public class FlujoArchivos 
{
///inicializacion de variables y funciones necesarias para la llectura y escritura de archivos
    private String pathOrg;
    private String pathErr;
    public  String path;
    
    public File fileOrg;
    private File fileErr;
    private FileReader FR;
    private BufferedReader BF;
    private FileWriter FW;

    public void abrirArchivo(String ruta) throws FileNotFoundException, IOException 
    { /// clase publica para manejo de inizializar el lector de archivos con el archivo a utilizar
        this.pathOrg = ruta;
        fileOrg = new File(pathOrg);
        
        
        
        if( fileOrg.exists())
        {   
            FR = new FileReader(fileOrg);
            BF = new BufferedReader(FR);
            System.out.println("Se encuentra el archivo en > "+ fileOrg.getAbsoluteFile());
            path = fileOrg.getAbsoluteFile().toString();
        }else{
            System.out.println("NO se encuentra el archivo en > "+ fileOrg.getAbsoluteFile());
        }
           
    }
    
    public String leeLinea() throws IOException
    {/// clase publica para leer linea del archivo
        String linea = BF.readLine(); 
        return linea;
    }
    
    public void abreEscribir(String ruta) throws IOException
    {/// clase publica para manejo de inizializar archivos con el archivo a crear con los errores del programa
        this.pathErr = ruta;
        fileErr = new File(pathErr);
        FW = new FileWriter(fileErr);
               
    }
    public void escribeLinea(String cadena) throws IOException
    { /// escribe en el archivo
    
        FW.write(cadena);
    }
    public void cierraEscribir() throws IOException
    { /// cierra el archivo en escritura
    
        FW.close();
    }
}
