package com.github.soyanga;


/**
 * @program: springcore-case-IoC
 * @Description:
 * @Author: SOYANGA
 * @Create: 2019-04-10 18:52
 * @Version 1.0
 */
public class ClientService {

    private static final ClientService cientService = new ClientService();

    public ClientService() {

    }

    public static ClientService getInstance() {
        return cientService;
    }
}
