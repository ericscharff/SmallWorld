EVAL Class addNewClass: ( Magnitude subclass: 'Char' variables: 'value ' classVariables: '')
METHOD Char
< aChar
	^ value < aChar value

!
METHOD Char
= aChar
	^ value = aChar value

!
METHOD Char
asString | r |
	" return char as a string value "
	r <- String new: 1.
	r at: 1 put: self.
	^ r

!
METHOD Char
isAlphabetic
	^ self isLowerCase or: [ self isUpperCase ]

!
METHOD Char
isAlphanumeric
		" are we a letter or a digit? "
	^ self isAlphabetic or: [ self isDigit ]

!
METHOD Char
isBlank
		"spaces, tabs and newlines are all blank"
	^ value = 32 or: [ value = 9 or: [ value = 10 ] ]

!
METHOD Char
isDigit
	^ self between: $0 and: $9

!
METHOD Char
isLowerCase
	^ self between: $a and: $z

!
METHOD Char
isUpperCase
	^ self between: $A and: $Z

!
METHOD Char
lowerCase
	self isUpperCase
		ifTrue: [ ^ Char new: (value - 65) + 97 ]

!
METHOD Char
printString | r |
	r <- String new: 2.
	r at: 1 put: $$.
	r at: 2 put: self.
	^ r

!
METHOD Char
upperCase
	self isLowerCase
		ifTrue: [ ^ Char new: (value - 97) + 65 ]

!
METHOD Char
value
  " return our ascii value as an integer "
  ^ value

!
META Char
new: v
	" create and initialize a new char "
	^ self in: super new initialize: 'value' with: v
!
META Char
newline
		" return newline character "
	^ self new: 10

!
