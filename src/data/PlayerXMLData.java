package data;

import domain.Pokemon;
import domain.Player;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdom.*;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import util.IConstants;

public class PlayerXMLData implements IConstants {

    private Document document;
    private Element root;
    private String filePath;
    private List<Player> playersList;
    private File file;

    public PlayerXMLData(String pFilePath) throws JDOMException, IOException {
        this.filePath = pFilePath;

        playersList = new ArrayList<>();

        if (existsFile()) {
            SAXBuilder saxBuilder = new SAXBuilder();
            saxBuilder.setIgnoringElementContentWhitespace(true);

            this.document = saxBuilder.build(this.filePath);
            this.root = this.document.getRootElement();
        } else {
            this.root = new Element(PLAYERS);
            this.document = new Document(this.root);
        }
    }//constructor

    public void saveXML() throws IOException, FileNotFoundException {
        XMLOutputter xmlOutputter = new XMLOutputter();
        xmlOutputter.output(this.document, new PrintWriter(this.filePath));
    }//guardarXML

    public void insertPlayer(Player player) throws IOException {
        if (validatePlayerExists(player.getCoachNumber()) == -1){
            Element ePlayer = new Element(PLAYER);
            ePlayer.setAttribute(COACH_NUMBER, String.valueOf(player.getCoachNumber()));

            for (Pokemon pokemon : player.getPokedex()) {
                Element ePokemon = new Element(POKEMON);
                ePokemon.setAttribute(NUMBER, String.valueOf(pokemon.getNumber()));

                Element eName = new Element(NAME);
                eName.addContent(pokemon.getName());

                Element eType1 = new Element(TYPE_1);
                eType1.addContent(pokemon.getType1());

                Element eType2 = new Element(TYPE_2);
                eType2.addContent(pokemon.getType2());

                Element eEggGroup1 = new Element(EGG_GROUP_1);
                eEggGroup1.addContent(pokemon.getEggGroup1());

                Element eEggGroup2 = new Element(EGG_GROUP_2);
                eEggGroup2.addContent(pokemon.getEggGroup2());

                Element eOriginalCoach = new Element(ORIGINAL_COACH);
                eOriginalCoach.addContent(String.valueOf(pokemon.getOriginalCoach()));

                Element eCoach = new Element(COACH);
                eCoach.addContent(String.valueOf(pokemon.getCoach()));

                Element eNextEvolution = new Element(NEXT_EVOLUTION);
                eNextEvolution.addContent(String.valueOf(pokemon.getNextEvolution()));

                Element eInterchanged = new Element(INTERCHANGED);
                eInterchanged.addContent(String.valueOf(pokemon.isInterchanged()));

                Element eImage = new Element(IMAGE);
                eImage.addContent(String.valueOf(pokemon.getImage()));

                ePokemon.addContent(eName);
                ePokemon.addContent(eType1);
                ePokemon.addContent(eType2);
                ePokemon.addContent(eEggGroup1);
                ePokemon.addContent(eEggGroup2);
                ePokemon.addContent(eOriginalCoach);
                ePokemon.addContent(eCoach);
                ePokemon.addContent(eNextEvolution);
                ePokemon.addContent(eInterchanged);
                ePokemon.addContent(eImage);

                ePlayer.addContent(ePokemon);
            }

            this.root.addContent(ePlayer);

            saveXML();
        }
    }//insertarPokemon

    public List<Player> getPlayers() {
        List listaElementos = this.root.getChildren();

        for (Object objetoActual : listaElementos) {
            Element elementoActual = (Element) objetoActual;
            List listaPokemones = elementoActual.getChildren();
            int coachNumber = Integer.parseInt(elementoActual.getAttributeValue(COACH_NUMBER));
            Player player = new Player(coachNumber);
            for (Object pokemones : listaPokemones) {
                Element pokemonElement = (Element) pokemones;
                Pokemon pokemon = new Pokemon(
                        Integer.parseInt(pokemonElement.getAttributeValue(NUMBER)),
                        pokemonElement.getChild(NAME).getValue(),
                        pokemonElement.getChild(TYPE_1).getValue(),
                        pokemonElement.getChild(TYPE_2).getValue(),
                        pokemonElement.getChild(EGG_GROUP_1).getValue(),
                        pokemonElement.getChild(EGG_GROUP_2).getValue(),
                        Integer.parseInt(pokemonElement.getChild(ORIGINAL_COACH).getValue()),
                        Integer.parseInt(pokemonElement.getChild(COACH).getValue()),
                        Integer.parseInt(pokemonElement.getChild(NEXT_EVOLUTION).getValue()),
                        Boolean.parseBoolean(pokemonElement.getChild(INTERCHANGED).getValue()),
                        pokemonElement.getChild(IMAGE).getValue());
                player.addPokemon(pokemon);
            }            
            playersList.add(player);
        }//for

        return playersList;
    }//getPlayers

    public void updatePokedex(int coachNumber, Pokemon[] pokedex) {
        System.out.println("Update pokedex");
        List listaElementos = this.root.getChildren();
        int i = 0;

        for (Object objetoActual : listaElementos) {
            Element elementoActual = (Element) objetoActual;
            List listaPokemones = elementoActual.getChildren();

            if (Integer.parseInt(elementoActual.getAttributeValue(COACH_NUMBER)) == coachNumber) {
                for (Object pokemones : listaPokemones) {
                    Element pokemonElement = (Element) pokemones;

                    pokemonElement.setAttribute(NUMBER, String.valueOf(pokedex[i].getNumber()));

                    pokemonElement.getChild(NAME).setText(pokedex[i].getName());
                    pokemonElement.getChild(TYPE_1).setText(pokedex[i].getType1());
                    pokemonElement.getChild(TYPE_2).setText(pokedex[i].getType2());
                    pokemonElement.getChild(EGG_GROUP_1).setText(pokedex[i].getEggGroup1());
                    pokemonElement.getChild(EGG_GROUP_2).setText(pokedex[i].getEggGroup2());
                    pokemonElement.getChild(ORIGINAL_COACH).setText(String.valueOf(pokedex[i].getOriginalCoach()));
                    pokemonElement.getChild(COACH).setText(String.valueOf(pokedex[i].getCoach()));
                    pokemonElement.getChild(NEXT_EVOLUTION).setText(String.valueOf(pokedex[i].getNextEvolution()));
                    pokemonElement.getChild(INTERCHANGED).setText(String.valueOf(pokedex[i].isInterchanged()));
                    pokemonElement.getChild(IMAGE).setText(pokedex[i].getImage());
                    i++;
                }//for            
            }//if               
        }//for
        try {
            saveXML();
            System.out.println("Pokedex salvado");
        } catch (IOException ex) {
            Logger.getLogger(PlayerXMLData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//updatePokedex

    public int validatePlayerExists(int searchingNumber) {
        List listaElementos = this.root.getChildren();
        int i = 0;
        
        for (Object objetoActual : listaElementos) {
            Element elementoActual = (Element) objetoActual;
            int coachNumber = Integer.parseInt(elementoActual.getAttributeValue(COACH_NUMBER));
            if (coachNumber == searchingNumber){
                return i;
            }
            i++;
        }
        return -1;
    }//getEstudiantes
    
    public boolean existsFile() {
        file = new File(this.filePath);
        return file.exists();
    }//existsFile

}// fin de la clase
