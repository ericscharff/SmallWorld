EVAL Class addNewClass: ( Context subclass: 'Block' variables: 'argumentLocation creatingContext oldBytePointer ' classVariables: '')
METHOD Block
fork
   " must be a no arg block "
   <19 self>
!
METHOD Block
value
	" start block execution "
	<8 self>

!
METHOD Block
value: a
	" start block execution "
	<8 a self>

!
METHOD Block
value: a value: b
	" start block execution "
	<8 a b self>

!
METHOD Block
whileFalse: aBlock
	self value ifFalse: [ aBlock value. ^ self whileFalse: aBlock ]

!
METHOD Block
whileTrue: aBlock
	self value ifTrue: [ aBlock value. ^ self whileTrue: aBlock ]

!
