# AmmosUncover: Adaptive Model and Multi-objective Search-based Unknown Behavior Explorer

## Introduction
Complex software-intensive systems commonly rely on information networks for communication, e.g., Cyber-physical Systems and Internet of Things. Such networks are inherently prone to uncertainties, e.g., unexpected and unpredictable occurrences of packet loss and delay, which make it very difficult to fully comprehend the impact of such uncertainties on system behaviors during their design phases, especially when they are deployed and operated in dynamic and complex environments. Therefore, possibly unexpected, unknown or even unsafe system behaviors are often observed during the operations of these systems. Discovering such unknown behaviors becomes critical to avoid potential failures and consequently helps to improve the overall system dependability.

We propose AmmosUncover, an approach that bene-fits from the strengths of both MBT and online testing. AMMOSUNCOVER utilizes historical information of test executions to incrementally evolve the initial test model constructed for a SUT, and to cumulatively guide the multi-objective search (combined with a set of adaptive objectives), with the overall aim of effi-ciently discovering unknown system behaviors un-der uncertain network environments. 

<!-- ## Overview of AmmosUncover
![process](https://github.com/RuihuaJi/AmmosUncover/blob/main/image/OverviewApproachwithME.png) -->

Our proposed approach, named as Adaptive Model and Multi-objective Search-based Unknown Behavior Explorer (AMMOSUNCOVER), uses an adaptive and incremental online MBT process, which utilizes test historical information to incrementally evolve the test model and to cumulatively guide the multi-objective search aiming at discovering unknown system behaviors under uncertain network environments.

The process of AMMOSUNCOVER is divided into a sequence of test cycles and each test cycle consists of three activities, i.e., adaptive model and multi-objective search-based test case generation (AMMOSGENERATOR), test case execution (Execution Activity), and historical information update (Update Activity). 



## Preparation and Pilot Study

### (P1) Compare AMMOSUNCOVER with the previous approach

* The average results of all the cost-effectiveness measures of the AmmosUncover and the previous work (ASUBE) methods.
![process](https://github.com/RuihuaJi/AmmosUncover/blob/main/image/RQ0-extend-with-previous-work-avg.png)

The rows, i.e., ASUBE-5, ASUBE-10, and ASUBE-15, illustrate the average results of our previous work (ASUBE). The 5, 10, and 15 denote the number of test cases to generate per cycle. 

From the ASUBE rows of the table, we can observe that the average results in terms of configured ASUBE methods are better than the non-adaptive and worse than most of the configured AmmosUncover methods.

In terms of the average values of all the effectiveness measures, we can observe that AmmosUncover performs better than ASUBE.



### (P2) Compare AMMOSUNCOVER implementing complex MOSAs with AMMOSUNCOVER implementing Random Search

The measures are the designed adaptive optimization objectives, i.e., O1~O4, and HyperVolume (HV). To O1-O4, a small value is good, while a big value is good to HV. So, in the following pair-wise comparisons (e.g., A1 v.s. A2), if A value is smaller than 0.5 and p value smaller than 0.05, A1 is better to O1-O4; if A value is biggger than 0.5 and p value smaller than 0.05, A1 is better to HV. 

* The statistical comparisons of selected MOSAs and RS when solving the possible system execution generation of the 4th test cycle of the pilot study.

![process](https://github.com/RuihuaJi/AmmosUncover/blob/main/image/Table-RQ0-4.png)

* The statistical comparisons of selected MOSAs and RS when solving the possible system execution generation of the 8th test cycle of the pilot study.

![process](https://github.com/RuihuaJi/AmmosUncover/blob/main/image/Table-RQ0-8.png)

* The statistical comparisons of selected MOSAs and RS when solving the possible system execution generation of the 12th test cycle of the pilot study.

![process](https://github.com/RuihuaJi/AmmosUncover/blob/main/image/Table-RQ0-12.png)


* The ranks of applying MOSAs to solve the problem of possible system execution generation via the statistical comparisons above.

![process](https://github.com/RuihuaJi/AmmosUncover/blob/main/image/MOSA_justification_conclusion_ranking.png)

Based on the statistical test results, the selected MOSAs can be ranked according to their pair-wise comparisons. Zhang et. al. in their work [1] proposed a ranking approach to handle such a circumstance: The selected MOSAs are ranked in terms of each measure, and a rank value is given to describe the ranking; The average ranking values are calculated to summerize the overall ranking of each MOSA; and a bigger ranking value denotes better performance. The above table illustrates the calculated rank value of each MOSAs in terms of each measure, and the last row illustrates the overall ranking of each MOSA. Cells in grey contain the best ranking values in each grid, while the underlined values are the lowest in each grid.

We can see that RS always has the lowest confidence (twice the lowest and once the second lowest) to be the best algorithm, and such results justify the usage of MOSAs in AmmosUncover. 




## The detailed Experiments and Results (RQ1-RQ4)
Details can be found in our paper.

## Experiment Data Link

* The experiment data of our empirical study on SysApp and SysDCS could be found:
https://drive.google.com/drive/folders/13D478pU0YiDWvEjMFUlMg0JaayRNkwfn?usp=sharing

## Usage

Ones are recommended to download the source code and import via **Eclipse**.

### Entrance Classes in the Java Project
To use AMMOSUNCOVER, we prepare the following two entrance classes in the Java Project's package "org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.process". 
  
  * **Experiment_AMMOSUNCOVER_MOSA_SysDCS_Simulator.java**
   
    Using the "main" function, ones can invoke the experiments of configured AmmosUncover methods on SysDCS. To illustrate the ability of AMMOSUNCOVER, we keep a simple version of the simulated SysDCS in this configuration. Users can directly run this "main" function, AMMOSUNCOVER will iteratively: I. generate possibly-existing system executions; II. generate test cases; III. execute test cases (in this step, AMMOSUNCOVER interacts with the simulated SysDCS and capture the truly-existing system executions); IV. update the NE test model and other relevant repositories. 
  
  * **Experiment_AMMOSUNCOVER_MOSA_SysApp_Appium_Executor.java**
  
    Via this class, ones can invoke the experiments of configured AmmosUncover methods on SysApp. This configuration is used to interact with the systems implemented in reality. So, users need to develop the necessary APIs according to systems under test. In our context, we employ Appium to interact with the system under test, Jitsi. We keep all the applied functions, e.g., specific test scripts in Appium's format, for reference. However, to run AMMOSUNCOVER on the systems implemented in reality, ones still need to establish the systems in reality. For example, we used a set of Android devices having Jitsi deployed on in a local network.
    
The above two configurations only differ in terms of the interaction with the selected systems under test.
    
### Main function and Used Parameters

We explain the main functions and the used parameters of the entrance classes as follows.

```Java

public static void main(String[] args) throws Exception {
    Experiment_AMMOSUNCOVER_MOSA_SysDCS_Simulator ex = new Experiment_AMMOSUNCOVER_MOSA_SysDCS_Simulator(
				new CellDE_ASUBERunner(), //implemented MOSA instances, e.g., CellDE
				"", //referenceParetoFront, but is not needed during AmmosUncover process
				10, //Number of Test Cases per test cycle, e.g., 5 or 10 or 15
				1, //the index of the starting trials (We conduct each configuration for many trails)
				1, //the index of the starting test cycle.
				"InputOutputFiles/", //Experiment Label for the File Folder storing all the input and output files.
				120, //the maximum of Executable Test Cases, in AmmosUncover, we set 120.
				10); //the maximum of trials
    ex.dsJitsiDesktop = new DSJitsiDesktop(
				"InputOutputFiles/Part_of_Repository_for_Executions_of_JitsiDesktop.txt" // the simple version of the simulated SysDCS
				);
    ex.setParametersofFileSystem();//set some default parameters
    ex.initialization();//preparation for run
    ex.ASUBE_MO_Appium_OneTestCycle_Process();//run
    ex.completeAllTestProcessesofOneMOSA();
    
    }
```

The functions and parameters are mainly explained in the comments.

Note that the optional MOSA instances also include *new MOCell_ASUBERunner()*, *new NSGAII_ASUBERunner()*, *new NSGAIII_ASUBERunner()*, *new SPEA2_ASUBERunner()*. 

Considering that the whole process could take a lot of time especially when testing systems in real operating environments, we make the above process could start from any breaking point via changing the index of the starting test cycle and the index of the starting trials. 
In the above example, we set them both 1, denoting that our AMMOSUNCOVER process starts from the 1st test cycle of the 1st trail. We also can start from, e.g., the 2nd test cycle of the 2nd trail by changing the both parameters as 2 (in such a case, we need to manually make sure that, to the 2nd test cycle, the state machine file and repository files are initialized). 
   
To conduct experiments on systems implemented in reality (SysApp), the applied functions are in directory "org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.systemcontrolmodule". These functions need to be customed when users test their own systems.
   

## Statistical Tests Scripts

We also provide the scripts for statitical test in the directory "Scripts", which consit of JAVA and PYTHON codes.

* Then entrance class is "ExtractInformationfromProcessLogToStatistics_forAllExperiments", which contain three main steps:
   - "ExtractInformationfromProcessLogToStatistics_forAllExperiments.process_1_rawData2ExcelFile" for collecting data from log files of each experiments, respectively;
   - "ExtractInformationfromProcessLogToStatistics_forAllExperiments.process_1_2_readFromExcelFileToBasicStatisticsResultWriteToExcelFile" process the basic statistical steps;
   - "ExtractInformationfromProcessLogToStatistics_forAllExperiments.process_2_readFromExcelFileToStatisticsResultWriteToExcelFile"  conduct the detail statistical tests;
   - The python code is called by the third steps.


## People

* Ruihua Ji
* Tian Zhang https://cs.nju.edu.cn/zhangtian/
* Minxue Pan https://minxuepan.github.io/
* Tao Yue https://www.simula.no/people/tao
* Shaukat Ali https://www.simula.no/people/shaukat
* Xuandong Li https://cs.nju.edu.cn/lixuandong/

Reference

[1] Uncertainty-wise Test Case Generation and Minimization for Cyber- Physical Systems.
