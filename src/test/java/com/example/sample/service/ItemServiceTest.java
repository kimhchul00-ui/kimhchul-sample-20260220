package com.example.sample.service;

import com.example.sample.entity.Item;
import com.example.sample.repository.ItemRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ItemService 테스트")
class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemService itemService;

    @Test
    @DisplayName("findAll - 목록 조회 시 저장된 순서의 역순으로 반환")
    void findAll() {
        Item item1 = new Item("A", "desc", 1);
        item1.setId(1L);
        Item item2 = new Item("B", "desc", 2);
        item2.setId(2L);
        when(itemRepository.findAllByOrderByIdDesc()).thenReturn(List.of(item2, item1));

        List<Item> result = itemService.findAll();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(2L);
        assertThat(result.get(1).getId()).isEqualTo(1L);
        verify(itemRepository).findAllByOrderByIdDesc();
    }

    @Test
    @DisplayName("findById - 존재하는 id면 Optional에 담아 반환")
    void findById_found() {
        Item item = new Item("테스트", "설명", 5);
        item.setId(1L);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        Optional<Item> result = itemService.findById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("테스트");
        assertThat(result.get().getQuantity()).isEqualTo(5);
        verify(itemRepository).findById(1L);
    }

    @Test
    @DisplayName("findById - 없는 id면 empty 반환")
    void findById_notFound() {
        when(itemRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<Item> result = itemService.findById(999L);

        assertThat(result).isEmpty();
        verify(itemRepository).findById(999L);
    }

    @Test
    @DisplayName("save - 새 항목 저장 시 id 부여된 엔티티 반환")
    void save_new() {
        Item input = new Item("새항목", "설명", 3);
        Item saved = new Item("새항목", "설명", 3);
        saved.setId(1L);
        when(itemRepository.save(any(Item.class))).thenReturn(saved);

        Item result = itemService.save(input);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("새항목");
        verify(itemRepository).save(input);
    }

    @Test
    @DisplayName("save - 기존 항목 수정")
    void save_update() {
        Item input = new Item("수정이름", "수정설명", 7);
        input.setId(1L);
        when(itemRepository.save(eq(input))).thenReturn(input);

        Item result = itemService.save(input);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("수정이름");
        verify(itemRepository).save(input);
    }

    @Test
    @DisplayName("deleteById - 지정 id 삭제 호출")
    void deleteById() {
        itemService.deleteById(1L);

        verify(itemRepository).deleteById(1L);
    }
}
