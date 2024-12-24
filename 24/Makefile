run:
	mkdir -p out
	kotlinc src/Main.kt -include-runtime -d out/Main.jar && java -jar out/Main.jar && rm out/Main.jar

image:
	dot -Tpng input.dot > input.png