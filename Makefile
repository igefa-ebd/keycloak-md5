.phony: build
build:
	docker run -it --rm -v "$(shell pwd)":/usr/src/mymaven -v "$(shell pwd)/.m2":"/root/.m2" -w /usr/src/mymaven maven:3.8-openjdk-17 mvn clean package
