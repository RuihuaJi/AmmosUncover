import numpy as np
from scipy import stats
import sys


def get_p_value(arrA, arrB):
    a = np.array(arrA)
    b = np.array(arrB)

    t, p = stats.ttest_ind(a, b)

    return p


if __name__ == "__main__":

    array_1 = sys.argv[1]
    array_2 = sys.argv[2]

    # parse String to arrays
    srcData_1 = array_1.split("; ")
    ## timeCostofMOSASearch
    arr_1_timeCostofMOSASearch = []
    srcData_1_timeCostofMOSASearch = srcData_1[0].split(" ")
    for i in range(len(srcData_1_timeCostofMOSASearch)):
        if i == 0:
            pass
        else:
            arr_1_timeCostofMOSASearch.append((float(srcData_1_timeCostofMOSASearch[i])) / 60000)
    ## otherTimeCost_1
    arr_1_otherTimeCost_1 = []
    srcData_1_otherTimeCost_1 = srcData_1[1].split(" ")
    for i in range(len(srcData_1_otherTimeCost_1)):
        if i == 0:
            pass
        else:
            arr_1_otherTimeCost_1.append(float(srcData_1_otherTimeCost_1[i]))
    ## timeCostofExecution
    arr_1_timeCostofExecution = []
    srcData_1_timeCostofExecution = srcData_1[2].split(" ")
    for i in range(len(srcData_1_timeCostofExecution)):
        if i == 0:
            pass
        else:
            arr_1_timeCostofExecution.append((float(srcData_1_timeCostofExecution[i])) / 60000)
    ## totalTimeCost
    arr_1_totalTimeCost = []
    srcData_1_totalTimeCost = srcData_1[3].split(" ")
    for i in range(len(srcData_1_totalTimeCost)):
        if i == 0:
            pass
        else:
            arr_1_totalTimeCost.append((float(srcData_1_totalTimeCost[i])) / 60000)
    ## totalExecutions
    arr_1_totalExecutions = []
    srcData_1_totalExecutions = srcData_1[4].split(" ")
    for i in range(len(srcData_1_totalExecutions)):
        if i == 0:
            pass
        else:
            arr_1_totalExecutions.append(float(srcData_1_totalExecutions[i]))
    # print(arr_1_totalExecutions)
    ## numberofNewExecutions
    arr_1_numberofNewExecutions = []
    srcData_1_numberofNewExecutions = srcData_1[5].split(" ")
    for i in range(len(srcData_1_numberofNewExecutions)):
        if i == 0:
            pass
        else:
            arr_1_numberofNewExecutions.append(float(srcData_1_numberofNewExecutions[i]))
    ## numberofNewTransitions
    arr_1_numberofNewTransitions = []
    srcData_1_numberofNewTransitions = srcData_1[6].split(" ")
    for i in range(len(srcData_1_numberofNewTransitions)):
        if i == 0:
            pass
        else:
            arr_1_numberofNewTransitions.append(float(srcData_1_numberofNewTransitions[i]))
    ## numberofNewStates
    arr_1_numberofNewStates = []
    srcData_1_numberofNewStates = srcData_1[7].split(" ")
    for i in range(len(srcData_1_numberofNewStates)):
        if i == 0:
            pass
        else:
            arr_1_numberofNewStates.append(float(srcData_1_numberofNewStates[i]))
    ## numberofNewExecutionsDivAllT
    arr_1_numberofNewExecutionsDivAllT = []
    srcData_1_numberofNewExecutionsDivAllT = srcData_1[8].split(" ")
    for i in range(len(srcData_1_numberofNewExecutionsDivAllT)):
        if i == 0:
            pass
        else:
            arr_1_numberofNewExecutionsDivAllT.append(float(srcData_1_numberofNewExecutionsDivAllT[i]))
    ## numberofNewTransitionsDivAllT
    arr_1_numberofNewTransitionsDivAllT = []
    srcData_1_numberofNewTransitionsDivAllT = srcData_1[9].split(" ")
    for i in range(len(srcData_1_numberofNewTransitionsDivAllT)):
        if i == 0:
            pass
        else:
            arr_1_numberofNewTransitionsDivAllT.append(float(srcData_1_numberofNewTransitionsDivAllT[i]))
    ## numberofNewStatesDivAllT
    arr_1_numberofNewStatesDivAllT = []
    srcData_1_numberofNewStatesDivAllT = srcData_1[10].split(" ")
    for i in range(len(srcData_1_numberofNewStatesDivAllT)):
        if i == 0:
            pass
        else:
            arr_1_numberofNewStatesDivAllT.append(float(srcData_1_numberofNewStatesDivAllT[i]))
    ## overallEffectivenessMeasure
    arr_1_overallEffectivenessMeasure = []
    srcData_1_overallEffectivenessMeasure = srcData_1[11].split(" ")
    for i in range(len(srcData_1_overallEffectivenessMeasure)):
        if i == 0:
            pass
        else:
            arr_1_overallEffectivenessMeasure.append(float(srcData_1_overallEffectivenessMeasure[i]))

    srcData_2 = array_2.split("; ")


    ## timeCostofMOSASearch
    arr_2_timeCostofMOSASearch = []
    srcData_2_timeCostofMOSASearch = srcData_2[0].split(" ")
    for i in range(len(srcData_2_timeCostofMOSASearch)):
        if i == 0:
            pass
        else:
            arr_2_timeCostofMOSASearch.append((float(srcData_2_timeCostofMOSASearch[i])) / 60000)
    ## otherTimeCost_1
    arr_2_otherTimeCost_1 = []
    srcData_2_otherTimeCost_1 = srcData_2[1].split(" ")
    for i in range(len(srcData_2_otherTimeCost_1)):
        if i == 0:
            pass
        else:
            arr_2_otherTimeCost_1.append(float(srcData_2_otherTimeCost_1[i]))
    ## timeCostofExecution
    arr_2_timeCostofExecution = []
    srcData_2_timeCostofExecution = srcData_2[2].split(" ")
    for i in range(len(srcData_2_timeCostofExecution)):
        if i == 0:
            pass
        else:
            arr_2_timeCostofExecution.append((float(srcData_2_timeCostofExecution[i])) / 60000)
    ## totalTimeCost
    arr_2_totalTimeCost = []
    srcData_2_totalTimeCost = srcData_2[3].split(" ")
    for i in range(len(srcData_2_totalTimeCost)):
        if i == 0:
            pass
        else:
            arr_2_totalTimeCost.append((float(srcData_2_totalTimeCost[i])) / 60000)
    ## totalExecutions
    arr_2_totalExecutions = []
    srcData_2_totalExecutions = srcData_2[4].split(" ")
    for i in range(len(srcData_2_totalExecutions)):
        if i == 0:
            pass
        else:
            arr_2_totalExecutions.append(float(srcData_2_totalExecutions[i]))
    # print(arr_2_totalExecutions)
    ## numberofNewExecutions
    arr_2_numberofNewExecutions = []
    srcData_2_numberofNewExecutions = srcData_2[5].split(" ")
    for i in range(len(srcData_2_numberofNewExecutions)):
        if i == 0:
            pass
        else:
            arr_2_numberofNewExecutions.append(float(srcData_2_numberofNewExecutions[i]))
    ## numberofNewTransitions
    arr_2_numberofNewTransitions = []
    srcData_2_numberofNewTransitions = srcData_2[6].split(" ")
    for i in range(len(srcData_2_numberofNewTransitions)):
        if i == 0:
            pass
        else:
            arr_2_numberofNewTransitions.append(float(srcData_2_numberofNewTransitions[i]))
    ## numberofNewStates
    arr_2_numberofNewStates = []
    srcData_2_numberofNewStates = srcData_2[7].split(" ")
    for i in range(len(srcData_2_numberofNewStates)):
        if i == 0:
            pass
        else:
            arr_2_numberofNewStates.append(float(srcData_2_numberofNewStates[i]))
    ## numberofNewExecutionsDivAllT
    arr_2_numberofNewExecutionsDivAllT = []
    srcData_2_numberofNewExecutionsDivAllT = srcData_2[8].split(" ")
    for i in range(len(srcData_2_numberofNewExecutionsDivAllT)):
        if i == 0:
            pass
        else:
            arr_2_numberofNewExecutionsDivAllT.append(float(srcData_2_numberofNewExecutionsDivAllT[i]))
    ## numberofNewTransitionsDivAllT
    arr_2_numberofNewTransitionsDivAllT = []
    srcData_2_numberofNewTransitionsDivAllT = srcData_2[9].split(" ")
    for i in range(len(srcData_2_numberofNewTransitionsDivAllT)):
        if i == 0:
            pass
        else:
            arr_2_numberofNewTransitionsDivAllT.append(float(srcData_2_numberofNewTransitionsDivAllT[i]))
    ## numberofNewStatesDivAllT
    arr_2_numberofNewStatesDivAllT = []
    srcData_2_numberofNewStatesDivAllT = srcData_2[10].split(" ")
    for i in range(len(srcData_2_numberofNewStatesDivAllT)):
        if i == 0:
            pass
        else:
            arr_2_numberofNewStatesDivAllT.append(float(srcData_2_numberofNewStatesDivAllT[i]))
    ## overallEffectivenessMeasure
    arr_2_overallEffectivenessMeasure = []
    srcData_2_overallEffectivenessMeasure = srcData_2[11].split(" ")
    for i in range(len(srcData_2_overallEffectivenessMeasure)):
        if i == 0:
            pass
        else:
            arr_2_overallEffectivenessMeasure.append(float(srcData_2_overallEffectivenessMeasure[i]))



    # p values
    p_timeCostofMOSASearch = get_p_value(arr_1_timeCostofMOSASearch, arr_2_timeCostofMOSASearch)
    p_otherTimeCost_1 = get_p_value(arr_1_otherTimeCost_1, arr_2_otherTimeCost_1)
    p_timeCostofExecution = get_p_value(arr_1_timeCostofExecution, arr_2_timeCostofExecution)
    p_totalTimeCost = get_p_value(arr_1_totalTimeCost, arr_2_totalTimeCost)
    p_totalExecutions = get_p_value(arr_1_totalExecutions, arr_2_totalExecutions)
    p_numberofNewExecutions = get_p_value(arr_1_numberofNewExecutions, arr_2_numberofNewExecutions)
    p_numberofNewTransitions = get_p_value(arr_1_numberofNewTransitions, arr_2_numberofNewTransitions)
    p_numberofNewStates = get_p_value(arr_1_numberofNewStates, arr_2_numberofNewStates)
    p_numberofNewExecutionsDivAllT = get_p_value(arr_1_numberofNewExecutionsDivAllT, arr_2_numberofNewExecutionsDivAllT)
    p_numberofNewTransitionsDivAllT = get_p_value(arr_1_numberofNewTransitionsDivAllT, arr_2_numberofNewTransitionsDivAllT)
    p_numberofNewStatesDivAllT = get_p_value(arr_1_numberofNewStatesDivAllT, arr_2_numberofNewStatesDivAllT)
    p_overallEffectivenessMeasure = get_p_value(arr_1_overallEffectivenessMeasure, arr_2_overallEffectivenessMeasure)

    # print(type(p_totalExecutions))

    if str(p_totalExecutions) == "nan":
        p_totalExecutions = -1


    print("%.6f" % p_timeCostofMOSASearch)
    print("%.6f" % p_otherTimeCost_1)
    print("%.6f" % p_timeCostofExecution)
    print("%.6f" % p_totalTimeCost)
    print("%.6f" % p_totalExecutions)
    print("%.6f" % p_numberofNewExecutions)
    print("%.6f" % p_numberofNewTransitions)
    print("%.6f" % p_numberofNewStates)
    print("%.6f" % p_numberofNewExecutionsDivAllT)
    print("%.6f" % p_numberofNewTransitionsDivAllT)
    print("%.6f" % p_numberofNewStatesDivAllT)
    print("%.6f" % p_overallEffectivenessMeasure)

    # arrA = [1, 2, 3, 4, 5]
    # arrB = [6, 7, 8, 9, 10]
    # p = get_p_value(arrA, arrB)
    # print(p)
