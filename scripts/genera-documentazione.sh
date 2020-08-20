#!/bin/bash
echo "Gradle clean javadoc"
./gradlew clean
./gradlew ar4kAllJavaDoc
echo "Genera il sito con MkDoc"
#mkdir -p build/javadoc
#for sub in $(cat list_packages.txt)
#do
#   mv $sub/build/docs/javadoc build/javadoc/$sub
#done
#rm -rf build/docs
#cp -LR mkdocs build/docs
#cd build
#mkdocs build --config-file=../mkdocs/mkdocs.yml -v -c -d doc-site
exit 0
