import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;
import java.util.function.Consumer;

public class Server {
    GameServer server; // instance of the main server thread
    int numClients = 1; // number of clients connected
    ArrayList<ClientThread> clients = new ArrayList<>(); // list of the clients connected
    private Consumer<Serializable> callback;

    public Server(Consumer<Serializable> call, int port){
        this.callback = call;

        // creating and starting the actual server thread
        this.server = new GameServer(port);
        this.server.start();
    }


    public class GameServer extends Thread{
        int port; // server the port is located on

        public GameServer(int port){
            this.port = port;
        }

        public void run() {
            try {
                ServerSocket serverSocket = new ServerSocket(this.port);
                try {
                    while(true) {
                        ClientThread c = new ClientThread(serverSocket.accept(), Server.this.numClients);
                        Server.this.callback.accept("client has connected to server: client #" + Server.this.numClients);
                        Server.this.clients.add(c);
                        c.start();
                        ++Server.this.numClients;
                    }
                } catch (Throwable var5) {
                    try {
                        serverSocket.close();
                    } catch (Throwable var4) {
                        var5.addSuppressed(var4);
                    }
                    throw var5;
                }
            } catch (Exception var6) {
                Server.this.callback.accept("Server socket did not launch");
            }
        }

    }

    public class ClientThread extends Thread{
        Socket connection; // socket to communicate with client
        int id; // identifies which client
        ObjectInputStream in;
        ObjectOutputStream out;

        // categories and word banks
        ArrayList<String> sports;
        ArrayList<String> computers;
        ArrayList<String> foods;


        public ClientThread(Socket s, int id){
            this.connection = s;
            this.id = id;

            sports = new ArrayList<>(Arrays.asList("COACH", "THROW", "FIELD", "MEDAL", "PITCH", "CATCH", "SCORE"));
            computers = new ArrayList<>(Arrays.asList("CACHE", "ARRAY", "DEBUG", "LINUX", "LOGIN", "MOUSE", "QUEUE"));
            foods = new ArrayList<>(Arrays.asList("BAGEL", "FEAST", "GRAVY", "HONEY", "MANGO", "SALAD", "WAFER"));
        }

        public void generateRandomWord(GameData data){
            // Use Random to generate a random index
            Random random = new Random();
            int randomIndex;
            if(Objects.equals(data.category, "sports")){
                randomIndex = random.nextInt(sports.size());
                data.word = sports.get(randomIndex);
                sports.remove(randomIndex);
            } else if(Objects.equals(data.category, "computers")){
                randomIndex = random.nextInt(computers.size());
                data.word = computers.get(randomIndex);
                computers.remove(randomIndex);
            } else{
                randomIndex = random.nextInt(foods.size());
                data.word = foods.get(randomIndex);
                foods.remove(randomIndex);
            }
        }

        public void handleGuess(GameData data){
            if(data.word == null){ // if there is no word selected to guess, pick random word from category and delete
                generateRandomWord(data);
                Server.this.callback.accept("client #" + this.id + " got a new word " + data.category + ":" + data.word);
            }

            // handle the user guess
            data.correctPosition = -1;
            char guess = Character.toUpperCase(data.letterGuess.charAt(0));
            for(int i = 0; i < data.word.length(); i++){
                if(guess == data.word.charAt(i)){
                    data.correctPosition = i;
                    data.currentWord.set(i, 1);
                }
            }

            data.finished = true;
            for(int tile: data.currentWord){
                if (tile == 0) {
                    data.finished = false;
                    break;
                }
            }

            // update lives
            if(data.correctPosition == -1){
                data.guessesLeft -= 1;
                Server.this.callback.accept("client #" + this.id + " guessed " + data.letterGuess + " incorrectly");
                if(data.guessesLeft == 0){
                    data.incorrectGuessesStreak += 1;
                    // update the game data
                    if(Objects.equals(data.category, "sports")){
                        data.incorrectSportGuesses += 1;
                    } else if(Objects.equals(data.category, "computers")){
                        data.incorrectComputerGuesses += 1;
                    } else{
                        data.incorrectFoodGuesses += 1;
                    }
                    Server.this.callback.accept("client #" + this.id + " failed to get " + data.category);

                    if(data.incorrectGuessesStreak == 3 || data.incorrectSportGuesses == 3 || data.incorrectComputerGuesses == 3 || data.incorrectFoodGuesses == 3){
                        Server.this.callback.accept("client #" + this.id + " lost the game!");
                    }
                }
            }
            else{
                Server.this.callback.accept("client #" + this.id + " guessed " + data.letterGuess + " correctly");
            }

            // checking if the user guessed category correctly
            if(data.finished){
                data.incorrectGuessesStreak = 0;
                Server.this.callback.accept("client #" + this.id + " correctly guessed " + data.category + " category");

                // update the game data
                if(Objects.equals(data.category, "sports")){
                    data.sportCorrect = true;
                } else if(Objects.equals(data.category, "computers")){
                    data.computerCorrect = true;
                } else{
                    data.foodCorrect = true;
                }

                if(data.sportCorrect && data.computerCorrect && data.foodCorrect){
                    Server.this.callback.accept("client #" + this.id + " won the game!");
                }
            }
        }

        public void run(){
            try {
                this.in = new ObjectInputStream(this.connection.getInputStream());
                this.out = new ObjectOutputStream(this.connection.getOutputStream());
                this.connection.setTcpNoDelay(true);

                while(true) {
                    try {
                        GameData data = (GameData)this.in.readObject();
                        handleGuess(data); // handle the user guess and respond with results
                        out.writeObject(data); // send the result of the user guess
                    } catch (Exception var2) {
                        Server.this.callback.accept("client #" + this.id + " left the server");
                        Server.this.clients.remove(this);
                        return;
                    }
                }

            } catch (Exception var3) {
                System.out.println("Streams not open");
            }
        }
    }
}
