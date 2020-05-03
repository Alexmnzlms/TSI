% Ejercicio 1 - Puzzle Cripto-Aritmetico
% Autor: Alejandro Manzanares Lemus
include "globals.mzn";

% 1. Definición de variables y constantes:
set of int: NUM = 0..9;
set of int: POS = 1..10;
array[POS] of var NUM: letras;
array[POS] of string: strLetras = ["T", "E", "S", "F", "D", "I", "N", "A", "R", "K"];
array[1..5] of var NUM: acarreo;

% 2. Definición de restricciones:
constraint alldifferent(letras);
  
% E + E + E = E
constraint (3*letras[2] mod 10 == letras[2]);
constraint (acarreo[1] = 3*letras[2] div 10);
  
% T + T + N = T
constraint ((2*letras[1] + letras[7] + acarreo[1]) mod 10 == letras[1]);
constraint (acarreo[2] = (2*letras[1] + letras[7] + acarreo[1]) div 10);
  
% S + S + I = F
constraint ((2*letras[3] + letras[6] + acarreo[2]) mod 10 == letras[4]);
constraint (acarreo[3] = (2*letras[3] + letras[6] + acarreo[2]) div 10);
  
% E + E + E = A
constraint ((3*letras[2] + acarreo[3]) mod 10 == letras[8]);
constraint (acarreo[4] = (3*letras[2] + acarreo[3]) div 10);
  
%T + F + D = R
constraint ((letras[1] + letras[4] + letras[5] + acarreo[4]) mod 10 == letras[9]);
constraint (acarreo[5] = (letras[1] + letras[4] + letras[5] + acarreo[4]) div 10);
  
%K
constraint(letras[10] = acarreo[5]);

% 3. Definición del problema:
solve satisfy;

% 4. Definición del output:
output ["\(strLetras[i]) = \(letras[i])  " | i in POS];