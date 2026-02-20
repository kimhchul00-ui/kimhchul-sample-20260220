package com.example.sample.service;

import com.example.sample.entity.Item;
import com.example.sample.repository.ItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ItemService {

    private static final Logger log = LoggerFactory.getLogger(ItemService.class);

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Transactional(readOnly = true)
    public List<Item> findAll() {
        log.info("[Step] Item 목록 조회 요청");
        List<Item> list = itemRepository.findAllByOrderByIdDesc();
        log.info("[Step] Item 목록 조회 완료 (size={})", list.size());
        return list;
    }

    @Transactional(readOnly = true)
    public Optional<Item> findById(Long id) {
        log.info("[Step] Item 조회 요청 (id={})", id);
        Optional<Item> result = itemRepository.findById(id);
        log.info("[Step] Item 조회 완료 (id={}, found={})", id, result.isPresent());
        return result;
    }

    @Transactional
    public Item save(Item item) {
        boolean isNew = item.getId() == null;
        log.info("[Step] Item 저장 요청 (id={}, name={}, isNew={})", item.getId(), item.getName(), isNew);
        Item saved = itemRepository.save(item);
        log.info("[Step] Item 저장 완료 (id={})", saved.getId());
        return saved;
    }

    @Transactional
    public void deleteById(Long id) {
        log.info("[Step] Item 삭제 요청 (id={})", id);
        itemRepository.deleteById(id);
        log.info("[Step] Item 삭제 완료 (id={})", id);
    }
}
