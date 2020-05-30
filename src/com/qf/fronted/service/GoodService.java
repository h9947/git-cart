package com.qf.fronted.service;

import com.qf.fronted.domain.Goods;
import java.util.List;

public interface GoodService {

    List<Goods> getAll();

    List<Goods> getGoodsByIds(Integer[] ids);
}
