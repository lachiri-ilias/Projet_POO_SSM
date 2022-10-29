# Ensimag 2A POO - TP 2018/19
# ============================
#
# Ce Makefile permet de compiler le test de l'ihm en ligne de commande.
# Alternative (recommandee?): utiliser un IDE (eclipse, netbeans, ...)
# Le but est ici d'illustrer les notions de "classpath", a vous de l'adapter
# a votre projet.
#
# Organisation:
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

all: Carte Case SaveDonnees Robot DonneesSimulation Incendie Graph Connection Amiral ChefPompier  TestSaveDonnees   testInvader testSimulateur testResolution testLecture TestLecteurDonnees

testInvader:
	javac -d bin -classpath bin/gui.jar -sourcepath src src/TestInvader.java

testSimulateur:
	javac -d bin -classpath bin/gui.jar -sourcepath src src/TestSimulateur.java

testResolution:
	javac -d bin -classpath bin/gui.jar -sourcepath src src/TestResolution.java

testLecture:
	javac -d bin -sourcepath src src/TestLecteurDonnees.java

DonneesSimulation:
	javac -d bin  -classpath bin/donnees -sourcepath src src/donnees/DonneesSimulation.java

TestSaveDonnees:
	javac -d bin -sourcepath src src/TestSaveDonnees.java

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

Connection:
	javac -d bin -classpath bin/plan  -sourcepath src src/plan/Connection.java

Amiral:
	javac -d bin -classpath bin/manager -sourcepath src src/manager/Amiral.java

ChefPompier:
	javac -d bin -classpath bin/manager -sourcepath src src/manager/ChefPompier.java



# Execution:
# on peut taper directement la ligne de commande :
#   > java -classpath bin:bin/gui.jar TestInvader
# ou bien lancer l'execution en passant par ce Makefile:
#   > make exeInvader
exeInvader:
	java -classpath bin:bin/gui.jar TestInvader

exeSimulateur:
		java -classpath bin:bin/gui.jar TestSimulateur cartes/carteSujet_test.map
# java -classpath bin:bin/gui.jar TestSimulateur cartes/carteSujet.map
# java -classpath bin:bin/gui.jar TestSimulateur cartes/desertOfDeath-20x20.map
# java -classpath bin:bin/gui.jar TestSimulateur cartes/spiralOfMadness-50x50.map

exeResolution:
		java -classpath bin:bin/gui.jar TestResolution cartes/carteSujet.map

exeLecture:
	java -classpath bin TestLecteurDonnees cartes/carteSujet.map

exeSave:
	java -classpath bin TestSaveDonnees cartes/carteSujet.map



clean:
	rm -rf bin/*.class
	rm -rf bin/io/*.class
	rm -rf bin/plan/*.class
	rm -rf bin/robot/*.class
	rm -rf bin/manager/*.class
	rm -rf bin/evenement/*.class
	rm -rf bin/incendie/*.class
	rm -rf bin/manager/*.class
	rm -rf bin/donnees/*.class
