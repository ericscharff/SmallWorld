EVAL Class addNewClass: ( Object subclass: 'Class' variables: 'name parentClass methods size variables ' classVariables: 'classes Parser ')
METHOD Class
addClassVariable: n  " put new class variable in class - dangerous "
   Object in: self add: nil.
   Object in: self class at: 5 put: (self class variables add: n).
   size <- size + 1
!
METHOD Class
addInstanceVariable: n
   variables <- variables add: n.
   size <- size + 1
!
METHOD Class
allMethods
	^ parentClass isNil
		ifTrue: [ methods asList ]
		ifFalse: [ methods asList addAll: parentClass allMethods ]

!
METHOD Class
compileMethod: aString | method |
	method <- self parseMethod: aString.
	method notNil 
		ifTrue: [ methods <- (((methods select: [:m | m name ~= method name])
				add: method) 
				asOrdered: [:a :b | a name < b name]) asArray ]
		ifFalse: [ self error: 'cannot compile method'].
	^ true
!
METHOD Class
edit | w t l ba |
   w <- Window new title: 'Class Editor: ' + name. w size: 500@300.
   t <- Pane textArea. 
   l <- Pane list: methods asStringArray
        action: [:i | t setText: (methods at: i) text].
   ba <- #((Pane title: 'clear text area' action: [ t setText: '' ])
      (Pane title: 'compile' action: 
      [ (self compileMethod: t getText) ifTrue: [ Window notify: 'compiliation successful' ].
          l setList: methods asStringArray. w repaint])
      (Pane title: 'delete method' action: 
     [ methods <- methods removeAt: l getIndex. l setList: methods asStringArray. t setText: ''. w repaint  ])
      (Pane title: 'instance variables' action: [ t setText: variables asString])
      (Pane title: 'edit class variables' action: [ self editClassVariables ])
      (Pane title: 'examine superclass' action: [ self superclass notNil ifTrue: [ self superclass edit] 
         ifFalse: [ Window notify: 'no superclass']])
      (Pane title: 'examine metaclass' action: [ self class edit ])
      (Pane title: 'object edit' action: [super edit])
      (Pane title: 'close' action: [ w close]) ).
   w setPane: ( Pane west: (Pane column: ba) east: l center: t).
   w show
!
METHOD Class
editClassVariables  | w p |
   w <- Window new title: 'Class Variable Editor: ' + name.
   w width: 200 height: 100.
   p <- Pane list: self class variables action: 
      [:i | (Object in: self at: i+5) edit: [:nv | Object in: self at: i+5 put: nv] ].
   w setPane: p.
   w show
!
METHOD Class
fileOut   " file out class methods "
  ^ self fileTo: (File openWrite: name)
!
METHOD Class
fileTo: aFile
  aFile write: 'EVAL Class addNewClass: ( ' + parentClass name + ' subclass: ' + $' asString +
	name + $' asString + ' variables: ' + $' asString + variables asString
	+ $' asString + ' classVariables: ' +$' asString +
	self class variables asString + $' asString + ')' + Char newline asString.
  methods do: [:m | aFile write: 'METHOD ' + name + Char newline asString +
   m text + Char newline asString + '!' + Char newline asString].
  self class methods do: [:m | aFile write: 'META ' + name + Char newline asString +
    m text + Char newline asString + '!' + Char newline asString].
  ^ 'File out successful'

!
METHOD Class
hierarchy
	^ self hierarchy: 0

!
METHOD Class
hierarchy: n | result |
	result <- ''.
	1 to: n do: [:i | result <- result + '   '].
	result <- result + name.
	self subclasses do: [:c |
		result <- result + Char newline asString + (c hierarchy: n+1) ]
	^ result

!
METHOD Class
instanceVariables	| names |
		" return all our variable names "
	parentClass notNil
		ifTrue: [ names <- parentClass instanceVariables ]
		ifFalse: [ names <- Array new: 0 ].
	variables notNil
		ifTrue: [ names <- names + variables ].
	^ names

