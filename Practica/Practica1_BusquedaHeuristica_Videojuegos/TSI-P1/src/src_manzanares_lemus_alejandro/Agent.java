package src_manzanares_lemus_alejandro;

import core.game.Observation;
import core.game.StateObservation;
import core.player.AbstractPlayer;
import ontology.Types;
import tools.ElapsedCpuTimer;
import tools.Vector2d;

import java.util.*;

import static java.util.Collections.*;
import static ontology.Types.*;

public class Agent extends AbstractPlayer {
  private Vector2d fescala;
  private Vector2d portal;
  private ArrayList<ACTIONS> ruta;
  private boolean ruta_completa = false;
  private ArrayList<Node> cerrados = new ArrayList<Node>();
  private ArrayList<Node> abiertos = new ArrayList<Node>();
  private ArrayList<Observation>[][] obv;
  private ArrayList<ACTIONS> acciones;
  private long tiempo = 0;

  public Agent(StateObservation stateObs, ElapsedCpuTimer elapsedTimer){
    fescala = new Vector2d(stateObs.getWorldDimension().width / stateObs.getObservationGrid().length ,
            stateObs.getWorldDimension().height / stateObs.getObservationGrid()[0].length);

    //Se crea una lista de observaciones de portales, ordenada por cercania al avatar
    ArrayList<Observation>[] posiciones = stateObs.getPortalsPositions(stateObs.getAvatarPosition());
    //Seleccionamos el portal mas proximo
    portal = posiciones[0].get(0).position;
    portal.x = Math.floor(portal.x / fescala.x);
    portal.y = Math.floor(portal.y / fescala.y);

    System.out.print("Mundo: ");
    System.out.print(stateObs.getObservationGrid().length);
    System.out.print(" x ");
    System.out.println(stateObs.getObservationGrid()[0].length);

    Vector2d pos = new Vector2d(stateObs.getAvatarPosition().x / fescala.x, stateObs.getAvatarPosition().y / fescala.y);
    Vector2d ori = new Vector2d(stateObs.getAvatarOrientation());
    Node padre = new Node(1, ori, pos, portal, new ArrayList<ACTIONS>());
    abiertos.add(padre);

    ruta = new ArrayList<ACTIONS>();
    obv = stateObs.getObservationGrid();
    acciones = stateObs.getAvailableActions();
    //ruta = A_estrella(portal,stateObs,elapsedTimer);
  }

  public void init(StateObservation stateObs, ElapsedCpuTimer elapsedTimer){

  }

  public boolean misma_orientacion_accion(Vector2d orientacion, ACTIONS accion){
    if(accion == ACTIONS.ACTION_RIGHT && orientacion.x == 1.0){
      return true;
    } else if (accion == ACTIONS.ACTION_LEFT && orientacion.x == -1.0){
      return true;
    } else if (accion == ACTIONS.ACTION_UP && orientacion.y == -1.0){
      return true;
    } else if (accion == ACTIONS.ACTION_DOWN && orientacion.y == 1.0){
      return true;
    } else {
      return false;
    }
  }

  public ArrayList<ACTIONS> A_estrella (Vector2d destino, StateObservation stateObs, ElapsedCpuTimer elapsedTimer){
    ArrayList<ACTIONS> path = new ArrayList<ACTIONS>();
    Node actual;
    boolean encontrado = false;
    boolean salir = false;

    //sort(abiertos);
    do{
      actual = abiertos.get(0);
      cerrados.add(actual);
      abiertos.remove(0);

      Iterator<ACTIONS> it_acc = acciones.iterator();
      while(it_acc.hasNext()){
        ACTIONS accion = it_acc.next();
        Vector2d new_pos = new Vector2d(actual.getPosicion());
        Vector2d new_ori = new Vector2d(actual.getOrientacion());

        if (accion != ACTIONS.ACTION_USE){

          if (accion == ACTIONS.ACTION_RIGHT) {
            new_pos.x = new_pos.x + 1;
          } else if (accion == ACTIONS.ACTION_LEFT) {
            new_pos.x = new_pos.x - 1;
          } else if (accion == ACTIONS.ACTION_UP) {
            new_pos.y = new_pos.y - 1;
          } else if (accion == ACTIONS.ACTION_DOWN) {
            new_pos.y = new_pos.y + 1;
          }

          int tipo;
          if((obv[(int) new_pos.x][(int) new_pos.y]).size() > 0){
            tipo = (obv[(int) new_pos.x][(int) new_pos.y]).get(0).itype;
          } else {
            tipo = 2;
          }

          if(tipo != 0) {
            ArrayList<ACTIONS> camino_actual = new ArrayList<ACTIONS>(actual.getAccion());
            camino_actual.add(accion);

            if(!misma_orientacion_accion(new_ori,accion)){
              camino_actual.add(accion);
            }

            if (accion == ACTIONS.ACTION_RIGHT) {
              new_ori.x = 1;
              new_ori.y = 0;
            } else if (accion == ACTIONS.ACTION_LEFT) {
              new_ori.x = -1;
              new_ori.y = 0;
            } else if (accion == ACTIONS.ACTION_UP) {
              new_ori.x = 0;
              new_ori.y = -1;
            } else if (accion == ACTIONS.ACTION_DOWN) {
              new_ori.x = 0;
              new_ori.y = 1;
            }

            Node hijo = new Node(tipo, new_ori, new_pos, destino, camino_actual);
            encontrado = false;
            salir = false;

            Iterator<Node> it_cerrados = cerrados.iterator();
            while(it_cerrados.hasNext() && !encontrado){
              Node i = it_cerrados.next();
              if(hijo.equals(i))
                encontrado = true;
            }

            if(!encontrado){
              Iterator<Node> it_abiertos = abiertos.iterator();
              while(it_abiertos.hasNext() && !salir){
                Node j = it_abiertos.next();
                if(hijo.equals(j) && hijo.getCoste_camino() < j.getCoste_camino()){
                  j.update(hijo);
                  salir = true;
                }
              }

              if (!salir) {
                abiertos.add(hijo);
              }
            }
          }
        }
      }
      sort(abiertos);

    }while(actual.getTipo() != 5 && elapsedTimer.remainingTimeMillis() > 10);

    tiempo += elapsedTimer.elapsedMillis();

    if(actual.getTipo() == 5){
      ruta_completa = true;
    }

    if(ruta_completa){
      path = new ArrayList<ACTIONS>(actual.getAccion());
    } else {
      path = new ArrayList<ACTIONS>();
      path.add(ACTIONS.ACTION_NIL);
    }

    return path;
  }

  public ACTIONS act( StateObservation stateObs, ElapsedCpuTimer elapsedTimer){
    if(!ruta_completa && ruta.size() == 0){
      ruta = A_estrella(portal,stateObs,elapsedTimer);
    }
    if(ruta_completa && ruta.size() == 1){
      System.out.println("Tiempo de A*: " + tiempo + "ms.");
    }
    ACTIONS accion = ruta.get(0);
    System.out.println(ruta.get(0));
    ruta.remove(0);
    return accion;
  }
}
