% Ejercicio 3 - Asignación de horario
% Autor: Alejandro Manzanares Lemus
include "globals.mzn";

% 1. Definición de variables y constantes:
set of int: PROF = 1..6;
set of int: HORA = 1..6;
array[HORA] of var PROF: horario;
array[HORA] of string: strHorario = ["09:00h - 10:00h", "10:00h - 11:00h", "11:00h - 12:00h", "12:00h - 13:00h", "13:00h - 14:00h", "14:00h - 15:00h"];

% 2. Definición de restricciones:
% Todos los profesores deben ser distintos. No se puede asignar dos veces al mismo profesor.
constraint alldifferent(horario);
% Restricciones del profesor 1
constraint forall(i in HORA)(if (horario[i] == 1) then (i >= 3) /\ (i <= 6) endif);
% Restricciones del profesor 2
constraint forall(i in HORA)(if (horario[i] == 2) then (i >= 3) /\ (i < 5) endif);
% Restricciones del profesor 3
constraint forall(i in HORA)(if (horario[i] == 3) then (i >= 2) /\ (i < 6) endif);
% Restricciones del profesor 4
constraint forall(i in HORA)(if (horario[i] == 4) then (i >= 2) /\ (i < 5) endif);
% Restricciones del profesor 5
constraint forall(i in HORA)(if (horario[i] == 5) then (i >= 3) /\ (i < 5) endif);
% Restricciones del profesor 6
constraint forall(i in HORA)(if (horario[i] == 6) then (i >= 1) /\ (i <= 6) endif);

  
% 3. Definición del problema:
solve satisfy;

% 4. Definición del output:
output ["\(strHorario[i]) = Profesor \(horario[i])\n" | i in HORA];
