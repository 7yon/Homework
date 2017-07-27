package my.service;

import my.service.IServiceClient;

interface IService{
    void startCount(IServiceClient callback);
}