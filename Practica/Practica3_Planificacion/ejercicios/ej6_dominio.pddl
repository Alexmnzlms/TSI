(define (domain p3dom)
    (:requirements :adl :fluents)
    (:types
        posicionable localizacion investigacion - object
        unidad edificio construible tipoRecurso - posicionable
        tipoUnidad tipoEdificio - construible
    )
    (:constants 
        CentroDeMando Barracones Extractor BahiaDeInvestigacion Deposito - tipoEdificio
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
        (necesitaInv ?i - investigacion ?tr - tipoRecurso)
        (investigado ?i - investigacion)
        (permite ?i - investigacion ?tu - tipoUnidad)   
    )
    (:functions
        (coste ?tr - tipoRecurso ?c - construible)
        (costeInv ?tr - tipoRecurso ?i - investigacion)
        (incrementaLimite ?te - tipoEdificio)
        (limite ?tr - tipoRecurso)
        (almacenado ?tr - tipoRecurso)

    )

    (:action Navegar
        :parameters (?u - unidad ?x ?y - localizacion)
        :precondition
            (and
                (camino ?x ?y)
                (en ?u ?x)
                (forall (?tr - tipoRecurso)
                    (not (extrae ?u ?tr))
                )
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
        (forall (?tr1 - tipoRecurso)
            (not (extrae ?u ?tr1))
        )
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

   (:action Desasignar
   :parameters (?u - unidad ?tr - tipoRecurso ?x - localizacion)
   :precondition
      (and
        (en ?u ?x)
        (extrae ?u ?tr)
        (en ?tr ?x)
      )
   :effect
      (and
        (not (extrae ?u ?tr))
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
                        (and
                            (exists(?u1 - unidad)
                                (and
                                    (extrae ?u1 ?tr)
                                    (esTipoUnidad ?u1 VCE)      
                                )
                            )
                            (>
                                (almacenado ?tr)
                                (coste ?tr ?te)
                            )
                            (>
                                (limite ?tr)
                                (coste ?tr ?te)
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
            (when (esTipoEdificio ?e Deposito)
                (and
                    (increase
                        (limite Gas)
                        (incrementaLimite Deposito)
                    )
                    (increase
                        (limite Minerales)
                        (incrementaLimite Deposito)
                    )
                
                )
            )
            (decrease
                (almacenado Gas)
                (coste Gas ?te)     
            )
            (decrease
                (almacenado Minerales)
                (coste Minerales ?te)     
            )
        )
    )
    (:action Reclutar
    :parameters (?u - unidad ?tu - tipoUnidad ?te - tipoEdificio ?x - localizacion)
    :precondition
        (and
            (esTipoUnidad ?u ?tu)
            (forall (?y - localizacion)
                (not (en ?u ?y))
            )
            (forall (?tr - tipoRecurso)
                (and
                    (imply
                        (necesita ?tu ?tr)
                        (and
                            (exists(?u1 - unidad)
                                (and
                                    (extrae ?u1 ?tr)
                                    (esTipoUnidad ?u1 VCE)      
                                )
                            )
                            (>
                                (almacenado ?tr)
                                (coste ?tr ?tu)
                            )
                            (>
                                (limite ?tr)
                                (coste ?tr ?tu)
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
            (forall (?i - investigacion)
                (and
                    (imply
                        (permite ?i ?tu)
                        (investigado ?i)
                    )
                )
            )
        )
    :effect
        (and
            (en ?u ?x)
            (decrease
                (almacenado Gas)
                (coste Gas ?tu)     
            )
            (decrease
                (almacenado Minerales)
                (coste Minerales ?tu)     
            )
        )
    )
    (:action Investigar
    :parameters (?i - investigacion)
    :precondition
        (and
            (forall (?tr - tipoRecurso)
                (and
                    (imply
                        (necesitaInv ?i ?tr)
                        (and
                            (exists(?u1 - unidad)
                                (and
                                    (extrae ?u1 ?tr)
                                    (esTipoUnidad ?u1 VCE)      
                                )
                            )
                            (>
                                (almacenado ?tr)
                                (costeInv ?tr ?i)
                            )
                            (>
                                (limite ?tr)
                                (costeInv ?tr ?i)
                            )
                        )
                    )
                )
            )
            (exists (?x - localizacion ?e - edificio) 
                (and
                    (en ?e ?x)
                    (esTipoEdificio ?e BahiaDeInvestigacion)
                )
            )
        )
    :effect
        (and
            (investigado ?i)
            (decrease
                (almacenado Gas)
                (costeInv Gas ?i)     
            )
            (decrease
                (almacenado Minerales)
                (costeInv Minerales ?i)     
            )
        )
    )
    (:action Recolectar
    :parameters (?u - unidad ?tr - tipoRecurso ?x - localizacion)
    :precondition
        (and
            (extrae ?u ?tr)
            (en ?u ?x)
            (>
                (limite ?tr)
                (almacenado ?tr)
            )
        )
    :effect
        (and
            (increase
                (almacenado ?tr)
                25
            )
        )
    )
)
