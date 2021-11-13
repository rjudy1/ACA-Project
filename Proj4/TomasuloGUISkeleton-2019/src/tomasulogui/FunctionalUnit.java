package tomasulogui;

public abstract class FunctionalUnit {
  PipelineSimulator simulator;
  ReservationStation[] stations = new ReservationStation[2];
  
  int zero = 0;
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
    //todo - start executing, ask for CDB, etc.
	  zero = (zero+1)%2; // determines which station to check first
	  // check station 0 then 1, repeats to allow one to execute
	  if (stations[zero] != null && stations[zero].occupied) {
		  stations[zero].snoop(cdb);
		  if (stations[zero].isReady()) {
			  calculateResult(zero);
			  // must add a check of cycles required because can't be done until cycles pass
			  // put on bus if good
		  }
	  } else if (stations[(zero+1)%2] != null && stations[(zero+1)%2].occupied) {
		  stations[(zero+1)%2].snoop(cdb);
		  if (stations[(zero+1)%2].isReady()) {
			  calculateResult((zero+1)%2);
			  // must add a check of cycles required because can't be done until cycles pass
			  // put on bus if good
		  }
	  }
	  
  }

  public void acceptIssue(IssuedInst inst) {
  // todo - fill in reservation station (if available) with data from inst
	  if (stations[zero] == null)
		  stations[zero] = new ReservationStation(simulator);
	  else if (stations[(zero+1)%2] == null)
		  stations[(zero+1)%2] = new ReservationStation(simulator);
	  
	  if (!stations[zero].occupied)
		  stations[zero].loadInst(inst);
	  else if (!stations[(zero+1)%2].occupied)
		  stations[(zero+1)%2].loadInst(inst);
  }

}
