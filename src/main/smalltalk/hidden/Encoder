EVAL Class addNewClass: ( Object subclass: 'Encoder' variables: 'name byteCodes literals stackSize maxStack ' classVariables: '')
METHOD Encoder
backUp
	" back up one instruction "
	byteCodes removeAt: byteCodes size

!
METHOD Encoder
currentLocation
	^ byteCodes size

!
METHOD Encoder
genCode: byte
	byteCodes add: byte.
	^ byteCodes size

!
METHOD Encoder
genHigh: high low: low
	(low >= 16)
		ifTrue: [ self genHigh: 0 low: high. self genCode: low ]
		ifFalse: [ self genCode: high * 16 + low ]

!
METHOD Encoder
genLiteral: aValue
	literals <- literals add: aValue.
	^ literals size - 1

!
METHOD Encoder
method: maxTemps class: c text: text
	" stack size doesn't take into account primitives "
	^ Method name: name byteCodes: byteCodes asByteArray literals: literals 
		stackSize: maxStack+5 temporarySize: maxTemps class: c
		text: text

!
METHOD Encoder
name: n
	name <- n.
	byteCodes <- List new.
	literals <- Array new: 0.
	stackSize <- 0.
	maxStack <- 1.

!
METHOD Encoder
patch: loc
		" patch a goto from a block "
	byteCodes at: loc put: self currentLocation

!
METHOD Encoder
popArgs: n
	stackSize <- stackSize - n.

!
METHOD Encoder
pushArgs: n
	stackSize <- stackSize + n.
	maxStack <- stackSize max: maxStack

!
