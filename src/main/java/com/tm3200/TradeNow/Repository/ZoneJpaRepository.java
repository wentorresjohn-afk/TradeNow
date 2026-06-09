package com.tm3200.TradeNow.Repository;

import com.tm3200.TradeNow.Model.PostsEntitys.Zone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface ZoneJpaRepository extends JpaRepository<Zone, Integer>
{
}
