#!/bin/bash
mkdir -p deps
pushd deps

wget https://github.com/kkos/oniguruma/releases/download/v5.9.6/onig-5.9.6.tar.gz
tar xvf onig-5.9.6.tar.gz
pushd onig-5.9.6
./configure --prefix $(cd .. && pwd -P)
make && make install
popd

wget https://github.com/stedolan/jq/releases/download/jq-1.5/jq-1.5.tar.gz
tar xvf jq-1.5.tar.gz
pushd jq-1.5
./configure --prefix $(cd .. && pwd -P) --with-oniguruma=$(cd .. && pwd -P)
make && make install
popd
popd

name=$(echo -n $(uname)-$(uname -m) | tr "[A-Z]" "[a-z]")
mkdir -p src/main/resources/lib/$name
pushd src/main/resources/lib/$name
find ../../../../../deps/lib -name '*.so' -exec ln -s \{} \;
popd 
