EVAL Class addNewClass: ( Number subclass: 'Fraction' variables: 'top bottom ' classVariables: '')
METHOD Fraction
* arg
	^ arg multByFraction: self
!
METHOD Fraction
+ arg
	^ arg addToFraction: self
!
METHOD Fraction
- arg
	^ arg subtractFromFraction: self
!
METHOD Fraction
/ arg
	^ arg divideByFraction: self
!
METHOD Fraction
addToFraction: arg
	^ Fraction top: top * arg bottom + (bottom * arg top) bottom: bottom * arg bottom
!
METHOD Fraction
addToSmallInt: arg
	^ (Fraction top: arg) + self
!
METHOD Fraction
asFloat
	^ top asFloat / bottom asFloat
!
METHOD Fraction
bottom
	^ bottom
!
METHOD Fraction
divideByFraction: arg
	^ Fraction top: arg top * bottom bottom: arg bottom * top
!
METHOD Fraction
divideBySmallInt: arg
	^ (Fraction top: arg) / self
!
METHOD Fraction
multByFraction: arg
	^ Fraction top: top * arg top bottom: bottom * arg bottom
!
METHOD Fraction
multBySmallInt: arg
	^ (Fraction top: arg) * self
!
METHOD Fraction
printString
	^ '(' + top printString + '/' + bottom printString + ')'
!
METHOD Fraction
subtractFromFraction: arg
	^ Fraction top: arg top * bottom - (top * arg bottom) bottom: bottom * arg bottom
!
METHOD Fraction
subtractFromSmallInt: arg
	^ (Fraction top: arg) - self
!
METHOD Fraction
top
	^ top
!
META Fraction
top: t
	^ self in: (self in: super new at: 1 put: t) at: 2 put: 1
!
META Fraction
top: t bottom: b  | r |
	b = 0 ifTrue: [ ^ self error: ' division by zero in fraction'].
	r <- t gcd: b.
	^ self in: (self in: super new at: 1 put: (t quo: r)) at: 2 put: (b quo: r)
!
