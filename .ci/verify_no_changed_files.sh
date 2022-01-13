#!/bin/bash
# verify zero changes in generated code
# exit with 1 if there are any changes
# exit with 0 if there are no other changes

echo "Verify that no changes have resulted from the build."
echo "If there are any changes due to vaadin-maven-plugin, run your local build in production mode."

git status --porcelain | grep -vs "any changes" && echo -e \'\\e[31mTHERE ARE UNCOMITTED CHANGES\\e[0m\' && exit 1 || exit 0
