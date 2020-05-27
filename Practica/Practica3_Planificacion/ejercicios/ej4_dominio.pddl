(define (domain p3dom)
    (:requirements :adl)
    (:types
        posicionable localizacion - object
        unidad edificio construible tipoRecurso - posicionable
        tipoUnidad tipoEdificio - construible
    )
    (:constants 
        CentroDeMando Barracones Extractor - tipoEdificio
        VCE Marines Segadores - tipoUnidad
        Minerales Gas - tipoRecurso
    )
    (:predicates
        (en ?p - posicionable ?x - localizacion)
        (camino ?x - localizacion ?y - localizacion)
        (extrae ?u - unidad ?tr - tipoRecurso)
        (esTipoEdificio ?e - edificio ?te - tipoEdificio)
        (esTipoUnidad ?u - unidad ?tu - tipoUnidad)
        (necesita ?c - construible ?tr - tipoRecurso)
        (recluta ?te - tipoEdificio ?tu - tipoUnidad)
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
         (or
            (exists (?e - edificio)
               (and 
                  (en ?e ?x)
                  (esTipoEdificio ?e Extractor)
               )
            )
            (not (en Gas ?x))
         )
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
            (forall (?tr -  tipoRecurso)
                (and
                    (imply
                        (necesita ?te ?tr)
                        (exists(?u1 - unidad)
                            (and
                                (extrae ?u1 ?tr)
                                (esTipoUnidad ?u1 VCE)
                            )
                        )
                    )
                )
            )
            (forall (?e1 - edificio) 
                (and
                    (not (en ?e1 ?x))
                )
            )
            (en ?u ?x)
            (esTipoEdificio ?e ?te)
            (esTipoUnidad ?u VCE)
        )
    :effect
        (and
            (en ?e ?x)
        )
    )
    (:action Reclutar
    :parameters (?u - unidad ?tu - tipoUnidad ?te - tipoEdificio ?x - localizacion)
    :precondition
        (and
            (esTipoUnidad ?u ?tu)
            (forall (?tr - tipoRecurso)
                (and
                    (imply
                        (necesita ?tu ?tr)
                        (exists(?u1 - unidad)
                            (and
                                (extrae ?u1 ?tr)
                                (esTipoUnidad ?u1 VCE)      
                            )
                        )
                    )
                )
            )
            (exists (?e - edificio)
                (and
                    (esTipoEdificio ?e ?te)
                    (recluta ?te ?tu)
                    (en ?e ?x)
                )   
            )
        )
    :effect
        (and
            (en ?u ?x)
        )
    )
)
