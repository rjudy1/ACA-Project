package tomasulogui;

public class IssueUnit {
  private enum EXEC_TYPE {
    NONE, LOAD, ALU, MULT, DIV, BRANCH} ;

    PipelineSimulator simulator;
    IssuedInst issuee;
    Object fu;

    public IssueUnit(PipelineSimulator sim) {
      simulator = sim;
    }

    public void execCycle() {
      // an execution cycle involves:
      // 1. checking if ROB and Reservation Station avail
    	// check type of issuee and say intMult.reservation station[0], [1] done
    	// if done, check for a structural hazard
    	    	
      // 2. issuing to reservation station, if no structural hazard
    	// put issued instruction in reservation station and tag with number and registers as necssary
    	
      // to issue, we make an IssuedInst, filling in what we know
      // We check the BTB, and put prediction if branch, updating PC
    	// puts result from predictBranch
      //     if pred taken, incr PC otherwise
    
      // We then send this to the ROB, which fills in the data fields
    	// reorder buffer fills in the data fields and tags

      // We then check the CDB, and see if it is broadcasting data we need,
      //    so that we can forward during issue
    	// special case of checking due to sequential ordering and if ready

      // We then send this to the FU, who stores in reservation station
    	// functional unit has to choose the reservation station
    }

  }
