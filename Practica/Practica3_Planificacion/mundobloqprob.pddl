(define (problem BWp1)
    (:domain blocksworld)
    (:objects b1 b2 b3 b4 b5 - block)
    (:init
        ;Bloque 5 en la mano
        (holding b5)

        ;El bloque 2 sobre el bloque 4, y bloque 3 sobre bloque 1
        (on b2 b4)
        (on b3 b1)

        ;En la mesa los bloques 4 y 1
        (ontable b4)
        (ontable b1)

        ;Y al descubierto los bloques 2 y 3
        (clear b2)
        (clear b3)

    )
    (:goal
        (and
            (on b3 b5)
            (on b5 b2)

            (ontable b4)
            (ontable b1)
            (ontable b2)
        )
    )
)
