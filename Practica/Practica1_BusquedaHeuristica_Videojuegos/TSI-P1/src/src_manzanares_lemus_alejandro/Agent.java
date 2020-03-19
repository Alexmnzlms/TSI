package src_manzanares_lemus_alejandro;

import core.game.Observation;
import core.game.StateObservation;
import core.player.AbstractPlayer;
import ontology.Types;
import tools.ElapsedCpuTimer;
import tools.Vector2d;

import java.util.ArrayList;
import java.util.Collections;

public class Agent extends AbstractPlayer {
  Vector2d fescala;
  Vector2d portal;

  public Agent(StateObservation stateObs, ElapsedCpuTimer elapsedTimer){
    fescala = new Vector2d(stateObs.getWorldDimension().width / stateObs.getObservationGrid().length ,
            stateObs.getWorldDimension().height / stateObs.getObservationGrid()[0].length);

    //Se crea una lista de observaciones de portales, ordenada por cercania al avatar
    ArrayList<Observation>[] posiciones = stateObs.getPortalsPositions(stateObs.getAvatarPosition());
    //Seleccionamos el portal mas proximo
    portal = posiciones[0].get(0).position;
    portal.x = Math.floor(portal.x / fescala.x);
    portal.y = Math.floor(portal.y / fescala.y);
  }

  public void init(StateObservation stateObs, ElapsedCpuTimer elapsedTimer){

  }

  public ArrayList<Types.ACTIONS> A_estrella (StateObservation stateObs, ElapsedCpuTimer elapsedTimer){
    ArrayList<Node> abiertos = new ArrayList<Node>();
    ArrayList<Node> cerrados = new ArrayList<Node>();
    ArrayList<Types.ACTIONS> path = new ArrayList<Types.ACTIONS>();
    Vector2d pos_inicial = new Vector2d((stateObs.getAvatarPosition().x / fescala.x, stateObs.getAvatarPosition().y / fescala.y)
    Nodo padre = new Nodo(pos_inicial);
    abiertos.add(padre);
    boolean final = false;

    while(!final){

    }
    return path;
  }

  public Types.ACTIONS act( StateObservation stateObs, ElapsedCpuTimer elapsedTimer){

    A_estrella(stateObs,elapsedTimer);
    return Types.ACTIONS.ACTION_NIL;

    /*Vector2d avatar =  new Vector2d(stateObs.getAvatarPosition().x / fescala.x,
            stateObs.getAvatarPosition().y / fescala.y);

    //Probamos las cuatro acciones y calculamos la distancia del nuevo estado al portal.
    Vector2d newPos_up = avatar, newPos_down = avatar, newPos_left = avatar, newPos_right = avatar;
    if (avatar.y - 1 >= 0) {
      newPos_up = new Vector2d(avatar.x, avatar.y-1);
    }
    if (avatar.y + 1 <= stateObs.getObservationGrid()[0].length-1) {
      newPos_down = new Vector2d(avatar.x, avatar.y+1);
    }
    if (avatar.x - 1 >= 0) {
      newPos_left = new Vector2d(avatar.x - 1, avatar.y);
    }
    if (avatar.x + 1 <= stateObs.getObservationGrid().length - 1) {
      newPos_right = new Vector2d(avatar.x + 1, avatar.y);
    }

    //Manhattan distance
    ArrayList<Integer> distances = new ArrayList<Integer>();
    distances.add((int) (Math.abs(newPos_up.x - portal.x) + Math.abs(newPos_up.y-portal.y)));
    distances.add((int) (Math.abs(newPos_down.x - portal.x) + Math.abs(newPos_down.y-portal.y)));
    distances.add((int) (Math.abs(newPos_left.x - portal.x) + Math.abs(newPos_left.y-portal.y)));
    distances.add((int) (Math.abs(newPos_right.x - portal.x) + Math.abs(newPos_right.y-portal.y)));

    // Nos quedamos con el menor y tomamos esa accion.
    int minIndex = distances.indexOf(Collections.min(distances));
    switch (minIndex) {
      case 0:
        return Types.ACTIONS.ACTION_UP;
      case 1:
        return Types.ACTIONS.ACTION_DOWN;
      case 2:
        return Types.ACTIONS.ACTION_LEFT;
      case 3:
        return Types.ACTIONS.ACTION_RIGHT;
      default:
        return Types.ACTIONS.ACTION_NIL;
    }*/

  }
}
