package com.example.sample.config;

import com.example.sample.entity.Item;
import com.example.sample.repository.ItemRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    private final ItemRepository itemRepository;

    public DataLoader(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public void run(String... args) {
        if (itemRepository.count() > 0) {
            return;
        }
        List<Item> samples = List.of(
                new Item("노트북", "업무용 노트북", 3),
                new Item("마우스", "무선 마우스", 10),
                new Item("키보드", "기계식 키보드", 5),
                new Item("모니터", "27인치 모니터", 2),
                new Item("헤드셋", "블루투스 헤드셋", 4),
                new Item("USB 케이블", "C타입 케이블 1m", 8),
                new Item("노트패드", "A4 노트 5권 세트", 6),
                new Item("볼펜", "검정 볼펜 0.5mm", 9),
                new Item("스탠드", "모니터 암 스탠드", 1),
                new Item("책받침", "목각 책받침", 7)
        );
        itemRepository.saveAll(samples);
    }
}
