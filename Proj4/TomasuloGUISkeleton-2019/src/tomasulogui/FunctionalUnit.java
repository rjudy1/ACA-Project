package tomasulogui;

public abstract class FunctionalUnit {
  PipelineSimulator simulator;
  ReservationStation[] stations = new ReservationStation[2];
  
  int stationToGo = 0;
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

  public void execCycle(CDB cdb) {
	  
	  // flips red robin style
	  stationToGo = (stationToGo+1)%2;
  	  if (canWriteback) {		  
          requestWriteback = false;
          stations[stationToGo] = null;
  	  }
	  
      //todo - start executing, ask for CDB, etc.
	  // check station 0 then 1, repeats to allow one to execute
      if (stations[stationToGo] != null) {
    	  stations[stationToGo].snoop(cdb);
    	  if (stations[stationToGo].isReady()) {
    		  calculateResult(stationToGo);
    		  // must add a check of cycles required because can't be done until cycles pass
    		  // put on bus if good
    	  }
	  } else if (stations[(stationToGo+1)%2] != null) {
		  stations[(stationToGo+1)%2].snoop(cdb);
		  if (stations[(stationToGo+1)%2].isReady()) {
			  calculateResult((stationToGo+1)%2);
			  // must add a check of cycles required because can't be done until cycles pass
			  // put on bus if good
		  }
	  }
      
  	// check reservationStations for cdb data
      if (simulator.cdb.getDataValid()) {
          for (int i = 0; i < 2; i++) {
            if (stations[i] != null) {
              stations[i].snoop(simulator.cdb);
            }
          }
      }
      canWriteback=false; // issue is related to this !!!!, wrong thing being overwritten/issued?
  }

  public void acceptIssue(IssuedInst inst) {
  // todo - fill in reservation station (if available) with data from inst
	  if (stations[stationToGo] == null)
		  stations[stationToGo] = new ReservationStation(simulator);
	  else if (stations[(stationToGo+1)%2] == null)
		  stations[(stationToGo+1)%2] = new ReservationStation(simulator);
	  
	  if (!stations[stationToGo].occupied)
		  stations[stationToGo].loadInst(inst);
	  else if (!stations[(stationToGo+1)%2].occupied)
		  stations[(stationToGo+1)%2].loadInst(inst);
  }

}
