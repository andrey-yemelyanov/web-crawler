package helvidios.search.searcher;

import java.util.*;

/**
 * Represents a closed interval.
 */
public class Interval{
    
    private final int from;
    private final int to;

    public int from() { return from; }
    public int to() { return to; }

    public Interval(int from, int to){
        this.from = from;
        this.to = to;
    }

    /**
     * Returns true if two intervals overlap.
     * @param i1 first interval
     * @param i2 second interval
     */
    static boolean overlap(Interval i1, Interval i2){
        return !((i1.to < i2.from) || (i2.to < i1.from));
    } 

    /**
     * Returns a merged interval from two overlapping intervals.
     * @param i1 first interval
     * @param i2 second interval
     */
    static Interval merge(Interval i1, Interval i2){
        return new Interval(
            Math.min(i1.from, i2.from),
            Math.max(i1.to, i2.to)
        );
    }

    /**
     * Merges a list of potentially overlapping intervals into a list of strictly disjoint intervals.
     * @param intervals list of intervals sorted by their 'from' coordinate
     */
    static List<Interval> mergeIntervals(List<Interval> intervals){
        Deque<Interval> mergedIntervals = new LinkedList<>();
        if(intervals.isEmpty()) return Arrays.asList();
        mergedIntervals.add(intervals.get(0));
        for(int i = 1; i < intervals.size(); i++){
            Interval current = intervals.get(i);
            if(overlap(current, mergedIntervals.getLast())){
                mergedIntervals.add(merge(current, mergedIntervals.removeLast()));
            }else{
                mergedIntervals.add(current);
            }
        }
        return new ArrayList<>(mergedIntervals);
    }
}