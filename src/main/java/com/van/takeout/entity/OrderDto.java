package com.van.takeout.entity;

import lombok.Data;

import java.util.List;

@Data
public class OrderDto extends Order {
    private static final long serialVersionUID = 751841185345674045L;

    private String userName;

    private String phone;

    private String address;

    private String consignee;

    private List<OrderDetail> orderDetails;
}
