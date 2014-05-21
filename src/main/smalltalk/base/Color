EVAL Class addNewClass: ( Object subclass: 'Color' variables: 'value ' classVariables: '')
METHOD Color
asInteger
  ^ value
!
METHOD Color
printString
  ^ 'Color [red ' + (value quo: 256*256) + ' green ' + 
      ((value quo: 256) rem: 256) + ' blue ' + (value rem: 256) + ']'
!
METHOD Color
setValue: v
  value <- v
!
META Color
black
  ^ self red: 0 green: 0 blue: 0
!
META Color
blue
  ^ self red: 0 green: 0 blue: 255
!
META Color
green
  ^ self red: 0 green: 255 blue: 0
!
META Color
red
  ^ self red: 255 green: 0 blue: 0
!
META Color
red: r green: g blue: b
  ^ self new setValue: ((r * 256) + g)*256 + b
!
META Color
select | w i r g b p c s k t |
   " color selection window "
  i <- Image size: 200@200. t <- Pane textLine.
  k <- [:x | c <- Color red: r getIndex green: g getIndex blue: b getIndex.
      i setColor: c. i at: 0@0 fillRect: 200@200. t setText: c printString. w repaint ].
  r <- Pane vSliderFrom: 0 to: 255 do: k.
  g <- Pane vSliderFrom: 0 to: 255 do: k.
  b <- Pane vSliderFrom: 0 to: 255 do: k.
  s <- Semaphore new.
  w <- Window new title: 'Color Selector'. w width: 300 height: 250.
  w setPane: (Pane north: t
    south: (Pane title: 'return' action: [ s set: c. w close ]) east: nil 
     west: (Pane row: #(r g b)) center: (Pane image: i)).
  w show. ^ s wait
!
META Color
white
  ^ self red: 255 green: 255 blue: 255
!
