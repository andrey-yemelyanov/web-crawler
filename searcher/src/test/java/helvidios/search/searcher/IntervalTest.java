package helvidios.search.searcher;

import org.junit.Test;
import java.util.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class IntervalTest {
    
    @Test
    public void mergeIntervals(){
        List<Interval> intervals = Arrays.asList(
            new Interval(1, 2),
            new Interval(3, 4),
            new Interval(5, 6),
            new Interval(7, 8)
        );
        assertThat(Interval.mergeIntervals(intervals), is(intervals));

        intervals = Arrays.asList(
            new Interval(1, 2),
            new Interval(2, 3),
            new Interval(3, 4),
            new Interval(4, 5)
        );
        assertThat(Interval.mergeIntervals(intervals), is(Arrays.asList(
            new Interval(1, 5)
        )));

        intervals = Arrays.asList(
            new Interval(1, 2)
        );
        assertThat(Interval.mergeIntervals(intervals), is(Arrays.asList(
            new Interval(1, 2)
        )));

        intervals = Arrays.asList(
            new Interval(5, 7),
            new Interval(1, 10),
            new Interval(11, 15)
        );
        assertThat(Interval.mergeIntervals(intervals), is(Arrays.asList(
            new Interval(1, 10),
            new Interval(11, 15)
        )));
    }

    @Test
    public void overlap(){
        Interval i1 = new Interval(1, 2);
        Interval i2 = new Interval(3, 4);
        assertThat(Interval.overlap(i1, i2), is(false));

        i1 = new Interval(1, 2);
        i2 = new Interval(2, 4);
        assertThat(Interval.overlap(i1, i2), is(true));

        i1 = new Interval(2, 4);
        i2 = new Interval(3, 5);
        assertThat(Interval.overlap(i1, i2), is(true));

        i1 = new Interval(1, 2);
        i2 = new Interval(1, 3);
        assertThat(Interval.overlap(i1, i2), is(true));

        i1 = new Interval(3, 4);
        i2 = new Interval(1, 2);
        assertThat(Interval.overlap(i1, i2), is(false));
    }

}