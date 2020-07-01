package helvidios.search.searcher;

import java.util.*;
import java.util.regex.*;

/**
 * Analyzes input text and generates a short snippet with highlights for a given query.
 */
public class TextHighlighter {

    private final String text;

    // how far from a query term do we expand the substring to the left and right (this gives us context in which the term occurs in a document)
    private static final int EXPANSION = 300;

    public TextHighlighter(String text){
        this.text = text;
    }

    /**
     * Generates a short summary snippet from the text with query terms highlighted.
     * @param query list of normalized query tokens
     */
    public TextSnippet generateSnippet(List<String> query){
        final String snippet = generateSnippetText(query);
        return new TextSnippet(snippet, generateOffsets(query, snippet));
    }

    private Map<String, List<Integer>> generateOffsets(List<String> query, String snippet){
        // build regex pattern of this form: \\b(term1|term2|term3...)
        StringBuilder pattern = new StringBuilder("\\b(");
        for(int i = 0; i < query.size(); i++){
            pattern.append(query.get(i));
            if(i < query.size() - 1){
                pattern.append("|");
            }
        }
        pattern.append(")");

        Matcher matcher = Pattern.compile(pattern.toString(), Pattern.CASE_INSENSITIVE).matcher(snippet);
        Map<String, List<Integer>> offsets = new HashMap<>();
        while(matcher.find()){
            offsets.computeIfAbsent(matcher.group().toLowerCase(), k -> new ArrayList<>()).add(matcher.start());
        }
        return offsets;
    }

    private String generateSnippetText(List<String> query){
        List<Interval> intervals = getTextIntervals(query);
        StringBuilder snippet = new StringBuilder("...");
        for(Interval interval : intervals){
            snippet.append(text.substring(interval.from(), interval.to()).replace("\n", " ").replace("\r", " "));
            snippet.append("...");
        }
        return snippet.toString();
    }

    private List<Interval> getTextIntervals(List<String> query){
        List<Interval> intervals = new ArrayList<>();
        for(String term : query){
            Matcher matcher = Pattern.compile("\\b" + term, Pattern.CASE_INSENSITIVE).matcher(text);
            if(matcher.find()){
                int pos = matcher.start();
                intervals.add(
                    new Interval(Math.max(pos - EXPANSION, 0), Math.min(pos + EXPANSION, text.length() - 1))  
                );
            }
        }
        // sort intervals by their 'from' coordinate
        Collections.sort(intervals, (i1, i2) -> Integer.compare(i1.from(), i2.from()));
        return Interval.mergeIntervals(intervals);
    }

}