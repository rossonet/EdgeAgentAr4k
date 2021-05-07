grammar DataFilter;

@header {
   package org.ar4k.grammar;
} 

start : filter_query | <EOF> ;

array_comparator: OPERATOR OPEN_PAR TEXT (COMMA TEXT)*? CLOSE_PAR ;
single_check: OPERATOR LABEL IN array_comparator ;

filter_query: single_check (single_check)*? ;

OPERATOR : ( O R  | A N D | A N D LOWER_LINE N O T  | O R LOWER_LINE N O T );
LABEL : ( T A G | D O M A I N | N A M E LOWER_LINE S P A C E | S T A T U S | S E R V I C E LOWER_LINE N A M E | S E R V I C E LOWER_LINE C L A S S ) ;

QUOTA:		'\'' ;
IN:			I N ;
OPEN_PAR:	'(' ;
CLOSE_PAR:	')' ;
COMMA:		',' ;
TEXT:		QUOTA .*? QUOTA ;
NUMBER:		DIGIT+ ([.,] DIGIT+)? ;

fragment LOWER_LINE: '_' ;
fragment HEX_DIGIT: [0-9a-fA-F] ;
fragment DIGIT:     [0-9] ;
fragment A: [aA] ;
fragment B: [bB] ;
fragment C: [cC] ;
fragment D: [dD] ;
fragment E: [eE] ;
fragment F: [fF] ;
fragment G: [gG] ;
fragment H: [hH] ;
fragment I: [iI] ;
fragment J: [jJ] ;
fragment K: [kK] ;
fragment L: [lL] ;
fragment M: [mM] ;
fragment N: [nN] ;
fragment O: [oO] ;
fragment P: [pP] ;
fragment Q: [qQ] ;
fragment R: [rR] ;
fragment S: [sS] ;
fragment T: [tT] ;
fragment U: [uU] ;
fragment V: [vV] ;
fragment W: [wW] ;
fragment X: [xX] ;
fragment Y: [yY] ;
fragment Z: [zZ] ;

SPACES: [ \u000B\t\r\n] -> channel(HIDDEN) ;

