% Ejercicio 10 - Problema de la mochila.
% Autor: Alejandro Manzanares Lemus
include "globals.mzn";

% 1. Definición de variables y constantes:
array[1..12] of var 0..1: asignacion;
array[1..12] of string: strNombre = ["Mapa", "Compás", "Agua", "Sandwich", "Azucar", "Lata", "Platano", "Manzana", "Queso", "Cerveza", "Protector Solar", "Camara", ];
array[1..12] of int: peso = [9,13,153,50,15,68,27,39,23,52,11,32];
array[1..12] of int: preferencia = [150,35,200,160,60,45,60,40,30,10,70,30];
var int: suma;
var int: peso_asignado;

% 2. Definición de restricciones:
constraint (suma = sum(i in 1..12 where asignacion[i] == 1)(preferencia[i]));

constraint (peso_asignado = sum(i in 1..12 where asignacion[i] == 1)(peso[i]) /\ peso_asignado <= 275);

% 3. Definición del problema:
solve maximize suma;

% 4. Definición del output:
output ["Objetos:\n"];
output ["\(strNombre[i]) = \(asignacion[i])\n" | i in 1..12];
output ["Suma: \(suma)\n"];
output ["Peso: \(peso_asignado)"];
