package jpabook.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jpabook.domain.Address;
import jpabook.domain.Member;
import jpabook.domain.Order;
import jpabook.domain.OrderStatus;
import jpabook.domain.exception.NotEnoughStockException;
import jpabook.domain.item.Book;
import jpabook.domain.item.Item;
import jpabook.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class OrderServiceTest {

    @PersistenceContext
    EntityManager em;

    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;

    @Test
    public void 상품주문() throws Exception {

        // given
        Member member = createMember();

        Book book = createBook("JPA", 1000, 10);

        int orderPrice = 1000;
        int orderCount = 2;

        // when
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        // then
        Order getOrder = orderRepository.findOne(orderId);
        assertEquals(OrderStatus.ORDER, getOrder.getStatus(), "상품 주문시 상태는 ORDER");
        assertEquals(1, getOrder.getOrderItems().size(), "주문한 상품 종류 수가 정확해야 한다.");
        assertEquals(orderPrice * orderCount, getOrder.getTotalPrice(), "주문 가격은 가격*수량");
        assertEquals(8, book.getStockQuantity(), "주문 수량만큼 재고가 줄어야 한다.");

    }

    @Test
    public void 상품주문_재고수량초과()throws Exception{
        // given
        Member member = createMember();
        Item item = createBook("JPA", 1000, 10);

        int orderCnt = 11;

        // when

        // then

        assertThrows(NotEnoughStockException.class, () -> {
            orderService.order(member.getId(), item.getId(), orderCnt);
        });

    }

    @Test
    public void 주문취소() throws Exception{

        // given
        Member member = createMember();
        Item item = createBook("JPA", 1000, 10);

        int orderCnt = 2;
        Long OrderId = orderService.order(member.getId(), item.getId(), orderCnt);

        // when
        orderService.cancelOrder(OrderId);

        // then
        Order getOrder = orderRepository.findOne(OrderId);
        assertEquals(OrderStatus.CANCEL, getOrder.getStatus(), "주문 상태 CANCEL");
        assertEquals(10, item.getStockQuantity(), "재고 수량 확인");

    }

    private Member createMember() {
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울", "경기", "123-123"));
        em.persist(member);
        return member;
    }

    private Book createBook(String name, int price, int stockQuantity){
        Book book = new Book();
        book.setName(name);
        book.setStockQuantity(stockQuantity);
        book.setPrice(price);
        em.persist(book);
        return book;
    }


}
