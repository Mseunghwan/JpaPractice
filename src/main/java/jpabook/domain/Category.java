package jpabook.domain;

import jakarta.persistence.*;
import jpabook.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Category {

    @Id @GeneratedValue
    @Column(name="cateogry_id")
    private Long id;

    private String name;

    // @ManyToMany는 실무에서는 사용 자제,
    // 중간 테이블에 칼럼을 추가할 수 없고 세밀한 쿼리 불가하기에 한계가 있음.
    // 중간 엔터티를 만들고 ManyToOne, OneToMany로 매핑해 사용해야
    @ManyToMany
    @JoinTable(name = "cateogry_item",
               joinColumns = @JoinColumn(name="cateogory_id"),
               inverseJoinColumns = @JoinColumn(name = "item_id"))
    private List<Item> items = new ArrayList<>();

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="parent_id")
    private Category parent;

    @OneToMany(mappedBy="parent")
    private List<Category> child = new ArrayList<>();

    //== 연관관계 메서드 ==//
    public void addChildCategory(Category child){
        this.child.add(child);
        this.setParent(this);
    }

}
