% Ejercicio 2 - Numero de 10 digitos.
% Autor: Alejandro Manzanares Lemus
include "globals.mzn";

% 1. Definición de variables y constantes:
set of int: NUM = 0..9;
array[NUM] of var NUM: numero_x;

% 2. Definición de restricciones:
% Numero i solo puede aparecer numero[i] veces.
constraint forall(i in NUM)(numero_x[i] == count(numero_x,i));
  
% 3. Definición del problema:
solve satisfy;

% 4. Definición del output:
output ["\(numero_x[i])" | i in NUM];
