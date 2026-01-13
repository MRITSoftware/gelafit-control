Consider the following knowledge base for spring data elastic searcher upgrade 
1. add or upgrade dependency rg.elasticsearch.client:elasticsearch-rest-high-level-client 
    `org.elasticsearch.client:elasticsearch-rest-high-level-client:7.17.26`
2. org.springframework.data.elasticsearch.core.EntityMapper is deprecated, just remove the bean or class. 
3. ElasticsearchRepository.search with query builder is not supported any more, use ElasticsearchRepository.searchSimilar instead.  
    - e.g, repo.search(query) -> repo.searchSimilar(null, null, null)  