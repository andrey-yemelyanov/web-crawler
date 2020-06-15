# web-search
Web search project consisting of a web crawler, indexer and searcher.

Indexer algorithm
1. Get document from the document queue
2. Tokenize the document and strip all stopwords
3. Get lemmas from the tokens
4. Group lemmas into a list of tuples (term, docId, termFrequency)
5. Add the list of tuples to the global term list
