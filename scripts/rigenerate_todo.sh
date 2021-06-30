#!/bin/bash
echo "## TODO LIST" > TODO.md
cat notes/macro_todo.txt >> TODO.md
echo "" >> TODO.md
echo "## TODO LIST ESTESA" >> TODO.md
echo "" >> TODO.md
for line in $( grep -R "TODO" ar4k-*/src | sed 's/ /<!sPaCe!>/g' | sed 's/\t//g' )
do
	label=$( echo $line | cut -d : -f 2-10 | sed 's/<!sPaCe!>/ /g' | sed 's/\///g' | sed 's/TODO//g' | sed 's/^ *//g')
	file='https://github.com/rossonet/EdgeAgentAr4k/blob/master/'$( echo $line | cut -d : -f 1 | sed 's/<!sPaCe!>/ /g' )
	echo "[$label]($file)" >> TODO.md
	echo "" >> TODO.md
done