!
METHOD Class
methodIncludesText: str | r |
   r <- ''.  methods do:
       [ :m | (m text find: str) notNil ifTrue: [ r <- r + Char newline asString + name + ':' + m name]].
   ^ r 
!
METHOD Class
methods
	^ methods

!
METHOD Class
name
	^ name

!
METHOD Class
name: n
	name <- n
!
METHOD Class
name: n parent: p variables: v
	" private method used for initialization "
	name <- n.
	parentClass <- p.
	variables <- v break.
	methods <- Array new: 0.
	size <- p size + v size

!
METHOD Class
new
	" return a new instace of ourselves "
	^ <7 self size>

!
METHOD Class
parseMethod: text
   ^ (Class parser  text: text instanceVars: self instanceVariables classVars: self class variables) parse: self

!
METHOD Class
printString
	^ name printString

!
METHOD Class
removeMethod: n
	methods <- (methods select: [:m | m name ~= n]) asArray
!
METHOD Class
size
	^ size

!
METHOD Class
subclass  | w n iv cv a s |
   w <- Window new title: 'Class Creation Form'. w width: 400 height: 150.
   n <- Pane textLine. iv <- Pane textLine. cv <- Pane textLine.
   a <- Array new: 8. a at: 1 put: (Pane title: 'name'). s  <- Semaphore new.
   a at: 2 put: n. a at: 3 put: (Pane title: 'instance variables'). a at: 4 put: iv.
   a at: 5 put: (Pane title: 'class variables'). a at: 6 put: cv.  a at: 7 put: (Pane title: '').
   a at: 8 put: (Pane title: 'create class' action: 
    [ s set: (self subclass: n getText variables: iv getText  classVariables: cv getText ). w close]).
   w setPane: (Pane rows: 4 columns: 2 data: a). w show. ^ s wait
!
METHOD Class
subclass: n variables: v classVariables: cv | meta |
	meta <- Class new name: 'Meta' + n 
		printString parent: self class variables: cv.
	^ meta new name: n parent: self variables: v
!
METHOD Class
subclasses
	^ Class classes select: [:c | c superclass = self]

!
METHOD Class
superclass
	^ parentClass

!
METHOD Class
variables
	^ variables

!
META Class
addNewClass: aClass
  classes <- ((classes add: aClass) 
     asOrdered: [:a :b | a name < b name]) asArray
  ^ aClass

!
META Class
at: name ifAbsent: noValue
	classes do: [:x | name = x name ifTrue: [ ^ x ] ].
	^ noValue value

!
META Class
browser | w t p out b |
   t <- Pane textLine. out <- Pane textArea.
   p <- Pane list: classes asStringArray action: [:i | nil ].
   b <- #(
     (Pane title: 'evaluate expression' action: [ out setText: t getText doIt printString])
     (Pane title: 'examine class' action: [ (classes at: (p getIndex)) edit ])
     (Pane title: 'delete class' action:  [ (Window question: 'do you really want to remove this class?')
         ifTrue: [ self removeClass: ((classes at: (p getIndex)) name)]])
     (Pane title: 'clear' action:  [ t setText: ''. out setText: ''. p setList: classes asStringArray ])
     (Pane title: 'close' action: [ w close ])).
   w <- Window new title: 'Smalltalk Browser'. w width: 500 height: 200.
   w setPane: (Pane north: t south: nil east: p west:
    (Pane rows: b size columns: 1 data: b) center: out).
   w show.
!
META Class
classes
	^ classes

!
META Class
methodsIncludeText: str | r |
   r <- ''.
   classes do: [:c | r <- r + (c methodIncludesText: str) ].
   r <- r + (Parser methodIncludesText: str).
   ^ r + 'End Of List'
!
META Class
parser
   ^ Parser new
!
META Class
removeClass: aClass
	classes <- (classes reject: [:x | x name = aClass]) asArray.
	^ 'Class removed'

!
