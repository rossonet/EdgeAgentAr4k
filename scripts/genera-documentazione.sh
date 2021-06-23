#!/bin/bash
echo "update todo"
scripts/rigenerate_todo.sh
echo "Gradle clean"
./gradlew clean
echo "Gradle javadoc"
./gradlew ar4kAllJavaDoc  -Psigning.password=${{ secrets.OSSRH_GPG_SECRET_KEY_PASSWORD }} -Psigning.keyId=F6113733 -Psigning.secretKeyRingFile=/home/runner/.gnupg/private.key -Dorg.gradle.jvmargs="-Xms512M -Xmx4G" -Dorg.gradle.daemon=false > /dev/null 
cp -LR mkdocs/* build/docs
echo "Genera il sito con MkDoc"
cd build
mv docs/mkdocs.yml mkdocs.yml
mkdocs build --config-file=mkdocs.yml -v -c -d doc-site
tar -czf doc-site.tgz doc-site
exit 0
