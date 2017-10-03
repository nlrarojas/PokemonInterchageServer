package controller;

import domain.Player;
import domain.Pokemon;
import business.PlayerXMLBusiness;
import java.util.List;

/**
 *
 * @author Nelson
 */
public class InterchangeController {

    private List<Player> players;
    private PlayerXMLBusiness playerBusiness;
    private int playerIndex;
    
    public InterchangeController(PlayerXMLBusiness pPlayerBusiness) {
        this.playerBusiness = pPlayerBusiness;
        this.players = playerBusiness.getPlayers();
        this.playerIndex = 0;
    }
    
    public void tradePokemons (Pokemon pokemon1, Pokemon pokemon2, int originCoachNumber, int forwardCoachNumber){       
        tradePokemonsKernel(pokemon1, pokemon2, originCoachNumber, forwardCoachNumber);
        tradePokemonsKernel(pokemon2, pokemon1, forwardCoachNumber, originCoachNumber);
    }
    
    public void tradePokemonsKernel (Pokemon pokemon1, Pokemon pokemon2, int originCoachNumber, int forwardCoachNumber){       
        Player forwardPlayer = getPlayer(forwardCoachNumber);        
        System.out.println("Intercambio pokemon");
                
        if(forwardPlayer != null){
            Pokemon [] forwardPlayerPokedex = forwardPlayer.getPokedex();
            for (int i = 0; i < forwardPlayerPokedex.length; i++) {
                if (forwardPlayerPokedex[i].getNumber() == pokemon2.getNumber()){
                    System.out.println("Pokemon encontrado");
                    pokemon2.setCoach(forwardCoachNumber);
                    forwardPlayerPokedex[i] = pokemon1;
                    break;
                }
            }
            forwardPlayer.setPokedex(forwardPlayerPokedex);
            players.set(playerIndex, forwardPlayer);
            playerBusiness.updatePokedex(forwardCoachNumber, forwardPlayer.getPokedex());            
        }
    }
    
    public void updateEvolution(Pokemon pokemon, Pokemon pokemonEvolution, int coachNumber) {
        System.out.println("cont0");
        Player player = getPlayer(coachNumber);
        System.out.println("Evolucion");
        pokemonEvolution.setCoach(pokemon.getCoach());
        pokemonEvolution.setOriginalCoach(pokemon.getOriginalCoach());
        System.out.println("cont1");
        if (player != null) {
            Pokemon [] pokedex = player.getPokedex();
            for (int i = 0; i < pokedex.length; i++) {                            
                if (pokedex[i].getNumber() == pokemon.getNumber()){
                    pokedex[i] = pokemonEvolution;
                    break;
                }
            }
            System.out.println("Cont2");
            player.setPokedex(pokedex);
            players.set(playerIndex, player);
            playerBusiness.updatePokedex(coachNumber, player.getPokedex());
        }
    }
    
    public Player getPlayer(int coachNumber){
        this.playerIndex = 0;
        for (Player player : players){
            if (player.getCoachNumber() == coachNumber){
                return player;
            }
            playerIndex++;
        }
        return null;
    }   
}
