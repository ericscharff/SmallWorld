EVAL Class addNewClass: ( nil subclass: 'Object' variables: '' classVariables: '')
METHOD Object
= arg
	^ self == arg

!
METHOD Object
== arg
      " primitive object equality test"
      ^ <1 self arg>

!
METHOD Object
class   " get class of object "
	^ <2 self>

!
METHOD Object
edit  | w m p |
   w <- Window new title: 'Object editor: ' + self printString.
   w width: 300 height: 200.
   p <- Pane list: self class instanceVariables 
action: [:i | (Object in: self at: i) edit: [:nv | Object in: self at: i put: nv] ].
   w setPane: p. m <- Menu new: 'information'. 
   m on: 'printString' do: [ self printString inspect ].
   m on: 'class' do: [ self class inspect ].
   w addMenu: m.
   w show
!
METHOD Object
edit: cb   | w m p |
   w <- Window new title: 'Object editor: ' + self printString.
   w width: 300 height: 200.
   p <- Pane list: self class instanceVariables action: 
      [ :i | (Object in: self at: i) edit: [:nv | Object in: self at: i put: nv]].
   w setPane: p.
   m <- Menu new: 'information'. 
   m on: 'printString' do: [ self printString inspect ].
   m on: 'class' do: [ self class inspect ].
   m on: 'eval' do: [ Window eval: cb ].
   m on: 'save' do: [ cb value: self ].
   w addMenu: m.
   w show
!
METHOD Object
error: str  | w t |
   t <- Pane textArea. 
   t setText: (str + Char newline asString + Context current backtrace).
   w <- Window new title: 'Error'. w width: 300 height: 300.
   w setPane: (Pane north: nil center: t south: (Pane title: 'close' action: [w close])).
   w show.  Object halt
!
METHOD Object
inspect   | w m |
   w <- Window new title: 'Object inspector: ' + self printString.
   w width: 300 height: 200.
   w setPane: (Pane list: self class instanceVariables action: [:i | (Object in: self at: i) inspect ]).
   m <- Menu new: 'information'. 
   m on: 'printString' do: [ self printString inspect ].
   m on: 'class' do: [ self class inspect ].
   w addMenu: m.
   w show
!
METHOD Object
isKindOf: aClass	| clas |
	clas <- self class.
	[ clas notNil ] whileTrue:
		[ clas == aClass ifTrue: [ ^ true ].
		  clas <- clas superclass ].
	^ false

!
METHOD Object
isMemberOf: aClass
	^ self class == aClass

!
METHOD Object
isNil
	^ false

!
METHOD Object
notNil
	^ true

!
METHOD Object
printString
	^ self class printString

!
METHOD Object
respondsTo: aMessage
	^ self class allMethods includesTest: [:m | m name = aMessage]

!
METHOD Object
~= arg
	^ (self = arg) not

!
META Object
halt  " primitive thread death "
   <34>
!
META Object
in: v add: nv "increase object with value "
  ^ <32 nv v>
!
META Object
in: o at: i 
   " set value. Dangerous, use carefully "
   ^ <30 o i>
!
META Object
in: o at: i put: v
   " object modification. Dangerous. Use carefully "
   ^ <5 v o i>

!
META Object
in: obj initialize: n with: val
   ^ self in: obj at:
      (variables indexOf: n
         ifAbsent: [ ^ self error: 'cannot initalize unknown name ' + name ])
      put: val
!
