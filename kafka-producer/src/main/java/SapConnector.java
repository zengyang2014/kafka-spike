import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;

import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class SapConnector {
    private String endpoint;
    private String user;
    private String password;
    private WebSocket webSocket;

    public SapConnector(String endpoint, String user, String password) throws NoSuchAlgorithmException, WebSocketException, IOException {
        this.endpoint = endpoint;
        this.user = user;
        this.password = password;
        this.setupWebSocket();
    }

    private void setupWebSocket() throws NoSuchAlgorithmException, IOException, WebSocketException {
        // Create a custom SSL context.
        SSLContext context = NaiveSSLContext.getInstance("TLS");

        // Set the custom SSL context.
        this.webSocket = new WebSocketFactory()
                .setSSLContext(context)
                .setVerifyHostname(false)
                .setSocketFactory(SocketFactory.getDefault())
                .setVerifyHostname(false)
                .createSocket(this.endpoint)
                .addListener(new WebSocketAdapter() {
                    @Override
                    public void onTextMessage(WebSocket ws, String message) {
                        System.out.println("Received:" + message);
                        GlobalVariable.receivedMessage = message + "\n this message has been changed!";

                        SimpleProducer simpleProducer = new SimpleProducer();
                        simpleProducer.produceMsg(GlobalVariable.receivedMessage);
                        simpleProducer.closeProducer();
                    }
                })
                .setUserInfo(this.user, this.password)
                .connect();
    }

    public void sendMsg(String msg) {
        if (this.webSocket == null)
            return;
        this.webSocket.sendText(msg);
    }

    public static void main(String[] args) throws Exception {
        SapConnector sapConnector = new SapConnector(args[0], args[1], args[2]);
        sapConnector.sendMsg("test");
    }
}
