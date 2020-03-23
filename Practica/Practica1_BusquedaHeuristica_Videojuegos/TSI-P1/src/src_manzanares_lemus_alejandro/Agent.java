package src_manzanares_lemus_alejandro;

import core.game.Observation;
import core.game.StateObservation;
import core.player.AbstractPlayer;
import ontology.Types;
import tools.ElapsedCpuTimer;
import tools.Pair;
import tools.Vector2d;

import java.awt.*;
import java.util.*;

import static java.util.Collections.*;
import static ontology.Types.*;

enum Nivel{
  DS, DC, RS, RC, RD
}

public class Agent extends AbstractPlayer {
  private Nivel estado = Nivel.DC;

  private Vector2d fescala;
  private Vector2d portal;

  private ArrayList<ACTIONS> ruta;
  private boolean ruta_completa = false;
  private ArrayList<Node> cerrados = new ArrayList<Node>();
  private ArrayList<Node> abiertos = new ArrayList<Node>();
  private ArrayList<Observation>[][] obv;
  private ArrayList<ACTIONS> acciones;
  private long tiempo = 0;

  private ArrayList<Vector2d> gemas;
  private int gemas_recogidas = 0;
  private boolean gema_encontrada = false;
  private ArrayList<ArrayList<Double>> matriz_distancias = new ArrayList<>();



  public Agent(StateObservation stateObs, ElapsedCpuTimer elapsedTimer){
    fescala = new Vector2d(stateObs.getWorldDimension().width / stateObs.getObservationGrid().length ,
            stateObs.getWorldDimension().height / stateObs.getObservationGrid()[0].length);

    if(estado == Nivel.DS){
      ArrayList<Observation>[] posiciones = stateObs.getPortalsPositions(stateObs.getAvatarPosition());

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

      //ruta = new ArrayList<ACTIONS>();
      obv = stateObs.getObservationGrid();
      acciones = stateObs.getAvailableActions();
      ruta = A_estrella(portal,stateObs,elapsedTimer);

    } else if(estado == Nivel.DC){
      ArrayList<Observation>[] posiciones = stateObs.getPortalsPositions(stateObs.getAvatarPosition());

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
      gemas = new ArrayList<Vector2d>();
      matriz_distancias = new ArrayList<>();

      calcular_distancias_gemas(stateObs,elapsedTimer);
      for(int i = 0; i < gemas.size(); i++){
        for(int j = 0; j < gemas.size(); j++){
          System.out.print(" " + matriz_distancias.get(i).get(j) + " ");
        }
        System.out.println();
      }
    }

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
    boolean objetivo_alcanzado = false;
    int tipo_dest = (obv[(int) destino.x][(int) destino.y]).get(0).itype;

    System.out.println("(" + stateObs.getAvatarPosition().x/fescala.x + ","
            + stateObs.getAvatarPosition().y/fescala.y + ")"
            + " -> " + "(" + destino.x + "," + destino.y + ")");

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

    }while((actual.getPosicion().x != destino.x || actual.getPosicion().y != destino.y) && elapsedTimer.remainingTimeMillis() > 10);

    tiempo += elapsedTimer.elapsedMillis();

    if(actual.getPosicion().x == destino.x && actual.getPosicion().y == destino.y){
      ruta_completa = true;
    }

    if((obv[(int) actual.getPosicion().x][(int) actual.getPosicion().y]).size() > 0){
      if((obv[(int) actual.getPosicion().x][(int) actual.getPosicion().y]).get(0).itype == 6){
        gema_encontrada = true;
        System.out.println("Gema encontrada");
      }
    }

    if(ruta_completa){
      path = new ArrayList<ACTIONS>(actual.getAccion());
      System.out.println("Camino encontrado");
      ruta_completa = false;
    } else {
      path = new ArrayList<ACTIONS>();
      path.add(ACTIONS.ACTION_NIL);
    }

    return path;
  }

  public double A_estrella_pos (Node padre, Vector2d destino, StateObservation stateObs, ElapsedCpuTimer elapsedTimer){
    abiertos.clear();
    cerrados.clear();
    abiertos.add(padre);
    ArrayList<ACTIONS> path = new ArrayList<ACTIONS>();
    Node actual;
    boolean encontrado = false;
    boolean salir = false;

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

    }while((actual.getPosicion().x != destino.x || actual.getPosicion().y != destino.y) && elapsedTimer.remainingTimeMillis() > 10);

    return actual.getCoste_total();


  }

  public void calcular_posicion_gemas(StateObservation stateObs, ElapsedCpuTimer elapsedTimer) {
    ArrayList<Observation>[] g = stateObs.getResourcesPositions(stateObs.getAvatarPosition());
    gemas.add(new Vector2d(stateObs.getAvatarPosition().x/fescala.x,stateObs.getAvatarPosition().y/fescala.y));
    for(int i = 0; i < g[0].size(); i++){
      gemas.add(new Vector2d(g[0].get(i).position.x / fescala.x ,g[0].get(i).position.y/fescala.x));
    }
    gemas.add(new Vector2d(portal.x,portal.y));

  }

  public void calcular_distancias_gemas(StateObservation stateObs, ElapsedCpuTimer elapsedTimer){
    calcular_posicion_gemas(stateObs,elapsedTimer);

    for(int i = 0; i < gemas.size()+2; i++){
      matriz_distancias.add(new ArrayList<>());
    }

    for(int i = 0; i < gemas.size(); i++){
      for(int j = 0; j < gemas.size(); j++){
        if(i != j){
          Node gema = new Node(6, new Vector2d(1,0), gemas.get(i), gemas.get(j), new ArrayList<ACTIONS>());
          matriz_distancias.get(i).add(A_estrella_pos(gema, gemas.get(j), stateObs, elapsedTimer));
        } else {
          matriz_distancias.get(i).add(1000.0);
        }
      }
    }

    ArrayList<Vector2d> objetivos = new ArrayList<Vector2d>();

  }

  public ACTIONS act( StateObservation stateObs, ElapsedCpuTimer elapsedTimer){
    if(estado == Nivel.DS){

      if(!ruta_completa && ruta.size() == 0){
        ruta = A_estrella(portal,stateObs,elapsedTimer);
        System.out.println("Tiempo de A*: " + tiempo + "ms.");
      }

      ACTIONS accion = ruta.get(0);
      System.out.println(ruta.get(0));
      ruta.remove(0);
      return accion;

    } else if (estado == Nivel.DC){

      if(!ruta_completa && ruta.size() == 0){
        System.out.println(gemas_recogidas);
        abiertos.clear();
        cerrados.clear();
        Vector2d pos = new Vector2d(stateObs.getAvatarPosition().x / fescala.x, stateObs.getAvatarPosition().y / fescala.y);
        Vector2d ori = new Vector2d(stateObs.getAvatarOrientation());
        Node padre = new Node(1, ori, pos, portal, new ArrayList<ACTIONS>());
        abiertos.add(padre);
        if(gemas_recogidas < 10){
          ruta = A_estrella(gemas.get(gemas_recogidas),stateObs,elapsedTimer);
          if(gema_encontrada){
            gemas_recogidas++;
            gema_encontrada = false;
          }
        } else {
          ruta = A_estrella(portal,stateObs,elapsedTimer);
        }

      }
      if(ruta_completa && ruta.size() == 0){
        System.out.println("Tiempo de A*: " + tiempo + "ms.");
      }

      ACTIONS accion = ruta.get(0);
      //System.out.println(ruta.get(0));
      ruta.remove(0);

      return accion;

    }

    return ACTIONS.ACTION_NIL;
  }
}
