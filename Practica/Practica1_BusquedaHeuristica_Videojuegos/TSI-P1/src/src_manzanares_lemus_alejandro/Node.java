package src_manzanares_lemus_alejandro;

import core.game.StateObservation;
import ontology.Types;
import tools.Vector2d;

import java.util.ArrayList;
import java.util.Iterator;

public class Node implements Comparable<Node>{

    private double distancia_objetivo;
    private double coste_camino;
    private double coste_total;
    private ArrayList<Types.ACTIONS> accion = new ArrayList<Types.ACTIONS>();
    private Vector2d posicion;
    private Vector2d orientacion;
    private Vector2d portal;
    private int tipo;

    public Node(int tipo,Vector2d ori ,Vector2d pos, Vector2d port, ArrayList<Types.ACTIONS> acc){
        posicion = pos;
        portal = port;
        coste_camino = acc.size();
        distancia_objetivo = calcular_heuristica();
        coste_total = distancia_objetivo + coste_camino;
        accion = new ArrayList<Types.ACTIONS>(acc);
        orientacion = ori;
        this.tipo = tipo;
    }

    public int compareTo(Node n) {
        if(this.coste_total < n.coste_total)
            return -1;
        if(this.coste_total > n.coste_total)
            return 1;
        if(this.coste_total == n.coste_total){
            if(this.distancia_objetivo < n.distancia_objetivo){
                return  -1;
            }
            if(this.distancia_objetivo > n.distancia_objetivo){
                return 1;
            }
        }
        return 0;
    }

    private double calcular_heuristica(){
        return Math.abs(posicion.x - portal.x) + Math.abs(posicion.y - portal.y);
    }

    public boolean equals(Node o)
    {
        return this.posicion.equals(o.posicion); /*&& this.orientacion.equals(o.orientacion);/*&& this.accion == o.accion*/
    }

    public void update(Node o){
        this.accion = o.accion;
        this.coste_camino = o.coste_camino;
        this.coste_total = o.coste_total;
        this.orientacion = o.orientacion;
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

    public Vector2d getOrientacion(){
        return orientacion;
    }

    @Override
    public String toString() {
        return "Nodo{" +
                "posicion.x=" + posicion.x +
                ", posicion.y=" + posicion.y +
                ", orientacion.x=" + orientacion.x +
                ", orientacion.y=" + orientacion.y +
                ", coste_total=" + coste_total +
                ", coste_camino=" + coste_camino +
                ", distancia_objetivo=" + distancia_objetivo +
                ", accion=" + accion +
                ", tipo=" + tipo +
                "}\n";
    }
}