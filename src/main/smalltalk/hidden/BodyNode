EVAL Class addNewClass: ( ParserNode subclass: 'BodyNode' variables: 'statements ' classVariables: '')
METHOD BodyNode
statements: s
	statements <- s

!
METHOD BodyNode
compile: encoder block: inBlock
	statements do: 
		[ :stmt | stmt compile: encoder block: inBlock.
			encoder genHigh: 15 low: 5 " pop "].
	encoder genHigh: 15 low: 1 " return self "

!
