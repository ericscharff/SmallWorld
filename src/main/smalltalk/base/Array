EVAL Class addNewClass: ( Indexed subclass: 'Array' variables: '' classVariables: '')
METHOD Array
+ aValue	| size1 size2 newValue |
	" catenate two arrays together "
	size1 <- self size.
	size2 <- aValue size.
	newValue <- self class new: (size1 + size2).
	1 to: size1 do: [ :i | newValue at: i put: (self at: i) ].
	1 to: size2 do: [ :i | newValue at: (size1 + i) put: (aValue at: i)].
	^ newValue

!
METHOD Array
add: newItem   " optimized as a primitive "
   ^ <31 newItem self>

!
METHOD Array
asString
  ^ self inject: '' into: [:str :element | 
      str + element + ' ']
!
METHOD Array
asStringArray
   ^ (self collect: [:n | n printString]) asArray 
!
METHOD Array
asTextArray | na |
   <18 'THIS SHOULD NOT BE CALLED (asTextArray)'>.
   na <- Array new: self size.
   1 to: self size do: [:i | na at: i put: (self at: i) printString].
   ^ na

!
METHOD Array
at: index ifAbsent: exceptionBlock
	(self includesKey: index)
	     ifTrue: [ ^ <30 self index>]
	     ifFalse: [ ^ exceptionBlock value ]

!
METHOD Array
at: index put: value
	(self includesKey: index)
		ifTrue: [<5 value self index>]
		ifFalse: [ ^ self error: 'array indexing error '
			+ self size printString + ' '+ index printString ]

!
METHOD Array
copy
	^ self asArray

!
METHOD Array
edit: cb    | w m p n |
   w <- Window new title: 'Array editor'.
   w width: 300 height: 200.
   p <- Pane list: self asStringArray action: 
        [:i | (self at: i) edit: [:nv | self at: i put: nv. p setList: self asStringArray ] ].
   w setPane: p.
   m <- Menu new: 'information'.
   m on: 'size' do: [ (self size) inspect ].
   m on: 'add ...' do: [ Window eval: [:nv | n <- self add: nv. cb value: n ]. w close. n edit: cb].
   m on: 'remove selected' do: [ n <- self removeAt: p indexSelected. cb value: n. w close. n edit: cb ].
   m on: 'change to...' do: [ Window eval: cb. w close ].
   w addMenu: m.
   w show
!
METHOD Array
from: start size: sz
  ^ self from: start to: (start + sz - 1)
!
METHOD Array
from: low to: high | start size newArray |
	start <- low max: 0.
	size <- (high min: self size) + 1 - start max: 0.
	newArray <- self class new: size.
	1 to: size do: [:i | 
		newArray at: i put: (self at: start).
		start <- start + 1].
	^ newArray

!
METHOD Array
inspect    | w m |
   w <- Window new title: 'Array inspector'.
   w width: 300 height: 200.
   w setPane: (Pane list: self asStringArray action: [:i | (self at: i) inspect ]).
   m <- Menu new: 'information'.
   m on: 'size' do: [ (self size) inspect ].
   w addMenu: m.
   w show

!
METHOD Array
printString
	^ '#' + super printString

!
METHOD Array
removeAt: i | na |
   (i between: 1 and: self size) ifFalse: [ ^ self].
   na <- Array new: self size - 1.
   (1 to: i-1) do: [:j | na at: j put: (self at: j)].
   (i+1 to: self size) do: [:j | na at: j-1 put: (self at: j)].
   ^ na
!
METHOD Array
size
	" compute number of elements "
	^ <4 self>

!
METHOD Array
with: newItem
  <18 ( 'WITH SHOULD NOT BE USED ANY MORE' + Context current backtrace)>.
    " optimized as a primitive "
	^ <31 newItem self>

!
META Array
new
	^ self error: 'array values cannot be created with new'

!
META Array
new: size
	^ <7 self size> 

!
