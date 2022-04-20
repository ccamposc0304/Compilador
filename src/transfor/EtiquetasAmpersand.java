
package transfor;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class EtiquetasAmpersand 
{
    
    private List<String> etiquetas;
    private String errMsjs;
    private boolean hayAmpersand;
    
    public EtiquetasAmpersand()
    {
        
        etiquetas = new ArrayList<String>();
        errMsjs = "";
    }
    
    public boolean revisaEtiqRep(String etiqueta)
    {
        boolean rev = false;
        
        for (int i = 0; i < etiquetas.size(); i++) 
        {
            if (etiquetas.get(i).equals(etiqueta)) 
            {
                rev = true;
            }
        }
        return rev;
    }
        
    public boolean validaFormatoEtiqueta(String sec1 )
    {
        boolean valido = false;
        String pat = "((\\d){5})|((\\s){5})|(^$)";
        Pattern.compile(pat);
        if (!Pattern.matches(pat, sec1)) 
        {
            this.errMsjs += ErrLog.erroresLog.E2.mostrarErr();
        }else if(Pattern.matches("((\\d){5})", sec1))
        {
            if (!this.revisaEtiqRep(sec1)) 
            {
                this.etiquetas.add(sec1);
                valido = true;
                
            }else
            {
                this.errMsjs += ErrLog.erroresLog.E4.mostrarErr();
            }
            
        }
        return valido;
    }
    
    public void setHayAmpersand(boolean bool)
    {
        this.hayAmpersand = bool;
    }
    
    public boolean getHayAmpersand()
    {
        return this.hayAmpersand;
    }
    
    public void validaFormatoAmpersand(String sec2,String sec4)
    {
        String pat = "(\\s)|(&)|";
        Pattern.compile(pat);
        
        /* INICIO ampersand en la columna 5*/
        if (!Pattern.matches(pat, sec2)) 
        {
            this.errMsjs += ErrLog.erroresLog.E3.mostrarErr();
        }
        if (Pattern.matches("&", sec2)) 
        {
            if (!hayAmpersand) 
            {
                this.errMsjs += ErrLog.erroresLog.E5.mostrarErr();
            }else
            {
                this.setHayAmpersand(false);
            }
            
        }
        if (Pattern.matches(" |\\w|", sec2))
        {
            if (hayAmpersand) 
            {
                this.errMsjs += ErrLog.erroresLog.E6.mostrarErr();
                this.setHayAmpersand(false);
            }
        }
        /* FIN ampersand en la columna 5*/
        /* INICIO ampersand en la columna 74*/
        if (!Pattern.matches(pat, sec4)) 
        {
            this.errMsjs += ErrLog.erroresLog.E3.mostrarErr();
        }else if (Pattern.matches("&", sec4)) 
        {
            this.setHayAmpersand(true);
        }
        /* FIN ampersand en la columna 74*/
    }
    
    
    public void resetErr()
    {
        this.errMsjs = "";
    }
    
    public String canalErrores()
    {
        return this.errMsjs;
    }
}
