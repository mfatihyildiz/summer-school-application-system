package com.sau.bitirme.controller;

import com.sau.bitirme.entity.Committee;
import com.sau.bitirme.repository.CommitteeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/committees")
public class CommitteeController {

    @Autowired
    private CommitteeRepo committeeRepo;

    @PostMapping
    public Committee createCommittee(final @RequestBody Committee committee) {
        return committeeRepo.save(committee);
    }

    @DeleteMapping("/{id}")
    public void deleteCommittee(final @PathVariable Long id) {
        committeeRepo.deleteById(id);
    }

}
