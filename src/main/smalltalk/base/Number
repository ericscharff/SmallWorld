EVAL Class addNewClass: ( Magnitude subclass: 'Number' variables: '' classVariables: '')
METHOD Number
abs
	^ self negative ifTrue: [ self negated] ifFalse: [ self ]
!
METHOD Number
addToFloat: arg
	^ arg + self asFloat
!
METHOD Number
addToFraction: arg
	^ arg asFloat + self asFloat
!
METHOD Number
addToSmallInt: arg
	^ arg asFloat + self
!
METHOD Number
divideByFloat: arg
	^ arg / self asFloat
!
METHOD Number
divideBySmallInt: arg
	^ arg asFloat / self
!
METHOD Number
equalToFloat: arg
	^ arg = self asFloat
!
METHOD Number
lessThanFloat: arg
	^ arg < self asFloat
!
METHOD Number
multByFloat: arg
	^ arg * self asFloat
!
METHOD Number
multBySmallInt: arg
	^ arg asFloat * self
!
METHOD Number
negated
	^ 0 - self
!
METHOD Number
negative
	^ self < 0
!
METHOD Number
subtractFromFloat: arg
	^ arg - self asFloat
!
METHOD Number
subtractFromSmallInt: arg
	^ arg asFloat - self
!
