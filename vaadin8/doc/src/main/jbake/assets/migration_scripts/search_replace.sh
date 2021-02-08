#!/bin/bash

# Script searches for files in the current directory that match the file pattern ($1)
# In all files it calls sed with a bunch of search/replace commands (listed in file $2)

###############################################

function usage {

echo "Usage:"
echo
echo "-f/--file   File pattern in posix-regex e.g. '.*\.java', default is all java and xml files '(.+\.java)|(.+\.xml)'"
echo
echo "-d/--dir     Root directory for the search, default is '.'"
echo
echo "-s/--sed     Path to a file containing all the sed search/replace commands"
echo "             Each sed command in one line"
echo "             For example replace all 'foo' with 'bar':"
echo "             s/foo/bar/g"
echo

}


DIR="."
FILE_PATTERN="(.+\.java)|(.+\.xml)"

while [[ $# -gt 0 ]]
do
key="$1"
case $key in
    -f|--file)
    FILE_PATTERN="$2"
    shift # past argument
    shift # past value
    ;;
    -s|--sed)
    SED_COMMANDS="$2"
    shift # past argument
    shift # past value
    ;;
    -d|--dir)
    DIR="$2"
    shift # past argument
    shift # past value
    ;;
    *)
    usage
    exit -1
esac
done

if [[ ! -f "$SED_COMMANDS" ]]
then
    echo "File not found: $SED_COMMANDS"
    echo "Provide file with a list of sed commands using parameter -s or --sed"
    exit -1
fi


find "$DIR" -not -path '**/.*' -regextype posix-extended -regex "$FILE_PATTERN" -type f -printf '\n%p' -exec sed -i -E -f $SED_COMMANDS {} \;

