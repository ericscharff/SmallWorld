EVAL Class addNewClass: ( Magnitude subclass: 'Collection' variables: '' classVariables: '')
METHOD Collection
addAll: aCollection
   aCollection do: [:element | self add: element ]
!
METHOD Collection
asArray		| newArray index |
	newArray <- Array new: self size.
	index <- 1.
	self do: [ :element | newArray at: index put: element.
		index <- index + 1 ].
	^ newArray

!
METHOD Collection
asByteArray		| newArray index |
	newArray <- ByteArray new: self size.
	index <- 1.
	self do: [ :element | newArray at: index put: element.
		index <- index + 1 ].
	^ newArray

!
METHOD Collection
asList
	^ List new addAll: self

!
METHOD Collection
asOrdered
	^ Ordered new addAll: self

!
METHOD Collection
asOrdered: testBlock
	^ (Ordered new: testBlock) addAll: self

!
METHOD Collection
asString	| newString index |
	newString <- String new: self size.
	index <- 1.
	self do: [ :element | newString at: index put: element.
		index <- index + 1 ].
	^ newString

!
METHOD Collection
collect: transformBlock
	^ self inject: List new into: [:list :element | 
		list add: (transformBlock value: element)]

!
METHOD Collection
find: testBlock ifAbsent: exceptionBlock
	self do: [:element | (testBlock value: element) ifTrue: [ ^ element ] ].
	^ exceptionBlock value

!
METHOD Collection
includes: value
	self do: [:element | element = value ifTrue: [ ^ true]].
	^ false

!
METHOD Collection
includesTest: aBlock
	self do: [:element | (aBlock value: element) ifTrue: [ ^ true]].
	^ false

!
METHOD Collection
inject: value into: binaryBlock | result |
	result <- value.
	self do: [:element | result <- binaryBlock value: result value: element ].
	^ result

!
METHOD Collection
isEmpty
		" empty if there are no elements "
	^ self size = 0

!
METHOD Collection
printString
	^ (self inject: '(' into: [:str :element | str + element + ' '] ) + ')'

!
METHOD Collection
reject: testBlock
	^ self select: [:x | (testBlock value: x) not ]

!
METHOD Collection
select: testBlock
	^ self inject: List new into: [:list :element | 
		(testBlock value: element) ifTrue: [ list add: element]. list ]

!
METHOD Collection
size
	^ self inject: 0 into: [:tally :element | tally + 1]

!
