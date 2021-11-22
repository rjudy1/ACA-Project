/*
 * authors: Aaron Johnston and Rachael Judy
 * file: FunctionalUnit.java
 * purpose: This is an abstract class for different functional units (intAlu, intDivide Ect).
 * 			It has the ability to check reservation station ability, as well as squash all reservation stations.
 * 			the exec cycle ensures the station is ready and then calculates the result through the individial FU's
 * 
 */

package tomasulogui;

public abstract class FunctionalUnit {
  PipelineSimulator simulator;
  ReservationStation[] stations = new ReservationStation[2];
  
  public int destTag;
  public int destVal;
  
  int stationToGo = 0;
  int stationDone = 0;
  int stationToInsert = 0;
  boolean requestWriteback = false;
  boolean canWriteback = false;
  boolean inProgress = false;
  
  public int getDestTag() {
      return destTag;
  }
  
  public void setDestTag(int dTag) {
  	destTag = dTag;
  }
  
  public int getDestValue() {
      return destVal;
  }
  
  public void setDestValue(int dVal) {
  	destVal = dVal;
  }
  

  public boolean getRequestWriteBack() {
      return requestWriteback;
  }
  
  public void setRequestWriteback(boolean reqWB) {
  	requestWriteback = reqWB;
  }
  
  public boolean getCanWriteback() {
      return canWriteback;
  }
  
  public void setCanWriteback(boolean canWB) {
  	canWriteback = canWB;
  }
  

  public FunctionalUnit(PipelineSimulator sim) {
    simulator = sim;
  }

 
  public void squashAll() {
  	for (int i = 0; i < 2; i++) {
  		stations[i] = null;
  	}
   	requestWriteback = false;
  }
  
  public abstract int calculateResult(int station);

  public abstract int getExecCycles();
  
  public boolean isReservationStationAvailable() {
		for (int i = 0; i < 2; i++) {
     		if (stations[i] == null) {
     			stationToInsert = i;
     			return true;
     		} 
 		}
		return false;
  }

  public void execCycle(CDB cdb) {
	  
  	  if (canWriteback) {		  
          requestWriteback = false;
          stations[stationDone] = null;
  	  }

  	  // check station 0 then 1, repeats to allow one to execute, round robin style
      if (stations[stationToGo] != null && stations[stationToGo].isReady()) {
    	  calculateResult(stationToGo);
    	  // if done, we can let the other station get checked first next time
    	  if (!inProgress) {
    		  stationToGo = (stationToGo+1)%2;
    	  }
	  } else if (stations[(stationToGo+1)%2] != null && stations[(stationToGo+1)%2].isReady()) {
		  calculateResult((stationToGo+1)%2);
		  stationToGo = (stationToGo+1)%2;
	  }
      
  	// check reservationStations for cdb data
      if (simulator.cdb.getDataValid()) {
          for (int i = 0; i < 2; i++) {
            if (stations[i] != null) {
              stations[i].snoop(simulator.cdb);
            }
          }
      }
      canWriteback=false; 
  }

  public void acceptIssue(IssuedInst inst) {
	  stations[stationToInsert] = new ReservationStation(simulator);
	  stations[stationToInsert].loadInst(inst);
	  return;
  }

}
