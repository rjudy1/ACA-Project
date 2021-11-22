-- Quicksort Program in MIPs assembly
-- Group: Aaron Johnston and Rachael Judy
-- Structures: uses a stack frame with storing from R31,
--				it uses two subroutines of quicksort and partition
-- Purpose: use quicksort algorithm to sort list of 32bit(4byte) integers
--
-- Registers and Constants
-- 4000 = address of data
-- 5000 = address of stack
-- Memory was allocated based on size of the array used; the stack was made
--	to be of size 400 and the data to sort 980 bytes
-- 
-- R0 is grounded
-- R1 acts as return register
-- R2 will hold index of smaller element and right position of pivot (i)
-- R3 will be counter (j)
-- R4 will be comparison used for loop back
-- R5 will hold first to swap
-- R6 will hold second to swap
-- R7 will hold pivot
-- R28, R29 = low, high (parameter passing)
-- R30 = SP address
-- R31 = return address
-- 
-- 
Begin Assembly
-- stack is at Org5000 - R30 is SP
ADDI R30, R0, 5000
-- data is stored at Org4000
-- load low and high(end address) storage addresses into params
ADDI R28, R0, 4004
LW R29, 4000(R0)
SLL R29, R29, 2
ADDI R29, R29, 4000
JAL quicksort
HALT
-- 
LABEL quicksort
-- use R5 for conditional
-- check conditional to see if anything will be done
SUB R5, R28, R29
BGEZ R5, exit
-- store at stack pointer and increment pointer
SW R31, 0(R30)
ADDI R30, R30, 4
SW R5, 0(R30)
ADDI R30, R30, 4
SW R2, 0(R30)
ADDI R30, R30, 4
SW R3, 0(R30)
ADDI R30, R30, 4
-- store low and high in R2, R3
ADD R2, R28, R0
ADD R3, R29, R0
-- pass params; reset high as partion-1 and then the other way
-- R1 should contain the value returned by partition
JAL partition
ADD R28, R2, R0
ADDI R29, R1, -4
JAL quicksort
ADDI R28, R1, 4
ADD R29, R3, R0
JAL quicksort
-- restore registers
ADDI R30, R30, -4
LW R3, 0(R30)
ADDI R30, R30, -4
LW R2, 0(R30)
ADDI R30, R30, -4
LW R5, 0(R30)
ADDI R30, R30, -4
LW R31, 0(R30)
LABEL exit
JR R31
NOP
-- end of quicksort routine
--
--
LABEL partition
-- store at stack pointer and increment pointer, save necessary registers
SW R31, 0(R30)
ADDI R30, R30, 4
SW R7, 0(R30)
ADDI R30, R30, 4
SW R3, 0(R30)
ADDI R30, R30, 4
SW R4, 0(R30)
ADDI R30, R30, 4
SW R5, 0(R30)
ADDI R30, R30, 4
SW R6, 0(R30)
ADDI R30, R30, 4
-- load value at arr[high] to R7, address counter i to R2, j to R3
LW R7, 0(R29)
-- just use the return reg as i
ADDI R1, R28, -4
ADD R3, R28, R0
-- the loop through elements, based on j (R3)
LABEL loop
-- check conditions
SUB R4, R3, R29
BGEZ R4, postloop
-- check the internal if statement
LW R4, 0(R3)
SUB R4, R4, R7
-- if R4 was less than R7, increment i and swap arr[i] and arr[j], else branch to postbranchp
BGEZ R4, postbranchp
ADDI R1, R1, 4
LW R5, 0(R1)
LW R6, 0(R3)
SW R5, 0(R3)
SW R6, 0(R1)
-- check conditions to branch to loop or continue
LABEL postbranchp
-- increment j
ADDI R3, R3, 4
J loop
LABEL postloop
-- move i+1 to return registers
-- swap arr[i+1] and arr[high]
ADDI R1, R1, 4
LW R5, 0(R1)
LW R6, 0(R29)
SW R5, 0(R29)
SW R6, 0(R1)
-- restore registers, put stack back, restore return address
ADDI R30, R30, -4
LW R6, 0(R30)
ADDI R30, R30, -4
LW R5, 0(R30)
ADDI R30, R30, -4
LW R4, 0(R30)
ADDI R30, R30, -4
LW R3, 0(R30)
ADDI R30, R30, -4
LW R7, 0(R30)
ADDI R30, R30, -4
LW R31, 0(R30)
JR R31
NOP
End Assembly
Begin Data 4000 600
15
4
5
2
0
-5
20
51
-5
34
23
4
5
2
0
-5
End Data
-- stack
Begin Data 5000 400
End Data