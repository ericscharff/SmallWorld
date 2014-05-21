EVAL Class addNewClass: ( Object subclass: 'Context' variables: 'method arguments temporaries stack bytePointer stackTop previousContext ' classVariables: '')
METHOD Context
backtrace 
  ^  method name + (arguments inject: '[' into: [:r :a | r + a class printString + ','])
        + ']' + Char newline asString + 
        (previousContext notNil ifTrue: [  previousContext backtrace ] ifFalse: [ ' '])
!
METHOD Context
method: aMethod arguments: a
	method <- aMethod.
	arguments <- a.
	temporaries <- Array new: aMethod tempSize.
	bytePointer <- 0.
	stack <- Array new: method stackSize.
	stackTop <- 0
!
METHOD Context
perform: aMethod withArguments: a
	method <- aMethod.
	arguments <- a.
	temporaries <- Array new: aMethod tempSize.
	bytePointer <- 0.
	stack <- Array new: method stackSize.
	stackTop <- 0.
	^ <6 self>

!
META Context
current
   ^ <35>
!
