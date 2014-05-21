EVAL Class addNewClass: ( Object subclass: 'Pane' variables: '' classVariables: '')
METHOD Pane
getIndex  " get index from list pane "
   ^ <83 self>
!
METHOD Pane
getSelectedText
   ^ <81 String self>

!
METHOD Pane
getText
   ^ <80 String self>
!
METHOD Pane
onMouseDown: b  "add mouse action "
   <86 self [:x :y | b value: x@y] >
!
METHOD Pane
onMouseMove: b  "add mouse action " | t |
   <88 self [:x :y |   t <- x@y.  b value: t] >
!
METHOD Pane
onMouseUp: b  " on mouse up do action "
   <87 self  [:x :y | b value: x@y] >
!
METHOD Pane
setImage: img " set image on pane "
   < 77 self img>
!
METHOD Pane
setList: d  "set data in list pane"
   ^ <84 self d>
!
METHOD Pane
setText: s " set text in text pane "
   ^ <82 self s>
!
META Pane
column: p
  ^ self rows: p size columns: 1 data: p
!
META Pane
hSliderFrom: low to: high do: b 
  " make horizontal slider "
  ^ <85 self false low high b>
!
META Pane
image: img
  ^ (self title: '') setImage: img
!
META Pane
list: d action: b  "create a list pane"
   ^ <75 self d b>
!
META Pane
north: n center: c south: s
   ^ self north: n south: s east: nil west: nil center: c
!
META Pane
north: n south: s east: e west: w center: c
   ^ <76 self n s e w c>
!
META Pane
row: p
  ^ self rows: 1 columns: p size data: p
!
META Pane
rows: r columns: c data: d
  ^ <74 self c r d>
!
META Pane
textArea  " create a multi-line text area"
   ^ <73 self>
!
META Pane
textLine "create a one-line text pane"
  ^ <72 self>

!
META Pane
title: t  "create a simple label pane "
   ^ <70 self t>
!
META Pane
title: t action: b  "create a button pane"
   ^ <71 self t b>
!
META Pane
vSliderFrom: low to: high do: b 
  " make vertical slider "
  ^ <85 self true low high b>
!
META Pane
west: w east: e
  ^ self north: nil south: nil east: e west: w center: nil
!
META Pane
west: w east: e center: c
  ^ self north: nil south: nil east: e west: w center: c
!
