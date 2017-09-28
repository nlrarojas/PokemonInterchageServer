package domain;

import java.io.Serializable;

public class Pokemon implements Serializable{

    private int number;
    private String name;
    private String type1;
    private String type2;
    private String eggGroup1;
    private String eggGroup2;
    private int originalCoach;
    private int coach;
    private int nextEvolution;
    private boolean interchanged;
    private String image;

    public Pokemon(int number, String name, String type1, String type2, String eggGroup1, String eggGroup2, int originalCoach, int coach, int nextEvolution, boolean interchanged, String image) {
        this.number = number;
        this.name = name;
        this.type1 = type1;
        this.type2 = type2;
        this.eggGroup1 = eggGroup1;
        this.eggGroup2 = eggGroup2;
        this.originalCoach = originalCoach;
        this.coach = coach;
        this.nextEvolution = nextEvolution;
        this.interchanged = interchanged;
        this.image = image;
    }

    public Pokemon(int number, String name, String type1, String type2, String eggGroup1, String eggGroup2, int originalCoach, int coach, int nextEvolution, String pImage) {
        this.number = number;
        this.name = name;
        this.type1 = type1;
        this.type2 = type2;
        this.eggGroup1 = eggGroup1;
        this.eggGroup2 = eggGroup2;
        this.originalCoach = originalCoach;
        this.coach = coach;
        this.nextEvolution = nextEvolution;
        this.interchanged = false;
        this.image = pImage;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType1() {
        return type1;
    }

    public void setType1(String type1) {
        this.type1 = type1;
    }

    public String getType2() {
        return type2;
    }

    public void setType2(String type2) {
        this.type2 = type2;
    }

    public String getEggGroup1() {
        return eggGroup1;
    }

    public void setEggGroup1(String eggGroup1) {
        this.eggGroup1 = eggGroup1;
    }

    public String getEggGroup2() {
        return eggGroup2;
    }

    public void setEggGroup2(String eggGroup2) {
        this.eggGroup2 = eggGroup2;
    }

    public int getOriginalCoach() {
        return originalCoach;
    }

    public void setOriginalCoach(int originalCoach) {
        this.originalCoach = originalCoach;
    }

    public int getCoach() {
        return coach;
    }

    public void setCoach(int coach) {
        this.coach = coach;
    }

    public int getNextEvolution() {
        return nextEvolution;
    }

    public void setNextEvolution(int nextEvolution) {
        this.nextEvolution = nextEvolution;
    }

    public boolean isInterchanged() {
        return interchanged;
    }

    public void setInterchanged(boolean interchanged) {
        this.interchanged = interchanged;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Pokemon{" + "number=" + number + ", name=" + name + ", type1=" + type1 + ", type2=" + type2 + ", eggGroup1=" + eggGroup1 + ", eggGroup2=" + eggGroup2 + ", originalCoach=" + originalCoach + ", coach=" + coach + ", nextEvolution=" + nextEvolution + ", interchanged=" + interchanged + ", image=" + image + '}';
    } 
}//fin de la clase
