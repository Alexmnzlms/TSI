% Ejercicio 6 -.
% Autor: Alejandro Manzanares Lemus
include "globals.mzn";

% 1. Definición de variables y constantes:
array[1..5] of var 1..5: casa;
array[1..5] of var 1..5: color;
array[1..5] of var 1..5: profesion;
array[1..5] of var 1..5: nacionalidad;
array[1..5] of var 1..5: bebida;
array[1..5] of var 1..5: animal;

array[1..5] of string: strCasa = ["Primera", "Segunda", "Tercera", "Cuarta", "Quinta"];
array[1..5] of string: strColor = ["Rojo", "Verde", "Blanco", "Amarillo", "Azul"];
array[1..5] of string: strProfesion = ["Pintor", "Escultor", "Diplomatico", "Violinista", "Medico"];
array[1..5] of string: strNacionalidad = ["Vasco", "Catalan", "Gallego", "Navarro", "Andaluz"];
array[1..5] of string: strBebida = ["Té", "Cafe", "Leche", "Zumo", "Agua"];
array[1..5] of string: strAnimal = ["Perro", "Caracoles", "Zorro", "Caballo", "Cebra"];

% 2. Definición de restricciones:

constraint alldifferent(casa);
constraint alldifferent(color);
constraint alldifferent(profesion);
constraint alldifferent(nacionalidad);
constraint alldifferent(bebida);
constraint alldifferent(animal);

% El vasco vive en la casa roja.
constraint (nacionalidad[1] == color[1]);

% El catalán tiene un perro.
constraint (nacionalidad[2] == animal[1]);

% El gallego es un pintor.
constraint (nacionalidad[3] == profesion[1]);

% El navarro bebe te.
constraint (nacionalidad[4] == bebida[1]);

% El andaluz vive en la primera casa de la izquierda.
constraint (nacionalidad[5] == casa[1]);

% El de la casa verde bebe café.
constraint (color[2] == bebida[2]);

% La casa verde está al lado de la blanca y a su derecha.
constraint forall(i,j in 1..5)(if (i == color[2] /\ j == color[3] ) 
                               then ((j == casa[1] /\ i == casa[2]) \/ (j == casa[2] /\ i == casa[3]) \/ 
                                    (j == casa[3] /\ i == casa[4]) \/ (j == casa[4] /\ i == casa[5])) endif);
                         
% El escultor cría caracoles.           
constraint (profesion[2] == animal[2]);

% El diplomático vive en la casa amarilla.
constraint (profesion[3] == color[4]);

% En la casa central se bebe leche.
constraint (casa[3] == bebida[3]);

% La casa del andaluz está al lado de la azul.
constraint forall(i,j in 1..5)(if ((i == nacionalidad[5] /\ j == color[5]) \/ (j == nacionalidad[5] /\ i == color[5]))
                               then ((j == casa[1] /\ i == casa[2]) \/ (j == casa[2] /\ i == casa[3]) \/ 
                                    (j == casa[3] /\ i == casa[4]) \/ (j == casa[4] /\ i == casa[5])) endif);
                  
% El violinista bebe zumo.                  
constraint (profesion[4] == bebida[4]);

% El zorro está en una casa al lado de la del médico.
constraint forall(i,j in 1..5)(if ((i == animal[3] /\ j == profesion[5]) \/ (j == animal[3] /\ i == profesion[5]))
                               then ((j == casa[1] /\ i == casa[2]) \/ (j == casa[2] /\ i == casa[3]) \/ 
                                    (j == casa[3] /\ i == casa[4]) \/ (j == casa[4] /\ i == casa[5])) endif);

% El caballo está en una casa al lado de la del diplomático.
constraint forall(i,j in 1..5)(if ((i == animal[4] /\ j == profesion[3]) \/ (j == animal[4] /\ i == profesion[3]))
                               then ((j == casa[1] /\ i == casa[2]) \/ (j == casa[2] /\ i == casa[3]) \/ 
                                    (j == casa[3] /\ i == casa[4]) \/ (j == casa[4] /\ i == casa[5])) endif);
                                    
% 3. Definición del problema:
solve satisfy;

% 4. Definición del output:
output ["\(strCasa[i]) = \(casa[i])\t" | i in 1..5];
output ["\n"];
output ["\(strColor[i]) = \(color[i])\t" | i in 1..5];
output ["\n"];
output ["\(strProfesion[i]) = \(profesion[i])\t" | i in 1..5];
output ["\n"];
output ["\(strNacionalidad[i]) = \(nacionalidad[i])\t" | i in 1..5];
output ["\n"];
output ["\(strBebida[i]) = \(bebida[i])\t" | i in 1..5];
output ["\n"];
output ["\(strAnimal[i]) = \(animal[i])\t" | i in 1..5];
output ["\n"];
