package src_manzanares_lemus_alejandro;

import core.game.Observation;
import core.game.StateObservation;
import ontology.Types;
import tools.Direction;
import tools.Vector2d;

import java.util.ArrayList;
import java.util.Collections;

public class Node implements Comparable<Node>{

    private double distancia_objetivo;
    private double coste_camino;
    private double coste_total;
    private ArrayList<Types.ACTIONS> accion = new ArrayList<Types.ACTIONS>();
    private Vector2d posicion;
    private Vector2d orientacion;
    private Vector2d portal;
    private int tipo;
    private StateObservation stateObs;

    public Node(StateObservation stateObs, Vector2d pos, Vector2d port, double coste, ArrayList<Types.ACTIONS> acc) {
        posicion = pos;
        portal = port;
        coste_camino = coste;
        distancia_objetivo = calcular_heuristica();
        coste_total = distancia_objetivo + coste_camino;
        for(int i = 0; i < acc.size(); i++){
            accion.add(acc.get(i));
        }
        this.stateObs = stateObs;
        orientacion = stateObs.getAvatarOrientation();
        tipo = (stateObs.getObservationGrid()[(int) posicion.x][(int) posicion.y]).get(0).itype;
    }

    public int compareTo(Node n) {
        if(this.coste_total < n.coste_total)
            return -1;
        if(this.coste_total > n.coste_total)
            return 1;
        return 0;
    }

    private double calcular_heuristica(){
        return Math.abs(posicion.x - portal.x) + Math.abs(posicion.y - portal.y);
    }

    public boolean equals(Node o)
    {
        return this.posicion.equals(o.posicion) && this.accion == o.accion && orientacion == o.orientacion;
    }

    public double getCoste_total() {
        return coste_total;
    }

    public ArrayList<Types.ACTIONS> getAccion() {
        return accion;
    }

    public double getCoste_camino() {
        return coste_camino;
    }

    public Vector2d getPosicion(){
        return posicion;
    }

    public int getTipo(){
        return tipo;
    }

    public Types.ACTIONS getLast_action(){
        if(accion.size() > 0){
            return accion.get(accion.size()-1);
        }

    }

    @Override
    public String toString() {
        return "Nodo{" +
                "posicion.x=" + posicion.x +
                ", posicion.y=" + posicion.y +
                ", coste_total=" + coste_total +
                ", coste_camino=" + coste_camino +
                ", distancia_objetivo=" + distancia_objetivo +
                ", accion=" + accion +
                ", orientacion.x=" + stateObs.getAvatarOrientation().x +
                ", orientacion.y=" + stateObs.getAvatarOrientation().y +
                ", tipo=" + tipo +
                "}\n";
    }
}