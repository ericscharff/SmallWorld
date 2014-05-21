EVAL Class addNewClass: ( Number subclass: 'Integer' variables: '' classVariables: '')
METHOD Integer
/ arg
	^ arg divideByInteger: self
!
METHOD Integer
@ y
  ^ Point new x: self y: y
!
METHOD Integer
addToFraction: arg
	^ arg + (Fraction top: self)
!
METHOD Integer
addToSmallInt: arg
	" private internal method "
	^ arg asLargeInteger + self
!
METHOD Integer
asInteger
  ^ self
!
METHOD Integer
divideByFraction: arg
	^ arg / (Fraction top: self)
!
METHOD Integer
divideByInteger: arg
	^ Fraction top: arg bottom: self
!
METHOD Integer
equalToSmallInt: arg
	^ arg asLargeInteger = self
!
METHOD Integer
gcd: x  | n  m |
	" euclids gcd algorithm "
	n <- self. m <- x.
	(n <= 0 or: [m <= 0]) ifTrue: [ ^ 1].
	[ n = m ] whileFalse: [ n < m ifTrue: [ m <- m - n ] ifFalse: [ n <- n - m ]].
	^ n
!
METHOD Integer
lessThanSmallInt: arg
	" private internal method "
	^ arg asLargeInteger < self
!
METHOD Integer
multByFraction: arg
	^ arg * (Fraction top: self)
!
METHOD Integer
random
  ^ (Float random * self) asInteger
!
METHOD Integer
subtractFromFraction: arg
	^ arg - (Fraction top: self)
!
METHOD Integer
subtractFromSmallInt: arg
	" private method, used internally "
	^ arg asLargeInteger - self
!
