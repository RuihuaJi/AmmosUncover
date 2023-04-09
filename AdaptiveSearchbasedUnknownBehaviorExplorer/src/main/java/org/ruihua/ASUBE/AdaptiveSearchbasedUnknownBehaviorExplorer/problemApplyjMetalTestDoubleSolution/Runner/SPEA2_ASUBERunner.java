package org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.problemApplyjMetalTestDoubleSolution.Runner;

import java.util.ArrayList;
import java.util.List;

import org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.ReadWriteModule.ReadFromFiles;
import org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.model.systembehavior.StateMachine;
import org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.problem.Solution;
import org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.problemApplyjMetalTestDoubleSolution.ASUBERunner;
import org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.problemApplyjMetalTestDoubleSolution.ProblemPSBGenwithWDVRules;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.spea2.SPEA2Builder;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.crossover.SBXCrossover;
import org.uma.jmetal.operator.impl.mutation.PolynomialMutation;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.ProblemUtils;
import org.uma.jmetal.util.comparator.RankingAndCrowdingDistanceComparator;

public class SPEA2_ASUBERunner extends ASUBERunner {
	
	public final String algName = "SPEA2";
	
	//20200202_To use the output in the experiment process.
	public SPEA2_ASUBERunner() {
		ASUBERunner.ALGO = this.algName;
	}
	
	public void run(int[] objs, String pName, String statemachinefile, String realbehaviorsfile, String referenceParetoFront) {
		ASUBERunner.ALGO = this.algName;
		run(ASUBERunner.ADAPTIVE_SEARCHBASED_UNKNOWN_BEHAVIOR_EXPLORER_PROBLEM, objs, pName, statemachinefile, realbehaviorsfile, referenceParetoFront);
	}
	
	public void run(String problemClassName, int[] objs, String pName, String statemachinefile, String realbehaviorsfile, String referenceParetoFront) {
		                             
		Problem<DoubleSolution> problem;
		Algorithm<List<DoubleSolution>> algorithm;
		CrossoverOperator<DoubleSolution> crossover;
		MutationOperator<DoubleSolution> mutation;
		SelectionOperator<List<DoubleSolution>, DoubleSolution> selection;
		
		problem = ProblemUtils.<DoubleSolution> loadProblem(problemClassName);
		
		List<Solution> realbehaviors = new ArrayList<Solution>();
		StateMachine statemachine = new StateMachine();
		System.out.println("====== Read State Machine ======");
		statemachine = ReadFromFiles.ReadStateMachineFromFile_v201606(statemachinefile);
		System.out.println("====== Read Behaviors ======");
		realbehaviors = ReadFromFiles.ReadBehaviourPairsFromFile_v201606(realbehaviorsfile, statemachine);
		
		if(problem instanceof ProblemPSBGenwithWDVRules) {
			System.out.println("+++++ YES +++++");
			((ProblemPSBGenwithWDVRules) problem).setObjs(objs);
			((ProblemPSBGenwithWDVRules) problem).setProblemName(pName);
			((ProblemPSBGenwithWDVRules) problem).preparation(statemachine, realbehaviors);
			((ProblemPSBGenwithWDVRules) problem).initial();
		}
 		
		double crossoverProbability = 0.9;
	    double crossoverDistributionIndex = 20.0;
	    crossover = new SBXCrossover(crossoverProbability, crossoverDistributionIndex);
		
	    double mutationProbability = 1.0 / problem.getNumberOfVariables();
	    double mutationDistributionIndex = 20.0;
	    mutation = new PolynomialMutation(mutationProbability, mutationDistributionIndex);
	    
	    selection = new BinaryTournamentSelection<DoubleSolution>(
	    		new RankingAndCrowdingDistanceComparator<DoubleSolution>());
	    
	    algorithm = new SPEA2Builder<DoubleSolution>(problem, crossover, mutation)
	    		.setSelectionOperator(selection)
	    		.setMaxIterations(2500)
	    		.setPopulationSize(100)
	    		.build();
	    
	    AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm).execute();
	    
	    List<DoubleSolution> population = algorithm.getResult();
	    long computingTime = algorithmRunner.getComputingTime();
	    JMetalLogger.logger.info("Total execution time: " + computingTime + "ms");
	    
	    ASUBERunner.printASUBEDoubleSolution(population, pName, computingTime);
	    
	    //20191122_To use the output in the experiment process.
	    this.existingStateMachine = statemachine;
	    this.p = problem;
	    this.resultSet = algorithm.getResult();
	    
	    
	    //printFinalSolutionSet(population);
	    if (!referenceParetoFront.equals("")) {
	      //printQualityIndicators(population, referenceParetoFront) ;
	    }
		
	}
	
	

}
