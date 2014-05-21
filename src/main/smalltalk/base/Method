EVAL Class addNewClass: ( Object subclass: 'Method' variables: 'name byteCodes literals stackSize temporarySize class text ' classVariables: '')
METHOD Method
byteCodes
	^ byteCodes

!
METHOD Method
literals
	^ literals

!
METHOD Method
name
	^ name

!
METHOD Method
printString
	^ name

!
METHOD Method
stackSize
	^ stackSize

!
METHOD Method
tempSize
	^ temporarySize

!
METHOD Method
text
	^ text

!
META Method
name: n byteCodes: b literals: l stackSize: s temporarySize: ts class: c text: t
	| newMethod |
	newMethod <- self new.
	super in: newMethod at: 1 put: n.
	super in: newMethod at: 2 put: b.
	super in: newMethod at: 3 put: l.
	super in: newMethod at: 4 put: s.
	super in: newMethod at: 5 put: ts.
	super in: newMethod at: 6 put: c.
	super in: newMethod at: 7 put: t.
	^ newMethod

!
