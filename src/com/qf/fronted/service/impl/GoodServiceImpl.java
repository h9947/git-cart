package com.qf.fronted.service.impl;

import com.qf.fronted.dao.GoodDao;
import com.qf.fronted.dao.impl.GoodDaoImpl;
import com.qf.fronted.domain.Goods;
import com.qf.fronted.service.GoodService;

import java.util.List;

public class GoodServiceImpl implements GoodService {

    // 图片服务器的地址
    private final String imageServerLocation = "http://localhost/";

    private GoodDao goodDao = new GoodDaoImpl();

    @Override
    public List<Goods> getAll() {
        List<Goods> list = goodDao.getAll();
        // 是将图片设置为可直接访问的地址
        list.forEach(g -> {
            String imageSrc = g.getImageSrc(); // mobile/huawei.jpg
            g.setImageSrc(imageServerLocation + imageSrc);
        });
        return list;
    }

    @Override
    public List<Goods> getGoodsByIds(Integer[] ids) {
        List<Goods> list = goodDao.getGoodsByIds(ids);
        // 是将图片设置为可直接访问的地址
        list.forEach(g -> {
            String imageSrc = g.getImageSrc(); // mobile/huawei.jpg
            g.setImageSrc(imageServerLocation + imageSrc);
        });

        return list;
    }
}
