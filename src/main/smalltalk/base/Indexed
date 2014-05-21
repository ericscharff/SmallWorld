EVAL Class addNewClass: ( Collection subclass: 'Indexed' variables: '' classVariables: '')
METHOD Indexed
at: index
	^ self at: index ifAbsent: [self error: 'out of range index']

!
METHOD Indexed
do: aBlock
	^ 1 to: self size do: [:i | aBlock value: (self at: i) ]

!
METHOD Indexed
includesKey: index
	^ index between: 1 and: self size

!
METHOD Indexed
indexOf: element ifAbsent: exceptionBlock
	1 to: self size do: [:i | (element = (self at: i)) ifTrue: [ ^ i ]].
	^ exceptionBlock value

!
METHOD Indexed
reverseDo: aBlock
	^ (1 to: self size) reverseDo: [:i | aBlock value: (self at: i) ]

!
METHOD Indexed
with: aCollection pad: aValue do: aBlock | ls rs |
	1 to: ((ls <- self size) max: (rs <- aCollection size)) do:
		[:i | aBlock value: ((i <= ls) ifTrue: [ self at: i] ifFalse: [aValue])
			value: ((i <= rs) ifTrue: [ aCollection at: i] ifFalse: [aValue]) ]

!
