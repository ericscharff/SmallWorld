EVAL Class addNewClass: ( Integer subclass: 'SmallInt' variables: '' classVariables: '')
METHOD SmallInt
* arg
	^ arg multBySmallInt: self

!
METHOD SmallInt
+ arg
	^ arg addToSmallInt: self

!
METHOD SmallInt
- arg
	^ arg subtractFromSmallInt: self

!
METHOD SmallInt
< arg
	^ arg lessThanSmallInt: self

!
METHOD SmallInt
= arg
	^ arg equalToSmallInt: self

!
METHOD SmallInt
addToLP: arg
    ^ arg + self asLargeInteger
!
METHOD SmallInt
addToSmallInt: arg	
	" this only gets invoked when overflow occurs "
	^ arg asLargeInteger + self asLargeInteger
!
METHOD SmallInt
asFloat
	^ <50 Float self>
!
METHOD SmallInt
asLargeInteger | newValue n |
	newValue <- List new.
	n <- self abs.
	[n > 0] whileTrue: [ newValue add: (n rem: 100). n <- n quo: 100].
	newValue size = 0 ifTrue: [ newValue add: 0].
	^ self < 0 ifTrue: [ LargeNegative new: newValue asByteArray] 
		ifFalse: [ LargePositive new: newValue asByteArray ]
!
METHOD SmallInt
compareToLP: arg
	^ self asLargeInteger compareToLP: arg
!
METHOD SmallInt
edit: cb | w m t|
   w <- Window new title: 'Integer editor'.
   w width: 300 height: 100.
   t <- Pane textArea. t setText: self printString.
   w setPane: t.
   m <- Menu new: 'edit'.
   m on: 'change to...' do: [cb value: t getText doIt ].
   w addMenu: m.
   w show

!
METHOD SmallInt
equalToSmallInt: arg
	^ <14 self arg>

!
METHOD SmallInt
inspect | w |
   w <- Window new title: 'Integer inspector'.
   w width: 200 height: 100.
   w setPane: (TextPanel new: self printString).
   w show

!
METHOD SmallInt
lessThanSmallInt: arg
	^ <13 arg self>

!
METHOD SmallInt
multByLP: arg
	^ self * arg
!
METHOD SmallInt
multBySmallInt: arg | r values |
   "must check for overflow "
   r <- <15 arg self>.
   r isNil ifTrue: [ r <- arg asLargeInteger * self asLargeInteger ].
   ^ r

!
METHOD SmallInt
printString
	(self < 0) ifTrue: [ ^ '-' + self negated printString].
	(self < 10) ifTrue: [ ^ (Char new: (self + 48)) asString ]
	ifFalse: [ ^ (self quo: 10) printString + (self rem: 10) printString ]

!
METHOD SmallInt
quo: arg
	(0 = arg) ifTrue: [^ self error: 'division by zero'].
	^ arg quoWithSmallInt: self

!
METHOD SmallInt
quoWithSmallInt: arg
	^ <11 arg self>

!
METHOD SmallInt
rem: arg
	(0 = arg) ifTrue: [^ self error: 'division by zero'].
	^ arg remWithSmallInt: self

!
METHOD SmallInt
remWithSmallInt: arg
	^ <12 arg self>

!
METHOD SmallInt
sleep " sleep for a while "
  <33 self>
!
METHOD SmallInt
subtractFromLP: arg
	" private internal method "
	^ arg - self asLargeInteger
!
METHOD SmallInt
subtractFromSmallInt: arg | r |
   " must check for overflow "
   r <- <16 arg self>.
   r isNil ifTrue: [ r <- arg asLargeInteger - self asLargeInteger ].
  ^ r

!
METHOD SmallInt
to: limit
	^ Interval from: self to: limit by: 1

!
METHOD SmallInt
to: limit by: step
	^ Interval from: self to: limit by: step

!
METHOD SmallInt
to: limit do: aBlock  | i |
		" optimize arithmetic loops "
	i <- self.
	[ i <= limit ] whileTrue: [ aBlock value: i. i <- i + 1 ]

!
META SmallInt
new
	" can't create this way, return zero "
	^ 0

!
