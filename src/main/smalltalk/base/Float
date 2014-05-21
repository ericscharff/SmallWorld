EVAL Class addNewClass: ( Number subclass: 'Float' variables: '' classVariables: '')
METHOD Float
* arg
	^ arg multByFloat: self
!
METHOD Float
+ arg
	^ arg addToFloat: self
!
METHOD Float
- arg
	^ arg subtractFromFloat: self
!
METHOD Float
/ arg
	^ arg divideByFloat: self
!
METHOD Float
< arg
	^ arg asFloat lessThanFloat: self
!
METHOD Float
= arg
	^ arg asFloat equalToFloat: self
!
METHOD Float
addToFloat: arg
	^ <51 Float arg self>
!
METHOD Float
asFloat
	^ self
!
METHOD Float
asInteger
  "convert self into integer"
  ^ <57 self>
!
METHOD Float
divideByFloat: arg
	^ <54 Float self arg>
!
METHOD Float
equalToFloat: arg
	^ <56 self arg>
!
METHOD Float
lessThanFloat: arg
	^ <55 self arg>
!
METHOD Float
lessThanSmallInt: s
  ^ s asFloat < self
!
METHOD Float
multByFloat: arg
	^ <53 Float self arg>
!
METHOD Float
printString
	^ <59 String self>
!
METHOD Float
subtractFromFloat: arg
	^ <52 Float self arg>
!
META Float
new
	^ self error: 'floats cannot be created with new'
!
META Float
random
  " return a new random number "
   ^ <58 self>
!
