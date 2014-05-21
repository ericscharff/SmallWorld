EVAL Class addNewClass: ( Object subclass: 'File' variables: '' classVariables: '')
METHOD File
fileIn  | line cls |
	[(line <- self readLine) notNil] whileTrue: 
		[ ((line from: 1 to: 4) = 'EVAL')
			ifTrue: [ (line from: 6 to: line size) doIt ].
		  ((line from: 1 to: 6) = 'METHOD')
			ifTrue: [ line <- line from: 8 to: line size.
				cls <- Class at: line ifAbsent: [ self error: 'unknown name in fileIn ' + line].
				cls compileMethod: (self readTo: '!') ].
		  ((line from: 1 to: 4) = 'META')
			ifTrue: [ line <- line from: 6 to: line size.
				cls <- Class at: line ifAbsent: [ self error: 'unknown name in fileIn ' + line].
				cls class compileMethod: (self readTo: '!') ] ].
	^ 'File In Successful'

!
METHOD File
readLine
	^ <44 String self>

!
METHOD File
readTo: end  | line result |
	result <- ''.
	[(line <- self readLine) notNil and: [ line ~= end]]
		whileTrue: [ result <- result + line + Char newline asString ].
	^ result

!
METHOD File
write: aString
	<43 (aString printString) self>

!
META File
fileIn: n
   (File openRead: n) fileIn
!
META File
openRead: name
	^ <42 self (name printString)>

!
META File
openWrite: name
	^ <41 self (name printString)>

!
META File
saveImage: n
   <29 (n printString)>.
   Window notify: 'Image saved in file ' + n.
   ^ 'Image saved in file ' + n

!
