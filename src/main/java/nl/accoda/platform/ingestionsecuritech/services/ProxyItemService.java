package nl.accoda.platform.ingestionsecuritech.services;

import nl.accoda.platform.ingestionsecuritech.model.securitech.ProxyItem;
import nl.accoda.platform.ingestionsecuritech.repositories.ProxyItemRepository;
import org.springframework.stereotype.Service;

@Service
public class ProxyItemService {


    private final ProxyItemRepository proxyItemRepository;

    public ProxyItemService(ProxyItemRepository proxyItemRepository) {
        this.proxyItemRepository = proxyItemRepository;
    }

    public ProxyItem save(ProxyItem item) {
        ProxyItem proxyItem = proxyItemRepository.save(item);
        return proxyItem;
    }
}
