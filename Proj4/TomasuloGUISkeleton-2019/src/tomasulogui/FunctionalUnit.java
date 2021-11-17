package tomasulogui;

public abstract class FunctionalUnit {
  PipelineSimulator simulator;
  ReservationStation[] stations = new ReservationStation[2];
  
  int stationToGo = 0;
  int stationDone = 0;
  int stationToInsert = 0;
  boolean requestWriteback = false;
  boolean canWriteback = false;

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
	  
	  // flips red robin style
	  stationToGo = (stationToGo+1)%2;

  	  if (canWriteback) {		  
          requestWriteback = false;
          stations[stationDone] = null;
  	  }

	  
      //todo - start executing, ask for CDB, etc.
	  // check station 0 then 1, repeats to allow one to execute
      if (stations[stationToGo] != null && stations[stationToGo].isReady()) {
    	  calculateResult(stationToGo);
    	  stationDone = stationToGo;
    	  // must add a check of cycles required because can't be done until cycles pass
    	  // put on bus if good
	  } else if (stations[(stationToGo+1)%2] != null && stations[(stationToGo+1)%2].isReady()) {
		  calculateResult((stationToGo+1)%2);
		  stationDone = (stationToGo+1)%2;
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
  // todo - fill in reservation station (if available) with data from inst
	  stations[stationToInsert] = new ReservationStation(simulator);
	  stations[stationToInsert].loadInst(inst);
	  return;
  }

}
