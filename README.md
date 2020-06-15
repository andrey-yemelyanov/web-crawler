# web-search
Web search project consisting of a web crawler, indexer and searcher.

# Indexer algorithm

Index construction is done by sorting and grouping and follows the algorithm specified in the [Introduction to Information Retrieval](https://nlp.stanford.edu/IR-book/information-retrieval-book.html) book.

![Indexing process](https://nlp.stanford.edu/IR-book/html/htmledition/img54.png)

As a preprocessing step, the indexer will add integer IDs of all documents stored in the repository into a thread-safe queue for further processing by concurrent document processors.

Multiple document processors run in parallel (using Java ExecutorService), where each processor has the following workflow:

1. Get docId from the document queue
2. Get document content for docId
2. Tokenize the document and strip all stopwords
3. Get lemmas (normalized words/terms) from the tokens
4. Build a list consisting of tuples **(term, docId)**
5. Add the list of tuples to the global term list

Once all the documents have been processed, the indexer will sort the global term list first by each term and then by docId. Equal terms will occur together. As a next step, the indexer will generate a sorted postings list for each distinct term. The postings list will consist of tuples **(docId, termFrequency)** sorted by docId in increasing order.

The resulting in-memory index will have the following example dictionary structure:

`term1 -> [(docId1, 432), (docId2, 2), (docId6, 21)]`
`term2 -> [(docId2, 12), (docId6, 21)]`
`term3 -> [(docId6, 1)]`
etc...

The index will then be stored in MongoDB for further use by the searcher.


