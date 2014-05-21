EVAL Class addNewClass: ( Array subclass: 'String' variables: '' classVariables: '')
METHOD String
+ arg
	" optimized by primitive for speed "
	^ <24 (arg printString) self>

!
METHOD String
< arg
	^ <26 self (arg printString)> < 0

!
METHOD String
= arg
	^ <26 self (arg printString)> = 0

!
METHOD String
at: index ifAbsent: exceptionBlock
	(self includesKey: index)
	     ifTrue: [ ^ Char new: <21 self index>]
	     ifFalse: [ ^ exceptionBlock value ]

!
METHOD String
at: index put: aValue
	(self includesKey: index)
		ifTrue: [<22 (aValue value) self index>]
		ifFalse: [self error: 'string indexing error' ]

!
METHOD String
break
   ^ (self break: ' ') asArray
!
METHOD String
break: separators  | words word |
	" break string into words, using separators "
	word <- ''.
	words <- List new.
	self do: [:c |
		(separators includes: c)
			ifTrue: [
				(word size > 0) " found a word "
					ifTrue: [ words add: word.
							word <- '' ] ]
			ifFalse: [ word <- word + c asString ] ].
		" maybe a last word "
	(word size > 0) ifTrue: [ words add: word ].
	^ words

!
METHOD String
copy
	" make a clone of ourself "
	^ <23 self String>

!
METHOD String
doIt	| meth |
	meth <- Object parseMethod: 'doItCommand ^' + self.
	meth notNil 
		ifTrue: [ ^ (Context new
			  perform: meth withArguments: (Array new: 1) )  ]

!
METHOD String
edit: cb | w t m |
   w <- Window new title: 'String Editor'.
   w width: 300 height: 100.
   t <- Pane textArea. t setText: self.
   w setPane: t.
   m <- Menu new: 'edit'.
   m on: 'save edits' do: [ cb value: t getText. w close ].
   m on: 'change to...' do: [ Window eval: cb. w close ].
   w addMenu: m.
   w show
!
METHOD String
find: substr
   1 to: (self size - substr size + 1) do: 
      [:i | (self from: i size: substr size) = substr ifTrue: [ ^ i] ].
   ^ nil
!
METHOD String
inspect | w t m |
   w <- Window new title: 'String Inspector'.
   w width: 300 height: 100.
   t <- Pane textArea. t setText: self.
   w setPane: t.
   w show
!
METHOD String
inspect: cb | w t m |
   w <- Window new title: 'String Inspector'.
   w width: 300 height: 100.
   t <- Pane textArea. t setText: self.
   w setPane: t.
   m <- Menu new: 'edit'.
   m on: 'change' do: [ cb value: t getText ].
   w addMenu: m.
   w show
!
METHOD String
last: n
	^ self from: self size - n + 1 to: self size
!
METHOD String
printString
	^ self

!
METHOD String
size
	^ <4 self>

!
META String
new: size
	^ <20 String size>

!
