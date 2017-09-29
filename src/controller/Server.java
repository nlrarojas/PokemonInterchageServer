package controller;

import Business.PokemonXMLBusiness;
import domain.Player;
import domain.Pokemon;
import business.PlayerXMLBusiness;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.io.StreamCorruptedException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdom.JDOMException;
import util.IConstants;

public class Server extends Thread implements IConstants {

    private PokemonXMLBusiness pokemonBusiness;
    private PlayerManager playerManager;
    private PlayerXMLBusiness playerBusiness;
    private InterchangeController interchangeController;
    
    public Server() {
        super("Server Thread");
        try {
            this.pokemonBusiness = new PokemonXMLBusiness();
            this.playerBusiness = new PlayerXMLBusiness();
            this.interchangeController = new InterchangeController(playerBusiness);
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

                System.out.println("\nCliente solicita la función: " + funcionString);
                
                if (funcionString.equalsIgnoreCase(CREATE_NEW_PLAYER)) {
                    createNewPlayer(socket);
                } else if (funcionString.equalsIgnoreCase(LOAD_EXISTING_PLAYER)){
                    int coachNumber = Integer.parseInt(recibir.readLine());
                    loadPlayer(socket, coachNumber);
                } else if (funcionString.equalsIgnoreCase(LOAD_FOREIGN_PLAYER)) {
                    int coachNumber = Integer.parseInt(recibir.readLine());
                    loadPlayer(socket, coachNumber);
                } else if (funcionString.equalsIgnoreCase(LOG_OUT)){
                    try {
                        int coachNumber = Integer.parseInt(recibir.readLine());
                        logOutPlayer(socket, coachNumber);
                    } catch (NumberFormatException | ClassNotFoundException | StreamCorruptedException | SocketException se){
                        System.out.println("Conección cerrada antes de tiempo");
                    }
                } else if (funcionString.equalsIgnoreCase(TRADE_POKEMONS)){
                    tradePokemons(socket);
                } else if (funcionString.equalsIgnoreCase(POKEMON_EVOLUTION)){
                    int pokemonNumber = Integer.parseInt(recibir.readLine());
                    getNextEvolutionForPokemon(socket, pokemonNumber);
                } else if (funcionString.equalsIgnoreCase(UPDATE_POKEDEX)){
                    int coachNumber = Integer.parseInt(recibir.readLine());
                    updatePokedexForCoach(socket, coachNumber);
                }
                socket.close();
            } while (true);
        } catch (IOException | ClassNotFoundException | JDOMException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
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
        ObjectOutputStream objectOut = new ObjectOutputStream(socket.getOutputStream());
        int validation = playerBusiness.validatePlayerExists(coachNumber);        
        if (validation != -1){
            Player player = playerBusiness.getPlayers().get(validation);            
            System.out.println("CoachNumber: " + player.getCoachNumber());
            objectOut.writeObject(player);
            System.out.println("Validación coach: " + validation);                        
        }else{
            objectOut.writeObject(false);
            System.out.println("CoachNumber: no existe el entrenador indicado");
        }                
    }    
    
    private void logOutPlayer(Socket socket, int coachNumber) throws IOException, ClassNotFoundException {
        ObjectInputStream objectIn = new ObjectInputStream(socket.getInputStream());
        Player player = (Player) objectIn.readObject();
        playerBusiness.updatePokedex(player.getCoachNumber(), player.getPokedex());
        System.out.println(player.getCoachNumber());
        //Salvar el estado del jugador
        //
        //
        //
        //
        //
    }
    
    private void tradePokemons(Socket socket) throws IOException, ClassNotFoundException {
        System.out.println("Recibiendo información");
        ObjectInputStream objectIn = new ObjectInputStream(socket.getInputStream());
        Player originCoach = (Player) objectIn.readObject();
        Player foreignCoach = (Player) objectIn.readObject();
        
        Pokemon originPokemon = (Pokemon) objectIn.readObject();
        Pokemon foreignPokemon = (Pokemon) objectIn.readObject();
             
        interchangeController.tradePokemons(originPokemon, foreignPokemon, originCoach.getCoachNumber(), foreignCoach.getCoachNumber()); 
        
        ObjectOutputStream objectOut = new ObjectOutputStream(socket.getOutputStream());
        objectOut.writeObject(getPlayerByIndex(originCoach.getCoachNumber()));
        objectOut.writeObject(getPlayerByIndex(foreignCoach.getCoachNumber()));
    }
    
    private void getNextEvolutionForPokemon(Socket socket, int pokemonNumber) throws IOException {
        ObjectOutputStream objectOut = new ObjectOutputStream(socket.getOutputStream());
        Pokemon nextEvolution = getPokemonByNumber(pokemonNumber);
        objectOut.writeObject(nextEvolution);
    }
    
    private void updatePokedexForCoach(Socket socket, int coachNumber) throws IOException, ClassNotFoundException {
        ObjectInputStream objectIn = new ObjectInputStream(socket.getInputStream());
        Pokemon pokemon = (Pokemon) objectIn.readObject();
        Pokemon pokemonEvolution = (Pokemon) objectIn.readObject();
        
        interchangeController.updateEvolution(pokemon, pokemonEvolution, coachNumber); 
    }
    
    public Player getPlayerByIndex (int coachNumber){
        for (Player player : playerBusiness.getPlayers()) {
            if (player.getCoachNumber() == coachNumber){
                return player;
            }
        }
        return null;
    }
    
    public Pokemon getPokemonByNumber(int pokemonNumber){
        for (Pokemon pokemon : pokemonBusiness.getPokemons()) {
            if (pokemon.getNumber() == pokemonNumber){
                return pokemon;
            }
        }
        return null;
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
