package data;

import Domain.Pokemon;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import util.IConstants;

public class PokemonXMLData implements  IConstants{

    private Document document;
    private Element root;
    private String filePath;
    private List<Pokemon> pokemonList;
    private boolean exists;
    private File file;
    
    public PokemonXMLData(String pFilePath) throws JDOMException, IOException {
        this.filePath = pFilePath;      
        
        pokemonList = new ArrayList<>();
        
        if (existsFile()) {                        
            SAXBuilder saxBuilder = new SAXBuilder();
            saxBuilder.setIgnoringElementContentWhitespace(true);

            this.document = saxBuilder.build(this.filePath);
            this.root = this.document.getRootElement();
            this.exists = true;
        } else {
            this.root = new Element(POKEMONES);
            this.document = new Document(this.root);
            this.exists = false;
        }
    }//constructor

    public void saveXML() throws IOException, FileNotFoundException {
        XMLOutputter xmlOutputter = new XMLOutputter();
        xmlOutputter.output(this.document, new PrintWriter(this.filePath));
    }//guardarXML

    public void insertPokemon(Pokemon pokemon) throws IOException {
        if(!exists){
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
            eImage.addContent(pokemon.getImage());

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

            this.root.addContent(ePokemon);

            saveXML();
        }
    }//insertarPokemon

    public List<Pokemon> getPokemones() {
        List listaElementos = this.root.getChildren();

        for (Object objetoActual : listaElementos) {
            Element elementoActual = (Element) objetoActual;
            Pokemon currentPokemon = new Pokemon(
                    Integer.parseInt(elementoActual.getAttributeValue(NUMBER)),
                    elementoActual.getChild(NAME).getValue(),
                    elementoActual.getChild(TYPE_1).getValue(),
                    elementoActual.getChild(TYPE_2).getValue(),
                    elementoActual.getChild(EGG_GROUP_1).getValue(),
                    elementoActual.getChild(EGG_GROUP_2).getValue(),
                    Integer.parseInt(elementoActual.getChild(ORIGINAL_COACH).getValue()),
                    Integer.parseInt(elementoActual.getChild(COACH).getValue()),
                    Integer.parseInt(elementoActual.getChild(NEXT_EVOLUTION).getValue()),
                    Boolean.parseBoolean(elementoActual.getChild(INTERCHANGED).getValue()),
                    elementoActual.getChild(IMAGE).getValue());
            pokemonList.add(currentPokemon);
        }//for

        return pokemonList;
    }//getEstudiantes

    public boolean existsFile(){
        file = new File(this.filePath);
        return file.exists();
    }
    
}// fin de la clase
