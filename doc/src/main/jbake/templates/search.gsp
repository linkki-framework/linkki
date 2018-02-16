<%include "header.gsp"%>
    
<%include "menu.gsp"%>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>

<script type="text/javascript" src="../js/tipuesearch/tipuesearch_set.js"></script>
<script type="text/javascript" src="../js/tipuesearch/tipuesearch_content.js"></script>
<link rel="stylesheet" type="text/css" href="../js/tipuesearch/css/tipuesearch.css">
<script type="text/javascript" src="../js/tipuesearch/tipuesearch.min.js"></script>
<script type="text/javascript" src="../js/tipuesearch/search.js"></script>  
<div class="book-body" onload="prettyPrint()">
    <div class="body-inner">
        <div class="page-wrapper" tabindex="-1" role="main">
            <div class="page-inner">             
                <section class="normal markdown-section">

                    <%
                    import groovy.json.StringEscapeUtils;
                    
                    String dir = config.project_basedir + '/src/main/jbake/'
                    PrintWriter pw
                    
                    // Content file for search
                    String beginningContent = 'var tipuesearch = {"pages": ['
                    String endContent = ']};'
                    
                    String beginngTitle = '{"title": "'
                    String endTitle = '",'
                    
                    String beginngText = ' "text": "'
                    String endText = '",'
                    
                    String beginngTags = ' "tags": "'
                    String endTags = '",'
                    
                    String beginngURL = ' "url": "'
                    String endURL = '"},'
                    
                    String filePath = dir + 'assets/js/tipuesearch/'
                    String fileName = filePath + 'tipuesearch_content.js'
                    
                    // Delete old file
                    new File(fileName).delete()  
                    
                    // Create new file
                    f = new File(fileName)
                    
                    f.append(beginningContent)
                        
                        sortedByFileName = published_sections.sort{ it.uri }
                        sortedByFileName.each {section -> 
                        // Title
                        f.append(beginngTitle)
                            f.append(section.title)
                        f.append(endTitle)
                        
                        // Text
                        f.append(beginngText)
                            String cleanedBody = (section.body).replaceAll("\\<.*?>","");
                            cleanedBody = cleanedBody.replaceAll("\n"," ");
                            cleanedBody = cleanedBody.replaceAll('"',"");
                            cleanedBody = cleanedBody.replaceAll('&',"");
                            cleanedBody = StringEscapeUtils.escapeJava(cleanedBody);
                            f.append(cleanedBody)
                        f.append(endTags)
                        
                        // Tags
                        f.append(beginngTags)
                            //f.append(section.tags)
                        f.append(endTags)
                        
                        // URL
                        f.append(beginngURL)
                        String sectionURI = content.rootpath + section.uri
                            f.append(sectionURI)
                        f.append(endURL)
                        }
                    f.append(endContent)
                    
                    
                    
                    
                    // Hot-Fix crossfile links
                    
                    def ankorMap = new HashMap<String, String>()
                    def basedir = new File(dir + "/content/")
                    def targetDir = new File(
                            config.jbake_outputDirectory);

                    for (File dirAdoc : basedir.listFiles()) {
                        for (File adoc : dirAdoc.listFiles()) {

                            def lines = adoc.readLines()

                            // Create map for ankors and their root
                            for (String line : lines) {
                                def matcher = line =~ /^\[\[.*?\]\]/
                                while (matcher.find()) {
                                    String s = matcher.group()
                                    def filename = adoc.getName().split('\\.')[0]
                                    ankorMap.put(s.substring(2, s.length() - 2), "../" + dirAdoc.getName() + "/" + filename + ".html")
                                }
                            }
                        }
                    }

                    if(ankorMap.isEmpty()){
                        throw new IllegalStateException("Empty Map");
                    }
                    
                    final List<String> internalDirs = Arrays.asList("search", "js", "images", "fonts", "css");
                    final List<File> nonInternalDirs = targetDir.listFiles().findAll{it.isDirectory() && !internalDirs.contains(it.getName())}.collect();
                    for (File dirTarget : nonInternalDirs) {
                        for (File html : dirTarget.listFiles()) {
                            String sourceCode = html.getText('UTF-8');
                            def result = sourceCode
                                
                            result = sourceCode.replaceAll(/(href=\")(#)(.*?)\"/, {all, href, hash, ankorKey ->
                                def absolutePath = ankorMap.get(ankorKey);
                                if (absolutePath != null)
                                    "$href$absolutePath$hash$ankorKey\""
                                else 
                                    all } );
                            result = result.replaceAll("<\\\\", "</")
                            result = result.replaceAll("\\\\>", "/>")
                            html.write(result, 'UTF-8');
                        }
                    }

                    
                    
                    
                    // PDF stuff
                    
                    // Create a File object representing the folder
                    String contentPath = new File( dir + 'content/' )
                    String pdfPath = config.project_basedir + '/target/' + 'pdf-index/'
                    def pdfFolder = new File(pdfPath)
                    String documentationPath = pdfPath + 'documentation.adoc'
                    
                    
                    // If it doesn't exist
                    if( !pdfFolder.exists() ) {
                      // Create all folders
                      pdfFolder.mkdirs()
                    }
                    
                    // Delete old file
                    new File(documentationPath).delete()  
                    
                    // Create new file
                    docFile = new File(documentationPath)
                    pw = new PrintWriter(documentationPath);
                    pw.close();
          
                    // Add jbake properties
                    docFile.append(':images: ' + config.images_pdf + '\n\n')
                        
                    // Add toc
                    docFile.append(':toc:\n:toclevels: 3\n\n')
                    
                    // Add title
                    docFile.append('= ' + config.dtitle + '\n\n')
                    
                    // include index of content
                    docFile.append('include::' + contentPath + '/index.adoc[]\n\n')
                    
                    // Include in documentation
                    sortedChapters = published_chapters.sort{ it.uri }
                    sortedChapters.each 
                    {    chapter ->
                        
                        fileName = chapter.uri.substring(0, chapter.uri.indexOf('/')) 
                        filePath = pdfPath + fileName + '.adoc'
                        
                        // Delete old file
                        new File(filePath).delete()  
                    
                        // Create new file
                        file = new File(filePath)
                        pw = new PrintWriter(filePath);
                        pw.close();
                        
                        // Include index.adoc
                        chapterName = fileName
                        docFile.append('<<<\n\n')
                        indexInclude = 'include::' + contentPath + '/' + chapterName + '/index.adoc[]'
                        docFile.append(indexInclude + '\n\n')
                        
                        def include 
                        sortedByFileName = published_sections.sort{ it.uri }
                        sortedByFileName.each
                                        
                        
                        {    
                                    
                            section ->                 
                            
                            if(chapter.uri.substring(0, chapter.uri.indexOf('/')) == section.uri.substring(0, section.uri.indexOf('/')))
                            {
                                include = 'include::' + contentPath + '/' + section.uri.substring(0, section.uri.indexOf('.')) + '.adoc[]'
                                
                                file.append(include.replaceAll('\\\\', '/') + '\n\n')
                                
                            }
                        }
                        
                        // Add to documentation
                        include = 'include::' + pdfPath + fileName + '.adoc[]'
                        docFile.append(include + '\n\n')
                    }
                    
                    %>
                    
                    <script>
                        window.onload = search;
                    </script>
                    
                    <h1>Results:</h1>
                    <div id="tipue_search_content"></div>
                    
                    
                    
                    
                    
                </section>
            </div>
        </div>
    </div>
</div>

<%include "footer.gsp"%>
