(define (domain p3dom)
   (:requirements :adl)
   (:types
       posicinable localizacion - object
       unidad edificio recurso - posicionable
   )
   (:constants 
      CentroDeMando Barracones - tipoEdificio
      VCE - tipoUnidad
      Minerales Gas - tipoRecurso
   )
   (:predicates
      (en ?p - posicionable ?x - localizacion)
      (camino ?x - localizacion ?y - localizacion)
      (extrae ?u - unidad ?r - recurso)
      (construir ?e - edificio ?r - recurso)
      (esTipoEdificio ?e - edificio ?t - tipoEdificio)
      (esTipoUnidad ?u - unidad ?t - tipoUnidad)
      (esTipoRecurso ?r - recurso ?t - tipoRecurso)
      (necesita ?e - edificio ?t - tipoRecurso)
   )

   (:action Navegar
      :parameters (?u - unidad ?x ?y - localizacion)
      :precondition
         (and
            (camino ?x ?y)
            (en ?u ?x)
            (forall (?u - unidad ?e - edificio)
               (and
                  (not (en ?u ?y))
                  (not (en ?e ?y))
               )
            )
         )
      :effect
         (and
            (en ?u ?y)
            (not (en ?u ?x))
         )
   )

   (:action Asignar
      :parameters (?u - unidad ?r - recurso ?x ?y - localizacion)
      :precondition
         (and
            (en ?u ?x)
            (en ?r ?y)
            (= ?x ?y)
         )
      :effect
         (and
            (extrae ?u ?r)
         )
   )

   (:action Construir
      :parameters (?u - unidad ?e - edificio ?x - localizacion)
      :precondition
         (and
            (forall (?r - recurso)
               (not (extrae ?u ?r))
            )
            (exists (?u1 - unidad ?r - recurso ?t - tipoRecurso)
               (and 
                  (extrae ?u1 ?r)
                  (necesita ?e ?t)
                  (esTipoRecurso ?r ?t)
               )
            )
            (en ?u ?x)
         )
      :effect
         (and
            (en ?e ?x)
         )
   )
)
