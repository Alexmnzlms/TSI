(define (domain p3dom)
   (:requirements :adl)
   (:types
       posicionable localizacion - object
       unidad edificio tipoUnidad tipoEdificio tipoRecurso - posicionable
   )
   (:constants 
      CentroDeMando Barracones - tipoEdificio
      VCE - tipoUnidad
      Minerales Gas - tipoRecurso
   )
   (:predicates
      (en ?p - posicionable ?x - localizacion)
      (camino ?x - localizacion ?y - localizacion)
      (extrae ?u - unidad ?tr - tipoRecurso)
      (esTipoEdificio ?e - edificio ?t - tipoEdificio)
      (esTipoUnidad ?u - unidad ?t - tipoUnidad)
      (necesita ?te - tipoEdificio ?tr - tipoRecurso)
   )

   (:action Navegar
   :parameters (?u - unidad ?x ?y - localizacion)
   :precondition
      (and
         (camino ?x ?y)
         (en ?u ?x)
      )
   :effect
      (and
         (en ?u ?y)
         (not (en ?u ?x))
      )
   )

   (:action Asignar
   :parameters (?u - unidad ?tr - tipoRecurso ?x - localizacion)
   :precondition
      (and
         (en ?u ?x)
         (en ?tr ?x)
      )
   :effect
      (and
         (extrae ?u ?tr)
      )
   )

   (:action Construir
   :parameters (?u - unidad ?e - edificio ?te - tipoEdificio ?x - localizacion)
   :precondition
      (and
         (forall (?tr - tipoRecurso)
            (not (extrae ?u ?tr))
         )
         (exists (?u1 - unidad ?tr - tipoRecurso)
            (and 
               (extrae ?u1 ?tr)
               (necesita ?te ?tr)
            )
         )
         (en ?u ?x)
         (esTipoEdificio ?e ?te)
      )
   :effect
      (and
         (en ?e ?x)
      )
   )
)
