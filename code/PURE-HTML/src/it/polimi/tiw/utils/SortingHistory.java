package it.polimi.tiw.utils;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import it.polimi.tiw.beans.Student;
import it.polimi.tiw.utils.SORTING_TYPE.ThreeState;

public class SortingHistory {
	
	private final Map<SORTING_TYPE, SORTING_TYPE.ThreeState> typesMap;
	
	public SortingHistory() {
		typesMap = Arrays.stream(SORTING_TYPE.values()).collect(Collectors.toMap(type -> type, type -> SORTING_TYPE.ThreeState.UNSET));
	}
	
	public void sort(Map<Student, MutablePair<Integer,String>> unsortedMap, SORTING_TYPE type) {
		SORTING_TYPE.ThreeState state = typesMap.get(type);
		type.sort(unsortedMap, state);
		toggleState(type);
	}

	private void toggleState(SORTING_TYPE type){ 
		 SORTING_TYPE.ThreeState prevState = typesMap.get(type);
		 SORTING_TYPE.ThreeState newState = prevState.equals(SORTING_TYPE.ThreeState.ASCENDING)? SORTING_TYPE.ThreeState.DESCENDING : SORTING_TYPE.ThreeState.ASCENDING;
		 typesMap.put(type, newState);
		
	}
	
	private void setState(SORTING_TYPE type, ThreeState state) {
		typesMap.put(type, state);
	}
	
	 public void resetAllExceptOne(SORTING_TYPE typeToNotReset){
	        typesMap.keySet().stream()
	                .filter(sortingType -> !sortingType.equals(typeToNotReset))
	                .forEach(sortingType -> setState(sortingType, ThreeState.UNSET));
	    }	

}
