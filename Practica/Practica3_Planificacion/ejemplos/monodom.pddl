(define (domain mono)
    (:requirements :strips :typing)
    (:types
        movible localizacion - object
        mono caja - movible
    )
    (:predicates
        (en ?obj - movible ?x - localizacion)
        (tienePlatano ?m - mono)
        (sobre ?m - mono ?c - caja)
        (platanoEn ?x - localizacion)
    )

    (:action cogerPlatanos
        :parameters (?m - mono ?c - caja)
        :precondition
            (and
                (sobre ?m ?c)
            )
        :effect
            (and
                (tienePlatano ?m)
            )
    )

)
