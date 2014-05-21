EVAL Class addNewClass: ( ParserNode subclass: 'ArgumentNode' variables: 'position ' classVariables: '')
METHOD ArgumentNode
position: p
	position <- p

!
METHOD ArgumentNode
isSuper
	^ position = 0

!
METHOD ArgumentNode
compile: encoder block: inBlock
	position = 0
		ifTrue: [ encoder genHigh: 2 low: 0 ]
		ifFalse: [ encoder genHigh: 2 low: position - 1 ]

!
