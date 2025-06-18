package com.vplay.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.MMapDirectory;

public class LuceneIndexer {
    private final Directory index;
    private final Analyzer analyzer;

    public LuceneIndexer() throws IOException {
        index = new MMapDirectory(Paths.get("lucene_index"));
        analyzer = new StandardAnalyzer();
    }

    public void indexFromFile(String filePath) throws IOException {
        List<String> paragraphs = Files.readAllLines(Paths.get(filePath))
            .stream()
            .map(String::trim)
            .filter(line -> !line.isEmpty())
            .collect(Collectors.toList());

        indexDocs(paragraphs);
    }

    public void indexDocs(List<String> paragraphs) throws IOException {
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        try (IndexWriter writer = new IndexWriter(index, config)) {
            for (String para : paragraphs) {
                Document doc = new Document();
                doc.add(new TextField("content", para, Field.Store.YES));
                writer.addDocument(doc);
            }
        }
    }

    public String searchBestParagraph(String question) throws Exception {
        try (DirectoryReader reader = DirectoryReader.open(index)) {
            IndexSearcher searcher = new IndexSearcher(reader);
            QueryParser parser = new QueryParser("content", analyzer);
            Query query = parser.parse(QueryParser.escape(question));

            TopDocs results = searcher.search(query, 3);
            if (results.totalHits.value == 0)
                return null;

            StringBuilder context = new StringBuilder();
            for (ScoreDoc sd : results.scoreDocs) {
                Document doc = searcher.doc(sd.doc);
                context.append(doc.get("content")).append(" ");
            }
            String fullContext = context.toString().trim();
            return fullContext.length() > 1000 ? fullContext.substring(0, 1000) : fullContext;
        }
    }
}
