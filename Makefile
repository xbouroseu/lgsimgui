all:
	javac -d bin src/glsim/*.java 

check:
	java -cp bin glsim.LGSimGui

clean:
	rm -rf bin