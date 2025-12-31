package jpabook.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jpabook.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

import static jpabook.domain.QMember.member;
import static jpabook.domain.QOrder.order;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public void save(Order order){
        em.persist(order);
    }

    public Order findOne(Long id){
        return em.find(Order.class, id);
    }


    // querydsl로 검색 처리
    public List<Order> findAll(OrderSearch orderSearch){

        return queryFactory
                .select(order)
                .from(order)
                .join(order.member, member)
                .where(statusEq(orderSearch.getOrderStatus()),
                        nameLike(orderSearch.getMemberName()))
                .limit(1000)
                .fetch();
    }

    private BooleanExpression statusEq(OrderStatus statusCond){
        if(statusCond == null) return null;
        return order.status.eq(statusCond);
    }

    private BooleanExpression nameLike(String nameCond){
        if(!StringUtils.hasText(nameCond)){
            return null;
        }
        return member.name.like(nameCond);
    }

}
