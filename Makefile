JAVAC=javac -classpath ./src
JVM=java
sources = $(wildcard src/code/*.java src/code/ui/*.java src/code/model/*.java) 
classes = $(sources:.java=.class)

all: $(classes)

clean :
	rm -rf src/code/*.class src/code/ui/*.class src/code/model/*.class


run:
	$(JVM) src/code/Driver

%.class : %.java
	$(JAVAC) $<
