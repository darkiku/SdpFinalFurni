package com.example.Backend.Order;

import com.example.Backend.Cart.Cart;
import com.example.Backend.User.User;
import com.example.Backend.visitor.OrderVisitor;
import com.example.Backend.visitor.Visitable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders")
public class Order implements Visitable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "userId")
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Cart> items;

    @Column(nullable = false)
    private Double totalPrice;

    @Column(nullable = false)
    private String status;

    @Column
    private String orderType;

    @Column
    private String deliveryMethod;

    @Column
    private LocalDateTime createdAt;

    @Override
    public String accept(OrderVisitor visitor) {
        return visitor.export(this);
    }
}
