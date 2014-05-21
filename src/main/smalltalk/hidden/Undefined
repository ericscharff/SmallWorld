EVAL Class addNewClass: ( Object subclass: 'Undefined' variables: '' classVariables: '')
METHOD Undefined
isNil
	" yes, we are nil "
	^ true

!
METHOD Undefined
notNil
	" no, we are not not-nil "
	^ false

!
METHOD Undefined
printString
	^ 'nil'

!
METHOD Undefined
test   | d w |
   d <- #('abc' 'def' 'ghi').
   w <- Window new title: 'List test'.
   w width: 200 height: 100.
   w setPane: (ListPane data: d do: [:i | <18 i>. <18 (d at: i)>] ).
   w show

!
METHOD Undefined
version
	^ 'image version 0.82'
!
META Undefined
new
	" there is only one nil object "
	^ nil

!
