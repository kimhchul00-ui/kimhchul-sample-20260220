package com.example.sample.web;

import com.example.sample.entity.Item;
import com.example.sample.service.ItemService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemController.class)
@DisplayName("ItemController 테스트")
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemService itemService;

    @Test
    @DisplayName("GET /items - 목록 화면")
    void list() throws Exception {
        Item item = new Item("테스트", "설명", 1);
        item.setId(1L);
        when(itemService.findAll()).thenReturn(List.of(item));

        mockMvc.perform(get("/items"))
                .andExpect(status().isOk())
                .andExpect(view().name("items/list"))
                .andExpect(model().attributeExists("items", "pageTitle"))
                .andExpect(model().attribute("items", hasSize(1)));

        verify(itemService).findAll();
    }

    @Test
    @DisplayName("GET /items/new - 등록 폼")
    void createForm() throws Exception {
        mockMvc.perform(get("/items/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("items/form"))
                .andExpect(model().attributeExists("item", "pageTitle"));
    }

    @Test
    @DisplayName("POST /items - 등록 성공 시 목록으로 리다이렉트")
    void create_success() throws Exception {
        when(itemService.save(any(Item.class))).thenAnswer(inv -> {
            Item i = inv.getArgument(0);
            i.setId(1L);
            return i;
        });

        mockMvc.perform(post("/items")
                        .param("name", "새항목")
                        .param("description", "설명")
                        .param("quantity", "5"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/items"))
                .andExpect(flash().attribute("message", "등록되었습니다."));

        verify(itemService).save(argThat(item ->
                "새항목".equals(item.getName()) && item.getQuantity() == 5));
    }

    @Test
    @DisplayName("POST /items - 검증 실패 시 폼 다시 표시")
    void create_validationError() throws Exception {
        mockMvc.perform(post("/items")
                        .param("name", "")
                        .param("quantity", "0"))
                .andExpect(status().isOk())
                .andExpect(view().name("items/form"));

        verify(itemService, never()).save(any());
    }

    @Test
    @DisplayName("GET /items/{id} - 상세 화면 (존재)")
    void detail_found() throws Exception {
        Item item = new Item("상세항목", "설명", 3);
        item.setId(1L);
        when(itemService.findById(1L)).thenReturn(Optional.of(item));

        mockMvc.perform(get("/items/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("items/detail"))
                .andExpect(model().attribute("item", allOf(
                        hasProperty("id", equalTo(1L)),
                        hasProperty("name", equalTo("상세항목")),
                        hasProperty("quantity", equalTo(3))
                )));

        verify(itemService).findById(1L);
    }

    @Test
    @DisplayName("GET /items/{id} - 상세 화면 (없음) 목록으로 리다이렉트")
    void detail_notFound() throws Exception {
        when(itemService.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/items/999"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/items"))
                .andExpect(flash().attribute("error", "항목을 찾을 수 없습니다."));

        verify(itemService).findById(999L);
    }

    @Test
    @DisplayName("GET /items/{id}/edit - 수정 폼 (존재)")
    void editForm_found() throws Exception {
        Item item = new Item("수정대상", "설명", 2);
        item.setId(1L);
        when(itemService.findById(1L)).thenReturn(Optional.of(item));

        mockMvc.perform(get("/items/1/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("items/form"))
                .andExpect(model().attribute("item", allOf(
                        hasProperty("id", equalTo(1L)),
                        hasProperty("name", equalTo("수정대상")),
                        hasProperty("quantity", equalTo(2))
                )));

        verify(itemService).findById(1L);
    }

    @Test
    @DisplayName("POST /items/{id} - 수정 성공 시 상세로 리다이렉트")
    void update_success() throws Exception {
        Item existing = new Item("기존", "설명", 1);
        existing.setId(1L);
        when(itemService.findById(1L)).thenReturn(Optional.of(existing));
        when(itemService.save(any(Item.class))).thenAnswer(inv -> inv.getArgument(0));

        mockMvc.perform(post("/items/1")
                        .param("name", "수정이름")
                        .param("description", "수정설명")
                        .param("quantity", "8"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/items/1"))
                .andExpect(flash().attribute("message", "수정되었습니다."));

        verify(itemService).save(argThat(item ->
                item.getId() == 1L && "수정이름".equals(item.getName()) && item.getQuantity() == 8));
    }

    @Test
    @DisplayName("POST /items/{id}/delete - 삭제 후 목록으로 리다이렉트")
    void delete() throws Exception {
        mockMvc.perform(post("/items/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/items"))
                .andExpect(flash().attribute("message", "삭제되었습니다."));

        verify(itemService).deleteById(1L);
    }
}
