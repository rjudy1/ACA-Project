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
      -- stack is at Org5000 - R30 is SP
0:  ADDI R30, R0, 5000
      -- data is stored at Org4000
      -- load low and high(end address) storage addresses into params
4:  ADDI R28, R0, 4004
8:  LW R29, 4000(R0)
12:  SLL R29, R29, 2
16:  ADDI R29, R29, 4000
20:  JAL quicksort
24:  HALT
      -- 
LABEL quicksort
      -- use R5 for conditional
      -- check conditional to see if anything will be done
28:  SUB R5, R28, R29
32:  BGEZ R5, exit
      -- store at stack pointer and increment pointer
36:  SW R31, 0(R30)
40:  ADDI R30, R30, 4
44:  SW R5, 0(R30)
48:  ADDI R30, R30, 4
52:  SW R2, 0(R30)
56:  ADDI R30, R30, 4
60:  SW R3, 0(R30)
64:  ADDI R30, R30, 4
      -- store low and high in R2, R3
68:  ADD R2, R28, R0
72:  ADD R3, R29, R0
      -- pass params; reset high as partion-1 and then the other way
      -- R1 should contain the value returned by partition
76:  JAL partition
80:  ADD R28, R2, R0
84:  ADDI R29, R1, -4
88:  JAL quicksort
92:  ADDI R28, R1, 4
96:  ADD R29, R3, R0
100:  JAL quicksort
      -- restore registers
104:  ADDI R30, R30, -4
108:  LW R3, 0(R30)
112:  ADDI R30, R30, -4
116:  LW R2, 0(R30)
120:  ADDI R30, R30, -4
124:  LW R5, 0(R30)
128:  ADDI R30, R30, -4
132:  LW R31, 0(R30)
LABEL exit
136:  JR R31
140:  NOP
      -- end of quicksort routine
      --
      --
LABEL partition
      -- store at stack pointer and increment pointer, save necessary registers
144:  SW R31, 0(R30)
148:  ADDI R30, R30, 4
152:  SW R7, 0(R30)
156:  ADDI R30, R30, 4
160:  SW R3, 0(R30)
164:  ADDI R30, R30, 4
168:  SW R4, 0(R30)
172:  ADDI R30, R30, 4
176:  SW R5, 0(R30)
180:  ADDI R30, R30, 4
184:  SW R6, 0(R30)
188:  ADDI R30, R30, 4
      -- load value at arr[high] to R7, address counter i to R2, j to R3
192:  LW R7, 0(R29)
      -- just use the return reg as i
196:  ADDI R1, R28, -4
200:  ADD R3, R28, R0
      -- the loop through elements, based on j (R3)
LABEL loop
      -- check conditions
204:  SUB R4, R3, R29
208:  BGEZ R4, postloop
      -- check the internal if statement
212:  LW R4, 0(R3)
216:  SUB R4, R4, R7
      -- if R4 was less than R7, increment i and swap arr[i] and arr[j], else branch to postbranchp
220:  BGEZ R4, postbranchp
224:  ADDI R1, R1, 4
228:  LW R5, 0(R1)
232:  LW R6, 0(R3)
236:  SW R5, 0(R3)
240:  SW R6, 0(R1)
      -- check conditions to branch to loop or continue
LABEL postbranchp
      -- increment j
244:  ADDI R3, R3, 4
248:  J loop
LABEL postloop
      -- move i+1 to return registers
      -- swap arr[i+1] and arr[high]
252:  ADDI R1, R1, 4
256:  LW R5, 0(R1)
260:  LW R6, 0(R29)
264:  SW R5, 0(R29)
268:  SW R6, 0(R1)
      -- restore registers, put stack back, restore return address
272:  ADDI R30, R30, -4
276:  LW R6, 0(R30)
280:  ADDI R30, R30, -4
284:  LW R5, 0(R30)
288:  ADDI R30, R30, -4
292:  LW R4, 0(R30)
296:  ADDI R30, R30, -4
300:  LW R3, 0(R30)
304:  ADDI R30, R30, -4
308:  LW R7, 0(R30)
312:  ADDI R30, R30, -4
316:  LW R31, 0(R30)
320:  JR R31
324:  NOP
