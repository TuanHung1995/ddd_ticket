package com.ddd.ddd.infrastructure.persistence.repository;

import com.ddd.ddd.domain.repository.HiDomainRepository;
import org.springframework.stereotype.Service;

@Service
public class HiInfrasRepository implements HiDomainRepository {

    @Override
    public String sayHi(String who) {
        return "Hi in infrastructure, " + who + "!";
    }
}
