package com.example.sample.web;

import com.example.sample.entity.Item;
import com.example.sample.service.ItemService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("pageTitle", "항목 목록");
        model.addAttribute("items", itemService.findAll());
        return "items/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("pageTitle", "항목 등록");
        model.addAttribute("item", new Item());
        return "items/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("item") Item item,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "items/form";
        }
        itemService.save(item);
        redirectAttributes.addFlashAttribute("message", "등록되었습니다.");
        return "redirect:/items";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Item> opt = itemService.findById(id);
        if (opt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "항목을 찾을 수 없습니다.");
            return "redirect:/items";
        }
        model.addAttribute("pageTitle", "항목 상세");
        model.addAttribute("item", opt.get());
        return "items/detail";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Item> opt = itemService.findById(id);
        if (opt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "항목을 찾을 수 없습니다.");
            return "redirect:/items";
        }
        model.addAttribute("pageTitle", "항목 수정");
        model.addAttribute("item", opt.get());
        return "items/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("item") Item item,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "items/form";
        }
        Optional<Item> existing = itemService.findById(id);
        if (existing.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "항목을 찾을 수 없습니다.");
            return "redirect:/items";
        }
        item.setId(id);
        itemService.save(item);
        redirectAttributes.addFlashAttribute("message", "수정되었습니다.");
        return "redirect:/items/" + id;
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        itemService.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "삭제되었습니다.");
        return "redirect:/items";
    }
}
