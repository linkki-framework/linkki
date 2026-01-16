// returns true if a file in the module at the given path has changed with last commit.
// copied from https://gerrit.faktorzehn.de/plugins/gitiles/pnc/ipm.pnc/+/refs/heads/main/ci/lib/vars/moduleHasChanges.groovy
def call(String pathToModule) {
    return sh(returnStatus: true, script: "git diff --quiet HEAD^ HEAD -- ${pathToModule}") != 0
}