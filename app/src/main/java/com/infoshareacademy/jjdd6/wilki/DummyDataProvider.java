package com.infoshareacademy.jjdd6.wilki;


import javax.ejb.Stateless;
import java.awt.*;

@Stateless
public class DummyDataProvider {

    public Point getData() {
        return new Point(2,3);
    }
}
