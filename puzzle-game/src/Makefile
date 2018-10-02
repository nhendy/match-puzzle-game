JAVAC=javac
JVM=java
sources = $(wildcard code/*.java code/ui/*.java code/model/*.java) 
classes = $(sources:.java=.class)

all: $(classes)

clean :
	rm -rf code/*.class code/ui/*.class code/model/*.class


run:
	$(JVM) code/Driver

%.class : %.java
	$(JAVAC) $<
