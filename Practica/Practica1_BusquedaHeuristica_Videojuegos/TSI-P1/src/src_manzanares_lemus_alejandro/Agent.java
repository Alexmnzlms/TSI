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

    System.out.println("Portal_x:");
    System.out.println(portal.x);
    System.out.println("Portal_y:");
    System.out.println(portal.y);
  }

  public void init(StateObservation stateObs, ElapsedCpuTimer elapsedTimer){

  }

  public ArrayList<Types.ACTIONS> A_estrella (Vector2d destino, StateObservation stateObs, ElapsedCpuTimer elapsedTimer){
    ArrayList<Node> abiertos = new ArrayList<Node>();
    ArrayList<Node> cerrados = new ArrayList<Node>();
    ArrayList<Types.ACTIONS> path = new ArrayList<Types.ACTIONS>();
    Vector2d pos_inicial = new Vector2d(stateObs.getAvatarPosition().x / fescala.x, stateObs.getAvatarPosition().y / fescala.y);
    Vector2d new_pos;
    Node padre = new Node(stateObs, pos_inicial,destino,0,Types.ACTIONS.ACTION_RIGHT);
    Node actual;
    abiertos.add(padre);
    boolean fin= false;

    while(!fin){
      actual = abiertos.get(0);
      ArrayList<Types.ACTIONS> acciones = stateObs.getAvailableActions();
      for (int i = 0; i < acciones.size(); i++){
        System.out.println(acciones.get(i));
        if(acciones.get(i) != Types.ACTIONS.ACTION_USE) {
          //Calcular aqui la nueva posicion en funcion de la accion
          new_pos = new Vector2d(stateObs.getAvatarPosition().x / fescala.x, stateObs.getAvatarPosition().y / fescala.y);
          if(acciones.get(i) == Types.ACTIONS.ACTION_UP){
            new_pos.y = new_pos.y + 1;
          } else if(acciones.get(i) == Types.ACTIONS.ACTION_DOWN){
            new_pos.y = new_pos.y - 1;
          } else if(acciones.get(i) == Types.ACTIONS.ACTION_RIGHT){
            new_pos.x = new_pos.x + 1;
          } else if(acciones.get(i) == Types.ACTIONS.ACTION_LEFT){
            new_pos.x = new_pos.x - 1;
          }

          if(acciones.get(i) == actual.getAccion()) {
            System.out.println("misma accion");
            cerrados.add(new Node(stateObs, new_pos, destino, actual.getCoste_camino()+1.0,acciones.get(i)));
          } else {
            System.out.println("distinta accion");
            cerrados.add(new Node(stateObs, new_pos, destino, actual.getCoste_camino()+2.0,acciones.get(i)));
          }
        }
      }
      for(int i = 0; i < cerrados.size(); i++) {
        System.out.println("Accion del nodo:");
        System.out.println(cerrados.get(i).getAccion());
        System.out.println("Coste del nodo:");
        System.out.println(cerrados.get(i).getCoste_total());
      }
      fin = true;
    }
    return path;
  }

  public Types.ACTIONS act( StateObservation stateObs, ElapsedCpuTimer elapsedTimer){
    A_estrella(portal,stateObs,elapsedTimer);
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
