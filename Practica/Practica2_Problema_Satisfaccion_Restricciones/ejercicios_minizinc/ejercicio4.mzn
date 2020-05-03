% Ejercicio 4 - Asignación de horario
% Autor: Alejandro Manzanares Lemus
include "globals.mzn";

% 1. Definición de variables y constantes:
set of int: ASIG = 1..16;
set of int: HORA = 1..4;
set of int: AULA = 1..4;

array[HORA,AULA] of var ASIG: horario;
array[1..16] of int: asig = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13,14,15,16];
array[1..12] of int: prof = [1, 1, 4, 4, 2, 2, 3, 3, 1, 1,  3,  3];

array[HORA] of string: strHora = ["09:00h - 10:00h", "10:00h - 11:00h", "11:00h - 12:00h", "12:00h - 13:00h"];
array[ASIG] of string: strAsig = ["IA-G1", "IA-G2", "IA-G3", "IA-G4", "FBD-G1", "FBD-G2", "FBD-G3", "FBD-G4", "TSI-G1", "TSI-G2", "TSI-G3", "TSI-G4","VACIO","VACIO","VACIO","VACIO"];

% 2. Definición de restricciones:
% Todas las asignaciones deben ser distintas
constraint alldifferent([horario[i,j] | i,j in 1..4]);

% El mismo profesor no debe dar dos asignaturas a la misma hora
constraint forall(i,j,i1,j1 in 1..4)(if (i == i1 /\ j != j1 /\ horario[i,j] <= 12 /\ horario[i1,j1] <= 12) then prof[horario[i,j]] != prof[horario[i1,j1]] endif);

% Restricciones especiales de cada profesor
constraint forall(i,j in 1..4)(if prof[horario[i,j]] == 2 then i != 2 endif);
constraint forall(i,j in 1..4)(if prof[horario[i,j]] == 4 then i != 1 endif);

% 3. Definición del problema:
solve satisfy;

% 4. Definición del output:
output ["Asignaturas:\n"];
output ["\(asig[i])-\(strAsig[i]) " | i in ASIG];
output ["\n"];
output ["\n"];
output ["\(strHora[1]): "];
output ["\(horario[1,i])\t" | i in 1..4];
output ["\n"];
output ["\(strHora[2]): "];
output ["\(horario[2,i])\t" | i in 1..4];
output ["\n"];
output ["\(strHora[3]): "];
output ["\(horario[3,i])\t" | i in 1..4];
output ["\n"];
output ["\(strHora[4]): "];
output ["\(horario[4,i])\t" | i in 1..4];
