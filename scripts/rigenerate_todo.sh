#!/bin/bash
echo "## TODO LIST" > site/TODO.md
cat notes/macro_todo.txt >> site/TODO.md
echo "" >> site/TODO.md
echo "## TODO LIST ESTESA" >> site/TODO.md
echo "" >> site/TODO.md
for line in $( grep -n -R "TODO" ar4k-*/src | sed 's/ /<!sPaCe!>/g' | sed 's/\t//g' )
do
	label=$( echo $line | cut -d : -f 2-10 | sed 's/<!sPaCe!>/ /g' | sed 's/\///g' | sed 's/TODO//g' | sed 's/^ *//g' | sed 's/^.[^:]*: *//')
	number=$( echo $line | cut -d : -f 2-10 | sed 's/<!sPaCe!>/ /g' | sed 's/\///g' | sed 's/TODO//g' | sed 's/^ *//g' | sed 's/:.*$//')
	file='https://github.com/rossonet/EdgeAgentAr4k/blob/master/'$( echo $line | cut -d : -f 1 | sed 's/<!sPaCe!>/ /g' )'#L'$number
	echo "[$label]($file)" >> site/TODO.md
	echo "" >> site/TODO.md
done
