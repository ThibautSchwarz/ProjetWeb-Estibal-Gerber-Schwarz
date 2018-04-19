/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

import java.sql.Date;

/**
 *
 * @author MrsFrozen
 */
public class PurchaseOrder {

    private  int ordernum;
    private int quantity;
    private float cost;
    private Date salesDate;
    private Date shippingDate;
    private String Company;
    private int cusId;
        private int productId;

        public PurchaseOrder(int ordernum, int cusId, int productId, int quantity, float cost, Date salesDate, Date shippingDate, String Company) {
        this.ordernum = ordernum;
        this.quantity = quantity;
        this.cost = cost;
        this.salesDate = salesDate;
        this.shippingDate = shippingDate;
        this.Company = Company;
        this.cusId = cusId;
        this.productId = productId;
    }
        
    public Date getSalesDate() {
        return salesDate;
    }

    public Date getShippingDate() {
        return shippingDate;
    }

    public String getCompany() {
        return Company;
    }
    public int getCusId() {
        return cusId;
    }

    public int getProductId() {
        return productId;
    }

    public int getOrdernum() {
        return ordernum;
    }

    public int getQuantity() {
        return quantity;
    }

    public float getCost() {
        return cost;
    }

}
