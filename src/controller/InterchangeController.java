package controller;

import Domain.Player;
import Domain.Pokemon;
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
            System.out.println("Intercambiando pokemon de: " + forwardPlayer.getCoachNumber());
            Pokemon [] forwardPlayerPokedex = forwardPlayer.getPokedex();
            for (int i = 0; i < forwardPlayerPokedex.length; i++) {
                if (forwardPlayerPokedex[i].getNumber() == pokemon1.getNumber()){
                    pokemon2.setCoach(forwardCoachNumber);
                    forwardPlayerPokedex[i] = pokemon2;
                    break;
                }
            }
            forwardPlayer.setPokedex(forwardPlayerPokedex);
            playerBusiness.updatePokedex(forwardCoachNumber, forwardPlayer.getPokedex());
            players.set(playerIndex, forwardPlayer);
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
