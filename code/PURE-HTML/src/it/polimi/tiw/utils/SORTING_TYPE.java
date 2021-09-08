package it.polimi.tiw.utils;

import java.util.Map;

import it.polimi.tiw.beans.Student;

public enum SORTING_TYPE {

    STUDENTS_IDS{
        @Override
        public void sort(Map<Student, MutablePair<Integer,String>> unsortedMap, ThreeState state){
            if(state.equals(ThreeState.DESCENDING) || state.equals(ThreeState.UNSET))
                SortingUtils.sortByIDsAscending(unsortedMap);
            else
                SortingUtils.sortByIDsDescending(unsortedMap);
        }
    },

            
    SURNAME{
        @Override
        public void sort(Map<Student, MutablePair<Integer,String>> unsortedMap, ThreeState state){
            if(state.equals(ThreeState.DESCENDING) || state.equals(ThreeState.UNSET))
                SortingUtils.sortBySurnameAscending(unsortedMap);
            else
                SortingUtils.sortBySurnameDescending(unsortedMap);
 
        }
    },
    
    NAME{
        @Override
        public void sort(Map<Student, MutablePair<Integer,String>> unsortedMap, ThreeState state){
            if(state.equals(ThreeState.DESCENDING) || state.equals(ThreeState.UNSET))
                SortingUtils.sortByNameAscending(unsortedMap);
            else
                SortingUtils.sortByNameDescending(unsortedMap);
        }
    },
    EMAIL{
        @Override
        public void sort(Map<Student, MutablePair<Integer,String>> unsortedMap, ThreeState state){
            if(state.equals(ThreeState.DESCENDING) || state.equals(ThreeState.UNSET))
                SortingUtils.sortByEmailAscending(unsortedMap);
            else
                SortingUtils.sortByEmailDescending(unsortedMap);   
        }
    },
        
    DEGREE{
        @Override
        public void sort(Map<Student, MutablePair<Integer,String>> unsortedMap, ThreeState state){

            if(state.equals(ThreeState.DESCENDING) || state.equals(ThreeState.UNSET))
                SortingUtils.sortByDegreeAscending(unsortedMap);
            else
                SortingUtils.sortByDegreeDescending(unsortedMap);
            
        }
       
    },

    GRADE{
        @Override
        public void sort(Map<Student, MutablePair<Integer,String>> unsortedMap, ThreeState state){
            if(state.equals(ThreeState.DESCENDING) || state.equals(ThreeState.UNSET))
                SortingUtils.sortByGradeAscending(unsortedMap);
         
            else
                SortingUtils.sortByGradeDescending(unsortedMap);
        }
    },
    
    GRADE_STATE{
        @Override
        public void sort(Map<Student, MutablePair<Integer,String>> unsortedMap, ThreeState state){
       

            if(state.equals(ThreeState.DESCENDING) || state.equals(ThreeState.UNSET))
                SortingUtils.sortByGradeStateAscending(unsortedMap);
            else
                SortingUtils.sortByGradeStateDescending(unsortedMap);
               
        }
    },
    
    INVALID{
        @Override
        public void sort(Map<Student, MutablePair<Integer,String>> unsortedMap, ThreeState state){
        //no sorting handling needed
        }
    };

    public enum ThreeState {
        ASCENDING, //ascending order 
        DESCENDING, //descending order 
        UNSET;
    }

    public abstract void sort(Map<Student, MutablePair<Integer,String>> unsortedMap, ThreeState state);

}