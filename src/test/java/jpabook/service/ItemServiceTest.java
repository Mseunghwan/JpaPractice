package jpabook.service;

import jpabook.domain.item.Album;
import jpabook.domain.item.Item;
import jpabook.repository.ItemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class ItemServiceTest {

    @Autowired
    ItemService itemService;

    @Autowired
    ItemRepository itemRepository;

    @Test
    public void 상품추가(){

        // given
        Album album = new Album();
        album.setArtist("Kim");
        album.setName("Serna");
        album.setEtc("what");
        album.setPrice(1000);
        album.setStockQuantity(100);


        // when
        itemService.saveItem(album);

        // then
        assertEquals(album, itemService.findOne(album.getId()));

    }


}
