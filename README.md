# web-search
Web search project consisting of a web crawler, indexer and searcher.

# Indexer algorithm

Index construction is sort-based and follows the algorithm specified in the [Introduction to Information Retrieval](https://nlp.stanford.edu/IR-book/information-retrieval-book.html) book.

![Indexing process](https://nlp.stanford.edu/IR-book/html/htmledition/img54.png)

Multiple indexers run in parallel (using Java ExecutorService), where each indexer has the following workflow:

1. Get document from the document queue
2. Tokenize the document and strip all stopwords
3. Get lemmas from the tokens
4. Group lemmas into a list of tuples (term, docId, termFrequency)
5. Add the list of tuples to the global term list
