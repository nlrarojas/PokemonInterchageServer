package main;

import controller.Server;
import util.IConstants;

/**
 *
 * @author Nelson
 */
public class Program implements IConstants{
    public static void main(String[] args) {        
        new Server().start();
    }
}
