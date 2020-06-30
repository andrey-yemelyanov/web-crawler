package helvidios.search.searcher;

import org.apache.logging.log4j.Logger;
import org.junit.Test;
import helvidios.search.index.Posting;
import helvidios.search.index.Term;
import helvidios.search.index.storage.IndexRepository;
import helvidios.search.linguistics.ApacheNlpLemmatizer;
import helvidios.search.storage.DocId;
import helvidios.search.storage.DocumentRepository;
import helvidios.search.storage.HtmlDocument;
import helvidios.search.tokenizer.HtmlTokenizer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import java.util.*;

public class TermHighlighterTest {

    @Test
    public void getTextFromHtml() throws IOException {
        HtmlTokenizer tokenizer = new HtmlTokenizer();
        String html = new String(Files.readAllBytes(Paths.get("C:\\Users\\A351509\\Downloads\\ex.html")));
        String text = tokenizer.getText(html);
        System.out.println(text);
        
        List<String> query = Arrays.asList("exception", "message");
        List<Integer> window = new ArrayList<>();
        for(String term : query){
            Matcher matcher = Pattern.compile("\\b" + term, Pattern.CASE_INSENSITIVE).matcher(text);
            if(matcher.find()){
                window.add(matcher.start());
            }
        }

        if(!window.isEmpty()){
            int minPos = window.stream().mapToInt(pos -> pos).min().getAsInt();
            int maxPos = window.stream().mapToInt(pos -> pos).max().getAsInt();
            String snippet = text.substring(minPos, maxPos + 200);

            System.out.println(snippet);

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
                System.out.printf("Matched: %s at position %d\n", matcher.group(), matcher.start());
                offsets.computeIfAbsent(matcher.group().toLowerCase(), k -> new ArrayList<>()).add(matcher.start());
            }
            System.out.println(offsets);
        }
        
    }

    private static class Item{
        String word;
        int pos;
        int index;

        Item(String word, int pos, int index){
            this.word = word;
            this.pos = pos;
            this.index = index;
        }
    }

    Map<String, Integer> smallestWindow(Map<String, List<Integer>> offsets, String text){
        List<Iterator<Item>> items = new ArrayList<>();
        int index = 0;
        for(String key : offsets.keySet()){
            final int i = index;
            items.add(offsets.get(key).stream().map(pos -> new Item(key, pos, i)).collect(Collectors.toList()).iterator());
            index++;
        }

        List<Item> window = new ArrayList<>();
        for(Iterator<Item> it : items){
            window.add(it.next());
        }

        String snippet = text.substring(getMin(window).pos, getMax(window).pos + 200);
        System.out.println(snippet);

        int minWindowLen = Integer.MAX_VALUE;
        List<Item> bestWindow = new ArrayList<>();

        while(true){
            Item maxItem = getMax(window);
            Item minItem = getMin(window);
            int windowLen = maxItem.pos - minItem.pos + 1;
            if(windowLen < minWindowLen){
                minWindowLen = windowLen;
                bestWindow = new ArrayList<>(window);
            }
            window.remove(minItem);
            if(!items.get(minItem.index).hasNext()) break;
            window.add(items.get(minItem.index).next());
        }

        Item maxItem = getMax(bestWindow);
        Item minItem = getMin(bestWindow);

        snippet = text.substring(minItem.pos - 200, maxItem.pos + 200);

        System.out.println(snippet);

        return null;
    }

    Item getMin(List<Item> window){
        Item min = window.get(0);
        for(Item item : window){
            if(item.pos < min.pos){
                min = item;
            }
        }
        return min;
    }

    Item getMax(List<Item> window){
        Item max = window.get(0);
        for(Item item : window){
            if(item.pos > max.pos){
                max = item;
            }
        }
        return max;
    }

}