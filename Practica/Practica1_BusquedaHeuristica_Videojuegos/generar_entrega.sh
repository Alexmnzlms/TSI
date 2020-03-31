#!/bin/bash

cp doc/demo.pdf .
mv demo.pdf Memoria_Practica1_Alejandro_Manzanares_Lemus.pdf
mkdir src_manzanares_lemus_alejandro
cp TSI-P1/src/src_manzanares_lemus_alejandro/* ./src_manzanares_lemus_alejandro
zip -r practica1_anzanares_lemus_alejandro.zip ./src_manzanares_lemus_alejandro ./Memoria_Practica1_Alejandro_Manzanares_Lemus.pdf
rm -rf src_manzanares_lemus_alejandro/
rm Memoria_Practica1_Alejandro_Manzanares_Lemus.pdf
