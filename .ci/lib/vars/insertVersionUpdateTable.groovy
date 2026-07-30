// This method is only for manual usage in the main method down below
def call(String releaseNotesPath, String versionText, String beginTag, String endTag) {
    if (!releaseNotesPath) { error("insertVersionUpdateTable: releaseNotesPath must not be empty") }
    if (!versionText)      { error("insertVersionUpdateTable: versionText must not be empty") }
    if (!beginTag)         { error("insertVersionUpdateTable: beginTag must not be empty") }
    if (!endTag)           { error("insertVersionUpdateTable: endTag must not be empty") }
    writeFile(file: releaseNotesPath, text: transform(readFile(releaseNotesPath), versionText, beginTag, endTag))
}

static String transform(String content, String versionText, String beginTag, String endTag) {
    List<String> lines = content.split('\n', -1).toList()

    int beginIndex = -1
    int endIndex   = -1
    for (int i = 0; i < lines.size(); i++) {
        String bare = lines[i].replaceAll(/^\/\/ ?/, '')
        if (bare.trim() == beginTag.trim() && beginIndex == -1) {
            beginIndex = i
        } else if (bare.trim() == endTag.trim() && beginIndex != -1) {
            endIndex = i
            break
        }
    }
    if (beginIndex == -1) { throw new IllegalArgumentException("beginTag '${beginTag}' not found") }
    if (endIndex   == -1) { throw new IllegalArgumentException("endTag '${endTag}' not found") }

    List<String> templateLines = []
    for (int i = beginIndex + 1; i < endIndex; i++) {
        String line = lines[i]
        if (line.startsWith('// '))     { templateLines.add(line.substring(3)) }
        else if (line.startsWith('//')) { templateLines.add(line.substring(2)) }
        else                            { templateLines.add(line) }
    }

    int insertAfterIndex = -1
    for (int i = 0; i < lines.size(); i++) {
        if (lines[i].contains(versionText)) { insertAfterIndex = i; break }
    }
    if (insertAfterIndex == -1) { throw new IllegalArgumentException("versionText '${versionText}' not found") }

    String firstTemplateLine = templateLines.find { !it.trim().isEmpty() }
    String nextNonEmptyAfterVersion = lines.subList(insertAfterIndex + 1, lines.size()).find { !it.trim().isEmpty() }
    if (firstTemplateLine != null && firstTemplateLine == nextNonEmptyAfterVersion) {
        return content
    }

    List<String> result = []
    result.addAll(lines.subList(0, insertAfterIndex + 1))
    result.add('')
    result.addAll(templateLines)
    result.addAll(lines.subList(insertAfterIndex + 1, lines.size()))
    return result.join('\n')
}

static void main(String[] args) {
    File dir = new File('.').absoluteFile
    while (dir != null && !new File(dir, 'vaadin-flow').exists()) {
        dir = dir.parentFile
    }
    if (dir == null) { println "ERROR: could not locate repo root"; System.exit(1) }
    File file = new File(dir, 'vaadin-flow/doc/src/main/jbake/content/00_releasenotes/index.adoc')
    if (!file.exists()) { println "ERROR: file not found: ${file.path}"; System.exit(1) }
    file.text = transform(file.text, 'Version {vlinkki}', '[tag::template-version-updates]', '[end::template-version-updates]')
    println "Done."
}
