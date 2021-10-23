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
4:  NOP
8:  NOP
12:  NOP
      -- Data is at Org 4000
16:  ADDI R4, R0, 4000
20:  NOP
24:  NOP
28:  NOP
      -- Load number of elements
32:  LW R2, 0(R4)
36:  NOP
40:  NOP
44:  NOP
      -- Multiply this by 4, since each element is 4 bytes
48:  SLL R3, R2, 2
52:  NOP
56:  NOP
60:  NOP
      -- R4 is address of beginning of array of numbers
64:  ADDI R4, R4, 8
68:  NOP
72:  NOP
76:  NOP
      -- R5 now points to first address past array
80:  ADD R5, R4, R3
84:  NOP
88:  NOP
92:  NOP
      -- initialize loop variable to first address (4008)
96:  ADD R6, R4, R0
100:  NOP
104:  NOP
108:  NOP
      -- sum = 0
112:  ADD R7, R0, R0
116:  NOP
120:  NOP
124:  NOP
LABEL LoopStart
128:  BEQ R6, R5, PostLoop
132:  NOP
136:  NOP
140:  NOP
      -- load current value
144:  LW R8, 0(R6)
148:  NOP
152:  NOP
156:  NOP
      -- pass parameters (curr value and curr sum)
160:  ADD R20, R8, R0
164:  NOP
168:  NOP
172:  NOP
176:  ADD R21, R7, R0
180:  NOP
184:  NOP
188:  NOP
192:  JAL AddThem
      -- move sum from return reg to R7
196:  ADD R7, R1, R0
200:  NOP
204:  NOP
208:  NOP
      -- increment address (by 4 bytes)
212:  ADDI R6, R6, 4
216:  NOP
220:  NOP
224:  NOP
228:  J LoopStart
232:  NOP
236:  NOP
240:  NOP
LABEL PostLoop
      -- store answer
244:  SW R7, -4(R4)
248:  NOP
252:  NOP
256:  NOP
260:  HALT
      -- subroutine to add 2 numbers
LABEL AddThem
      -- if doing recursion, must save R31
264:  SW R31, 0(R30)
268:  NOP
272:  NOP
276:  NOP
      -- post incr the SP
280:  ADDI R30, R30, 4
284:  NOP
288:  NOP
292:  NOP
      -- Since subroutine uses R5, must save
296:  SW R5, 0(R30)
300:  NOP
304:  NOP
308:  NOP
312:  ADDI R30, R30, 4
316:  NOP
320:  NOP
324:  NOP
      -- get nums from parameter regs and sum
328:  ADD R5, R20, R21
332:  NOP
336:  NOP
340:  NOP
      -- move result to return reg
344:  ADD R1, R5, R0
348:  NOP
352:  NOP
356:  NOP
      -- now put stack back the way it was
      -- and restore return address and R5
360:  ADDI R30, R30, -4
364:  NOP
368:  NOP
372:  NOP
376:  LW R5, 0(R30)
380:  NOP
384:  NOP
388:  NOP
392:  ADDI R30, R30, -4
396:  NOP
400:  NOP
404:  NOP
408:  LW R31, 0(R30)
412:  NOP
416:  NOP
420:  NOP
      -- return from subroutine
424:  JR R31
428:  NOP
432:  NOP
436:  NOP
440:  NOP
