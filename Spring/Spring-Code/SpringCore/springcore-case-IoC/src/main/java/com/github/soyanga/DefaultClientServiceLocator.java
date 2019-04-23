package com.github.soyanga;

/**
 * @program: springcore-case-IoC
 * @Description:
 * @Author: SOYANGA
 * @Create: 2019-04-10 19:12
 * @Version 1.0
 */
public class DefaultClientServiceLocator {
//    private static ClientService clientService = new ClientService();

    public ClientService getClientService() {
        return ClientService.getInstance();
    }
}
