EVAL Class addNewClass: ( ParserNode subclass: 'TemporaryNode' variables: 'position ' classVariables: '')
METHOD TemporaryNode
position: p
	position <- p

!
METHOD TemporaryNode
compile: encoder block: inBlock
	encoder genHigh: 3 low: position - 1

!
METHOD TemporaryNode
assignable
	^ true

!
METHOD TemporaryNode
assign: encoder
	encoder genHigh: 7 low: position - 1

!
