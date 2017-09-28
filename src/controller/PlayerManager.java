package controller;

import Business.PokemonXMLBusiness;
import domain.Player;
import domain.Pokemon;
import business.PlayerXMLBusiness;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.IConstants;

/**
 *
 * @author Nelson
 */
public class PlayerManager implements IConstants{
    private PokemonXMLBusiness pokemonBusiness;
    private PlayerXMLBusiness playerBusiness;
    private List<Pokemon> pokemonList;
    private Player player;
    private Random random;
    
    public PlayerManager(int pPlayerNumber, PlayerXMLBusiness pPokemonBusiness, PokemonXMLBusiness pPlayerBusiness) {
        try {
            this.player = new Player(pPlayerNumber);
            this.pokemonBusiness = pPlayerBusiness;
            this.playerBusiness = pPokemonBusiness;
            this.pokemonList = pokemonBusiness.getPokemons();
            this.random = new Random();
            generatePokedex();
        } catch (IOException ex) {
            Logger.getLogger(PlayerManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void generatePokedex() throws IOException{        
        for (int i = 0; i < AMOUNT_OF_POKEMONS; i++) {
            Pokemon newPokemon = pokemonList.get(random.nextInt(151));
            newPokemon.setOriginalCoach(player.getCoachNumber());
            newPokemon.setCoach(player.getCoachNumber());
            player.addPokemon(newPokemon);
        }
        playerBusiness.insertPlayer(player);
        System.out.println("Creando jugador");
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
