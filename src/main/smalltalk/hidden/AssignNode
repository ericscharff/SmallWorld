EVAL Class addNewClass: ( ParserNode subclass: 'AssignNode' variables: 'target expression ' classVariables: '')
METHOD AssignNode
target: t expression: e
	target <- t.
	expression <- e

!
METHOD AssignNode
compile: encoder block: inBlock
	expression compile: encoder block: inBlock.
	target assign: encoder

!
