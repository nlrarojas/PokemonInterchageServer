package Business;

import data.PokemonXMLData;
import domain.Pokemon;
import java.io.IOException;
import java.util.List;
import org.jdom.JDOMException;
import util.IConstants;

public class PokemonXMLBusiness implements IConstants{

    private PokemonXMLData pokemonXMLData;

    public PokemonXMLBusiness() throws JDOMException, IOException {
        this.pokemonXMLData = new PokemonXMLData(POKEMONS_FILE_PATH);
    }

    public void insertPokemon(Pokemon pokemon) throws IOException {
        this.pokemonXMLData.insertPokemon(pokemon);
    }

    public List<Pokemon> getPokemons() {
        return this.pokemonXMLData.getPokemones();
    }
    
    public boolean existsFile(){
        return this.pokemonXMLData.existsFile();
    }
}// fin de la clase
