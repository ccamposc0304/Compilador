
package transfor;

import java.util.ArrayList;
import java.util.List;

public class Estructura 
{
    private List<String> REAL;
    private List<String> INTEGER;
    private List<String> CHARACTER;
   
    
    /*-----------------SECCION ENUMS */
    public enum EstructuraTransfor
    {
        PROGRAM     ("(?i)^((PROGRAM)\\s)(([A-Za-z1-9][\\w]{1,28}[A-Za-z1-9]))$"),
        ENDPROGRAM  ("(?i)^(ENDPROGRAM)[\\s]*$"),
        DECLARACION ("(?i)^((DOUBLE\\sPRECISION|REAL|INTEGER|CHARACTER)[\\s]*)"+ "(([A-Za-z1-9][A-Za-z1-9])|([A-Za-z1-9][\\w]{1,28}[A-Za-z1-9]))"
                + "(((,[\\s]*[A-Za-z1-9]{1,2})|(,[\\s]*[A-Za-z1-9][\\w]{1,28}[A-Za-z1-9]))*)$"),
        PRINT       ("(?i)^Print\\*$|^(Print\\*)(([\\s]+)|([,][\\s]+)('[\\w+|\\W+|\\s]*)'|((([,][\\s]+)([\\w]+))+))$"),
        READ        ("(?i)^(READ\\*)([\\s]*)(([,][\\s]+)([\\w]+))$"),
        ASIGNACION  ("(?i)(^[A-Za-z][\\w]+[\\s]*=[\\s]*([\\d|\\w|\\s|\\W]+))"),
        DO          ("(?i)^((DO)[\\s+]((([A-Za-z][A-Za-z1-9])|([A-Za-z1-9][\\w]{1,28}[A-Za-z1-9]))[\\s]*=[\\s]*[\\d]+[,][\\s]*"
                        + "(([A-Za-z][A-Za-z1-9])|([A-Za-z1-9][\\w]{1,28}[A-Za-z1-9]))))$"),
        ENDDO       ("(?i)^[\\s]*(ENDDO)[\\s]*"),
        IF          ("(?i)^((if)[\\s]*[\\(][\\s]*([\\w|\\W]*[\\s]*[.LE.|<=|>=|=][\\s]*[\\w|\\W]*)[\\s]*[\\)][\\s]*((THEN){0,1}))$"),
        ELSE        ("(?i)^[\\s]*(ELSE)[\\s]*"),
        ENDIF       ("(?i)^[\\s]*(ENDIF)[\\s]*"),
        GOTO        ("(?i)^[\\s]*(GOTO)[\\s]*([\\d]{5})[\\s]*"),
        STOP        ("(?i)^[\\s]*(STOP)[\\s]((['][\\w|\\W]*[']))$");
        
        public final String formato;
        EstructuraTransfor(String str)
        {
            this.formato = str;
        }
    }
    
    public enum FortranSimilar
    {
        PROGRAM     ("(?i)(PROGRAM)[\\s|\\w]*"),
        ENDPROGRAM  ("(?i)[\\s]*(ENDPROGRAM)[\\s|\\w]*"),
        DECLARACION ("(?i)(^([\\w]+)[\\s]+)(([A-Za-z][A-Za-z1-9])|([A-Za-z1-9][\\w]{1,28}[A-Za-z1-9]))"
                         + "(((,[\\s]*[A-Za-z1-9]{1,2})$|(,[\\s]*[A-Za-z1-9][\\w]{1,28}[A-Za-z1-9]))*$)"),
        PRINT       ("(?i)^Print$|^(Print)(([\\s]+)|([,][\\s]+)('[\\w+|\\W+|\\s]*)'|((([,][\\s]+)([\\w]+))+))$"),
        READ        ("(?i)^(READ\\*)([\\s]*)(([,][\\s]+)([\\w]+))$"),
        ASIGNACION  ("(?i)(^[A-Za-z][\\w]+[\\s]*[=][\\s]*[0-9]+(\\S[0-9])?$)|(^[A-Za-z][\\w]+=[A-Za-z]+$)"),
        DO          ("(?i)^((DO)[\\s+]((([A-Za-z][A-Za-z1-9])|([A-Za-z1-9][\\w]{1,28}[A-Za-z1-9]))[\\s]*=[\\s]*[\\d]+[,][\\s]*"
                        + "(([A-Za-z][A-Za-z1-9])|([A-Za-z1-9][\\w]{1,28}[A-Za-z1-9]))))$"),
        ENDDO       ("(?i)^[\\s|\\w]*(ENDDO)[\\s|\\w]*"),
        IF          ("(?i)^((if)[\\s]*[\\(][\\s]*([\\w|\\W]*[\\s]*[.LE.|<=|>=|=][\\s]*[\\w|\\W]*)[\\s]*[\\)][\\s]*([\\w|\\W]+))$"),
        GOTO        ("(?i)^[\\s]*(GOTO)[\\s]*([\\d]+})[\\s]*"),
        STOP        ("(?i)^[\\s]*(STOP)[\\s](([']?[\\w|\\W]*[']?)+)$*");;
        
        public final String formato;
        FortranSimilar(String str)
        {
            this.formato = str;
        }
    }

    /*-----------------SECCION ENUMS */
    public Estructura() {
        REAL = new ArrayList<>();
        INTEGER= new ArrayList<>();
        CHARACTER= new ArrayList<>();
        
    }
    /*-----------------SECCION LISTA TIPOS DE VARIABLE*/
    public List<String> getREAL() {
        return REAL;
    }

    public void setREAL(String REAL) {
        this.REAL.add(REAL);
    }

    public List<String> getINTEGER() {
        return INTEGER;
    }

    public void setINTEGER(String INTEGER) {
        this.INTEGER.add(INTEGER);
    }

    public List<String> getCHARACTER() {
        return CHARACTER;
    }

    public void setCHARACTER(String CHARACTER) {
        this.CHARACTER.add(CHARACTER);
    }
    
    public boolean validaVar(String var)
    {
        boolean validaVar = false;
        
        
        if(this.validaReal(var))
            validaVar = true;
        if(this.validaInteger(var))
            validaVar = true;
        if(this.validaCharacter(var))
            validaVar = true;
        return validaVar;
    }
    
    public boolean validaReal(String str)
    {
        boolean validaREAL = false;
        
        for (int i = 0; i < REAL.size(); i++) 
        {
            if(str.matches(REAL.get(i)))
                validaREAL= true;
            
        }
        
        return validaREAL;
    }
    public boolean validaInteger(String str)
    {
        boolean validaINTEGER = false;
        
        for (int i = 0; i < INTEGER.size(); i++) {
            
            if(str.matches(INTEGER.get(i)))
                validaINTEGER= true;
        }
        
        return validaINTEGER;
    }
    public boolean validaCharacter(String str)
    {
        boolean validaCHARACTER = false;
        
        for (int i = 0; i < CHARACTER.size(); i++) 
        {
            if(str.matches(CHARACTER.get(i)))
                validaCHARACTER= true;
            
        }
        
        return validaCHARACTER;
    }
    /*-----------------SECCION LISTA TIPOS DE VARIABLE*/
    
}
