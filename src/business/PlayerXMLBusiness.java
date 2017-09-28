package business;

import domain.Player;
import domain.Pokemon;
import data.PlayerXMLData;
import java.io.IOException;
import org.jdom.JDOMException;
import util.IConstants;
import java.util.List;
/**
 *
 * @author Nelson
 */
public class PlayerXMLBusiness implements IConstants{
    
    private PlayerXMLData playerXMLData;
    
    public PlayerXMLBusiness () throws JDOMException, IOException {
        this.playerXMLData = new PlayerXMLData(PLAYERS_FILE_PATH);
    }
    
    public void insertPlayer(Player player) throws IOException {
        this.playerXMLData.insertPlayer(player);
    }//insertarPokemon

    public List<Player> getPlayers() {
        return playerXMLData.getPlayers();
    }//getEstudiantes

    public void updatePokedex(int coachNumber, Pokemon [] pokedex){
        this.playerXMLData.updatePokedex(coachNumber, pokedex);
    }
    
    public int validatePlayerExists(int searchingNumber) {
        return playerXMLData.validatePlayerExists(searchingNumber);
    }
    
    public boolean existsFile(){
        return playerXMLData.existsFile();
    }
}
