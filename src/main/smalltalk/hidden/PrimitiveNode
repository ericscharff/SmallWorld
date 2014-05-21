EVAL Class addNewClass: ( ParserNode subclass: 'PrimitiveNode' variables: 'number arguments ' classVariables: '')
METHOD PrimitiveNode
number: n arguments: a
	number <- n.
	arguments <- a.

!
METHOD PrimitiveNode
compile: encoder block: inBlock
	arguments do: [ :a | a compile: encoder block: inBlock ].
	encoder genHigh: 13 low: arguments size.
	encoder genCode: number

!
