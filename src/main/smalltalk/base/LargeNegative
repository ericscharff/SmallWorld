EVAL Class addNewClass: ( Integer subclass: 'LargeNegative' variables: 'values ' classVariables: '')
METHOD LargeNegative
* arg
	^ (self negated * arg) negated
!
METHOD LargeNegative
+ arg
	^ arg - self negated
!
METHOD LargeNegative
- arg
	^ (self negated + arg) negated
!
METHOD LargeNegative
< arg
	^ (self negated < arg negated) not
!
METHOD LargeNegative
= arg
	^ self negated = arg negated
!
METHOD LargeNegative
addToLP: arg
	" private internal method "
	^ arg - self negated
!
METHOD LargeNegative
asFloat
	^ self negated asFloat negated
!
METHOD LargeNegative
compareToLP: arg
	" we are always smaller "
	^ -1
!
METHOD LargeNegative
multByLP: arg
	^ (self negated multByLP: arg) negated
!
METHOD LargeNegative
multBySmallInt: arg
	^ (self negated multBySmallInt: arg) negated
!
METHOD LargeNegative
negated
	^ LargePositive new: values
!
METHOD LargeNegative
printString
	^ '-' + self negated printString
!
METHOD LargeNegative
subtractFromLP: arg
	" private internal method "
	^ arg + self negated
!
META LargeNegative
new: v
	^ self in: super new at: 1 put: v
!
