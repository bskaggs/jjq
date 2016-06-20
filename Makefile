JJQ_VERSION=0.0.1
ONIGURUMA_VERSION=5.9.6
JQ_VERSION=1.5
PLATFORM=$$(echo -n $$(uname)-$$(uname -m) | tr "[A-Z]" "[a-z]")

.PHONY: fetch install build clen

build: target/jjq-$(JJQ_VERSION)-SNAPSHOT.jar
	
install: build
	mvn install	

fetch: deps/onig-$(ONIGURUMA_VERSION).tar.gz deps/jq-$(JQ_VERSION).tar.gz

deps/onig-$(ONIGURUMA_VERSION).tar.gz:
	mkdir -p deps && cd deps && wget https://github.com/kkos/oniguruma/releases/download/v$(ONIGURUMA_VERSION)/onig-$(ONIGURUMA_VERSION).tar.gz

deps/jq-$(JQ_VERSION).tar.gz:
	mkdir -p deps && cd deps && wget https://github.com/stedolan/jq/releases/download/jq-$(JQ_VERSION)/jq-$(JQ_VERSION).tar.gz

deps/lib/libonig.so: deps/onig-$(ONIGURUMA_VERSION).tar.gz
	cd deps && tar xvf onig-$(ONIGURUMA_VERSION).tar.gz && cd onig-$(ONIGURUMA_VERSION) && ./configure --prefix $$(cd .. && pwd -P) && make && make install

deps/lib/libjq.so: deps/jq-$(JQ_VERSION).tar.gz deps/lib/libonig.so
	cd deps && tar xvf jq-$(JQ_VERSION).tar.gz && cd jq-$(JQ_VERSION) && ./configure --prefix $$(cd .. && pwd -P) --with-oniguruma=$$(cd .. && pwd -P) && make && make install

src/main/resources/lib/: deps/lib/libjq.so deps/lib/libonig.so
	mkdir -p src/main/resources/lib/$(PLATFORM)
	cd src/main/resources/lib/$(PLATFORM) && find ../../../../../deps/lib -name '*.so' -exec ln -s \{} \;

target/jjq-$(JJQ_VERSION)-SNAPSHOT.jar: src/main/resources/lib/
	mvn package

clean:
	rm -rf deps src/main/resources/lib target
