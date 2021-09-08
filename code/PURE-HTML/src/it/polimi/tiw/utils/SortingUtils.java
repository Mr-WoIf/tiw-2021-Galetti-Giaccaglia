package it.polimi.tiw.utils;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import it.polimi.tiw.beans.Course;
import it.polimi.tiw.beans.Student;

public class SortingUtils {

    private SortingUtils(){}

    public static class NaturalComparator<T extends Comparable<? super T>> implements Comparator<T> {
        public int compare(T a, T b) {
            return a.compareTo(b);
        }
    }
    
    //currently unused method (sql query is doing the job)
    public static void sortCoursesListByNameDescending(List<Course> list) {
      	 Comparator<String> cmp = new NaturalComparator<>();
      	list.sort((o1, o2) -> cmp.compare(o2.getName(), o1.getName()));
      }


    public static void sortByNameAscending(Map<Student, MutablePair<Integer,String>> unsortedMap) {

        List<Map.Entry<Student, MutablePair<Integer, String>>> list = new LinkedList<>(unsortedMap.entrySet());
        sortStudentsNames(list, new NaturalComparator<>(), true);
        fillMap(list, unsortedMap);
    }
    
    public static void sortByNameDescending(Map<Student, MutablePair<Integer,String>> unsortedMap) {

        List<Map.Entry<Student, MutablePair<Integer, String>>> list = new LinkedList<>(unsortedMap.entrySet());
        sortStudentsNames(list, new NaturalComparator<>(), false);
        fillMap(list, unsortedMap);
    }

    public static void sortBySurnameAscending(Map<Student, MutablePair<Integer,String>> unsortedMap) {

        List<Map.Entry<Student, MutablePair<Integer, String>>> list = new LinkedList<>(unsortedMap.entrySet());
        sortStudentsSurnames(list, new NaturalComparator<>(), true);
        fillMap(list, unsortedMap);
    }

    public static void sortBySurnameDescending(Map<Student, MutablePair<Integer,String>> unsortedMap) {

        List<Map.Entry<Student, MutablePair<Integer, String>>> list = new LinkedList<>(unsortedMap.entrySet());
        sortStudentsSurnames(list, new NaturalComparator<>(), false);
        fillMap(list, unsortedMap);
    }

    public static void sortByIDsAscending(Map<Student, MutablePair<Integer,String>> unsortedMap) {

        List<Map.Entry<Student, MutablePair<Integer, String>>> list = new LinkedList<>(unsortedMap.entrySet());
        sortStudentsIDs(list, new NaturalComparator<>(), true);
        fillMap(list, unsortedMap);
    }

    public static void sortByIDsDescending(Map<Student, MutablePair<Integer,String>> unsortedMap) {

        List<Map.Entry<Student, MutablePair<Integer, String>>> list = new LinkedList<>(unsortedMap.entrySet());
        sortStudentsIDs(list, new NaturalComparator<>(), false);
        fillMap(list, unsortedMap);
    }

    public static void sortByEmailAscending(Map<Student, MutablePair<Integer,String>> unsortedMap) {

        List<Map.Entry<Student, MutablePair<Integer, String>>> list = new LinkedList<>(unsortedMap.entrySet());
        sortStudentsEmails(list, new NaturalComparator<>(), true);
        fillMap(list, unsortedMap);
    }

    public static void sortByEmailDescending(Map<Student, MutablePair<Integer,String>> unsortedMap) {

        List<Map.Entry<Student, MutablePair<Integer, String>>> list = new LinkedList<>(unsortedMap.entrySet());
        sortStudentsEmails(list, new NaturalComparator<>(), false);
        fillMap(list, unsortedMap);
    }

    public static void sortByDegreeAscending(Map<Student, MutablePair<Integer,String>> unsortedMap) {

        List<Map.Entry<Student, MutablePair<Integer, String>>> list = new LinkedList<>(unsortedMap.entrySet());
        sortStudentsDegrees(list, new NaturalComparator<>(), true);
        fillMap(list, unsortedMap);
    }

    public static void sortByDegreeDescending(Map<Student, MutablePair<Integer,String>> unsortedMap) {

        List<Map.Entry<Student, MutablePair<Integer, String>>> list = new LinkedList<>(unsortedMap.entrySet());
        sortStudentsDegrees(list, new NaturalComparator<>(), false);
        fillMap(list, unsortedMap);
    }

    public static void sortByGradeAscending(Map<Student, MutablePair<Integer,String>> unsortedMap) {

        List<Map.Entry<Student, MutablePair<Integer, String>>> list = new LinkedList<>(unsortedMap.entrySet());
        sortStudentsGrades(list, new NaturalComparator<>(), true);
        fillMap(list, unsortedMap);
    }

