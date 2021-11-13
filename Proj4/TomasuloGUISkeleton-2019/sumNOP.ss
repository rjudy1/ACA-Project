      --A program to find the sum of a list of numbers
      -- The program uses a subroutine to add 2 numbers, as a demo
      -- It also sets up a stack frame, although not needed for this program
      -- 4000 = # of nums to sum
      -- 4004  = location for sum to be put
      -- 4008 = beginning of array of nums
      -- 
      -- R20, R21 - parameter passing regs
      -- R30 = SP
      -- R31 = Ret Addr Reg
      -- R3 = size of array, in bytes
      -- R4 = Address of beginning of array (4008)
      -- R5 = first address past array, for loop termination
      -- R6 = current address being worked on (loop i variable)
      -- R7 = sum
      -- R8 = current array data value
      -- 
      -- Stack will be at Org5000 - R30 is SP
0:  ADDI R30, R0, 5000
      -- Data is at Org 4000
4:  ADDI R4, R0, 4000
      -- Load number of elements
8:  LW R2, 0(R4)
      -- Multiply this by 4, since each element is 4 bytes
12:  SLL R3, R2, 2
      -- R4 is address of beginning of array of numbers
16:  ADDI R4, R4, 8
      -- R5 now points to first address past array
20:  ADD R5, R4, R3
      -- initialize loop variable to first address (4008)
24:  ADD R6, R4, R0
      -- sum = 0
28:  ADD R7, R0, R0
LABEL LoopStart
32:  NOP
36:  NOP
40:  NOP
44:  NOP
48:  BEQ R6, R5, PostLoop
52:  NOP
56:  NOP
60:  NOP
64:  NOP
      -- load current value
68:  LW R8, 0(R6)
      -- pass parameters (curr value and curr sum)
72:  ADD R20, R8, R0
76:  ADD R21, R7, R0
80:  NOP
84:  NOP
88:  NOP
92:  NOP
96:  JAL AddThem
100:  NOP
104:  NOP
108:  NOP
112:  NOP
      -- move sum from return reg to R7
116:  ADD R7, R1, R0
      -- increment address (by 4 bytes)
120:  ADDI R6, R6, 4
124:  NOP
128:  NOP
132:  NOP
136:  NOP
140:  J LoopStart
144:  NOP
148:  NOP
152:  NOP
156:  NOP
LABEL PostLoop
      -- store answer
160:  SW R7, -4(R4)
164:  HALT
      -- subroutine to add 2 numbers
LABEL AddThem
      -- if doing recursion, must save R31
168:  SW R31, 0(R30)
      -- post incr the SP
172:  ADDI R30, R30, 4
      -- Since subroutine uses R5, must save
176:  SW R5, 0(R30)
180:  ADDI R30, R30, 4
      -- get nums from parameter regs and sum
184:  ADD R5, R20, R21
      -- move result to return reg
188:  ADD R1, R5, R0
      -- now put stack back the way it was
      -- and restore return address and R5
192:  ADDI R30, R30, -4
196:  LW R5, 0(R30)
200:  ADDI R30, R30, -4
204:  LW R31, 0(R30)
208:  NOP
      -- return from subroutine
212:  NOP
216:  NOP
220:  NOP
224:  NOP
228:  JR R31
232:  NOP
236:  NOP
240:  NOP
244:  NOP
248:  NOP
