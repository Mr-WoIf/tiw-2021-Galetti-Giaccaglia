package it.polimi.tiw.utils;

import java.util.Arrays;
import java.util.Map;

import it.polimi.tiw.beans.Student;

public enum SORTING_TYPE {

    STUDENTS_IDS(ThreeState.UNSET){
        @Override
        public void sort(Map<Student, MutablePair<Integer,String>> unsortedMap){
            ThreeState thisState = getState();

            if(thisState.equals(ThreeState.DESCENDING) || thisState.equals(ThreeState.UNSET)) {
                SortingUtils.sortByIDsAscending(unsortedMap);
                this.setState(ThreeState.ASCENDING);
            }
            else {
                SortingUtils.sortByIDsDescending(unsortedMap);
                this.setState(ThreeState.DESCENDING);
            }
        }
    },
    SURNAME(ThreeState.UNSET){
        @Override
        public void sort(Map<Student, MutablePair<Integer,String>> unsortedMap){
            ThreeState thisState = getState();

            if(thisState.equals(ThreeState.DESCENDING) || thisState.equals(ThreeState.UNSET)) {
                SortingUtils.sortBySurnameAscending(unsortedMap);
                this.setState(ThreeState.ASCENDING);
            }
            else {
                SortingUtils.sortBySurnameDescending(unsortedMap);
                this.setState(ThreeState.DESCENDING);
            }
        }
    },
    NAME(ThreeState.UNSET){
        @Override
        public void sort(Map<Student, MutablePair<Integer,String>> unsortedMap){
            ThreeState thisState = getState();

            if(thisState.equals(ThreeState.DESCENDING) || thisState.equals(ThreeState.UNSET)) {
                SortingUtils.sortByNameAscending(unsortedMap);
                this.setState(ThreeState.ASCENDING);
            }
            else {
                SortingUtils.sortByNameDescending(unsortedMap);
                this.setState(ThreeState.DESCENDING);
            }

        }
    },
    EMAIL(ThreeState.UNSET){
        @Override
        public void sort(Map<Student, MutablePair<Integer,String>> unsortedMap){
            ThreeState thisState = getState();

            if(thisState.equals(ThreeState.DESCENDING) || thisState.equals(ThreeState.UNSET)) {
                SortingUtils.sortByEmailAscending(unsortedMap);
                this.setState(ThreeState.ASCENDING);
            }
            else {
                SortingUtils.sortByEmailDescending(unsortedMap);
                this.setState(ThreeState.DESCENDING);
            }
        }
    },
    DEGREE(ThreeState.UNSET){
        @Override
        public void sort(Map<Student, MutablePair<Integer,String>> unsortedMap){
            ThreeState thisState = getState();

            if(thisState.equals(ThreeState.DESCENDING) || thisState.equals(ThreeState.UNSET)) {
                SortingUtils.sortByDegreeAscending(unsortedMap);
                this.setState(ThreeState.ASCENDING);
            }
            else {
                SortingUtils.sortByDegreeDescending(unsortedMap);
                this.setState(ThreeState.DESCENDING);
            }


        }
    },
    GRADE(ThreeState.UNSET){
        @Override
        public void sort(Map<Student, MutablePair<Integer,String>> unsortedMap){
            ThreeState thisState = getState();

            if(thisState.equals(ThreeState.DESCENDING) || thisState.equals(ThreeState.UNSET)) {
                SortingUtils.sortByGradeAscending(unsortedMap);
                this.setState(ThreeState.ASCENDING);
            }
            else {
                SortingUtils.sortByGradeDescending(unsortedMap);
                this.setState(ThreeState.DESCENDING);
            }

        }
    },
    GRADE_STATE(ThreeState.UNSET){
        @Override
        public void sort(Map<Student, MutablePair<Integer,String>> unsortedMap){
            ThreeState thisState = getState();

            if(thisState.equals(ThreeState.DESCENDING) || thisState.equals(ThreeState.UNSET)) {
                SortingUtils.sortByGradeStateAscending(unsortedMap);
                this.setState(ThreeState.ASCENDING);
            }
            else {
                SortingUtils.sortByGradeStateDescending(unsortedMap);
                this.setState(ThreeState.DESCENDING);
            }

        }
    },
    INVALID(ThreeState.UNSET){
        @Override
        public void sort(Map<Student, MutablePair<Integer,String>> unsortedMap){

        }
    };

    enum ThreeState {
        ASCENDING, //ascending order 
        DESCENDING, //descending order 
        UNSET;
    }

    public abstract void sort(Map<Student, MutablePair<Integer,String>> unsortedMap);

    private ThreeState state;

    SORTING_TYPE(ThreeState state){
        this.state = state; }

    public void setState(ThreeState state){
        this.state = state;
    }

    public ThreeState getState(){
        return state;
    }


    public void toggle(){ this.state = state.equals(ThreeState.ASCENDING)? ThreeState.DESCENDING : ThreeState.ASCENDING;}

    public static void resetAllExceptOne(SORTING_TYPE typeToNotReset){
        Arrays.stream(SORTING_TYPE.values())
                .filter(sortingType -> !sortingType.equals(typeToNotReset))
                .forEach(sortingType -> sortingType.setState(ThreeState.UNSET));
    }
}