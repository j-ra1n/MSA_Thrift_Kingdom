package com.cloud.sbserver.controller;

import com.cloud.sbserver.dto.ItemRequestDto;
import com.cloud.sbserver.entity.Item;
import com.cloud.sbserver.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/item")
public class ItemController {

    @Autowired
    private ItemService itemService;

    // Sharing Board Item 등록
    @PostMapping("/")
    public String itemWrite(@RequestBody ItemRequestDto item) {
        itemService.write(item);
        return "[Sharing Board] Writing is complete";
    }

    // Sharing Board Item 리스트 목록
    @GetMapping("/list")
    public Page<Item> itemList(@PageableDefault(page = 0, size = 10, sort = "id",
            direction = Sort.Direction.DESC) Pageable pageable,
                                @RequestParam(name = "searchKeyword", required = false) String searchKeyword) {
        if (searchKeyword == null) {
            return itemService.itemList(pageable);
        } else {
            return itemService.itemSearchList(searchKeyword, pageable);
        }
    }

    // Sharing Board item 조회
    @GetMapping("/{id}")
    public Item itemView(@PathVariable("id") Integer id) {
        return itemService.itemView(id);
    }

    // Sharing Board item 삭제
    @DeleteMapping("/{id}")
    public String itemDelete(@PathVariable("id") Integer id) {
        itemService.itemDelete(id);
        return "[Sharing Board] Deleted complete";
    }

    // Bulletin Board item 수정
    @PutMapping("/{id}")
    public String itemUpdate(@PathVariable("id") Integer id, @RequestBody Item item) {
        Item itemTemp = itemService.itemView(id);
        itemTemp.setNickname(item.getNickname());
        itemTemp.setPrice(item.getPrice());
        itemTemp.setProductName(item.getProductName());
        itemService.itemUpdate(itemTemp);
        return "[Sharing Board] post has been updated";
    }
}
