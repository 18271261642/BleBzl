package com.ble.blebzl.B18I.b18isupport;


import com.ble.blebzl.B18I.b18ibean.Axis;

/**
 * 描述：
 * </br>
 */
public interface Chart {

    void setAxisX(Axis axisX);

    void setAxisY(Axis axisY);

    Axis getAxisX();

    Axis getAxisY();
}
