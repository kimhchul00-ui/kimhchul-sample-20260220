package com.example.sample.repository;

import com.example.sample.entity.Item;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayName("ItemRepository 테스트")
class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Test
    @DisplayName("save - 저장 후 id 부여")
    void save() {
        Item item = new Item("저장테스트", "설명", 5);

        Item saved = itemRepository.save(item);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("저장테스트");
    }

    @Test
    @DisplayName("findAllByOrderByIdDesc - id 내림차순 정렬")
    void findAllByOrderByIdDesc() {
        itemRepository.save(new Item("첫번째", null, 1));
        itemRepository.save(new Item("두번째", null, 2));
        itemRepository.save(new Item("세번째", null, 3));

        List<Item> list = itemRepository.findAllByOrderByIdDesc();

        assertThat(list).hasSize(3);
        assertThat(list.get(0).getName()).isEqualTo("세번째");
        assertThat(list.get(1).getName()).isEqualTo("두번째");
        assertThat(list.get(2).getName()).isEqualTo("첫번째");
    }

    @Test
    @DisplayName("findById - 존재하는 경우")
    void findById_found() {
        Item saved = itemRepository.save(new Item("조회대상", "설명", 7));

        Optional<Item> result = itemRepository.findById(saved.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("조회대상");
        assertThat(result.get().getQuantity()).isEqualTo(7);
    }

    @Test
    @DisplayName("findById - 없는 id")
    void findById_notFound() {
        Optional<Item> result = itemRepository.findById(999L);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("deleteById - 삭제 후 조회 시 empty")
    void deleteById() {
        Item saved = itemRepository.save(new Item("삭제대상", null, 1));
        Long id = saved.getId();

        itemRepository.deleteById(id);

        assertThat(itemRepository.findById(id)).isEmpty();
    }
}
