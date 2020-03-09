package src_manzanares_lemus_alejandro;

import ontology.Types;
import tools.Direction;
import tools.Vector2d;

public class Node{

    public double totalCost;
    public double estimatedCost;
    public Node parent;
    public Vector2d position;
    public 
    public int id;

    public Node(Vector2d pos, int coste)
    {
        estimatedCost = 0.0f;
        totalCost = 1.0f;
        parent = null;
        position = pos;
        id = ((int)(position.x) * 100 + (int)(position.y));
    }

    public int compareTo(Node n) {
        if(this.estimatedCost + this.totalCost < n.estimatedCost + n.totalCost)
            return -1;
        if(this.estimatedCost + this.totalCost > n.estimatedCost + n.totalCost)
            return 1;
        return 0;
    }

    public boolean equals(Object o)
    {
        return this.position.equals(((Node)o).position);
    }


    public void setMoveDir(Node pre) {

        Direction action = Types.DNONE;

        if(pre.position.x < this.position.x)
            action = Types.DRIGHT;
        if(pre.position.x > this.position.x)
            action = Types.DLEFT;

        if(pre.position.y < this.position.y)
            action = Types.DDOWN;
        if(pre.position.y > this.position.y)
            action = Types.DUP;

        this.comingFrom = new Vector2d(action.x(), action.y());
    }
}