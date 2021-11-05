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
Begin Assembly
-- Grab Data
ADDI R4, R0, 4000
-- Load Size of array
LW R2, 0(R4)
-- Multiply this by 4, since each element is 4 bytes
SLL R2, R2, 2
-- R4 is address of beginning of array of numbers
ADDI R4, R4, 4
-- R5 now points to first address past array
ADD R5, R4, R2
-- initialize loop variable (i) to first address (4004)
ADDI R6, R4, 4
LABEL OuterLoop
-- Branch If we are out of array bounds
BEQ R6, R5, Postloop
-- Reset variable (j) to 0
ADD R7, R0, R0
-- increment variable (i) 
ADDI R6, R6, 4
-- Go into inner loop
LABEL InnerLoop
SUB R8, R5, R6
ADDI R8, R8, 4
-- if j = n-i jump back to OuterLoop
BEQ R7, R8, OuterLoop
-- Arr[j]
LW R9, 4004(R7)
-- Arr[j+1]
LW R10, 4008(R7)
-- Increment J
ADDI R7, R7, 4
-- If Arr[j] is less than or equal arr[j+1] Then branch back up
SUB R11, R9, R10
BLEZ R11, InnerLoop
-- Swap Array elements
ADDI R12, R10, 0
ADDI R10, R9, 0
ADDI R9, R12, 0
-- Store back Results 
SW R10, 4004(R7)
SW R9, 4000(R7)
J InnerLoop
LABEL Postloop
HALT
End Assembly
Begin Data 4000 960
240
1
8
8
10
2
3
7
4
5
6
1
8
8
10
2
3
7
4
5
6
1
8
8
10
2
3
7
4
5
6
1
8
8
10
2
3
7
4
5
6
1
8
8
10
2
3
7
4
5
6
1
8
8
10
2
3
7
4
5
6
1
8
8
10
2
3
7
4
5
6
1
8
8
10
2
3
7
4
5
6
1
8
8
10
2
3
7
4
5
6
1
8
8
10
2
3
7
4
5
6
1
8
8
10
2
3
7
4
5
6
1
8
8
10
2
3
7
4
5
6
1
8
8
10
2
3
7
4
5
6
1
8
8
10
2
3
7
4
5
6
1
8
8
10
2
3
7
4
5
6
1
8
8
10
2
3
7
4
5
6
1
8
8
10
2
3
7
4
5
6
1
8
8
10
2
3
7
4
5
6
1
8
8
10
2
3
7
4
5
6
End Data
