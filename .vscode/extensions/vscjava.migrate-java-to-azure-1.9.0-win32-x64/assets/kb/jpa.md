Consider the following knowledge base for jpa repository upgrade: 
1. findOne is deprecated, use findById instead. e.g. repo.findOne(id) -> repo.findById(id).orElse(null) 
2. delete is deprecated, use deleteById instead. e.g. repo.delete(id) -> repo.deleteById(id) 