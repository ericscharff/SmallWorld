EVAL Class addNewClass: ( Object subclass: 'Image' variables: 'color ' classVariables: '')
METHOD Image
at: loc drawImage: img
  <113 self (loc y) (loc x) img>
!
METHOD Image
at: loc drawLine: to 
  <115 self (color asInteger) (loc y) (loc x) (to y) (to x) 5>
!
METHOD Image
at: loc drawOval: s
  <115 self (color asInteger) (loc y) (loc x) (s y - loc y) (s x - loc x) 1>
!
METHOD Image
at: loc drawRect: s
  <115 self (color asInteger) (loc y) (loc x) (s y - loc y) (s x - loc x) 3>
!
METHOD Image
at: loc fillOval: s
  <115 self (color asInteger) (loc y) (loc x) (s y - loc y) (s x - loc x) 2>
!
METHOD Image
at: loc fillRect: s
  <115 self (color asInteger) (loc y) (loc x) (s y - loc y) (s x - loc x) 4>
!
METHOD Image
at: loc text: t
  <114 self (color asInteger) (loc y) (loc x) t>
!
METHOD Image
edit   | w p pp b s |
  w <- Window new title: 'Image editor'. w width: 300 height: 300.
   p <- Pane image: self. pp <- nil. s <- Semaphore new.
   p onMouseDown: [:np | pp <- np].
   p onMouseUp: [:np | pp <- nil ].
   p onMouseMove: [:np | pp notNil ifTrue: 
      [ self at: pp drawLine: np. pp <- np. w repaint ] ].
   b <- Array new: 2.
   b at: 1 put: (Pane title: 'color' action: [ self setColor: Color select ]).
   b at: 2 put: (Pane title: 'close' action: [ s set: self. w close ]).
   w setPane: (Pane north: nil south: nil east: nil west:
       (Pane rows: 2 columns: 1 data: b) center: p).
   w show. ^ s wait
!
METHOD Image
setColor: c
   color <- c
!
META Image
fromFile: n
   ^ <111 self n>
!
META Image
size: s
  ^ self width: (s x) height: (s y)
!
META Image
width: w height: h " make a blank image of given size "
  ^ Object in: <110 self w h> add: Color black
!
