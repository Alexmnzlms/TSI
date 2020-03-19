package src_manzanares_lemus_alejandro;

import ontology.Types;
import tools.Direction;
import tools.Vector2d;

public class Node{

    private double distancia_objetivo;
    private double coste_camino;
    private double coste_total;
    private Types.ACTIONS accion;
    private Vector2d position;

    public Node(Vector2d pos, int coste,Types.ACTIONS accion)
    {
        position = pos;
        coste_camino = coste;
        distancia_objetivo = calcular_heuristica();
        coste_total = distancia_objetivo + coste_camino;
        this.accion = accion;
    }

    public int compareTo(Node n) {
        if(this.coste_total < n.coste_total)
            return -1;
        if(this.coste_total > n.coste_total)
            return 1;
        return 0;
    }

    public boolean equals(Object o)
    {
        return this.position.equals(((Node)o).position);
    }

}