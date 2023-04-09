package org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.problemApplyjMetalTestDoubleSolution;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.model.systembehavior.StateMachine;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.fileoutput.SolutionListOutput;
import org.uma.jmetal.util.fileoutput.impl.DefaultFileOutputContext;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;


public class ASUBERunner {
	public final static String ADAPTIVE_SEARCHBASED_UNKNOWN_BEHAVIOR_EXPLORER_PROBLEM = "org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.problemApplyjMetalTestDoubleSolution.ProblemPSBGenwithWDVRules";
	public static String OUTPUT_PATH = "asube_analyzer";
	
	public static String CASE = "";
	public static String USE_CASE = "";
	public static String ALGO = "";
	
	public static int times;
	
	public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	
	
	//20191122_To use the output in the experiment process.
	public List<DoubleSolution> resultSet;
	public Problem<DoubleSolution> p;
	public StateMachine existingStateMachine;
	public void run(int[] objs, String pName, String statemachinefile, String realbehaviorsfile, String referenceParetoFront) {}
	
	public static void printASUBEDoubleSolution(List<DoubleSolution> population, String pName, long computingTime){
		
		String path = OUTPUT_PATH + "/";
		if(!CASE.equals("")){
			path = path + CASE +"/";
		}
		if(!USE_CASE.equals("")){
			path = path + USE_CASE +"/";
		}
		
		File directory = new File(path);
	    if (! directory.exists()){
	        directory.mkdirs();
	    }
		
		
		String var = path + pName+"_01_VAR.tsv";
		String fun = path + pName+"_02_FUN.tsv";
		String log = path + pName+"_03_log.log";
		
		Path plog = Paths.get(log);
		
		try {
			if(!Files.exists(plog)){
				Files.createFile(plog);
			}
			
			Files.write(plog, (LocalDateTime.now().format(formatter)+" "+ computingTime+"ms"+System.getProperty("line.separator")).getBytes(), StandardOpenOption.APPEND);
			
		} catch (IOException e) {
			e.printStackTrace();
		}

		
		DefaultFileOutputContext varContext = new DefaultFileOutputContext(var);
		varContext.setSeparator("");
		new SolutionListOutput(population)
        .setSeparator("\t")
        .setVarFileOutputContext(varContext)
        .setFunFileOutputContext(new DefaultFileOutputContext(fun))
        .print();

	    JMetalLogger.logger.info("Random seed: " + JMetalRandom.getInstance().getSeed());
	}
	
	
	
	public static String formatNum(int number){
		return String.format("%06d", number);
	}
}