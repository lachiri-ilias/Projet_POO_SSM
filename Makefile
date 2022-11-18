# Ensimag 2A POO - TP 2018/19
# ============================
#
#  1) Les sources (*.java) se trouvent dans le repertoire src
#     Les classes d'un package toto sont dans src/toto
#     Les classes du package par defaut sont dans src
#
#  2) Les bytecodes (*.class) se trouvent dans le repertoire bin
#     La hierarchie des sources (par package) est conservee.
#     L'archive bin/gui.jar contient les classes de l'interface graphique
#
# Compilation:
#  Options de javac:
#   -d : repertoire dans lequel sont places les .class compiles
#   -classpath : repertoire dans lequel sont cherches les .class deja compiles
#   -sourcepath : repertoire dans lequel sont cherches les .java (dependances)

map ?= cartes/carteSujet.map

all: Carte Case SaveDonnees Robot DonneesSimulation Incendie Graph Dijkstra ChefPompier testInvader testLecture TestLecteurDonnees Test1 Test2  SimulBas  Simul 


testInvader:
	javac -d bin -classpath bin/gui.jar -sourcepath src src/TestInvader.java

Test1:
	javac -d bin -classpath bin/gui.jar -sourcepath src src/Test1.java

Test2:
	javac -d bin -classpath bin/gui.jar -sourcepath src src/Test2.java

Test3:
	javac -d bin -classpath bin/gui.jar -sourcepath src src/Test3.java

SimulBas:
	javac -d bin -classpath bin/gui.jar -sourcepath src src/SimulBas.java

Simul:
	javac -d bin -classpath bin/gui.jar -sourcepath src src/Simul.java

testLecture:
	javac -d bin -sourcepath src src/TestLecteurDonnees.java

DonneesSimulation:
	javac -d bin  -classpath bin/donnees -sourcepath src src/donnees/DonneesSimulation.java

TestLecteurDonnees:
	javac -d bin -sourcepath src src/TestLecteurDonnees.java

SaveDonnees:
	javac -d bin  -sourcepath src src/SaveDonnees.java

Robot:
	javac -d bin -classpath bin/robot -sourcepath src src/robot/Robot.java

Carte:
	javac -d bin -classpath bin/plan  -sourcepath src src/plan/Carte.java

Case:
	javac -d bin -classpath bin/plan  -sourcepath src src/plan/Case.java

Incendie:
	javac -d bin -classpath bin/incendie -sourcepath src src/incendie/Incendie.java

Graph:
	javac -d bin -classpath bin/plan  -sourcepath src src/plan/Graph.java

Dijkstra:
	javac -d bin -classpath bin/plan  -sourcepath src src/plan/Dijkstra.java

ChefPompier:
	javac -d bin -classpath bin/manager -sourcepath src src/manager/ChefPompier.java


# Execution:
# on peut taper directement la ligne de commande :
#   > java -classpath bin:bin/gui.jar TestInvader
# ou bien lancer l'execution en passant par ce Makefile:
#   > make exeInvader

exeInvader:
		java -classpath bin:bin/gui.jar TestInvader

exeLecture:
	java -classpath bin TestLecteurDonnees $(map)

exeSave:
	java -classpath bin TestSaveDonnees $(map)

exeTest1:
	java -classpath bin:bin/gui.jar Test1 cartes/carteSujet.map

exeTest2:
	java -classpath bin:bin/gui.jar Test2 cartes/carteSujet.map

exeSimulBasic:
	java -classpath bin:bin/gui.jar SimulBas $(map)

exeSimul:
	java -classpath bin:bin/gui.jar Simul $(map)




clean:
	rm -rf bin/*/*.class
	rm -rf bin/*.class

