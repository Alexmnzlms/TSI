package src_manzanares_lemus_alejandro;

import ontology.Types;
import tools.Vector2d;

import java.util.ArrayList;

public class Gem implements Comparable<Gem>{

    public ArrayList<Integer> gems;
    public double peso;

    public Gem(ArrayList<Integer> gems, double peso){
        this.gems = new ArrayList<>(gems);
        this.peso = peso;
    }

    @Override
    public int compareTo(Gem g) {
        if(this.peso < g.peso)
            return -1;
        if(this.peso > g.peso)
            return 1;
        return 0;
    }
}
