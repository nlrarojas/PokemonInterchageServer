package controller;

import Business.PokemonXMLBusiness;
import Domain.Player;
import Domain.Pokemon;
import business.PlayerXMLBusiness;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdom.JDOMException;
import util.IConstants;

public class Server extends Thread implements IConstants {

    private PokemonXMLBusiness pokemonBusiness;
    private PlayerManager playerManager;
    private PlayerXMLBusiness playerBusiness;
    
    public Server() {
        super("Server Thread");
        try {
            this.pokemonBusiness = new PokemonXMLBusiness();
            this.playerBusiness = new PlayerXMLBusiness();
        } catch (JDOMException | IOException ex) {
            System.err.println(ex.getMessage());
        }
    }//constructor

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Iniciado");
            createPrincipalFile();
            do {
                Socket socket = serverSocket.accept();

                PrintStream enviar = new PrintStream(socket.getOutputStream()); //output
                BufferedReader recibir = new BufferedReader(new InputStreamReader(socket.getInputStream()));//input

                String funcionString = recibir.readLine();// primer input
                enviar.println("Este es el servidor (Intercambio de Pokemones)");//primer output
                enviar.println(SERVER_READY);// segundo output

                System.out.println("Cliente solicita la función: " + funcionString);

                if (funcionString.equalsIgnoreCase(CREATE_NEW_PLAYER)) {
                    createNewPlayer(socket);
                } else if (funcionString.equalsIgnoreCase(LOAD_EXISTING_PLAYER)){
                    int coachNumber = Integer.parseInt(recibir.readLine());
                    loadPlayer(socket, coachNumber);
                } else if (funcionString.equalsIgnoreCase(LOAD_FOREIGN_PLAYER)) {
                    int coachNumber = Integer.parseInt(recibir.readLine());
                    loadPlayer(socket, coachNumber);
                }
                socket.close();
            } while (true);
        } catch (IOException | JDOMException ex) {
            System.out.println(ex.getMessage());
        }
    }//run

    private void createNewPlayer(Socket socket) throws IOException, JDOMException {
        System.out.println("Tara");        
        ObjectOutputStream objectOut = new ObjectOutputStream(socket.getOutputStream());     
        playerManager = new PlayerManager(playerBusiness.getPlayers().size() + 1, playerBusiness, pokemonBusiness);
        Player newPlayer = playerManager.getPlayer();
        
        objectOut.writeObject(newPlayer);
        System.out.println("CoachNumber: " + newPlayer.getCoachNumber());
    }//funcionCreateNewPlayer
    
    private void loadPlayer(Socket socket, int coachNumber) throws IOException, JDOMException {
        System.out.println("Tara");        
        ObjectOutputStream objectOut = new ObjectOutputStream(socket.getOutputStream());
        int validation = playerBusiness.validatePlayerExists(coachNumber);
        Player player = null;
        if (validation != -1){
            System.out.println("Validación coach: " + validation);
            player = playerBusiness.getPlayers().get(validation);
            System.out.println("CoachNumber: " + player.getCoachNumber());
        }else{
            System.out.println("CoachNumber: no existe el entrenador indicado");
        }
        objectOut.writeObject(player);        
    }    
    
    private void log(String msg) {
        System.out.println(msg);
    }

    public boolean createPrincipalFile() {
        BufferedReader br = null;
        try {
            System.out.println("Creando archivo general de pokemones");
            br = new BufferedReader(new FileReader(POKEMON_LIST_FILE_PATH));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            String[] pokemonAtributes;
            while (line != null) {
                pokemonAtributes = line.split(";");
                Pokemon pokemon = new Pokemon(Integer.parseInt(pokemonAtributes[0]), pokemonAtributes[1], pokemonAtributes[2], pokemonAtributes[3],
                        pokemonAtributes[4], pokemonAtributes[5], -1, -1, Integer.parseInt(pokemonAtributes[6]), PREFIX_PATH + pokemonAtributes[1] + FORMAT);
                pokemonBusiness.insertPokemon(pokemon);
                line = br.readLine();
            }
        } catch (FileNotFoundException ex) {
            System.err.println(ex.getMessage());
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        } finally {
            try {
                br.close();
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return true;
    }
}
