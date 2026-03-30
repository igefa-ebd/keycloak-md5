.PHONY: clean build dep-tree

UID := $(shell id -u)
GID := $(shell id -g)
M2_DIR ?= $(CURDIR)/.m2-user
DOCKER_MVN_BASE = docker run --rm --user "$(UID):$(GID)" \
	-e HOME=/var/maven \
	-e MAVEN_CONFIG=/var/maven/.m2 \
	-e MAVEN_OPTS=-Duser.home=/var/maven \
	-v "$(CURDIR)":/usr/src/mymaven \
	-v "$(M2_DIR)":/var/maven/.m2 \
	-w /usr/src/mymaven
DOCKER_MVN_IMAGE = maven:3.8-openjdk-17

clean:
	mkdir -p "$(M2_DIR)"
	$(DOCKER_MVN_BASE) $(DOCKER_MVN_IMAGE) mvn clean

build: clean
	mkdir -p "$(M2_DIR)"
	$(DOCKER_MVN_BASE) $(DOCKER_MVN_IMAGE) mvn package


dep-tree:
	mkdir -p "$(M2_DIR)"
	$(DOCKER_MVN_BASE) $(DOCKER_MVN_IMAGE) mvn dependency:tree
