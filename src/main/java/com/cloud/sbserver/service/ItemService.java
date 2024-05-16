package com.cloud.sbserver.service;

import com.cloud.sbserver.dto.ItemRequestDto;
import com.cloud.sbserver.entity.Item;
import com.cloud.sbserver.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    // Sharing Board item 등록
    public void write(ItemRequestDto item) {
        itemRepository.save(
                Item.builder()
                        .productName(item.getProductName())
                        .price(item.getPrice())
                        .url(item.getUrl())
                        .nickname(item.getNickname())
                        .user_id(item.getUser_id())
                        .createdTime(LocalDateTime.now())
                        .build()
        );
    }

    // Sharing Board 리스트 조회
    public Page<Item> itemList(Pageable pageable) {
        return itemRepository.findAll(pageable);
    }

    public Page<Item> itemSearchList(String searchKeyword, Pageable pageable) {
        return itemRepository.findByProductNameContaining(searchKeyword, pageable);
    }

    // Sharing Board item 불러오기
    public Item itemView(Integer id) {
        return itemRepository.findById(id).orElse(null);
    }

    // 특정 Sharing Board item 삭제
    public void itemDelete(Integer id) {
        itemRepository.deleteById(id);
    }

    // Sharing Board item 업데이트
    public void itemUpdate(Item item) {
        Item existingItem = itemRepository.findById(item.getId()).orElse(null);
        if (existingItem != null) {
            existingItem.setNickname(item.getNickname());
            existingItem.setPrice(item.getPrice());
            existingItem.setProductName(item.getProductName());
            itemRepository.save(existingItem);
        }
    }
}