package src_manzanares_lemus_alejandro;

import core.game.Observation;
import core.game.StateObservation;
import ontology.Types;
import tools.Direction;
import tools.Vector2d;

import java.util.ArrayList;

public class Node implements Comparable<Node>{

    private double distancia_objetivo;
    private double coste_camino;
    private double coste_total;
    private ArrayList<Types.ACTIONS> accion;
    private Vector2d posicion;
    private Vector2d orientacion;
    private Vector2d portal;
    private StateObservation stateObs;

    public Node(StateObservation stateObs, Vector2d pos, Vector2d port, double coste, ArrayList<Types.ACTIONS> accion) {
        posicion = pos;
        portal = port;
        coste_camino = coste;
        distancia_objetivo = calcular_heuristica();
        coste_total = distancia_objetivo + coste_camino;
        this.accion = accion;
        this.stateObs = stateObs;
        orientacion = stateObs.getAvatarOrientation();
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

    @Override
    public String toString() {
        return "Nodo{" +
                "posicion.x=" + posicion.x +
                ", posicion.y=" + posicion.y +
                ", coste_camino=" + coste_camino +
                ", distancia_objetivo=" + distancia_objetivo +
                ", accion=" + accion +
                ", orientacion.x=" + stateObs.getAvatarOrientation().x +
                ", orientacion.y=" + stateObs.getAvatarOrientation().y +
                ", grid=" + (stateObs.getObservationGrid()[(int) posicion.x][(int) posicion.y]).toString() +
                "}\n";
    }
}