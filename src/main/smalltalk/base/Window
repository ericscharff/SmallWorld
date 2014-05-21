EVAL Class addNewClass: ( Object subclass: 'Window' variables: '' classVariables: '')
METHOD Window
addMenu: m
    <64 self m>
!
METHOD Window
close
   ^ <61 self false>
!
METHOD Window
repaint
  <66 self>
!
METHOD Window
setPane: c
   <62 self c>
!
METHOD Window
show
   <61 self true>
!
METHOD Window
size: p
  self width: p x height: p y
!
METHOD Window
title: t
   <65 self t>
!
METHOD Window
width: w height: h
   <63 self h w>
!
META Window
eval: cb  | w m t |
   w <- Window new title: 'Expression Window'.
   w width: 300 height: 100.
   t <- Pane textArea. t setText: 'type expression here'.
   w setPane: t.
   m <- Menu new: 'Edit'.
   m on: 'clear' do: [ t setText: '' ].
   m on: 'Evaluate and Save' do: [ cb value: t getText doIt. w close ].
   w addMenu: m.
   w show

!
META Window
getString: str   |w s p t | " put up a window to ask question "
   w <- self new title: 'get string'. w width: 200 height: 100.
   s <- Semaphore new.t <- Pane textLine.
   p <- Array new: 2.
   p at: 1 put: (Pane title: 'return' action: [ w close. s set: t getText ]).
   p at: 2 put: (Pane title: 'cancel' action: [ w close. Object halt ]).
   w setPane: (
      Pane north: (Pane title: str) center: t south: 
         (Pane rows: 1 columns: 2 data: p)).
   w show. ^ s wait
!
META Window
new
   ^ <60 self>
!
META Window
notify: str | w | " create a simple notification window"
   w <- Window new  title: 'notify'. w width: 200 height: 100.
   w setPane: (Pane north: nil center: (Pane title: str)
   south: (Pane title: 'close' action: [ w close])). 
   w show. ^ str
   
!
META Window
question: str   |w s p | " put up a window to ask question "
   w <- self new title: 'Question'. w width: 300 height: 150.
   s <- Semaphore new.
   p <- Array new: 3.
   p at: 1 put: (Pane title: 'yes' action: [ w close. s set: true ]).
   p at: 2 put: (Pane title: 'cancel' action: [ w close. Object halt ]).
   p at: 3 put: (Pane title: 'no' action: [ w close. s set: false ]).
   w setPane: (
      Pane north: (Pane title: str) center: nil south: 
         (Pane rows: 1 columns: 3 data: p)).
   w show. ^ s wait
!
