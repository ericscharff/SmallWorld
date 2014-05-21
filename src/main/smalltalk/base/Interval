EVAL Class addNewClass: ( Collection subclass: 'Interval' variables: 'low high step ' classVariables: '')
METHOD Interval
do: aBlock	| current |
	current <- low.
	[ current <= high ] whileTrue:
		[ aBlock value: current.  current <- current + step ]

!
METHOD Interval
reverseDo: aBlock  | current |
	current <- high.
	[ current >= low ] whileTrue: 
		[ aBlock value: current.  current <- current - step ]

!
META Interval
from: l to: h by: s | newInterval |
	newInterval <- self in: self new at: 1 put: l.
	self in: newInterval at: 2 put: h.
	self in: newInterval at: 3 put: s.
	^ newInterval

!
