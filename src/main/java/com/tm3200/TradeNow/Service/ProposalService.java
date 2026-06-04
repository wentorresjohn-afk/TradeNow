package com.tm3200.TradeNow.Service;

import com.tm3200.TradeNow.Model.Proposal;
import com.tm3200.TradeNow.Model.User;
import com.tm3200.TradeNow.Repository.ProposalJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProposalService {
    @Autowired
    private ProposalJpaRepository repository;

    @Autowired
    private UserService userService;

    //(aquí tengo que conectar con el service de Posts)


}
