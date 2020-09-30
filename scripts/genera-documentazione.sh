#!/bin/bash
echo "update todo"
scripts/rigenerate_todo.sh
echo "Gradle clean"
./gradlew clean
echo "Gradle javadoc"
./gradlew ar4kAllJavaDoc
cp -LR mkdocs/* build/docs
echo "Genera il sito con MkDoc"
cd build
mv docs/mkdocs.yml mkdocs.yml
mkdocs build --config-file=mkdocs.yml -v -c -d doc-site
tar -czf doc-site.tgz doc-site
exit 0
