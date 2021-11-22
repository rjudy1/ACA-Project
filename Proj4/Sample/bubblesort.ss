      --A program to implement bubblesort on an array of numbers
      -- 4000 = # of nums to sort
      -- 4004 = beginning of array of nums
      -- R2 = size of array, in bytes (N)
      -- R4 = Address of beginning of array (4004)
      -- R5 = first address past array, for loop termination
      -- R6 = Outer loop variable (i)
      -- R7 = Inner loop variable (j)
      -- R8 = Termination value for inner loop (j)
      -- R9 = value of array[j]
      -- R10 = value of array[j+1]
      -- R11 = Comparison between array[j] and array[j+1]
      -- R12 = Temp variable for swapping values
      -- Grab Data
0:  ADDI R4, R0, 4000
      -- Load Size of array
4:  LW R2, 0(R4)
      -- Multiply this by 4, since each element is 4 bytes
8:  SLL R2, R2, 2
      -- R4 is address of beginning of array of numbers
12:  ADDI R4, R4, 4
      -- R5 now points to first address past array
16:  ADD R5, R4, R2
      -- initialize loop variable (i) to first address (4004)
20:  ADDI R6, R4, 4
LABEL OuterLoop
      -- Branch If we are out of array bounds
24:  BEQ R6, R5, Postloop
      -- Reset variable (j) to 0
28:  ADD R7, R0, R0
      -- increment variable (i) 
32:  ADDI R6, R6, 4
      -- Go into inner loop
LABEL InnerLoop
36:  SUB R8, R5, R6
40:  ADDI R8, R8, 4
      -- if j = n-i jump back to OuterLoop
44:  BEQ R7, R8, OuterLoop
      -- Arr[j]
48:  LW R9, 4004(R7)
      -- Arr[j+1]
52:  LW R10, 4008(R7)
      -- Increment J
56:  ADDI R7, R7, 4
      -- If Arr[j] is less than or equal arr[j+1] Then branch back up
60:  SUB R11, R9, R10
64:  BLEZ R11, InnerLoop
      -- Swap Array elements
68:  ADDI R12, R10, 0
72:  ADDI R10, R9, 0
76:  ADDI R9, R12, 0
      -- Store back Results 
80:  SW R10, 4004(R7)
84:  SW R9, 4000(R7)
88:  J InnerLoop
LABEL Postloop
92:  HALT