    public static void sortByGradeDescending(Map<Student, MutablePair<Integer,String>> unsortedMap) {

        List<Map.Entry<Student, MutablePair<Integer, String>>> list = new LinkedList<>(unsortedMap.entrySet());
        sortStudentsGrades(list, new NaturalComparator<>(), false);
        fillMap(list, unsortedMap);
    }

    public static void sortByGradeStateAscending(Map<Student, MutablePair<Integer,String>> unsortedMap) {

        List<Map.Entry<Student, MutablePair<Integer, String>>> list = new LinkedList<>(unsortedMap.entrySet());
        sortStudentsGradeStates(list, new NaturalComparator<>(), true);
        fillMap(list, unsortedMap);
    }

    public static void sortByGradeStateDescending(Map<Student, MutablePair<Integer,String>> unsortedMap) {

        List<Map.Entry<Student, MutablePair<Integer, String>>> list = new LinkedList<>(unsortedMap.entrySet());
        sortStudentsGradeStates(list, new NaturalComparator<>(), false);
        fillMap(list, unsortedMap);
    }
 
    private static void sortStudentsNames(List<Map.Entry<Student, MutablePair<Integer, String>>> list, Comparator<String> cmp, final boolean ascendingOrder) {
        list.sort((o1 , o2) -> {
            if (ascendingOrder) {
                return cmp.compare(o1.getKey().getName() , o2.getKey().getName());
            } else {
                return cmp.compare(o2.getKey().getName() , o1.getKey().getName());
            }
        });
    }

    private static void sortStudentsSurnames(List<Map.Entry<Student, MutablePair<Integer, String>>> list, Comparator<String> cmp, final boolean ascendingOrder) {
        list.sort((o1 , o2) -> {
            if (ascendingOrder) {
                return cmp.compare(o1.getKey().getSurname() , o2.getKey().getSurname());
            } else {
                return cmp.compare(o2.getKey().getSurname() , o1.getKey().getSurname());
            }
        });
    }

    private static void sortStudentsIDs(List<Map.Entry<Student, MutablePair<Integer, String>>> list, Comparator<Integer> cmp, final boolean ascendingOrder) {
        list.sort((o1 , o2) -> {
            if (ascendingOrder) {
                return cmp.compare(o1.getKey().getId() , o2.getKey().getId());
            } else {
                return cmp.compare(o2.getKey().getId() , o1.getKey().getId());
            }
        });
    }

    private static void fillMap(List<Map.Entry<Student, MutablePair<Integer, String>>> list , Map<Student, MutablePair<Integer,String>> unsortedMap)
    {

        // Maintaining insertion order with the help of LinkedList
        unsortedMap.clear();
        for (Map.Entry<Student, MutablePair<Integer,String>> entry : list)
        {
            unsortedMap.put(entry.getKey(), entry.getValue());
        }

    }

    private static void sortStudentsEmails(List<Map.Entry<Student, MutablePair<Integer, String>>> list, Comparator<String> cmp, final boolean ascendingOrder) {
        list.sort((o1 , o2) -> {
            if (ascendingOrder) {
                return cmp.compare(o1.getKey().getEmail() , o2.getKey().getEmail());
            } else {
                return cmp.compare(o2.getKey().getEmail() , o1.getKey().getEmail());
            }
        });
    }

    private static void sortStudentsDegrees(List<Map.Entry<Student, MutablePair<Integer, String>>> list, Comparator<String> cmp, final boolean ascendingOrder) {
        list.sort((o1 , o2) -> {
            if (ascendingOrder) {
                return cmp.compare(o1.getKey().getDegree() , o2.getKey().getDegree());
            } else {
                return cmp.compare(o2.getKey().getDegree() , o1.getKey().getDegree());
            }
        });
    }

    private static void sortStudentsGrades(List<Map.Entry<Student, MutablePair<Integer, String>>> list, Comparator<Integer> cmp, final boolean ascendingOrder) {
        list.sort((o1 , o2) -> {
            if (ascendingOrder) {
                return cmp.compare(o1.getValue().getLeft() , o2.getValue().getLeft());
            } else {
                return cmp.compare(o2.getValue().getLeft() , o1.getValue().getLeft());
            }
        });
    }

    private static void sortStudentsGradeStates(List<Map.Entry<Student, MutablePair<Integer, String>>> list, Comparator<String> cmp, final boolean ascendingOrder) {
        list.sort((o1 , o2) -> {
            if (ascendingOrder) {
                return cmp.compare(o1.getValue().getRight() , o2.getValue().getRight());
            } else {
                return cmp.compare(o2.getValue().getRight() , o1.getValue().getRight());
            }
        });
    }
}

