EVAL Class addNewClass: ( ParserNode subclass: 'LiteralNode' variables: 'value ' classVariables: '')
METHOD LiteralNode
value: v
	value <- v

!
METHOD LiteralNode
compile: encoder block: inBlock
	value == nil ifTrue: [ ^ encoder genHigh: 5 low: 10 ].
	value == true ifTrue: [ ^ encoder genHigh: 5 low: 11 ].
	value == false ifTrue: [ ^ encoder genHigh: 5 low: 12 ].
	(value class == SmallInt and: [ value between: 0 and: 9 ])
		ifTrue: [ ^ encoder genHigh: 5 low: value ].
	encoder genHigh: 4 low: (encoder genLiteral: value)

!
