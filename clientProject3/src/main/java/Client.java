import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.UncheckedIOException;
import java.net.Socket;
import java.util.function.Consumer;

public class Client extends Thread{
    Socket socketClient;
    ObjectInputStream in;
    ObjectOutputStream out;

    public Client(Socket s){
        this.socketClient = s;
    }

    public void run() {
        try {
            this.out = new ObjectOutputStream(this.socketClient.getOutputStream());
            this.in = new ObjectInputStream(this.socketClient.getInputStream());
            this.socketClient.setTcpNoDelay(true);

            // keeps the client running until exit(1)
            while(true) {int i = 0; i++;}

        } catch (Exception var2) {

        }

    }

}
