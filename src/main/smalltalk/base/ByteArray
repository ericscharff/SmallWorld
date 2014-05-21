EVAL Class addNewClass: ( Array subclass: 'ByteArray' variables: '' classVariables: '')
METHOD ByteArray
asCode | r |
	r <- '('.
	self do: [:element |
		r<- r+ (element quo: 16) + ':' + (element rem: 16) + ' '].
	^ r + ')'

!
METHOD ByteArray
at: index ifAbsent: exceptionBlock
	(self includesKey: index)
	     ifTrue: [ ^ <21 self index>]
	     ifFalse: [ ^ exceptionBlock value ]

!
METHOD ByteArray
at: index put: aValue
  (self includesKey: index)
    ifTrue: [(aValue between: 0 and: 255) ifTrue: [<22 aValue self index>]
      ifFalse: [self error: 'illegal insertion into byte array']]
    ifFalse: [self error: 'byte array indexing error' ]

!
META ByteArray
new: s
    "create a new byte array of given size. Done as primitive "
    ^ <20 ByteArray s>

!
