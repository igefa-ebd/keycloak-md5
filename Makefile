.phony: build
build:
	docker run -it --rm -v "$(shell pwd)":/usr/src/mymaven -w /usr/src/mymaven maven:3.8-jdk-11 mvn clean package
