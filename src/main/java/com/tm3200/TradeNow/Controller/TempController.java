package com.tm3200.TradeNow.Controller;

import com.tm3200.TradeNow.Model.PostsEntitys.Category;
import com.tm3200.TradeNow.Model.PostsEntitys.Zone;
import com.tm3200.TradeNow.Repository.CategoryJpaRepository;
import com.tm3200.TradeNow.Repository.ZoneJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/temp")
public class TempController
{
    @Autowired
    private CategoryJpaRepository categoryJpaRepository;

    @Autowired
    private ZoneJpaRepository zoneJpaRepository;

    @PostMapping("/categoria")
    public Category addCategory(@RequestBody Category category) {
        return categoryJpaRepository.save(category);
    }

    @PostMapping("/zona")
    public Zone addZone(@RequestBody Zone zone) {
        return zoneJpaRepository.save(zone);
    }
}
