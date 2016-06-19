JJQ_VERSION=0.0.1
PLATFORM=$$(echo -n $$(uname)-$$(uname -m) | tr "[A-Z]" "[a-z]")

.PHONY: fetch

build: target/jjq-0.0.1-SNAPSHOT.jar


fetch: deps/onig-5.9.6.tar.gz deps/jq-1.5.tar.gz

deps/onig-5.9.6.tar.gz:
	mkdir -p deps && cd deps && wget https://github.com/kkos/oniguruma/releases/download/v5.9.6/onig-5.9.6.tar.gz

deps/jq-1.5.tar.gz:
	mkdir -p deps && cd deps && wget https://github.com/stedolan/jq/releases/download/jq-1.5/jq-1.5.tar.gz

deps/lib/libonig.so: deps/onig-5.9.6.tar.gz
	cd deps && tar xvf onig-5.9.6.tar.gz && cd onig-5.9.6 && ./configure --prefix $$(cd .. && pwd -P) && make && make install

deps/lib/libjq.so: deps/jq-1.5.tar.gz deps/lib/libonig.so
	cd deps && tar xvf jq-1.5.tar.gz && cd jq-1.5 && ./configure --prefix $$(cd .. && pwd -P) --with-oniguruma=$$(cd .. && pwd -P) && make && make install

src/main/resources/lib/: deps/lib/libjq.so deps/lib/libonig.so
	mkdir -p src/main/resources/lib/$(PLATFORM)
	cd src/main/resources/lib/$(PLATFORM) && find ../../../../../deps/lib -name '*.so' -exec ln -s \{} \;

target/jjq-$(JJQ_VERSION)-SNAPSHOT.jar: src/main/resources/lib/
	mvn package

clean:
	rm -rf deps src/main/resources/lib target
