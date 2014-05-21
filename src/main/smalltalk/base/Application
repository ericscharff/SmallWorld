EVAL Class addNewClass: ( Object subclass: 'Application' variables: 'win ' classVariables: '')
METHOD Application
repaint  " repaint window "
  ^ win repaint. 100 sleep
!
METHOD Application
run
   " subclass must override to perform action "
  ^ nil
!
METHOD Application
start
  " subclass should override and call title:size:pane: "
  ^ nil
!
METHOD Application
title: t size: s pane: p  |  m | 
    "create an application window "
   win <- Window new. 
   win title: t. 
   win size: s.
   win setPane: p.
   m <- Menu new: 'run'.
   m on: 'quit' do: [ Object halt ].
   m on: 'close' do: [ win close ].
   win addMenu: m.
   win show. ^ win
!
META Application
new  | r | " create and initialize a new application "
   r <- super new.  r start.  r run. ^ r
!
