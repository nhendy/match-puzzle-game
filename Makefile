JAVAC=javac -classpath ./src
JVM=java -classpath ./src
sources = $(wildcard src/code/*.java src/code/ui/*.java src/code/model/*.java) 
classes = $(sources:.java=.class)

all: $(classes)

clean :
	rm -rf src/code/*.class src/code/ui/*.class src/code/model/*.class


run:
	$(JVM) code/Driver

%.class : %.java
	$(JAVAC) $<


	
