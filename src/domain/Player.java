package Domain;

import java.io.Serializable;

/**
 *
 * @author Nelson
 */
public class Player implements Serializable{
    private int coachNumber;
    private Pokemon [] pokedex;
    private int position;

    public Player(int coachNumber) {
        this.coachNumber = coachNumber;
        this.pokedex = new Pokemon[6];
        this.position = 0;
    }
    
    public boolean addPokemon(Pokemon pokemon){
        if(position < 6){
            pokedex[position++] = pokemon;
            return true;
        }
        return false;
    }

    public int getCoachNumber() {
        return coachNumber;
    }

    public void setCoachNumber(int coachNumber) {
        this.coachNumber = coachNumber;
    }

    public Pokemon[] getPokedex() {
        return pokedex;
    }

    public void setPokedex(Pokemon[] pokedex) {
        this.pokedex = pokedex;
    }
}
