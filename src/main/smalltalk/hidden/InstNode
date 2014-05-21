EVAL Class addNewClass: ( ParserNode subclass: 'InstNode' variables: 'position ' classVariables: '')
METHOD InstNode
position: p
	position <- p

!
METHOD InstNode
compile: encoder block: inBlock
	encoder genHigh: 1 low: position - 1

!
METHOD InstNode
assign: encoder
	encoder genHigh: 6 low: position - 1

!
METHOD InstNode
assignable
	^ true

!
