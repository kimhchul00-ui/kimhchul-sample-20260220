package com.example.sample.web;

import com.example.sample.entity.Item;
import com.example.sample.service.ItemService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/items")
public class ItemController {

    private static final Logger log = LoggerFactory.getLogger(ItemController.class);

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    public String list(Model model) {
        log.info("[Step] 항목 목록 화면 요청");
        model.addAttribute("pageTitle", "항목 목록");
        model.addAttribute("items", itemService.findAll());
        log.info("[Step] 항목 목록 화면 렌더링 (items/list)");
        return "items/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        log.info("[Step] 항목 등록 폼 화면 요청");
        model.addAttribute("pageTitle", "항목 등록");
        model.addAttribute("item", new Item());
        log.info("[Step] 항목 등록 폼 렌더링 (items/form)");
        return "items/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("item") Item item,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {
        log.info("[Step] 항목 등록 요청 (name={})", item.getName());
        if (bindingResult.hasErrors()) {
            log.warn("[Step] 항목 등록 검증 실패 (errors={})", bindingResult.getAllErrors());
            return "items/form";
        }
        itemService.save(item);
        redirectAttributes.addFlashAttribute("message", "등록되었습니다.");
        log.info("[Step] 항목 등록 완료, 목록으로 리다이렉트 (id={})", item.getId());
        return "redirect:/items";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        log.info("[Step] 항목 상세 화면 요청 (id={})", id);
        Optional<Item> opt = itemService.findById(id);
        if (opt.isEmpty()) {
            log.warn("[Step] 항목 없음 (id={}), 목록으로 리다이렉트", id);
            redirectAttributes.addFlashAttribute("error", "항목을 찾을 수 없습니다.");
            return "redirect:/items";
        }
        model.addAttribute("pageTitle", "항목 상세");
        model.addAttribute("item", opt.get());
        log.info("[Step] 항목 상세 화면 렌더링 (id={}, name={})", id, opt.get().getName());
        return "items/detail";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        log.info("[Step] 항목 수정 폼 화면 요청 (id={})", id);
        Optional<Item> opt = itemService.findById(id);
        if (opt.isEmpty()) {
            log.warn("[Step] 항목 없음 (id={}), 목록으로 리다이렉트", id);
            redirectAttributes.addFlashAttribute("error", "항목을 찾을 수 없습니다.");
            return "redirect:/items";
        }
        model.addAttribute("pageTitle", "항목 수정");
        model.addAttribute("item", opt.get());
        log.info("[Step] 항목 수정 폼 렌더링 (id={})", id);
        return "items/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("item") Item item,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {
        log.info("[Step] 항목 수정 요청 (id={}, name={})", id, item.getName());
        if (bindingResult.hasErrors()) {
            log.warn("[Step] 항목 수정 검증 실패 (id={}, errors={})", id, bindingResult.getAllErrors());
            return "items/form";
        }
        Optional<Item> existing = itemService.findById(id);
        if (existing.isEmpty()) {
            log.warn("[Step] 수정 대상 항목 없음 (id={})", id);
            redirectAttributes.addFlashAttribute("error", "항목을 찾을 수 없습니다.");
            return "redirect:/items";
        }
        item.setId(id);
        itemService.save(item);
        redirectAttributes.addFlashAttribute("message", "수정되었습니다.");
        log.info("[Step] 항목 수정 완료, 상세로 리다이렉트 (id={})", id);
        return "redirect:/items/" + id;
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        log.info("[Step] 항목 삭제 요청 (id={})", id);
        itemService.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "삭제되었습니다.");
        log.info("[Step] 항목 삭제 완료, 목록으로 리다이렉트 (id={})", id);
        return "redirect:/items";
    }
}
